import javax.sound.sampled.Line;
import java.util.Arrays;

public class FastCollinearPoints {

    LineSegment[] lineSegments;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException("points array was null!");

        lineSegments = new LineSegment[0];

        for (int p = 0; p < points.length; ++p) {
            if (points[p] == null) throw new IllegalArgumentException("Found a null point!");

            Point[] pairs = new Point[points.length-1-p];

            for (int q = p + 1; q < points.length; ++q) {
                if (points[p].equals(points[q])) throw new IllegalArgumentException("Two identical points found!");
                pairs[q-1-p] = points[q];
            }

            Arrays.sort(pairs, points[p].slopeOrder());

            for (int q = 0; q < pairs.length - 1; ++q) {

                double firstSlope = points[p].slopeTo(pairs[q]);

                if (firstSlope == points[p].slopeTo(pairs[q+1])) {

                    // found three collinear points, see if there are more
                    int numInLine = 3;
                    for (int r = q + 2; r < pairs.length; ++r) {
                        if (firstSlope != points[p].slopeTo(pairs[r])) break;
                        ++numInLine;
                    }

                    // order them, points[p] always lies at one end
                    // ascending order
                    if (points[p].compareTo(pairs[q]) == -1) {
                        Point top = pairs[q];
                        for (int i = q+1; i < q+numInLine-2; ++i) {
                            if (top.compareTo(pairs[i]) == -1) top = pairs[i];
                        }
                        createSegment(points[p], top);
                    }
                    // descending order
                    if (points[p].compareTo(pairs[q]) == +1) {
                        Point bottom = pairs[q];
                        for (int i = q+1; i < q+numInLine-2; ++i) {
                            if (bottom.compareTo(pairs[i]) == +1) bottom = pairs[i];
                        }
                        createSegment(bottom, points[p]);
                    }

                }


            }
        }

    }

    private void createSegment(Point bottomLeft, Point topRight) {
        // memory-saving solution (slow)
        LineSegment[] oldLineSegments = lineSegments;
        lineSegments = new LineSegment[lineSegments.length+1];
        for (int i = 0; i < oldLineSegments.length; ++i) lineSegments[i] = oldLineSegments[i];
        lineSegments[lineSegments.length-1] = new LineSegment(bottomLeft, topRight);
    }

    // the number of line segments
    public int numberOfSegments() {
        return lineSegments.length;
    }

    // the line segments
    public LineSegment[] segments() {
        return lineSegments;
    }
}