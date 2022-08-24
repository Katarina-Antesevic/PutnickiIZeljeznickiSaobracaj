package model.mapa;

public class Put extends Polje implements PutnaSaobracajnica{

	public Put() {
	}
	
	public static final String boja = "lightblue";
	
	public Put(int x,int y) {
		super(x,y);
	}
	
	@Override
	public boolean isPut() {return true;}
	
}
