import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;

public class KdTree {

    private Node root;
    private int numNodes;

    // construct an empty set of points
    public KdTree() {
        root = null;
        numNodes = 0;
    }

    // is the set empty?
    public boolean isEmpty() { return (numNodes == 0); }

    // number of points in the set
    public int size() { return numNodes; }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Null argument!");
        root = insert(root, p, true, 0, 0, 1, 1);
    }


    private Node insert(Node node, Point2D p, boolean compareX, double minX, double minY, double maxX, double maxY) {
        if (node == null) {
            ++numNodes;

            // create a new leaf
            RectHV rect;
            if (compareX) rect = new RectHV(p.x(), minY, p.x(), maxY); // create vertical line
            else rect = new RectHV(minX, p.y(), maxX, p.y()); // horizontal line
            return new Node(p, rect);
        }
        // if we find the point already in the tree
        if (node.p.equals(p)) return node;

        if (compareX) {
            // current node vertical line; next one is horizontal
            if (p.x() < node.p.x()) node.lb = insert(node.lb, p, false, minX, minY, node.p.x(), maxY);
            else node.rt = insert(node.rt, p, false, node.p.x(), minY, maxX, maxY);
        } else {
            // current node horizontal line
            if (p.y() < node.p.y()) node.lb = insert(node.lb, p, true, minX, minY, maxX, node.p.y());
            else node.rt = insert(node.rt, p, true, minX, node.p.y(), maxX, maxY);
        }

        return node;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Null argument!");
        return get(root, p, true);
    }

    private boolean get(Node node, Point2D p, boolean compareX) {
        if (node == null) return false;
        if (node.p.equals(p)) return true;
        boolean cmp = compareX ? (p.x() < node.p.x()) : (p.y() < node.p.y());
        if (cmp) return get(node.lb, p, !compareX);
        else     return get(node.rt, p, !compareX);
    }


    // draw all points to standard draw
    public void draw() {
        ArrayList<Node> allNodes = new ArrayList<>();
        allNodes(root, allNodes);

        for (Node node : allNodes) {
            StdDraw.setPenColor(StdDraw.BLACK);
            node.p.draw();
            StdDraw.setPenColor(StdDraw.BLUE);
            if (node.rect.xmin() == node.rect.xmax()) StdDraw.setPenColor(StdDraw.RED);
            node.rect.draw();
        }
    }

    private void allNodes(Node node, ArrayList<Node> nodes) {
        if (node == null) return;
        nodes.add(node);
        allNodes(node.lb, nodes);
        allNodes(node.rt, nodes);
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("Null argument!");

        ArrayList<Point2D> results = new ArrayList<>();
        range(root, rect, true, results);

        return results;
    }

    private void range(Node node, RectHV rect, boolean compareX, ArrayList<Point2D> results) {
        if (node == null) return;
        // if this point in the rectangle, we might have more points on either side
        if (rect.contains(node.p)) {
            results.add(node.p);
            range(node.lb, rect, !compareX, results);
            range(node.rt, rect, !compareX, results);
            return;
        }

        // if rectangle min bounds to the left or below, search left/bottom branch
        if (compareX ? rect.xmin() < node.p.x() : rect.ymin() < node.p.y()) range(node.lb, rect, !compareX, results);
        // if rectangle max bounds above and to the right, search right/top branch
        if (compareX ? node.p.x() < rect.xmax() : node.p.y() < rect.ymax()) range(node.rt, rect, !compareX, results);

    }


    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Null argument!");
        return isEmpty() ? null : nearest(root, p, true, root.p);
    }

    private Point2D nearest(Node node, Point2D queryPt, boolean compareX, Point2D champion) {
        if (node == null) return champion;
        if (node.p.equals(queryPt)) return node.p;

        // if this point is closer, make the new champion
        double closestDist = champion.distanceSquaredTo(queryPt);
        if (node.rect.distanceSquaredTo(queryPt) < closestDist) {
            if (node.p.distanceSquaredTo(queryPt) < closestDist) champion = node.p;
            // may be closer points to left or below
            if (compareX ? node.p.x() > queryPt.x() : node.p.y() > queryPt.y()) champion = nearest(node.lb, queryPt, !compareX, champion);
            else champion = nearest(node.rt, queryPt, !compareX, champion);
        }

        return champion;
    }

    private static class Node {
        private final Point2D p;      // the point
        private final RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree

        Node(Point2D p, RectHV rect) {
            this.p = p;
            this.rect = rect;
            this.lb = null;
            this.rt = null;
        }
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        // initialize the two data structures with point from file
        String filename = args[0];
        In in = new In(filename);
        KdTree kdTree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdTree.insert(p);
        }

        System.out.println(kdTree.contains(new Point2D(0.1, 0.3)));
        System.out.println(kdTree.contains(new Point2D(0.206107, 0.095492)));
        System.out.println(kdTree.range(new RectHV(0, 0, 0.5, 1)));
        System.out.println(kdTree.nearest(new Point2D(0.1, 0.3)));
        System.out.println(kdTree.nearest(new Point2D(0.206106, 0.095493)));

    }
}