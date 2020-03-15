import edu.princeton.cs.algs4.StdStats;
import static edu.princeton.cs.algs4.StdRandom.uniform;
import static java.lang.Math.sqrt;

public class PercolationStats {

    double[] thresholds;
    int numTrials; // number of trials
    Percolation percolation;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n<=0 || trials<=0)
            throw new IllegalArgumentException(("n and trials must be >0!"));

        numTrials = trials;
        thresholds = new double[trials];

        int numSites = n*n;
        for (int i=0; i<trials; ++i) {
            percolation = new Percolation(n);
            Double numOpens = 0.0;
            while (!percolation.percolates()) {
                percolation.open(uniform(n), uniform(n));
                ++numOpens;
            }
            thresholds[i] = numOpens/numSites;
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(thresholds);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(thresholds);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean()-stddev()*1.96/sqrt(numTrials);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean()+stddev()*1.96/sqrt(numTrials);
    }

    // test client (see below)
    public static void main(String[] args) {
        PercolationStats percolationStats = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        System.out.println("Mean                    = " + percolationStats.mean());
        System.out.println("Stddev                  = " + percolationStats.stddev());
        System.out.println("95% Confidence Interval = [" + percolationStats.confidenceLo() + ", " + percolationStats.confidenceHi()+"]");
    }

}