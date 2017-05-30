package GraphAlgorithms;
public class ex_knapsack2{
	
public ex_knapsack2(){
}
public static void main() { 
	
	
	int n = 10; //Items
    int m = 2; //Kooleh poshti
    int depth = -1; 
    int profit2[] = {0, 46, 50, 45, 58, 46, 50, 45, 58, 45, 58}; 
    int weight2[] = {0, 81, 83, 43, 34, 81, 83, 43, 34, 45, 58}; 
    int capacity[] = {0, 125, 146}; //Knapsack capacity
    int sol[] = new int[n + 1]; 
 
    knapsack.multipleKnapsack(n, m, profit2, weight2, capacity, depth, sol); 
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
