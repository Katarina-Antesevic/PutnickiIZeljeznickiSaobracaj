package model.vozila;


@SuppressWarnings("serial")
public class Kamion extends Vozilo {
	
	private int nosivost;

	public static final String boja = "limegreen";
	 
	@Override
	public boolean isKamion() {return true;}
	
	public Kamion() {
		
	}
	 
	public Kamion(String marka,String model,int godiste,int brzina,int put) {
		super(marka,model,godiste,brzina,put);
	}

	
	public int getNosivost() {
		return nosivost;
	}
	

	public void setNosivost(int nosivost) {
		this.nosivost=nosivost;
	}

}
