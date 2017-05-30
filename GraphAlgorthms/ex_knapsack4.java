package GraphAlgorithms;

public class ex_knapsack4 extends Object{
	
public ex_knapsack4(){
}
public static void main(String args[]) { 
	   
	int n = 4; //Items
    int m = 1; //Kooleh poshti
    int depth = -1; 
    int profit[] = {0, 70,75,70,75}; 
    int weight[] = {0, 120,150,80,150}; 
    int capacity[] = {0, 440}; //Knapsack capacity
    int sol[] = new int[n + 1]; 
 
    knapsack.multipleKnapsack(n, m, profit, weight, capacity, depth, sol); 
    if (sol[0] > 0) { 
      System.out.println("Optimal solution found:"); 
      for (int i=1; i<=n; i++) 
        System.out.print("  " + sol[i]); 
      System.out.println("\n\nTotal profit = " + sol[0]); 
    } 
    else 
      System.out.println("Error returned = " + sol[0]); 
  }
}
