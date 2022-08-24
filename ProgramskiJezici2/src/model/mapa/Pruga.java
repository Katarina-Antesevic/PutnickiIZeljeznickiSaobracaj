package model.mapa;

public class Pruga extends Polje implements ZeljeznickaSaobracajnica {
	
	public Pruga() {
		
	} 
	
	public static final String boja = "darkgray";
	
	public Pruga(int x,int y) {
		super(x,y);
	}
	
	@Override
	public boolean isPruga() {return true;}

}
