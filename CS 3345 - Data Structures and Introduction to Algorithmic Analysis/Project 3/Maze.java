import java.util.*;

public class Maze {
    
    private int[][] maze;
    private int n;
    private int m;
    private Random rand;

    public Maze(int n, int m) {
        this.n = n;
        this.m = m;
        this.maze = new int[n][m];
        this.rand = new Random();
    }
    
    public void generateMaze() {
        // Initialize maze with all walls present
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                maze[i][j] = 15; // 15 = 0b1111 (all walls present)
            }
        }
        
        // Set starting and ending points
        maze[0][0] &= ~1; // remove top wall of starting point
        maze[n-1][m-1] &= ~4; // remove bottom wall of ending point
        
        // Generate maze using recursive backtracking algorithm
        generateMazeRecursive(0, 0);
    }
    
    private void generateMazeRecursive(int x, int y)  {
        
        int[] dx = {1, 0, -1, 0};
        int[] dy = {0, 1, 0, -1};
        List<Integer> directions = Arrays.asList(0, 1, 2, 3);
        Collections.shuffle(directions, rand);
        
        for (int d : directions) {
            int nx = x + dx[d];
            int ny = y + dy[d];
            
            if (nx < 0 || ny < 0 || nx >= n || ny >= m) 
                continue;
            
            if (maze[nx][ny] == 15) {
                // Remove wall between current cell and neighbor
                int wallToRemove = 1 << d;
                maze[x][y] &= ~wallToRemove;
                maze[nx][ny] &= ~(wallToRemove ^ 2);
                generateMazeRecursive(nx, ny);
            }
        }
    }
    
    public void printMaze() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                System.out.print(String.format("%x", maze[i][j]));
            }
            System.out.println();
        }
    }
    
    public static void main(String[] args)  {
        Maze mazeGenerator = new Maze(10, 10);
        mazeGenerator.generateMaze();
        mazeGenerator.printMaze();
    }
}
