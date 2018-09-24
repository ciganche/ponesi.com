package beans;

public class TempOrder2 {



	public int id;
	public int stanje;
	public String artikli;
	public String napomena;


	public int dostavljac_id;
	public int vozilo_id;

	public int user_id;
	
	public TempOrder2()
	{
		
	}
	
	public TempOrder2(int id, int stanje, String artikli, String napomena, int dostavljac, int vozilo) 
	{
		super();
		this.id = id;
		this.stanje = stanje;
		this.artikli = artikli;
		this.napomena = napomena;
		this.dostavljac_id = dostavljac;
		this.vozilo_id = vozilo;
	}
	
	public TempOrder2(String artikli, String napomena, int dostavljac, int vozilo, int user_id) 
	{
		super();
		this.artikli = artikli;
		this.napomena = napomena;
		this.dostavljac_id = dostavljac;
		this.vozilo_id = vozilo;
		this.user_id = user_id;
	}
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getStanje() {
		return stanje;
	}
	public void setStanje(int stanje) {
		this.stanje = stanje;
	}
	public String getArtikli() {
		return artikli;
	}
	public void setArtikli(String artikli) {
		this.artikli = artikli;
	}
	public String getNapomena() {
		return napomena;
	}
	public void setNapomena(String napomena) {
		this.napomena = napomena;
	}
	
	public int getDostavljac_id() {
		return dostavljac_id;
	}

	public void setDostavljac_id(int dostavljac_id) {
		this.dostavljac_id = dostavljac_id;
	}

	public int getVozilo_id() {
		return vozilo_id;
	}

	public void setVozilo_id(int vozilo_id) {
		this.vozilo_id = vozilo_id;
	}
	
	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	
}
