package GraphAlgorithms;


public class Quadratic_Assignment_Problem_ex extends Object{
	
	public Quadratic_Assignment_Problem_ex(){
	}
	public static void main(String args[]) { 
		 
	    int n=7; 
	    int a[][] = {{0, 0, 0, 0, 0, 0, 0, 0}, 
	                 {0, 0, 3, 5, 1, 6, 4, 1}, 
	                 {0, 3, 0, 5, 1, 9, 2, 8}, 
	                 {0, 5, 5, 0, 2, 4, 5, 1}, 
	                 {0, 1, 1, 2, 0, 5, 9, 1}, 
	                 {0, 6, 9, 4, 5, 0, 2, 3}, 
	                 {0, 4, 2, 5, 9, 2, 0, 6}, 
	                 {0, 1, 8, 1, 1, 3, 6, 0}}; 

	    int b[][] = {{0, 0, 0, 0, 0, 0, 0, 0}, 
	                 {0, 0, 2, 6, 1, 3, 0, 5}, 
	                 {0, 2, 0, 5, 8, 0, 8, 1}, 
	                 {0, 6, 5, 0, 0, 5, 3, 4}, 
	                 {0, 1, 8, 0, 0, 4, 3, 8}, 
	                 {0, 3, 0, 5, 4, 0, 3, 0}, 
	                 {0, 0, 8, 3, 3, 3, 0, 2}, 
	                 {0, 5, 1, 4, 8, 0, 2, 0}}; 
	 
	    int sol[] = new int[n+1]; 
	 
	    GraphAlgorithms.Quadratic_Assignment_Problem.quadraticAssignment(n, a, b, sol); 
	    System.out.println("Optimal assignment:"); 
	    for (int i=1; i<=n; i++) 
	      System.out.println("  " + i + " -- " + sol[i]); 
	    System.out.println("\nOptimal objective function value = " + sol[0]); 
	  } 
	 
}
