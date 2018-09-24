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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Comparator;

import beans.Artikal;
import beans.Restoran;
import beans.User;

public class ArtikalDAO 
{


	public static HashMap<Integer, Artikal> artikli = new HashMap<Integer, Artikal>();


	public ArtikalDAO(String contextPath)
	{
		loadArticles(contextPath);
	}
	
	private void loadArticles(String contextPath) 
	{
		BufferedReader in = null;
		try {
			File file = new File(contextPath + "/data/artikli.csv");
			FileInputStream fis = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(fis,"UTF-8");
			in = new BufferedReader(isr);
			
			String line, ime = "", tip = "", restoran_maticni = "";
			int kolicina=0,id=0,id_restorana=0, gramaza = 0, visible = 1;
			double cena=0;
			Restoran maticni = null;
			
			StringTokenizer st;
			
			while ((line = in.readLine()) != null) {
				line = line.trim();
				if (line.equals("") || line.indexOf('#') == 0)
					continue;
				st = new StringTokenizer(line, ",");
				
				System.out.println("* * * NEW LINE ARTIKAL: ");
				
				while (st.hasMoreTokens()) 
				{
					
					id = Integer.parseInt(st.nextToken().trim());
					System.out.println("ID: " + id);
					
					id_restorana = Integer.parseInt(st.nextToken().trim());
					System.out.println("ID_RESTORANA: " + id_restorana);

					maticni = RestoranDAO.findRestoran(id_restorana);
					restoran_maticni = maticni.getIme();
					
					ime = st.nextToken().trim();
					System.out.println("IME: " + ime);

					
					tip = st.nextToken().trim();
					System.out.println("TIP: " + tip);

					kolicina = Integer.parseInt(st.nextToken().trim());
					System.out.println("KOLICINA: " + kolicina);
					
					cena = Double.parseDouble(st.nextToken().trim());
					System.out.println("CENA: " + cena);
					
					
					gramaza = Integer.parseInt(st.nextToken().trim());
					System.out.println("GRAMAZA: " + gramaza);
					
					visible = Integer.parseInt(st.nextToken().trim());
					System.out.println("VISIBLE: " + visible);

				}
				
				if(visible == 1)
				{
					Artikal a = new Artikal(tip, ime, kolicina, cena,id,id_restorana,restoran_maticni,gramaza);
					artikli.put(id, a);
					maticni.getArtikli_restorana().add(a);
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
	
	public Collection<Artikal> findAll() 
	{
		return artikli.values();
	}
	
	public static Artikal findArtikal(int id)
	{
		return artikli.containsKey(id) ? artikli.get(id) : null;
	}

	public synchronized void remove(Artikal artikal, String realPath) 
	{
		Path path = Paths.get(realPath + "/data/artikli.csv");
		List<String> fileContent;
		
		try
		{
			fileContent = new ArrayList<>(Files.readAllLines(path, StandardCharsets.UTF_8));
			for( int i = 0 ; i < fileContent.size() ; i ++ )
			{
				String[] lines_array = fileContent.get(i).split(",");
				if(lines_array[0].equals("")) continue;
				
				int id = Integer.parseInt(lines_array[0]);
				if(id == artikal.getId())
				{
					fileContent.remove(i);
					break;
				}
			}
			Files.write(path, fileContent, StandardCharsets.UTF_8);
			artikli.remove(artikal.getId());
		}
		catch(Exception e)
		{
			System.out.println("err: " + e);
		}
	}

	public synchronized void modify(Artikal artikal, String realPath) 
	{
		Path path = Paths.get(realPath + "/data/artikli.csv");
		List<String> fileContent;
		
		try
		{
			fileContent = new ArrayList<>(Files.readAllLines(path, StandardCharsets.UTF_8));
			
			for( int i = 0 ; i < fileContent.size() ; i ++ )
			{
				String[] lines_array = fileContent.get(i).split(",");
				if(lines_array[0].equals("")) continue;
				
				int id = Integer.parseInt(lines_array[0]);
				if(id == artikal.getId())
				{
					String newArtikal = artikal.getId() + "," + artikal.getId_restorana() + "," +
							artikal.getIme() + "," + artikal.getTip() + "," + artikal.getKolicina() + "," + 
							artikal.getCena() + "," + artikal.getGramaza() + "," + artikal.getVisible();
					
					fileContent.set(i, newArtikal);
				}
				else
				{
					continue;
				}
			}
			Files.write(path, fileContent, StandardCharsets.UTF_8);
			artikli.put(artikal.getId(), artikal);
		}
		catch(Exception e)
		{
			System.out.println("err: " + e);
		}
		
		
	}
	
	public int getNextId() 
	{
		Set<Integer> keys = artikli.keySet();
		
		int max = -1;
		for(int key : keys)
		{
			if(key >= max)
				max = key;
		}
		
		max++;
		return max;
	}

	public synchronized void add(Artikal artikal, String realPath) 
	{
		Path path = Paths.get(realPath + "/data/artikli.csv");
		List<String> fileContent;
		
		try
		{
			fileContent = new ArrayList<>(Files.readAllLines(path, StandardCharsets.UTF_8));
			String newArtikal = artikal.getId() + "," + artikal.getId_restorana() + "," +
					artikal.getIme() + "," + artikal.getTip() + "," + 0 + "," + 
					artikal.getCena() + "," + artikal.getGramaza() + "," + "1";
			
			fileContent.add(newArtikal);
			
			Files.write(path, fileContent, StandardCharsets.UTF_8);
			artikli.put(artikal.getId(), artikal);
		}
		catch(Exception e)
		{
			System.out.println("err: " + e);
		}
	}

	public HashMap<Integer, Artikal> getArtikli() {
		return artikli;
	}

	public void setArtikli(HashMap<Integer, Artikal> artikli) {
		this.artikli = artikli;
	}
	
	
	
	public void setPopolarity(List<Artikal> input, String context_path)
	{
		for( int i = 0 ; i < input.size() ; i ++ )
		{
			Artikal target = findArtikal(input.get(i).getId());
			target.setKolicina(target.getKolicina()+1);
			
			modify(target, context_path);
		}
	}
	
	public ArrayList<Artikal> top10collection()
	{
			
		ArrayList<Artikal> all = new ArrayList<Artikal>(artikli.values());
		
		ArrayList<Artikal> top10 = 	new ArrayList<Artikal>();
		
		
		
		Collections.sort(all,new Comparator<Artikal>() {
			//
			public int compare(Artikal s1, Artikal s2)
			{
				return Integer.valueOf(s2.getKolicina()).compareTo(s1.getKolicina());
			}
			
		});
		
		System.out.println("---------SORT: ");
		for( int j = 0 ; j < all.size() ; j ++ )
		{
			System.out.println(all.get(j).getKolicina());
		}
		
		if(all.size() <= 10)
			return all;
		else
		{
			for( int j = 0 ; j < 10 ; j ++ )
			{
				top10.add(all.get(j));
			}
		
			return top10;
		}
	}

	
}

