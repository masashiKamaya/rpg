package role.data;

import java.io.IOException;
import java.awt.image.BufferedImage;
import java.awt.Graphics;

import javax.imageio.ImageIO;

public class Chara implements role.main.Common {
	private int No;
	private int direction;
	private int moveType;
	private int x, y;
	private String name;
	private static BufferedImage image;
	private int count;
	private int beforeCount;
	private int px, py;
	private int mapNo;
	private String message;
	private boolean isMoving;
	private int moveSpd;
	private int movingLength;

	public Chara(int No, int direction, int mapNo, int moveType, int x, int y){
		this.No = No;
		setDirection(direction);
		setMapNo(mapNo);
		this.moveType = moveType;
		this.x = x;
		this.y = y;
		isMoving = false;
		setMoveSpd(CHIP_SIZE / 8);

		if(image == null) loadImage();
		resetCount();
		beforeCount = count;
		px = x * CHIP_SIZE;
		py = y * CHIP_SIZE;
	}

	private void loadImage(){
		try{
			image = ImageIO.read(getClass().getResource("/res/img/Chara.png"));
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	public boolean move(Map map){
		if(map.isHit(this)){
			resetMove();
			return false;
		}
		switch(direction){
			case LEFT:
				return moveLeft(map);
			case RIGHT:
				return moveRight(map);
			case UP:
				return moveUp(map);
			case DOWN:
				return moveDown(map);
		}
		return false;
	}

	private boolean moveLeft(Map map){
		if(!map.isHit(this)){
			px -= moveSpd;
			if(px < 0) px = 0;
			movingLength += moveSpd;
			if(CHIP_SIZE <= movingLength){
				x--;
				if(x < 0) x = 0;
				px = x * CHIP_SIZE;
				isMoving = false;
				return true;
			}
		}else{
			resetMove();
		}
		return false;
	}

	private boolean moveRight(Map map){
		if(!map.isHit(this)){
			px += moveSpd;
			if((map.getCol() - 1) * CHIP_SIZE < px) px = (map.getCol() - 1) * CHIP_SIZE;
			movingLength += moveSpd;
			if(CHIP_SIZE <= movingLength){
				x++;
				if(map.getCol() - 1 < x) x = map.getCol() - 1;
				px = x * CHIP_SIZE;
				isMoving = false;
				return true;
			}
		}else{
			resetMove();
		}
		return false;
	}

	private boolean moveUp(Map map){
		if(!map.isHit(this)){
			py -= moveSpd;
			if(py < 0) py = 0;
			movingLength += moveSpd;
			if(CHIP_SIZE <= movingLength){
				y--;
				if(y < 0) y = 0;
				py = y * CHIP_SIZE;
				isMoving = false;
				return true;
			}
		}else{
			resetMove();
		}
		return false;
	}

	private boolean moveDown(Map map){
		if(!map.isHit(this)){
			py += moveSpd;
			if((map.getRow() - 1) * CHIP_SIZE < py) py = (map.getRow() - 1) * CHIP_SIZE;
			movingLength += moveSpd;
			if(CHIP_SIZE <= movingLength){
				y++;
				if(map.getRow() - 1 < y) y = map.getRow() - 1;
				py = y * CHIP_SIZE;
				isMoving = false;
				return true;
			}
		}else{
			resetMove();
		}
		return false;
	}

	private void resetMove(){
		isMoving = false;
		px = x * CHIP_SIZE;
		py = y * CHIP_SIZE;
	}

	public void update(int loopCount){
		if(loopCount % 5 != 0) return;

		switch(count){
			case 0:
				beforeCount = count;
				count++;
				break;
			case 1:
				if(beforeCount == 2) count--;
				else count++;
				break;
			case 2:
				beforeCount = count;
				count--;
				break;
		}
	}

	public void draw(Graphics g, int x, int y){
		x += px;
		y += py;
		int cx = (No % 8) * (MASS * 3);
		int cy = (No / 8) * (MASS * 4);
		g.drawImage(image, x, y, x + CHIP_SIZE, y + CHIP_SIZE, cx + count * MASS, cy + direction * MASS, cx + MASS * (count + 1), cy + MASS * (direction + 1), null);
	}

	public int getNo(){
		return No;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public int getMoveType(){
		return moveType;
	}

	public int getX(){
		return x;
	}

	public int getNextX(){
		int nextX = x;
		switch(direction){
			case LEFT:
				nextX--;
				break;
			case RIGHT:
				nextX++;
				break;
		}
		return nextX;
	}

	public int getY(){
		return y;
	}

	public int getNextY(){
		int nextY = y;
		switch(direction){
			case UP:
				nextY--;
				break;
			case DOWN:
				nextY++;
				break;
		}
		return nextY;
	}

	public String getName(){
		return name;
	}

	public int getPx(){
		return px;
	}

	public int getPy(){
		return py;
	}

	public void setMapNo(int mapNo) {
		this.mapNo = mapNo;
	}

	public int getMapNo() {
		return mapNo;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setMoving(boolean isMoving){
		this.isMoving = isMoving;
		movingLength = 0;
	}

	public boolean isMoving(){
		return isMoving;
	}

	public void setMoveSpd(int moveSpd){
		this.moveSpd = moveSpd;
	}

	public void resetCount(){
		count = 1;
	}
}
