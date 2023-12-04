public class Cell implements Cloneable {

	public Coordonnee coordonnee;
	public int state; // 0: base 1: safe 2: onFire 3: Burned
	public int hasHuman; // 0:nobody 1:safe 2:saved 3:dead
	public int fire; // value between 0 and 1 with 0.1 steps down
	public int duration;
	public double fireProximity;
	
	public Cell(Coordonnee coordonnee, int state) {
		this.coordonnee=coordonnee;
		this.state=state;
		this.hasHuman=0;
		this.fire=0;
		this.duration=0;
		this.fireProximity=1;
	}

	public void set(int state, int hasHuman, int fire, int duration) {
		this.state=state;
		this.hasHuman=hasHuman;
		this.fire=fire;
		this.duration=duration;
	}

	public Coordonnee getCoordonnee() {
		return coordonnee;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public boolean isValidCell(int size) {
		if( (coordonnee.x >= 0 && coordonnee.x < size) && (coordonnee.y >= 0 && coordonnee.y < size))
			return true;
		else
			return false;
	}

	public Cell clone() {
        try {
            return (Cell) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
    }
}