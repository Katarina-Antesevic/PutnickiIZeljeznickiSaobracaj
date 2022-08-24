package model.mapa;

import main.Main;

public class Mapa {
	
	
	public Polje[][] mapa = new Polje[Main.SIZE][Main.SIZE];
	
	public Mapa() {
		for(int i = 0; i<Main.SIZE; i++)
			for(int j= 0; j<Main.SIZE ; j++){
				mapa[i][j] = new Polje(i,j);
			}
	}
	
	public void setPolje(int x,int y,Polje p) {
		mapa[x][y] = p;
	}
	
	public Polje getPolje(int x,int y) {
		return mapa[x][y];
	}
	
}
