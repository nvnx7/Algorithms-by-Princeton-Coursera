import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MergeX;
import edu.princeton.cs.algs4.Quick;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;

public class FastCollinearPoints {

    private LineSegment[] lineSegments;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException();

        for (Point p : points) {
            if (p == null) throw new IllegalArgumentException();
        }

        Point[] pointsClone = points.clone();
        Quick.sort(pointsClone);

        for (int i = 0; i < pointsClone.length - 1; i++) {
            if (pointsClone[i].compareTo(pointsClone[i + 1]) == 0)
                throw new IllegalArgumentException();
        }

        List<LineSegment> segmentArrayList = new ArrayList<>();

        for (int i = 0; i < pointsClone.length - 1; i++) {
            Point p = pointsClone[i];
            ArrayList<Point> tmpPoints = new ArrayList<>();

            for (int j = i + 1; j < pointsClone.length; j++) {
                tmpPoints.add(pointsClone[j]);
            }

            Point[] sortedPoints = tmpPoints.toArray(new Point[tmpPoints.size()]);

            MergeX.sort(sortedPoints, p.slopeOrder());

            for (int k = 0; k < sortedPoints.length - 1; k++) {
                double m1 = p.slopeTo(sortedPoints[k]);
                Point endPoint = null;
                int counter = 1;

                for (int l = k + 1; l < sortedPoints.length; l++) {
                    double m2 = p.slopeTo(sortedPoints[l]);

                    if (m1 == m2 || Math.abs(m1 - m2) < 0.000001) {
                        counter++;
                        if (counter >= 3) {
                            endPoint = sortedPoints[l];
                            k = l;
                        }
                    }
                    else {
                        break;
                    }
                }

                if (endPoint != null) {
                    segmentArrayList.add(new LineSegment(p, endPoint));
                }
            }
        }

        lineSegments = segmentArrayList.toArray(new LineSegment[segmentArrayList.size()]);
    }

    // the number of line segments
    public int numberOfSegments() {
        return lineSegments.length;
    }

    // the line segments
    public LineSegment[] segments() {
        return lineSegments.clone();
    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
