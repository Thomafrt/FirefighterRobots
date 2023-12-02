	// la carte se modifie
	// les robots se déplacent et signalent un changement ou font des actions (éteindre, un feu, se recharger, signaler un humain, etc.))
	// la base traite les updates et met à jour la carte globale pour tous les robots
	// TODO fin si la carte globale ne change pas? ou fin si la carte globale n'as plus de feu sur que des cases connus depuis un certain temps?
import java.util.Timer;
import java.util.TimerTask;

public class Turn {

	Grid currentGrid;
	Boolean isStillFire = true;
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

				while(isStillFire && turnNb<100){
					updateGrid();
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

	public void updateGrid(){
		System.out.println("Turn "+turnNb);
		turnNb++;
	}

	


}