package beans;

public class Vozilo {

	public int visible; //0 - ne;1 - da 
	public int id;
	public int zauzeto; //0 - ne; 1 - da
	public String marka;
	public String model;
	public int type; // 0 - bajs; 1 - skuter; 2 - automobil
	
	public String registracija;
	public int godinaProzivodnje;
	public String napomena;
	
	public Vozilo()
	{
		
	}

	public Vozilo(int id, String marka, String model, int type, String registracija, int godinaProzivodnje, String napomena)
	{
		this();
		this.visible = 1;
		this.id = id;
		this.marka = marka;
		this.model = model;
		this.type = type;
		this.registracija = registracija;
		this.godinaProzivodnje = godinaProzivodnje;
		this.napomena = napomena;
	}
	
	public Vozilo(int visible, int id, int zauzeto, String marka, String model, int type, String registracija,
			int godinaProzivodnje, String napomena) 
	{
		this();
		this.visible = visible;
		this.id = id;
		this.zauzeto = zauzeto;
		this.marka = marka;
		this.model = model;
		this.type = type;
		this.registracija = registracija;
		this.godinaProzivodnje = godinaProzivodnje;
		this.napomena = napomena;
	}



	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public int getVisible() {
		return visible;
	}

	public void setVisible(int visible) {
		this.visible = visible;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getZauzeto() {
		return zauzeto;
	}

	public void setZauzeto(int zauzeto) {
		this.zauzeto = zauzeto;
	}

	public String getMarka() {
		return marka;
	}

	public void setMarka(String marka) {
		this.marka = marka;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getRegistracija() {
		return registracija;
	}

	public void setRegistracija(String registracija) {
		this.registracija = registracija;
	}

	public int getGodinaProzivodnje() {
		return godinaProzivodnje;
	}

	public void setGodinaProzivodnje(int godinaProzivodnje) {
		this.godinaProzivodnje = godinaProzivodnje;
	}

	public String getNapomena() {
		return napomena;
	}

	public void setNapomena(String napomena) {
		this.napomena = napomena;
	}

	
	
}
