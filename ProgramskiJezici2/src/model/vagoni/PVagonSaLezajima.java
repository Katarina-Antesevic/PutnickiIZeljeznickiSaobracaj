package model.vagoni;

@SuppressWarnings("serial")
public class PVagonSaLezajima extends  PutnickiV {
	
	private Integer brojMjesta;
	
	public PVagonSaLezajima() {
		
	} 
	 
	public PVagonSaLezajima(int duzina,String oznaka,int brojMjesta) {
		super(duzina,oznaka);
		this.brojMjesta = brojMjesta;
	}
	
	public int getBrojMjesta() {
		return this.brojMjesta;
	}
	
	public void setBrojMjesta(int brojMjesta) {
		this.brojMjesta = brojMjesta;
	}

}
