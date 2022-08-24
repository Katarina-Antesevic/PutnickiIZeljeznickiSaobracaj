package model.mapa;

import java.io.Serializable;
import java.util.HashMap;



@SuppressWarnings("serial")
public class ZeljeznickaStanica extends Polje implements Serializable,ZeljeznickaSaobracajnica{
	
	 
	protected String name ;
	
	public static final String boja = "gray";
	public HashMap<String,Kontroler> dionice = new HashMap<>();
	public HashMap<String,String> narednaStanica = new HashMap<>();
	public HashMap<String,Integer[]> koordinate = new HashMap<>();
	
	@Override
	public boolean isZeljeznickaStanica() {return true;}
	
	public Kontroler getKontroler(String name) {
		return dionice.get(name);
	}
	
	public void setKoord(HashMap<String,Integer[]> koordinate) {
		this.koordinate = new HashMap<>(koordinate);
	}
	
	public synchronized int[] koord(String end) {
		return new int[2];
	}
	
	public void setNarednaStanica(HashMap<String,String> ns) {
		this.narednaStanica = new HashMap<>(ns);
	}
	
	public void addDionicu(String name,Kontroler lock) {
		dionice.put(name,lock);
	}
	
	public ZeljeznickaStanica() {
		
	}
	
	public ZeljeznickaStanica(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}
