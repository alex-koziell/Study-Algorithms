import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {

    private final LineSegment[] lineSegments;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        // error handling
        if (points == null) throw new IllegalArgumentException("points array was null!");
        checkNullsAndDuplicates(points);

        Point[] pointsSO = Arrays.copyOf(points, points.length);
        Point[] pointsNO = Arrays.copyOf(points, points.length);
        ArrayList<LineSegment> segmentList = new ArrayList<LineSegment>();

        // sort points in natural order - we then avoid taking duplicate lines
        // by only taking a line for when the point we are iterating over in the
        // outer loop is the min point of the line.
        Arrays.sort(pointsNO);

        for (int p = 0; p < pointsNO.length; ++p) {
            // points[p] is the origin, and will always lie at the beginning of the slope order array
            Point origin = pointsNO[p];
            Arrays.sort(pointsSO);
            Arrays.sort(pointsSO, origin.slopeOrder());

            Point secondPoint = null;
            int count = 1;

            for (int q = 1; q < pointsSO.length - 1; ++q) {
                // if two adjacent points have the same slope to p
                if (pointsSO[q].slopeTo(origin) == pointsSO[q+1].slopeTo(origin)) {
                    ++count;
                    // if we only have three points (p, q and q+1)
                    if (count == 2) {
                        secondPoint = pointsSO[q];
                        ++count; // count = 3 for three collinear points
                    }
                    // if we have four or more collinear points, and we have no more points to check
                    else if (count >= 4 && q + 1 == pointsSO.length - 1) {
                        // if the origin is the min point on this line
                        if (secondPoint.compareTo(origin) > 0) {
                            // since pointsSO was also sorted in the natural order, the q+1 point is the max
                            segmentList.add(new LineSegment(origin, pointsSO[q + 1]));
                        }
                        count = 1;
                    }
                }
                // if we reached the end of a line containing at least four points
                else if (count >= 4) {
                    if (secondPoint.compareTo(origin) > 0) {
                        segmentList.add(new LineSegment(origin, pointsSO[q]));
                    }
                    count = 1;
                }
                // line was less than four points
                else {
                    count = 1;
                }
            }
        }

        lineSegments = segmentList.toArray(new LineSegment[segmentList.size()]);

    }

    // the number of line segments
    public int numberOfSegments() {
        return lineSegments.length;
    }

    // the line segments
    public LineSegment[] segments() {
        return Arrays.copyOf(lineSegments, lineSegments.length);
    }

    private void checkNullsAndDuplicates(Point[] points) {
        for (int i = 0; i < points.length; ++i) {
            if (points[i] == null) throw new IllegalArgumentException("Found a null point!");
        }

        for (int i = 0; i < points.length - 1; ++i) {
            for (int j = i + 1; j < points.length; ++j) {
                if (points[i].compareTo(points[j]) == 0) throw new IllegalArgumentException("Found duplicate points!");
            }
        }
    }
}