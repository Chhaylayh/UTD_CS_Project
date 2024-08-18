
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MazeC extends JPanel implements ActionListener {

    private int[][] maze;
    private int n;
    private int m;
    private int cellSize;
    private boolean drawPath;
    private String path;

    public MazeC(int[][] maze, int cellSize) {
        this.maze = maze;
        this.n = maze.length;
        this.m = maze[0].length;
        this.cellSize = cellSize;
        this.drawPath = false;
        this.path = "";

        JFrame frame = new JFrame("Maze");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(this);
        JButton button = new JButton("Show path");
        button.addActionListener(this);
        frame.getContentPane().add(button, BorderLayout.SOUTH);
        frame.pack();
        frame.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        this.drawPath = true;
        Maze mazeSolver = new Maze(maze);
        if (mazeSolver.solveMaze()) {
            this.path = mazeSolver.getPath();
        }
        repaint();
    }

    public Dimension getPreferredSize() {
        return new Dimension(m * cellSize, n * cellSize);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                int x = j * cellSize;
                int y = i * cellSize;
                g.setColor(Color.BLACK);
                if ((maze[i][j] & 1) != 0) { // top
                    g.drawLine(x, y, x + cellSize, y);
                }
                if ((maze[i][j] & 2) != 0) { // right
                    g.drawLine(x + cellSize, y, x + cellSize, y + cellSize);
                }
                if ((maze[i][j] & 4) != 0) { // bottom
                    g.drawLine(x, y + cellSize, x + cellSize, y + cellSize);
                }
                if ((maze[i][j] & 8) != 0) { // left
                    g.drawLine(x, y, x, y + cellSize);
                }
            }
        }
        if (drawPath) {
            g.setColor(Color.RED);
            int x = 0;
            int y = 0;
            for (int i = 0; i < path.length(); i++) {
                char c = path.charAt(i);
                switch (c) {
                    case 'S':
                        g.drawLine(x, y, x, y + cellSize);
                        y += cellSize;
                        break;
                    case 'E':
                        g.drawLine(x, y, x + cellSize, y);
                        x += cellSize;
                        break;
                    case 'N':
                        g.drawLine(x, y, x, y - cellSize);
                        y -= cellSize;
                        break;
                    case 'W':
                        g.drawLine(x, y, x - cellSize, y);
                        x -= cellSize;
                        break;
                }
            }
        }
    }

    public static void main(String[] args) {
        int[][] maze = {
            {10, 5, 10, 9, 8, 8, 9, 12},
            {6, 6, 12, 3, 9, 9, 12, 2},
            {10, 10, 7, 6, 14, 5, 10, 6},
            {6, 3, 3, 13, 5, 10, 2, 10},
            {11, 8, 14, 5, 12, 6, 5, 10},
            {6, 7, 5, 8, 6, 3, 12, 2},
            {11, 8, 10, 10, 15, 9, 8, 10},
            {14, 5, 12, 6, 5, 10, 10, 6},
            {7, 5, 10, 10, 10, 10, 10, 5}
        };
        MazeC mazeGUI = new MazeC(maze, 30);
    }

}
