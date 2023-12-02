import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class GridView extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private Grid grid;

	/**
	 * Launch the application.
	 */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    GridView frame = new GridView(new Grid(21));  // Passer la taille souhaitée
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public GridView(Grid grid) {
        this.grid = grid;

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

			// Stocker les coordonnées des bases (si plusieurs) et des feux
			ArrayList<Coordonnee> bases = new ArrayList<>();
			ArrayList<Coordonnee> fires = new ArrayList<>();
			for (int i = 0; i < grid.getSize(); i++) {
				for (int j = 0; j < grid.getSize(); j++) {
					if (grid.getCell(i, j).getState() == 0) {
						bases.add(grid.getCell(i, j).getCoordonnee());
					}
					if (grid.getCell(i, j).getState() == 2) {
						fires.add(grid.getCell(i, j).getCoordonnee());
					}
				}
			}

			for (int row = 0; row < grid.getSize(); row++) {
				for (int col = 0; col < grid.getSize(); col++) {
					int x = offsetX + col * cellSize;
					int y = offsetY + row * cellSize;
					g.setColor(Color.BLACK);
					g.drawRect(x, y, cellSize, cellSize);

					// Vérifier si la cellule est une base et colorier en noir
					for (Coordonnee base : bases) {
						if (row == base.x && col == base.y) {
							g.setColor(Color.BLACK);
							g.fillRect(x, y, cellSize, cellSize);
						}
					}
					// Vérifier si la cellule est un feu et colorier en rouge
					for (Coordonnee fire : fires) {
						if (row == fire.x && col == fire.y) {
							g.setColor(Color.RED);
							g.fillRect(x, y, cellSize, cellSize);
						}
					}
				}
			}
		}
	}
}
