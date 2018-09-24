	package beans;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class User {
	


	public int id;
	public String username;
	public String password;
	
	public String name;
	public String lastname;
	public String tel;
	public String email;
	
	public String registration_date;
	public int user_type; //0 - obican; 1 - vozac; 2 - admin
	public int tokens;
	
	
	//liste
	public List<HistoryOrder> history;
	public List<Integer> saved;
	
	
	//samo za sesiju
	public List<Artikal> korpa;
			
	
	public User()
	{
		
	}
	
	
	
	
	public User(int id, String username, String password, String name, String lastname, String tel, String email,
			String registration_date, int user_type, int tokens, List<HistoryOrder> history, List<Integer> saved) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.name = name;
		this.lastname = lastname;
		this.tel = tel;
		this.email = email;
		this.registration_date = registration_date;
		this.user_type = user_type;
		this.tokens = tokens;
		
		this.history = history;
		this.saved = saved;
		
		//jedino sto se ne ucitava
		this.korpa = new ArrayList<Artikal>();

	}




	//za novokreiranog korisnika
	public User(int id, String username, String password, String name, String lastname, String tel, String email) 
	{
		this();
		this.username = username;
		this.password = password;
		this.name = name;
		this.lastname = lastname;
		this.tel = tel;
		this.email = email;

		LocalDateTime date = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		this.registration_date = date.format(formatter);
		
		this.user_type = 0; 
		this.tokens = 5;
		this.saved = new ArrayList<Integer>();
		this.history = new ArrayList<HistoryOrder>();
		this.korpa = new ArrayList<Artikal>();
		
		this.id = id;
	}
	
	
	public User(int id, String username, String password, String name, String lastname, String tel, String email, String reg_date) 
	{
		this();
		this.username = username;
		this.password = password;
		this.name = name;
		this.lastname = lastname;
		this.tel = tel;
		this.email = email;


		this.registration_date = reg_date;
		
		this.user_type = 0; 
		this.tokens = 5;
		this.saved = new ArrayList<Integer>();
		this.history = new ArrayList<HistoryOrder>();
		this.korpa = new ArrayList<Artikal>();
		
		this.id = id;
	}
	
	//za hendlanje gresaka
	public User(String username,String password) 
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

	public int getUser_type() {
		return user_type;
	}

	public void setUser_type(int user_type) {
		this.user_type = user_type;
	}

	public List<Artikal> getKorpa() {
		return korpa;
	}

	public void setKorpa(List<Artikal> korpa) {
		this.korpa = korpa;
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

	public int getTokens() {
		return tokens;
	}

	public void setTokens(int tokens) {
		this.tokens = tokens;
	}

	public List<Integer> getSaved() {
		return saved;
	}

	public void setSaved(List<Integer> saved) {
		this.saved = saved;
	}
	
	public List<HistoryOrder> getHistory() {
		return history;
	}


	public void setHistory(List<HistoryOrder> history) {
		this.history = history;
	}

	public void printUser()
	{
		System.out.println("* * * PRIMLJEN KORISNIK: ");
		
		System.out.println("ID: " + id);
		System.out.println("USERNAME: " + username);
		System.out.println("PASSWORD: " + password);
		System.out.println("IME: " + name);
		System.out.println("PRZ: " + lastname);
		System.out.println("TELEFON: " + tel);
		System.out.println("MEJL: " + email);
		System.out.println("DATUM: " + registration_date);

		System.out.println("* * *");
	}

	public void addToKorpa(Artikal a)
	{
		korpa.add(a);
	}
	
	public void addToSavedRestaurants(int a)
	{
		saved.add(a);
	}
	
	public void removeFromSavedRestaurants(int a)
	{
		saved.remove(a);
	}
	
}
