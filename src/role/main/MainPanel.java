package role.main;

import java.awt.Dimension;

import javax.swing.JPanel;

public class MainPanel extends JPanel implements Common{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public static final int COL = 9;
	public static final int ROW = COL;
	public static final int WIDTH = COL * CHIP_SIZE;
	public static final int HEIGHT = ROW * CHIP_SIZE;

	public MainPanel(){
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
	}
}
