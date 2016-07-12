import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stack;

public class Solver {
	private int mShortestNumOfMove;	
	private Stack<Board> mSolutionResult;	
	
    public Solver(Board initial) {
    	mShortestNumOfMove = 0;
    	
    	mSolutionResult = new Stack<Board>();
    	MinPQ<SearchNode> pqOrigin = new MinPQ<SearchNode>();
    	MinPQ<SearchNode> pqTwin = new MinPQ<SearchNode>();

    	pqOrigin.insert(new SearchNode(0, initial, null));
    	pqTwin.insert(new SearchNode(0, initial.twin(), null));

    	Board previousBoard = initial;
    	SearchNode originalNode;
    	SearchNode twinNode;
    	
    	while (!pqOrigin.isEmpty() && !pqTwin.isEmpty()) {
            originalNode = pqOrigin.delMin(); 
            if (originalNode.previousNode != null) {
                previousBoard = originalNode.previousNode.board;
            }
            
            for (Board neighborBoard : originalNode.board.neighbors()) {
                if (!previousBoard.equals(neighborBoard)) {
                    pqOrigin.insert(new SearchNode(originalNode.numOfMoves + 1, neighborBoard, originalNode));
                }
            }
            if (originalNode.board.isGoal()) {
                while (originalNode.previousNode != null) {
                	mSolutionResult.push(originalNode.board);
                    originalNode = originalNode.previousNode;
                    mShortestNumOfMove++;   
                }
                mSolutionResult.push(initial);
                break;
            }
            
            twinNode = pqTwin.delMin();
            if (twinNode.previousNode != null) {
                previousBoard = twinNode.previousNode.board;
            }
            
            for (Board neighborBoard : twinNode.board.neighbors()) {
                if (!previousBoard.equals(neighborBoard)) {
                    pqTwin.insert(new SearchNode(originalNode.numOfMoves + 1, neighborBoard, twinNode));
                }
            }
            if (twinNode.board.isGoal()) {
                mShortestNumOfMove = -1;
                break;
            }
        }
    }
    
    public boolean isSolvable() {
    	return !mSolutionResult.isEmpty();
    }
    
    public int moves() {
    	return mShortestNumOfMove;
    }
    
    public Iterable<Board> solution() {
    	if (!mSolutionResult.isEmpty()) {
    		return mSolutionResult;
    	}
    	return null;
    }
    
    private class SearchNode implements Comparable<SearchNode> {
    	private int numOfMoves;
        private Board board;
        private SearchNode previousNode;
        public SearchNode(int numOfMoves, Board board, SearchNode previousNode) {
            this.numOfMoves = numOfMoves;
            this.board = board;
            this.previousNode = previousNode;
        }

        public int compareTo(SearchNode node) {
        	int result = 0;
            if (board.manhattan() + numOfMoves > node.board.manhattan() + node.numOfMoves) {
            	result = 1;
            }
            else if (board.manhattan() + numOfMoves < node.board.manhattan() + node.numOfMoves) {
            	result = -1;
            }
            return result;
        }
    }
    
    public static void main(String[] args) {
    	// create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}