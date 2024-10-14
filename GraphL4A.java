
import java.util.Scanner;

public class GraphL4A {

	private int n;
	private int type; 
	private int weighted; 
	private WeightedNode4A[] adjlistW; 
	private Node4A[] adjlist;

	public GraphL4A(Scanner sc) {
		String[] firstline = sc.nextLine().split(" ");
		this.n = Integer.parseInt(firstline[0]);
		System.out.println(this.n);
		if (firstline[1].equals("undirected"))
			this.type = 0;
		else
			this.type = 1;
		System.out.println("Type= " + this.type);
		if (firstline[2].equals("unweighted"))
			this.weighted = 0;
		else
			this.weighted = 1;
		System.out.println("Weighted= " + this.weighted);
		if (this.weighted == 0) {
			this.adjlist = new Node4A[this.n];
			for (int i = 0; i < this.n; i++)
				adjlist[i] = null;
			adjlistW = null;
		} else {
			this.adjlistW = new WeightedNode4A[this.n];
			for (int i = 0; i < this.n; i++)
				adjlistW[i] = null;
			adjlist = null;
		}

		for (int k = 0; k < this.n; k++) {
			String[] line = sc.nextLine().split(" : ");
			int i = Integer.parseInt(line[0]); 
			if (weighted == 0) {
				if ((line.length > 1) && (line[1].charAt(0) != ' ')) {
					String[] successors = line[1].split(", ");
					for (int h = 0; h < successors.length; h++) {
						Node4A node = new Node4A(Integer.parseInt(successors[h]) - 1, null);
						node.setNext(adjlist[i - 1]);
						adjlist[i - 1] = node;
					}
				}
			} else {
				line = line[1].split(" // ");
				if ((line.length == 2) && (line[1].charAt(0) != ' ')) {
																		
																		
					String[] successors = line[0].split(", ");
					String[] theirweights = line[1].split(", ");
					for (int h = 0; h < successors.length; h++) {
						WeightedNode4A nodeW = new WeightedNode4A(Integer.parseInt(successors[h]) - 1, null,
								Float.parseFloat(theirweights[h]));
						nodeW.setNext(adjlistW[i - 1]);
						adjlistW[i - 1] = nodeW;
					}

				}
			}
		}

	}

	
	public int[] degree() {
		int[] tmp = new int[this.n];
		for (int i = 0; i < this.n; i++)
			tmp[i] = 0;
		for (int i = 0; i < this.n; i++) {
			Node4A p = adjlist[i];
			while (p != null) {
				tmp[i] = tmp[i] + 1;
				p = p.getNext();
			}
		}
		return (tmp);
	}

	
	public int[] degreeW() {
		int[] tmp = new int[this.n];
		for (int i = 0; i < this.n; i++)
			tmp[i] = 0;
		for (int i = 0; i < this.n; i++) {
			WeightedNode4A p = adjlistW[i];
			while (p != null) {
				tmp[i] = tmp[i] + 1;
				p = p.getNext();
			}
		}
		return (tmp);
	}

	
	public TwoArrays4A degrees() {
		int[] tmp1 = new int[this.n]; 
		int[] tmp2 = new int[this.n]; 
		for (int i = 0; i < this.n; i++) {
			tmp1[i] = 0;
			tmp2[i] = 0;
		}
		for (int i = 0; i < this.n; i++) {
			Node4A p = adjlist[i];
			while (p != null) {
				tmp2[i] = tmp2[i] + 1;
				tmp1[p.getVal()] = tmp1[p.getVal()] + 1;
				p = p.getNext();
			}
		}
		return (new TwoArrays4A(tmp1, tmp2));
	}

	
	public TwoArrays4A degreesW() {
		int[] tmp1 = new int[this.n]; 
		int[] tmp2 = new int[this.n]; 
		for (int i = 0; i < this.n; i++) {
			tmp1[i] = 0;
			tmp2[i] = 0;
		}
		for (int i = 0; i < this.n; i++) {
			WeightedNode4A p = adjlistW[i];
			while (p != null) {
				tmp2[i] = tmp2[i] + 1;
				tmp1[p.getVal()] = tmp1[p.getVal()] + 1;
				p = p.getNext();
			}
		}
		return (new TwoArrays4A(tmp1, tmp2));
	}

	public int getType() {
		return this.type;
	}

	public int getWeighted() {
		return this.weighted;
	}

	public Node4A[] getAdjList() {
		return this.adjlist;
	}

	public WeightedNode4A[] getAdjListW() {
		return this.adjlistW;
	}

	public Node4A[] transposeAdjList(Node4A[] adjlist) {
		int n = adjlist.length;
		Node4A[] transposed = new Node4A[n];

		for (int i = 0; i < n; i++) {
			Node4A current = adjlist[i];
			while (current != null) {
				int neighbor = current.getVal();
			
				Node4A node = new Node4A(i, transposed[neighbor]);
				transposed[neighbor] = node;
				current = current.getNext();
			}
		}
		return transposed;
	}

	public float[][] adjListToAdjMatrix(Node4A[] adjlist) {
		if (adjlist == null) {
			throw new IllegalArgumentException("The adjacency list is null.");
		}

		int n = adjlist.length;
		float[][] adjmat = new float[n][n];

		for (int i = 0; i < n; i++) {
			Node4A current = adjlist[i];
			while (current != null) {
				adjmat[i][current.getVal()] = 1; 
				current = current.getNext();
			}
		}
		return adjmat;
	}

	
	public float[][] adjListWToAdjMatrix(WeightedNode4A[] adjlistW) {
		if (adjlistW == null) {
			throw new IllegalArgumentException("The weighted adjacency list is null.");
		}

		int n = adjlistW.length;
		float[][] adjmat = new float[n][n];

		for (int i = 0; i < n; i++) {
			WeightedNode4A current = adjlistW[i];
			while (current != null) {
				adjmat[i][current.getVal()] = current.getWeight(); 
				current = current.getNext();
			}
		}
		return adjmat;
	}

	public WeightedNode4A[] transposeAdjListW(WeightedNode4A[] adjlistW) {
		if (adjlistW == null) {
			throw new IllegalArgumentException("The weighted adjacency list is null.");
		}
	
		int n = adjlistW.length;
		WeightedNode4A[] transposed = new WeightedNode4A[n];
	
		for (int i = 0; i < n; i++) {
			WeightedNode4A current = adjlistW[i];
			while (current != null) {
				int neighbor = current.getVal();
				float weight = current.getWeight(); 
				
				WeightedNode4A nodeW = new WeightedNode4A(i, transposed[neighbor], weight);
				transposed[neighbor] = nodeW;
				current = current.getNext();
			}
		}
		return transposed;
	}
	

}
