import java.util.TreeSet;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.Stopwatch;
import edu.princeton.cs.algs4.RectHV;

public class PointSET {
    private TreeSet<Point2D> mTreeSet;
    
    public PointSET() {
        mTreeSet = new TreeSet<Point2D>(); 
    }
    
    public boolean isEmpty() {
        return mTreeSet.isEmpty();
    }
    
    public int size() {
        return mTreeSet.size(); 
    }
    
    public void insert(Point2D p) {
        if (p == null) {
            throw new NullPointerException("Null argument");
        }
        mTreeSet.add(p);
    }
    
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new NullPointerException("Null argument");
        }
        return mTreeSet.contains(p); 
    }
    
    public void draw() {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(.01);
        for (Point2D point : mTreeSet) {
            point.draw();
        } 
    }
    
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new NullPointerException("Null argument");
        }
        
        Queue<Point2D> queue = new Queue<Point2D>();
        for (Point2D point2d : mTreeSet) {
            if (rect.contains(point2d)) {
                queue.enqueue(point2d);
            }
        }
        return queue;
    }
    
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new NullPointerException("Null argument");
        }
        Point2D nearest = null;
        double minDistance = Double.POSITIVE_INFINITY;
        for (Point2D point2d : mTreeSet) {
            double tmpDistance = p.distanceSquaredTo(point2d); 
            if (tmpDistance < minDistance) {
                minDistance = tmpDistance;
                nearest = point2d;
            }
        }
        return nearest; 
    }

    public static void main(String[] args) {
        Stopwatch w = new Stopwatch();
        String filename = args[0];
        In in = new In(filename);
        StdDraw.show(0);

        // initialize the data structures with N points from standard input
        PointSET pointSettree = new PointSET();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            pointSettree.insert(p);
        }
        StdDraw.clear();
        pointSettree.draw();
        Point2D p = new Point2D(0.01, 0.01);
        StdDraw.setPenColor(StdDraw.BLACK); // point
        StdDraw.setPenRadius(0.02);
        p.draw();
        StdDraw.setPenColor(StdDraw.GREEN); // point
        StdDraw.setPenRadius(0.02);
        pointSettree.nearest(p).draw();
        StdDraw.show(50);
        System.out.print(w.elapsedTime());
    }
}
