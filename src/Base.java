public class Base {

	public Coordonnee coord;
	public String [] infos; //les infos envoy�s par les robots
	public Robot[] inBase;
	public Robot[] onField;
	
	public Base() {
		coord = new Coordonnee(16,16);				
	}

	public Coordonnee[] getnewPath(Coordonnee from, int proximity){
		//TODO définir la cellule à allez voir en fonction de la distance et de la position du robot
		Coordonnee C2=null;
		return getPath(coord, C2);
	}

	public Coordonnee[] getGoing(){
		return getnewPath(coord, 15);
	}

	public Coordonnee[] getPathHome(Coordonnee robotCell){		
		return getPath(robotCell, coord);
	}

	public Coordonnee[] getPath(Coordonnee C1, Coordonnee C2){
		// TODO Auto-generated method stub
		return null;
	}



}