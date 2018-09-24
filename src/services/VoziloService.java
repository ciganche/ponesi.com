package services;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import beans.Admin;
import beans.Artikal;
import beans.DeliveryGuy;
import beans.Vozilo;
import dao.ArtikalDAO;
import dao.OrderDAO;
import dao.UserDAO;
import dao.VoziloDAO;

@Path("vozila")
public class VoziloService {
	
	@Context
	ServletContext ctx;
	
	
	@PostConstruct
	public void init()
	{
		if(ctx.getAttribute("voziloDAO") == null)
		{
			
	    	String contextPath = ctx.getRealPath("");
	    	VoziloDAO voziloDAO = new VoziloDAO(contextPath);
	    	
	    	ctx.setAttribute("voziloDAO", voziloDAO);
		}
	}
	
	
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Vozilo> getArticles() 
	{
		VoziloDAO dao = (VoziloDAO) ctx.getAttribute("voziloDAO");
		return dao.findAll();
	}
	
	
	@GET
	@Path("/getVehicle/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Vozilo getVozilo(@PathParam("id") String id_str, @Context HttpServletRequest request)
	{
		DeliveryGuy deliveryGuy = (DeliveryGuy) request.getSession().getAttribute("deliveryGuy");
		if (deliveryGuy == null)
				return null;
		try
		{
			int id = Integer.parseInt(id_str);
			
			VoziloDAO voziloDAO = (VoziloDAO) ctx.getAttribute("voziloDAO");
			
			return voziloDAO.findVozilo(id);
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	@PUT
	@Path("/takeVehicle/{id}")
	public void takeVozilo(@PathParam("id") String id_str, @Context HttpServletRequest request)
	{
		DeliveryGuy deliveryGuy = (DeliveryGuy) request.getSession().getAttribute("deliveryGuy");
		
		if (deliveryGuy == null)
			return;
		

		
		try
		{
			
			int id_vozila = Integer.parseInt(id_str);
			
			
			VoziloDAO voziloDAO = (VoziloDAO) ctx.getAttribute("voziloDAO");
			Vozilo vozilo = voziloDAO.findVozilo(id_vozila);
			
			if(vozilo.getZauzeto() == 1) //ukoliko je zauzeto vozilo
				return;
			
			System.out.println("Dobija vozilo: " + vozilo.getMarka());
			
			vozilo.setZauzeto(1);
			voziloDAO.modifyVehicle(vozilo, ctx.getRealPath(""));
			
			//izmena deliveryGuy-a tako da poseduje id vozila
			deliveryGuy.setVoziloId(id_vozila);	
			UserDAO userDAO = (UserDAO) ctx.getAttribute("userDAO");
			userDAO.modifyDeliveryGuy(deliveryGuy, ctx.getRealPath(""));
			ctx.setAttribute("userDAO", userDAO);
			request.getSession().setAttribute("deliveryGuy", deliveryGuy);
			
			
			
		}
		catch(Exception e)
		{
			
		}
	}
	
	
	@PUT
	@Path("/returnVehicle/{id}")
	public void returnVehicle(@PathParam("id") String id_str, @Context HttpServletRequest request)
	{
		DeliveryGuy deliveryGuy = (DeliveryGuy) request.getSession().getAttribute("deliveryGuy");
		
		if (deliveryGuy == null)
			return;
		
		try
		{
			int id_vozila = Integer.parseInt(id_str);
			
			
			VoziloDAO voziloDAO = (VoziloDAO) ctx.getAttribute("voziloDAO");
			Vozilo vozilo = voziloDAO.findVozilo(id_vozila);
			
			if(vozilo.getZauzeto() == 0) //ukoliko vraca slobodno
				return;
			
			System.out.println("Vraca vozilo: " + vozilo.getMarka());
			
			vozilo.setZauzeto(0);
			voziloDAO.modifyVehicle(vozilo, ctx.getRealPath(""));
			
			//izmena deliveryGuy-a tako da poseduje id vozila
			deliveryGuy.setVoziloId(-1);	
			UserDAO userDAO = (UserDAO) ctx.getAttribute("userDAO");
			userDAO.modifyDeliveryGuy(deliveryGuy, ctx.getRealPath(""));
			ctx.setAttribute("userDAO", userDAO);
			request.getSession().setAttribute("deliveryGuy", deliveryGuy);
			
		}
		catch(Exception e)
		{
			
		}
	}
	
	
	@PUT
	@Path("/modify")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Vozilo modify (Vozilo vozilo, @Context HttpServletRequest request)
	{
		Admin admin = (Admin) request.getSession().getAttribute("admin");
		if(admin == null)
			return null;
		

		
		vozilo.setVisible(1);
		vozilo.setZauzeto(0);
		VoziloDAO voziloDAO = (VoziloDAO) ctx.getAttribute("voziloDAO");
		voziloDAO.modifyVehicle(vozilo, ctx.getRealPath(""));
		ctx.setAttribute("voziloDAO", voziloDAO);
		
		return vozilo;
	}
	
	//provera da li neki od dostavljaca je uzeo vozilo da obavi dostavu
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
			
			UserDAO userDAO = (UserDAO) ctx.getAttribute("userDAO");
			
			HashMap<String, DeliveryGuy> hash = userDAO.getDeliveryGuys();
			Collection<DeliveryGuy> deliveryGuys = hash.values();
			for(DeliveryGuy deliveryGuy : deliveryGuys)
			{
				if(deliveryGuy.getVoziloId() == id)
				{
					moze = false;
					break;
				}
			}
			
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
	public void removeVehicle(@PathParam("id") String id_str, @Context HttpServletRequest request)
	{
		Admin admin = (Admin) request.getSession().getAttribute("admin");
		if(admin == null)
			return;
		
		try 
		{
			int id = Integer.parseInt(id_str);
			
			VoziloDAO voziloDAO = (VoziloDAO) ctx.getAttribute("voziloDAO");
			Vozilo vozilo = voziloDAO.findVozilo(id);
			
			if(vozilo == null)
				return;
			
			vozilo.setVisible(0);
			voziloDAO.modifyVehicle(vozilo, ctx.getRealPath(""));
			
			voziloDAO.getVozila().remove(id);
			ctx.setAttribute("voziloDAO", voziloDAO);
		}
		catch(Exception e)
		{
			System.out.println("err: " + e);
			return;
		}
	}
	
	@POST
	@Path("/add")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Vozilo add (Vozilo vozilo, @Context HttpServletRequest request)
	{
		Admin admin = (Admin) request.getSession().getAttribute("admin");
		if(admin == null)
			return null;
		
		
		VoziloDAO voziloDAO = (VoziloDAO) ctx.getAttribute("voziloDAO");
		vozilo.setId(voziloDAO.getNextId());
		vozilo.setVisible(1);
		vozilo.setZauzeto(0);
		voziloDAO.add(vozilo, ctx.getRealPath(""));
		ctx.setAttribute("voziloDAO", voziloDAO);
		
		
		return vozilo;
	}
	
}
