import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;

public class SAP {
    private final Digraph digraph;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null)
            throw new IllegalArgumentException();

        digraph = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (!isValid(v) || !isValid(w))
            throw new IllegalArgumentException();

        int ancestor = ancestor(v, w);
        if (ancestor == -1)
            return -1;

        BreadthFirstDirectedPaths bfs_v = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths bfs_w = new BreadthFirstDirectedPaths(digraph, w);

        return bfs_v.distTo(ancestor) + bfs_w.distTo(ancestor);
    }

    // a common ancestor of v and w that participates in a shortest ancestral path;
    // -1 if no such path
    public int ancestor(int v, int w) {
        if (!isValid(v) || !isValid(w))
            throw new IllegalArgumentException();

        BreadthFirstDirectedPaths bfs_v = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths bfs_w = new BreadthFirstDirectedPaths(digraph, w);

        if (v == w)
            return v;

        int distance = 2 * digraph.V();
        int ancestor = -1;
        for (int vertex = 0; vertex < digraph.V(); vertex++) {
            if (bfs_v.hasPathTo(vertex) && bfs_w.hasPathTo(vertex)) {
                int checkedDistance = bfs_v.distTo(vertex) + bfs_w.distTo(vertex);
                if (checkedDistance < distance) {
                    distance = checkedDistance;
                    ancestor = vertex;
                }
            }
        }

        return ancestor;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in
    // w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null)
            throw new IllegalArgumentException();

        for (Integer id : v) {
            if (id == null)
                throw new IllegalArgumentException();
            if (!isValid(id))
                throw new IllegalArgumentException();
        }

        for (Integer id : w) {
            if (id == null)
                throw new IllegalArgumentException();
            if (!isValid(id))
                throw new IllegalArgumentException();
        }

        int ancestor = ancestor(v, w);
        if (ancestor == -1)
            return -1;

        BreadthFirstDirectedPaths bfs_v = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths bfs_w = new BreadthFirstDirectedPaths(digraph, w);

        return bfs_v.distTo(ancestor) + bfs_w.distTo(ancestor);
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such
    // path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null)
            throw new IllegalArgumentException();

        for (Integer id : v) {
            if (id == null)
                throw new IllegalArgumentException();
            if (!isValid(id))
                throw new IllegalArgumentException();
        }

        for (Integer id : w) {
            if (id == null)
                throw new IllegalArgumentException();
            if (!isValid(id))
                throw new IllegalArgumentException();
        }

        BreadthFirstDirectedPaths bfs_v = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths bfs_w = new BreadthFirstDirectedPaths(digraph, w);

        int distance = 2 * digraph.V();
        int ancestor = -1;

        for (int vertex = 0; vertex < digraph.V(); vertex++) {
            if (bfs_v.hasPathTo(vertex) && bfs_w.hasPathTo(vertex)) {
                int checkedDistance = bfs_v.distTo(vertex) + bfs_w.distTo(vertex);
                if (checkedDistance < distance) {
                    distance = checkedDistance;
                    ancestor = vertex;
                }
            }
        }

        return ancestor;
    }

    private boolean isValid(int v) {
        return v >= 0 && v <= (digraph.V() - 1);
    }

    private boolean isValid(Integer v) {
        return v >= 0 && v <= (digraph.V() - 1);
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);

        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}