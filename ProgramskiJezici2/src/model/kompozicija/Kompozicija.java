package model.kompozicija;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingFormatArgumentException;
import java.util.Random;
import java.util.logging.Level;

import main.Main;
import model.lokomotive.*;
import model.mapa.*;
import model.vagoni.*;

@SuppressWarnings("serial")
public class Kompozicija extends Thread implements Serializable{
	 
	private int x = 0, y = 0;
	private Kontroler control;
	private long vrijemeKretanja;
	private int brzinaKretanja;
	private int[][] istorija = new int[Main.SIZE][Main.SIZE];
	private ArrayList<Vagon> vagoni = new ArrayList<>();
	private ArrayList<Lokomotiva> lokomotive = new ArrayList<>();
	private boolean krajKretanja = false;
	private String oznaka;
	private String vrijemeKraja;
	private ArrayList<ZeljeznickaStanica> zStanice=new ArrayList<>();
	private boolean naelektrisanje = false;
	private Integer korak = 0;
	public Map<Integer, ArrayList<Integer>> koraciPoRedu= new HashMap<>();
	private ZeljeznickaStanica pocetna ,krajnja;
	private String trenutna = "";
	private boolean istaPocetnaIKrajnja=false;
	private boolean krajMape=false;
	private File file;
	
	public Kompozicija() {
		
	}
	
	public Kompozicija(File file) {
		this.file=file;
	}
	
	public boolean isKrajMape() {
		return krajMape;
	}
	
	public void setKrajMape(boolean value) {
		this.krajMape=value;
	}
	
	public boolean isIstaPocetnaIKrajnja() {
		return istaPocetnaIKrajnja;
	}
	
	public void setIstaPocetnaIKrajnja(boolean value) {
		this.istaPocetnaIKrajnja=value;
	}
	
	public int getKorak() {
		return korak;
	}

	public boolean isNaelektrisanje() {
		return naelektrisanje;
	}

	public void setNaelektrisanje(boolean naelektrisanje) {
		this.naelektrisanje = naelektrisanje;
	}

	public ArrayList<ZeljeznickaStanica> getzStanice() {
		return zStanice;
	}

	public void setzStanice(ArrayList<ZeljeznickaStanica> zStanice) {
		this.zStanice = zStanice;
	}

	public int getIstorija(int x,int y) {
		return istorija[x][y];
	}

	public void setIstorija(int[][] istorija) {
		this.istorija = istorija;
	}
	
	private PruzniPrelaz[] getPrelaze(String narednaStanica) {
		
		PruzniPrelaz[] prelazi = new PruzniPrelaz[2];
		
		if(trenutna.equals("A") && narednaStanica.equals("B")) {
			prelazi[1] = (PruzniPrelaz)Main.map.getPolje(20, 2);
			prelazi[0] = (PruzniPrelaz)Main.map.getPolje(21, 2);
		}
		else if(narednaStanica.equals("A")) {
			prelazi[0] = (PruzniPrelaz)Main.map.getPolje(20, 2);
			prelazi[1] = (PruzniPrelaz)Main.map.getPolje(21, 2);
		}
		else if(trenutna.equals("B") && narednaStanica.equals("C")) {
			prelazi[1] = (PruzniPrelaz)Main.map.getPolje(6, 13);
			prelazi[0] = (PruzniPrelaz)Main.map.getPolje(6, 14);
		}
		else if(trenutna.equals("C") && narednaStanica.equals("B")) {
			prelazi[0] = (PruzniPrelaz)Main.map.getPolje(6, 13);
			prelazi[1] = (PruzniPrelaz)Main.map.getPolje(6, 14);
		}
		else if(trenutna.equals("C") && narednaStanica.equals("E")) {
			prelazi[0] = (PruzniPrelaz)Main.map.getPolje(20, 26);
			prelazi[1] = (PruzniPrelaz)Main.map.getPolje(21, 26);
		}
		else if(trenutna.equals("E") && narednaStanica.equals("C")) {
			prelazi[1] = (PruzniPrelaz)Main.map.getPolje(20, 26);
			prelazi[0] = (PruzniPrelaz)Main.map.getPolje(21, 26);
		}
		
		return prelazi;
	}
	
	
	public ZeljeznickaStanica getPocetna() {
		return pocetna;
	}

	public void setPocetna(ZeljeznickaStanica pocetna) {
		this.pocetna = pocetna;
		trenutna = this.pocetna.getName();
	}

	public ZeljeznickaStanica getKrajnja() {
		return krajnja;
	}

	public void setKrajnja(ZeljeznickaStanica krajnja) {
		this.krajnja = krajnja;
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
	
	public long getVrijemeKretanja() {
		return vrijemeKretanja;
	}

	public void setVrijemeKretanja(long vrijemeKretanja) {
		this.vrijemeKretanja = vrijemeKretanja;
	}
	
	public void addLokomotiva(Lokomotiva l) {
		lokomotive.add(l);
	}
	
	public void addVagon(Vagon v) {
		vagoni.add(v);
	}
	
	public  ArrayList<Vagon> getVagoni() {
		return vagoni;
	}
	public void setVagoni( ArrayList<Vagon> vagoni) {
		this.vagoni = vagoni;
	}
	public ArrayList<Lokomotiva> getLokomotive() {
		return lokomotive;
	}
	public void setLokomotive(ArrayList<Lokomotiva> lokomotive) {
		this.lokomotive = lokomotive;
	}
	public int getBrzinaKretanja() {
		return brzinaKretanja;
	}
	public void setBrzinaKretanja(int brzinaKretanja) {
		this.brzinaKretanja = brzinaKretanja;
	}
	
	ArrayList<Element> elementi = new ArrayList<>();
	
	private void setKompozicija() {
		for(Element e : lokomotive) {
			elementi.add(e);
		}
		for(Element e : vagoni) {
			elementi.add(e);
		}
	}
	
	private int[] sledeciKorak(Element element) {
		Integer x = element.getX(); Integer y = element.getY();
		int[] koordinate = new int[2];
		if(trenutna.equals("A")) {
		if(Main.map.getPolje(x-1, y) instanceof ZeljeznickaSaobracajnica) {
			koordinate[0]= x-1;
			koordinate[1] = y;
		}else if(Main.map.getPolje(x, y+1) instanceof ZeljeznickaSaobracajnica ) {
			koordinate[0]= x;
			koordinate[1] = y+1;
		}
		} else if(trenutna.equals("B") && krajnja.getName().equals("A")) {
			if(Main.map.getPolje(x+1, y) instanceof ZeljeznickaSaobracajnica) {
				koordinate[0]= x+1;
				koordinate[1] = y;
			}else if(Main.map.getPolje(x, y-1) instanceof ZeljeznickaSaobracajnica  ) {
				koordinate[0]= x;
				koordinate[1] = y-1;
			}
		}else if(trenutna.equals("B") && ( krajnja.getName().equals("C") ||  krajnja.getName().equals("D") ||  krajnja.getName().equals("E"))) {
			if(Main.map.getPolje(x+1, y) instanceof ZeljeznickaSaobracajnica) {
				koordinate[0]= x+1;
				koordinate[1] = y;
			}else if(Main.map.getPolje(x, y+1) instanceof ZeljeznickaSaobracajnica ) {
				koordinate[0]= x;
				koordinate[1] = y+1;
			}
		}else if(trenutna.equals("C") && ( krajnja.getName().equals("A") ||  krajnja.getName().equals("B"))) {
			if(Main.map.getPolje(x-1, y) instanceof ZeljeznickaSaobracajnica) {
				koordinate[0]= x-1;
				koordinate[1] = y;
			}else if(Main.map.getPolje(x, y-1) instanceof ZeljeznickaSaobracajnica) {
				koordinate[0]= x;
				koordinate[1] = y-1;
			}
		}
		else if(trenutna.equals("C") && krajnja.getName().equals("E") ) {
			if(Main.map.getPolje(x+1, y) instanceof ZeljeznickaSaobracajnica) {
				koordinate[0]= x+1;
				koordinate[1] = y;
			}else if(Main.map.getPolje(x, y+1) instanceof ZeljeznickaSaobracajnica  ) {
				koordinate[0]= x;
				koordinate[1] = y+1;
			}
		}
		else if(trenutna.equals("C") && krajnja.getName().equals("D") ) {
			if(Main.map.getPolje(x-1, y) instanceof ZeljeznickaSaobracajnica) {
				koordinate[0]= x-1;
				koordinate[1] = y;
			}
			else if(Main.map.getPolje(x, y+1) instanceof ZeljeznickaSaobracajnica ) {
				if(istorija[x][y+1]==1) { //idemo u 2 smjera tokom kretanja po y -> zato provjera da li smo vec prosli to polje
					koordinate[0]= x;
					koordinate[1] = y-1;
					}
				else {
					koordinate[0] = x;
					koordinate[1] = y+1;
				}
				}
			else if(Main.map.getPolje(x, y-1) instanceof ZeljeznickaSaobracajnica) {
				koordinate[0]= x;
				koordinate[1] = y-1;
			}
		
		}
		else if(trenutna.equals("D") ) {
			if(Main.map.getPolje(x+1, y) instanceof ZeljeznickaSaobracajnica) {
				koordinate[0]= x+1;
				koordinate[1] = y;
			}else if(Main.map.getPolje(x, y-1) instanceof ZeljeznickaSaobracajnica ) {
				if(istorija[x][y-1] == 1) { //idemo u 2 smjera tokom kretanja po y -> zato provjera da li smo vec prosli to polje
					koordinate[0]= x;
					koordinate[1] = y+1;
				}else {
					koordinate[0] = x;
					koordinate[1] = y-1;
				}
			}else if(Main.map.getPolje(x,y+1) instanceof ZeljeznickaSaobracajnica ) {
				koordinate[0]= x;
				koordinate[1] = y+1;
			}
		}
		else if(trenutna.equals("E") ) {
			if(Main.map.getPolje(x-1, y) instanceof ZeljeznickaSaobracajnica) {
				koordinate[0]= x-1;
				koordinate[1] = y;
			}else if(Main.map.getPolje(x, y-1) instanceof ZeljeznickaSaobracajnica  ) {
				
				koordinate[0] = x;
				koordinate[1] = y-1;
				
			}
		}
		else if(trenutna.equals("") && pocetna.getName().equals("E") ) {
			if(Main.map.getPolje(x, y-1) instanceof ZeljeznickaSaobracajnica ) {
				
				koordinate[0] = x;
				koordinate[1] = y-1;
				
			}
		}
		else if(trenutna.equals("") && pocetna.getName().equals("A") ) {
			if(Main.map.getPolje(x-1, y) instanceof ZeljeznickaSaobracajnica) {
				koordinate[0]= x-1;
				koordinate[1] = y;
			}
		}
		return koordinate;
	}
	
	public void serijalizacija() {
		try {
			
			FileOutputStream pisac=new FileOutputStream(Main.DETALJI_PATH+getVrijemeKraja()+"_kompozicija_"+this.getOznaka()+".txt");
			ObjectOutputStream upisObjekta=new ObjectOutputStream(pisac);
			upisObjekta.writeObject(this);					
			upisObjekta.close();
			pisac.close();
		}catch(FileNotFoundException e){
			Main.loggerKompozicija.log(Level.SEVERE, e.fillInStackTrace().toString());
		}
		catch(IOException e){
			Main.loggerKompozicija.log(Level.SEVERE, e.fillInStackTrace().toString());
		}
		
	}
	
	private static boolean provjeriKompoziciju(Kompozicija k) {
		 ArrayList<String> lokomotive = new ArrayList<>();
		 ArrayList<String> vagoni= new ArrayList<>();
		  
		 
		 ArrayList<Lokomotiva> kompozicijaL = k.getLokomotive();
		 
		 if(kompozicijaL.isEmpty())
			 return false;
		 
		 for(Lokomotiva l:kompozicijaL){
			 if(l.isManeverska()) {
				// System.out.println("M");
			 	lokomotive.add("manevarska");}
			 else if(l.isPutnicka())
			  	lokomotive.add("putnicka");
			  else if(l.isTeretna())
			  	lokomotive.add("teretna");
			  else {//System.out.println("U");
			  	lokomotive.add("univerzalna");}
		  }
		  
		  //provjera lokomotiva
		  
		  if(lokomotive.contains("manevarska") && lokomotive.contains("teretna"))
		  	return false;
		  else if(lokomotive.contains("manevarska") && lokomotive.contains("putnicka"))
		  	return false;
		  else if(lokomotive.contains("manevarska") && lokomotive.contains("univerzalna"))
		  	return false;
		  else if(lokomotive.contains("teretna") && lokomotive.contains("putnicka"))
		  	return false;
		  
		  String tipKompozicije="";
		  if(lokomotive.contains("manevarska"))
		  	tipKompozicije="manevarska";
		  else if(lokomotive.contains("putnicka") && lokomotive.contains("univerzalna"))
		  	tipKompozicije="putnicka";
		  else if(lokomotive.contains("teretna") && lokomotive.contains("univerzalna"))
		  	tipKompozicije="teretna";
		  else if(lokomotive.contains("univerzalna") && !(lokomotive.contains("teretna") && lokomotive.contains("univerzalna")))
		  	tipKompozicije="univerzalna";
		  
		  //provjera vagona
		  
		  ArrayList<Vagon> vagoniK=k.getVagoni();
		  
		  for(Vagon v:vagoniK){
			  if(v.isPosebneNamjeneVagon())
			  	vagoni.add("posebni");
			  else if(v.isPutnickiVagon())
			  	vagoni.add("putnicki");
			  else	if(v.isTeretniVagon())
			  	vagoni.add("teretni");
		   }
		  
		 if(vagoni.contains("posebni") && vagoni.contains("putnicki"))
		  	return false;
		 if(vagoni.contains("posebni") && vagoni.contains("teretni"))
		  	return false;
		 if(vagoni.contains("teretni") && vagoni.contains("putnicki"))
		 	return false;
		 else if(vagoni.isEmpty())
		 	return true; //moze se desiti da postoje samo lokomotive u kompoziciji
		 
		 
		//provjera slaganja lokomotiva i vagona
		
		if(vagoni.contains("posebni") && (tipKompozicije.equals("manevarska") || tipKompozicije.equals("univerzalna")))
			return true;
		else if(vagoni.contains("teretni") && (tipKompozicije.equals("teretna") || tipKompozicije.equals("univerzalna")))
			return true;
		else if(vagoni.contains("putnicki") && (tipKompozicije.equals("putnicka") || tipKompozicije.equals("univerzalna")))
		 	return true;
		else return false;
	}
	
	public void run() {
		
		try {
			
			try (BufferedReader br = new BufferedReader(new FileReader(file))){
				System.out.println("Fajl "+file);
				
				String oznaka="";
				int pozicija=-1;
				for(int i=10;i<file.getName().length();i++) {
					if(file.getName().charAt(i)=='.') {
						pozicija=i;
						break;
					}
					
				}
				
				for(int i=11;i<pozicija;i++) {
					oznaka+=file.getName().charAt(i)+"";
				}
				
				setOznaka(oznaka);
			
			String line = br.readLine();
			String[] info = line.split("#");
			
			if(info.length!=4) {
				throw new MissingFormatArgumentException("Kompozicija nema sve komponente!");
			}
			
			
			int brzina=Integer.parseInt(info[1]);
			if(brzina<500) {
				throw new MissingFormatArgumentException("Minimalna brzina je 0.5s!");
			}
			else 
			{
				setBrzinaKretanja(Integer.parseInt(info[1]));
			}
			
			String[] putanja  = info[2].split("-");
			if(putanja[0].length()>1) {
				
				//kompozicija moze kretati sa stanice A i E ili izvan mape
				if(putanja[0].charAt(1) == '0' && (putanja[0].charAt(0) == 'A' || (putanja[0].charAt(0) == 'E'))) {
					setKrajMape(true);
					
					if(putanja[0].charAt(0) == 'A') {
						setX(29);
						setY(2);
					}
					else if(putanja[0].charAt(0) == 'E') {
						setX(25);
						setY(29);
					}
				}
				else {
					throw new MissingFormatArgumentException("Kompozicija nije pravilno formulisana!");
				}
				

			}
			String str = putanja[0].charAt(0)+""; //pocetna stanica
			
			if(str.equals("A") || str.equals("B") || str.equals("C") || str.equals("D") || str.equals("E"))
				setPocetna(Main.stanice.get(str));
			
			else
				throw new MissingFormatArgumentException("Naziv pocetne zeljeznicke stanice nije ispravan!");
			
			if(putanja[1].length()>1) {
				throw new MissingFormatArgumentException("Naziv krajnje zeljeznicke stanice nije ispravan!");
			}
			
			if(putanja[1].equals("A") || putanja[1].equals("B") || putanja[1].equals("C") || putanja[1].equals("D") || putanja[1].equals("E"))
				setKrajnja(Main.stanice.get(putanja[1])); //krajnja stanica
			
			else
				throw new MissingFormatArgumentException("Naziv krajnje zeljeznicke stanice nije ispravan!");
			
			if(getPocetna().getName().equals(getKrajnja().getName()))
				setIstaPocetnaIKrajnja(true);
			
			String[] dijelovi = info[0].split("-");
			
			for(int j=0;j<dijelovi.length;j++) {
				
				if(dijelovi[j].length() < 2)
					throw new MissingFormatArgumentException("Nepotpun format kompozicije!");
				
				char c = dijelovi[j].charAt(0);
				int tip = Integer.parseInt(dijelovi[0].substring(1));
				tip = Integer.parseInt(dijelovi[j].substring(1));
				
				Lokomotiva l=null;
				Vagon v=null;
				
				if(c == 'L') {
					
					switch(tip) {
					case 1: l = new ManevarskaL("ML", new Random().nextInt(3500)+501); break;
					case 2: l = new TeretnaL("TL", new Random().nextInt(3500)+501); break;
					case 3: l = new PutnickaL("PL", new Random().nextInt(3500)+501); break;
					case 4: l = new UniverzalnaL("UL", new Random().nextInt(3500)+501); break;
					
					default : throw new MissingFormatArgumentException("Vrsta lokomotive nije ispravna!");
					}
					
					String pogon=info[3];
					if(pogon.equals("E")) {
						l.setElektricna(true);
						l.setPogon(Pogon.ELEKTRICNI);
						l.postaviBoju("sienna");
					}
					else if(pogon.equals("D")) {
						l.setPogon(Pogon.DIZEL);
					}
					else if(pogon.equals("P")) {
						l.setPogon(Pogon.PARNI);
					}
					else {
						throw new MissingFormatArgumentException("Pogon nije pravilno formulisan!");
					}
					 
					addLokomotiva(l);
						
				}
				else if(c == 'V') {
					switch(tip) {
					case 1: v = new PosebneNamjeneV(new Random().nextInt(6)+10, "PN");break;
					case 2: v = new TeretniV(new Random().nextInt(6)+10, "PVR", new Random().nextInt(3000)+1500);break;						
					case 3: {
						int temp = new Random().nextInt(4)+1;
						switch(temp) {
						case 1: v= new PVagonRestorani(new Random().nextInt(6)+10, "PVR", "Ovo je vagon restoran"); break;
						case 2: v= new PVagonSaLezajima(new Random().nextInt(6)+10, "PVSL", new Random().nextInt(20)+25); break;
						case 3: v= new PVagonSaSjedistima(new Random().nextInt(6)+10, "PVSS", new Random().nextInt(50)+55);  break;
						case 4: v= new PVagonZaSpavanje(new Random().nextInt(6)+10, "PVZS"); break;
						}
					} break;
					default : throw new MissingFormatArgumentException("Vrsta putnickog vagona nije ispravna!");
					}
					
					if(v!=null) {
						 addVagon(v);
					}
											
				}
				else {
					throw new MissingFormatArgumentException("Dio kompozicije moze biti oznacen sa L ili V!");
				}
				
			}
						
			}catch (FileNotFoundException e) {
				Main.loggerKompozicija.log(Level.SEVERE, e.fillInStackTrace().toString());
			} catch (IOException e) {
				Main.loggerKompozicija.log(Level.SEVERE, e.fillInStackTrace().toString());
			}
			
			
		boolean provjera = provjeriKompoziciju(this);
		if(!provjera)
			throw new MissingFormatArgumentException("Kompozicija nije pravilno formulisana!");
			
		
		
		setNaelektrisanje(false);
		long start = System.currentTimeMillis();
		boolean naStanici = false;
		setKompozicija();
		
			
		if(!istaPocetnaIKrajnja) {
			while(!trenutna.equals(krajnja.getName())) {
				naStanici=false;
				int[] naelektrisanjeIspred=new int[2];
				
			while(true) {
				if(x==0 && y==0) {
					String narednaS = pocetna.narednaStanica.get(krajnja.getName()); //koja je naredna stanica na putu do krajnje
					Integer[] temp = pocetna.koordinate.get(narednaS); //koordinate prvog polja pri kretanju ka narednoj stanici na putu do krajnje
					x = temp[0];y = temp[1];
					Kontroler kk = pocetna.getKontroler(narednaS);  //kontroler prema narednoj stanici
					kk.isZauzetaDionica(this, pocetna);  //provjeri ide li neki voz prema nasoj stanici
					
					control = Main.stanice.get(narednaS).getKontroler(pocetna.getName()); //zauzmi dionicu prema narednoj stanici od pocetne, tako da ne moze krenuti voz od naredne stanice prema pocetnoj
					control.zauzmiDionicu();
					
					PruzniPrelaz[] prelazi = getPrelaze(narednaS);
					if(prelazi[0] != null) {
						prelazi[0].zauzmiPrelaz(); //da vozila moraju cekati od pocetka kretanja voza iz stanice, pa sve dok ne prodje prelaz
						prelazi[1].zauzmiPrelaz();
					}
				}
				{{
					elementi.get(0).setX(x);	elementi.get(0).setY(y); //prvi element je lokomotiva
					
					if(istorija[x][y] != 1) {
					istorija[x][y] = 1;
					
					ArrayList<Integer> polje=new ArrayList<>();
					polje.add(x);
					polje.add(y);
					koraciPoRedu.put(korak++, polje);
					}
					 
					
					if(Main.map.getPolje(x,y).getElement()==null) { 
						setPolje(elementi.get(0),0,0); //postavi lokomotivu na polje
											
						if(Main.map.getPolje(x,y).getElement().isElektricna()) {
							Main.map.getPolje(x, y).setPodNaponom(true); //naelektrisana prva lokomotiva
							setNaelektrisanje(true);
							
							naelektrisanjeIspred=sledeciKorak(Main.map.getPolje(x,y).getElement());
							Main.map.getPolje(naelektrisanjeIspred[0], naelektrisanjeIspred[1]).setPodNaponom(true);
							Main.map.getPolje(x, y).setPodNaponom(true);
							
						}
											
						break;
						
					}
					else {
						Main.map.getPolje(x,y).setKomp(this);
						try {
							synchronized(this) {
							wait();
							continue;
							}
						}catch(InterruptedException e) {
							Main.loggerKompozicija.log(Level.SEVERE, e.fillInStackTrace().toString());}
					}
				}}
			
			
			}
			try {
				sleep(brzinaKretanja);
			}catch(InterruptedException e) {
				Main.loggerKompozicija.log(Level.SEVERE, e.fillInStackTrace().toString());}
			
			
			Main.map.getPolje(naelektrisanjeIspred[0], naelektrisanjeIspred[1]).setPodNaponom(false);
			Main.map.getPolje(x, y).setPodNaponom(false);
			
			
			if(isNaelektrisanje()) {
				Main.map.getPolje(x, y).setPodNaponom(false);
			}
			
			
			int oslobodjeniPrelazi=0;
			
			while(true) {
				for(int i=1;i<elementi.size();i++) { 
					
					int[] nove = sledeciKorak(elementi.get(0)); //nove su sledeca pozicija prve lokomotive
					if(Main.map.getPolje(nove[0], nove[1]).isZeljeznickaStanica()) {
						break;
					}
					
					istorija[nove[0]][nove[1]] = 1;  
					
					ArrayList<Integer> polje=new ArrayList<>();
					polje.add(nove[0]);
					polje.add(nove[1]);
					koraciPoRedu.put(korak++, polje);
					
					int xnew = -1,ynew = -1,xold = -1,yold = -1;
					for(int j=0;j<=i;j++) {
						xold = elementi.get(j).getX();
						yold = elementi.get(j).getY();
						
						if(j+1==elementi.size()) { 
						if(Main.map.getPolje(xold, yold).isPruzniPrelaz()) {
							PruzniPrelaz[] prelazi = getPrelaze(Main.stanice.get(trenutna).narednaStanica.get(krajnja.getName()));
							if(oslobodjeniPrelazi==1)
							{
							
							if(prelazi[1] != null) {
							
							prelazi[1].oslobodiPrelaz();
								}
							oslobodjeniPrelazi=0;
							}else {
								prelazi[0].oslobodiPrelaz();
								oslobodjeniPrelazi++;
							}}
						setPolje(null,xold,yold);
						}
						if(j==0) {
								elementi.get(0).setX(nove[0]);	elementi.get(0).setY(nove[1]); //neka prva lokomotiva ide naprijed
								if(Main.map.getPolje(nove[0],nove[1]).getElement()!=null) { 
									
									Main.map.getPolje(nove[0],nove[1]).setKomp(this);
									try {
										synchronized(this) { //ne moze jos ici naprijed
										wait();
										elementi.get(0).setX(xold);	elementi.get(0).setY(yold); //zadrzi poziciju
										j--;
										continue;
										}
									}catch(InterruptedException e) {
										Main.loggerKompozicija.log(Level.SEVERE, e.fillInStackTrace().toString());
									}
								}
							}
						else {
							elementi.get(j).setX(xnew);	elementi.get(j).setY(ynew); 
							}
						setPolje(elementi.get(j),0,0);
						if(isNaelektrisanje()) {
							Main.map.getPolje(xold, yold).setPodNaponom(true); 
						}
						
						xnew = xold;
						ynew = yold;
					}
					
					try {
						sleep(brzinaKretanja);
					}catch(InterruptedException e) {
						Main.loggerKompozicija.log(Level.SEVERE, e.fillInStackTrace().toString());
					}
	
				}
				
				
				while(true) {
					int[] nove = sledeciKorak(elementi.get(0));
					istorija[nove[0]][nove[1]] = 1;
					
					int xnew = -1,ynew = -1,xold = -1,yold = -1;
					if(!(Main.map.getPolje(nove[0], nove[1]).isZeljeznickaStanica())) {
						
						ArrayList<Integer> polje2=new ArrayList<>();
						polje2.add(nove[0]);
						polje2.add(nove[1]);
						koraciPoRedu.put(korak++, polje2);
						
					for(int j=0;j<elementi.size();j++) { //pomjeranje naprijed
						xold = elementi.get(j).getX();
						yold = elementi.get(j).getY();
						if(j+1==elementi.size()) {
						if(Main.map.getPolje(xold, yold).isPruzniPrelaz()) {
							PruzniPrelaz[] prelazi = getPrelaze(Main.stanice.get(trenutna).narednaStanica.get(krajnja.getName()));
							if(oslobodjeniPrelazi==1)
							{
								if(prelazi[1] != null) {
							
								prelazi[1].oslobodiPrelaz();
		
								}
							oslobodjeniPrelazi=0;
							}else {
								prelazi[0].oslobodiPrelaz();
								oslobodjeniPrelazi++;
							}}
						setPolje(null,xold,yold);
						}
						if(j==0) {
								elementi.get(0).setX(nove[0]);	elementi.get(0).setY(nove[1]);
								if(Main.map.getPolje(nove[0],nove[1]).getElement()!=null) { 
									
									Main.map.getPolje(nove[0],nove[1]).setKomp(this);
									try {
										synchronized(this) {
										wait();
										j--;
										elementi.get(0).setX(xold);	elementi.get(0).setY(yold);
										continue;
										}
									}catch(InterruptedException e) {
										Main.loggerKompozicija.log(Level.SEVERE, e.fillInStackTrace().toString());
									}
								}
							
							}
						else {
							elementi.get(j).setX(xnew);	elementi.get(j).setY(ynew);
							}
						setPolje(elementi.get(j),0,0);
						xnew = xold;
						ynew = yold;
						
					}
					try {
						sleep(brzinaKretanja);
					}catch(InterruptedException e) {
						Main.loggerKompozicija.log(Level.SEVERE, e.fillInStackTrace().toString());
					}
				}
				else 
				{
					
					
					String bivsa = trenutna;
					trenutna = ((ZeljeznickaStanica) Main.map.getPolje(nove[0], nove[1])).getName();
					
					
					for(int i=0;i<elementi.size();i++) {
						for(int j=i;j<elementi.size();j++) {
						xold = elementi.get(j).getX();
						yold = elementi.get(j).getY();
						if(j>i) {
							
							elementi.get(j).setX(xnew); elementi.get(j).setY(ynew);
							setPolje(elementi.get(j),0,0);
						}
						xnew=xold;
						ynew = yold;
						
						if(j+1==elementi.size())
							if(Main.map.getPolje(xnew, ynew).isPruzniPrelaz()) {
								
								String temp = trenutna;
								trenutna = bivsa;
							
								PruzniPrelaz[] prelazi = getPrelaze(temp);
								trenutna = temp;
								if(oslobodjeniPrelazi==1)
								{
									if(prelazi[1] != null) {
								
									prelazi[1].oslobodiPrelaz();
								
									}
								oslobodjeniPrelazi=0;
								}else {
									prelazi[0].oslobodiPrelaz();
									oslobodjeniPrelazi++;
								}}
							setPolje(null,xnew,ynew);
					}
						
						
						try {
							sleep(brzinaKretanja);
						}catch(InterruptedException e) {
							Main.loggerKompozicija.log(Level.SEVERE, e.fillInStackTrace().toString());
						}
						
					}
					
					
					
					String narednaS="";
					if(trenutna != krajnja.getName())
						narednaS = Main.stanice.get(trenutna).narednaStanica.get(krajnja.getName());
					Integer[] temp = Main.stanice.get(trenutna).koordinate.get(narednaS);
					naStanici = true;
					if(temp != null) {
					x = temp[0];y = temp[1];
					}
					break;
				}
				}
				
				
				
				if(naStanici) {
					if(control!= null)
						control.oslobodiDionicu();
					if(!trenutna.equals(krajnja.getName())) {
					ZeljeznickaStanica tr = Main.stanice.get(trenutna);
					String narednaS = tr.narednaStanica.get(krajnja.getName()); //naredna stanica na putu
					control = Main.stanice.get(narednaS).getKontroler(trenutna);
					Kontroler l2 = tr.getKontroler(narednaS);
					l2.isZauzetaDionica(this,tr);
					if(control!=null) {
						control.zauzmiDionicu();
						PruzniPrelaz[] prelazi = getPrelaze(narednaS);
						if(prelazi[0] != null) {
						prelazi[0].zauzmiPrelaz();
						prelazi[1].zauzmiPrelaz();
						}
					}
					
					}
					break;
				}
			}
		}
			long end = System.currentTimeMillis();
			setVrijemeKretanja((end-start)/1000);
			setKrajKretanja(true);
			SimpleDateFormat  sdf=new SimpleDateFormat ("dd.MM.yy_HH_mm_ss");
			String date= sdf.format(new Date());
			setVrijemeKraja(date);
			
			serijalizacija();
			
		
	}
		
		
		
	else {
			if(pocetna.getName().equals("A") || pocetna.getName().equals("E")) {
				elementi.get(0).setX(x);	elementi.get(0).setY(y);
				
				while(!naStanici) {
					setPolje(elementi.get(0),0,0);
			
					istorija[x][y] = 1;  
					
					ArrayList<Integer> polje=new ArrayList<>();
					polje.add(x);
					polje.add(y);
					koraciPoRedu.put(korak++, polje);
					
					try {
						sleep(brzinaKretanja);
					}catch(InterruptedException e) {
						Main.loggerKompozicija.log(Level.SEVERE, e.fillInStackTrace().toString());
					}
					
					while(true) {
					
					int[] nove1 = sledeciKorak(elementi.get(0)); 
					
					if(!Main.map.getPolje(nove1[0], nove1[1]).isZeljeznickaStanica()) {
						
						for(int i=1;i<elementi.size();i++) { 
							
							int[] nove = sledeciKorak(elementi.get(0)); 
							
							int xnew = -1,ynew = -1,xold = -1,yold = -1;
							for(int j=0;j<=i;j++) {
								xold = elementi.get(j).getX();
								yold = elementi.get(j).getY();
								
								if(istorija[xold][yold] != 1 && xold!=0 && yold!=0) {
									
									istorija[xold][yold] = 1;  
									
									polje=new ArrayList<>();
									polje.add(xold);
									polje.add(yold);
									koraciPoRedu.put(korak++, polje);
									
									}
								
								if(j+1==elementi.size()) { 
							
									setPolje(null,xold,yold);
								}
								if(j==0) {
										elementi.get(0).setX(nove[0]);	elementi.get(0).setY(nove[1]);
										}
								else {
									elementi.get(j).setX(xnew);	elementi.get(j).setY(ynew); 
									}
								setPolje(elementi.get(j),0,0);
								
								xnew = xold;
								ynew = yold;
							}
							
							try {
								sleep(brzinaKretanja);
							}catch(InterruptedException e) {
								Main.loggerKompozicija.log(Level.SEVERE, e.fillInStackTrace().toString());
							}
			
						}
					}
					
					break;
					}
						int xnew = -1,ynew = -1,xold = -1,yold = -1;
						
						for(int i=0;i<elementi.size();i++) {
							for(int j=i;j<elementi.size();j++) {
							xold = elementi.get(j).getX();
							yold = elementi.get(j).getY();
							if(j>i) {
								
								elementi.get(j).setX(xnew); elementi.get(j).setY(ynew);
								setPolje(elementi.get(j),0,0);
							}
							xnew=xold;
							ynew = yold;
							
							if(j+1==elementi.size())
								setPolje(null,xnew,ynew);
						}
						
						try {
							sleep(brzinaKretanja);
						}catch(InterruptedException e) {
							Main.loggerKompozicija.log(Level.SEVERE, e.fillInStackTrace().toString());
						}
				}
						naStanici = true;	
						
			}//kraj true
					
			}
			long end = System.currentTimeMillis();
			setVrijemeKretanja((end-start)/1000);
			setKrajKretanja(true);
			SimpleDateFormat  sdf=new SimpleDateFormat ("dd.MM.yy_HH_mm_ss");
			String date= sdf.format(new Date());
			setVrijemeKraja(date);
			
			serijalizacija();
			
		} 
		}catch(MissingFormatArgumentException e) {
			Main.loggerKompozicija.log(Level.SEVERE, e.fillInStackTrace().toString());
		}
		 
		
	}
	
	private void setPolje(Element e,int x1,int y1) {
		int x ,y;
		if(e == null) {
			x = x1;
			y = y1;
		}
		else {
			x = e.getX();
			y = e.getY();
		}
		
		Main.map.getPolje(x,y).setElement(e);
	}

	public boolean isKrajKretanja() {
		return krajKretanja;
	}

	public void setKrajKretanja(boolean krajKretanja) {
		this.krajKretanja = krajKretanja;
	}

	public String getOznaka() {
		return oznaka;
	}

	public void setOznaka(String oznaka) {
		this.oznaka = oznaka;
	}

	public String getVrijemeKraja() {
		return vrijemeKraja;
	}

	public void setVrijemeKraja(String vrijemeKraja) {
		this.vrijemeKraja = vrijemeKraja;
	}

	
}
