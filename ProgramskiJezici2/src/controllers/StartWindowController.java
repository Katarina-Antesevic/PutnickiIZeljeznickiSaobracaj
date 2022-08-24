package controllers;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import main.Main;
import model.kompozicija.Kompozicija;
import model.mapa.*;
import model.vagoni.Vagon;
import model.vozila.Automobil;
import model.vozila.GeneratorVozila;
import model.vozila.Kamion;
import watcher.VozilaWatcher;
import watcher.KompozicijeWatcher;

import java.io.*;

public class StartWindowController implements Initializable {
	
	@FXML
	GridPane grid ;
	@FXML
	Button start;
	@FXML
	Button detalji;
	 
	

	public static Logger logger = Logger.getLogger(StartWindowController.class.getName());
	public static Handler fileHandler;
	{
		try{
			fileHandler =  new FileHandler(Main.LOGGER_PATH+"startWindowController.log");
			logger.addHandler(fileHandler);
		}catch(IOException e){
			e.printStackTrace();
			
		}
	}
	
	
	String[][] saobracaj = new String[Main.SIZE][Main.SIZE];
	
	static Pane mapa[][] = new Pane[Main.SIZE][Main.SIZE];
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//prvo isprazniti direktorijum serijalizacije detalja kretanja
				File directory=new File(Main.DETALJI_PATH);
				for(File f: directory.listFiles()) {
					if(f.isFile()) {
						f.delete();
					}
				}
				
				File dir=new File(Main.IZUZECI_PATH);
				for(File f:dir.listFiles()) {
					if(f.isFile()) {
						f.delete();
					}
				}
		
		for(int i=0;i<Main.SIZE;i++)
			for(int j=0;j<Main.SIZE;j++) {
				mapa[i][j] = new Pane();
				mapa[i][j].setStyle("-fx-border-color: black;-fx-border-width:0.5");
			}
	
		start.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
            	new Thread(new GeneratorVozila()).start();
            	new Thread(new VozilaWatcher()).start();
            	new Thread(new KompozicijeWatcher()).start();
            	
            	
            	File dir = new File(Main.KOMPOZICIJE_PATH);
            	File[] kompozicije = dir.listFiles(new FilenameFilter() {

        			@Override
        			public boolean accept(File dir, String name) {
        				if(name.startsWith("kompozicija"))
        					return true;
        				return false;
        			}
        			
        		});
            	
            	for(File file:kompozicije) {
            		new Kompozicija(file).start();
            	}
            }
        });
		
		detalji.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				
				FXMLLoader loader = new FXMLLoader();
				 loader.setLocation(getClass().getResource("/gui/DetailsWindow.fxml"));
				 
				 Parent root = null;
			        try {
			            root = (Parent) loader.load();
			        } catch (IOException e) {
			        	logger.log(Level.SEVERE, e.fillInStackTrace().toString());
			        }
			        
			     DetailsWindowController controller = loader.getController();
			     controller.initDetalji();
			     Stage newStage = new Stage();
			     newStage.setTitle("Detalji");
			     newStage.setResizable(false);
			     newStage.getIcons().add(new Image(Main.RESOURCES_PATH+"info.png"));
			     newStage.setScene(new Scene(root, 467, 530));
			     newStage.show();
			}
		});
		
		
		grid.setGridLinesVisible(true);
	
		final int columnCount = Main.SIZE;
		final int rowCount = Main.SIZE;
		ColumnConstraints cc = new ColumnConstraints();
        cc.setFillWidth(true);
        cc.setHgrow(Priority.ALWAYS);
        cc.setPercentWidth(100d / columnCount);

        for (int i = 0; i < columnCount; i++) {
            grid.getColumnConstraints().add(cc);
        }

        RowConstraints rc = new RowConstraints();
        rc.setFillHeight(true);
        rc.setVgrow(Priority.ALWAYS);
        rc.setPercentHeight(100d / columnCount);
        
        for (int i = 0; i < rowCount; i++) {
            grid.getRowConstraints().add(rc);
        }
        initMap();
       
		
	}
	
	
	private void initMap() {
		ImageView iv1 = new ImageView();
		ImageView iv2 = new ImageView();
		ImageView iv3 = new ImageView();
		ImageView iv4 = new ImageView();
		ImageView iv5 = new ImageView();
		try {
			Image image = new Image(new File(Main.RESOURCES_PATH + "station.png").toURI().toURL().toExternalForm());
			iv1.setImage(image);
			iv2.setImage(image);
			iv3.setImage(image);
			iv4.setImage(image);
			iv5.setImage(image);
		} catch (MalformedURLException e) {
			logger.log(Level.SEVERE, e.fillInStackTrace().toString());
			
		}
		
		Label a = new Label(" A");
		a.setTextFill(Color.BLACK);
		a.setStyle("-fx-font-size: 1.5em;-fx-font-weight: bold");
		mapa[28][1].getChildren().add(a);
		Label b = new Label(" B");
		b.setStyle("-fx-font-size: 1.5em;-fx-font-weight: bold");
		b.setTextFill(Color.BLACK);
		mapa[6][6].getChildren().add(b);
		Label c = new Label(" C");
		c.setStyle("-fx-font-size: 1.5em;-fx-font-weight: bold");
		c.setTextFill(Color.BLACK);
		mapa[13][19].getChildren().add(c);
		Label d = new Label(" D");
		d.setStyle("-fx-font-size: 1.5em;-fx-font-weight: bold");
		d.setTextFill(Color.BLACK);
		mapa[2][26].getChildren().add(d);
		Label e = new Label(" E");
		e.setStyle("-fx-font-size: 1.5em;-fx-font-weight: bold");
		e.setTextFill(Color.BLACK);
		mapa[26][25].getChildren().add(e);
		mapa[27][2].getChildren().add(iv1);
		mapa[25][26].getChildren().add(iv2);
		mapa[12][20].getChildren().add(iv3);
		mapa[1][27].getChildren().add(iv4);
		mapa[5][7].getChildren().add(iv5);
		
		
		for(int i=0;i<Main.SIZE;i++)
			for(int j=0;j<Main.SIZE;j++)
				grid.add(mapa[i][j], j, i);
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(Main.MAP_PATH)));
			String line = "";
			String boja = "";
			while((line = br.readLine()) != null) {
				if(line.equals(Saobracajnica.PUTEVI.toString())) {
					boja = "lightblue";
					continue;
				}
				else if(line.equals(Saobracajnica.PRUGE.toString())) {
					boja = "darkgray";
					continue;
				}
				else if(line.equals(Saobracajnica.STANICE.toString())) {
					boja = "gray";
					continue;
				}
				else if(line.equals(Saobracajnica.PRELAZI.toString())) {
					boja = "black";
					continue;
				}
				postaviBojeNaMapu(line,boja);
			}
			br.close();
		} catch (FileNotFoundException ex) {
			logger.log(Level.SEVERE, ex.fillInStackTrace().toString());
		} catch (IOException ex) {
			logger.log(Level.SEVERE, ex.fillInStackTrace().toString());
		}
		 setSaobracaj(saobracaj);
		 setStanice();
	}
	
	private void setStanice() {
		Kontroler kontroler1 =  new Kontroler();
		Kontroler kontroler2 =  new Kontroler();
		Kontroler kontroler3 =  new Kontroler();
		Kontroler kontroler4 =  new Kontroler();
		Kontroler kontroler5 =  new Kontroler();
		Kontroler kontroler6 =  new Kontroler();
		Kontroler kontroler7 =  new Kontroler();
		Kontroler kontroler8 =  new Kontroler();
		HashMap<String,String> narednaStanica = new HashMap<>();
		HashMap<String,Integer[]> koordinate = new HashMap<>();
		Integer[] koordinata = new Integer[2];
		
		((ZeljeznickaStanica) Main.map.getPolje(27, 2)).setName("A");
		((ZeljeznickaStanica) Main.map.getPolje(28, 2)).setName("A");
		((ZeljeznickaStanica) Main.map.getPolje(27, 1)).setName("A");
		((ZeljeznickaStanica) Main.map.getPolje(28, 1)).setName("A");
		Main.stanice.put("A" ,(ZeljeznickaStanica) Main.map.getPolje(28, 2));
		((ZeljeznickaStanica) Main.map.getPolje(28, 2)).addDionicu("B", kontroler1);
		narednaStanica.put("B", "B");
		narednaStanica.put("C", "B");
		narednaStanica.put("D", "B");
		narednaStanica.put("E", "B");
		((ZeljeznickaStanica) Main.map.getPolje(28, 2)).setNarednaStanica(narednaStanica);
		koordinata[0]=26;koordinata[1] = 2; 
		koordinate.put("B", koordinata);
		((ZeljeznickaStanica) Main.map.getPolje(28, 2)).setKoord(koordinate);
		
		
		((ZeljeznickaStanica) Main.map.getPolje(6, 6)).setName("B");
		((ZeljeznickaStanica) Main.map.getPolje(6, 7)).setName("B");
		((ZeljeznickaStanica) Main.map.getPolje(5, 6)).setName("B");
		((ZeljeznickaStanica) Main.map.getPolje(5, 7)).setName("B");
		Main.stanice.put("B" ,(ZeljeznickaStanica) Main.map.getPolje(6, 6));
		((ZeljeznickaStanica) Main.map.getPolje(6, 6)).addDionicu("A", kontroler2);
		((ZeljeznickaStanica) Main.map.getPolje(6, 6)).addDionicu("C", kontroler3);
		narednaStanica.clear();narednaStanica.put("A", "A");
		narednaStanica.put("C", "C");
		narednaStanica.put("D", "C");
		narednaStanica.put("E", "C");
		((ZeljeznickaStanica) Main.map.getPolje(6, 6)).setNarednaStanica(narednaStanica);
		koordinate.clear();
		koordinata = new Integer[2];
		koordinata[0]=6;koordinata[1] = 5; 
		koordinate.put("A", koordinata);
		koordinata = new Integer[2];
		koordinata[0]=6; koordinata[1] = 8;
		koordinate.put("C",koordinata);
		((ZeljeznickaStanica) Main.map.getPolje(6, 6)).setKoord(koordinate);
		
		
		((ZeljeznickaStanica) Main.map.getPolje(12,19)).setName("C");	
		((ZeljeznickaStanica) Main.map.getPolje(12, 20)).setName("C");
		((ZeljeznickaStanica) Main.map.getPolje(13, 20)).setName("C");
		((ZeljeznickaStanica) Main.map.getPolje(13, 19)).setName("C");
		Main.stanice.put("C" ,(ZeljeznickaStanica) Main.map.getPolje(12, 19));
		((ZeljeznickaStanica) Main.map.getPolje(12, 19)).addDionicu("B", kontroler4);
		((ZeljeznickaStanica) Main.map.getPolje(12, 19)).addDionicu("D", kontroler5);
		((ZeljeznickaStanica) Main.map.getPolje(12, 19)).addDionicu("E", kontroler6);
		narednaStanica.clear();
		narednaStanica.put("A", "B");
		narednaStanica.put("B", "B");
		narednaStanica.put("D", "D");
		narednaStanica.put("E", "E");
		((ZeljeznickaStanica) Main.map.getPolje(12, 19)).setNarednaStanica(narednaStanica);
		koordinate.clear();
		koordinata = new Integer[2];
		koordinata[0]=11;koordinata[1] = 19;
		koordinate.put("B", koordinata);
		koordinata = new Integer[2];
		koordinata[0]=14; koordinata[1] = 20;
		koordinate.put("E",koordinata);
		koordinata = new Integer[2];
		koordinata[0]=12; koordinata[1] = 21;
		koordinate.put("D",koordinata);
		((ZeljeznickaStanica) Main.map.getPolje(12, 19)).setKoord(koordinate);

		((ZeljeznickaStanica) Main.map.getPolje(1, 26)).setName("D");
		((ZeljeznickaStanica) Main.map.getPolje(2, 26)).setName("D");
		((ZeljeznickaStanica) Main.map.getPolje(1, 27)).setName("D");
		((ZeljeznickaStanica) Main.map.getPolje(2, 27)).setName("D");
		Main.stanice.put("D" ,(ZeljeznickaStanica) Main.map.getPolje(2, 26));
		((ZeljeznickaStanica) Main.map.getPolje(2,26)).addDionicu("C", kontroler7);
		narednaStanica.clear();
		narednaStanica.put("A", "C");
		narednaStanica.put("B", "C");
		narednaStanica.put("C", "C");
		narednaStanica.put("E", "C");
		((ZeljeznickaStanica) Main.map.getPolje(2, 26)).setNarednaStanica(narednaStanica);
		koordinate.clear();
		koordinata = new Integer[2];
		koordinata[0]=1; koordinata[1] = 25;
		koordinate.put("C",koordinata);
		((ZeljeznickaStanica) Main.map.getPolje(2, 26)).setKoord(koordinate);
		
		((ZeljeznickaStanica) Main.map.getPolje(25, 26)).setName("E");
		((ZeljeznickaStanica) Main.map.getPolje(26, 25)).setName("E");
		((ZeljeznickaStanica) Main.map.getPolje(25, 25)).setName("E");
		((ZeljeznickaStanica) Main.map.getPolje(26, 26)).setName("E");
		Main.stanice.put("E" ,(ZeljeznickaStanica) Main.map.getPolje(26, 25));
		((ZeljeznickaStanica) Main.map.getPolje(26, 25)).addDionicu("C", kontroler8);
		narednaStanica.clear();
		narednaStanica.put("A", "C");
		narednaStanica.put("B", "C");
		narednaStanica.put("C", "C");
		narednaStanica.put("D", "C");
		((ZeljeznickaStanica) Main.map.getPolje(26, 25)).setNarednaStanica(narednaStanica);
		koordinate.clear();
		koordinata = new Integer[2];
		koordinata[0]=24; koordinata[1] = 26; 
		koordinate.put("C",koordinata);
		((ZeljeznickaStanica) Main.map.getPolje(26, 25)).setKoord(koordinate);
		
	}
	
	private void setSaobracaj(String[][] saobracaj) {
		for(int i=0;i<30;i++)
			for(int j=0;j<30;j++) {
				if(saobracaj[i][j] != null) {
					switch(saobracaj[i][j]) {
					case "PUTEVI" : Main.map.setPolje(i, j, new Put(i,j));break;
					case "PRUGA" : Main.map.setPolje(i, j, new Pruga(i,j)); break;
					case "STANICA": Main.map.setPolje(i, j, new ZeljeznickaStanica());break;
					case "PRELAZ" : Main.map.setPolje(i, j, new PruzniPrelaz(i,j));break;
					}
				}
			}
	}
	
	public static void setBoja(String boja,int x,int y,Polje p) {
		if(p!=null) {
			Element e = p.getElement();
			if(e != null) {
			if(e.isAutomobil())
				boja = Automobil.boja;
			else if(e.isKamion())
				boja = Kamion.boja;
			else if(e.isLokomotiva()) {
				if(e.isElektricna())
				boja = "darkblue";
				else boja="sienna";
			}
			else if(e.isVagon())
				boja = Vagon.boja;
			} 
			else {
				if(p.isPut())
					boja = Put.boja;
				else if(p.isPruga())
					boja = Pruga.boja;
				else if(p.isPruzniPrelaz())
					boja = PruzniPrelaz.boja;
				else if(p.isZeljeznickaStanica())
					boja = ZeljeznickaStanica.boja;
			}
		}
		if(x!= 0 || y!=0)
		mapa[x][y].setStyle("-fx-background-color:" + boja + ";-fx-border-color: black;-fx-border-width:0.5");
	
	}

	public void postaviBojeNaMapu(String ruta,String boja) {
		String vrstaPuta = "";
		switch(boja) {
			case "lightblue": vrstaPuta = "PUTEVI"; break;
			case "darkgray": vrstaPuta = "PRUGA";break;
			case "gray" : vrstaPuta = "STANICA";break;
			case "black": vrstaPuta = "PRELAZ";break;
		}
		int[][] temp = importMapa(ruta);
		if(temp[0].length==2 && temp[1].length==2) {
			for(int i=temp[0][0];i<=temp[0][1];i++)
				for(int j=temp[1][0];j<=temp[1][1];j++) {
					setBoja(boja,i,j,null);
					saobracaj[i][j]=vrstaPuta;
				}
		}else if(temp[0].length==2 && temp[1].length==1) {
			for(int i=temp[0][0],j=temp[1][0];i<=temp[0][1];i++) {
				setBoja(boja,i,j,null);
				saobracaj[i][j]=vrstaPuta;
			}
		}else if(temp[0].length == 1 && temp[1].length ==2) {
			for(int i=temp[0][0],j=temp[1][0];j<=temp[1][1];j++) {
				setBoja(boja,i,j,null);
				saobracaj[i][j]=vrstaPuta;
			}
		}else {
			setBoja(boja,temp[0][0],temp[1][0],null);
			saobracaj[temp[0][0]][temp[1][0]]=vrstaPuta;
		}
		
 	}
	
	public int[][] importMapa(String ruta) { //npr 20-21,0-8
		String[] xy = ruta.split("#"); //xy[0]=20-21  xy[1]=0-8
		int x = -1;
		int[] xa = new int[2]; 
		int y=-1;
		int[] ya = new int[2];
		if(xy[0].contains("-")) {
			xa[0] = Integer.parseInt(xy[0].split("-")[0]); //xa[0]=20
			xa[1] = Integer.parseInt(xy[0].split("-")[1]);//xa[1]=21
		}
		else {
			x = Integer.parseInt(xy[0]); 
		}
		if(xy[1].contains("-")) {
			ya[0] = Integer.parseInt(xy[1].split("-")[0]); //ya[0]=0
			ya[1] = Integer.parseInt(xy[1].split("-")[1]);//ya[1]=8
		}
		else {
			y = Integer.parseInt(xy[1]);
		}
		int[] xend = new int[0];
		if(x==-1) { //bila je crtica
			xend = xa; // xend[0]=20 xend[1]=21
		}
		else {
			xend = new int[] {x}; //exnd=8
		}
		int[] yend = new int[0];
		if(y==-1) {
			yend = ya;
		}
		else {
			yend = new int[] {y};
		}
		
		int[][] result = new int[][] {xend,yend};
		return result;
	}
}

enum Saobracajnica{
	PUTEVI,
	PRUGE,
	STANICE,
	PRELAZI;
	
}