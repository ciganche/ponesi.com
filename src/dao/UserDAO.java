package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Arrays;
import java.util.Collection;

import beans.Admin;
import beans.Artikal;
import beans.DeliveryGuy;
import beans.Restoran;
import beans.User;
import beans.Order;
import beans.HistoryOrder;

public class UserDAO {

	
	private HashMap<String, User> users = new HashMap<String, User>();
	private HashMap<String, DeliveryGuy> deliveryGuys = new HashMap<String, DeliveryGuy>();
	private HashMap<String, Admin> admins = new HashMap<String,Admin>();
	
	//private HashMap<String, Admin> admins = new HashMap<String, Admin>();

	public UserDAO(String context_path)
	{
		loadUsers(context_path);
	}
	
	public HashMap<String, User> getUsers() {
		return users;
	}

	public void setUsers(HashMap<String, User> users) {
		this.users = users;
	}
	
	public HashMap<String, DeliveryGuy> getDeliveryGuys() {
		return deliveryGuys;
	}

	public void setDeliveryGuys(HashMap<String, DeliveryGuy> deliveryGuys) {
		this.deliveryGuys = deliveryGuys;
	}
	
	public void loadUsers(String contextPath)
	{
		BufferedReader in = null;
		try {
			File file = new File(contextPath + "/data/users.csv");
			
			FileInputStream fis = new FileInputStream(file);
			
			InputStreamReader isr = new InputStreamReader(fis,"UTF-8");
			
			in = new BufferedReader(isr);
			
			String line,username = "", password = "", ime = "", prezime = "", telefon = "", mejl = "", date_str="", history_path="";
			int id = 0;
			int type=0;
			
			//KARAKTERISTIKE KORISNIKA
			int tokens = 0;
			List<HistoryOrder> history = null;
			List<Integer> pinned_restaurants = new ArrayList<Integer>();
			StringTokenizer st;
			
			//KARAKTERISTIKE DOSTAVLJACA
			List<Integer> orders_id = new ArrayList<Integer>();
			int vozilo_id = 0;
			
			while ((line = in.readLine()) != null) {
				line = line.trim();
				if (line.equals("") || line.indexOf('#') == 0)
					continue;
				st = new StringTokenizer(line, ",");
				
				System.out.println("* * * NEW LINE USER: ");
				
				while (st.hasMoreTokens()) 
				{
					
					//SVE ZAJEDNICKO ZA SVE TIPOVE KORISNIKA
					id = Integer.parseInt(st.nextToken().trim());
					System.out.println("ID: " + id);
					
					
					
					username = st.nextToken().trim();
					System.out.println("USERNAME: " + username);

					
					password = st.nextToken().trim();
					System.out.println("PASSWORD: " + password);
					
					ime = st.nextToken().trim();
					System.out.println("IME: " + ime);
					
					prezime = st.nextToken().trim();
					System.out.println("PRZ: " + prezime);

					telefon = st.nextToken().trim();
					System.out.println("TELEFON: " + telefon);

					mejl = st.nextToken().trim();
					System.out.println("MEJL: " + mejl);
					
					date_str = st.nextToken().trim();
					System.out.println("REGISTRATION DATE: " + date_str);
	
					
					//NA OSNOVU TYPE ODREDJUJEM GDE SMESTAM 
					type = Integer.parseInt(st.nextToken().trim());

					
					//KARAKTERISTICNI ATRIBUTI KORISNIKA
					if(type == 0)
					{
						tokens = Integer.parseInt(st.nextToken().trim());
	
						//pisace "x" umesto sadrzaja liste ako je lista prazna
						history_path = st.nextToken().trim();
						history = getHistory(contextPath + "/data/userlogs/" + history_path);
						
						
						//dodati x za praznu listu
						String temp_str = st.nextToken().trim();
						if(temp_str.equals("x"))
						{
							
						}
						else
						{
							pinned_restaurants = getPinnedRestaurants(temp_str);
						}
							
					}
					
					//KARAKTERISTICNI ATRIBUTI DOSTAVLJACA
					if(type==1)
					{
						vozilo_id = Integer.parseInt(st.nextToken().trim());
						
						String temp_str = st.nextToken().trim();
						if(!temp_str.equals("x"))
							orders_id = getPinnedRestaurants(temp_str); //ISTI JE NACIN ZAPISIVANJA pinnovanih RESTORANA i ID-EVA NARUDZBINA


					}
					if(type == 2)
					{
						//UCITAVANJE ADMINA
					}
					
					
				}
				if(type == 0)
				{
					User user = new User(id, username, password, ime, prezime, telefon, mejl, date_str, type, tokens, history, pinned_restaurants) ;
					users.put(username, user);
					System.out.println("* * * Gotov user.");

				}
				if(type == 1)
				{
					DeliveryGuy deliveryGuy = new DeliveryGuy (username, password, ime, prezime, telefon, mejl, id, date_str, type, orders_id, vozilo_id);  
					deliveryGuys.put(username, deliveryGuy);
					System.out.println("* * * Gotov deliveryguy.");

				}
				if(type == 2)
				{
					Admin admin = new Admin(id, username, password, ime, prezime, telefon, mejl, date_str, type);
					admins.put(username, admin);
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
	
	public synchronized void remove_any_type(int id, String username, String realPath)
	{
		Path path = Paths.get(realPath + "/data/users.csv");
		List<String> fileContent;
		
		try
		{
			fileContent = new ArrayList<>(Files.readAllLines(path, StandardCharsets.UTF_8));
			
			for( int i = 0 ; i < fileContent.size() ; i ++ )
			{
				
				
				String [] lines_array = fileContent.get(i).split(",");
				if(lines_array[0].equals(""))
					continue;
				
				int user_id = Integer.parseInt(lines_array[0]);
				
				if(id == user_id)
				{
					fileContent.remove(i);
					break;
				}
			}
			
			Files.write(path, fileContent, StandardCharsets.UTF_8);
			
			users.remove(username);
			deliveryGuys.remove(username);
			admins.remove(username);
			
			
		}
		catch(Exception e)
		{
			System.out.println("err: " + e);
		}
	}
	
	//OBICAN KORISNIK
	public User findUser(String username)
	{
		return users.containsKey(username) ? users.get(username) : null;
	}
	
	public User findUserById(int id)
	{
		User retVal = null;
		Set<String> keys = users.keySet();
		
		for(String key : keys)
		{
			User temp = findUser(key);
			if(temp.getId() == id)
				retVal = temp;
		}	
		
		return retVal;
	}
	
	//DELIVERY GUY
	public DeliveryGuy findDeliveryGuy(String username)
	{
		return deliveryGuys.containsKey(username) ? deliveryGuys.get(username) : null;
	}
	
	public DeliveryGuy findDeliveryGuyById(int id)
	{
		DeliveryGuy retVal = null;
		Set<String> keys = deliveryGuys.keySet();
		
		for(String key : keys)
		{
			DeliveryGuy temp = findDeliveryGuy(key);
			if(temp.getId() == id)
				retVal = temp;
		}	
		
		return retVal;
	}
	
	//ADMIN
	public Admin findAdmin(String username)
	{
		return admins.containsKey(username) ? admins.get(username) : null;
	}
	
	public Admin findAdminById(int id)
	{
		Admin retVal = null;
		Set<String> keys = admins.keySet();
		
		for(String key : keys)
		{
			Admin temp = findAdmin(key);
			if(temp.getId() == id)
				retVal = temp;
		}	
		
		return retVal;
	}
	
	public List<HistoryOrder> getHistory(String file_path)
	{
		List<HistoryOrder> retVal = new ArrayList<HistoryOrder>();

		BufferedReader in = null;
		try {
			File file = new File(file_path);
			FileInputStream fis = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(fis,"UTF-8");
			in = new BufferedReader(isr);
			
			String line;
			StringTokenizer st;
			
			while ((line = in.readLine()) != null) 
			{
				
								
				line = line.trim();
				
				if(line.equals("x"))
					return retVal;
				
				
				if (line.equals("") || line.indexOf('#') == 0)
					continue;
				
				st = new StringTokenizer(line, ",");
				
				String dateTime="";
				List<String> orderInfos = null;
				double total_cena=0;
				
				while (st.hasMoreTokens()) 
				{			
					dateTime = st.nextToken().trim();			
					
					total_cena = Double.parseDouble(st.nextToken().trim());
					
					orderInfos = getOrderInfos(st.nextToken().trim());			
				}			
				retVal.add(new HistoryOrder(dateTime,total_cena,orderInfos));
			}
			
			return retVal;
			
		} catch (Exception e) 
		{
			e.printStackTrace();
			return null;
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
	
	
	public List<Integer> getPinnedRestaurants(String input)
	{
		
		List<Integer> retVal = new ArrayList<Integer>();
		String output[] = input.split(";");
		
		for( int i = 0 ; i < output.length ; i++ )
		{
			retVal.add(Integer.parseInt(output[i]));
		}
		
		
		return retVal;
		
	}
	
	
	public List<String> getOrderInfos(String input)
	{
				
		String output[] = input.split(";");
		
		List<String> retVal = Arrays.asList(output);
		
		return retVal;
	}
	
	//UPIS NOVOG KORISNIKA U USERS.CSV
	public synchronized void addUser(User user, String context_path)
	{
		PrintWriter out = null;
		try
		{
			//DRUGI PARAMETAR NA FileWriteru OZNACAVA APPENDOVANJE
			File file = new File(context_path + "/data/users.csv");
			FileOutputStream fos = new FileOutputStream(file, true);
			
			//da bi latinicu mogao da upise 
			OutputStreamWriter osw = new OutputStreamWriter(fos,"UTF-8");
			
			BufferedWriter bw  = new BufferedWriter(osw);
			out = new PrintWriter(bw);
			
			
			String appendingUser = "\n" + user.getId() + "," + user.getUsername() + "," +
					user.getPassword() + "," + user.getName() + "," + user.getLastname() + "," +
					user.getTel() + "," + user.getEmail() + "," + user.getRegistration_date() + "," + 
					user.getUser_type() + "," + user.getTokens() + "," + "history"+user.getId()+".csv" + "," + "x";
	
			
			overWriteHistoryLog(context_path, user);	
			
			out.print(appendingUser);
			
			users.put(user.getUsername(), user);
			
		}
		catch (Exception e) 
		{
			e.printStackTrace();
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
	
	public synchronized void addDeliveryGuy(DeliveryGuy deliveryGuy, String context_path)
	{
		Path path = Paths.get(context_path + "/data/users.csv");
		List<String> fileContent;
		
		try
		{
			fileContent = new ArrayList<>(Files.readAllLines(path, StandardCharsets.UTF_8));
			
			String newDeliveryGuy = deliveryGuy.getId() + "," + deliveryGuy.getUsername() + "," +
					deliveryGuy.getPassword() + "," + deliveryGuy.getName() + "," + 
					deliveryGuy.getLastname() + "," + deliveryGuy.getTel() + "," + 
					deliveryGuy.getEmail() + "," + deliveryGuy.getRegistration_date() + "," + 
					deliveryGuy.getUser_type() + "," + deliveryGuy.getVoziloId() + "," + "x";
		
		
			fileContent.add(newDeliveryGuy);
			
			Files.write(path, fileContent, StandardCharsets.UTF_8);
			deliveryGuys.put(deliveryGuy.getUsername(), deliveryGuy);
		}
		catch(Exception e)
		{
			System.out.println("err: " + e);
		}
	}
	
	
	public synchronized void modifyDeliveryGuy(DeliveryGuy deliveryGuy, String context_path)
	{
		
		Path path = Paths.get(context_path + "/data/users.csv");
	
		List<String> fileContent;
		try {
			fileContent = new ArrayList<>(Files.readAllLines(path, StandardCharsets.UTF_8));
			
			
			for (int i = 0; i < fileContent.size(); i++)
			{
				
				
				String[] lines_array = fileContent.get(i).split(",");
				

				int id = Integer.parseInt(lines_array[0]);
					
					
				if(id == deliveryGuy.getId())
				{
						String newDeliveryGuy = "";
						newDeliveryGuy = deliveryGuy.getId() + "," + deliveryGuy.getUsername() + "," +
						deliveryGuy.getPassword() + "," + deliveryGuy.getName() + "," + deliveryGuy.getLastname() + 
						"," + deliveryGuy.getTel() + "," + deliveryGuy.getEmail() + "," + deliveryGuy.registration_date + 
						"," + deliveryGuy.getUser_type() + "," + deliveryGuy.getVoziloId() + ",";
						
						
						//UPIS ID-EVA DODELJENIH DOSTAVLJACU
						if(deliveryGuy.getOrder_id().size() == 0)
							newDeliveryGuy += "x";
						
						
						for ( int j = 0 ; j < deliveryGuy.getOrder_id().size() ; j ++ )
						{
							newDeliveryGuy += deliveryGuy.getOrder_id().get(j);
							if(j == deliveryGuy.getOrder_id().size() - 1)
								break;
							newDeliveryGuy += ";";
						}
					

						
						fileContent.set(i, newDeliveryGuy);
				 }
				else
				{
			     		continue;
				}
			}
			    
			    
			Files.write(path, fileContent, StandardCharsets.UTF_8);
			
			deliveryGuys.put(deliveryGuy.getUsername(), deliveryGuy);
	
		} catch (Exception e) 
		{

		}

	}
	
	
	//trazi liniju u kojoj se nazlazi postojeci korisnik i menja je
	public synchronized void modifyUser(User user, String context_path)
	{
		
		BufferedReader in = null;
		PrintWriter out = null;
		
		List<String> lines = new ArrayList<String>();

		try
		{
			File file = new File(context_path + "/data/users.csv");
			FileInputStream fis = new FileInputStream(file);
			
			InputStreamReader isr = new InputStreamReader(fis,"UTF-8");
			
			in = new BufferedReader(isr);
		
		
			String line;
			int id = 0;

			StringTokenizer st;
			
			
			while ((line = in.readLine()) != null) 
			{
				line = line.trim();
				if (line.equals("") || line.indexOf('#') == 0)
					continue;
				st = new StringTokenizer(line, ",");
				
				System.out.println("LINE: " + line);
								

					String a = st.nextToken();
					id = Integer.parseInt(a);

					if(id == user.getId())
					{
						//kreiranje nove linije:
						
						String changed_line = user.getId() + "," + user.getUsername() + "," +
						user.getPassword() + "," + user.getName() + "," + user.getLastname() + "," +
						user.getTel() + "," + user.getEmail() + "," + user.getRegistration_date() + "," + 
						user.getUser_type() + "," + user.getTokens() + "," + "history"+user.getId()+".csv" + ",";
						
						if(user.getSaved().size() == 0)
							changed_line += "x";
						
						for( int i = 0 ; i < user.getSaved().size() ; i ++ )
						{
							changed_line += user.getSaved().get(i);
							if(i == user.getSaved().size()-1)
								break;
							changed_line += ";";	
						}
						lines.add(changed_line);
						overWriteHistoryLog(context_path, user);	
					}
					else
					{
						lines.add(line);
					}
			}
		}
		catch (Exception e) 
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
		
		
		
		//ispis
		try
		{
			File file = new File(context_path + "/data/users.csv");			
			
			FileOutputStream fos = new FileOutputStream(file);
			
			//da bi latinicu mogao da upise 
			OutputStreamWriter osw = new OutputStreamWriter(fos,"UTF-8");
			
			BufferedWriter bw  = new BufferedWriter(osw);
			out = new PrintWriter(bw);
			
			
			System.out.println("PRINTING:");
			for( int i = 0 ; i < lines.size() ; i ++ )
			{
				System.out.println(lines.get(i));
				out.write(lines.get(i));
				out.write("\n");
			}		
		}
		catch (Exception e) 
		{
			e.printStackTrace();
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
		
		users.put(user.getUsername(),user);
	}
	
	public synchronized void overWriteHistoryLog(String context_path, User user)
	{
		PrintWriter out = null;
		try
		{
			File file = new File(context_path + "/data/userlogs/history" + user.getId() + ".csv");			
			
			FileOutputStream fos = new FileOutputStream(file);
			
			//da bi latinicu mogao da upise 
			OutputStreamWriter osw = new OutputStreamWriter(fos,"UTF-8");
			
			BufferedWriter bw  = new BufferedWriter(osw);
			out = new PrintWriter(bw);
			
			
			if(user.getHistory().size() == 0)
			{
				out.write("x");
			}
			for( int i = 0 ; i < user.getHistory().size() ; i ++ )
			{

				String line = user.getHistory().get(i).getDateTime() + "," + user.getHistory().get(i).getCost() + ",";
				
				for ( int j = 0 ; j < user.getHistory().get(i).getOrderInfos().size() ; j ++ )
				{
					line += user.getHistory().get(i).getOrderInfos().get(j);
					if(j == user.getHistory().get(i).getOrderInfos().size() - 1)
						break;
					line += ";";
				}
				
				out.write(line);
				out.write("\n");
			}		
		}
		catch (Exception e) 
		{
			e.printStackTrace();
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
	
	//nadji max id pa inc
	public int generateUserId()
	{
		int retVal;
		Set<String> keys = users.keySet();
		Set<String> deliveryKeys = deliveryGuys.keySet();
		Set<String> adminKeys = admins.keySet();
		
		int max = -1;
		for(String key : keys)
		{
			User temp = findUser(key);
			if(temp.getId() >= max)
				max = temp.getId();
		}	
		for(String key : deliveryKeys)
		{
			DeliveryGuy temp = this.findDeliveryGuy(key);
			if(temp.getId() >= max)
				max = temp.getId();
		}	
		for(String key : adminKeys)
		{
			Admin temp = this.findAdmin(key);
			if(temp.getId() >= max)
				max = temp.getId();
		}	
		
		
		
		
		retVal = ++max;
		
		return retVal;
		
	}
	
	
	public void printHash()
	{
		System.out.println("******************");
		
		Set<String> keys = users.keySet();
		for(String key : keys)
		{
			System.out.println("Username: " + key + " Id: " + findUser(key).getId());
		}
		
		System.out.println("******************");	
	}
	
	
	
	public HashMap<String, Admin> getAdmins() {
		return admins;
	}

	public void setAdmins(HashMap<String, Admin> admins) {
		this.admins = admins;
	}

	public Collection<User> findAllUsers() 
	{
		return users.values();
	}

	public Collection<DeliveryGuy> findAllDeliveryGuys() 
	{
		return deliveryGuys.values();
	}

	public Collection<Admin> findAllAdmins() 
	{
		return admins.values();
	}

	public synchronized void addAdmin(Admin admin, String realPath) 
	{
		Path path = Paths.get(realPath + "/data/users.csv");
		List<String> fileContent;
		
		try
		{
			fileContent = new ArrayList<>(Files.readAllLines(path, StandardCharsets.UTF_8));
			
			String newww = admin.getId() + "," + admin.getUsername() + "," +
					admin.getPassword() + "," + admin.getName() + "," + 
					admin.getLastname() + "," + admin.getTel() + "," + 
					admin.getEmail() + "," + admin.getRegistration_date() + "," + 
					admin.getUser_type();
		
		
			fileContent.add(newww);
			
			Files.write(path, fileContent, StandardCharsets.UTF_8);
			admins.put(admin.getUsername(), admin);
		}
		catch(Exception e)
		{
			System.out.println("err: " + e);
		}
		
	}
}
