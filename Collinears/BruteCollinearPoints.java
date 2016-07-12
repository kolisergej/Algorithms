import java.util.Arrays;
import java.util.ArrayList;

public class BruteCollinearPoints {
    private ArrayList<LineSegment> lineSegments;

    public BruteCollinearPoints(Point[] points) {
        if (points == null)
        {
            throw new NullPointerException("Exception");
        }

        for (int i = 0; i < points.length; i++) {
            if (points[i] == null)
                throw new NullPointerException("Exception");
            for (int j = i; j < points.length; j++) {
                if (i != j && points[i].slopeTo(points[j]) == Double.NEGATIVE_INFINITY)
                    throw new IllegalArgumentException("Exception");
            }
        }

        lineSegments = new ArrayList<LineSegment>();

        for (int i = 0; i < points.length; i++) {
            for (int j = i+1; j < points.length; j++) {
                for (int k = j+1; k < points.length; k++) {
                    double slope1 = points[i].slopeTo(points[j]);
                    double slope2 = points[j].slopeTo(points[k]);
                    if (slope1 == slope2)
                    {
                        for (int z = k+1; z < points.length; z++) {    
                            double slope3 = points[k].slopeTo(points[z]);
                            if (slope2 == slope3) {
                                Point[] tmp = new Point[4];
                                tmp[0] = points[i];
                                tmp[1] = points[j];
                                tmp[2] = points[k];
                                tmp[3] = points[z];
                                Arrays.sort(tmp);
                                lineSegments.add(new LineSegment(tmp[0], tmp[3]));
                            }
                        }
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
}
