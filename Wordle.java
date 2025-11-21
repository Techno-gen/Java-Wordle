import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.File;

public class Wordle {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        File fileread = new File("words.txt");
        boolean contchar;
        int count;
        // Load words file into array
        String[] words = new String[3103];
        int i = 0;
        try (Scanner myReader = new Scanner(fileread)) {
            while (myReader.hasNextLine()) {
                System.out.println("Loading words...");
                words[i] = myReader.nextLine();
                i++;
            }
        } catch (FileNotFoundException e) {
            System.out.println("file could not be read.");
        }
        String targetWord = words[(int)(Math.random() * words.length)];

        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.println("\nWordle");
        System.out.println("'=' when equal and is in the right place, '^' when it exists in the word but isn't in the right place, '*' when it doesn't exist.");
        for (i = 0; i < 6; i++) {
            System.out.println();
            String guess = sc.nextLine();

            for (int j = 0; j < 5; j++) {
                count = 0;
                contchar = false;
                for (int k = 0; k < 5; k++) {
                    if (!containstwo(guess, guess.charAt(j)) && guess.charAt(j) == targetWord.charAt(k)) {
                        contchar = true;
                        break;
                    }
                    else if (containstwo(targetWord, guess.charAt(j))) { // If it contains two it takes double the characters for a letter to register, solving the problem of double detection.
                        count++;                                         // For example: you have a word "heaps" and the user inputs "seems". The previous code would register both Es as if there are two in the target word, even though there's only one.
                        if (count > 1) {
                            contchar = true;
                            break;
                        }
                    }
                }

                if (guess.equals(targetWord)) {
                    System.out.println("You won!\n");
                    return;
                }
                else if (guess.charAt(j) == targetWord.charAt(j)) {
                    System.out.print("=");
                }
                else if (contchar) {
                    System.out.print("^");
                }
                else {
                    System.out.print("*");
                }
            }
        }
        System.out.println("\nYou lost!");
        System.out.println("Your word was: " + targetWord + "\n");
    }

    // Method to check for multiple occurances of letters
    public static boolean containstwo(String word, Character character) {
        int count = 0;
        for (int i = 0; i < word.length(); i++) {
            for (int k = 0; k < word.length(); k++) {
                if (word.charAt(i) == character && word.charAt(i) == word.charAt(k)) count++;
            }
        }
        return count > 1;
    }
}