public class Cell {

	public Coordonnee coordonnee;
	public int state; // 0: base 1: safe 2: onFire 3: beingWatered 4: Burned
	public boolean asHuman;
	
	public Cell(Coordonnee coordonnee, int state) {
		this.coordonnee=coordonnee;
		this.state=state;
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
}