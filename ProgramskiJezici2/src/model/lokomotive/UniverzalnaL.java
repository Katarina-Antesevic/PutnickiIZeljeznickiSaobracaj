package model.lokomotive;
 
@SuppressWarnings("serial")
public class UniverzalnaL extends Lokomotiva {

	public UniverzalnaL() {
		
	}
	
	public UniverzalnaL(String oznaka,int snaga) {
		super(oznaka, snaga);
	}
 
	public UniverzalnaL(String oznaka,int snaga,Pogon pogon) {
		super(oznaka,snaga,pogon);
		
	}
	
	@Override
	public boolean isUniverzalna() {return true;}
}
