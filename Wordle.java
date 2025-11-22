import java.util.Scanner;
import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.io.File;

public class Wordle {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        File fileread = new File("words.txt");
        ArrayList<Character> noletters = new ArrayList<>();
        boolean contchar;
        boolean cont2char;
        String guess;
        int count;

        // Load words file into array
        ArrayList<String> words = new ArrayList<>();
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
        System.out.println("\nWordle " + targetWord);
        System.out.println("'=' when equal and is in the right place, '^' when it exists in the word but isn't in the right place, '*' when it doesn't exist.");
        for (i = 0; i < 6; i++) {
            // Only lets the user continue with a valid word from the wordlist.
            // If an invalid word is inputted, the console is cleared and redrawn and the input loop is ran until a valid word is entered.
            while (true) {
                System.out.println();
                guess = sc.nextLine();
                if (words.contains(guess)) {
                    break;
                }
                else {
                    printAt("Guess is not in wordlist", i + 6, guess.length() + 3);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        System.out.println(e);
                    }
                    printAt(" ".repeat(guess.length() + 27), i + 6, 0);
                    printAt("", i + 5, 0);
                }
            }
            
            count = 1;
            noletters.clear();
            for (int j = 0; j < 5; j++) {
                contchar = false;
                cont2char = false;
                for (int k = 0; k < 5; k++) {
                    if (!containsTwo(guess, guess.charAt(j)) && guess.charAt(j) == targetWord.charAt(k)) {
                        contchar = true;
                        break;
                    }
                    else if (containsTwo(guess, guess.charAt(j)) && guess.charAt(j) == targetWord.charAt(k)) { // If it contains two the letter is stored into a "noletters" list that prvents it form being detected for that iteration, unless targetWord also contains two of those letters.
                        cont2char = true;                                                                      // For example: you have a word "heaps" and the user inputs "seems". The previous code would register both Es as if there are two in the target word, even though there's only one. Now it only registers one of those Es.
                        break;                                                                                 // Another example: you have a word "keeps" and you enter "enter". the code will now select for E's in enter, since "keeps" also has those two letters.
                    }
                }

                if (guess.charAt(j) == targetWord.charAt(j)) {
                    printAt("\u001B[32m" + guess.charAt(j) + " \u001B[0m", i + 6, count);
                    if (cont2char && (!containsTwo(targetWord, guess.charAt(j)))) {
                        noletters.add(guess.charAt(j));
                    }
                }
                else if (contchar) {
                    printAt("\u001B[33m" + guess.charAt(j) + " \u001B[0m", i + 6, count);
                }
                else if (cont2char) {
                    if (!noletters.contains(guess.charAt(j))) {
                        printAt("\u001B[33m" + guess.charAt(j) + " \u001B[0m", i + 6, count);
                    }
                    else printAt("\u001B[31m" + guess.charAt(j) + " \u001B[0m", i + 6, count);

                    if (!containsTwo(targetWord, guess.charAt(j)))
                        noletters.add(guess.charAt(j));
                }
                else {
                    printAt("\u001B[31m" + guess.charAt(j) + " \u001B[0m", i + 6, count);
                }
                count += 2;
            }
            if (guess.equals(targetWord)) {
                System.out.println("\n\nYou won!\n");
                return;
            }
        }
        System.out.println("\n\nYou lost!");
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

    public static void printAt(String phrase, int row, int col) {
        System.out.print(String.format("\u001B[%d;%dH", row, col) + phrase);
    }
}