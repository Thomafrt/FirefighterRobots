import java.awt.EventQueue;

public class Main {
    	/**
	 * Launch the application.
	 */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {

                    /////////////////////////////////////////////////////////////////////////////////////////////
					//PARAMETRES modifiables
					int nbRobots = 25; //Le nombre de robots
					int sleepTime = 100; //Le temps d'attente entre chaque tour en ms
					double propagationProb = 0.5; //Pourcentage de chance de propagation d'une case à sa voisine
					double extinctionProb = 0.3; //Pourcentage de chance que le feu consume entièrement la case
					int gridSize = 40; //Taille de la grille
					int nbHumans = 20; //Nombre d'humains sur la grille (placés aléatoirement)
					int nbFireStart = 3; //Nombre de départs de feu (placés aléatoirement)
                    ////////////////////////////////////////////////////////////////////////////////////////////

					//Créer la grille
                    GridView frame = new GridView(nbRobots, gridSize, propagationProb, nbHumans, nbFireStart);  // Passer la taille souhaitée
                    frame.setVisible(true);
					//Lancer les tours
					Turn turn = new Turn(frame, sleepTime, propagationProb, extinctionProb);
					turn.run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
