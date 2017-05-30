package GraphAlgorithms;

public class AssignmentProblem_ex1 extends Object{
	
	public static void main(String args[]) { 
	    int n = 5; 
	    int cost[][] = {{0,  0,  0,  0,  0,  0,  0}, 
	                    {0,  9,  6,  8,  4,  7,  0}, 
	                    {0,  8,  7,  6,  8,  3,  0},  
	                    {0,  7,  4,  3,  5,  6,  0}, 
	                    {0,  9,  8,  3,  7,  6,  0}, 
	                    {0,  8,  7,  3,  6,  5,  0}}; 
	    int sol[] = new int[n + 1]; 
	 
	    AssignmentProblem.assignment(n, cost, sol); 
	    System.out.println("Optimal assignment:\n" + "  column  row"); 
	    for (int i=1; i<=n; i++) 
	      System.out.println("     " + i + "  -  " + sol[i]); 
	    System.out.println("\nTotal assignment cost = " + cost[0][0]); 
	  }
}