import java.util.Scanner;
import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.io.File;

public class Wordle {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        File fileread = new File("words.txt");
        boolean contchar;
        int count;
        String guess = "seems";

        // Load words file into array
        ArrayList<String> words = new ArrayList<>();
        ArrayList<String> guesses = new ArrayList<>();
        ArrayList<String> confirms = new ArrayList<>();
        int i = 0;
        try (Scanner myReader = new Scanner(fileread)) {
            while (myReader.hasNextLine()) {
                System.out.println("Loading words...");
                words.add(myReader.nextLine());
                i++;
            }
        } catch (FileNotFoundException e) {
            System.out.println("file could not be read.");
            return;
        }
        String targetWord = words.get((int)(Math.random() * words.size()));

        clrscreen();
        System.out.println("\nWordle");
        System.out.println("'=' when equal and is in the right place, '^' when it exists in the word but isn't in the right place, '*' when it doesn't exist.");
        for (i = 0; i < 6; i++) {
            // Only lets the user continue with a valid word from the wordlist.
            // If an invalid word is inputted, the console is cleared and redrawn and the input loop is ran until a valid word is entered.
            while (true) {
                System.out.println();
                guess = sc.nextLine();
                if (words.contains(guess)) {
                    guesses.add(guess);
                    break;
                }
                else {
                    clrscreen();
                    System.out.println("\nWordle");
                    System.out.println("'=' when equal and is in the right place, '^' when it exists in the word but isn't in the right place, '*' when it doesn't exist.");
                    for (int j = 0; j < guesses.size(); j++) {
                        System.out.println("\n" + guesses.get(j));
                        for (int k = j * 5; k < (j * 5) + 5; k++) {
                            System.out.print(confirms.get(k));
                        }
                    }
                }
            }

            for (int j = 0; j < 5; j++) {
                count = 0;
                contchar = false;
                for (int k = 0; k < 5; k++) {
                    if (!containsTwo(guess, guess.charAt(j)) && guess.charAt(j) == targetWord.charAt(k)) {
                        contchar = true;
                        break;
                    }
                    else if (containsTwo(targetWord, guess.charAt(j))) { // If it contains two it takes double the characters for a letter to register, solving the problem of double detection.
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
                    confirms.add("=");
                    System.out.print("=");
                }
                else if (contchar) {
                    confirms.add("^");
                    System.out.print("^");
                }
                else {
                    confirms.add("*");
                    System.out.print("*");
                }
            }
        }
        System.out.println("\nYou lost!");
        System.out.println("Your word was: " + targetWord + "\n");
    }

    // Method to check for multiple occurances of letters
    public static boolean containsTwo(String word, Character character) {
        int count = 0;
        for (int i = 0; i < word.length(); i++) {
            for (int k = 0; k < word.length(); k++) {
                if (word.charAt(i) == character && word.charAt(i) == word.charAt(k)) count++;
            }
        }
        return count > 1;
    }

    public static void clrscreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}