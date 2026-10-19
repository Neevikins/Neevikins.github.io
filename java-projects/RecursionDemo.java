import java.util.Scanner;

public class RecursionDemo {

    /**
     * Recursively calculates the sum of all integers from 0 up to n.
     * 
     * @param n an integer >= 0
     * @return sum of integers from 0 to n
     */
    public static int sumTo(int n) {
        // Base case: stopping condition
        if (n <= 0) {
            return 0;
        }
        // Recursive step: progress toward base case
        return n + sumTo(n - 1);
    }

    public static void main(String[] args) {
        // Testing required cases
        System.out.println("sumTo(0) -> " + sumTo(0)); // Expected: 0
        System.out.println("sumTo(1) -> " + sumTo(1)); // Expected: 1
        System.out.println("sumTo(4) -> " + sumTo(4)); // Expected: 10
    }
}