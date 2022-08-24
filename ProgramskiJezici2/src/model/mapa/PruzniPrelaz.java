package model.mapa;


import java.util.logging.Level;

import main.Main;



public class PruzniPrelaz extends Polje implements PutnaSaobracajnica,ZeljeznickaSaobracajnica{
	
	public int brojac;
	public boolean podNaponom=false; 
	
	@Override
	public boolean isPruzniPrelaz() {return true;}
	
	public PruzniPrelaz() {
		brojac = 0;
	}
	
	public static final String boja = "black";
	
	public synchronized void isSlobodanPrelaz() {
		if(brojac > 0) {
			try {
				wait();
			}catch(InterruptedException e) {
				Main.loggerPruzniPrelaz.log(Level.SEVERE, e.fillInStackTrace().toString());
			}
		}
	}
	
	public synchronized void isNaelektrisanPrelaz(Polje p) {
		if (p.isPodNaponom()) {
			try {
				wait();
			}catch(InterruptedException e) {
				Main.loggerPruzniPrelaz.log(Level.SEVERE, e.fillInStackTrace().toString());
			}
		}
	}
	
	public synchronized void oslobodiNaelektrisanje() {
		podNaponom=false;
		
		notify();
	}
	
	
	public synchronized void zauzmiNaelektisanje() {
		podNaponom=true;
	}
	
	
	public synchronized void zauzmiPrelaz() {
		brojac++;
	}
	
	public synchronized void oslobodiPrelaz() {
		brojac--;
		if(brojac==0)
			notify();
	}
	
	public PruzniPrelaz(int x,int y) {
		super(x,y);
	}

	
}
