import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.Stopwatch;
import edu.princeton.cs.algs4.In;

public class KdTree {
    private static class Node {
        private Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree
        
        Node(Point2D initPoint, RectHV initRect) {
            p = initPoint;
            rect = initRect;
            lb = null;
            rt = null;
        }
    }

    private Node mRoot;
    private int mSize;
    private Point2D mNearestPoint;
    private double mNearestDistance;

    public KdTree() {
        mRoot = null;
        mSize = 0;
    }

    public boolean isEmpty() {
        return mSize == 0;
    }

    public int size() {
        return mSize;
    }

    public void insert(Point2D p) {
        if (p == null) {
            throw new NullPointerException("Null argument");
        }
        boolean isXCompare = true;
        double xMin = 0;
        double xMax = 1;
        double yMin = 0;
        double yMax = 1;
        if (mRoot == null) {
            mRoot = new Node(p, new RectHV(xMin, yMin, xMax, yMax));
            ++mSize;
        }
        else {
            Node tmp = mRoot;
            while (tmp != null && !tmp.p.equals(p)) {
                if (isXCompare) {
                    yMin = tmp.rect.ymin();
                    yMax = tmp.rect.ymax();
                    if (p.x() < tmp.p.x()) {
                        xMin = tmp.rect.xmin();
                        xMax = tmp.p.x();
                        if (tmp.lb != null) {
                            tmp = tmp.lb;
                        }
                        else {
                            tmp.lb = new Node(p, new RectHV(xMin, yMin, xMax, yMax));
                            ++mSize;
                            break;
                        }
                    }
                    else {
                        xMin = tmp.p.x(); 
                        xMax = tmp.rect.xmax();
                        if (tmp.rt != null) {
                            tmp = tmp.rt;
                        }
                        else {
                            tmp.rt = new Node(p, new RectHV(xMin, yMin, xMax, yMax));
                            ++mSize;
                            break;
                        }
                    }
                }
                else {
                    xMin = tmp.rect.xmin();
                    xMax = tmp.rect.xmax();
                    if (p.y() < tmp.p.y()) {
                        yMin = tmp.rect.ymin();
                        yMax = tmp.p.y();
                        if (tmp.lb != null) {
                            tmp = tmp.lb;
                        }
                        else {
                            tmp.lb = new Node(p, new RectHV(xMin, yMin, xMax, yMax));
                            ++mSize;
                            break;
                        }   
                    }
                    else {
                        yMin = tmp.p.y();
                        yMax = tmp.rect.ymax();
                        if (tmp.rt != null) {
                            tmp = tmp.rt;
                        }
                        else {
                            tmp.rt = new Node(p, new RectHV(xMin, yMin, xMax, yMax));
                            ++mSize;
                            break;
                        }
                    }
                }
                isXCompare = !isXCompare;
            }
        } 
    }

    public boolean contains(Point2D p) {
        if (p == null) {
            throw new NullPointerException("Null argument");
        }
        boolean isXCompare = true;
        boolean result = false;
        Node tmp = mRoot;
        while (tmp != null && !result) {
            if (tmp.p.equals(p)) {
                result = true;
            }
            else if (isXCompare) {   
                if (p.x() < tmp.p.x()) {
                    tmp = tmp.lb;
                }
                else {
                    tmp = tmp.rt;
                }
            }
            else {
                if (p.y() < tmp.p.y()) {
                    tmp = tmp.lb;
                }
                else
                {
                    tmp = tmp.rt;
                }
            }
            isXCompare = !isXCompare;
        }
        return result;
    }

    // draw all of the points to standard draw
    public void draw() {
        boolean red = true;
        Node x = mRoot;
        drawInOrder(x, red);
    }

    private void drawInOrder(Node x, boolean red) {
        if (x != null) {
            drawInOrder(x.lb, !red);
            StdDraw.setPenColor(StdDraw.BLACK); // point
            StdDraw.setPenRadius();
            x.p.draw();
            if (red) {
                StdDraw.setPenColor(StdDraw.RED); //vertical
                StdDraw.line(x.p.x(), x.rect.ymin(), x.p.x(), x.rect.ymax());
            }
            else {
                StdDraw.setPenColor(StdDraw.BLUE); // horizontal
                StdDraw.line(x.rect.xmin(), x.p.y(), x.rect.xmax(), x.p.y());
            }
            drawInOrder(x.rt, !red);
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new NullPointerException("Null argument");
        }
        return range(mRoot, rect, new Queue<Point2D>());
    }

    private Iterable<Point2D> range(Node x, RectHV rect, Queue<Point2D> queue) {
        if (x != null) {
            if (x.rect.intersects(rect)) {
                if (rect.contains(x.p)) {
                    queue.enqueue(x.p);
                }
                range(x.lb, rect, queue);
                range(x.rt, rect, queue);
            }
        }
        return queue;
    }

    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new NullPointerException("Null argument");
        }
        if (mRoot == null) {
            return null;
        }
        mNearestDistance = Double.POSITIVE_INFINITY;
        nearestNode(mRoot, p, true);
        return mNearestPoint;
    }

    private void nearestNode(Node node, Point2D p, boolean isXCompared) {
        if (node != null) {
            if (isXCompared) {
                if (p.x() < node.p.x()) {
                    nearestNode(node.lb, p, !isXCompared);
                    checkDistance(node, p);
                    
                    if (node.rt != null && node.rt.rect.distanceSquaredTo(p) < mNearestDistance) {
                        nearestNode(node.rt, p, !isXCompared);
                    }
                }
                else {
                    nearestNode(node.rt, p, !isXCompared);
                    checkDistance(node, p);
                    if (node.lb != null && node.lb.rect.distanceSquaredTo(p) < mNearestDistance) {
                        nearestNode(node.lb, p, !isXCompared);
                    }
                }
            }
            else {
                if (p.y() < node.p.y()) {
                    nearestNode(node.lb, p, !isXCompared);
                    checkDistance(node, p);
                    if (node.rt != null && node.rt.rect.distanceSquaredTo(p) < mNearestDistance) {
                        nearestNode(node.rt, p, !isXCompared);
                    }
                }
                else {
                    nearestNode(node.rt, p, !isXCompared);
                    checkDistance(node, p);
                    if (node.lb != null && node.lb.rect.distanceSquaredTo(p) < mNearestDistance) {
                        nearestNode(node.lb, p, !isXCompared);
                    }
                }
            }
        }
    }
    
    private void checkDistance(Node node, Point2D point) {
        double currentDistance = node.p.distanceSquaredTo(point);
        if (mNearestDistance > currentDistance) {
            mNearestDistance = currentDistance;
            mNearestPoint = node.p;
        }
    }

    public static void main(String[] args) {
        Stopwatch w = new Stopwatch();
        String filename = args[0];
        In in = new In(filename);
        StdDraw.show(0);

        // initialize the data structures with N points from standard input
        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
        }
        StdDraw.clear();
        kdtree.draw();
        Point2D p = new Point2D(0.01, 0.01);
        StdDraw.setPenColor(StdDraw.BLACK); // point
        StdDraw.setPenRadius(0.02);
        p.draw();
        StdDraw.setPenColor(StdDraw.GREEN); // point
        StdDraw.setPenRadius(0.02);
        //        kdtree.nearest(p).draw();
        StdDraw.show(50);
        System.out.print(w.elapsedTime());
    }
}
