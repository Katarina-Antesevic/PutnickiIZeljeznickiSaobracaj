package model.vagoni;

@SuppressWarnings("serial")
public class PVagonSaSjedistima extends PutnickiV {

	private Integer brojMjesta;
	public PVagonSaSjedistima() {
		
	} 
	
	
	public PVagonSaSjedistima(int duzina,String oznaka,int brojMjesta) {
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
