package role.main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import javax.swing.JPanel;

import role.data.Map;
import role.event.Event;
import role.event.MoveEvent;
import role.manager.MapManager;
import role.data.Chara;

public class MainPanel extends JPanel implements Common, Runnable, KeyListener{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public static final int COL = 9;
	public static final int ROW = COL;
	public static final int WIDTH = COL * CHIP_SIZE;
	public static final int HEIGHT = ROW * CHIP_SIZE;
	private int count;

	private ActionKey leftKey;
	private ActionKey rightKey;
	private ActionKey upKey;
	private ActionKey downKey;
	private ActionKey zKey;
	private ActionKey xKey;
	private ActionKey enterKey;
	private Map map;
	private Chara chara;
	private Random r;

	public MainPanel(){
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setFocusable(true);

		addKeyListener(this);

		setActionKey();
		setChara();
		setMap();
		r = new Random();

		Thread loop = new Thread(this);
		loop.start();
		count = 0;
	}

	private void setActionKey(){
		leftKey = new ActionKey();
		rightKey = new ActionKey();
		upKey = new ActionKey();
		downKey = new ActionKey();
		zKey = new ActionKey(ActionKey.INITIAL);
		xKey = new ActionKey(ActionKey.INITIAL);
		enterKey = new ActionKey(ActionKey.INITIAL);
	}

	private void setChara(){
		chara = new Chara(0, 0, 0, 0, 4, 4);
	}

	private void setMap(){
		map = MapManager.getInstance().getMap(chara.getMapNo());
		map.addChara(chara);
	}

	@Override
	public void run() {
		while(true){
			checkInput();
			update();
			repaint();
			try{
				Thread.sleep(25);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
	}

	private void checkInput(){
		mainCheckInput();
	}

	private void mainCheckInput(){
		if(leftKey.isPressed()){
			if(!chara.isMoving()){
				chara.setDirection(LEFT);
				chara.setMoving(true);
			}
		}else if(rightKey.isPressed()){
			if(!chara.isMoving()){
				chara.setDirection(RIGHT);
				chara.setMoving(true);
			}
		}else if(upKey.isPressed()){
			if(!chara.isMoving()){
				chara.setDirection(UP);
				chara.setMoving(true);
			}
		}else if(downKey.isPressed()){
			if(!chara.isMoving()){
				chara.setDirection(DOWN);
				chara.setMoving(true);
			}
		}else if(zKey.isPressed()){
			System.out.println("z");
		}else if(xKey.isPressed()){
			System.out.println("x");
		}else if(enterKey.isPressed()){
			System.out.println("enter");
		}
	}

	private void update(){
		count++;
		heroMove(count);
		charaMove(count);
	}

	private void heroMove(int count){
		if(leftKey.isPressed() || rightKey.isPressed() || upKey.isPressed() || downKey.isPressed() || chara.isMoving()){
			chara.update(count);
		}else{
			chara.resetCount();
		}

		if(chara.isMoving()){
			if(chara.move(map)){
				//When the move is completed.
				checkMass();
			}
		}
	}

	private void charaMove(int count){
		for(Chara c : map.getCharas()){
			switch(c.getMoveType()){
				case 1:
					if(c.isMoving()){
						c.move(map);
						c.update(count);
						if(!c.isMoving()) c.resetCount();
					}else if(r.nextDouble() < 0.1 && map.isMoveNoOne()){
						c.setDirection(r.nextInt(4));
						c.setMoving(true);
					}
					break;
				case 2:
					if(0.98 < r.nextDouble()){
						c.setDirection(r.nextInt(4));
					}
					break;
			}
		}
	}

	private void checkMass(){
		Event e = map.getEvent(chara.getX(), chara.getY());
		if(e instanceof MoveEvent){
			MoveEvent m = (MoveEvent) e;
			if(chara.getMapNo() == m.getDestMapNo()){
				chara.trans(m.getDestX(), m.getDestY(), DOWN);
			}else{
				map.removeChara(chara);
				chara.setMapNo(m.getDestMapNo());
				chara.trans(m.getDestX(), m.getDestY(), DOWN);
				map = MapManager.getInstance().getMap(chara.getMapNo());
				map.addChara(chara);
			}
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		int offsetX = (COL / 2) * CHIP_SIZE - chara.getPx();
		offsetX = setOffset(offsetX, WIDTH, map.getWidth());
		int offsetY = (ROW / 2) * CHIP_SIZE - chara.getPy();
		offsetY = setOffset(offsetY, HEIGHT, map.getHeight());
		map.draw(g, offsetX, offsetY);
	}

	private int setOffset(int offset, int panelMax, int mapMax){
		offset = Math.min(offset, 0);
		offset = Math.max(offset, panelMax - mapMax);
		return offset;
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()){
			case KeyEvent.VK_LEFT:
				leftKey.press();
				break;
			case KeyEvent.VK_RIGHT:
				rightKey.press();
				break;
			case KeyEvent.VK_UP:
				upKey.press();
				break;
			case KeyEvent.VK_DOWN:
				downKey.press();
				break;
			case KeyEvent.VK_Z:
				zKey.press();
				break;
			case KeyEvent.VK_X:
				xKey.press();
				break;
			case KeyEvent.VK_ENTER:
				enterKey.press();
				break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch(e.getKeyCode()){
			case KeyEvent.VK_LEFT:
				leftKey.release();
				break;
			case KeyEvent.VK_RIGHT:
				rightKey.release();
				break;
			case KeyEvent.VK_UP:
				upKey.release();
				break;
			case KeyEvent.VK_DOWN:
				downKey.release();
				break;
			case KeyEvent.VK_Z:
				zKey.release();
				break;
			case KeyEvent.VK_X:
				xKey.release();
				break;
			case KeyEvent.VK_ENTER:
				enterKey.release();
				break;
		}
	}
}
