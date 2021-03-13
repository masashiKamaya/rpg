package role.event;

public class MoveEvent extends Event{

	private int destMapNo;
	private int destX;
	private int destY;

	public MoveEvent(int x, int y, int chipNo, int destMapNo, int destX, int destY) {
		super(x, y, chipNo, false, "");
		this.destMapNo = destMapNo;
		this.destX = destX;
		this.destY = destY;
	}

	public int getDestMapNo(){
		return destMapNo;
	}

	public int getDestX(){
		return destX;
	}

	public int getDestY(){
		return destY;
	}
	
}
