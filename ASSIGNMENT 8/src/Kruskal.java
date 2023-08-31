/**
 * NAME         :       JEET AGRAWAL
 * NSID         :       jea316
 * STUDENT ID    :       11269096
 */
import lib280.base.CursorPosition280;
import lib280.graph.Edge280;
import lib280.graph.Vertex280;
import lib280.graph.WeightedEdge280;
import lib280.graph.WeightedGraphAdjListRep280;
import lib280.tree.ArrayedMinHeap280;

public class Kruskal {
	
	public static WeightedGraphAdjListRep280<Vertex280> minSpanningTree(WeightedGraphAdjListRep280<Vertex280> G) {

		// TODO -- Complete this method.
		// FOR CREATING minST and setting all its nodes as same as G but without edges.
		WeightedGraphAdjListRep280<Vertex280> minST = new WeightedGraphAdjListRep280<Vertex280>(G.numVertices(),false);

		G.goFirst();
		while (G.itemExists()){
			minST.addVertex(G.item().index());
			G.goForth();
		}

		UnionFind280 UF = new UnionFind280(minST.numVertices());
		ArrayedMinHeap280<WeightedEdge280<Vertex280>> heap = new ArrayedMinHeap280<>(G.numEdges());


		G.goFirst();
		while (G.itemExists()){
			G.eGoFirst(G.item());
			while(G.eItemExists()){
				if (G.eItem().firstItem().index() < G.eItem().secondItem().index()) {
					heap.insert(G.eItem());
				}
				G.eGoForth();
			}
			G.goForth();
		}

		System.out.println();
		while (!heap.isEmpty()){
			WeightedEdge280<Vertex280> item = heap.item();
			heap.deleteItem();
			if (UF.find(item.firstItem().index()) != UF.find(item.secondItem().index())){
				minST.addEdge(item.firstItem(),item.secondItem());
				minST.setEdgeWeight(item.firstItem(),item.secondItem(),G.getEdgeWeight(item.firstItem(),item.secondItem()));
				UF.union(item.firstItem().index(),item.secondItem().index());
			}
		}

		return minST;  // Remove this when you're ready -- it is just a placeholder to prevent a compiler error.
	}
	
	
	public static void main(String args[]) {
		WeightedGraphAdjListRep280<Vertex280> G = new WeightedGraphAdjListRep280<Vertex280>(1, false);
		// If you get a file not found error here and you're using eclipse just remove the 
		// 'Kruskal-template/' part from the path string.
		G.initGraphFromFile("mst.graph");
		System.out.println(G);
		
		WeightedGraphAdjListRep280<Vertex280> minST = minSpanningTree(G);
		
		System.out.println(minST);
	}
}


