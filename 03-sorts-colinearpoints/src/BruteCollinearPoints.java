import javax.sound.sampled.Line;

public class BruteCollinearPoints {

    private LineSegment[] lineSegments;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException("points array was null!");

        lineSegments = new LineSegment[0];

        // go through every combination
        for (int p = 0; p < points.length; ++p) {
            if (points[p] == null) throw new IllegalArgumentException("Found a null point!");
            for (int q = p + 1; q < points.length; ++q) {
                if (points[p].equals(points[q])) throw new IllegalArgumentException("Two identical points found!");
                for (int r = q + 1; r < points.length; ++r) {
                    if (points[p].equals(points[r])) throw new IllegalArgumentException("Two identical points found!");
                    if (points[q].equals(points[r])) throw new IllegalArgumentException("Two identical points found!");
                    for (int s = r + 1; s < points.length; ++s) {
                        if (points[p].equals(points[s])) throw new IllegalArgumentException("Two identical points found!");
                        if (points[q].equals(points[s])) throw new IllegalArgumentException("Two identical points found!");
                        if (points[r].equals(points[s])) throw new IllegalArgumentException("Two identical points found!");

                        // points are collinear iff their pairwise line segments are all parallel
                        // p --- q --- r -- s
                        // check if collinear
                        if (Math.abs(points[p].slopeTo(points[q])) == Math.abs(points[p].slopeTo(points[r])) &&
                            Math.abs(points[p].slopeTo(points[q])) == Math.abs(points[p].slopeTo(points[s]))) {
                            // if collinear, order the points
                            int bottomLeft = p;
                            int topRight = p;
                            if (points[q].compareTo(points[bottomLeft]) == -1) bottomLeft = q;
                            if (points[r].compareTo(points[bottomLeft]) == -1) bottomLeft = r;
                            if (points[s].compareTo(points[bottomLeft]) == -1) bottomLeft = s;
                            if (points[r].compareTo(points[topRight]) == +1) topRight = r;
                            if (points[q].compareTo(points[topRight]) == +1) topRight = q;
                            if (points[s].compareTo(points[topRight]) == +1) topRight = s;

                            // memory-saving solution (slow)
                            LineSegment[] oldLineSegments = lineSegments;
                            lineSegments = new LineSegment[lineSegments.length+1];
                            for (int i = 0; i < oldLineSegments.length; ++i) lineSegments[i] = oldLineSegments[i];
                            lineSegments[lineSegments.length-1] = new LineSegment(points[bottomLeft], points[topRight]);
                        }
                    }
                }
            }
        }

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
