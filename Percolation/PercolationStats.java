import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
  private double[] means;
  
  public PercolationStats(int N, int T) {
    if (N <= 0 || T <= 0) {
      throw new IllegalArgumentException("Incorrect params");
    }
    means = new double[T];
    for (int i = 0; i < means.length; i++) {
      Percolation p = new Percolation(N);
      int counter = 0;
      while (!p.percolates()) {
        int a = StdRandom.uniform(1, N + 1);
        int b = StdRandom.uniform(1, N + 1);
        if (!p.isOpen(a, b)) {
          p.open(a, b);
          ++counter;
        }
      }
      means[i] = (double) counter / (N * N);
    }
  }

  public double mean() {
    return StdStats.mean(means);
  }

  public double stddev() {
    return StdStats.stddev(means);
  }

  public double confidenceLo() {
    return mean() - (1.96 * stddev() / Math.sqrt(means.length));
  }

  public double confidenceHi() {
    return mean() + (1.96 * stddev() / Math.sqrt(means.length));
  }

  public static void main(String[] args) {
    int N = StdIn.readInt();
    int T = StdIn.readInt();
    PercolationStats pStats = new PercolationStats(N, T);
    StdOut.println("mean = " + pStats.mean());
    StdOut.println("stddev = " + pStats.stddev());
    StdOut.println("95% confidence interval = " + pStats.confidenceLo() + ", " + pStats.confidenceHi());
  }
}