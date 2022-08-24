package model.vagoni;



import java.io.Serializable;

import model.mapa.Element;

@SuppressWarnings("serial")
public abstract class Vagon extends Element implements Serializable{
	
	public static final String boja = "lightpink";
	
	@Override  
	public boolean isVagon() {return true;}
	
	public boolean isPutnickiVagon() {return false;}
	public boolean isPosebneNamjeneVagon() {return false;}
	public boolean isTeretniVagon() {return false;}

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
	protected Integer duzina;
	protected String oznaka;
	
	public Vagon() {
		
	}
	
	public Vagon(int duzina,String oznaka) {
		this.duzina = duzina;
		this.oznaka = oznaka;
	}
	
	public Integer getDuzina() {
		return duzina;
	}
	public void setDuzina(Integer duzina) {
		this.duzina = duzina;
	}
	public String getOznaka() {
		return oznaka;
	}
	public void setOznaka(String oznaka) {
		this.oznaka = oznaka;
	}
	
	

}
