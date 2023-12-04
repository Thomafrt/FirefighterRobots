public class Base {

	public Coordonnee coord;
	public Grid knownGrid; //la carte connue par la base
	public Robot[] robots; //les robots de la base
	public int inactivityCounter;
	public boolean inActivity;
	
	public Base(Grid realGrid, Coordonnee coordonnee, int nbRobots) {
		this.coord = coordonnee;	
		this.knownGrid = new Grid(realGrid.getSize(), coordonnee, false, realGrid.propagationProb, 0, 0);
		this.robots = new Robot[nbRobots];
		for(int i=0; i<nbRobots; i++) {
			robots[i] = new Robot(knownGrid.clone(), realGrid, this);
		}
		this.inactivityCounter=0;
		this.inActivity=true;
	}

	public void turn() {
		// si tout les robots sont à la base j'incremente le compteur d'inactivité sinon il est remis à 0
		boolean allAtBase = true;
		for (int i = 0; i < robots.length; i++){
			if(!robots[i].atBase) {
				allAtBase=false;
			}
		}
		if (allAtBase) inactivityCounter++;
		else inactivityCounter=0;
		// si le compteur d'inactivité est à 5 je met fin au jeu
		if(inactivityCounter==5) {
			System.out.println("Fin du jeu");
			this.inActivity=false;
		}
		// sinon je fais tourner les robots
		else {
			knownGrid.incrementDuration();
			for (int i = 0; i < robots.length; i++){
				robots[i].turn();
			} 
		}
	}
}