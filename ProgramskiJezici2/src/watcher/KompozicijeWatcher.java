package watcher;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;

import java.io.File;
import java.io.IOException;
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

import main.Main;
import model.kompozicija.Kompozicija;

public class KompozicijeWatcher extends Thread {
	 
	public static Logger logger = Logger.getLogger(KompozicijeWatcher.class.getName());
	public static Handler fileHandler;
	{
		try{
			fileHandler =  new FileHandler(Main.LOGGER_PATH+"kompozicijeWatcher.log");
			logger.addHandler(fileHandler);
		}catch(IOException e){
			e.printStackTrace();
			
		}
	}
	
	public void run() {
		try {
			WatchService watcher = FileSystems.getDefault().newWatchService();
			Path dir = Paths.get(Main.KOMPOZICIJE_PATH );
			dir.register(watcher, ENTRY_CREATE);

			while (true) {
				WatchKey key;
				try {
					key = watcher.take();
				} catch (InterruptedException e) {
					logger.log(Level.SEVERE, e.fillInStackTrace().toString());
					return;
				}
				
				for (WatchEvent<?> event : key.pollEvents()) {
					try {
						sleep(10);
					}catch(InterruptedException e) {
						logger.log(Level.SEVERE, e.fillInStackTrace().toString());
					}
					WatchEvent.Kind<?> kind = event.kind();
					@SuppressWarnings("unchecked")
					WatchEvent<Path> ev = (WatchEvent<Path>) event;
					Path fileName = ev.context();
					if(fileName.toString().trim().endsWith(".txt") && kind.equals(ENTRY_CREATE)) {
						try {
							sleep(500);
						}catch(InterruptedException e) {
							logger.log(Level.SEVERE, e.fillInStackTrace().toString());
						}
						
						File file=new File(Main.KOMPOZICIJE_PATH+File.separator+fileName.toString());
						
						new Thread(new Kompozicija(file)).start();
					}
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
