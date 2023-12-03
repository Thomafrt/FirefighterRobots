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
	 * Coordonnées de la base
	 */
	public Coordonnee base;
	/**
	 * Liste des coordonnées des départs de feu
	 */
	public ArrayList<Coordonnee> fireStart = new ArrayList<>();
	
	/**
	 * Constructeur de la grille
	 * @param size
	 */
	public Grid(int size) {
		this.size = size;
		cells = new Cell [size] [size]; //initialisation de la grille
		base = new Coordonnee(size/2, size/2); //coordonnées de la base (au centre de la grille)
		for(int i=0; i<cells.length; i++) {
			for(int j=0; j<cells[i].length;j++) {
				if(i==(base.x) && j==(base.y)) { //la base au centre de la grille
					cells[i][j] = new Cell(new Coordonnee(i,j), 0);
				}
				else{ //les autres cellules (safe)
					cells[i][j] = new Cell(new Coordonnee(i,j), 1);
				}
			}
		}
		setFirstFire(base); //mettre le premier feu (case différente de la base)
	}

	public void setFirstFire(Coordonnee coord) {
		Random rand = new Random();
		int x;
		int y;
		do {
			x = rand.nextInt(size);
			y = rand.nextInt(size);
		}while(x==coord.x && y==coord.y);
		cells[x][y].state=2;
		fireStart.add(new Coordonnee(x,y));
	}

	public Cell getCell(Coordonnee coord) {
		return cells[coord.x][coord.y];
	}
	public Cell getCell(int x, int y) {
		return cells[x][y];
	}

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

	@Override
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