package beans;

import java.util.List;

public class TempOrder {
	
	
	public List<Artikal> artikli;
	public String napomena;
	

	public TempOrder()
	{
		
	}
	
	

	public TempOrder(List<Artikal> artikli, String napomena) {
		this();
		this.artikli = artikli;
		this.napomena = napomena.replace(",", " ");
	}



	public List<Artikal> getArtikli() {
		return artikli;
	}

	public void setArtikli(List<Artikal> artikli) {
		this.artikli = artikli;
	}
	
	public String getNapomena() {
		return napomena;
	}



	public void setNapomena(String napomena) {
		this.napomena = napomena.replace(",", " ");
	}
	

}
