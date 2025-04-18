import java.util.Random;
import java.util.Scanner;

public class Minesweeper {
    static final int SIZE = 9;   // Defines the size of the grid (9x9)
    static final int MINES = 10; // Defines the total number of mines on the board

    // Function to initialize the board and randomly place mines
    static void initializeBoard(char[][] board, int[][] mineCount) {
        Random rand = new Random();
        int placedMines = 0; // Keeps track of how many mines have been placed

        // Keep placing mines until the desired number is reached
        while (placedMines < MINES) {
            int x = rand.nextInt(SIZE); // Random row index
            int y = rand.nextInt(SIZE); // Random column index

            if (board[x][y] != '*') { // Check if there's already a mine
                board[x][y] = '*';     // Place a mine at that cell
                placedMines++;         // Increment the mine counter

                // Update mine counts for all surrounding cells
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        int nx = x + i; // Neighboring row index
                        int ny = y + j; // Neighboring column index

                        // If the neighbor cell is valid and not a mine
                        if (nx >= 0 && nx < SIZE && ny >= 0 && ny < SIZE && board[nx][ny] != '*') {
                            mineCount[nx][ny]++; // Increase the count of surrounding mines
                        }
                    }
                }
            }
        }
    }

    // Function to display the game board
    // If revealMines is true, it will show where the mines are
    static void displayBoard(char[][] board, boolean revealMines) {
        System.out.print("   "); // Aligns column headers
        for (int i = 0; i < SIZE; i++)
            System.out.print(i + " "); // Print column numbers
        System.out.println();

        for (int i = 0; i < SIZE; i++) {
            System.out.print(i + " | "); // Print row number and separator
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == '*' && !revealMines) {
                    System.out.print(". "); // Hide mines during gameplay
                } else {
                    System.out.print(board[i][j] + " "); // Print actual value (mine or number)
                }
            }
            System.out.println(); // Go to the next row
        }
    }

    // Function to reveal a cell selected by the player
    static boolean revealCell(char[][] board, int[][] mineCount, int x, int y) {
        if (board[x][y] == '*') { // If it's a mine
            return false; // Game over
        }

        // Show number of neighboring mines at this cell
        board[x][y] = (char) ('0' + mineCount[x][y]);
        return true; // Safe cell revealed
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        char[][] board = new char[SIZE][SIZE];    // Game board: holds cells like '.', '*', or numbers
        int[][] mineCount = new int[SIZE][SIZE];  // Array to store mine counts around each cell

        // Fill the board with initial placeholder '.'
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++)
                board[i][j] = '.';

        // Place mines on the board
        initializeBoard(board, mineCount);

        // Ensure non-mine cells are still marked as '.'
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++)
                if (board[i][j] != '*')
                    board[i][j] = '.';

        int revealedCells = 0; // Number of safe cells revealed
        int totalCells = SIZE * SIZE - MINES; // Total number of non-mine cells

        // Main game loop
        while (true) {
            displayBoard(board, false); // Show board without revealing mines
            System.out.print("Enter coordinates (row and column) to reveal (e.g., '2 3'): ");
            int x = scanner.nextInt();
            int y = scanner.nextInt();

            // Validate input coordinates
            if (x < 0 || x >= SIZE || y < 0 || y >= SIZE) {
                System.out.println("Invalid coordinates! Please try again.");
                continue; // Ask again for valid input
            }

            // If selected cell is a mine, end game
            if (!revealCell(board, mineCount, x, y)) {
                System.out.println("Game Over! You hit a mine!");
                displayBoard(board, true); // Reveal all mines on the board
                break;
            }

            revealedCells++; // One more safe cell revealed

            // If all safe cells are revealed, player wins
            if (revealedCells == totalCells) {
                System.out.println("Congratulations! You've cleared the board!");
                break;
            }
        }

        scanner.close(); // Close scanner resource
    }
}
