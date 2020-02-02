import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.ArrayList;

public class KdTree {

    private enum Seperator {HORIZONTAL, VERTICAL}

    private Node root;

    private int size;

    // construct an empty set of points
    public KdTree() {
        root = null;
        this.size = 0;
    }

    // is the set empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();

        if (root == null) {
            root = new Node(p, Seperator.VERTICAL, new RectHV(0, 0, 1, 1));
            size++;
        }
        else {
            Node node = root;
            Node insertionNode = null;

            while (node != null) {
                if (node.point.equals(p)) return;

                insertionNode = node;

                if (node.isLeftBottomOf(p)) node = node.rt;
                else node = node.lb;
            }

            if (insertionNode.isLeftBottomOf(p)) {
                insertionNode.rt = new Node(p, insertionNode.nextLevelSeperator(),
                                            insertionNode.nextRect(p));
            }
            else {
                insertionNode.lb = new Node(p, insertionNode.nextLevelSeperator(),
                                            insertionNode.nextRect(p));
            }

            size++;
        }
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();

        if (root == null) return false;

        Node node = root;

        while (node != null) {
            if (node.point.equals(p)) return true;

            if (node.isLeftBottomOf(p)) node = node.rt;
            else node = node.lb;
        }

        return false;
    }

    // draw all points to standard draw
    public void draw() {
        draw(root);
    }

    private void draw(Node node) {
        if (node == null) return;

        node.point.draw();
        draw(node.rt);
        draw(node.lb);
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        ArrayList<Point2D> pointsList = new ArrayList<>();
        fetchPoints(root, rect, pointsList);

        return pointsList;
    }

    private void fetchPoints(Node node, RectHV rect, ArrayList<Point2D> pointsList) {
        if (node == null) return;

        if (rect.contains(node.point)) {
            pointsList.add(node.point);
            fetchPoints(node.rt, rect, pointsList);
            fetchPoints(node.lb, rect, pointsList);
            return;
        }

        if (!node.isLeftBottomOf(new Point2D(rect.xmin(), rect.ymin()))) {
            fetchPoints(node.lb, rect, pointsList);
        }

        if (node.isLeftBottomOf(new Point2D(rect.xmax(), rect.ymax()))) {
            fetchPoints(node.rt, rect, pointsList);
        }

    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();

        if (isEmpty()) return null;

        return nearest(p, root.point, root);
    }

    private Point2D nearest(Point2D p, Point2D closest, Node node) {
        if (node == null) return closest;

        double closestDist = closest.distanceTo(p);
        if (node.rect.distanceTo(p) < closestDist) {
            double dist = node.point.distanceTo(p);
            if (dist < closestDist) {
                closest = node.point;
            }

            if (node.isLeftBottomOf(p)) {
                closest = nearest(p, closest, node.rt);
                closest = nearest(p, closest, node.lb);
            }
            else {
                closest = nearest(p, closest, node.lb);
                closest = nearest(p, closest, node.rt);
            }

        }

        return closest;
    }

    private static class Node {
        private Point2D point;
        private Node lb;
        private Node rt;
        private Seperator seperator;
        private RectHV rect;

        Node(Point2D point, Seperator seperator, RectHV rect) {
            this.point = point;
            this.seperator = seperator;
            this.rect = rect;
        }

        public Seperator nextLevelSeperator() {
            return (this.seperator == Seperator.VERTICAL ? Seperator.VERTICAL :
                    Seperator.HORIZONTAL);
        }

        public boolean isLeftBottomOf(Point2D q) {
            if (this.seperator == Seperator.VERTICAL) {
                return point.x() < q.x();
            }
            else {
                return point.y() < point.y();
            }
        }

        public RectHV nextRect(Point2D q) {
            if (isLeftBottomOf(q)) {
                return (this.seperator == Seperator.VERTICAL ?
                        new RectHV(this.point.x(), this.rect.ymin(), this.rect.xmax(),
                                   this.rect.ymax())
                                                             :
                        new RectHV(this.rect.xmin(), this.point.y(), this.rect.xmax(),
                                   this.rect.ymax())
                );

            }
            else {
                return (this.seperator == Seperator.VERTICAL ?
                        new RectHV(this.rect.xmin(), this.rect.ymin(), this.point.x(),
                                   this.rect.ymax())
                                                             :
                        new RectHV(this.rect.xmin(), this.rect.ymin(), this.rect.xmax(),
                                   this.point.y()));
            }
        }

    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {

    }
}
