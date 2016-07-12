import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
  private WeightedQuickUnionUF mUnionFind;
  private WeightedQuickUnionUF mUnionFindForBackWash;
  private boolean[] mIsOpened;
  private int mSize;

  public Percolation(int N) {
    mSize = N;
    if (mSize <= 0) {
      throw new IllegalArgumentException("Constructor exception");
    }
    mUnionFind = new WeightedQuickUnionUF(mSize * mSize + 2);
    mUnionFindForBackWash = new WeightedQuickUnionUF(mSize * mSize + 1);
    mIsOpened = new boolean[mSize * mSize + 2];
    mIsOpened[0] = true;
    mIsOpened[mIsOpened.length - 1] = true;
    for (int i = 1; i < mIsOpened.length - 1; i++) {
      mIsOpened[i] = false;
    }
  }

  public void open(int i, int j) {
    if (i < 1 || i > mSize || j < 1 || j > mSize) {
      throw new IndexOutOfBoundsException("Exception");
    }
    final int index = linearIndex(i, j);
    mIsOpened[index] = true;
    if (i > 1 && i < mSize) {
      final int up = linearIndex(i - 1, j);
      if (mIsOpened[up]) {
        mUnionFind.union(index, up);
        mUnionFindForBackWash.union(index, up);
      }
      final int down = linearIndex(i + 1, j);
      if (mIsOpened[down]) {
        mUnionFind.union(index, down);
        mUnionFindForBackWash.union(index, down);
      }
      if (j > 1 && j < mSize) {
        final int left = linearIndex(i, j - 1);
        final int right = linearIndex(i, j + 1);
        if (mIsOpened[left]) {
          mUnionFind.union(index, left);
          mUnionFindForBackWash.union(index, left);
        }
        if (mIsOpened[right]) {
          mUnionFind.union(index, right);
          mUnionFindForBackWash.union(index, right);
        }
      }
      else if (j == 1) {
        final int right = linearIndex(i, j + 1);
        if (mIsOpened[right]) {
          mUnionFind.union(index, right);
          mUnionFindForBackWash.union(index, right);
        }
      }
      else if (j == mSize) {
        final int left = linearIndex(i, j - 1);
        if (mIsOpened[left]) {
          mUnionFind.union(index, left);
          mUnionFindForBackWash.union(index, left);
        }
      }
    }
    else if (i == 1) {
      mUnionFind.union(0, index);
      mUnionFindForBackWash.union(0, index);
      if (j > 1 && j < mSize) {
        final int down = linearIndex(i + 1, j);
        final int left = linearIndex(i, j - 1);
        final int right = linearIndex(i, j + 1);
        if (mIsOpened[down]) {
          mUnionFind.union(index, down);
          mUnionFindForBackWash.union(index, down);
        }
        if (mIsOpened[left]) {
          mUnionFind.union(index, left);
          mUnionFindForBackWash.union(index, left);
        }
        if (mIsOpened[right]) {
          mUnionFind.union(index, right);
          mUnionFindForBackWash.union(index, right);
        }
      }
      else if (j == 1) {
        final int down = linearIndex(i + 1, j);
        final int right = linearIndex(i, j + 1);
        if (mIsOpened[down]) {
          mUnionFind.union(index, down);
          if (mSize != 1) {
            mUnionFindForBackWash.union(index, down);
          }
        }
        if (mIsOpened[right]) {
          mUnionFind.union(index, right);
          if (mSize != 1) {
            mUnionFindForBackWash.union(index, right);
          }
        }
      }
      else if (j == mSize) {
        final int down = linearIndex(i + 1, j);
        final int left = linearIndex(i, j - 1);
        if (mIsOpened[down]) {
          mUnionFind.union(index, down);
          mUnionFindForBackWash.union(index, down);
        }
        if (mIsOpened[left]) {
          mUnionFind.union(index, left);
          mUnionFindForBackWash.union(index, left);
        }
      }
    }
    else if (i == mSize) {
      mUnionFind.union(mIsOpened.length - 1, index);
      final int up = linearIndex(i - 1, j);
      if (mIsOpened[up]) {
        mUnionFind.union(index, up);
        mUnionFindForBackWash.union(index, up);
      }
      if (j > 1 && j < mSize) {
        final int left = linearIndex(i, j - 1);
        final int right = linearIndex(i, j + 1);
        if (mIsOpened[left]) {
          mUnionFind.union(index, left);
          mUnionFindForBackWash.union(index, left);
        }
        if (mIsOpened[right]) {
          mUnionFind.union(index, right);
          mUnionFindForBackWash.union(index, right);
        }
      }
      else if (j == 1) {
        final int right = linearIndex(i, j + 1);
        if (mIsOpened[right]) {
          mUnionFind.union(index, right);
          mUnionFindForBackWash.union(index, right);
        }
      }
      else if (j == mSize) {
        final int left = linearIndex(i, j - 1);
        if (mIsOpened[left]) {
          mUnionFind.union(index, left);
          mUnionFindForBackWash.union(index, left);
        }
      }
    }
  }

  public boolean isOpen(int i, int j) {
    if (i < 1 || i > mSize || j < 1 || j > mSize) {
      throw new IndexOutOfBoundsException("Exception");
    }
    final int index = linearIndex(i, j);
    return mIsOpened[index];
  }

  public boolean isFull(int i, int j) {
    if (i < 1 || i > mSize || j < 1 || j > mSize) {
      throw new IndexOutOfBoundsException("Exception");
    }
    final int index = linearIndex(i, j);
    boolean isFull = false;
    if (mIsOpened[index])
    {
      isFull = mUnionFindForBackWash.connected(0, index); 
    }
    return isFull;
  }

  public boolean percolates() {
    return mUnionFind.connected(0, mIsOpened.length - 1);
  }

  private int linearIndex(int i, int j) {
    return (i - 1) * mSize + (j - 1) + 1;
  }

  public static void main(String[] args) {
    int N = Integer.parseInt(args[0]);
    Percolation per = new Percolation(N);
    for (int i = 1; i < args.length; i += 2) {
      int p = Integer.parseInt(args[i]);
      int q = Integer.parseInt(args[i + 1]);
      per.open(p, q);
    }
    StdOut.println(per.percolates());
  }
}