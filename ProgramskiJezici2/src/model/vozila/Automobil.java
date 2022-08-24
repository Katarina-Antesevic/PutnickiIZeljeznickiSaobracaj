package model.vozila;


@SuppressWarnings("serial")
public class Automobil extends Vozilo {
	
	public static final String boja = "#ff4500";
	private int brojVrata;
	
	@Override
	public boolean isAutomobil() {return true;}
	
	public Automobil() {
		
	} 
	
	public Automobil(String marka,String model,int godiste,int brzina,int put) {
		super(marka,model,godiste,brzina,put);
	}

	public int getBrojVrata() {
		return brojVrata;
	}

	
	public void setBrojVrata(int brojVrata) {
		this.brojVrata = brojVrata;
	}

}
