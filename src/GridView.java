import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class GridView extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
	/**
	 * La grille du modèle
	 */
    public Grid grid;

	public Base base;
	/**
	 * Launch the application.
	 */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
					int nbRobots = 7;
					int sleepTime = 100;
					int gridSize = 21;
					double propagationProb = 0.5;
					double extinctionProb = 0.3;
					int nbHumans = 5;
					//PARAMETRES
                    GridView frame = new GridView(nbRobots, gridSize, propagationProb, nbHumans);  // Passer la taille souhaitée
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

    public GridView(int nbRobots, int gridSize, double propagationProb, int nbHumans) {
		Coordonnee baseCoordonnee = new Coordonnee(gridSize/2, gridSize/2);
		this.grid= new Grid(gridSize, baseCoordonnee, true, propagationProb, nbHumans);		
		this.base = new Base(grid, baseCoordonnee, nbRobots);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, grid.getSize() * 20, grid.getSize() * 20); // Ajuster la taille de la fenêtre en fonction de la grille
        contentPane = new GridPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
    }

	/**
	 * Create the frame and paint the grid
	 */
	private class GridPanel extends JPanel {
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			//Definir la taille des cellules en fonction de la taille de la grille et de la taille de la fenêtre
			int cellSize = Math.min(getWidth() / grid.getSize(), getHeight() / grid.getSize());
			int offsetX = (getWidth() - grid.getSize() * cellSize) / 2;
			int offsetY = (getHeight() - grid.getSize() * cellSize) / 2;
			// Mettre à jour les listes de coordonnées des bases, des feux et des cases brûlés
			for (int i = 0; i < grid.getSize(); i++) {
				for (int j = 0; j < grid.getSize(); j++) {
					if (grid.getCell(i, j).getState() == 0) {
						grid.bases.add(grid.getCell(i, j).getCoordonnee());
					}
					if (grid.getCell(i, j).getState() == 2) {
						grid.fires.add(grid.getCell(i, j).getCoordonnee());
					}
					if (grid.getCell(i, j).getState() == 3) {
						grid.burned.add(grid.getCell(i, j).getCoordonnee());
						grid.fires.remove(grid.getCell(i, j).getCoordonnee());
					}
				}
			}

			/*System.out.println("Bases : " + grid.bases);
			System.out.println("Fires : " + grid.fires);
			System.out.println("Gurned : " + grid.burned);*/

			// Dessiner la grille
			for (int row = 0; row < grid.getSize(); row++) {
				for (int col = 0; col < grid.getSize(); col++) {
					int x = offsetX + col * cellSize;
					int y = offsetY + row * cellSize;
					g.setColor(Color.BLACK);
					g.drawRect(x, y, cellSize, cellSize);

					// Vérifier si la cellule est une base et colorier en gris
					for (Coordonnee base : grid.bases) {
						if (row == base.x && col == base.y) {
							g.setColor(Color.GRAY);
							g.fillRect(x, y, cellSize, cellSize);
						}
					}
					// Vérifier si la cellule est un feu et colorier en rouge
					for (Coordonnee fire : grid.fires) {
						if (row == fire.x && col == fire.y) {
							g.setColor(Color.RED);
							g.fillRect(x, y, cellSize, cellSize);
						}
					}
					// Vérifier si la cellule est brûlée et colorier en noir
					for (Coordonnee burn : grid.burned) {
						if (row == burn.x && col == burn.y) {
							g.setColor(Color.BLACK);
							g.fillRect(x, y, cellSize, cellSize);
						}
					}
					// Ajouter les robots
					for (int i=0; i<base.robots.length; i++) {
						Coordonnee robot = base.robots[i].currentCell.getCoordonnee();
						if (row == robot.x && col == robot.y) {
							g.setColor(Color.BLUE);
							int circleSize = (int) (cellSize * 0.8); // Taille du cercle, ajustez si nécessaire
               				int circleX = x + (cellSize - circleSize) / 2;
                			int circleY = y + (cellSize - circleSize) / 2;
                			g.fillOval(circleX, circleY, circleSize, circleSize);
						}
					}
					// Ajouter les humains
					if(grid.getCell(row, col).hasHuman != 0) {
						if(grid.getCell(row, col).hasHuman == 1){
							g.setColor(Color.ORANGE);
							int circleSize = (int) (cellSize * 0.5);
							int circleX = x + (cellSize - circleSize) / 2;
							int circleY = y + (cellSize - circleSize) / 2;
							g.fillOval(circleX, circleY, circleSize, circleSize);
						}
						else if(grid.getCell(row, col).hasHuman == 2){
							g.setColor(Color.GREEN);
							int circleSize = (int) (cellSize * 0.5);
							int circleX = x + (cellSize - circleSize) / 2;
							int circleY = y + (cellSize - circleSize) / 2;
							g.fillOval(circleX, circleY, circleSize, circleSize);
						}
						else if(grid.getCell(row, col).hasHuman == 3){
							g.setColor(Color.RED);
							int circleSize = (int) (cellSize * 0.5);
							int circleX = x + (cellSize - circleSize) / 2;
							int circleY = y + (cellSize - circleSize) / 2;
							g.fillOval(circleX, circleY, circleSize, circleSize);
						}

					}
					
				}
			}
		}
	}	
	/**
	 * Met à jour la grille
	 * @param newGrid
	 */
	public void updateGrid(Grid newGrid){
		grid = newGrid; //la grille est mise à jour
		EventQueue.invokeLater(() -> {
            contentPane.repaint(); //redessiner la grille
        });
	}
}
