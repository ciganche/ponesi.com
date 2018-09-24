package services;

import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import beans.Admin;
import beans.Artikal;
import beans.DeliveryGuy;
import beans.HistoryOrder;
import beans.Order;
import beans.Restoran;
import beans.TempOrder;
import beans.TempOrder2;
import beans.User;
import beans.Vozilo;
import dao.ArtikalDAO;
import dao.OrderDAO;
import dao.RestoranDAO;
import dao.UserDAO;
import dao.VoziloDAO;

@Path("orders")
public class OrderService 
{
	@Context
	ServletContext ctx;
	
	@PostConstruct
	public void init() 
	{
		if(ctx.getAttribute("orderDAO") == null)
		{
			
			System.out.println("******************adaj0");
	    	String contextPath = ctx.getRealPath("");
			
	    	OrderDAO orderDAO = new OrderDAO(contextPath);
	    	
	    	ctx.setAttribute("orderDAO", orderDAO);
		}
	}
	
	@GET
	@Path("/check/{tokens}")
	public void checkTokens(@PathParam("tokens") String tokens_str, @Context HttpServletRequest request)
	{
		
		  OrderDAO dao = (OrderDAO) ctx.getAttribute("orderDAO");
		  User user = null;
		  user =  (User)request.getSession().getAttribute("user");
		  if(user!=null)
		  {
			  int tokens = 0;
			  try
			  {
				  tokens = Integer.parseInt(tokens_str);
			  }
			  catch(Exception e)
			  {
				  return;
			  }
			  dao.getUserToken().put(user.getId(), tokens);
			  			  
			  ctx.setAttribute("orderDAO", dao);

		  }
	}
	
	
	
	//TODO: order prima samo listu 
	@POST
	@Path("/take")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String takeOrder( TempOrder carrier,  @Context HttpServletRequest request)
	{
	
		  OrderDAO dao = (OrderDAO) ctx.getAttribute("orderDAO");
		
		  User user = null;
		  user =  (User)request.getSession().getAttribute("user");	
		  
		  if(user!=null)
		  {
			  int users_selected_tokens = 0;
			  
			  if(dao.getUserToken().containsKey(user.getId()))
			  {

				  users_selected_tokens = dao.getUserToken().get(user.getId());
				  
				  System.out.println("Uneo tokena: " + users_selected_tokens);
				  System.out.println("Poseduje tokena: " + user.getTokens());
				   
				  
				  //ako je pokusao da u jQuery-ju unese veci broj tokena nego sto ima 
				  if(users_selected_tokens > user.getTokens())
					  return null;
				  
				  user.setTokens(user.getTokens() - users_selected_tokens);
				  
			  }
			  else
			  {
				  return null;
			  }
			  
			  if(carrier!=null)
			  {

				  
				  ArtikalDAO artikalDAO = (ArtikalDAO) ctx.getAttribute("artikalDAO");
				  artikalDAO.setPopolarity(carrier.getArtikli(), ctx.getRealPath(""));
				  ctx.setAttribute("artikalDAO", artikalDAO);
				  
				  double total_cena = 0;
				  
				  Order order = new Order(carrier.getArtikli());
				  
				  order.setId(dao.gimmieNextOrderId());
				  
				  //za popunjavanje istrije narudzbina korisnika
				  List<String> orderInfos = new ArrayList<String>();
					 
				  System.out.println("Svi artikli sa njihovim kolicinama: ");
				  for( int i = 0 ; i < carrier.getArtikli().size() ; i ++ )
				  {
					  System.out.println(carrier.getArtikli().get(i).getIme() + " kolicina: " + carrier.getArtikli().get(i).getKolicina());
					  total_cena += carrier.getArtikli().get(i).getCena() * (double)carrier.getArtikli().get(i).getKolicina();
				  
					  orderInfos.add(carrier.getArtikli().get(i).getKolicina() + " x " + carrier.getArtikli().get(i).getIme() + " from " + carrier.getArtikli().get(i).restoran_maticni);
				  }
				  
				  total_cena = total_cena - ((double)users_selected_tokens * 0.03 * total_cena);  
				  order.setTotal_cena(total_cena);
				  order.setUser(user.getId());

				  order.setStanje(0);
				  order.setVisible(1);
				  order.setNapomena(carrier.getNapomena());
				  
				  LocalDateTime date = LocalDateTime.now();
				  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
				  String date_str = date.format(formatter);
				  order.setDateTime_order(date_str);
				  System.out.println("date_str : " + date_str);


				  order.setDostavljac_id(-1);

				  
				  order.setUsed_tokens(users_selected_tokens);
				  
				  
				  /*
				   * 
				   ********************DODATI ORDERU NAPOMENU  
				   * 
				   */
				  
				  
				  //od ovog ubacivanja se gleda kao narucena;
				  dao.getOrders().put(dao.gimmieNextOrderId(), order);
				  dao.addOrder(order, ctx.getRealPath(""));
				  //UPISI ORDER U FAJL******
				  
				  ctx.setAttribute("orderDAO", dao);
				  
				  
				  String dejt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
				  
				  
				  HistoryOrder historyItem = new HistoryOrder(dejt, total_cena, orderInfos);
				  

				  user.getHistory().add(0, historyItem);
				  
				  
				  //UPISIVANJE
				  
				  request.getSession().setAttribute("user", user);
				   
				  UserDAO userDAO = (UserDAO) ctx.getAttribute("userDAO");
				  userDAO.modifyUser(user, ctx.getRealPath(""));
				  ctx.setAttribute("userDAO", userDAO);

				  
				  return "nenulnesto";
			  }
			  else
			  {
				  return null;
			  }
			  

		  }
		  else
		  {
			  return null;
		  }
	}
	
	
	@PUT
	@Path("/deleteBasket")
	public void deleteBasketItems(@Context HttpServletRequest request)
	{
		  User user = null;
		  user =  (User)request.getSession().getAttribute("user");

		  if(user!=null)
		  {
			  user.getKorpa().clear();
			  
			  request.getSession().setAttribute("user",user);
		  }  
	}
	
	
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Order> getProducts(@Context HttpServletRequest request) 
	{		
		OrderDAO dao = (OrderDAO) ctx.getAttribute("orderDAO");
		return dao.findAll();
	}
	
	
	@PUT
	@Path("/InsertToDeliveryGuysOrders/{id}")
	public void insertOrderId(@PathParam("id") String id_str, @Context HttpServletRequest request)
	{
				
		DeliveryGuy deliveryGuy = (DeliveryGuy) request.getSession().getAttribute("deliveryGuy");
	
		if(deliveryGuy == null)
			return;
		
		try
		{
			int id = Integer.parseInt(id_str);
			
			deliveryGuy.getOrder_id().add(id);
			
			request.getSession().setAttribute("deliveryGuy", deliveryGuy);
			
			UserDAO userDAO = (UserDAO) ctx.getAttribute("userDAO");
			userDAO.modifyDeliveryGuy(deliveryGuy, ctx.getRealPath(""));
			ctx.setAttribute("userDAO",userDAO);
			
			/*
			 * 
			 * ************
			 * promena ordera u fajlu
			 * 
			 * promena u hashmapi
			 * 
			 * 
			 * */
			
			//PROMENA ORDER FAJLA
			OrderDAO orderDAO = (OrderDAO) ctx.getAttribute("orderDAO");
			Order order_to_be_changed = orderDAO.findOrder(id);
			
			order_to_be_changed.setDostavljac_id(deliveryGuy.getId());
			order_to_be_changed.setStanje(1);
			
			System.out.println("Pozivam modifikaciju");
			orderDAO.modifyOrder(order_to_be_changed, ctx.getRealPath(""));
			ctx.setAttribute("orderDAO",orderDAO);
		}
		catch(Exception e)
		{
			return;
		}
		
		
	}
	
	@PUT
	@Path("/MarkAsDelivered/{id}")
	public void markAsDelivered(@PathParam("id") String id_str, @Context HttpServletRequest request)
	{
		DeliveryGuy deliveryGuy = (DeliveryGuy) request.getSession().getAttribute("deliveryGuy");
		
		if(deliveryGuy == null)
			return;
		
		try
		{
			int id = Integer.parseInt(id_str);
			
			for( int i = 0 ; i < deliveryGuy.getOrder_id().size() ; i ++ )
			{
				if(deliveryGuy.getOrder_id().get(i) == id)
				{
					
					System.out.println("Obelezeno kao dostavljeno: " + deliveryGuy.getOrder_id().get(i));
					
					deliveryGuy.getOrder_id().remove(i);
					
					break;
				}
			}
			
			
			request.getSession().setAttribute("deliveryGuy", deliveryGuy);
			
			UserDAO userDAO = (UserDAO) ctx.getAttribute("userDAO");
			userDAO.modifyDeliveryGuy(deliveryGuy, ctx.getRealPath(""));
			ctx.setAttribute("userDAO",userDAO);
			
			
			
			
			//PROMENA ORDER FAJLA
			OrderDAO orderDAO = (OrderDAO) ctx.getAttribute("orderDAO");
			Order order_to_be_changed = orderDAO.findOrder(id);
			
			order_to_be_changed.setDostavljac_id(-1);
			order_to_be_changed.setStanje(2);
			
			System.out.println("Pozivam modifikaciju");
			orderDAO.modifyOrder(order_to_be_changed, ctx.getRealPath(""));
			ctx.setAttribute("orderDAO",orderDAO);
			
			
			if(order_to_be_changed.total_cena >= 500)
			{
				User user = userDAO.findUserById(order_to_be_changed.getUser());
				
				int current_tokens = user.getTokens() + 1;
				if(current_tokens >= 10)
					user.setTokens(10);
				else
					user.setTokens(current_tokens);
				
				userDAO.modifyUser(user, ctx.getRealPath(""));
				
			}
			
		}
		
		
		
		
		
		catch(Exception e)
		{
			return;
		}
	}
	
	
	@PUT
	@Path("/MarkAsCancled/{id}")
	public void markAsCancled(@PathParam("id") String id_str, @Context HttpServletRequest request)
	{
		DeliveryGuy deliveryGuy = (DeliveryGuy) request.getSession().getAttribute("deliveryGuy");
		
		if(deliveryGuy == null)
			return;
		
		try
		{
			int id = Integer.parseInt(id_str);
			
			for( int i = 0 ; i < deliveryGuy.getOrder_id().size() ; i ++ )
			{
				if(deliveryGuy.getOrder_id().get(i) == id)
				{
					System.out.println("Obelezeno kao otkazano: " + deliveryGuy.getOrder_id().get(i));
					
					deliveryGuy.getOrder_id().remove(i);
					
					break;
					
				}
			}
			
			
			request.getSession().setAttribute("deliveryGuy", deliveryGuy);
			
			UserDAO userDAO = (UserDAO) ctx.getAttribute("userDAO");
			userDAO.modifyDeliveryGuy(deliveryGuy, ctx.getRealPath(""));
			ctx.setAttribute("userDAO",userDAO);
			
			
			
			
			//PROMENA ORDER FAJLA
			OrderDAO orderDAO = (OrderDAO) ctx.getAttribute("orderDAO");
			Order order_to_be_changed = orderDAO.findOrder(id);
			
			order_to_be_changed.setDostavljac_id(-1);
			order_to_be_changed.setStanje(3); //3 kao otkazano
			
			System.out.println("Pozivam modifikaciju");
			orderDAO.modifyOrder(order_to_be_changed, ctx.getRealPath(""));
			ctx.setAttribute("orderDAO",orderDAO);
			
			
		}
		
		
		
		
		
		catch(Exception e)
		{
			return;
		}
	}
	
	
	
	
	
	@PUT
	@Path("/modify")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Order editOrder(TempOrder2 arrived, @Context HttpServletRequest request)
	{
		Admin admin = (Admin) request.getSession().getAttribute("admin");
		if(admin == null)
			return null;
		
		if(arrived == null)
			return null;
		
		OrderDAO orderDAO = (OrderDAO) ctx.getAttribute("orderDAO");
		ArtikalDAO artikalDAO = (ArtikalDAO) ctx.getAttribute("artikalDAO");

		
		Order order = orderDAO.findOrder(arrived.getId());
		
		order.setNapomena(arrived.getNapomena());
		
		
		order.setArtikli(new ArrayList<String>());
		order.setArtikli_objs(new ArrayList<Artikal>());
		//UBACIVANJE NOVIH ARTIKALA 		
		System.out.println("STRINGCINA: " + arrived.getArtikli());
		
		String [] orders_kolicina = arrived.getArtikli().split(";");
		
		for( int i = 0 ; i < orders_kolicina.length ; i ++ )
		{
			if(orders_kolicina[i].equals("")) continue;
			
			String [] data = orders_kolicina[i].split("-");
			int artikal_id = Integer.parseInt(data[0]);
			int kolicina = Integer.parseInt(data[1]);
			
			Artikal artikal = new Artikal(artikalDAO.findArtikal(artikal_id));
			artikal.setKolicina(kolicina);
			
			order.getArtikli().add(artikal.getKolicina() + " x " + artikal.getIme() + " from " + artikal.restoran_maticni);			
			order.getArtikli_objs().add(artikal);
		}
		
		  //IZRACUNAVANJE CENE
		  double total_cena = 0;
		  for( int i = 0 ; i < order.getArtikli_objs().size() ; i ++ )
		  {
			  total_cena += order.getArtikli_objs().get(i).getCena() * (double)order.getArtikli_objs().get(i).getKolicina();
		  }
		  total_cena = total_cena - ((double)order.getUsed_tokens() * 0.03 * total_cena); 
		  order.setTotal_cena(total_cena);
		
		System.out.println("ID vozaca: " + arrived.getDostavljac_id() + " ID vozila: " + arrived.getVozilo_id());
		
		
		//AKO JE TRENUTNO U PORUDZBINI ONDA IZBRISI IZ DOSTAVLJACA 
		if(order.getStanje() == 1)
		{
			
			UserDAO userDAO = (UserDAO) ctx.getAttribute("userDAO");
			
			
			DeliveryGuy deliveryGuy = userDAO.findDeliveryGuyById(order.getDostavljac_id());
			
			for( int i = 0 ; i < deliveryGuy.getOrder_id().size() ; i ++ )
			{
				if(deliveryGuy.getOrder_id().get(i) == order.getId())
				{
					
					System.out.println("Obelezeno kao dostavljeno: " + deliveryGuy.getOrder_id().get(i));
					
					deliveryGuy.getOrder_id().remove(i);
					
					break;
				}
			}
			
				
			userDAO.modifyDeliveryGuy(deliveryGuy, ctx.getRealPath(""));
			ctx.setAttribute("userDAO",userDAO);
			
			//VRACA VOZILO AKO MU JE TO POSLEDNJA BILA
			if(deliveryGuy.getOrder_id().size() == 0)
			{
				VoziloDAO voziloDAO = (VoziloDAO) ctx.getAttribute("voziloDAO");
				Vozilo vozilo = voziloDAO.findVozilo(deliveryGuy.getVoziloId());
				
				
				System.out.println("Vraca vozilo: " + vozilo.getMarka());
				
				vozilo.setZauzeto(0);
				voziloDAO.modifyVehicle(vozilo, ctx.getRealPath(""));
				
				//izmena deliveryGuy-a tako da poseduje id vozila
				deliveryGuy.setVoziloId(-1);	
				userDAO.modifyDeliveryGuy(deliveryGuy, ctx.getRealPath(""));
				ctx.setAttribute("userDAO", userDAO);
			}
		}
		
		
		if(arrived.getStanje() == 0)
		{
			System.out.println("PROMENA STANJA U 0");
			order.setStanje(0);
			order.setDostavljac_id(-1);
		}
		
		if(arrived.getStanje() == 1)
		{
				
			UserDAO userDAO = (UserDAO) ctx.getAttribute("userDAO");

			order.setStanje(1);
			order.setDostavljac_id(arrived.getDostavljac_id());
			
			DeliveryGuy deliveryGuy = userDAO.findDeliveryGuyById(arrived.getDostavljac_id());
			
			
			deliveryGuy.getOrder_id().add(order.getId());
	
			//DOSTAVLJAC UZIMA VOZILO
			VoziloDAO voziloDAO = (VoziloDAO) ctx.getAttribute("voziloDAO");
			Vozilo vozilo = voziloDAO.findVozilo(arrived.getVozilo_id());
			System.out.println("PROMENA STANJA U 1: DOSTAVLJAC: " + deliveryGuy.username + " SA VOZILOM: " + vozilo.getMarka()); 

			vozilo.setZauzeto(1);
			voziloDAO.modifyVehicle(vozilo, ctx.getRealPath(""));
			ctx.setAttribute("voziloDAO", voziloDAO);
			
			
			ctx.setAttribute("userDAO", userDAO);
			deliveryGuy.setVoziloId(vozilo.getId());	
			
			
			userDAO.modifyDeliveryGuy(deliveryGuy, ctx.getRealPath(""));
			ctx.setAttribute("userDAO",userDAO);	
		}
		if(arrived.getStanje() == 2)
		{
			System.out.println("PROMENA STANJA U 2");
			order.setStanje(2);
			order.setDostavljac_id(-1);
			
			if(order.total_cena >= 500)
			{
				UserDAO userDAO = (UserDAO) ctx.getAttribute("userDAO");
				User user = userDAO.findUserById(order.getUser());
				
				int current_tokens = user.getTokens() + 1;
				if(current_tokens >= 10)
					user.setTokens(10);
				else
					user.setTokens(current_tokens);
				
				userDAO.modifyUser(user, ctx.getRealPath(""));
			}
		}
		if(arrived.getStanje() == 3)
		{
			System.out.println("PROMENA STANJA U 3");
			order.setStanje(3);
			order.setDostavljac_id(-1);
		}
		
		orderDAO.modifyOrder(order, ctx.getRealPath(""));
		ctx.setAttribute("orderDAO", orderDAO);
		return order;
		
	}
	
	
	@POST
	@Path("/adminAdd")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Order adminAdd(TempOrder2 arrived, @Context HttpServletRequest request)
	{
		Admin admin = (Admin) request.getSession().getAttribute("admin");
		if(admin == null)
			return null;
		
		if(arrived == null)
			return null;
		
		OrderDAO orderDAO = (OrderDAO) ctx.getAttribute("orderDAO");
		ArtikalDAO artikalDAO = (ArtikalDAO) ctx.getAttribute("artikalDAO");

		Order order = new Order();
		
		
		order.setArtikli(new ArrayList<String>());
		order.setArtikli_objs(new ArrayList<Artikal>());
		//UBACIVANJE NOVIH ARTIKALA 		
		System.out.println("STRINGCINA: " + arrived.getArtikli());
		
		String [] orders_kolicina = arrived.getArtikli().split(";");
		
		for( int i = 0 ; i < orders_kolicina.length ; i ++ )
		{
			if(orders_kolicina[i].equals("")) continue;
			
			String [] data = orders_kolicina[i].split("-");
			int artikal_id = Integer.parseInt(data[0]);
			int kolicina = Integer.parseInt(data[1]);
			
			Artikal artikal = new Artikal(artikalDAO.findArtikal(artikal_id));
			artikal.setKolicina(kolicina);
			
			order.getArtikli().add(artikal.getKolicina() + " x " + artikal.getIme() + " from " + artikal.restoran_maticni);			
			order.getArtikli_objs().add(artikal);
		}
		double total_cena = 0;
		
		
		  artikalDAO.setPopolarity(order.getArtikli_objs(), ctx.getRealPath(""));
		  ctx.setAttribute("artikalDAO", artikalDAO);
		
		
		for( int i = 0 ; i < order.getArtikli_objs().size() ; i ++ )
		  {
			  total_cena += order.getArtikli_objs().get(i).getCena() * (double)order.getArtikli_objs().get(i).getKolicina();
		  }
		  total_cena = total_cena - ((double)order.getUsed_tokens() * 0.03 * total_cena); 
		  order.setTotal_cena(total_cena);
		
		order.setDostavljac_id(arrived.getDostavljac_id());
		order.setUser(arrived.getUser_id());
		if(arrived.getNapomena() == null || arrived.getNapomena().equals(""))
			order.setNapomena("null");
		else
			order.setNapomena(arrived.getNapomena());
		
		order.setId(orderDAO.gimmieNextOrderId());
		order.setStanje(1);
		order.setUsed_tokens(0);
		order.setVisible(1);
		
		LocalDateTime date = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		String date_str = date.format(formatter);
		order.setDateTime_order(date_str);
		
		
		
		//SETTAPOVANJE DELIVERY GUYA I VOZILA
		UserDAO userDAO = (UserDAO) ctx.getAttribute("userDAO");

		
		DeliveryGuy deliveryGuy = userDAO.findDeliveryGuyById(arrived.getDostavljac_id());
		deliveryGuy.getOrder_id().add(order.getId());

		//DOSTAVLJAC UZIMA VOZILO
		VoziloDAO voziloDAO = (VoziloDAO) ctx.getAttribute("voziloDAO");
		Vozilo vozilo = voziloDAO.findVozilo(arrived.getVozilo_id());
		System.out.println("PROMENA STANJA U 1: DOSTAVLJAC: " + deliveryGuy.username + " SA VOZILOM: " + vozilo.getMarka()); 

		vozilo.setZauzeto(1);
		voziloDAO.modifyVehicle(vozilo, ctx.getRealPath(""));
		ctx.setAttribute("voziloDAO", voziloDAO);
		
		
		ctx.setAttribute("userDAO", userDAO);
		deliveryGuy.setVoziloId(vozilo.getId());	
		
		
		userDAO.modifyDeliveryGuy(deliveryGuy, ctx.getRealPath(""));
		ctx.setAttribute("userDAO",userDAO);
		
		
		//VRACANJE NAPRAVLJENOG ORDERA
		orderDAO.addOrder(order, ctx.getRealPath(""));
		ctx.setAttribute("orderDAO", orderDAO);
		return order;
		
		
	}
	
	
	
	@GET
	@Path("/deleteValidation/{id}")
	public String removeValidation(@PathParam("id") String id_str, @Context HttpServletRequest request)
	{
		Admin admin = (Admin) request.getSession().getAttribute("admin");
		if(admin == null)
			return null;
		
		boolean moze = true;
		
		try 
		{
			int id = Integer.parseInt(id_str);
			
			OrderDAO orderDAO = (OrderDAO) ctx.getAttribute("orderDAO");
			
			Order order = orderDAO.findOrder(id);
			
			if(order.getStanje() == 1)
				moze = false;
			
			if(moze)
				return "moze";
			else
				return null;
		}
		catch(Exception e)
		{
			System.out.println("err: " + e);
			return null;
		}
	}
	
	
	@DELETE
	@Path("/delete/{id}")
	public void removeOrder(@PathParam("id") String id_str, @Context HttpServletRequest request)
	{
		Admin admin = (Admin) request.getSession().getAttribute("admin");
		if(admin == null)
			return;
		
		try 
		{
			int id = Integer.parseInt(id_str);
			
			OrderDAO orderDAO = (OrderDAO) ctx.getAttribute("orderDAO");
			
			Order order = orderDAO.findOrder(id);
			
			if(order == null)
				return;
			
			order.setVisible(0);
			orderDAO.modifyOrder(order, ctx.getRealPath(""));
			
			orderDAO.getOrders().remove(id);
			ctx.setAttribute("orderDAO", orderDAO);
		}
		catch(Exception e)
		{
			System.out.println("err: " + e);
			return;
		}
	}
	
	
	
	
	
	
	
}
