public class MazeSolver {
    
    private int[][] maze;
    private int n;
    private int m;
    private String path;
    
    public MazeSolver(int[][] maze) {
        this.maze = maze;
        this.n = maze.length;
        this.m = maze[0].length;
        this.path = "";
    }
    
    public boolean solveMaze() {
        boolean[][] visited = new boolean[n][m];
        return solveMazeRecursive(0, 0, visited);
    }
    
    private boolean solveMazeRecursive(int x, int y, boolean[][] visited){
        
        if (x == n-1 && y == m-1)
            return true;
       
        visited[x][y] = true;
        int[] dx = {1, 0, -1, 0};
        int[] dy = {0, 1, 0, -1};
        char[] directions = {'S', 'E', 'N', 'W'};
        
        for (int i = 0; i < 4; i++) {
            int nx = x + dx[i];
            int ny = y + dy[i];
            
            if (nx < 0 || ny < 0 || nx >= n || ny >= m) 
                continue;
            
            int wall = 1 << i;
            
            if ((maze[x][y] & wall) == 0 && !visited[nx][ny]) {
                if (solveMazeRecursive(nx, ny, visited)) {
                    path += directions[i];
                    return true;
                }
            }
        }
        return false;
    }
    
    public String getPath() {
        return path;
    }
    
    public static void main(String[] args)  {
        
        int[][] maze =  { {10, 5, 10, 9, 8, 8, 9, 12},
                          {6, 6, 12, 3, 9, 9, 12, 2},
                          {2, 3, 6, 12, 3, 10, 3, 10},
                          {4, 3, 3, 10, 5, 3, 10, 3},
                          {6, 10, 9, 8, 10, 5, 5, 5},
                          {5, 5, 5, 5, 5, 5, 5, 5},
                          {5, 5, 5, 5, 5, 5, 5, 5},
                          {5, 5, 5, 5, 5, 5, 5, 5}
        };
        
        MazeSolver mazeSolver = new MazeSolver(maze);
        
        if (mazeSolver.solveMaze())
            System.out.println(mazeSolver.getPath());
        else 
            System.out.println("No path found.");
    }
}
