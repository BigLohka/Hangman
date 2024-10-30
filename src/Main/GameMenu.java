package Main;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public class GameMenu {
    private enum menuOption {
        PLAY_GAME,
        VIEW_RULES,
        CHECK_SCORES,
        EXIT_GAME
    }

    private Scanner scanner = new Scanner(System.in);
    private int wins = 0;
    private int loses = 0;

    private char[] gameWord;
    private List<Character> wrongLetters;
    private String hiddenWord;

    private int attempts;

    private boolean isWin;
    private boolean isPlay = true;

    private final String[] words = {"cat", "bow", "gallery", "rainbows", "accidentally", "photographic"};

    public GameMenu() {
        attempts = 0;
        wrongLetters = new ArrayList<>();
    }

    public void showMenu() {
        System.out.println("Welcome to HANGMAN");
        System.out.println("1. Play HANGMAN");
        System.out.println("2. Rules ");
        System.out.println("3. Your score");
        System.out.println("4. Exit");

        menuOption option = selectMenuOption();

        switch (option) {
            case PLAY_GAME -> {
                hiddenWord = createWord();
                gameWord = new char[hiddenWord.length()];
                IntStream.range(0, gameWord.length).forEach(i -> gameWord[i] = '_');
                wrongLetters.clear();
                showGameField(attempts);
            }
            case VIEW_RULES -> showRules();
            case CHECK_SCORES -> showScore();
            case EXIT_GAME -> isPlay = false;
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameMenu gameMenu = (GameMenu) o;
        return Arrays.equals(gameWord, gameMenu.gameWord) && Objects.equals(hiddenWord, gameMenu.hiddenWord);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(hiddenWord);
        result = 31 * result + Arrays.hashCode(gameWord);
        return result;
    }

    private String createWord() {
        return words[ThreadLocalRandom.current().nextInt(words.length)];
    }

    private void showGameField(int attempts) {
        while (isPlay) {
            switch (attempts) {
                case 0 -> System.out.println("+---+\n|   |\n|\n|\n|\n|");
                case 1 -> System.out.println("+---+\n|   |\n|   O\n|\n|\n|");
                case 2 -> System.out.println("+---+\n|   |\n|   O\n|   |\n|\n|");
                case 3 -> System.out.println("+---+\n|   |\n|   O\n|  /|\n|\n|");
                case 4 -> System.out.println("+---+\n|   |\n|   O\n|  /|\\ \n|\n|");
                case 5 -> System.out.println("+---+\n|   |\n|   O\n|  /|\\ \n|  /\n|");
                case 6 -> System.out.println("+---+\n|   |\n|   O\n|  /|\\\n|  / \\\n|");
                default -> System.out.println("Default");
            }

            if (isWin) {
                hiddenWord = createWord();
                winScene();
                System.out.println("Key to continue");
            }

            if (getAttempts() >= 6) {
                looseScene();
                System.out.println("You loose,current loses:" + getLoses());
            }

            showWord();
            String userInput = scanner.next();
            char letter = userInput.charAt(0);

            if (showHiddenWord(userInput)) {
                showWord();
                showUncorrectLetters();
            } else {
                showUncorrectLetters();
                if (wrongLetters.contains(letter)) {
                    System.out.println();
                } else {
                    setAttempts(attempts++);
                    showAttempts();
                }
            }
            wrongLetters.add(letter);
        }
    }


    private void showWord() {
        System.out.println(gameWord);
    }

    private void showRules() {
        System.out.println("Game rules ");
        System.out.println("1. Hidden word ");
        System.out.println("2. Guess letter ");
        System.out.println("3. Incorrect guess comes lose ");
        System.out.println("4. Guess the word and win ");

        showMenu();
    }

    private void showScore() {
        System.out.println("You win: " + wins + "\n" + "You lose: " + loses);
        showMenu();
    }

    private menuOption selectMenuOption() {
        int userInput = 0;
        boolean isValidOption = true;

        while (isValidOption) {
            userInput = scanner.nextInt();
            if (userInput >= 1 && userInput <= 4) {
                isValidOption = false;
            } else {
                System.out.println("Not valid number enter between 1 and 4. ");
            }
        }

        return menuOption.values()[userInput - 1];
    }


    private Boolean showHiddenWord(String symbol) {
        char[] wordArray = hiddenWord.toCharArray();
        char letter = Character.toLowerCase(symbol.charAt(0));


        if (!hiddenWord.contains(symbol)) {
            return false;
        }

        for (int i = 0; i < wordArray.length; i++) {
            if (Character.toLowerCase(wordArray[i]) == letter) {
                gameWord[i] = letter;
            }
        }

        if (new String(gameWord).equals(hiddenWord)) {
            isWin = true;
        }

        return true;
    }

    private void looseScene() {
        int value = getLoses();
        value++;
        setLoses(value);
        setAttempts(0);
        showMenu();
    }

    private void winScene() {
        int value = getWins();
        value++;
        setWins(value);
        setAttempts(0);
        System.out.println("You win, current wins: " + getWins());
        isWin = false;
        showMenu();

    }

    private void showAttempts() {
        System.out.println("Attempts : " + attempts);
    }

    private int getAttempts() {
        return attempts;
    }

    private void setAttempts(int value) {
        attempts = value;
    }

    private int getWins() {
        return wins;
    }

    private void setWins(int wins) {
        this.wins = wins;
    }

    private int getLoses() {
        return loses;
    }

    private void setLoses(int loses) {
        this.loses = loses;
    }

    private void showUncorrectLetters() {
        for (char c : wrongLetters) {
            System.out.print(c + ", ");
        }
    }

}

