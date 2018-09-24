package services;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
import beans.Restoran;
import beans.User;
import dao.ArtikalDAO;
import dao.RestoranDAO;

@Path("restorani")
public class RestoranService 
{
	
	@Context
	ServletContext ctx;
	
	
	public RestoranService()
	{		
	}
	
	
	@PostConstruct
	public void init() 
	{
		RestoranDAO init_restoran = null;
    	String contextPath = ctx.getRealPath("");

		if (ctx.getAttribute("restoranDAO") == null) 
		{
			init_restoran = new RestoranDAO(contextPath);
		}
		
		if (ctx.getAttribute("artikalDAO") == null) 
		{
			System.out.println("baaaaaam");

			ctx.setAttribute("artikalDAO", new ArtikalDAO(contextPath));
	
		}
		
		if(ctx.getAttribute("restoranDAO") == null)
		{
			ctx.setAttribute("restoranDAO", init_restoran);
		}
		
	}
	
	
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Restoran> getProducts() 
	{
		RestoranDAO dao = (RestoranDAO) ctx.getAttribute("restoranDAO");
		return dao.findAll();
	}
	
	
	@GET
	@Path("restaurants/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Restoran getSelectedRestoran(@PathParam("id") String id) 
	{
		RestoranDAO dao = (RestoranDAO) ctx.getAttribute("restoranDAO");
		return dao.findRestoran(Integer.parseInt(id));
	
	}
	
	@PUT
	@Path("/modify")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Restoran modifyRestaurant(Restoran restoran, @Context HttpServletRequest request)
	{
		Admin admin = (Admin) request.getSession().getAttribute("admin");
		if(admin == null)
			return null;
		
		
		
		
		restoran.print();
		
		System.out.println("STIGO RESTORAN id: " + restoran.getId() + " ime: " + restoran.getIme());
		RestoranDAO dao = (RestoranDAO) ctx.getAttribute("restoranDAO");
		Restoran to_be_modified = dao.findRestoran(restoran.id);
		if(to_be_modified == null)
		{
			System.out.println("null - odo ja.");
			return null;
		}
		
		to_be_modified.setIme(restoran.getIme());
		to_be_modified.setUlica(restoran.getUlica());
		to_be_modified.setBroj_ulice(restoran.getBroj_ulice());
		to_be_modified.setTip_str(restoran.getTip_str());
		to_be_modified.namestiTip(restoran.getTip_str());
		
		dao.modify(to_be_modified,ctx.getRealPath(""));
		ctx.setAttribute("restoranDAO", dao);
		

		//ZAMENA NAZIVA RESTORANA U SVAKOM ARTIKLU KOJI GA SADRZI
		ArtikalDAO artikalDAO = (ArtikalDAO) ctx.getAttribute("artikalDAO");
		List<Artikal> artikli = new ArrayList<Artikal>(artikalDAO.findAll());
		for( int i = 0; i < artikli.size() ; i ++ )
		{
			if(to_be_modified.getId() == artikli.get(i).getId_restorana())
			{
				artikli.get(i).setRestoran_maticni(to_be_modified.getIme());
				artikalDAO.modify(artikli.get(i), ctx.getRealPath(""));
			}
		}
		ctx.setAttribute("artikalDAO", artikalDAO);
		
		System.out.println("Success modding!");

		
		return to_be_modified;
				
	}
	
	@DELETE
	@Path("/delete/{id}")
	public void removeRestaurant(@PathParam("id") String id_str, @Context HttpServletRequest request)
	{
		Admin admin = (Admin) request.getSession().getAttribute("admin");
		if(admin == null)
			return;
		
		try
		{
			int id = Integer.parseInt(id_str);
			RestoranDAO dao = (RestoranDAO) ctx.getAttribute("restoranDAO");
			Restoran to_be_removed = dao.findRestoran(id);
			
			if(to_be_removed == null)
				return;
			
			to_be_removed.setVisible(0);
			dao.modify(to_be_removed, ctx.getRealPath(""));
			dao.getRestorani().remove(to_be_removed.getId());
			
			ctx.setAttribute("restoranDAO", dao);
			
			//BRISANJE ARTIKALA SU UNUTAR RESTORANA
			ArtikalDAO artikalDAO = (ArtikalDAO) ctx.getAttribute("artikalDAO");
			List<Artikal> artikli = new ArrayList<Artikal>(artikalDAO.findAll());
			
			for( int i = 0 ; i < artikli.size() ; i ++ )
			{
				if(artikli.get(i).getId_restorana() == id)
				{
					artikli.get(i).setVisible(0);
					artikalDAO.modify(artikli.get(i), ctx.getRealPath(""));
					artikalDAO.getArtikli().remove(artikli.get(i).getId());
				}
			}
			ctx.setAttribute("artikalDAO", artikalDAO);
			
		}
		catch(Exception e)
		{
			System.out.println("err: " + e);
		}
	}
	
	@POST
	@Path("/add")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Restoran add(Restoran restoran, @Context HttpServletRequest request)
	{
		Admin admin = (Admin) request.getSession().getAttribute("admin");
		if(admin == null)
			return null;
		
		RestoranDAO dao = (RestoranDAO) ctx.getAttribute("restoranDAO");
		restoran.setId(dao.getNextId());
		dao.add(restoran, ctx.getRealPath(""));
		ctx.setAttribute("restoranDAO", dao);
		
		return restoran;
		
	}
	
	
}
