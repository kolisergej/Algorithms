import edu.princeton.cs.algs4.Queue;

public class Board {
    private int[][] mBlocks;
    private int mDimension;

    public Board(int[][] blocks) {
        if (blocks == null) {
            throw new NullPointerException("Null element");
        }
        mDimension = blocks.length;
        mBlocks = new int[mDimension][mDimension];
        for (int i = 0; i < mDimension; i++) {
            for (int j = 0; j < mDimension; j++) {
                mBlocks[i][j] = blocks[i][j];
            }
        }
    }

    public int dimension() {
        return mDimension;
    }

    public int hamming() {
        int hamming = 0;
        for (int i = 0; i < mDimension; i++) {
            for (int j = 0; j < mDimension; j++) {
                if (mBlocks[i][j] != 0 && mBlocks[i][j] != i * mDimension + j + 1) {
                    ++hamming;
                }   
            }
        }
        return hamming;
    }

    public int manhattan() {
        int manhattan = 0;
        for (int i = 0; i < mDimension; i++) {
            for (int j = 0; j < mDimension; j++) {
                if (mBlocks[i][j] != 0) {
                    int mustJ = (mBlocks[i][j] - 1) % mDimension;
                    int mustI = (mBlocks[i][j] - 1) / mDimension;
                    manhattan += (Math.abs(mustI - i) + Math.abs(mustJ - j));
                }
            }
        }
        return manhattan;
    }

    public boolean isGoal() {
    	boolean result = (mBlocks[mDimension - 1][mDimension - 1] == 0);
    	if (result) {
    		for (int i = 0; i < mDimension; i++) {
    			for (int j = 0; j < mDimension; j++) {
    				if (mBlocks[i][j] != 0 && mBlocks[i][j] != i * mDimension + j + 1)
    				{
    					return false;
    				}
    			}
    		}
    	}
        return result;
    }

    public Board twin() {
        int[][] twinBlocks = new int[mDimension][mDimension];
        for (int i = 0; i < mDimension; i++) {
            for (int j = 0; j < mDimension; j++) {
            	twinBlocks[i][j] = mBlocks[i][j];
            }
        }
        
        for (int i = 0; i < mDimension; i++) {
            for (int j = 0; j < mDimension-1; j++) {
                if (twinBlocks[i][j] != 0 && twinBlocks[i][j+1] != 0) {
                    int tmp = twinBlocks[i][j];
                    twinBlocks[i][j] = twinBlocks[i][j+1];
                    twinBlocks[i][j+1] = tmp;
                    return new Board(twinBlocks);
                }
            }
        }
        return new Board(twinBlocks);
    }

    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null || y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        if (that.dimension() != this.dimension()) return false;
        boolean result = true;
        for (int i = 0; i < mDimension; i++) {
            for (int j = 0; j < mDimension; j++) {
                if (that.mBlocks[i][j] != mBlocks[i][j]) {
                    return false;
                }
            }
        }
        return result;
    }

    public Iterable<Board> neighbors() {
        Queue<Board> q = new Queue<Board>();
        int zeroI = 0;
        int zeroJ = 0;
        outer : for (int i = 0; i < mDimension; i++) {
            for (int j = 0; j < mDimension; j++) {
                if (mBlocks[i][j] == 0) {
                    zeroI = i;
                    zeroJ = j;
                    break outer;
                }
            }
        }
        
        int [][] first = new int[mDimension][mDimension];
        int [][] second = new int[mDimension][mDimension];
        int [][] third = new int[mDimension][mDimension];
        int [][] fourth = new int[mDimension][mDimension];
        for (int i = 0; i < mDimension; i++) {
            for (int j = 0; j < mDimension; j++) {
                first[i][j] = mBlocks[i][j];
                second[i][j] = mBlocks[i][j];
                third[i][j] = mBlocks[i][j];
                fourth[i][j] = mBlocks[i][j];
            }
        }

        if (zeroI > 0 && zeroJ > 0 && zeroI < mDimension-1 && zeroJ < mDimension-1) {
            first[zeroI][zeroJ] = first[zeroI-1][zeroJ]; 
            first[zeroI-1][zeroJ] = 0;
            q.enqueue(new Board(first));
            
            second[zeroI][zeroJ] = second[zeroI][zeroJ-1]; 
            second[zeroI][zeroJ-1] = 0;
            q.enqueue(new Board(second));
            
            third[zeroI][zeroJ] = third[zeroI+1][zeroJ]; 
            third[zeroI+1][zeroJ] = 0;
            q.enqueue(new Board(third));
            
            fourth[zeroI][zeroJ] = fourth[zeroI][zeroJ+1]; 
            fourth[zeroI][zeroJ+1] = 0;
            q.enqueue(new Board(fourth));
        }
        else if (zeroI == 0) {
            first[zeroI][zeroJ] = first[zeroI+1][zeroJ]; 
            first[zeroI+1][zeroJ] = 0;
            q.enqueue(new Board(first));
            
            if (zeroJ == 0) {
            	second[zeroI][zeroJ] = second[zeroI][zeroJ+1]; 
            	second[zeroI][zeroJ+1] = 0;
            	q.enqueue(new Board(second));
            }
            else if (zeroJ == mDimension-1) {
            	 second[zeroI][zeroJ] = second[zeroI][zeroJ-1]; 
                 second[zeroI][zeroJ-1] = 0;
                 q.enqueue(new Board(second));
            }
            else {
            	second[zeroI][zeroJ] = second[zeroI][zeroJ-1]; 
                second[zeroI][zeroJ-1] = 0;
                q.enqueue(new Board(second));
                
                third[zeroI][zeroJ] = third[zeroI][zeroJ+1]; 
                third[zeroI][zeroJ+1] = 0;
                q.enqueue(new Board(third));
            }
        }
        else if (zeroI == mDimension-1) {
            first[zeroI][zeroJ] = first[zeroI-1][zeroJ]; 
            first[zeroI-1][zeroJ] = 0;
            q.enqueue(new Board(first));
            
            if (zeroJ == 0) {
            	second[zeroI][zeroJ] = second[zeroI][zeroJ+1]; 
            	second[zeroI][zeroJ+1] = 0;
            	q.enqueue(new Board(second));
            }
            else if (zeroJ == mDimension-1) {
            	second[zeroI][zeroJ] = second[zeroI][zeroJ-1]; 
                second[zeroI][zeroJ-1] = 0;
                q.enqueue(new Board(second));
            }
            else {
            	second[zeroI][zeroJ] = second[zeroI][zeroJ-1]; 
                second[zeroI][zeroJ-1] = 0;
                q.enqueue(new Board(second));
                
                third[zeroI][zeroJ] = third[zeroI][zeroJ+1]; 
                third[zeroI][zeroJ+1] = 0;
                q.enqueue(new Board(third));
            }
        }
        else {
        	first[zeroI][zeroJ] = first[zeroI+1][zeroJ]; 
            first[zeroI+1][zeroJ] = 0;
            q.enqueue(new Board(first));
            
            second[zeroI][zeroJ] = second[zeroI-1][zeroJ]; 
            second[zeroI-1][zeroJ] = 0;
            q.enqueue(new Board(second));
            
            if (zeroJ == 0) {
                third[zeroI][zeroJ] = third[zeroI][zeroJ+1]; 
                third[zeroI][zeroJ+1] = 0;
                q.enqueue(new Board(third));
            }
            else if (zeroJ == mDimension-1) { 
                third[zeroI][zeroJ] = third[zeroI][zeroJ-1]; 
                third[zeroI][zeroJ-1] = 0;
                q.enqueue(new Board(third));
            }
        }
        return q;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(mDimension + "\n");
        for (int i = 0; i < mDimension; i++) {
            for (int j = 0; j < mDimension; j++) {
                s.append(String.format("%2d ", mBlocks[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    public static void main(String[] args) {
        // unit tests (not graded)
    }
}