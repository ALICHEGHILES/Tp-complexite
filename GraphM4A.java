
import java.util.Scanner;

public class GraphM4A {

	private int n;
	private int type; 
	private int weighted; 
	private float[][] adjmat;

	public GraphM4A(Scanner sc) {
		String[] firstline = sc.nextLine().split(" ");
		this.n = Integer.parseInt(firstline[0]);
		System.out.println("Number of vertices " + this.n);
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

		this.adjmat = new float[this.n][this.n];
		for (int i = 0; i < this.n; i++)
			for (int j = 0; j < this.n; j++)
				adjmat[i][j] = 0; 

		for (int k = 0; k < this.n; k++) {
			String[] line = sc.nextLine().split(" : ");
			int i = Integer.parseInt(line[0]); 
			if (weighted == 0) {
				if ((line.length > 1) && (line[1].charAt(0) != ' ')) {
					String[] successors = line[1].split(", ");
					for (int h = 0; h < successors.length; h++) {
						this.adjmat[i - 1][Integer.parseInt(successors[h]) - 1] = 1;
					}
				}
			} else {
				line = line[1].split(" // ");
				if ((line.length == 2) && (line[1].charAt(0) != ' ')) {
																		
																		
					String[] successors = line[0].split(", ");
					String[] theirweights = line[1].split(", ");
					for (int h = 0; h < successors.length; h++)
						this.adjmat[i - 1][Integer.parseInt(successors[h]) - 1] = Float.parseFloat(theirweights[h]);
				}
			}
		}
	}

	
	public int[] degree() {
		int[] tmp = new int[this.n];
		for (int i = 0; i < this.n; i++)
			tmp[i] = 0;
		for (int i = 0; i < this.n; i++)
			for (int j = 0; j < this.n; j++)
				if (this.adjmat[i][j] != 0)
					tmp[i] = tmp[i] + 1;
		return tmp;

	}

	
	public TwoArrays4A degrees() {
		int[] tmp1 = new int[this.n]; 
		int[] tmp2 = new int[this.n]; 
		for (int i = 0; i < this.n; i++) {
			tmp1[i] = 0;
			tmp2[i] = 0;
		}
		for (int i = 0; i < this.n; i++)
			for (int j = 0; j < this.n; j++)
				if (this.adjmat[i][j] != 0) {
					tmp2[i] = tmp2[i] + 1;
					tmp1[j] = tmp1[j] + 1;
				}
		return (new TwoArrays4A(tmp1, tmp2));

	}

	public int getType() {
		return this.type;
	}

	public float[][] getAdjMatrix() {
		return this.adjmat;
	}

	public float[][] transposeAdjMatrix(float[][] adjmat) {
		int n = adjmat.length;
		float[][] transposed = new float[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				transposed[j][i] = adjmat[i][j];
			}
		}
		return transposed;
	}

	public Node4A[] adjMatrixToAdjList(float[][] adjmat) {
		int n = adjmat.length;
		Node4A[] adjlist = new Node4A[n];

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (adjmat[i][j] != 0) {
					Node4A node = new Node4A(j, adjlist[i]);
					adjlist[i] = node;
				}
			}
		}
		return adjlist;
	}

}