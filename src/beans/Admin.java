package beans;

public class Admin 
{
	public int id;
	public String username;
	public String password;
	public String name;
	public String lastname;
	public String tel;
	public String email;
	public String registration_date;
	
	public int user_type = 2;
	
	public Admin()
	{
		
	}

	public Admin(int id, String username, String password, String name, String lastname, String tel, String email,
			String registration_date, int user_type) {
		this();
		this.id = id;
		this.username = username;
		this.password = password;
		this.name = name;
		this.lastname = lastname;
		this.tel = tel;
		this.email = email;
		this.registration_date = registration_date;
		this.user_type = user_type;
	}
	
	//za hendlanje greske
	public Admin(String username, String password)
	{
		this();
		this.username = username;
		this.password = password;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
	
	
}
