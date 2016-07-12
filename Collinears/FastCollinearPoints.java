import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

public class FastCollinearPoints {
    private ArrayList<LineSegment> lineSegments;
    private HashMap<Double, Set<Point>> map;
    private Point[] mypoints;
    
    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new NullPointerException("Exception");
        }

        mypoints = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null)
                throw new NullPointerException("Exception");
            for (int j = i; j < points.length; j++) {
                if (i != j && points[i].slopeTo(points[j]) == Double.NEGATIVE_INFINITY)
                    throw new IllegalArgumentException("Exception");
            }
            mypoints[i] = points[i];
        }
        
        lineSegments = new ArrayList<LineSegment>();
        map = new HashMap<Double, Set<Point>>();
        
        ArrayList<Point> collinears = new ArrayList<Point>();
        for (int pointIndex = 0; pointIndex < mypoints.length; pointIndex++) {
            Arrays.sort(mypoints);
            Arrays.sort(mypoints, mypoints[pointIndex].slopeOrder());
            Point currentPoint = mypoints[0];
            collinears.clear();
            collinears.add(currentPoint);
            for (int j = 2; j < mypoints.length; j++) {
                if (mypoints[j-1].slopeTo(currentPoint) == mypoints[j].slopeTo(currentPoint)) {
                    collinears.add(mypoints[j - 1]);
                    if (j == mypoints.length - 1) {
                        collinears.add(mypoints[j]);
                        if (collinears.size() > 3) {
                            push(collinears);
                        }
                    }
                }
                else if (mypoints[j-2].slopeTo(currentPoint) == mypoints[j-1].slopeTo(currentPoint)) {
                    collinears.add(mypoints[j-1]);
                    if (collinears.size() > 3) {
                        push(collinears);
                        collinears.clear();
                        collinears.add(currentPoint);
                    }
                    else {
                        collinears.clear();
                        collinears.add(currentPoint);
                    }
                }
            }
        }
    }

    public int numberOfSegments() {
        return lineSegments.size();
    }

    public LineSegment[] segments() {
        return lineSegments.toArray(new LineSegment[lineSegments.size()]);
    }
    
    private void push(ArrayList<Point> collinears) {
        Point max = Collections.max(collinears);
        double slope = collinears.get(1).slopeTo(collinears.get(0));
        if (map.containsKey(slope)) {
            Set<Point> alreadyHave = map.get(slope);
            if (!alreadyHave.contains(max)) {
                alreadyHave.add(max);
                lineSegments.add(new LineSegment(Collections.min(collinears), max));
            }
        }
        else {
            Set<Point> set = new TreeSet<Point>();
            set.add(max);
            map.put(slope, set);
            lineSegments.add(new LineSegment(Collections.min(collinears), max));
        }
//        StdOut.println(collinears + "===>" + segment.toString());
    }
}