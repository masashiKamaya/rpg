package role.data;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.awt.Graphics;

import javax.imageio.ImageIO;

import role.main.Common;
import role.main.MainPanel;

public class Map implements Common{
	private int row, col;
	private int data[][];
	private static BufferedImage image;
	private String name;

	public Map(String mapFile, String eventFile){
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

	public void draw(Graphics g, int x, int y){
		drawMap(g, x, y);
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

	private int pixelsToTiles(double pixels){
		return (int) Math.floor(pixels / CHIP_SIZE);
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
}
