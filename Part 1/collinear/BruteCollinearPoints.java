import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {

    private LineSegment[] lineSegments;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException();


        for (Point p : points) {
            if (p == null) {
                throw new IllegalArgumentException();
            }
        }

        Point[] pointsClone = points.clone();
        Arrays.sort(pointsClone);

        for (int i = 0; i < pointsClone.length - 1; i++) {
            if (pointsClone[i].compareTo(pointsClone[i + 1]) == 0) {
                throw new IllegalArgumentException();
            }
        }

        ArrayList<LineSegment> lineSegmentArrayList = new ArrayList<>();

        for (int i = 0; i < points.length; i++) {
            for (int j = 1; j < points.length; j++) {
                for (int k = 2; k < points.length; k++) {
                    for (int l = 3; l < points.length; l++) {
                        if (i < j && j < k && k < l) {
                            Point p1 = pointsClone[i];
                            Point p2 = pointsClone[j];
                            Point p3 = pointsClone[k];
                            Point p4 = pointsClone[l];

                            double m1 = p1.slopeTo(p2);
                            if (p1.slopeTo(p3) == m1 && p1.slopeTo(p4) == m1)
                                lineSegmentArrayList.add(new LineSegment(p1, p4));
                        }
                    }
                }
            }
        }

        lineSegments = lineSegmentArrayList.toArray(new LineSegment[lineSegmentArrayList.size()]);
    }

    // the number of line segments
    public int numberOfSegments() {
        return lineSegments.length;
    }

    // // the line segments
    public LineSegment[] segments() {
        return lineSegments.clone();
    }

    public static void main(String[] args) {

    }
}

