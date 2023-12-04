	// la carte se modifie
	// les robots se déplacent et signalent un changement ou font des actions (éteindre, un feu, se recharger, signaler un humain, etc.))
	// la base traite les updates et met à jour la carte globale pour tous les robots
	// TODO fin si la carte globale ne change pas? ou fin si la carte globale n'as plus de feu sur que des cases connus depuis un certain temps?
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.ArrayList;
import java.util.List;

public class Turn {

	Grid grid;
	GridView view;
	int turnNb = 0;
	boolean stillFire = true;
	int sleepTime;
	double propagationProb;
	double extinctionProb;

	public Turn(GridView view, int sleepTime, double propagationProb, double extinctionProb) {
		this.view = view;
		this.grid = view.grid;
		this.sleepTime = sleepTime;
		this.propagationProb = propagationProb;
		this.extinctionProb = extinctionProb;
	}

	public void run() {

		Timer timer = new Timer();
		// Créez une tâche qui sera exécutée toutes les secondes
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				while(stillFire){ //tant qu'il reste des cases en feu
					try {
						// thread en pause pendant 1 seconde
						Thread.sleep(sleepTime);
						// puis calcule de la grille suivante
						nextGrid();
						view.base.turn();
						//! FIN DU JEU MARCHE PAS
						if(grid.fires.isEmpty()){//si il n'y a plus de feu
							stillFire = false; //fin du jeu
							System.out.println("Fin du jeu");
						} 
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		timer.scheduleAtFixedRate(task, 0, sleepTime);
	}

	/**
	 * Calcule la grille suivante
	 */
	public void nextGrid(){
		System.out.println("Turn "+turnNb);
		if(turnNb%10==0)propagateFire(); //propager le feu
		view.updateGrid(grid); //mettre à jour la grille dans la vue
		turnNb++;
	}
	 
	private void propagateFire() {
		int size = grid.getSize();
		//* crée une liste des cellule en feu */
		List<Cell> fireCells= new ArrayList<Cell>();; //tableau des cellules en feu
		//parcourir la grille
		for(int i=0; i<size; i++) {
			for(int j=0; j<size; j++) {
				Cell currentCell = grid.getCell(i,j);
				if(currentCell.getState()==2){ //si la cellule est en feu
					fireCells.add(currentCell); //ajouter la cellule au tableau des cellules en feu
					//*propagateFire(i, j); //propager le feu
				}
			}
		}
		//* le feu se propage */
		for (Cell currentCell : fireCells) {
		Random random = new Random();
		int x = currentCell.coordonnee.x;
		int y = currentCell.coordonnee.y;
		if(grid.isInSide(x, y-1)){ //si la cellule de gauche est dans la grille
			Cell leftCell = grid.getCell(x,y-1);
			if(leftCell.getState()==1 && random.nextDouble() < propagationProb){ //et qu'elle est safe
				leftCell.setState(2); //elle devient en feu
			}
		}
		if(grid.isInSide(x, y+1)){ //si la cellule de droite est dans la grille
			Cell rightCell = grid.getCell(x,y+1);
			if(rightCell.getState()==1 && random.nextDouble() < propagationProb){ //et qu'elle est safe
				rightCell.setState(2); //elle devient en feu
			}
		}
		if(grid.isInSide(x-1, y)){ //si la cellule du haut est dans la grille
			Cell topCell = grid.getCell(x-1,y);
			if(topCell.getState()==1 && random.nextDouble() < propagationProb){ //et qu'elle est safe
				topCell.setState(2); //elle devient en feu
			}
		} 
		if(grid.isInSide(x+1, y)){ //si la cellule du bas est dans la grille
			Cell bottomCell = grid.getCell(x+1,y);
			if(bottomCell.getState()==1 && random.nextDouble() < propagationProb){ //et qu'elle est safe
				bottomCell.setState(2); //elle devient en feu
			}
		}
		//si la cellule est en feu depuis plus de 3 tours
		if (currentCell.getState() == 2 && random.nextDouble() < extinctionProb) { //10% de chance de passer à l'état burned
			currentCell.setState(3);
		}
		}
    }

}