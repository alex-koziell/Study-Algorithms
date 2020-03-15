import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdRandom;

public class PercolationStats {

    private static final double CONFIDENCE_95 = 1.96;
    private final int numTrials; // number of trials
    private final double meanValue;
    private final double stdDevValue;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0)
            throw new IllegalArgumentException(("n and trials must be >0!"));

        numTrials = trials;
        double[] thresholds = new double[trials];

        int numSites = n*n;
        for (int i = 0; i < trials; ++i) {
            Percolation percolation = new Percolation(n);
            while (!percolation.percolates()) {
                percolation.open(StdRandom.uniform(1, n+1), StdRandom.uniform(1, n+1));
            }
            double numOpens = percolation.numberOfOpenSites();
            thresholds[i] = numOpens/numSites;
        }

        meanValue = StdStats.mean(thresholds);
        stdDevValue = StdStats.stddev(thresholds);
    }

    // sample mean of percolation threshold
    public double mean() {
        return meanValue;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return stdDevValue;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return meanValue-stdDevValue*CONFIDENCE_95/Math.sqrt(numTrials);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return meanValue+stdDevValue*CONFIDENCE_95/Math.sqrt(numTrials);
    }

    // test client (see below)
    public static void main(String[] args) {
        PercolationStats percolationStats = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        System.out.println("Mean                    = " + percolationStats.mean());
        System.out.println("Stddev                  = " + percolationStats.stddev());
        System.out.println("95% Confidence Interval = [" + percolationStats.confidenceLo() + ", " + percolationStats.confidenceHi()+"]");
    }

}