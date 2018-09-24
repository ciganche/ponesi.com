package beans;

import dao.RestoranDAO;

/**
 * @author Al
 *
 */
public class Artikal {

	


	public String tip;
	public String ime;
	public int kolicina;
	public double cena;
	public int id;
	public int id_restorana;
	public String restoran_maticni;
	public int gramaza;
	int visible = 1; //0 - ne; 1 - da 
	



	public Artikal()
	{
		
	}


	public Artikal(int id_restorana, String ime, double cena, String tip, int gramaza)
	{
		this.id_restorana = id_restorana;
		this.cena = cena;
		this.tip = tip;
		this.gramaza = gramaza;
	}
	
	
	public Artikal(int id, int id_restorana, String ime, double cena, String tip, int gramaza)
	{
		this.id = id;
		this.id_restorana = id_restorana;
		this.cena = cena;
		this.tip = tip;
		this.gramaza = gramaza;
	}
	
	
	
	
	public Artikal(Artikal original)
	{
		this();
		this.tip = original.tip;
		this.ime = original.ime;
		this.kolicina = 0;
		this.cena = original.cena;
		this.id = original.id;
		this.id_restorana = original.id_restorana;
		this.restoran_maticni = original.restoran_maticni;
		this.gramaza = original.gramaza;
		
	}


	public Artikal(String tip, String ime, int kolicina, double cena, int id, int id2,String maticni, int gramaza) 
	{
		this.tip = tip;
		this.ime = ime;
		this.kolicina = kolicina;
		this.cena = cena;
		this.id = id;
		this.id_restorana = id2;
		this.restoran_maticni = maticni;
		this.gramaza = gramaza;
	}
	
	public String getRestoran_maticni() {
		return restoran_maticni;
	}

	public void setRestoran_maticni(String restoran_maticni) {
		this.restoran_maticni = restoran_maticni;
	}
	
	public int getId_restorana() {
		return id_restorana;
	}

	public void setId_restorana(int id_restorana) {
		this.id_restorana = id_restorana;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int idl) {
		this.id = idl;
	}
	
	public String getTip() {
		return tip;
	}
	public void setTip(String tip) {
		this.tip = tip;
	}
	public String getIme() {
		return ime;
	}
	public void setIme(String ime) {
		this.ime = ime;
	}
	public int getKolicina() {
		return kolicina;
	}
	public void setKolicina(int kolicina) {
		this.kolicina = kolicina;
	}
	public double getCena() {
		return cena;
	}
	public void setCena(double cena) {
		this.cena = cena;
	}
	
	public int getGramaza() {
		return gramaza;
	}

	public void setGramaza(int gramaza) {
		this.gramaza = gramaza;
	}
	
	public int getVisible() {
		return visible;
	}

	public void setVisible(int visible) {
		this.visible = visible;
	}
	
	
}
