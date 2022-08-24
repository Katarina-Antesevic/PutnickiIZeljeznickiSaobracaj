package model.mapa;


import java.io.Serializable;
import java.util.logging.*;

import main.Main;
import model.kompozicija.Kompozicija;


@SuppressWarnings("serial")
public class Kontroler implements Serializable {
	
	private int brojac;
	
	public Kontroler() {
		brojac = 0;
	}
	
	public synchronized int getBrojac() {
		return brojac;
	}
	
	public synchronized void isZauzetaDionica(Kompozicija k, ZeljeznickaStanica zs) {
		if(brojac > 0) {
			try {
				k.getzStanice().add(zs);
				wait();
			}catch(InterruptedException e) {
				Main.loggerKontroler.log(Level.SEVERE, e.fillInStackTrace().toString());
			}
		}
	}
	
	public synchronized boolean isSlobodan() {
		return brojac==0;
	}
	
	public synchronized  void zauzmiDionicu() {
		brojac++;
	}
	
	
	public synchronized void oslobodiDionicu() {
		brojac--;
		if(brojac==0)
			notify();
	}
}
