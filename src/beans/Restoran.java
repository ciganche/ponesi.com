package beans;

import java.util.ArrayList;

import beans.Artikal;

public class Restoran
{
	public enum Tip {domaca, rostilj, kinezi, indijci, picerija, poslasticarnica}
	//max 5
	
	public int id;
	public String ime;
	public String ulica;
	public int broj_ulice;
	public int tip;
	public String tip_str;
	public int visible = 1; //0 - ne; 1 - da
	
	


	//liste
	public ArrayList <Artikal> artikli_restorana = new ArrayList<Artikal>(); 

	public Restoran()
	{
		
	}
	
	public Restoran(String ime, String ulica, int broj_ulice, String tip_str)
	{
		this();
		this.ime = ime;
		this.broj_ulice = broj_ulice;
		this.ulica = ulica;
		this.tip_str = tip_str;
		namestiTip(tip_str);
	}
	
	public Restoran(int id, String ime, String ulica, int broj_ulice, String tip_str)
	{
		this();
		this.id = id;
		this.ime = ime;
		this.broj_ulice = broj_ulice;
		this.ulica = ulica;
		this.tip_str = tip_str;
		namestiTip(tip_str);
	}
	
	
	public Restoran(String id_str, String ime, String ulica, String broj_ulice_str, String tip_strr)
	{
		this.id = Integer.parseInt(id_str);
		this.ime = ime;
		this.ulica = ulica;
		this.broj_ulice = Integer.parseInt(broj_ulice_str);
		this.tip = Integer.parseInt(tip_strr);
		
		
		
		switch(tip)
		{
		case 0:
			this.tip_str = "Serbian";
			break;
		
		case 1:
			this.tip_str = "BBQ";
			break;
			
		case 2:
			this.tip_str = "Chinese";
			break;	
		
		case 3:
			this.tip_str = "Indian";
			break;	
		case 4:
			this.tip_str = "Pizza";
			break;
		case 5:
			this.tip_str = "Pastaria";
			break;
		}
		
	}
	
	
	public ArrayList<Artikal> getArtikli_restorana() 
	{
		return artikli_restorana;
	}




	public void setArtikli_restorana(ArrayList<Artikal> artikli_restorana) {
		this.artikli_restorana = artikli_restorana;
	}


	
	public String getTip_str() {
		return tip_str;
	}

	public void setTip_str(String tip_str) {
		this.tip_str = tip_str;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getIme() {
		return ime;
	}
	public void setIme(String ime) {
		this.ime = ime;
	}
	public String getUlica() {
		return ulica;
	}
	public void setUlica(String ulica) {
		this.ulica = ulica;
	}
	public int getBroj_ulice() {
		return broj_ulice;
	}
	public void setBroj_ulice(int broj_ulice) {
		this.broj_ulice = broj_ulice;
	}
	public int getTip() {
		return tip;
	}
	public void setTip(int tip) {
		this.tip = tip;
	}
	public void namestiTip(String stringic)
	{
		
		if(stringic.equals("Serbian"))
			this.tip = 0;
		if(stringic.equals("BBQ"))
			this.tip = 1;
		if(stringic.equals("Chinese"))
			this.tip = 2;
		if(stringic.equals("Indian"))
			this.tip = 3;
		if(stringic.equals("Pizza"))
			this.tip = 4;
		if(stringic.equals("Pastaria"))
			this.tip = 5;

		System.out.println("CHANGED TYPE: " + stringic + " - " + this.tip);
	}
	
	public void print()
	{
		System.out.print("ID: " + id + " IME: " + ime + " ULICA: " + ulica + " BROJ: " + broj_ulice + " TIP: " + tip);
	}
	
	public int getVisible() {
		return visible;
	}

	public void setVisible(int visible) {
		this.visible = visible;
	}
}
