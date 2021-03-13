package role.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.imageio.ImageIO;

import role.event.Event;
import role.event.MoveEvent;
import role.main.Common;
import role.main.MainPanel;

public class Map implements Common{
	private int row, col;
	private int data[][];
	private static BufferedImage image;
	private String name;
	private ArrayList<Chara> charas;
	private ArrayList<Event> events;

	public Map(String mapFile, String eventFile){
		charas = new ArrayList<Chara>();
		events = new ArrayList<Event>();
		loadData(mapFile);
		loadEvent(eventFile);
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

	private void loadEvent(String path){
		BufferedReader br = null;
		try{
			br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(path)));
			String line;

			while((line = br.readLine()) != null){
				if("".equals(line) || line.startsWith("#") || line.startsWith(",")) continue;
				String[] st = line.split(",", 0);
				String eventType = st[0];
				if("CHARA".equals(eventType)) setCharacter(st);
				else if("MOVE".equals(eventType)) setMove(st);
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

	private void setCharacter(String[] st){
		String key = st[7];
		int charaNo = Integer.parseInt(st[1]);
		int direction = Integer.parseInt(st[2]);
		int mapNo = Integer.parseInt(st[3]);
		int moveType = Integer.parseInt(st[4]);
		int x = Integer.parseInt(st[5]);
		int y = Integer.parseInt(st[6]);
		Chara c = new Chara(charaNo, direction, mapNo, moveType, x, y);
		c.setMessage(st[8]);
		c.setKey(key);
		charas.add(c);
	}

	private void setMove(String[] st){
		int x = Integer.parseInt(st[1]);
		int y = Integer.parseInt(st[2]);
		int chipNo = Integer.parseInt(st[3]);
		int destMapNo = Integer.parseInt(st[4]);
		int destX = Integer.parseInt(st[5]);
		int destY = Integer.parseInt(st[6]);
		MoveEvent m = new MoveEvent(x, y, chipNo, destMapNo, destX, destY);
		events.add(m);
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
		}
		for(Event e : events){
			if(e.getX() == nextX && e.getY() == nextY) return e.isHit();
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
				Event e = getEvent(j, i);
				if(e == null) continue;
				cx = (e.getChipNo() % 8) * MASS;
				cy = (e.getChipNo() / 8) * MASS;
				g.drawImage(image, e.getX() * CHIP_SIZE + x, e.getY() * CHIP_SIZE + y, (e.getX() + 1) * CHIP_SIZE + x, (e.getY() + 1) * CHIP_SIZE + y, cx, cy, cx + MASS, cy + MASS, null);
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

	public Event getEvent(int x, int y){
		for(Event e : events){
			if(e.getX() == x && e.getY() == y) return e;
		}
		return null;
	}

	public void removeEvent(Event e){
		events.remove(e);
	}

	public boolean isMoveNoOne(){
		for(Chara c : charas){
			if(c.isMoving()) return false;
		}
		return true;
	}
}
