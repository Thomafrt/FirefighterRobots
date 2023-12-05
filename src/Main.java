import java.awt.EventQueue;
import javax.swing.JOptionPane;

public class Main {
    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    // Valeurs par défaut
                    int defaultNbRobots = 25; // 25 robots
                    int defaultSleepTime = 100; // 100 ms entre chaque tour
                    double defaultPropagationProb = 0.5; // 50% de chance qu'une case prenne feu si elle est adjacente à une case en feu
                    double defaultExtinctionProb = 0.3; // 30% de chance qu'une case en feu s'éteigne
                    int defaultGridSize = 25; // Grille de 25x25
                    int defaultNbHumans = 20; // 20 humains
                    int defaultNbFireStart = 3; // 3 départs de feu

                    // Demander à l'utilisateur de saisir les paramètres avec des valeurs par défaut
                    int nbRobots = Integer.parseInt(JOptionPane.showInputDialog(null, "Nombre de robots:", defaultNbRobots));
                    int sleepTime = Integer.parseInt(JOptionPane.showInputDialog(null, "Temps d'attente entre chaque tour (ms):", defaultSleepTime));
                    double propagationProb = Double.parseDouble(JOptionPane.showInputDialog(null, "Probabilité de chance de propagation du feu:", defaultPropagationProb));
                    double extinctionProb = Double.parseDouble(JOptionPane.showInputDialog(null, "Probabilité de chance d'extinction du feu:", defaultExtinctionProb));
                    int gridSize = Integer.parseInt(JOptionPane.showInputDialog(null, "Taille de la grille:", defaultGridSize));
                    int nbHumans = Integer.parseInt(JOptionPane.showInputDialog(null, "Nombre d'humains sur la grille:", defaultNbHumans));
                    int nbFireStart = Integer.parseInt(JOptionPane.showInputDialog(null, "Nombre de départs de feu:", defaultNbFireStart));

                    // Créer la grille
                    GridView frame = new GridView(nbRobots, gridSize, propagationProb, nbHumans, nbFireStart);  // Passer la taille souhaitée
                    frame.setVisible(true);

                    // Lancer les tours
                    Turn turn = new Turn(frame, sleepTime, propagationProb, extinctionProb);
                    turn.run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
