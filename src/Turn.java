	// la carte se modifie
	// les robots se déplacent et signalent un changement ou font des actions (éteindre, un feu, se recharger, signaler un humain, etc.))
	// la base traite les updates et met à jour la carte globale pour tous les robots
	// TODO fin si la carte globale ne change pas? ou fin si la carte globale n'as plus de feu sur que des cases connus depuis un certain temps?
import java.util.Timer;
import java.util.TimerTask;

public class Turn {

	Grid currentGrid;
	Boolean isStillFire = true; //si la grille contient toujours au moins 1 feu
	int turnNb = 0;

	public Turn(Grid initGrid) {
		currentGrid = initGrid;
	}

	public void run() {

		Timer timer = new Timer();
		// Créez une tâche qui sera exécutée toutes les secondes
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				while(isStillFire && turnNb<10){
					nextGrid();
					try {
						// thread en pause pendant 1 seconde
						Thread.sleep(1000);
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
		System.out.println("Turn "+turnNb);
		
		for(int i=0; i<currentGrid.getSize(); i++) {
			for(int j=0; j<currentGrid.getSize();j++) {
				Cell currentCell = currentGrid.getCell(i,j);
				if(currentCell.getState()==2){ //si la cellule est en feu, brule toutes les voisines
					Cell topCell = currentGrid.getCell(i,j-1);
					Cell botCell = currentGrid.getCell(i,j+1);
					Cell leftCell = currentGrid.getCell(i-1,j);
					Cell rightCell = currentGrid.getCell(i+1,j);
					if(topCell.getState()==1){
						topCell.setState(2);
					}
					if(botCell.getState()==1){
						botCell.setState(2);
					}
					if(leftCell.getState()==1){
						leftCell.setState(2);
					}
					if(rightCell.getState()==1){
						rightCell.setState(2);
					}
				}
			}
		}
		updateGrid(currentGrid);
		turnNb++;
	}

	/**
	 * Met à jour la grille visuelle
	 * @param newGrid
	 */
	public void updateGrid(Grid newGrid){
		//TODO mettre à jour la grille dans la vue
	}

	


}