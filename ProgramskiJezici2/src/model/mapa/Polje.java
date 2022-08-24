package model.mapa;

import java.util.ArrayList;

import controllers.StartWindowController;
import javafx.application.Platform;
import model.kompozicija.Kompozicija;

public class Polje {
	
	public static Object lock  = new Object();
	 
	protected int x,y;
	
	protected boolean podNaponom;
	
	private ArrayList<Element> vozilaURedu = new ArrayList<Element>();
	private ArrayList<Kompozicija> kompozicijeURedu = new ArrayList<>();
	
	public boolean isPruga() {return false;}
	public boolean isPruzniPrelaz() {return false;}
	public boolean isPut() {return false;}
	public boolean isZeljeznickaStanica() {return false;}
	
	public synchronized void setKomp(Kompozicija k) {
		kompozicijeURedu.add(k);
	}
	
	public boolean isPodNaponom() {
		return podNaponom;
	}

	public void setPodNaponom(boolean podNaponom) {
		this.podNaponom = podNaponom;
	}

	public Polje() {
		
	}
	
	public boolean isEmpty() {
		return element==null;
	}
	
	public Polje(int x,int y) {
		this.x = x;
		this.y = y;
	}
	
	protected Element element;
	
	public synchronized void setElement(Element e) {
		this.element = e; 
		if(element == null && !vozilaURedu.isEmpty()) {
			synchronized(vozilaURedu.get(0)) {
				vozilaURedu.get(0).notify();
				vozilaURedu.remove(0);
			}
		}
		else if(element == null && !kompozicijeURedu.isEmpty()) {
			synchronized(kompozicijeURedu.get(0)) {
				kompozicijeURedu.get(0).notify();
				kompozicijeURedu.remove(0);
			}
		}
		final Polje p = this;
		Platform.runLater(() -> {
			StartWindowController.setBoja(null, x, y,p);
        });
		
	}
	
	public synchronized void setRed(Element e) {
		vozilaURedu.add(e);
	}
	
	public synchronized Element getElement() {
		return element;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}
	

}
