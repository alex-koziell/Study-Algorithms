import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

public class Solver {

    private int moves;
    private final boolean solvable;
    private final Stack<Board> solutionStack;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException("Initial board was null!");

        MinPQ<SearchNode> minPQ = new MinPQ<SearchNode>(manhattanPriority());
        // to detect unsolvable boards
        MinPQ<SearchNode> twinPQ = new MinPQ<SearchNode>(manhattanPriority());

        minPQ.insert(new SearchNode(initial, 0, null));
        twinPQ.insert(new SearchNode(initial.twin(), 0, null));

        solutionStack = new Stack<>();
        while (true) {

            // actual board
            SearchNode dequeued = minPQ.delMin();
            if (dequeued.board.isGoal()) {
                solvable = true;
                moves = dequeued.moves;

                SearchNode currentNode = dequeued;
                for (int i = 0; i < moves + 1; ++i) {
                    solutionStack.push(currentNode.board);
                    currentNode = currentNode.prevNode;
                }
                return;
            }
            // next move: add each possible neighbour to minPQ
            for (Board neighbour : dequeued.board.neighbors()) {
                if (dequeued.moves == 0) {
                    minPQ.insert(new SearchNode(neighbour, dequeued.moves + 1, dequeued));
                } else {
                    if (!neighbour.equals(dequeued.prevNode.board))
                        minPQ.insert(new SearchNode(neighbour, dequeued.moves + 1, dequeued));
                }
            }

            // twin board
            dequeued = twinPQ.delMin();
            // if twin board solution found, break loop leaving solved false
            if (dequeued.board.isGoal()) {
                solvable = false;
                return;
            }
            for (Board neighbour : dequeued.board.neighbors()) {
                if (dequeued.moves == 0) {
                    twinPQ.insert(new SearchNode(neighbour, dequeued.moves + 1, dequeued));
                } else {
                    if (!neighbour.equals(dequeued.prevNode.board))
                        twinPQ.insert(new SearchNode(neighbour, dequeued.moves + 1, dequeued));
                }
            }
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() { return solvable; }

    // min number of moves to solve initial board
    public int moves() { return solvable ? moves : -1; }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() { return solvable ? solutionStack : null; }

    private class SearchNode {
        private final int moves;
        private final Board board;
        private final SearchNode prevNode;
//        private final int hamming;
        private final int manhattan;

        public SearchNode(Board toBoard, int moveNum, SearchNode fromNode) {
            moves = moveNum;
            board = toBoard;
            prevNode = fromNode;
//            hamming = moves + board.hamming();
            manhattan = moves + board.manhattan();
        }

//        public int hamming() { return hamming; }
        public int manhattan() { return manhattan; }

    }

//    private Comparator<SearchNode> hammingPriority() {
//        return new HammingPriority();
//    }

//    private class HammingPriority implements Comparator<SearchNode> {
//        public int compare(SearchNode node1, SearchNode node2) {
//            int p1 = node1.hamming();
//            int p2 = node2.hamming();
//            if (p1 < p2) return -1;
//            if (p1 > p2) return +1;
//            return 0;
//        }
//    }

    private Comparator<SearchNode> manhattanPriority() {
        return new ManhattanPriority();
    }

    private class ManhattanPriority implements Comparator<SearchNode> {
        public int compare(SearchNode node1, SearchNode node2) {
            int p1 = node1.manhattan();
            int p2 = node2.manhattan();
            if (p1 < p2) return -1;
            if (p1 > p2) return +1;
            return 0;
        }
    }

    // test client
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

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