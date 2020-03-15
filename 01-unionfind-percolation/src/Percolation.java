import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    /*
    Models an n-by-n grid with an associated boolean value for each site. The value at each site is
    stored in the array siteValues, where the indexing uses row-major order.
    */
    
    private final WeightedQuickUnionUF openUF;
    private final WeightedQuickUnionUF fillUF;
    private final int gridWidth;
    private final int numSites;
    private int numOpenSites;
    private boolean[] openSites;

    // creates an n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0)
            throw new IllegalArgumentException("Grid size must be a positive integer!");
        gridWidth = n;
        numSites = n*n;
        numOpenSites = 0;

        openUF = new WeightedQuickUnionUF(numSites+2); // additional nodes for 'top' and 'bottom' at [numsites] and [numsites+1] for faster algorithm
        fillUF = new WeightedQuickUnionUF(numSites+1); // just a top node to bypass the 'backwash problem'
        openSites = new boolean[numSites];
        for (int i = 0; i < numSites; ++i) {
            openSites[i] = false;
        }
    }

    // opens the site at (row, col) if not already open
    public void open(int row, int col) {
        if (row > gridWidth || col > gridWidth || row <= 0 || col <= 0)
            throw new IllegalArgumentException("Index out of range: row=" + row + ", col=" + col);

        int index = indexAt(row, col);
        if (!openSites[index]) { // in case we call open() on the same site twice
            openSites[index] = true;
            ++numOpenSites;

            // If any neighbouring sites are open, connect them.
            // Cell above: Check if top row first
            if (row != 1) {
                if (isOpen(row - 1, col)) {
                    openUF.union(index, index - gridWidth);
                    fillUF.union(index, index - gridWidth);
                }
            } else {
                openUF.union(index, numSites); // If top row, connect to top node
                fillUF.union(index, numSites);
            }

            // Cell below: Check if bottom row first
            if (row != gridWidth) {
                if (isOpen(row + 1, col)) {
                    openUF.union(index, index + gridWidth);
                    fillUF.union(index, index + gridWidth);
                }
            } else {
                openUF.union(index, numSites+1); // If bottom row, connect site to bottom node in just one UF object
            }
            // Cell to the left
            if (col != 1) {
                if (isOpen(row, col - 1)) {
                    openUF.union(index, index - 1);
                    fillUF.union(index, index - 1);
                }
            }
            // Cell to the right
            if (col != gridWidth) {
                if (isOpen(row, col + 1)) {
                    openUF.union(index, index + 1);
                    fillUF.union(index, index + 1);
                }
            }
        }
    }

    // is the site at (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row > gridWidth || col > gridWidth || row <= 0 || col <= 0)
            throw new IllegalArgumentException("Index out of range: row=" + row + ", col=" + col);
        return openSites[indexAt(row, col)];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row > gridWidth || col > gridWidth || row <= 0 || col <= 0)
            throw new IllegalArgumentException("Index out of range: row=" + row + ", col=" + col);
        return (fillUF.find(indexAt(row, col)) == fillUF.find(numSites));
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return numOpenSites;
    }
    // does the system percolate?
    public boolean percolates() {
        if (openUF.find(numSites) == openUF.find(numSites+1))
            return true;
        return false;
    }

    private int indexAt(int row, int col) {
        return (row-1)*gridWidth+col-1;
    }

    // test client (optional)
    public static void main(String[] args) {
        // Not used.
    }
}