package model.vagoni;

@SuppressWarnings("serial")
public class PosebneNamjeneV extends Vagon {
	
	public PosebneNamjeneV() {
		
	}
	 
	public PosebneNamjeneV(int duzina,String oznaka) {
		super(duzina,oznaka);
	}
 
	@Override
	public boolean isPosebneNamjeneVagon() {return true;}
}
