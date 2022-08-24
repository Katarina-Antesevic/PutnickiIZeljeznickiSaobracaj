package model.vozila;


import java.util.Random;
import java.util.logging.Level;

import main.*;

public class GeneratorVozila extends Thread {
	 
	private static int[] brojGenerisanihVozila = new int[3];
	 
	public enum Marka{
		BMW,
		OPEL,
		FIAT,
		MERCEDES,
		IVECO;
	}
	
	public enum Model{
		X6, //bmw
		X5, //bmw
		PUNTO, //fiat
		PANDA, //fiat
		ASTRA, //opel
		CORSA, //opel
		ACTROS, //mercedes
		ATEGO,  //mercedes
		EUROCARGO, //iveco
		TURBOSTAR; //iveco
	} 
	
	public static void setGodiste(Vozilo v) {
		v.setGodiste(new Random().nextInt(21)+2000);
	}
	
	public static void pokreniNit(Vozilo v) {
		new Thread(v).start();
	}
	
	public static void setStranaPuta(Vozilo v, int i) {
		boolean stranaPuta = new Random().nextBoolean();
		if(stranaPuta == true) {
			v.setX(Main.pocetakLijeveTrake[i][0]);
			v.setY(Main.pocetakLijeveTrake[i][1]);
			if(i==0)
				v.setSmjer(Smjer.DESNO);
			else if (i==1)
				v.setSmjer(Smjer.DOLJE);
			else
				v.setSmjer(Smjer.LIJEVO);
			v.setPut(i);
		}else {
			v.setX(Main.pocetakDesneTrake[i][0]);
			v.setY(Main.pocetakDesneTrake[i][1]);
			v.setPut(i);
			v.setSmjer(Smjer.GORE);
		}
		
	}	
	
	public void run() {
		
		postaviVozilaNaPut();
		
	}
	
	private static synchronized void postaviVozilaNaPut() {
		Main.loadConfig();
		
		int i=0;
		
		boolean[] put = new boolean[3];
		put[0]=false;
		put[1]=false;
		put[2]=false;
		
		while(put[0]==false || put[1]==false || put[2]==false) { //nisu sva vozila generisana
			Kamion k=new Kamion();
			Automobil a=new Automobil();
						
			if(brojGenerisanihVozila[i]==Main.brojVozila[i]) {
				put[i] = true;
				i=(i+1)%3; //predji na sledeci put
				continue;
			}
				
				boolean vozilo = new Random().nextBoolean();
				if(vozilo == true) {
					boolean marka=new Random().nextBoolean();
					boolean model=new Random().nextBoolean();
					setGodiste(k);
					int nosivost = new Random().nextInt(9001)+1000; 
					k.setBrzina(Main.maxBrzina[i]);
					System.out.println((i+1)+". put Vozilo: max brzina: "+Main.maxBrzina[i]+" brzina: "+k.getBrzina());
					k.setNosivost(nosivost);
					
					if(marka) {
						k.setMarka(Marka.MERCEDES.toString());
						
						
						if(model) {
							k.setModel(Model.ACTROS.toString());
						}
						else {
							k.setModel(Model.ATEGO.toString());
						}
					}
					else {
						k.setMarka(Marka.IVECO.toString());
						
						if(model) {
							k.setModel(Model.EUROCARGO.toString());
						}
						else {
							k.setModel(Model.TURBOSTAR.toString());
						}
					}
					
					setStranaPuta(k, i);
					pokreniNit(k);
					
				}else {
					int marka = new Random().nextInt(3);
					boolean model = new Random().nextBoolean();
					setGodiste(a);
					boolean brojVrata=new Random().nextBoolean();
					a.setBrzina(Main.maxBrzina[i]);
					System.out.println((i+1)+". put Vozilo: max brzina: "+Main.maxBrzina[i]+" brzina: "+a.getBrzina());
					
					if(brojVrata) {
						a.setBrojVrata(2);
					}
					else {
						a.setBrojVrata(4);
					}
					
					
					if(marka==0) {
						a.setMarka(Marka.BMW.toString());
						
						if(model) {
							a.setModel(Model.X5.toString());
						}
						else {
							a.setModel(Model.X6.toString());
						}
					}
					else if(marka==1) {
						a.setMarka(Marka.FIAT.toString());
						
						if(model) {
							a.setModel(Model.PANDA.toString());
						}
						else {
							a.setModel(Model.PUNTO.toString());
						}						
					}
					else {
						a.setMarka(Marka.OPEL.toString());
						
						if(model) {
							a.setModel(Model.ASTRA.toString());
						}
						else {
							a.setModel(Model.CORSA.toString());
						}
					}
	
					setStranaPuta(a,i);
					pokreniNit(a);
				}
				brojGenerisanihVozila[i]++;
				
				//vozila se generisu svake 2s
				try {
					sleep(2000);
				}catch(InterruptedException e) {
					Main.loggerGeneratorV.log(Level.SEVERE, e.fillInStackTrace().toString());
				}
				i=(i+1)%3; //prvo se generise vozilo na putu 1, pa 2, pa 3, pa opet ispocetka
		}
	}
	
	
}
