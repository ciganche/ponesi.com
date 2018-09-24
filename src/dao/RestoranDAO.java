package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import beans.Restoran;


public class RestoranDAO {
	
	
	private static HashMap<Integer, Restoran> restorani = new HashMap<Integer, Restoran>();
	
	



	public RestoranDAO(String contextPath)
	{
		loadProducts(contextPath);
	}

	
	public Collection<Restoran> findAll() 
	{
		return restorani.values();
	}
	
	
	public static Restoran findRestoran(int id) 
	{
		return restorani.containsKey(id) ? restorani.get(id) : null;
	}
	
	
	
	
	private void loadProducts(String contextPath) 
	{
		BufferedReader in = null;
		try {
			File file = new File(contextPath + "/data/restorani.csv");
			System.out.println(file.getCanonicalPath());
			FileInputStream fis = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(fis,"UTF-8");
			in = new BufferedReader(isr);
			String line, id = "", ime = "", ulica = "", broj_ulice_str = "", tip_str = "";
			StringTokenizer st;
			int visible = 1;
			while ((line = in.readLine()) != null) {
				line = line.trim();
				if (line.equals("") || line.indexOf('#') == 0)
					continue;
				st = new StringTokenizer(line, ",");
				
				System.out.println("* * * NEW LINE: ");
				
				while (st.hasMoreTokens()) 
				{
					
					id = st.nextToken().trim();
					System.out.println("ID: " + id);

					
					ime = st.nextToken().trim();
					System.out.println("IME: " + ime);

					
					ulica = st.nextToken().trim();
					System.out.println("ULICA: " + ulica);

					broj_ulice_str = st.nextToken().trim();
					System.out.println("BROJ: " + broj_ulice_str);

					tip_str = st.nextToken().trim();
					System.out.println("Tip stringic: " + tip_str);

					visible = Integer.parseInt(st.nextToken().trim());
					

				}
				if(visible == 1)
				{
					restorani.put(Integer.parseInt(id), new Restoran(id, ime, ulica, broj_ulice_str, tip_str));
				}
			}
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		finally 
		{
			if ( in != null ) {
				try {
					in.close();
				}
				catch (Exception e) 
				{ 
					
				}	
			}
		}
		
	}
	
	public synchronized void modify(Restoran restoran, String contexPath)
	{
		Path path = Paths.get(contexPath + "/data/restorani.csv");
		List<String> fileContent;
		
		try
		{
			fileContent = new ArrayList<>(Files.readAllLines(path, StandardCharsets.UTF_8));
			
			for (int i = 0; i < fileContent.size(); i++)
			{

				String[] lines_array = fileContent.get(i).split(",");
				if(lines_array[0].equals("")) continue;
				
				int id = Integer.parseInt(lines_array[0]);

				if(id == restoran.getId())
				{
					String newRestoran = restoran.getId() + "," + restoran.getIme() + "," +
							restoran.getUlica() + "," + restoran.getBroj_ulice() + "," + restoran.getTip() + "," + restoran.getVisible();
					System.out.println("***" + newRestoran);
					
					fileContent.set(i, newRestoran);
				}
				else
				{
					continue;
				}
			}
			Files.write(path, fileContent, StandardCharsets.UTF_8);
			restorani.put(restoran.getId(), restoran);
		}
		catch(Exception e)
		{ 
			System.out.println("err:" + e);
		}
	}
	
	public synchronized void remove(Restoran restoran, String contexPath)
	{
		Path path = Paths.get(contexPath + "/data/restorani.csv");
		List<String> fileContent;
		
		try
		{
			fileContent = new ArrayList<>(Files.readAllLines(path, StandardCharsets.UTF_8));
			for( int i = 0 ; i < fileContent.size() ; i ++ )
			{
				String[] lines_array = fileContent.get(i).split(",");
				if(lines_array[0].equals("")) continue;
				
				int id = Integer.parseInt(lines_array[0]);
				if(id == restoran.getId())
				{
					fileContent.remove(i);
					break;
				}
			}
			
			Files.write(path, fileContent, StandardCharsets.UTF_8);
			restorani.remove(restoran.getId());
		}
		catch(Exception e)
		{
			System.out.println("err: " + e);
		}
	}


	public int getNextId() 
	{
		Set<Integer> keys = restorani.keySet();
		
		int max = -1;
		for(int key : keys)
		{
			if(key >= max)
				max = key;
		}
		
		max++;
		return max;
	}


	public synchronized void add(Restoran restoran, String realPath) 
	{
		
		Path path = Paths.get(realPath + "/data/restorani.csv");
		List<String> fileContent;
		
		try
		{
			fileContent = new ArrayList<>(Files.readAllLines(path, StandardCharsets.UTF_8));
			
			String newRestoran = restoran.getId() + "," + restoran.getIme() + "," +
					restoran.getUlica() + "," + restoran.getBroj_ulice() + "," + restoran.getTip() + "," + 1;
			
			fileContent.add(newRestoran);
			
			Files.write(path, fileContent, StandardCharsets.UTF_8);
			restorani.put(restoran.getId(), restoran);
		}
		catch(Exception e)
		{
			System.out.println("err: " + e);
		}
	}
	
	
	public synchronized void removeFromArtikli(int id_restorana, int id_artikla)
	{
		Restoran restoran = findRestoran(id_restorana);
		
		for( int i = 0 ; i < restoran.getArtikli_restorana().size() ; i ++ )
		{
			if(restoran.getArtikli_restorana().get(i).getId() == id_artikla)
			{
				restoran.getArtikli_restorana().remove(i);
				break;
			}
		}
	}
	
	
	public static HashMap<Integer, Restoran> getRestorani() {
		return restorani;
	}


	public static void setRestorani(HashMap<Integer, Restoran> restorani) {
		RestoranDAO.restorani = restorani;
	}

}
