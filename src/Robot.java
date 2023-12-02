public class Robot {

	public float time;
	public float water;
	public double speed=0.1;
	public Grid knownGrid;
	public boolean atBase;
	public boolean inActivity;
	public Modification[] modifications;
	public Cell currentCase;
	public Coordonnee[] path;
	
	public Robot() {
		// TODO Auto-generated constructor stub
	}

	public void turn(){
		if(!inActivity) moveOnPath();
		if(atBase){
			decisionAtBase(knownGrid);
		}else{
			decisionOnField(knownGrid);
		}
	}
		public void decisionAtBase(Grid knownGrid){
			// si il y a un feu non-verouillé à éteindre, je le “vérouille” et je vais l’éteindre			
			// else si il y n’y a pas de feu verouillé, je vais chercher une zone non Eteinte, proche d’un feu à explorer
		}

		public void decisionOnField(Grid knownGrid){
			// si je vois un humain, je rentre à la base pour avertir,
			// si je n’ai plus d’énérgie, je rentre
			// si je vois un feu et que j'ai de l'eau, je lache de l’eau sur le feu
			// j’explore la case la plus proche selon un rapport distance - proximité avec un feu connu - date de dernière update.
		}

		public void moveOnPath(){
			// je me déplace sur le chemin
		}

		public void backToBase(){
			// je rentre à la base
		}

		public void traceRoute(Cell cell){
			// je trace la route pour aller à la case
		}

}