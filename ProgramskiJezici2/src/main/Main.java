package main;

import java.io.*;
import java.util.HashMap;
import java.util.MissingFormatArgumentException;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import controllers.DetailsWindowController;
import controllers.StartWindowController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.kompozicija.Kompozicija;
import model.mapa.*;
import model.vozila.GeneratorVozila;
import model.vozila.Vozilo;
import watcher.VozilaWatcher;
import watcher.KompozicijeWatcher;

public class Main extends Application {
	
	public static final int SIZE = 30;
	 
	public final static String RESOURCES_PATH = System.getProperty("user.dir") + "\\src\\resources\\";
	public static String LOGGER_PATH;
	public static String KOMPOZICIJE_PATH;
	public static String DETALJI_PATH;
	public static String MAP_PATH;
	public static String IZUZECI_PATH; 
	
	public static Object lock=new Object();
	
	public static int[][] pocetakLijeveTrake = {{21,0},{0,13},{20,29}};
	public static int[][] pocetakDesneTrake = {{29,8},{29,14},{29,22}};
	public static HashMap<String,ZeljeznickaStanica> stanice = new HashMap<>();
	public static long[] maxBrzina = new long[3];
	public static int[] brojVozila = new int[3];
	public static Mapa map = new Mapa();
	
	 

	public static Logger logger = Logger.getLogger(Main.class.getName());
	public static Handler fileHandler;
	{
		try{
			fileHandler =  new FileHandler(LOGGER_PATH+"main.log");
			logger.addHandler(fileHandler);
		}catch(IOException e){
			e.printStackTrace();
			
		}
	}
	
	//za vozilo logger i handler
	public static final Logger loggerVozilo = Logger.getLogger(Vozilo.class.getName());
	public static Handler fileHandlerVozilo;
	{
		try{
			fileHandlerVozilo =  new FileHandler(Main.LOGGER_PATH+"vozilo.log");
			loggerVozilo.addHandler(fileHandlerVozilo);
		}catch(IOException e){
			e.printStackTrace();
			
		}
	}
	
	//za kompoziciju
	public static final Logger loggerKompozicija = Logger.getLogger(Kompozicija.class.getName());
	public static Handler fileHandlerKompozicija;
	{
		try{
			fileHandlerKompozicija =  new FileHandler(Main.LOGGER_PATH+"kompozicija.log");
			loggerKompozicija.addHandler(fileHandlerKompozicija);
		}catch(IOException e){
			e.printStackTrace();
			
		}
	}
	
	//kontroler
	public static final Logger loggerKontroler = Logger.getLogger(Kontroler.class.getName());
	public static Handler fileHandlerKontroler;
	{
		try{
			fileHandlerKontroler =  new FileHandler(Main.LOGGER_PATH+"kontroler.log");
			loggerKontroler.addHandler(fileHandlerKontroler);
		}catch(IOException e){
			e.printStackTrace();
			
		}
	}
	
	//pruzni prelaz
	public static final Logger loggerPruzniPrelaz = Logger.getLogger(PruzniPrelaz.class.getName());
	public static Handler fileHandlerPruzniPrelaz;
	{
		try{
			fileHandlerPruzniPrelaz =  new FileHandler(Main.LOGGER_PATH+"pruzniPrelaz.log");
			loggerPruzniPrelaz.addHandler(fileHandlerPruzniPrelaz);
		}catch(IOException e){
			e.printStackTrace();
			
		}
	}
	
	
	//generator vozila
	public static Logger loggerGeneratorV = Logger.getLogger(GeneratorVozila.class.getName());
	public static Handler fileHandlerGeneratorV;
	{
		try{
			fileHandlerGeneratorV =  new FileHandler(Main.LOGGER_PATH+"generatorVozila.log");
			loggerGeneratorV.addHandler(fileHandlerGeneratorV);
		}catch(IOException e){
			e.printStackTrace();
			 
		}    
	}
	
	
	@Override
	public void start(Stage primaryStage) {
		Stage s = new Stage();
    	try {
            AnchorPane myPane = (AnchorPane) FXMLLoader.load(this.getClass().getResource("/gui/StartWindow.fxml"));
            Scene myScene = new Scene(myPane);
            s.setScene(myScene);
            s.setMaximized(true);
            s.setTitle("Zeljeznicki saobracaj");
            s.setResizable(true);
            s.getIcons().add(new Image(Main.RESOURCES_PATH + "train.png"));
            s.show();
            s.setOnCloseRequest(new EventHandler<WindowEvent>() {
    		    @Override
    		    public void handle(WindowEvent t) {
    		        Platform.exit();
    		        System.exit(0);
    		    }
    		});
        } catch (IOException e) {
        	logger.log(Level.SEVERE, e.fillInStackTrace().toString());
			
        }
		
	}
	
	
	public static void main(String[] args) {
		loadPaths();
		//System.out.println(Main.RESOURCES_PATH);
		try {
			Thread.sleep(10);
		}
		catch(InterruptedException e) {
			e.printStackTrace();
		}
		
		launch(args);
		
		fileHandler.close();
		fileHandlerVozilo.close();
		fileHandlerKompozicija.close();
		fileHandlerKontroler.close();
		fileHandlerPruzniPrelaz.close();
		fileHandlerGeneratorV.close();
		
		VozilaWatcher.fileHandler.close();
		KompozicijeWatcher.fileHandler.close();
		DetailsWindowController.fileHandler.close();
		StartWindowController.fileHandler.close();
		
		
	}
	
	
	public synchronized static void loadPaths() {
		try {
			Properties prop = new Properties();
			InputStream input = null;

			input = new FileInputStream(RESOURCES_PATH + "config.properties");
			prop.load(input);
			
			String loggerPath=prop.getProperty("LOGGER_PATH");
			String str[]=loggerPath.split("/");
			LOGGER_PATH = System.getProperty("user.dir") +"\\";
			for(String s:str) {
				LOGGER_PATH+=s+"\\";
			}
			
			String kPath=prop.getProperty("KOMPOZICIJE_PATH");
			String str1[]=kPath.split("/");
			KOMPOZICIJE_PATH = System.getProperty("user.dir") +"\\";
			for(String s:str1) {
				KOMPOZICIJE_PATH+=s+"\\";
			}
			
			String dPath=prop.getProperty("DETALJI_PATH");
			String str2[]=dPath.split("/");
			DETALJI_PATH = System.getProperty("user.dir") +"\\";
			for(String s:str2) {
				DETALJI_PATH+=s+"\\";
			}
			
			String iPath=prop.getProperty("IZUZECI_PATH");
			String str3[]=iPath.split("/");
			IZUZECI_PATH = System.getProperty("user.dir") +"\\";
			for(String s:str3) {
				IZUZECI_PATH+=s+"\\";
			}
			
			
			String mPath=prop.getProperty("MAP_PATH");
			String str4[]=mPath.split("/");
			MAP_PATH = System.getProperty("user.dir");
			for(String s:str4) {
				MAP_PATH+="\\"+s;
			}
			
		}catch(IOException e) {
			logger.log(Level.SEVERE, e.fillInStackTrace().toString());
		}
	}
	
	
	public synchronized static void loadConfig()  {
		
		
		try {
		Properties prop = new Properties();
		InputStream input = new FileInputStream(RESOURCES_PATH + "config.properties");
		prop.load(input);
		
		synchronized(Main.brojVozila){
		for(int i=1;i<=3;i++) {

		String value = prop.getProperty("BROJ_VOZILA_" + i);
		if (value != null) {
			
			brojVozila[i-1] = Integer.parseInt(value);
			
			if(brojVozila[i-1]<0) {
				
				throw new MissingFormatArgumentException("Broj vozila na " +  i + ". putu ne smije biti manji od nule.");
			}
		} else {
			throw new MissingFormatArgumentException("Broj vozila na " +  i + ". putu nije u odgovarajucem formatu.");
		}

		value = prop.getProperty("MAX_BRZINA_" + i);
		if (value != null) {
			maxBrzina[i-1] = Long.parseLong(value);
			if(maxBrzina[i-1]<1) {
				 
				throw new MissingFormatArgumentException("Maksimalna brzina na " +  i + ". putu ne smije biti manja od jedan.");
			}
		} else {
			throw new MissingFormatArgumentException("Maksimalna brzina na " + i + ". putu nije u odgovarajucem formatu.");
		}
		}}
		}catch(MissingFormatArgumentException e) {
			logger.log(Level.SEVERE, e.fillInStackTrace().toString());
		}catch(IOException e) {
			logger.log(Level.SEVERE, e.fillInStackTrace().toString());
		}
		
		
	}
}
