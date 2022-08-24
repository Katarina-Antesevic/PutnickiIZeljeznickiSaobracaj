package controllers;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import main.Main;
import model.kompozicija.Kompozicija;
import model.mapa.ZeljeznickaStanica;
 
public class DetailsWindowController implements Initializable { 
	@FXML
	Label label; 
	@FXML
	ListView<String> listView;
	
	public static Logger logger = Logger.getLogger(DetailsWindowController.class.getName());
	public static Handler fileHandler;
	{
		try{
			fileHandler =  new FileHandler(Main.LOGGER_PATH+"detailsWindowController.log");
			logger.addHandler(fileHandler);
		}catch(IOException e){
			e.printStackTrace();
			
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		
	}

	public void initDetalji() {
		
		
		File[] files=new File(Main.DETALJI_PATH).listFiles();
		 if(files.length==0)
			 listView.getItems().add("Ni jedna kompozicija nije zavrsila kretanje! " );
		 
		 else {
			
			 FileInputStream citac;
			 for(File f:files){
					try{
						String s="";
						citac=new FileInputStream(f);
						ObjectInputStream citajObjekat=new ObjectInputStream(citac);
						Kompozicija k=(Kompozicija) citajObjekat.readObject();
						if(k.getzStanice().size()!=0) {
							
							for(ZeljeznickaStanica z:k.getzStanice())
								s+=z.getName()+" ";
							
						}
						listView.getItems().add(k.getVrijemeKraja()+": Kompozicija"+ k.getOznaka() +":");
						if(k.getKorak()==0) {
							listView.getItems().add("KOMPOZICIJA SE NIJE POMJERILA SA STANICE "+k.getKrajnja().getName() );
							listView.getItems().add("");
						}
						else {
							if(!k.isIstaPocetnaIKrajnja()) {
								
								if(!k.isKrajMape()) {
								listView.getItems().add("PUT OD STANICE "+k.getPocetna().getName()+" DO "+k.getKrajnja().getName()+" TRAJAO JE "+k.getVrijemeKretanja()+"s" );
								}
								else {
									listView.getItems().add("PUT OD KRAJA MAPE, STANICE "+k.getPocetna().getName()+", PA DO STANICE "+k.getKrajnja().getName()+" TRAJAO JE "+k.getVrijemeKretanja()+"s" );
									
								}
						}
							
						else {
							listView.getItems().add("PUT OD KRAJA MAPE DO STANICE "+k.getKrajnja().getName()+" TRAJAO JE "+k.getVrijemeKretanja()+"s" );
						
						}
						if(k.getzStanice().size()!=0) {
							listView.getItems().add("KOMPOZICIJA SE ZAUSTAVILA NA ZELJEZNICKIM STANICAMA: "+s);
						}
						listView.getItems().add("TACKE KOJE JE KROZ KOJE JE PROSLA KOMPOZICIJA:");
						for(int i=0;i<k.getKorak();i++) {
							int x=k.koraciPoRedu.get(i).get(0);
							int y=k.koraciPoRedu.get(i).get(1);
							listView.getItems().add("Kompozicija prosla kroz tacku ["+x+"]"+"["+y+"]");
						}
						
						listView.getItems().add("");
						citajObjekat.close();
							citac.close();
						}
					}catch (FileNotFoundException e) {
						logger.log(Level.SEVERE, e.fillInStackTrace().toString());
				} catch (Exception e) {
					logger.log(Level.SEVERE, e.fillInStackTrace().toString());
				}}
		 }
		 fileHandler.close();
	
		
	}
				 
	
	
}

