import java.util.ArrayList;
import java.util.Random;

public class Grid implements Cloneable{
	/**
	 * Taille de la grille : impaire pour centrer la base
	 */
	public int size;
	/**
	 * Tableau des cellules de la grille
	 */
	public Cell [][] cells;
	/**
	 * Liste des coordonnées des bases
	 */
	public ArrayList<Coordonnee> bases = new ArrayList<>();
	/**
	 * Liste des coordonnées des cases en feu
	 */
	public ArrayList<Coordonnee> fires = new ArrayList<>();
	/**
	 * Liste des coordonnées des cases brûlées
	 */
	public ArrayList<Coordonnee> burned = new ArrayList<>();

	public Coordonnee base;

	public double propagationProb;
	
	/**
	 * Constructeur de la grille
	 * @param size
	 */
	public Grid(int size, Coordonnee baseCoord, boolean real, double propagationProb, int nbHumans){
		this.size = size;
		cells = new Cell [size] [size]; //initialisation de la grille
		bases.add(baseCoord); //coordonnées de la base au centre de la grille (possibilité d'avoir plusieurs bases)
		for(int i=0; i<cells.length; i++) {
			for(int j=0; j<cells[i].length;j++) {
				for (Coordonnee base : bases) {
					if(i==(base.x) && j==(base.y)) { //place la ou les base(s)
						cells[i][j] = new Cell(new Coordonnee(i,j), 0);
					}
					else{ //les autres cellules (safe)
						cells[i][j] = new Cell(new Coordonnee(i,j), 1);
					}
				}
			}
		}
		if(real){ //Si c'est la grille originale
			setFirstFire(bases); //mettre le premier feu (case différente de la base)
			setHumans(nbHumans);
		} 
		this.propagationProb = propagationProb;
	}

	/**
	 * Mettre le premier feu
	 * @param bases
	 */
	public void setFirstFire(ArrayList<Coordonnee> bases) {
        Random rand = new Random();
        int x;
        int y;
        do {
            x = rand.nextInt(size);
            y = rand.nextInt(size);
        } while (isBase(x, y));
        cells[x][y].state = 2;
		cells[x][y].fire = 10;
        fires.add(new Coordonnee(x, y));
    }

	public void setHumans(int nbHumans){
		Random rand = new Random();
		int x;
		int y;
		for(int i=0; i<nbHumans; i++){
			do {
				x = rand.nextInt(size);
				y = rand.nextInt(size);
			} while (isBase(x, y));
			cells[x][y].hasHuman = 1;
		}
	}

	/**
	 * Vérifier si la position (x, y) est une base
	 * @param x
	 * @param y
	 * @return
	 */
    private boolean isBase(int x, int y) {
        for (Coordonnee base : bases) {
            if (base.x == x && base.y == y) {
                return true;  // La position (x, y) est une base
            }
        }
        return false;  // La position (x, y) n'est pas une base
    }

	/**
	 * Récupérer une cellule avec une coordonnée
	 * @param coord
	 * @return
	 */
	public Cell getCell(Coordonnee coord) {
		return cells[coord.x][coord.y];
	}
	/**
	 * Récupérer une cellule une la position (x, y)
	 * @param x
	 * @param y
	 * @return
	 */
	public Cell getCell(int x, int y) {
		return cells[x][y];
	}

	/**
	 * Récupérer la taille de la grille
	 * @return
	 */
	public int getSize() {
		return size;
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @return true si la coordonnée est dans la grille, false sinon
	 */
	public boolean isInSide(int x, int y){
		if(x<0 || y<0 || x>=size || y>=size)
			return false;
		else
			return true;
	}

	public void incrementDuration(){
		for(int i=0; i<cells.length; i++) {
			for(int j=0; j<cells[i].length;j++) {
				cells[i][j].duration++;
			}
		}
	}

	public void addfireProximity(Cell onFire, int step, ArrayList<Cell> ponderated){
		if(step<6){
		int x = onFire.coordonnee.x;
		int y = onFire.coordonnee.y;
		ArrayList<Cell> neighbors = new ArrayList<Cell>();
		if(isInSide(x, y-1)){ //si la cellule de gauche est dans la grille
			Cell leftCell = getCell(x,y-1);
			if(leftCell.getState()==1){ //et qu'elle est safe
				neighbors.add(leftCell);
			}
		}
		if(isInSide(x, y+1)){ //si la cellule de droite est dans la grille
			Cell rightCell = getCell(x,y+1);
			if(rightCell.getState()==1){ //et qu'elle est safe
				neighbors.add(rightCell);
			}
		}
		if(isInSide(x-1, y)){ //si la cellule du haut est dans la grille
			Cell topCell = getCell(x-1,y);
			if(topCell.getState()==1){ //et qu'elle est safe
				neighbors.add(topCell);
			}
		}
		if(isInSide(x+1, y)){ //si la cellule du bas est dans la grille
			Cell bottomCell = getCell(x+1,y);
			if(bottomCell.getState()==1){ //et qu'elle est safe
				neighbors.add(bottomCell);
			}
		}
		for (Cell neighbor : neighbors) {
			double coeff= (propagationProb/step)+1.5;
			if(neighbor.fireProximity<coeff)neighbor.fireProximity=coeff;
			ponderated.add(neighbor);
			addfireProximity(neighbor, step+1, ponderated);
		}
		}
	}


	@Override
	/**
	 * Clone la grille
	 */
	public Grid clone() {
		try {
			Grid clonedGrid = (Grid) super.clone();
			// Implémentez la copie profonde pour les objets internes
			clonedGrid.cells = new Cell[size][size];
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					clonedGrid.cells[i][j] = this.cells[i][j].clone();
				}
			}
			return clonedGrid;
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e);
		}
	}

}