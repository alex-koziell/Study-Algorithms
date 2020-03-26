import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {

    private final LineSegment[] lineSegments;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        // error handling
        if (points == null) throw new IllegalArgumentException("points array was null!");
        checkNullsAndDuplicates(points);

        ArrayList<LineSegment> segmentsList = new ArrayList<LineSegment>();
        Point[] pointsNO = Arrays.copyOf(points, points.length);
        // sort in natural order
        Arrays.sort(pointsNO);

        // go through every combination, now naturally ordered
        for (int p = 0; p < pointsNO.length - 3; ++p) {
            for (int q = p + 1; q < pointsNO.length - 2; ++q) {
                for (int r = q + 1; r < pointsNO.length - 1; ++r) {
                    // check if 3rd point collinear
                    if (pointsNO[p].slopeTo(pointsNO[q]) == pointsNO[p].slopeTo(pointsNO[r])) {
                        for (int s = r + 1; s < pointsNO.length; ++s) {
                            // check if 4th point collinear
                            if (pointsNO[p].slopeTo(pointsNO[q]) == pointsNO[p].slopeTo(pointsNO[s])) {
                                segmentsList.add(new LineSegment(pointsNO[p], pointsNO[s]));
                            }
                        }
                    }
                }
            }
        }

        lineSegments = segmentsList.toArray(new LineSegment[segmentsList.size()]);
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
