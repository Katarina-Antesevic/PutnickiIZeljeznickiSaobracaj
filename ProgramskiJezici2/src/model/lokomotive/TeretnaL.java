package model.lokomotive;
 
@SuppressWarnings("serial")
public class TeretnaL extends Lokomotiva {

	public TeretnaL() {
		
	}

	public TeretnaL(String oznaka,int snaga) {
		super(oznaka, snaga);
	}
 
	public TeretnaL(String oznaka,int snaga,Pogon pogon) {
		super(oznaka,snaga,pogon);
		
	}
	
	@Override
	public boolean isTeretna() {return true;}
}
