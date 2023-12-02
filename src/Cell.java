public class Cell {

	public Coordonnee coordonnee;
	public int state; // 0: safe 1: onFire 2: Burned
	public boolean asHuman;
	
	public Cell(Coordonnee coordonnee, int state) {
		this.coordonnee=coordonnee;
		this.state=state;
	}
}