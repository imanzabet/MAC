package GraphAlgorithms;

public class AssignmentProblem_ex2 extends Object{
	
	public static void main(String args[]) { 
	    int n = 5; 
	    int cost[][] = {{0,  0,  0,  0,  0,  0,  0  }, 
	                    {0,  9,  0,  0,  0,  0,  0  }, 
	                    {0,  9,  1,  0,  0,  0,  0  },  
	                    {0,  4,  4,  0,  0,  0,  0  }, 
	                    {0,  1,  9,  0,  0,  0,  0  }, 
	                    {0,  0,  9,  0,  0,  0,  0  }}; 
	    int sol[] = new int[n + 1]; 
	 
	    AssignmentProblem.assignment(n, cost, sol); 
	    System.out.println("Optimal assignment:\n" + "  column  row"); 
	    for (int i=1; i<=n; i++) 
	      System.out.println("     " + i + "  -  " + sol[i]); 
	    System.out.println("\nTotal assignment cost = " + cost[0][0]); 
	  }
}