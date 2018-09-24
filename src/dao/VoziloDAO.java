package dao;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import beans.Vozilo;

public class VoziloDAO {

	private HashMap<Integer,Vozilo> vozila = new HashMap<Integer,Vozilo>();
	



	public VoziloDAO(String context_path)
	{
		loadVozila(context_path);
	}
	
	private void loadVozila(String context_path)
	{
		Path path = Paths.get(context_path + "/data/vozila.csv");
		
		List<String> fileContent;
		try
		{
			fileContent = new ArrayList<>(Files.readAllLines(path,StandardCharsets.UTF_8));
			
			
			int visible = 0, id = 0, type = 0, godiste = 0, zauzeto = 0;
			String marka = "",registracija="",napomena="",model = "";
			
			for (int i = 0; i < fileContent.size(); i++)
			{
				System.out.println("- - - " + fileContent.get(i));
				
				String [] splitted_line = fileContent.get(i).split(",");
				
				visible = Integer.parseInt(splitted_line[0]);
				System.out.println(splitted_line[0]);
				
				id = Integer.parseInt(splitted_line[1]);
				System.out.println(splitted_line[1]);
				
				zauzeto = Integer.parseInt(splitted_line[2]);
				System.out.println(splitted_line[2]);
				
				marka = splitted_line[3];
				System.out.println(splitted_line[3]);
				
				model = splitted_line[4];
				System.out.println(splitted_line[4]);
				
				type = Integer.parseInt(splitted_line[5]);
				System.out.println(splitted_line[5]);
				
				registracija = splitted_line[6];
				System.out.println(splitted_line[6]);
				
				godiste = Integer.parseInt(splitted_line[7]);
				System.out.println(splitted_line[7]);
				
				napomena = splitted_line[8];
				System.out.println(splitted_line[8]);
				
				Vozilo vozilo = new Vozilo(visible,id,zauzeto,marka,model,type,registracija,godiste,napomena);
				if(vozilo.getVisible() == 1)
					vozila.put(id, vozilo);
				System.out.println("Success!");
				
			}
			
			
		}
		catch (Exception e)
		{
			
		}
	}
	
	public synchronized void modifyVehicle(Vozilo vozilo, String context_path)
	{
		Path path = Paths.get(context_path + "/data/vozila.csv");
		
		List<String> fileContent;
		try
		{
			fileContent = new ArrayList<>(Files.readAllLines(path,StandardCharsets.UTF_8));
			
			
			int id = 0;
			
			for (int i = 0; i < fileContent.size(); i++)
			{
				String [] splitted_line = fileContent.get(i).trim().split(",");
				
				id = Integer.parseInt(splitted_line[1]);
				
				if(id == vozilo.getId())
				{
					String newVehicle = vozilo.getVisible() + "," + vozilo.getId() + "," + vozilo.getZauzeto() + "," +
							vozilo.getMarka() + "," + vozilo.getModel() + "," + vozilo.getType() + "," + vozilo.getRegistracija() + "," + 
				  vozilo.getGodinaProzivodnje() + "," + vozilo.getNapomena();
					
					fileContent.set(i, newVehicle);
				}
				else
				{
					continue;
				}
				
			}
			Files.write(path, fileContent, StandardCharsets.UTF_8);
			vozila.put(vozilo.getId(), vozilo);
			
		}
		catch (Exception e)
		{
			
		}
	}
	
	public synchronized void add(Vozilo vozilo, String realPath)
	{
		Path path = Paths.get(realPath + "/data/vozila.csv");
		List<String> fileContent;
		
		try
		{
			fileContent = new ArrayList<>(Files.readAllLines(path, StandardCharsets.UTF_8));

			String newVozilo = 1 + "," + vozilo.getId() + "," + 0 + "," +
					vozilo.getMarka() + "," + vozilo.getModel() + "," + vozilo.getType() + "," + vozilo.getRegistracija() + "," + 
		  vozilo.getGodinaProzivodnje() + "," + vozilo.getNapomena();

			
			fileContent.add(newVozilo);
			
			Files.write(path, fileContent, StandardCharsets.UTF_8);
			vozila.put(vozilo.getId(), vozilo);
		}
		catch(Exception e)
		{
			System.out.println("err: " + e);
		}
	}
	
	public int getNextId() 
	{
		Set<Integer> keys = vozila.keySet();
		
		int max = -1;
		for(int key : keys)
		{
			if(key >= max)
				max = key;
		}
		
		max++;
		return max;
	}
	
	public Collection<Vozilo> findAll()
	{
		return vozila.values();
	}
	
	public Vozilo findVozilo(int id)
	{
		return vozila.containsKey(id) ? vozila.get(id) : null;
	}
	
	public HashMap<Integer, Vozilo> getVozila() {
		return vozila;
	}

	public void setVozila(HashMap<Integer, Vozilo> vozila) {
		this.vozila = vozila;
	}
	
}
