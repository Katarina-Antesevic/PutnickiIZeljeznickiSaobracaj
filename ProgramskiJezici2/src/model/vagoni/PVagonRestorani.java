package model.vagoni;

@SuppressWarnings("serial")
public class PVagonRestorani extends PutnickiV {
	
	private String opis;
	
	public PVagonRestorani() {
		
	} 
	 
	public PVagonRestorani(int duzina,String oznaka,String opis) {
		super(duzina,oznaka);
		this.opis = opis;
	}
	 
	public String getOpis() {
		return this.opis;
	}
	
	public void setOpis(String opis) {
		this.opis = opis;
	}

}
