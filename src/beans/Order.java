package beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Order 
{
	public int id;
	public int user; //id_korisnika
	public int dostavljac_id;
	public int stanje = 0;
	public String dateTime_order;
	public double total_cena;
	public int used_tokens;
	public List<String> artikli = new ArrayList<String>();
	public String napomena;


	public int visible = 1; // 0 - ne; 1 - da
	public List<Artikal> artikli_objs;

	public Order()
	{
		
	}
	
	public Order(List<Artikal> orders)
	{
		this();
		
		for( int i = 0 ; i < orders.size() ; i ++ )
		{
			artikli.add(orders.get(i).getKolicina() + " x " + orders.get(i).getIme() + " from " + orders.get(i).getRestoran_maticni());
		}
		
		artikli_objs = orders;
	
	}
	
	public Order(int id, int stanje, String napomena)
	{
		this.id = id;
		this.stanje = stanje;
		this.napomena = napomena;
	}
	

	
	public Order(int id, int user, int dostavljac_id, int stanje, String dateTime_order, double total_cena,
			int used_tokens, String napomena, List<String> artikli, int visible, List<Artikal> artikli_objs) 
	{
		this();
		this.id = id;
		this.user = user;
		this.dostavljac_id = dostavljac_id;
		this.stanje = stanje;
		this.dateTime_order = dateTime_order;
		this.total_cena = total_cena;
		this.used_tokens = used_tokens;
		this.napomena = napomena;
		this.artikli = artikli;	
		this.visible = visible;
		this.artikli_objs = artikli_objs;
	}

	
	
	
	
	
	
	public void clearAllOrders()
	{
		this.artikli.clear();
		this.artikli_objs.clear();
	}
	
	
	
	
	
	public int getUsed_tokens() {
		return used_tokens;
	}

	public void setUsed_tokens(int used_tokens) {
		this.used_tokens = used_tokens;
	}

	public int getId() {
		return id;
	}

	public void setId(int order_id) {
		this.id = order_id;
	}
	
	public double getTotal_cena() {
		return total_cena;
	}

	public void setTotal_cena(double total_cena) {
		this.total_cena = total_cena;
	}

	public int getStanje() {
		return stanje;
	}
	public void setStanje(int stanje) {
		this.stanje = stanje;
	}
	
	public List<String> getArtikli() {
		return artikli;
	}
	public void setArtikli(List<String> artikli) {
		this.artikli = artikli;
	}
	
	public String getDateTime_order() {
		return dateTime_order;
	}
	public void setDateTime_order(String dateTime_order) {
		this.dateTime_order = dateTime_order;
	}
	public int getUser() {
		return user;
	}
	public void setUser(int user) {
		this.user = user;
	}
	public int getDostavljac_id() {
		return dostavljac_id;
	}
	public void setDostavljac_id(int dostavljac_id) {
		this.dostavljac_id = dostavljac_id;
	}
	public String getNapomena() {
		return napomena;
	}

	public void setNapomena(String napomena) {
		this.napomena = napomena;
	}
	
	public int getVisible() {
		return visible;
	}

	public void setVisible(int visible) {
		this.visible = visible;
	}

	public List<Artikal> getArtikli_objs() {
		return artikli_objs;
	}

	public void setArtikli_objs(List<Artikal> artikli_objs) {
		this.artikli_objs = artikli_objs;
	}

	
}
