package services;

import java.util.ArrayList;
import java.util.Collection;
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
import dao.ArtikalDAO;
import dao.RestoranDAO;
import dao.UserDAO;

@Path("artikli")
public class ArtikalService 
{
	
	@Context 
	ServletContext ctx;

	public ArtikalService()
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
	public Collection<Artikal> getArticles() 
	{
		ArtikalDAO dao = (ArtikalDAO) ctx.getAttribute("artikalDAO");
		return dao.findAll();
	}
	
	@GET
	@Path("/getArtikalById/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Artikal getArticlesById(@PathParam("id") String id_str) 
	{
		int id = -1;
		try
		{
			id = Integer.parseInt(id_str);
		}
		catch(Exception e)
		{}
		
		ArtikalDAO dao = (ArtikalDAO) ctx.getAttribute("artikalDAO");
		
		return dao.findArtikal(id);
	}
	
	@PUT
	@Path("/modify")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Artikal modifyArtikal(Artikal artikal, @Context HttpServletRequest request)
	{
		Admin admin = (Admin) request.getSession().getAttribute("admin");
		if(admin == null)
			return null;			
		
		
		ArtikalDAO dao = (ArtikalDAO) ctx.getAttribute("artikalDAO");
		RestoranDAO restoranDAO = (RestoranDAO) ctx.getAttribute("restoranDAO");
		
		artikal.setRestoran_maticni(restoranDAO.findRestoran(artikal.getId_restorana()).getIme());
		artikal.setKolicina(0);
		
		restoranDAO.removeFromArtikli(artikal.getId_restorana(), artikal.getId());
		restoranDAO.findRestoran(artikal.getId_restorana()).getArtikli_restorana().add(artikal);
		
		dao.modify(artikal, ctx.getRealPath(""));
		ctx.setAttribute("restoranDAO", restoranDAO);
		ctx.setAttribute("artikalDAO", dao);
		
		return artikal;
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
			ArtikalDAO artikalDAO = (ArtikalDAO) ctx.getAttribute("artikalDAO");
			
			Artikal to_be_removed = artikalDAO.findArtikal(id);
			
			if(to_be_removed == null)
				return;
			
			to_be_removed.setVisible(0);
			artikalDAO.modify(to_be_removed, ctx.getRealPath(""));
			artikalDAO.getArtikli().remove(to_be_removed.getId());
			ctx.setAttribute("artikalDAO", artikalDAO);
			
			RestoranDAO dao = (RestoranDAO) ctx.getAttribute("restoranDAO");
			dao.removeFromArtikli(to_be_removed.getId_restorana(), to_be_removed.getId());
			ctx.setAttribute("restoranDAO", dao);
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
	public Artikal add(Artikal artikal, @Context HttpServletRequest request)
	{
		Admin admin = (Admin) request.getSession().getAttribute("admin");
		if(admin == null)
			return null;
		


		if(artikal.getCena() < 0 || artikal.getGramaza() < 0)
			return null;
		
		RestoranDAO restoranDAO = (RestoranDAO) ctx.getAttribute("restoranDAO");
		ArtikalDAO artikalDAO = (ArtikalDAO) ctx.getAttribute("artikalDAO");
		artikal.setId(artikalDAO.getNextId());
		artikal.setKolicina(0);
		artikal.setVisible(1);
		artikal.setRestoran_maticni(restoranDAO.findRestoran(artikal.getId_restorana()).getIme());
		
		artikalDAO.add(artikal,ctx.getRealPath(""));
		ctx.setAttribute("artikalDAO", artikalDAO);
		
		restoranDAO.findRestoran(artikal.getId_restorana()).getArtikli_restorana().add(artikal);
		ctx.setAttribute("resotranDAO", restoranDAO);
		return artikal;
	}
	
	
	
	@GET
	@Path("/top10")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Artikal> getTop10() 
	{
		ArtikalDAO dao = (ArtikalDAO) ctx.getAttribute("artikalDAO");
		return dao.top10collection();
	}
	
	
	
}
