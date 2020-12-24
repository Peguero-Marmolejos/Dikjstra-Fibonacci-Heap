import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import org.graphstream.algorithm.util.FibonacciHeap;

public class Dijkstra_heap {

	private int count;
    private ArrayList<ArrayList<Edge>> graph;
    private int edgeTo[];
    private double distTo[];
    private boolean inQ[];
    private FibonacciHeap.Node nodeRef[];
    private int V;
    FibonacciHeap<Double, Integer> pq;

    public Dijkstra_heap(int V) {
        graph = new ArrayList<>();
        graph.ensureCapacity(V);
        for(int i = 0; i < V; ++i)
            graph.add(new ArrayList<>());
        edgeTo = new int[V];
        distTo = new double[V];
        this.V = V;
        this.count = 0;
    }
    
    public void addEdge(int from, int to, double weight) {
        graph.get(from).add(new Edge(from, to, weight));
    }
    
    public void runDijkstra(int s) {
        pq = new FibonacciHeap<>();
        inQ = new boolean[V];
        nodeRef = new FibonacciHeap.Node[V];

        Arrays.fill(distTo, Double.POSITIVE_INFINITY);
        distTo[s] = 0;
        
        nodeRef[s] = pq.add(0.0, s);
        inQ[s] = true;
        
        while(!pq.isEmpty()) {
            int v = pq.extractMin();
            for(Edge e : graph.get(v))
                relax(e);
        }
    }
    
    public void relax(Edge e) {
        if(!inQ[e.to]) {
            distTo[e.to] = distTo[e.from] + e.weight;
            nodeRef[e.to] = pq.add(distTo[e.to], e.to);
            inQ[e.to] = true;
            edgeTo[e.to] = e.from;
            this.count++;
        }
        else if(distTo[e.to] > distTo[e.from] + e.weight) {
            distTo[e.to] = distTo[e.from] + e.weight;
            edgeTo[e.to] = e.from;
            pq.decreaseKey(nodeRef[e.to], distTo[e.to]);
            this.count++;
        }
    }
    
    public static void main(String[] args) {
        // TODO code application logic here
        long before = System.currentTimeMillis();
        Dijkstra_heap d = new Dijkstra_heap(10000);
        ArrayList<String> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("data1(10000).txt"))) {
            String sCurrentLine;

            while ((sCurrentLine = br.readLine()) != null) {
                data.add(sCurrentLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(int i = 0; i < data.size(); i++) {
            String temp = data.get(i);
            int to = Integer.parseInt(temp.split(",")[1]);
            int from = Integer.parseInt(temp.split(",")[0]);
            double weight = Double.parseDouble(temp.split(",")[2]);
            d.addEdge(from, to, weight);
        }
 
        d.runDijkstra(0);
        
        System.out.println("Results: ");
        for(int i = 0; i < d.V; ++i) {
            System.out.println("Vertex " + i + " Distance : "+d.distTo[i]);
        }
        
        // get current time
        long time = System.currentTimeMillis() - before;
        System.out.println("Actual clock time : " + String.valueOf(time));
        System.out.println(d.count);
    }

}
