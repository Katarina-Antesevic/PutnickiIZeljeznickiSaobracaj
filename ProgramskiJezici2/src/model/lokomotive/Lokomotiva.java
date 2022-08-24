package model.lokomotive;

import java.io.Serializable;

import model.mapa.Element;

@SuppressWarnings("serial")
public abstract class Lokomotiva extends Element implements Serializable {
	
	private boolean elektricna=false;
	 
	public void setElektricna(boolean elektricna) {
		this.elektricna = elektricna;
	}
 
	@Override
	public boolean isLokomotiva() {return true;}
	
	public boolean isManeverska() {return false;}
	public boolean isPutnicka() {return false;}
	public boolean isTeretna() {return false;}
	public boolean isUniverzalna() {return false;}
	
	@Override
	public boolean isElektricna() {
		return elektricna;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public static String boja="";
	
	public void postaviBoju(String boja) {
		Lokomotiva.boja=boja;
	}
	
	public static String getBoja() {
		return boja;
	}
	
	protected Pogon pogon;
	
	public void setPogon(Pogon pogon) {
		this.pogon = pogon;
	}

	public Lokomotiva() {
		
	}
	
	public Lokomotiva(String oznaka,int snaga) {
		this.oznaka = oznaka;
		this.snaga = snaga;
	}
	
	public Lokomotiva(String oznaka,int snaga,Pogon pogon) {
		this.oznaka = oznaka;
		this.snaga = snaga;
		this.pogon = pogon;
	}
	
	public String getPogon() {
		return pogon.toString();
	}
	
	protected Integer snaga;
	
	protected String oznaka;
	
	public void setSnaga(int snaga) {
		this.snaga = snaga;
	}
	
	public int getSnaga() {
		return this.snaga;
	}
	
	public String getOznaka() {
		return this.oznaka;
	}
}
