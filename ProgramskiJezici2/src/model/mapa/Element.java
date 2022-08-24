package model.mapa;

import java.io.Serializable;
 
@SuppressWarnings("serial")
public abstract class Element implements Serializable{

	protected int x,y;
	public abstract void setX(int x);
	public abstract void setY(int y) ;
	public abstract int getX();
	public abstract int getY(); 
	
	public boolean isVozilo() {return false;}
	public boolean isVagon() {return false;}
	public boolean isAutomobil() {return false;}
	public boolean isKamion() {return false;}
	public boolean isLokomotiva() {return false;}
	public boolean isElektricna() {return false;}
	
	
}
