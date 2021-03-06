package role.main;

public class ActionKey{
	public static final int NORMAL = 0;	//検知し続ける
	public static final int INITIAL = 1;	//一回

	private static final int RELEASED = 0;
	private static final int PRESSED = 1;
	private static final int WAITING = 2;

	private int mode;
	private int amount;
	private int state;

	public ActionKey(int mode){
		this.mode = mode;
		state = RELEASED;
		amount = 0;
	}
	public ActionKey(){
		this(NORMAL);
	}
	public void press(){
		if(state != WAITING){
			amount++;
			state = PRESSED;
		}
	}
	public void release(){
		state = RELEASED;
	}
	public boolean isPressed(){
		if(amount != 0){
			if(state == RELEASED) amount = 0;
			else if(mode == INITIAL){
				state = WAITING;
				amount = 0;
			}
			return true;
		}
		return false;
	}
}