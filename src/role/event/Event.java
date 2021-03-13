package role.event;

public abstract class Event {
	protected int x, y;
	protected int chipNo;
	protected boolean isHit;
	protected String key;

	public Event (int x, int y, int chipNo, boolean isHit, String key){
		this.x = x;
		this.y = y;
		this.chipNo = chipNo;
		this.isHit = isHit;
		this.key = key;
	}

	public int getX(){
		return x;
	}

	public int getY(){
		return y;
	}

	public int getChipNo(){
		return chipNo;
	}

	public boolean isHit(){
		return isHit;
	}

	public String getKey(){
		return key;
	}
}
