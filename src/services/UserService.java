package services;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
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
import beans.Restoran;
import beans.User;
import dao.ArtikalDAO;
import dao.RestoranDAO;
import dao.UserDAO;

@Path("users")
public class UserService {
		
	@Context
	ServletContext ctx;
	
	@PostConstruct
	public void init() 
	{
		if (ctx.getAttribute("userDAO")==null) 
		{
	    	String contextPath = ctx.getRealPath("");
	     	UserDAO userDAO = new UserDAO(contextPath);
	    	ctx.setAttribute("userDAO", userDAO);
		}
	}
	
	
	//SAMO ZA OBICNOG KORISNIKA - povlacenje narudzbina iz sesije
	@GET
	@Path("/getBasketContent")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Artikal> getOrders(@Context HttpServletRequest request) 
	{
		  User user = null;
		  user =  (User)request.getSession().getAttribute("user");
		  
		  if(user!=null)
		  {
			  return user.getKorpa();
		  }
		  
		  return null;
	}
	
	
	
	
	
	//SAMO ZA OBICNOG KORISNIKA - dodavanje u kropu
	@PUT
	@Path("/AddToBasket/{id_product}/{kol}")
	public void updateBasket(@PathParam("id_product") String id_product, @PathParam("kol") String kol, @Context HttpServletRequest request) 
	{
	  User user = null;
	  user =  (User)request.getSession().getAttribute("user");

	  if (user != null) 
	  {
		  int id_prod = Integer.parseInt(id_product);
		  int kolicina = Integer.parseInt(kol);
		 
		  ArtikalDAO dao = (ArtikalDAO) ctx.getAttribute("artikalDAO");
		  
		  if(dao!=null)
		  {
			  Artikal temp = dao.findArtikal(id_prod);
			  if(temp!=null)
			  {
				  //KREIRANJE NOVOG OBJEKTA DA NE BIH IZ BAZE POREMETIO 
				  Artikal za_korpu = new Artikal(temp);
				  za_korpu.setKolicina(kolicina);
				  
				  
				  List<Artikal> pomocna_lista =  user.getKorpa();
				  boolean dalie_ubacen_vec = false;
				  
				  for(int i = 0 ; i < pomocna_lista.size() ; i++ )
				  {
					  if(pomocna_lista.get(i).getId() == id_prod)
					  {
						  dalie_ubacen_vec = true;
						  pomocna_lista.get(i).setKolicina(pomocna_lista.get(i).getKolicina() + kolicina);
					  }
				  }
				  if(!dalie_ubacen_vec)
				  {
					  user.addToKorpa(za_korpu);
				  }
				  
				  
				  request.getSession().setAttribute("user",user);
				  
				  System.out.println("Check - Velicina liste: " + user.getKorpa().size());

			  }
		  }
	   }
	}
	
	//LOGINOVANJE SAMO ZA OBICNOG KORISNIKA
	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public User loginUser(User user, @Context HttpServletRequest request) 
	{
		
		UserDAO userDAO = (UserDAO) ctx.getAttribute("userDAO");
		User loggedUser = userDAO.findUser(user.getUsername());
		
		
		if (loggedUser == null) 
		{
			return new User("1","0");  //1 znaci da je problem u username - ne postoji unet
		}
		if (!loggedUser.getPassword().equals(user.getPassword())) 
		{
			return new User("0","1"); //1 znaci da je problem u pass - username postoji 
		}
		
		
		request.getSession().setAttribute("user", loggedUser);
		return loggedUser;		
		
	}
	
	//LOGINOVANJE ZA DOSTAVLJACA
	@POST
	@Path("/loginDeliveryGuy")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public DeliveryGuy loginDeliveryGuy(DeliveryGuy deliveryGuy, @Context HttpServletRequest request) 
	{
		
		UserDAO userDAO = (UserDAO) ctx.getAttribute("userDAO");
		DeliveryGuy loggeddeliveryGuy = userDAO.findDeliveryGuy(deliveryGuy.getUsername());
		
		
		if (loggeddeliveryGuy == null) 
		{
			return new DeliveryGuy("1","0");  //1 znaci da je problem u username - ne postoji unet
		}
		if (!loggeddeliveryGuy.getPassword().equals(deliveryGuy.getPassword())) 
		{
			return new DeliveryGuy("0","1"); //1 znaci da je problem u pass 
		}
		
		
		request.getSession().setAttribute("deliveryGuy", loggeddeliveryGuy);
		return loggeddeliveryGuy;		
		
	}
	
	
	//LOGIN - ADMIN
	@POST
	@Path("/loginAdmin")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Admin loginAdmin(Admin admin, @Context HttpServletRequest request) 
	{
		
		UserDAO userDAO = (UserDAO) ctx.getAttribute("userDAO");
		Admin loggedAdmin = userDAO.findAdmin(admin.getUsername());
		
		
		if (loggedAdmin == null) 
		{
			return new Admin("1","0");  //1 znaci da je problem u username - ne postoji unet
		}
		if (!loggedAdmin.getPassword().equals(admin.getPassword())) 
		{
			return new Admin("0","1"); //1 znaci da je problem u pass 
		}
		
		
		request.getSession().setAttribute("admin", loggedAdmin);

		return loggedAdmin;		
		
	}	
	
	
	
	
	//OBICAN KORISNIK - SIGNUPuje SE SAMO OBICAN KORISNIK
	@POST
	@Path("/signUp")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public User signUp(User user, @Context HttpServletRequest request) 
	{
		UserDAO userDAO = (UserDAO) ctx.getAttribute("userDAO");
		
		//username check
		String patternString = "^[a-zA-Z0-9]+$";
		Pattern pattern = Pattern.compile(patternString);
		Matcher matcher = pattern.matcher(user.getUsername());
		
		if(!matcher.matches())
		{
			return null;
		}
		
		if(userDAO.findUser(user.getUsername())!=null)
		{
			return null;
		}
		
		//password check
		if(!matcher.matches())
		{
			return null;
		}
		if(user.getPassword().length() < 5)
		{
			return null;
		}
		
		//name check
		patternString = "^[a-zA-ZšđžčćŠĐŽČĆ]+$";
		pattern = Pattern.compile(patternString);
		matcher = pattern.matcher(user.getName());
		
		if(!matcher.matches())
		{
			return null;
		}
		
		//lastname check
		patternString = "^[a-zA-ZšđžčćŠĐŽČĆ]+$";
		pattern = Pattern.compile(patternString);
		matcher = pattern.matcher(user.getLastname());
		
		if(!matcher.matches())
		{
			return null;
		}
			
		
		//email check
		patternString = "^[a-zA-Z]+$";
		pattern = Pattern.compile("^[a-zA-Z0-9@\\.]+$");
		matcher = pattern.matcher(user.getEmail());
		if(!matcher.matches())
		{
			return null;
		}
		
		
		//phone check
		patternString = "^[0-9]*$";
		pattern = Pattern.compile("^[0-9]+$");
		matcher = pattern.matcher(user.getTel());
		if(!matcher.matches())
		{
			return null;
		}
		
		
		User newUser = new User(user.getId(),user.getUsername(),user.getPassword(),user.getName(),user.getLastname(),user.getTel(),user.getEmail());
		
		
		newUser.setId(userDAO.generateUserId());
		
		
		newUser.printUser();
		
		userDAO.addUser(newUser, ctx.getRealPath(""));
		
		ctx.setAttribute("userDAO", userDAO);
		
		UserDAO userDAO2 = (UserDAO) ctx.getAttribute("userDAO");
		userDAO2.printHash();
		
		request.getSession().setAttribute("user", newUser);
		return user;		
		
	}
	
	
	
	//ZA OBICNOG KORISNIKA SAMO
	@GET
	@Path("/currentUser")
	@Produces(MediaType.APPLICATION_JSON)
	public User getUser(@Context HttpServletRequest request) 
	{
		return (User) request.getSession().getAttribute("user");
	}
	
	
	
	@GET
	@Path("/getUserById/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public User getUserByID(@PathParam("id") String id_str) 
	{
		int id = -1;
		try
		{
			id = Integer.parseInt(id_str);
		}
		catch(Exception e)
		{}
		
		UserDAO userDAO = (UserDAO) ctx.getAttribute("userDAO");

		User retVal = userDAO.findUserById(id);
		return retVal;
		
	}
	
	//ZA DOSTAVLJACA
	@GET
	@Path("/currentDeliveryGuy")
	@Produces(MediaType.APPLICATION_JSON)
	public DeliveryGuy getDeliveryGuy(@Context HttpServletRequest request) 
	{
		return (DeliveryGuy) request.getSession().getAttribute("deliveryGuy");
	}

	//GET CURRENT ADMIN
	@GET
	@Path("/currentAdmin")
	@Produces(MediaType.APPLICATION_JSON)
	public Admin getAdmin(@Context HttpServletRequest request) 
	{
		System.out.println("Pozvana provera:");
		return (Admin) request.getSession().getAttribute("admin");
	}
	
	
	
	//ZA SVE TIPOVE KORISNIKA
	@GET
	@Path("/logout")
	public void logout(@Context HttpServletRequest request) 
	{
	  User user = null;
	  DeliveryGuy deliveryGuy = null;
	  Admin admin = null;
	  
	  user =  (User)request.getSession().getAttribute("user");
	  if (user != null) 
	  {
		  request.getSession().invalidate();  
	  }
	  
	  
	  deliveryGuy =  (DeliveryGuy)request.getSession().getAttribute("deliveryGuy");
	  if (deliveryGuy != null) 
	  {
		  request.getSession().invalidate();  
	  }
	  
	  admin =  (Admin)request.getSession().getAttribute("admin");
	  if (admin != null) 
	  {
		  request.getSession().invalidate();  
	  }
	  
	}
	
	//OBICAN KORISNIK - PINOVANJE RESTORANA
	@PUT
	@Path("insertToPinned/{restoran_id}")
	public void insertPinnedRestaurant(@PathParam("restoran_id") String id_str, @Context HttpServletRequest request)
	{
		 User user = null;
		 user =  (User)request.getSession().getAttribute("user");
		 if(user!=null)
		 {
			 int id_restorana = Integer.parseInt(id_str);
			 user.getSaved().add(id_restorana);
			
			 request.getSession().setAttribute("user", user);
			 
			  UserDAO userDAO = (UserDAO) ctx.getAttribute("userDAO");
			  userDAO.modifyUser(user, ctx.getRealPath(""));
			  ctx.setAttribute("userDAO", userDAO);
			 
		 }
	}
	
	
	//OBICAN KORISNIK - PINOVANJE RESTORANA
	@PUT
	@Path("deleteFromPinned/{restoran_id}")
	public void deletePinnedRestaurant(@PathParam("restoran_id") String id_str, @Context HttpServletRequest request)
	{
		 User user = null;
		 user =  (User)request.getSession().getAttribute("user");
		 if(user!=null)
		 {
			 int id_restorana = Integer.parseInt(id_str);

			 for( int i = 0 ; i < user.getSaved().size() ; i ++ )
			 {
				 if(user.getSaved().get(i) == id_restorana)
				 {
					 user.getSaved().remove(i);
					 break;
				 }
			 }
			 
			 request.getSession().setAttribute("user", user);	
			 
			  UserDAO userDAO = (UserDAO) ctx.getAttribute("userDAO");
			  userDAO.modifyUser(user, ctx.getRealPath(""));
			  ctx.setAttribute("userDAO", userDAO);

		 }
	}
	
	/*
	 * DODATI CHECK IMENA AKO JE ADMIN
	 * 
	 * */
	
	
	//OBICAN KORISNIK - SAMO ZA PRIJAVU
	@GET
	@Path("checkIfUnique/{username}")
	@Produces(MediaType.TEXT_PLAIN)
	public String check (@PathParam("username") String username)
	{
		
		boolean moze = true;
		UserDAO userDAO = (UserDAO) ctx.getAttribute("userDAO");
		
		if(userDAO.findUser(username)!=null)
			moze = false;
		if(userDAO.findDeliveryGuy(username)!=null)
			moze = false;
			
		if(userDAO.findAdmin(username)!=null)
			moze = false;
			
		if(moze)	
			return "oke";
		else
			return null;
	}
	
	//OBICAN KORISNIK - SAMO ZA PRIJAVU
	@GET
	@Path("checkIfUniqueEmail/{username}")
	@Produces(MediaType.TEXT_PLAIN)
	public String checkEmail (@PathParam("username") String email)
	{
		boolean moze = true;
		UserDAO userDAO = (UserDAO) ctx.getAttribute("userDAO");
		
		Set<String> userKeys = userDAO.getUsers().keySet();
		Set<String> deliveryGuysKeys = userDAO.getDeliveryGuys().keySet();
		Set<String> adminKeys = userDAO.getAdmins().keySet();

		for(String key : userKeys)
		{
			User temp = userDAO.findUser(key);
			if(temp.getEmail().equals(email))
				moze = false;
		}	
		
		for(String key : deliveryGuysKeys)
		{
			DeliveryGuy temp = userDAO.findDeliveryGuy(key);
			if(temp.getEmail().equals(email))
				moze = false;
		}	
		for(String key : adminKeys)
		{
			Admin temp = userDAO.findAdmin(key);
			if(temp.getEmail().equals(email))
				moze = false;
		}
		
		if(moze)
			return "asdf";
		else
			return null;
	}
	
	//GETTOVANJE SVIH KORISNIKA
	
	@GET
	@Path("/getAllUsers")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<User> getAllUsers()
	{
		UserDAO userDAO = (UserDAO) ctx.getAttribute("userDAO");
		
		return userDAO.findAllUsers();
	}
	
	@GET
	@Path("/getAllDeliveryGuys")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<DeliveryGuy> getAllDeliveryGuys()
	{
		UserDAO userDAO = (UserDAO) ctx.getAttribute("userDAO");
		
		return userDAO.findAllDeliveryGuys();
	}
	
	@GET
	@Path("/getAllAdmins")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Admin> getAllAdmins()
	{
		UserDAO userDAO = (UserDAO) ctx.getAttribute("userDAO");
		
		return userDAO.findAllAdmins();
	}
	
	
	@PUT
	@Path("/changeType/{user_id}/{selected_type}")
	public String changeType(@PathParam("user_id") String x, @PathParam("selected_type") String y, @Context HttpServletRequest request)
	{
		

		
		Admin admin = (Admin) request.getSession().getAttribute("admin");
		if(admin == null)
			return null;
		

		
		try
		{
			int incoming_type = 10;
			int user_id = Integer.parseInt(x);
			int selected_type = Integer.parseInt(y);
			
			UserDAO userDAO = (UserDAO) ctx.getAttribute("userDAO");
			String context_path = ctx.getRealPath("");
			
			if(userDAO.findDeliveryGuyById(user_id) != null)
				incoming_type = 1;
			if(userDAO.findUserById(user_id) != null)
				incoming_type = 0;	
			if(userDAO.findAdminById(user_id)!=null)
				incoming_type = 2;
			
			
		
			//korisnik u dostavljac
			if(incoming_type == 0 && selected_type == 1)
			{
				System.out.println("0 ----------- 1 Delivery guy");
				
				
				User user = userDAO.findUserById(user_id);
				
				DeliveryGuy deliveryGuy= new DeliveryGuy(user.getId(), user.getUsername(), user.getPassword(), user.getName(), user.getLastname(), user.getTel(), user.getEmail(), user.getRegistration_date());
				
				userDAO.remove_any_type(user_id, user.getUsername(), context_path); //uklanjanje korisnika
				userDAO.addDeliveryGuy(deliveryGuy, context_path); 
				
				ctx.setAttribute("userDAO", userDAO);
				
				return ""+selected_type;
			}
			
			//korisnik u admin
			if(incoming_type == 0 && selected_type == 2)
			{
				
				System.out.println("0 ----------- 2 Admin");
				User user = userDAO.findUserById(user_id);
				
				Admin newAdmin = new Admin(user.getId(), user.getUsername(), user.getPassword(), user.getName(), user.getLastname(), user.getTel(), user.getEmail(), user.getRegistration_date(), 2);
				
				userDAO.remove_any_type(user_id, user.getUsername(), context_path); //izbrisem korisnika
				userDAO.addAdmin(newAdmin, context_path);
				
				ctx.setAttribute("userDAO", userDAO);
				
				return ""+selected_type;
			}
			
			if(incoming_type == 1 && selected_type == 0)
			{
				System.out.println("1 ----------- 0 User");
				DeliveryGuy deliveryGuy = userDAO.findDeliveryGuyById(user_id);
				
				User user = new User(deliveryGuy.getId(), deliveryGuy.getUsername(), deliveryGuy.getPassword(), deliveryGuy.getName(), deliveryGuy.getLastname(), deliveryGuy.getTel(), deliveryGuy.getEmail(), deliveryGuy.getRegistration_date());
				
				userDAO.remove_any_type(user_id, deliveryGuy.getUsername(), context_path); //izbrisem deliveryGuy
				userDAO.addUser(user, context_path);
				ctx.setAttribute("userDAO", userDAO);
				
				return ""+selected_type;
			}
			
			if(incoming_type == 1 && selected_type == 2)
			{
				System.out.println("1 ----------- 2 Admin");
				DeliveryGuy deliveryGuy = userDAO.findDeliveryGuyById(user_id);
			
				Admin newAdmin = new Admin(deliveryGuy.getId(), deliveryGuy.getUsername(), deliveryGuy.getPassword(), deliveryGuy.getName(), deliveryGuy.getLastname(), deliveryGuy.getTel(), deliveryGuy.getEmail(), deliveryGuy.getRegistration_date(), 2);
			
				userDAO.remove_any_type(user_id, deliveryGuy.getUsername(), context_path); //izbrisem deliveryGuy
				
				userDAO.addAdmin(admin, context_path);
				
				ctx.setAttribute("userDAO", userDAO);
				
				return ""+selected_type;
			}
			
			if(incoming_type == 2 && selected_type == 0)
			{
				System.out.println("2 ----------- 1 Delivery");
				
				Admin admincina = userDAO.findAdminById(user_id);	
				DeliveryGuy deliveryGuy= new DeliveryGuy(admincina.getId(), admincina.getUsername(), admincina.getPassword(), admincina.getName(), admincina.getLastname(), admincina.getTel(), admincina.getEmail(), admincina.getRegistration_date());
			
				userDAO.remove_any_type(user_id, admincina.getUsername(), context_path);
				userDAO.addDeliveryGuy(deliveryGuy, context_path);
				
				ctx.setAttribute("userDAO", userDAO);
				
				return ""+selected_type;	
			}
			
			if(incoming_type == 2 && selected_type == 0)
			{
				
				System.out.println("2 ----------- 0 User");
				
				Admin admincina = userDAO.findAdminById(user_id);	
				User user = new User(admincina.getId(), admincina.getUsername(), admincina.getPassword(), admincina.getName(), admincina.getLastname(), admincina.getTel(), admincina.getEmail(), admincina.getRegistration_date());
			
				userDAO.remove_any_type(user_id, admincina.getUsername(), context_path);
				userDAO.addUser(user, context_path);
				
				ctx.setAttribute("userDAO", userDAO);
				
				return ""+selected_type;
			}
			
			
		}
		catch(Exception e)
		{
			System.out.println("err: " + e);
			return null;
		}
		
		return null;
	}
	
}
