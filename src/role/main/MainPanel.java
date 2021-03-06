package role.main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

import role.data.Map;

public class MainPanel extends JPanel implements Common, Runnable, KeyListener{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public static final int COL = 9;
	public static final int ROW = COL;
	public static final int WIDTH = COL * CHIP_SIZE;
	public static final int HEIGHT = ROW * CHIP_SIZE;

	private ActionKey leftKey;
	private ActionKey rightKey;
	private ActionKey upKey;
	private ActionKey downKey;
	private ActionKey zKey;
	private ActionKey xKey;
	private ActionKey enterKey;
	private Map map;

	public MainPanel(){
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setFocusable(true);

		addKeyListener(this);

		setActionKey();
		setMap();

		Thread loop = new Thread(this);
		loop.start();
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

	private void setMap(){
		map = new Map("/res/map/map0.map", "");
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
		if(leftKey.isPressed()){
			System.out.println("left");
		}else if(rightKey.isPressed()){
			System.out.println("right");
		}else if(upKey.isPressed()){
			System.out.println("up");
		}else if(downKey.isPressed()){
			System.out.println("down");
		}else if(zKey.isPressed()){
			System.out.println("z");
		}else if(xKey.isPressed()){
			System.out.println("x");
		}else if(enterKey.isPressed()){
			System.out.println("enter");
		}
	}

	private void update(){}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		int offsetX = (COL / 2) * CHIP_SIZE;
		offsetX = setOffset(offsetX, WIDTH, map.getWidth());
		int offsetY = (ROW / 2) * CHIP_SIZE;
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
