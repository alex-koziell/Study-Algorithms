import edu.princeton.cs.algs4.*;

import java.util.ArrayList;

public class PointSET {

    private final SET<Point2D> points;

    // construct an empty set of points
    public PointSET() { points = new SET<>(); }

    // is the set empty?
    public boolean isEmpty() { return points.isEmpty(); }

    // number of points in the set
    public int size() { return points.size(); }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Null argument!");
        if (!points.contains(p)) points.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Null argument!");
        return points.contains(p);
    }

    // draw all points to standard draw
    public void draw() { for (Point2D point : points) point.draw(); }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("Null argument!");

        ArrayList<Point2D> inPoints = new ArrayList<>();
        for (Point2D point : points) {
            if (rect.contains(point)) inPoints.add(point);
        }
        return inPoints;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Null argument!");

        Point2D nearestPoint = null;
        for (Point2D point : points) {
            if (nearestPoint == null || (p.distanceTo(point) < p.distanceTo(nearestPoint))) nearestPoint = point;
        }
        return nearestPoint;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        // initialize the two data structures with point from file
        String filename = args[0];
        In in = new In(filename);
        PointSET brute = new PointSET();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            brute.insert(p);
        }

        // process nearest neighbor queries
        StdDraw.enableDoubleBuffering();
        while (true) {

            // the location (x, y) of the mouse
            double x = StdDraw.mouseX();
            double y = StdDraw.mouseY();
            Point2D query = new Point2D(x, y);

            // draw all of the points
            StdDraw.clear();
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            brute.draw();

            // draw in red the nearest neighbor (using brute-force algorithm)
            StdDraw.setPenRadius(0.03);
            StdDraw.setPenColor(StdDraw.RED);
            brute.nearest(query).draw();
            StdDraw.setPenRadius(0.02);
        }

    }
}