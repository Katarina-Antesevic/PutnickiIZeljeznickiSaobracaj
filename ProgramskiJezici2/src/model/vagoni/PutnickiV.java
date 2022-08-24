package model.vagoni;

@SuppressWarnings("serial")
public class PutnickiV extends Vagon{

	public PutnickiV() {
		super();
	}
 
	public PutnickiV(int duzina, String oznaka) {
		super(duzina, oznaka);
	}
	
	@Override
	public boolean isPutnickiVagon() {return true;}
	

	
}
