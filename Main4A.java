import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main4A {

  private static List<List<Integer>> adjList;
  private static boolean[] visited;
  private static int[] discoveryTime, finishTime, parent;
  private static int time = 0;
  private static List<EdgeType> edgeTypes = new ArrayList<>();
  private static boolean hasCycle = false;
  private static List<Integer> cycleVertices = new ArrayList<>();

  public static void main(String args[]){

    try {
      File file = new File(args[0]);

      
      Scanner input = new Scanner(System.in);
      System.out.println("Choose operation: ");
      System.out.println("1 - Matrix to Transposed Matrix");
      System.out.println("2 - List to Transposed List");
      System.out.println("3 - Matrix to List");
      System.out.println("4 - List to Matrix");
      System.out.println("5 - Test if a sequence of vertices is a valid path ");  
      System.out.println("6 - DFS and find cycle in graph");
      int choice = input.nextInt();

      
      Scanner sc = new Scanner(file);

      
      if (choice == 1 || choice == 3) {
        GraphM4A graphM = new GraphM4A(sc);

        if (choice == 1) {
         
          float[][] transposedMatrix = graphM.transposeAdjMatrix(graphM.getAdjMatrix());
          System.out.println("Transposed adjacency matrix:");
          printMatrix(transposedMatrix);
        } else if (choice == 3) {
         
          Node4A[] adjList = graphM.adjMatrixToAdjList(graphM.getAdjMatrix());
          System.out.println("Adjacency list derived from matrix:");
          printAdjList(adjList);
        }
      }

      
      if (choice == 2 || choice == 4) {
        sc = new Scanner(file);  
        GraphL4A graphL = new GraphL4A(sc);

        if (choice == 2) {
         
          if (graphL.getWeighted() == 1) {
             
              WeightedNode4A[] transposedListW = graphL.transposeAdjListW(graphL.getAdjListW());
              System.out.println("Transposed weighted adjacency list:");
              printWeightedAdjList(transposedListW);
          } else {
              
              Node4A[] transposedList = graphL.transposeAdjList(graphL.getAdjList());
              System.out.println("Transposed adjacency list:");
              printAdjList(transposedList);
          }
      
        } else if (choice == 4) {
          
          if (graphL.getWeighted() == 1) {
              
              if (graphL.getAdjListW() != null) {
                  float[][] adjMatrix = graphL.adjListWToAdjMatrix(graphL.getAdjListW());
                  System.out.println("Adjacency matrix derived from weighted list:");
                  printMatrix(adjMatrix);
              } else {
                  System.out.println("Error: Weighted adjacency list is null.");
              }
          } else {
              
              if (graphL.getAdjList() != null) {
                  float[][] adjMatrix = graphL.adjListToAdjMatrix(graphL.getAdjList());
                  System.out.println("Adjacency matrix derived from list:");
                  printMatrix(adjMatrix);
              } else {
                  System.out.println("Error: Unweighted adjacency list is null.");
              }
          }
        }
      }

     
      if (choice == 5) {
        System.out.println("Choose representation: ");
        System.out.println("1 - Adjacency list");
        System.out.println("2 - Adjacency matrix");
        int representation = input.nextInt();

        System.out.println("Enter the sequence of vertices (e.g., 1 2 3):");
        input.nextLine();  
        String line = input.nextLine();
        String[] vertices = line.split(" ");
        int[] path = new int[vertices.length];
        for (int i = 0; i < vertices.length; i++) {
            path[i] = Integer.parseInt(vertices[i]);
        }

        if (representation == 1) {
         
          GraphL4A graphL = new GraphL4A(sc);
          
          if (graphL.getWeighted() == 1) {
              
              if (isPathInAdjListW(graphL.getAdjListW(), path)) {
                  System.out.println("The path is valid.");
              } else {
                  System.out.println("The path is not valid.");
              }
          } else {
              
              if (isPathInAdjList(graphL.getAdjList(), path)) {
                  System.out.println("The path is valid.");
              } else {
                  System.out.println("The path is not valid.");
              }
          }
      
      
         } else if (representation == 2) {
          
          GraphM4A graphM = new GraphM4A(sc);
          if (isPathInAdjMatrix(graphM.getAdjMatrix(), path)) {
              System.out.println("The path is valid.");
          } else {
              System.out.println("The path is not valid.");
          }
        }
      }

      
      if (choice == 6) {
        System.out.println("Choose representation: ");
        System.out.println("1 - Adjacency list");
        System.out.println("2 - Adjacency matrix");
        int representation = input.nextInt();

        sc = new Scanner(file);  
        String[] firstLine = sc.nextLine().split(" ");
        int n = Integer.parseInt(firstLine[0]);
        boolean isDirected = firstLine[1].equals("directed");

        adjList = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adjList.add(new ArrayList<>());
        }
        visited = new boolean[n];
        discoveryTime = new int[n];
        finishTime = new int[n];
        parent = new int[n];
        Arrays.fill(parent, -1);

      
       while (sc.hasNextLine()) {
        String line = sc.nextLine().trim();  
        if (line.isEmpty()) {
            continue; 
        }
    
        String[] parts = line.split(" : ");
        if (parts.length < 2 || parts[0].isEmpty()) {
            System.out.println("Warning: Invalid line format or missing vertex. Skipping line: " + line);
            continue;  
        }
    
        int u = Integer.parseInt(parts[0].trim()) - 1;  
    
        
        if (parts.length > 1 && !parts[1].trim().isEmpty()) {
            if (firstLine[2].equals("weighted")) {  
                String[] weightedParts = parts[1].split(" // ");
                String[] neighbors = weightedParts[0].split(", ");
                String[] weights = weightedParts[1].split(", ");
                
                
                if (neighbors.length != weights.length) {
                    System.out.println("Warning: Mismatch between neighbors and weights at vertex " + (u + 1));
                    continue;
                }
                
                for (int i = 0; i < neighbors.length; i++) {
                    int v = Integer.parseInt(neighbors[i].trim()) - 1;
                    float weight = Float.parseFloat(weights[i].trim());
                    adjList.get(u).add(v);
                    if (!isDirected) {
                        adjList.get(v).add(u);  
                    }
                }
            } else {  
                String[] neighbors = parts[1].split(", ");
                for (String neighbor : neighbors) {
                    if (!neighbor.trim().isEmpty()) {
                        int v = Integer.parseInt(neighbor.trim()) - 1;
                        adjList.get(u).add(v);
                        if (!isDirected) {
                            adjList.get(v).add(u);  
                        }
                    }
                }
            }
        }
    }
    
    
        
        System.out.print("Enter the starting vertex for DFS: ");
        int startVertex = input.nextInt() - 1;

        boolean hasCycle = containsCycle(isDirected, startVertex);
        printEdgeTypes();
        printCycle();
      }

      sc.close();
      input.close();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  
  public static void dfsUndirected(int u, int parent) {
      visited[u] = true;
      discoveryTime[u] = ++time;

      for (int v : adjList.get(u)) {
          if (!visited[v]) {
              edgeTypes.add(new EdgeType(u, v, "Tree Edge"));
              Main4A.parent[v] = u;
              dfsUndirected(v, u);
          } else if (v != parent && !hasCycle) {
              edgeTypes.add(new EdgeType(u, v, "Back Edge"));
              hasCycle = true; 
              cycleVertices.add(v);
              for (int cur = u; cur != v; cur = Main4A.parent[cur]) {
                  cycleVertices.add(cur);
              }
              cycleVertices.add(v);  
          }
      }
      finishTime[u] = ++time;
  }

  
  public static void dfsDirected(int u, boolean[] recStack) {
      visited[u] = true;
      recStack[u] = true;  
      discoveryTime[u] = ++time;

      for (int v : adjList.get(u)) {
          if (!visited[v]) {
              edgeTypes.add(new EdgeType(u, v, "Tree Edge"));
              Main4A.parent[v] = u;
              dfsDirected(v, recStack);
          } else if (recStack[v] && !hasCycle) {
              edgeTypes.add(new EdgeType(u, v, "Back Edge"));
              hasCycle = true; 
              cycleVertices.add(v);
              for (int cur = u; cur != v; cur = Main4A.parent[cur]) {
                  cycleVertices.add(cur);
              }
              cycleVertices.add(v);  
          }
      }
      recStack[u] = false; 
      finishTime[u] = ++time;
  }

  
  public static boolean containsCycle(boolean isDirected, int startVertex) {
      if (isDirected) {
          boolean[] recStack = new boolean[adjList.size()];
          dfsDirected(startVertex, recStack);
      } else {
          dfsUndirected(startVertex, -1);  
      }
      return hasCycle;
  }

  public static void printCycle() {
      if (hasCycle) {
          System.out.println("The graph contains a cycle with the vertices :");
          for (int vertex : cycleVertices) {
              System.out.print((vertex + 1) + " ");
          }
          System.out.println();
      } else {
          System.out.println("The graph does not contain a cycle.");
      }
  }

  public static void printEdgeTypes() {
      for (EdgeType edge : edgeTypes) {
          System.out.println(edge);
      }
  }

  
  public static boolean isPathInAdjMatrix(float[][] adjMatrix, int[] path) {
    int n = path.length;

    
    for (int i = 0; i < n - 1; i++) {
        int v1 = path[i] - 1;  
        int v2 = path[i + 1] - 1;

        
        if (adjMatrix[v1][v2] == 0) {
            return false;
        }
    }
    return true;
  }
  public static boolean isPathInAdjListW(WeightedNode4A[] adjListW, int[] path) {
    int n = path.length;

    
    for (int i = 0; i < n - 1; i++) {
        int v1 = path[i] - 1;  
        int v2 = path[i + 1] - 1;

        
        WeightedNode4A current = adjListW[v1];
        boolean found = false;
        while (current != null) {
            if (current.getVal() == v2) {
                found = true;
                break;
            }
            current = current.getNext();
        }

        
        if (!found) {
            return false;
        }
    }
    return true;  
  }

  
  public static boolean isPathInAdjList(Node4A[] adjList, int[] path) {
    int n = path.length;

   
    for (int i = 0; i < n - 1; i++) {
        int v1 = path[i] - 1;  
        int v2 = path[i + 1] - 1;

        
        Node4A current = adjList[v1];
        boolean found = false;
        while (current != null) {
            if (current.getVal() == v2) {
                found = true;
                break;
            }
            current = current.getNext();
        }

        
        if (!found) {
            return false;
        }
    }
    return true;
  }

 
  public static void printMatrix(float[][] matrix) {
    for (int i = 0; i < matrix.length; i++) {
      for (int j = 0; j < matrix[i].length; j++) {
        System.out.print(matrix[i][j] + " ");
      }
      System.out.println();
    }
  }

  
  public static void printWeightedAdjList(WeightedNode4A[] adjListW) {
    for (int i = 0; i < adjListW.length; i++) {
        System.out.print((i + 1) + ": ");
        WeightedNode4A current = adjListW[i];
        while (current != null) {
            System.out.print("(" + (current.getVal() + 1) + ", weight: " + current.getWeight() + ") ");
            current = current.getNext();
        }
        System.out.println();
    }
  }

  
  public static void printAdjList(Node4A[] adjList) {
    for (int i = 0; i < adjList.length; i++) {
      System.out.print((i + 1) + ": ");
      Node4A current = adjList[i];
      while (current != null) {
        System.out.print((current.getVal() + 1) + " ");
        current = current.getNext();
      }
      System.out.println();
    }
  }

  
  static class EdgeType {
      int u, v;
      String type;

      public EdgeType(int u, int v, String type) {
          this.u = u;
          this.v = v;
          this.type = type;
      }

      @Override
      public String toString() {
          return "Edge (" + (u + 1) + " -> " + (v + 1) + ") : " + type;
      }
  }

}
