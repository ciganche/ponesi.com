package beans;

import java.util.ArrayList;
import java.util.List;

public class DeliveryGuy
{
	public int id;
	public String username;
	public String password;
	
	public String name;
	public String lastname;
	public String tel;
	public String email;
	
	public String registration_date;
	public int user_type = 1; //0 - obican; 1 - vozac; 2 - admin

	//KARAKTERISTIKE SAMO DOSTAVLJACA
	public int voziloId;
	List<Integer> order_id;
	
	
	public DeliveryGuy()
	{
		
	}
	
	public DeliveryGuy(String username, String password)
	{
		this();
		this.username = username;
		this.password = password;
	}
	
	
	public DeliveryGuy(String username, String password, String name, String lastname, String tel, String email, int id,
			String registration_date, int user_type, List<Integer> tasks, int vozilo_id) {
		this();
		this.username = username;
		this.password = password;
		this.name = name;
		this.lastname = lastname;
		this.tel = tel;
		this.email = email;
		this.id = id;
		this.registration_date = registration_date;
		this.user_type = user_type;
		this.order_id = tasks;
		this.voziloId = vozilo_id;
	}
	
	
	//uvodjenje nogovg
	public DeliveryGuy(int id, String username, String password, String name, String lastname, String tel, String email, String registration_date)
	{
		this.id = id;
		this.username = username;
		this.password = password;
		this.name = name;
		this.lastname = lastname;
		this.tel = tel;
		this.email = email;
		this.registration_date = registration_date;
		//default
		this.user_type = 1;
		this.order_id = new ArrayList<Integer>();
		this.voziloId = -1;
	}



	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRegistration_date() {
		return registration_date;
	}

	public void setRegistration_date(String registration_date) {
		this.registration_date = registration_date;
	}

	public int getUser_type() {
		return user_type;
	}

	public void setUser_type(int user_type) {
		this.user_type = user_type;
	}
	
	public int getVoziloId() {
		return voziloId;
	}

	public void setVoziloId(int voziloId) 
	{
		this.voziloId = voziloId;
	}

	public List<Integer> getOrder_id() {
		return order_id;
	}

	public void setOrder_id(List<Integer> order_id) {
		this.order_id = order_id;
	}


	
}
