package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import beans.Artikal;
import beans.DeliveryGuy;
import beans.Order;
import beans.Restoran;
import beans.User;

public class OrderDAO 
{

	private HashMap<Integer, Order> orders = new HashMap<Integer, Order>();
	private HashMap<Integer,Integer> userToken = new HashMap<Integer,Integer>();

	




	public OrderDAO(String contextPath)
	{
		
		System.out.println("*****************************daj");

		loadOrders(contextPath);
	}
	
	public void loadOrders(String contextPath)
	{
		
		BufferedReader in = null;
		try
		{

			File file = new File(contextPath + "/data/orders.csv");
			FileInputStream fis = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(fis,"UTF-8");
			in = new BufferedReader(isr);
			String line;
			StringTokenizer st;
			
			int id = 0, id_user = 0, id_dostavljac = 0, stanje = 0, visible = 1;
			String dateTime = "";
			double total_cena = 0;
			int tokens = 0;
			String napomena = "";
			List<String> artikli = new ArrayList<String>();
			List<Artikal> artikli_objs = new ArrayList<Artikal>();
			
			while((line = in.readLine()) != null)
			{
				line = line.trim();
				if(line.equals("x"))
					continue;
				st = new StringTokenizer(line,",");
				System.out.println("NEW ORDER: ");
				while(st.hasMoreTokens())
				{
					id = Integer.parseInt(st.nextToken().trim());
					System.out.println("ID: " + id);

					id_user = Integer.parseInt(st.nextToken().trim());
					System.out.println("USER: " + id_user);
					
					id_dostavljac = Integer.parseInt(st.nextToken().trim());
					System.out.println("DOSTAVLJAC: " + id_dostavljac);
					
					stanje = Integer.parseInt(st.nextToken().trim());
					System.out.println("STANJE: " + stanje);
					
					dateTime = st.nextToken();
					System.out.println("DATE: " + dateTime);

					total_cena = Double.parseDouble(st.nextToken().trim());
					System.out.println("CENA: " + total_cena);
					
					tokens = Integer.parseInt(st.nextToken().trim());
					System.out.println("TOKENS: " + tokens);
					
					napomena = st.nextToken();
					System.out.println("NAPOMENA: " + napomena);
					
					System.out.println("***************** ucitana napomena");
					
					artikli = getArtikli(st.nextToken());
					
					System.out.println("***************** ucitani artikli");
					visible = Integer.parseInt(st.nextToken().trim());
					
					System.out.println("***************** ucit visible");
					
					System.out.println("VISIBLE: " + visible);
					
					artikli_objs = getArtikli_objs(st.nextToken().trim());
					
					System.out.println("***************** ucitana lista artikala");
					
					
				}
				if(visible == 1)
				{
					System.out.println("*****************ubacio order u hashmapu");
					Order order = new Order(id, id_user, id_dostavljac, stanje, dateTime, total_cena, tokens, napomena, artikli, visible, artikli_objs);
					orders.put(id, order);
				}
			}

		}
		catch(Exception e)
		{
			
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
	
	
	//UPIS NOVE PORUDZBINE 
	public synchronized void addOrder(Order order, String context_path)
	{
		PrintWriter out = null;
		
		if(order.getNapomena() == null || order.getNapomena().equals(""))
		{
			order.setNapomena("null");
		}
		
		try
		{
			File file = new File(context_path + "/data/orders.csv");
			FileOutputStream fos = new FileOutputStream(file, true);
			
			//da bi latinicu mogao da upise 
			OutputStreamWriter osw = new OutputStreamWriter(fos,"UTF-8");
			
			BufferedWriter bw  = new BufferedWriter(osw);
			out = new PrintWriter(bw);	
			
			String appendingOrder = order.getId() + "," + order.getUser() + "," +
			order.getDostavljac_id() + "," + order.getStanje() + "," + order.getDateTime_order()+ "," + order.getTotal_cena() +
			"," + order.getUsed_tokens() + "," + order.getNapomena() + "," + printArtikli(order.getArtikli()) + "," + order.getVisible() + "," + PrintObjs(order.getArtikli_objs()) +"\n"; 
		
			
			System.out.println("****************************NEW ORDER: " + appendingOrder);
			
			out.print(appendingOrder);
			
			
			orders.put(order.getId(), order);
		}
		catch(Exception e)
		{
			
		}
		finally 
		{
			if ( out != null ) {
				try {
					out.close();
				}
				catch (Exception e) 
				{ 
					
				}	
			}
		}
	}
	
	//IZMENA POSTOJECEG ORDERA
	public synchronized void modifyOrder(Order order, String context_path)
	{
		Path path = Paths.get(context_path + "/data/orders.csv");
		
		List<String> fileContent;
		try {
			fileContent = new ArrayList<>(Files.readAllLines(path, StandardCharsets.UTF_8));
			
			if(order.getNapomena() == null || order.getNapomena().equals(""))
			{
				order.setNapomena("null");
			}
			
			for (int i = 0; i < fileContent.size(); i++)
			{
				
				
				String[] lines_array = fileContent.get(i).split(",");
				
				System.out.println("TRENUTNA: " + fileContent.size());
				
				int id = 0;
				
				try 
				{
					id = Integer.parseInt(lines_array[0]);
				}
				catch(Exception e)
				{
					
					System.out.println("err: " + e);
					continue;
				}
					
				System.out.println("--------------CITAM LINIJU");
				
				if(id == order.getId())
				{
					
						String newOrder = "";
						newOrder = order.getId() + "," + order.getUser() + "," + order.getDostavljac_id() + 
						"," + order.getStanje() + "," + order.getDateTime_order() + "," + order.getTotal_cena() + 
						"," + order.getUsed_tokens() + "," + order.getNapomena() + ",";
						
						
						
						for ( int j = 0 ; j < order.getArtikli().size() ; j ++ )
						{
							newOrder += order.getArtikli().get(j);
							if(j == order.getArtikli().size() - 1)
								break;
							newOrder += ";";
						}
						
						newOrder += "," + order.getVisible() + "," + PrintObjs(order.getArtikli_objs());
					
						System.out.println(newOrder);

						
						fileContent.set(i, newOrder);
						break;
				 }
				else
				{
			     		continue;
				}
			}
			    
			    
			Files.write(path, fileContent, StandardCharsets.UTF_8);
			
			orders.put(order.getId(), order);
	
		} catch (Exception e) 
		{
			System.out.println("err: " + e);
		}
	
	}
	
	
	
	public List<String> getArtikli(String input)
	{
		String output[] = input.split(";");
		
		List<String> retVal = Arrays.asList(output);
		
		return retVal;
	}	
	public String printArtikli(List<String> input)
	{
		String retVal = "";
		
		for( int i = 0 ; i < input.size() ; i ++ )
		{
			retVal += input.get(i);
			if(i == input.size() - 1)
				break;
			retVal += ";";
		}
		return retVal;
	}
	
	public List<Artikal> getArtikli_objs(String input)
	{
		List<Artikal> retVal = new ArrayList<Artikal>();
		
		String [] arts = input.split(";");
		for( int i = 0 ; i < arts.length ; i ++ )
		{
			String [] object_nmbr = arts[i].split("-");
			int artikal_id = Integer.parseInt(object_nmbr[0]);
			int kolicina = Integer.parseInt(object_nmbr[1]);
			
			Artikal artikal = ArtikalDAO.findArtikal(artikal_id);
			artikal.setKolicina(kolicina);
			
			retVal.add(artikal);
		}
		
		return retVal;
	}
	
	public String PrintObjs(List<Artikal> input)
	{
		String retVal = "";
		
		for( int i = 0 ; i < input.size() ; i ++ )
		{
			retVal += input.get(i).getId() + "-" + input.get(i).getKolicina();
			if(i == input.size() - 1)
				break;
			retVal += ";";
		}
		
		
		System.out.println("************ono sto mi treba: " + retVal);
		
		return retVal;
	}
	
	
	
	
	
	

	public Order findOrder(int id)
	{
		return orders.containsKey(id) ? orders.get(id) : null;
	}
	
	
	//F-JA ZA GENERISANJE ORDER_ID NA OSNOVU POSTOJECE MAPE ORDERA;
	public int gimmieNextOrderId()
	{
				
		Set<Integer> keys = orders.keySet();
		
		int max = -1;
		for(int key : keys)
		{
			if(key >= max)
				max = key;
		}
		
		max++;
		return max;
		
	}
	
	public Collection<Order> findAll() 
	{
		return orders.values();
	}
	

	public HashMap<Integer, Order> getOrders() {
		return orders;
	}

	public void setOrders(HashMap<Integer, Order> orders) {
		this.orders = orders;
	}
	
	
	public HashMap<Integer, Integer> getUserToken() {
		return userToken;
	}

	public void setUserToken(HashMap<Integer, Integer> user_token) {
		this.userToken = user_token;
	}
	
}
