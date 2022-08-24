package model.vagoni;

@SuppressWarnings("serial")
public class TeretniV extends Vagon {

	private Integer maxNosivost;
	
	public TeretniV() {
		
	}
	 
	public TeretniV(int duzina,String oznaka,Integer maxNosivost) {
		super(duzina,oznaka);
		this.maxNosivost = maxNosivost;
	}

	public Integer getMaxNosivost() {
		return maxNosivost;
	}

	public void setMaxNosivost(Integer maxNosivost) {
		this.maxNosivost = maxNosivost;
	} 
	
	@Override
	public boolean isTeretniVagon() {return true;}
	
	
}
