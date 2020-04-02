import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;

import java.util.Arrays;

public class Board {

    private final int n;
    private int blankRow;
    private int blankCol;
    private final int[][] tiles;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] fromTiles) {

        n = fromTiles.length;
        tiles = new int[n][n];
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                tiles[i][j] = fromTiles[i][j];
                if (tiles[i][j] == 0) {
                    blankRow = i;
                    blankCol = j;
                }
            }
        }

    }

    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", tiles[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        int outOfPlace = 0;
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                if (tiles[i][j] != i*n + j + 1) ++outOfPlace;
            }
        }
        // counted one extra for blank tile
        --outOfPlace;
        return outOfPlace;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int sum = 0;
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                if (tiles[i][j] != 0) {
                    // correct number i*n + (j + 1), with j < n
                    // floor(t/n) should therefore be equal to i
                    // and t % n equal to j
                    int t = tiles[i][j] - 1;
                    sum += Math.abs(t/n - i) + Math.abs(t % n - j);
                }
            }
        }
        return sum;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return (hamming() == 0);
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        if (n != that.dimension()) return false;
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                if (this.tiles[i][j] != that.tiles[i][j]) return false;
            }
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        // consider neighbours by ways we can move blank square
        Stack<Board> neighbourStack = new Stack<Board>();

        // swap blank with top tile
        if (blankRow > 0) {
            int[][] neighbourTiles = tilesCopy();
            neighbourTiles[blankRow][blankCol] = neighbourTiles[blankRow-1][blankCol];
            neighbourTiles[blankRow-1][blankCol] = 0;

            Board neighbour = new Board(neighbourTiles);

            neighbourStack.push(neighbour);
        }
        // tile on left
        if (blankCol > 0) {
            int[][] neighbourTiles = tilesCopy();
            neighbourTiles[blankRow][blankCol] = neighbourTiles[blankRow][blankCol-1];
            neighbourTiles[blankRow][blankCol-1] = 0;

            Board neighbour = new Board(neighbourTiles);

            neighbourStack.push(neighbour);
        }
        // tile on bottom
        if (blankRow < n-1) {
            int[][] neighbourTiles = tilesCopy();
            neighbourTiles[blankRow][blankCol] = neighbourTiles[blankRow+1][blankCol];
            neighbourTiles[blankRow+1][blankCol] = 0;

            Board neighbour = new Board(neighbourTiles);

            neighbourStack.push(neighbour);
        }
        // tile on right
        if (blankCol < n-1) {
            int[][] neighbourTiles = tilesCopy();
            neighbourTiles[blankRow][blankCol] = neighbourTiles[blankRow][blankCol+1];
            neighbourTiles[blankRow][blankCol+1] = 0;

            Board neighbour = new Board(neighbourTiles);

            neighbourStack.push(neighbour);
        }

        return neighbourStack;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        // take top left most tiles that are not blank
        int row1 = 0;
        int col1 = 0;
        while (tiles[row1][col1] == 0) {
            if (col1 == n-1) {
                col1 = 0;
                ++row1;
            } else {
                ++col1;
            }
        }
        int row2 = 0;
        int col2 = 1;
        while (tiles[row2][col2] == 0 || (row2 == row1 && col2 == col1)) {
            if (col2 == n-1) {
                col2 = 0;
                ++row2;
            } else {
                ++col2;
            }
        }

        int[][] twinTiles = tilesCopy();

        int tmpTile = twinTiles[row1][col1];
        twinTiles[row1][col1] = twinTiles[row2][col2];
        twinTiles[row2][col2] = tmpTile;

        return new Board(twinTiles);
    }

    private int[][] tilesCopy() {
        int[][] copy = new int[n][n];
        for (int i = 0; i < n; ++i) {
            copy[i] = Arrays.copyOf(tiles[i], n);
        }
        return copy;
    }

    private void summary() {
        System.out.println(toString());
        System.out.println("Dimension: " + dimension());
        System.out.println("Hamming: " + hamming());
        System.out.println("Manhattan: " + manhattan());
        System.out.println("Solved: " + isGoal());
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        // for each command-line argument
        for (String filename : args) {

            // read in the board specified in the filename
            In in = new In(filename);
            int n = in.readInt();
            int[][] tiles = new int[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    tiles[i][j] = in.readInt();
                }
            }

            Board myBoard = new Board(tiles);
            myBoard.summary();
            System.out.println("Same as itself: " + myBoard.equals(myBoard));


            for (Board neighbour : myBoard.neighbors()) {
                neighbour.summary();
                System.out.println("Same as original: " + neighbour.equals(myBoard));
            }

            myBoard.twin().summary();
            System.out.println("Same as original: " + myBoard.twin().equals(myBoard));
        }
    }

}