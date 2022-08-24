package model.lokomotive;
 
@SuppressWarnings("serial")
public class PutnickaL extends Lokomotiva {
	
	public PutnickaL() {
		
	}
	
	public PutnickaL(String oznaka,int snaga) {
		super(oznaka, snaga);
	}
 
	public PutnickaL(String oznaka,int snaga,Pogon pogon) {
		super(oznaka,snaga,pogon);
		
	}
	
	@Override
	public boolean isPutnicka() {return true;}
}
