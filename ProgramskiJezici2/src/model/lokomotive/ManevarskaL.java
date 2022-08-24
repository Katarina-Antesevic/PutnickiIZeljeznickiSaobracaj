package model.lokomotive;

@SuppressWarnings("serial")
public class ManevarskaL extends Lokomotiva{

	public ManevarskaL() {
		
	} 
	
	public ManevarskaL(String oznaka,int snaga) {
		super(oznaka, snaga);
	}
	
	public ManevarskaL(String oznaka,int snaga,Pogon pogon) {
		super(oznaka,snaga,pogon);
		
	}
	
	@Override
	public boolean isManeverska() {return true;}
	
}
