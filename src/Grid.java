public class Grid {
	public int size=31;
	public boolean [][] isFire; //tableaux des feux
	public int [][] isSomething; //tableaux des objets - valeurs : 1=robot, 2=base
	
	
	public Grid() {
		//Rempli avec des false (mis � jour plus tard � true si la case est en feu)
		isFire = new boolean [31] [31];
		for(int i=0; i<isFire.length;i++) {
			for(int j=0; i<isFire[i].length;j++) {
				isFire[i][j] = false;
			}
		}
		//On place la base
		isSomething [16][16] = 2;
		
	}

	public void setFire(Coordonnee coord) {
		isFire[coord.x][coord.y] = true;
	}

	public void updateGrid() {
		
	}

}