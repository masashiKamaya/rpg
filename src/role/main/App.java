package role.main;

import javax.swing.JFrame;

public class App extends JFrame{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String[] args){
		App a = new App();
		a.setDefaultCloseOperation(EXIT_ON_CLOSE);
		a.setLocationRelativeTo(null);
		a.setVisible(true);
	}

	public App(){
		setTitle("reason");
		setResizable(false);
		getContentPane().add(new MainPanel());
		pack();
	}
}