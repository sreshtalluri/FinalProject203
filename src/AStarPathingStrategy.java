import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class AStarPathingStrategy
        implements PathingStrategy
{


    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors)
    {
        List<Point> path = new LinkedList<Point>();
        PriorityQueue<Node> queue = new PriorityQueue<>();
        HashSet<Integer> seen = new HashSet<>();
        Node noderMon = new Node(0, calcDist(start, end), start, null);

        queue.add(noderMon);
        seen.add(noderMon.hashCode());

        while (queue.size() > 0) {
            Node node = queue.poll();
            if (withinReach.test(node.getPosition(), end)) {
                while (node.getPrevNode() != null) {
                    path.add(0, node.getPosition());
                    node = node.getPrevNode();
                }
                return path;
            }
            for (Point p : potentialNeighbors.apply(node.getPosition())
                    .filter(canPassThrough).collect(Collectors.toList())) {
                Node temp = new Node(calcDist(p, start), calcDist(p, end), p, node);
                if (!seen.contains(temp.hashCode())) {
                    queue.add(temp);
                    seen.add(temp.hashCode());
                }
            }
        }

        return path;
    }

    private double calcDist(Point p1, Point p2){
        double a = p2.x - p1.x;
        double b = p2.y - p1.y;
        double c = (a * a) + (b * b);
        return Math.sqrt(c);
    }
}
