package watcher;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.io.*;

import main.Main;
import model.vozila.GeneratorVozila;

import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

import java.io.IOException;


public class VozilaWatcher extends Thread {
	public static boolean pokreni=true;
	
	public static Logger logger = Logger.getLogger(VozilaWatcher.class.getName());
	public static Handler fileHandler;
	{
		try{
			fileHandler =  new FileHandler(Main.LOGGER_PATH+"vozilaWatcher.log");
			logger.addHandler(fileHandler);
		}catch(IOException e){
			e.printStackTrace();
			
		}  
	}
 

	public void run() {
		try {
			WatchService watcher = FileSystems.getDefault().newWatchService();
			Path dir = Paths.get(Main.RESOURCES_PATH );
			dir.register(watcher, ENTRY_MODIFY);

			while (true) {
				WatchKey key;
				try {
					key = watcher.take();
				} catch (InterruptedException e) {
					logger.log(Level.SEVERE, e.fillInStackTrace().toString());
					return;
				}

				for (WatchEvent<?> event : key.pollEvents()) {
					WatchEvent.Kind<?> kind = event.kind();
					@SuppressWarnings("unchecked")
					WatchEvent<Path> ev = (WatchEvent<Path>) event;
					Path fileName = ev.context();
					if(fileName.toString().trim().endsWith(".properties") && kind.equals(ENTRY_MODIFY)) {
						
						int[] brojVozilaNaPutu=new int[3];
						synchronized(Main.brojVozila) {
							brojVozilaNaPutu[0] = Main.brojVozila[0];
							brojVozilaNaPutu[1] = Main.brojVozila[1];
							brojVozilaNaPutu[2] = Main.brojVozila[2];
						}
						
						try {
							sleep(500);
						}catch(InterruptedException e) {
							logger.log(Level.SEVERE, e.fillInStackTrace().toString());
						}
						
						
						File modifiedFile = new File(Main.RESOURCES_PATH+File.separator+fileName.toString());
						//System.out.println("\n\n "+modifiedFile);
						//System.out.println("\n\n PROMJENA U FILEU\n\n");
						
						
						int[] procitaniBrojevi=new int[3];
						String s="";
						BufferedReader br=null;
						int i=0;
						try {
							br=new BufferedReader(new FileReader(modifiedFile));
							i=0;
							
							while((s=br.readLine())!=null) {
								if(s.startsWith("BROJ_VOZILA_")) {
									String[] str=s.split(" ");
									int broj=Integer.parseInt(str[2]);
									procitaniBrojevi[i++]=broj;
									//System.out.println("Procitani broj: "+broj);
									
								}
							}
							br.close();
						}catch(IOException e) {
							logger.log(Level.SEVERE, e.fillInStackTrace().toString());
						}
						
						int isto=0;
						int manje=0;
						for(int j=0;j<3;j++) {
							if(procitaniBrojevi[j]<=brojVozilaNaPutu[j]){
								if(procitaniBrojevi[j]<brojVozilaNaPutu[j])
									manje++;
								else if(procitaniBrojevi[j]==brojVozilaNaPutu[j])
									isto++;
							}
						}
						
						if((manje==1 && isto==2) || (manje==0 && isto==3))
							pokreni=false;
						
						
						if(pokreni)
							new Thread(new GeneratorVozila()).start();
					}
					pokreni=true;
					
				}
				
				boolean valid = key.reset();
				if (!valid) {
					break;
				}
			}

		} catch (IOException e) {
			logger.log(Level.SEVERE, e.fillInStackTrace().toString());
		}
		
		
	}

}
