import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;

public class Node implements Comparable<Node> {

    private double f, g, h;
    private Point position;
    private Node prev;

    public Node(double g, double h, Point position, Node prev){
        this.g = g;
        this.h = h;
        this.f = g + h;
        this.position = position;
        this.prev = prev;
    }

    public double getF(){
        return f;
    }

    public Point getPosition(){
        return position;
    }

    public Node getPrevNode() {
        return prev;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        Node node = (Node) o;
        return f == node.f && g == node.g && h == node.h;
    }

    @Override
    public int compareTo(Node o) {
        if (o.getF() == this.getF()) {
            return 0;
        }
        else if (o.getF() > this.getF()){
            return -1;
        }
        else {
            return 1;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(f, g, h, position.x, position.y);
    }

}
