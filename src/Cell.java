public class Cell implements Cloneable {

	public Coordonnee coordonnee;
	public int state; // 0: base 1: safe 2: onFire 3: Burned 4: unknown
	public boolean asHuman;
	public double fire; // value between 0 and 1 with 0.1 steps down
	public int duration;
	
	public Cell(Coordonnee coordonnee, int state) {
		this.coordonnee=coordonnee;
		this.state=state;
		this.asHuman=false;
		this.fire=0;
		this.duration=0;
	}

	public void set(int state, boolean asHuman, double fire, int duration) {
		this.state=state;
		this.asHuman=asHuman;
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