package model.vozila;


import java.util.Random;
import java.util.logging.*;

import main.Main;
import model.mapa.Element;
import model.mapa.PruzniPrelaz;
import model.mapa.PutnaSaobracajnica;

@SuppressWarnings("serial")
public abstract class Vozilo extends Element implements Runnable{
	  
	protected String marka;
	protected String model;
	protected int godiste;
	protected long brzina;
	protected int x,y;
	private int endx,endy;
	protected Smjer smjer;
	protected int put;  //0-lijevi put, 1-put u sredini, 2-desni put
	
	@Override
	public boolean isVozilo() {return true;}
	
	
	
	
	public int getPut() {
		return put;
	}

	public void setPut(int put) {
		this.put = put;
	}

	
	
	public Smjer getSmjer() {
		return smjer;
	}

	public void setSmjer(Smjer smjer) {
		this.smjer = smjer;
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

	public Vozilo() {
		
	}
	
	public Vozilo(String marka, String model, int godiste, int brzina,int put) {
		super();
		this.marka = marka;
		this.model = model;
		this.godiste = godiste;
		this.brzina = brzina;
		this.put = put;
	}

	public void setBrzina(long max) {
		Random r = new Random();
		int maxx=(int) max;
		brzina =(long) r.nextInt(maxx)+1; //ne moze biti 0
	}
	
	public long getBrzina() {
		return brzina;
	}
	
	public String getMarka() {
		return marka;
	}
	public void setMarka(String marka) {
		this.marka = marka;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public Integer getGodiste() {
		return godiste;
	}
	public void setGodiste(int godiste) {
		this.godiste = godiste;
	}
	
	
	private int[] pomjeriSe(Smjer smjer) {
		int[] temp = new int[2];
		if(smjer.toString().equals("GORE")) {
			temp[0] = x-1;
			temp[1] = y;
		}
		else if(smjer.toString().equals("DOLJE")) {
			temp[0] = x+1;
			temp[1] = y;
		}
		else if(smjer.toString().equals("LIJEVO")) {
			temp[0] = x;
			temp[1] = y-1;
		}
		else {
			temp[0] = x;
			temp[1] = y+1;
		}
		return temp;
	}
	
	private Smjer[] odrediSmjer() {
		Smjer[] smjerovi = new Smjer[2];
		if(smjer.toString().equals("GORE")) {
			if(put == 0) {
			smjerovi[0] = Smjer.GORE;
			smjerovi[1] = Smjer.LIJEVO;
			}
			else {
				smjerovi[0] = Smjer.DESNO;
				smjerovi[1] = Smjer.GORE;
			}
		}
		else {
			if(put == 0) {
			smjerovi[0] = Smjer.DOLJE;
			smjerovi[1] = smjer;
			}
			else {
				smjerovi[0] = smjer;
				smjerovi[1] = Smjer.DOLJE;
			}
		}
		return smjerovi;
	}
	
	private void krajnjaPozicija() {
		if(x==21 && y==0)
		{
			endx = 29;endy=7;
		}
		else if(x==0 && y==13)
		{
			endx = 29;endy=13;
		}
		else if(x==20 && y==29)
		{
			endx = 29;endy=21;
		}
		else if(x==29 && y==8)
		{
			endx = 20;endy=0;
		}
		else if(x==29 && y==14)
		{
			endx = 0;endy=14;
		}
		else if(x==29 && y==22)
		{
			endx = 21;endy=29;
		}
	}
	
	@Override
	public void run() {
		krajnjaPozicija();
		Smjer[] smjerovi = odrediSmjer();
		boolean pokreniVozilo=false;
		
		while(!pokreniVozilo) {
		if(Main.map.getPolje(x, y) instanceof PutnaSaobracajnica) {
			
			if(Main.map.getPolje(x,y).getElement()==null && !Main.map.getPolje(x,y).isPodNaponom()) { 
				
				Main.map.getPolje(x,y).setElement(this);
				pokreniVozilo=true;
				break;
				
			}
			else  {
				Main.map.getPolje(x,y).setRed(this);
				try {
					synchronized(this) {
					wait();
					}
				}catch(InterruptedException e) {
					Main.loggerVozilo.log(Level.SEVERE, e.fillInStackTrace().toString());
					
				}
			}
		}}
		while(x!=endx || y!=endy) {
			int[] noveKoord = new int[2];
			
			try {
				Thread.sleep(brzina);
			}catch(InterruptedException e) {
				Main.loggerVozilo.log(Level.SEVERE, e.fillInStackTrace().toString());
			}
			for(int i=0;i<2;i++) { //svako vozilo se moze kretati u 2 moguca smjera po mapi(osim vozila na putu u sredini)
				int[] temp = pomjeriSe(smjerovi[i]);
				
					if(Main.map.getPolje(temp[0], temp[1]) instanceof PutnaSaobracajnica) {
						
						if(Main.map.getPolje(temp[0], temp[1]).isPruzniPrelaz()) {
							
							PruzniPrelaz p =((PruzniPrelaz)Main.map.getPolje(temp[0], temp[1]));
							p.isSlobodanPrelaz();
							p.isNaelektrisanPrelaz(Main.map.getPolje(temp[0], temp[1]));
							
						}
						if(Main.map.getPolje(temp[0], temp[1]).getElement()==null) { 
							
							Main.map.getPolje(temp[0],temp[1]).setElement(this);
							noveKoord = temp;
							break;
							
						}
						else {
							Main.map.getPolje(temp[0], temp[1]).setRed(this);
							try {
								synchronized(this) {
								wait();
								i--;
								}
							}catch(InterruptedException e) {
								Main.loggerVozilo.log(Level.SEVERE, e.fillInStackTrace().toString());
							}
						}
					
					}
			}
			
			
							
			Main.map.getPolje(x, y).setElement(null);
			x = noveKoord[0];
			y = noveKoord[1];				
			
		}
		try {
			Thread.sleep(brzina);
		}catch(InterruptedException e) {
			Main.loggerVozilo.log(Level.SEVERE, e.fillInStackTrace().toString());
		}
		
		
			Main.map.getPolje(x, y).setElement(null);
		
		
		
	}
}
