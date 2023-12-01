import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class GridView extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    GridView frame = new GridView();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public GridView() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 500, 500); // Ajustez la taille de la fenêtre pour mieux accueillir la grille
        contentPane = new GridPanel(); // Utilisation de la sous-classe GridPanel au lieu de JPanel
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
    }

    // Sous-classe de JPanel qui dessine la grille
    private class GridPanel extends JPanel {
        private static final int ROWS = 31;
        private static final int COLS = 31;

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            int cellWidth = getWidth() / COLS;
            int cellHeight = getHeight() / ROWS;

            // Calculer le décalage pour centrer la grille
            int offsetX = (getWidth() - COLS * cellWidth) / 2;
            int offsetY = (getHeight() - ROWS * cellHeight) / 2;

            // Dessiner la grille centrée
            for (int row = 0; row < ROWS; row++) {
                for (int col = 0; col < COLS; col++) {
                    int x = offsetX + col * cellWidth;
                    int y = offsetY + row * cellHeight;

                    // Dessiner la bordure de la cellule
                    g.setColor(Color.BLACK);
                    g.drawRect(x, y, cellWidth, cellHeight);
                }
            }
        }
    }
}
