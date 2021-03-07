package role.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.imageio.ImageIO;

import role.main.Common;
import role.main.MainPanel;

public class Map implements Common{
	private int row, col;
	private int data[][];
	private static BufferedImage image;
	private String name;
	private ArrayList<Chara> charas;

	public Map(String mapFile, String eventFile){
		charas = new ArrayList<Chara>();
		loadData(mapFile);
		if(image == null) loadImage();
	}

	private void loadData(String name){
		BufferedReader br = null;
		try{
			br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(name)));
			String line = br.readLine();
			name = line;
			line = br.readLine();
			col = Integer.parseInt(line);
			line = br.readLine();
			row = Integer.parseInt(line);

			data = new int[row][col];
			for(int i = 0; i < row; i++){
				line = br.readLine();
				String[] lineData = line.split(",");
				for(int j = 0; j < col; j++){
					data[i][j] = Integer.parseInt(lineData[j]);
				}
			}
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			try{
				if(br != null) br.close();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}

	private void loadImage(){
		try{
			image = ImageIO.read(getClass().getResource("/res/img/map.png"));
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	public boolean isHit(Chara c){
		int nextX = c.getNextX();
		int nextY = c.getNextY();
		Rectangle rect = new Rectangle(nextX * CHIP_SIZE, nextY * CHIP_SIZE, CHIP_SIZE, CHIP_SIZE);
		int mapData = data[nextX][nextY];
		if(mapData == 0) return true;
		for(Chara other : charas){
			if(c.equals(other)) continue;
			if(other.getX() == nextX && other.getY() == nextY) return true;

			Rectangle otherRect = new Rectangle(other.getPx(), other.getPy(), CHIP_SIZE, CHIP_SIZE);
			if(rect.intersects(otherRect)) return true;
			// otherRect = new Rectangle(other.getNextX() * CHIP_SIZE, other.getNextY() * CHIP_SIZE, CHIP_SIZE, CHIP_SIZE);
			// if(rect.intersects(otherRect) && other.isMoving()) return true;
		}

		return false;
	}

	public void draw(Graphics g, int x, int y){
		drawMap(g, x, y);
		drawChara(g, x, y);
	}

	private void drawMap(Graphics g, int x, int y){
		int firstTileX = pixelsToTiles(-x);
		int lastTileX = firstTileX + pixelsToTiles(MainPanel.WIDTH) + 1;
		lastTileX = Math.min(lastTileX, col);

		int firstTileY = pixelsToTiles(-y);
		int lastTileY = firstTileY + pixelsToTiles(MainPanel.HEIGHT) + 1;
		lastTileY = Math.min(lastTileY, row);

		for(int i = firstTileY; i < lastTileY; i++){
			for(int j = firstTileX; j < lastTileX; j++){
				int cx = (data[i][j] % 8) * MASS;
				int cy = (data[i][j] / 8) * MASS;
				g.drawImage(image, j * CHIP_SIZE + x, i * CHIP_SIZE + y, (j + 1) * CHIP_SIZE + x, (i + 1) * CHIP_SIZE + y, cx, cy, cx + MASS, cy + MASS, null);
			}
		}
	}

	private void drawChara(Graphics g, int x, int y){
		for(Chara c : charas){
			c.draw(g, x, y);
		}
	}

	private int pixelsToTiles(double pixels){
		return (int) Math.floor(pixels / CHIP_SIZE);
	}

	public int getRow(){
		return row;
	}

	public int getCol(){
		return col;
	}

	public int getWidth(){
		return col * CHIP_SIZE;
	}

	public int getHeight(){
		return row * CHIP_SIZE;
	}

	public String getName(){
		return name;
	}

	public void addChara(Chara c){
		charas.add(c);
	}

	public void removeChara(Chara c){
		charas.remove(c);
	}

	public Chara getChara(int x, int y){
		for(Chara c : charas){
			if(c.getX() == x && c.getY() == y) return c;
		}
		return null;
	}

	public ArrayList<Chara> getCharas(){
		return charas;
	}

	public boolean isMoveNoOne(){
		for(Chara c : charas){
			if(c.isMoving()) return false;
		}
		return true;
	}
}
