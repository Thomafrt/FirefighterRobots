	// la carte se modifie
	// les robots se déplacent et signalent un changement ou font des actions (éteindre, un feu, se recharger, signaler un humain, etc.))
	// la base traite les updates et met à jour la carte globale pour tous les robots
	// TODO fin si la carte globale ne change pas? ou fin si la carte globale n'as plus de feu sur que des cases connus depuis un certain temps?
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Turn {

	Grid currentGrid;
	Grid nextGrid;
	GridView view;
	int turnNb = 0;
	boolean stillFire = true;

	public Turn(GridView view) {
		this.view = view;
		currentGrid = view.grid;
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
						Thread.sleep(1000);
						// puis calcule de la grille suivante
						nextGrid();
						//! FIN DU JEU MARCHE PAS
						if(currentGrid.fires.isEmpty()){//si il n'y a plus de feu
							stillFire = false; //fin du jeu
							System.out.println("Fin du jeu");
						} 
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		timer.scheduleAtFixedRate(task, 0, 1000);
	}

	/**
	 * Calcule la grille suivante
	 */
	public void nextGrid(){
		nextGrid = currentGrid.clone();
		System.out.println("Turn "+turnNb);
		int size = currentGrid.getSize();
		//parcourir la grille
		for(int i=0; i<size; i++) {
			for(int j=0; j<size; j++) {
				Cell currentCell = currentGrid.getCell(i,j);
				if(currentCell.getState()==2){ //si la cellule est en feu
					propagateFire(i, j); //propager le feu
				}
			}
		}
		currentGrid = nextGrid.clone(); //la grille actuelle devient la grille suivante
		view.updateGrid(nextGrid); //mettre à jour la grille dans la vue
		turnNb++;
	}
	 
	private void propagateFire(int x, int y) {
		Random random = new Random();
		Cell currentCell = nextGrid.getCell(x,y); //cellule actuelle
		if(nextGrid.isInSide(x, y-1)){ //si la cellule de gauche est dans la grille
			Cell oldLeftCell = currentGrid.getCell(x,y-1);
			Cell leftCell = nextGrid.getCell(x,y-1);
			if(oldLeftCell.getState()==1 && random.nextDouble() < 0.5){ //et qu'elle est safe
				leftCell.setState(2); //elle devient en feu
			}
		}
		if(nextGrid.isInSide(x, y+1)){ //si la cellule de droite est dans la grille
			Cell oldRightCell = nextGrid.getCell(x,y+1);
			Cell rightCell = nextGrid.getCell(x,y+1);
			if(oldRightCell.getState()==1 && random.nextDouble() < 0.5){
				rightCell.setState(2);
			}
		}
		if(nextGrid.isInSide(x-1, y)){ //si la cellule du haut est dans la grille
			Cell oldTopCell = nextGrid.getCell(x-1,y);
			Cell topCell = nextGrid.getCell(x-1,y);
			if(oldTopCell.getState()==1 && random.nextDouble() < 0.5){
				topCell.setState(2);
			}
		} 
		if(nextGrid.isInSide(x+1, y)){ //si la cellule du bas est dans la grille
			Cell oldBotCell = nextGrid.getCell(x+1,y);
			Cell botCell = nextGrid.getCell(x+1,y);
			if(oldBotCell.getState()==1 && random.nextDouble() < 0.5){
				botCell.setState(2);
			}
		}
		//si la cellule est en feu depuis plus de 3 tours
		if (currentCell.getState() == 2 && random.nextDouble() < 0.1) { //10% de chance de passer à l'état burned
			currentCell.setState(4);
		}
    }

}