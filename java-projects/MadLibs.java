import java.util.Scanner;

public class MadLibs {
    public static void main(String[] args) {
        // Step 3: Create one Scanner object
        Scanner input = new Scanner(System.in);

        // Step 2 & 3: Prompt for and store inputs
        System.out.print("Enter a name: ");
        String name = input.nextLine();

        System.out.print("Enter a whole number: ");
        int count = input.nextInt();
        // Consume the leftover newline character left by nextInt()
        input.nextLine(); 

        System.out.print("Enter an adjective: ");
        String adjective = input.nextLine();

        System.out.print("Enter a place: ");
        String location = input.nextLine();

        // Step 4: Print using System.out.printf with format specifiers
        // %s for Strings, %d for decimal integers, %n for a line break
        System.out.printf("Yesterday, %s ate %d %s apples at %s.%n", 
                          name, count, adjective, location);

        // Step 4: Close the Scanner
        input.close();
    }
}