public class SearchDemo {

    public static int linearSearch(int[] data, int target) {
        for (int i = 0; i < data.length; i++) {
            if (data[i] == target) {
                return i;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        // Test 1: {8, 2, 5}, target 2 -> 1
        int[] test1 = {8, 2, 5};
        System.out.println("Test 1: " + linearSearch(test1, 2));

        // Test 2: {4, 9}, target 7 -> -1
        int[] test2 = {4, 9};
        System.out.println("Test 2: " + linearSearch(test2, 7));

        // Test 3: {3, 3, 7}, target 3 -> 0
        int[] test3 = {3, 3, 7};
        System.out.println("Test 3: " + linearSearch(test3, 3));
    }
}