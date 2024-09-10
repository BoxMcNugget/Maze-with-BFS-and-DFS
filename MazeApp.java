import java.util.*;

public class MazeApp {

    // Node class for each square in the maze
    static class Node {
        //variable for setting it as a wall
        String value;
        // variables for left right up and down directions of the current node
        Node left;
        Node right;
        Node up;
        Node down;

        // variables for the start and end nodes
        boolean isStart;
        boolean isEnd;

        //
        // Setting the initial values the nodes node
        //
        public Node(String value) {
            this.value = value;
            this.left = null;
            this.right = null;
            this.up = null;
            this.down = null;
            this.isStart = false;
            this.isEnd = false;
        }
    }

    //
    // Creating the maze itself
    //
    static class Maze {
        // using a double linked list
        private Node[][] grid;
        //defining rows and columns
        private int rows;
        private int cols;
        // defining start and end as a variable
        private Node start;
        private Node end;

        //
        // Instantiating the maze
        //
        public Maze(int rows, int cols) {
            // setting the number of rows and columns
            this.rows = rows;
            this.cols = cols;
            // creating the double linked list with that many rows and columns
            grid = new Node[rows][cols];
            //
            // Calling initilize maze to set all the nodes to open to start 
            //
            initializeMaze();
        }

        //
        // Sets all the nodes to open
        //
        public void initializeMaze() {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    // set the space for every row and every column to " " or an empty space in other words
                    grid[i][j] = new Node(" ");
                }
            }

            // for every row
            for (int i = 0; i < rows; i++) {
                // and for every column in every row
                for (int j = 0; j < cols; j++) {
                    // link that node to its surrounding nodes if its not outside the linked lists
                    if (i > 0) grid[i][j].up = grid[i-1][j];
                    if (i < rows - 1) grid[i][j].down = grid[i+1][j];
                    if (j > 0) grid[i][j].left = grid[i][j-1];
                    if (j < cols - 1) grid[i][j].right = grid[i][j+1];
                }
            }
        }

        //
        // setter to set the node as a wall by setting it equal to "0"
        //
        public void setWall(int row, int col) {
            if (isValidPosition(row, col)) {
                grid[row][col].value = "0";
            }
        }

        //
        // setter to set the node as an open space by setting it equal to " "
        //
        public void setOpenSpace(int row, int col) {
            if (isValidPosition(row, col)) {
                grid[row][col].value = " ";
            }
        }

        //
        // setter to set a node as the starting space
        //
        public void setStart(int row, int col) {
            if (isValidPosition(row, col)) {
                if (start != null) start.isStart = false;
                start = grid[row][col];
                start.isStart = true;
            }
        }

        //
        // setter to set a node as the ending space
        //
        public void setEnd(int row, int col) {
            if (isValidPosition(row, col)) {
                if (end != null) end.isEnd = false;
                end = grid[row][col];
                end.isEnd = true;
            }
        }

        //
        // getter for the start node
        //
        public Node getStart() {
            return start;
        }

        //
        // getter for the end node
        //
        public Node getEnd() {
            return end;
        }

        //
        // checks if a node is a valid position 
        //
        public boolean isValidPosition(int row, int col) {
            // if the position is on the board return true else return false
            if (row >= 0 && row < rows && col >= 0 && col < cols)
                return true;
            else 
                return false;
        }

        //
        // returns a list of nodes next to the input parameter node
        //
        public List<Node> getNeighbors(Node node) {
            // instantiating a new list of nodes
            List<Node> neighbors = new ArrayList<>();

            // check if the surrounding nodes are not null and empty
            // if they are add it to the list of nodes
            if (node.up != null && node.up.value == " ") neighbors.add(node.up);
            if (node.down != null && node.down.value == " ") neighbors.add(node.down);
            if (node.left != null && node.left.value == " ") neighbors.add(node.left);
            if (node.right != null && node.right.value == " ") neighbors.add(node.right);
            //return the list
            return neighbors;
        }

        public void printMaze() {
            //for every row
            for (int i = 0; i < rows; i++) {
                // and for every column
                for (int j = 0; j < cols; j++) {
                    // if node is the start node print an 's' for start
                    if (grid[i][j].isStart) {
                        System.out.print("S ");
                    } 
                    // if the node is the end node print an 'e' fr the end
                    else if (grid[i][j].isEnd) {
                        System.out.print("E ");
                    } 
                    // else print the value of that node
                    else {
                        System.out.print(grid[i][j].value + " ");
                    }
                }
                System.out.println();
            }
        }
        
        //
        // BFS Algorithm
        //
        public List<int[]> bfs() {
            if (start == null || end == null) return null;

            Queue<Node> queue = new LinkedList<>();
            Map<Node, Node> cameFrom = new HashMap<>();
            queue.add(start);
            cameFrom.put(start, null);

            while (!queue.isEmpty()) {
                Node current = queue.poll();
                if (current == end) break;

                for (Node neighbor : getNeighbors(current)) {
                    if (!cameFrom.containsKey(neighbor)) {
                        queue.add(neighbor);
                        cameFrom.put(neighbor, current);
                    }
                }
            }

            // Reconstruct the path as coordinates
            List<int[]> path = new ArrayList<>();
            Node step = end;
            while (step != null) {
                path.add(new int[] {getRow(step), getCol(step)});
                step = cameFrom.get(step);
            }
            Collections.reverse(path);
            return path;
        }

        //
        // DFS Algorithm
        //
        public List<int[]> dfs() {
            if (start == null || end == null) return null;

            Stack<Node> stack = new Stack<>();
            Map<Node, Node> cameFrom = new HashMap<>();
            stack.add(start);
            cameFrom.put(start, null);

            while (!stack.isEmpty()) {
                Node current = stack.pop();
                if (current == end) break;

                for (Node neighbor : getNeighbors(current)) {
                    if (!cameFrom.containsKey(neighbor)) {
                        stack.add(neighbor);
                        cameFrom.put(neighbor, current);
                    }
                }
            }

            

            // Reconstruct the path as coordinates
            List<int[]> path = new ArrayList<>();
            Node step = end;
            while (step != null) {
                path.add(new int[] {getRow(step), getCol(step)});
                step = cameFrom.get(step);
            }
            Collections.reverse(path);
            return path;
        }
                
        //
        // getter to return the row coordinate for the search algorithms
        //
        public int getRow(Node node) {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    if (grid[i][j] == node) return i;
                }
            }
            return 0;
        }

        //
        // getter to return the column coordinate for the search algorithms
        // 
        public int getCol(Node node) {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    if (grid[i][j] == node) return j;
                }
            }
            return 0;
        }
    }

    // Main class to test the maze implementation
    public static void main(String[] args) {
        Maze maze = new Maze(10, 10); // Create a 10x10 maze

        maze.setWall(0, 0);
        maze.setWall(0, 1);
        maze.setWall(0, 2);
        maze.setWall(0, 3);
        maze.setWall(0, 4);
        maze.setWall(0, 5);
        maze.setWall(0, 6);
        maze.setWall(0, 7);
        maze.setWall(0, 8);
        maze.setWall(0, 9);
        maze.setWall(1, 0);
        maze.setWall(1, 9);
        maze.setWall(2, 0);
        maze.setWall(2, 9);
        maze.setWall(3, 0);
        maze.setWall(3, 9);
        maze.setWall(4, 0);
        maze.setWall(4, 9);
        maze.setWall(5, 0);
        maze.setWall(5, 9);
        maze.setWall(6, 0);
        maze.setWall(6, 9);
        maze.setWall(7, 0);
        maze.setWall(7, 9);
        maze.setWall(8, 0);
        maze.setWall(8, 9);
        maze.setWall(9, 0);
        maze.setWall(9, 1);
        maze.setWall(9, 2);
        maze.setWall(9, 3);
        maze.setWall(9, 4);
        maze.setWall(9, 5);
        maze.setWall(9, 6);
        maze.setWall(9, 7);
        maze.setWall(9, 8);
        maze.setWall(9, 9);

        // Set some walls
        maze.setWall(1, 5);
        maze.setWall(1, 7);
        maze.setWall(2, 2);
        maze.setWall(2, 3);
        maze.setWall(2, 5);
        maze.setWall(2, 7);
        maze.setWall(3, 5);
        maze.setWall(3, 7);
        maze.setWall(4, 1);
        maze.setWall(4, 3);
        maze.setWall(4, 4);
        maze.setWall(4, 5);
        maze.setWall(5, 3);
        maze.setWall(5, 7);
        maze.setWall(6, 5);
        maze.setWall(6, 6);
        maze.setWall(6, 7);
        maze.setWall(7, 2);
        maze.setWall(7, 3);
        maze.setWall(7, 4);
        maze.setWall(7, 5);
        maze.setWall(8, 5);
        maze.setWall(8, 7);

        // Set start and end positions
        maze.setStart(1, 2);  // Start at top-left corner
        maze.setEnd(8, 6);    // End at bottom-right corner

        // Print the maze
        maze.printMaze();

        // Run BFS and DFS
        List<int[]> bfsPath = maze.bfs();
        List<int[]> dfsPath = maze.dfs();

        // Print BFS Path
        System.out.println("BFS Path:");
        for (int[] coord : bfsPath) {
            System.out.print(Arrays.toString(coord) + " ");
        }
        System.out.println();

        // Print DFS Path
        System.out.println("DFS Path:");
        for (int[] coord : dfsPath) {
            System.out.print(Arrays.toString(coord) + " ");
        }
        System.out.println();
    }
}