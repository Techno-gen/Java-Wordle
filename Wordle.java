import java.util.Scanner;
import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.io.File;

public class Wordle {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        File fileread = new File("words.txt");
        String guess;
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
        System.out.println("\n Wordle");
        for (i = 0; i < 6; i++) {
            while (true) {
                System.out.println();
                guess = sc.nextLine();
                if (words.contains(guess)) {
                    break;
                }
                else {
                    printAt("Guess is not in wordlist", i + 4, guess.length() + 3);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        System.out.println(e);
                    }
                    printAt(" ".repeat(guess.length() + 27), i + 4, 0);
                    printAt("", i + 3, 0);
                }
            }
            
            int count = 1;
            int[] score = scoreGuess(guess, targetWord);
            for (int j = 0; j < 5; j++) {
                char g = guess.charAt(j);
                switch (score[j]) {
                    case 1 -> printAt("\u001B[32m" + g + " \u001B[0m", i + 4, count);
                    case 2 -> printAt("\u001B[33m" + g + " \u001B[0m", i + 4, count);
                    case 0 -> printAt("\u001B[31m" + g + " \u001B[0m", i + 4, count);
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

    public static int[] scoreGuess(String guess, String target) {
        int[] result = new int [5];
        char[] remaining = target.toCharArray();

        // check full
        for (int i = 0; i < 5; i++) {
            if (guess.charAt(i) == target.charAt(i)) {
                result[i] = 1;
                remaining[i] = '*';
            }
        }
        // check partial
        for (int i = 0; i < 5; i++) {
            if (result[i] != 1) {
                char g = guess.charAt(i);
                boolean found = false;

                for (int j = 0; j < 5; j++) {
                    if (remaining[j] == g) {
                        result[i] = 2;
                        remaining[j] = '*';
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    result[i] = 0;
                }
            }
        }

        return result;
    }

    public static void clrscreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void printAt(String phrase, int row, int col) {
        System.out.print(String.format("\u001B[%d;%dH", row, col) + phrase);
    }
}