import java.util.Random;
import java.util.Scanner;

public class GuessingGame {

    public static void main(String[] args) {
        Random random = new Random();
        Scanner scanner = new Scanner(System.in);
        
        int randomNumber = random.nextInt(100) + 1; // Random number between 1 and 100
        int userGuess = 0;
        
        System.out.println("Welcome to the Guessing Game!");
        System.out.println("I have selected a random number between 1 and 100.");
        System.out.println("Can you guess what it is?");
        
        while (userGuess != randomNumber) {
            System.out.print("Enter your guess: ");
            userGuess = scanner.nextInt();
            
            if (userGuess < randomNumber) {
                System.out.println("Too low! Try again.");
            } else if (userGuess > randomNumber) {
                System.out.println("Too high! Try again.");
            } else {
                System.out.println("Congratulations! You guessed the number.");
            }
        }
        
        scanner.close();
    }
}
