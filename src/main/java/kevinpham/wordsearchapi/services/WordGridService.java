package kevinpham.wordsearchapi.services;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class WordGridService {

    private enum Direction {
        HORIZONTAL,
        VERTICAL,
        DIAGONAL,
        HORIZONTAL_INVERSE,
        VERTICAL_INVERSE,
        DIAGONAL_INVERSE,

    }

    private class Coordinate {
        int x;
        int y;

        Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }


    /**
     * Populate grid with words
     *
     * @param words the list of words to find
     */
    public char[][] fillGrid(int gridSize, List<String> words) {

        List<Coordinate> coordinates = new ArrayList<>();
        char[][] contents = new char[gridSize][gridSize];

        // Initialize grid with underscores
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                // Add all possible coordinates into list of coordinates
                coordinates.add(new Coordinate(i, j));
                contents[i][j] = '_';
            }
        }


        // Randomize and shuffle the coordinates in the list
        Collections.shuffle(coordinates);

        for (String word : words) {
            // Loop through each coordinate and see which one could fit word
            for (Coordinate coordinate : coordinates) {
                int x = coordinate.x;
                int y = coordinate.y;
                Direction selectedDirection = getDirectionForFit(contents, word, coordinate);

                if (selectedDirection != null) {

                    // Place word based on valid direction
                    switch (selectedDirection) {
                        case HORIZONTAL:
                            // Convert word to charArray and for each character insert into grid
                            for (char c : word.toCharArray()) {
                                contents[x][y++] = c;
                            }
                            break;
                        case VERTICAL:
                            for (char c : word.toCharArray()) {
                                contents[x++][y] = c;
                            }
                            break;
                        case DIAGONAL:
                            for (char c : word.toCharArray()) {
                                contents[x++][y++] = c;
                            }
                            break;
                        case HORIZONTAL_INVERSE:
                            // Convert word to charArray and for each character insert into grid
                            for (char c : word.toCharArray()) {
                                contents[x][y--] = c;
                            }
                            break;
                        case VERTICAL_INVERSE:
                            for (char c : word.toCharArray()) {
                                contents[x--][y] = c;
                            }
                            break;
                        case DIAGONAL_INVERSE:
                            for (char c : word.toCharArray()) {
                                contents[x--][y--] = c;
                            }
                            break;
                    }
                    // After we fit a word, move onto the next word
                    break;
                }
            }
        }
        // Fill remaining empty spaces after adding words
        randomFillGird(contents);
        return contents;
    }


    /**
     * Method to print out the word search grid
     */
    public void displayGrid(char[][] contents) {
        int gridSize = contents[0].length;
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                // Print each row
                System.out.print(contents[i][j] + " ");
            }
            System.out.println("");
        }
    }


    /**
     * Fill up the remaining spaces in grid with random letters
     */
    private void randomFillGird(char[][] contents) {
        int gridSize = contents[0].length;

        String allCapLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if (contents[i][j] == '_') {
                    int randomIndex = ThreadLocalRandom.current().nextInt(0, allCapLetters.length());

                    contents[i][j] = allCapLetters.charAt(randomIndex);
                }
            }
        }
    }


    /**
     * Check if word fits within grid based on random direction
     *
     * @param word       the word
     * @param coordinate the coordinate
     * @return Valid word placement direction
     */
    private Direction getDirectionForFit(char[][] contents, String word, Coordinate coordinate) {

        List<Direction> directions = Arrays.asList(Direction.values());
        Collections.shuffle(directions);
        for (Direction direction : directions) {
            if (doesFit(contents, word, coordinate, direction)) {
                return direction;
            }
        }
        return null;
    }


    /**
     * Check if word fits in grid given word, coordinate, and direction
     *
     * @param word       the word
     * @param coordinate the coordinate
     * @param direction  random direction
     * @return true if fits, otherwise false
     */
    private boolean doesFit(char[][] contents, String word, Coordinate coordinate, Direction direction) {
        int gridSize = contents[0].length;
        int wordLength = word.length();

        switch (direction) {
            case HORIZONTAL:
                if (coordinate.y + wordLength > gridSize) return false;
                for (int i = 0; i < wordLength; i++) {
                    char gridLetter = contents[coordinate.x][coordinate.y + i];
                    if (gridLetter != '_' && gridLetter != word.charAt(i)) return false;
                }
                break;
            case VERTICAL:
                if (coordinate.x + wordLength > gridSize) return false;
                for (int i = 0; i < wordLength; i++) {
                    char gridLetter = contents[coordinate.x + i][coordinate.y];
                    if (gridLetter != '_' && gridLetter != word.charAt(i)) return false;
                }
                break;
            case DIAGONAL:
                if (coordinate.x + wordLength > gridSize || coordinate.y + wordLength > gridSize) return false;
                for (int i = 0; i < wordLength; i++) {
                    char gridLetter = contents[coordinate.x + i][coordinate.y + i];
                    if (gridLetter != '_' && gridLetter != word.charAt(i)) return false;
                }
                break;
            case HORIZONTAL_INVERSE:
                if (coordinate.y < wordLength) return false;
                for (int i = 0; i < wordLength; i++) {
                    char gridLetter = contents[coordinate.x][coordinate.y - i];
                    if (gridLetter != '_' && gridLetter != word.charAt(i)) return false;
                }
                break;
            case VERTICAL_INVERSE:
                if (coordinate.x < wordLength) return false;
                for (int i = 0; i < wordLength; i++) {
                    char gridLetter = contents[coordinate.x - i][coordinate.y];
                    if (gridLetter != '_' && gridLetter != word.charAt(i)) return false;
                }
                break;
            case DIAGONAL_INVERSE:
                if (coordinate.x < wordLength || coordinate.y < wordLength) return false;
                for (int i = 0; i < wordLength; i++) {
                    char gridLetter = contents[coordinate.x - i][coordinate.y - i];
                    if (gridLetter != '_' && gridLetter != word.charAt(i)) return false;
                }
                break;
        }
        return true;
    }

}