import java.util.Random;
import java.util.Scanner;

public class Minesweeper {
    static final int SIZE = 9;   // Grid ki size define kar di
    static final int MINES = 10; // Kitni mines hongi game mein

    // Ye function board ko initialize karta hai aur mines ko randomly place karta hai
    static void initializeBoard(char[][] board, int[][] mineCount) {
        Random rand = new Random();
        int placedMines = 0; // Kitni mines ab tak place ho chuki hain

        while (placedMines < MINES) { // Jab tak sabhi mines place na ho jaayein
            int x = rand.nextInt(SIZE); // Random row index choose kar rahe
            int y = rand.nextInt(SIZE); // Random column index choose kar rahe

            if (board[x][y] != '*') { // Check kar rahe ki already mine to nahi hai waha
                board[x][y] = '*';     // Mine ko place kar diya
                placedMines++;         // Counter increment kar diya

                // Ab uss mine ke aas paas wale cells ka count badha rahe
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        int nx = x + i; // Neighboring row index
                        int ny = y + j; // Neighboring column index

                        // Valid coordinate aur waha mine nahi honi chahiye
                        if (nx >= 0 && nx < SIZE && ny >= 0 && ny < SIZE && board[nx][ny] != '*') {
                            mineCount[nx][ny]++; // Mine count increase kar rahe
                        }
                    }
                }
            }
        }
    }

    // Ye function board ko print karta hai, agar revealMines true hai to mines bhi dikhata hai
    static void displayBoard(char[][] board, boolean revealMines) {
        System.out.print("   "); // Column ke headings align karne ke liye
        for (int i = 0; i < SIZE; i++)
            System.out.print(i + " "); // Column numbers print kar rahe
        System.out.println();

        for (int i = 0; i < SIZE; i++) {
            System.out.print(i + " | "); // Row ka number aur separator
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == '*' && !revealMines) {
                    System.out.print(". "); // Agar mine hai aur reveal nahi karni to '.'
                } else {
                    System.out.print(board[i][j] + " "); // Nahi to actual value print karo
                }
            }
            System.out.println(); // Newline after each row
        }
    }

    // Ye function user ke dwara selected cell ko reveal karta hai
    static boolean revealCell(char[][] board, int[][] mineCount, int x, int y) {
        if (board[x][y] == '*') { // Agar mine hai to game over
            return false;
        }

        board[x][y] = (char) ('0' + mineCount[x][y]); // Agar safe cell hai to uske aas paas ki mines ka count dikhate hai
        return true;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        char[][] board = new char[SIZE][SIZE];    // Game board jisme '.' se initialize karenge
        int[][] mineCount = new int[SIZE][SIZE];  // Ye array har cell ke aas paas ki mines count karega

        // Board ko initially '.' se fill kar rahe
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++)
                board[i][j] = '.';

        // Board pe mines place kar rahe
        initializeBoard(board, mineCount);

        // Jo cells mines nahi hai unko dubara '.' se mark kar rahe
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++)
                if (board[i][j] != '*')
                    board[i][j] = '.';

        int revealedCells = 0; // Kitne cells reveal ho chuke
        int totalCells = SIZE * SIZE - MINES; // Total non-mine cells

        // Main game loop
        while (true) {
            displayBoard(board, false); // Board print karo bina mines ke
            System.out.print("Enter coordinates (row and column) to reveal (e.g., '2 3'): ");
            int x = scanner.nextInt();
            int y = scanner.nextInt();

            // Invalid coordinates ka check
            if (x < 0 || x >= SIZE || y < 0 || y >= SIZE) {
                System.out.println("Invalid coordinates! Please try again.");
                continue; // Loop dobara chalu karo
            }

            // Agar mine hit hui to game over
            if (!revealCell(board, mineCount, x, y)) {
                System.out.println("Game Over! You hit a mine!");
                displayBoard(board, true); // Ab mines bhi dikhao
                break; // Game end
            }

            revealedCells++; // Safe cell reveal hua

            // Agar sabhi safe cells reveal ho gaye to jeet gaye
            if (revealedCells == totalCells) {
                System.out.println("Congratulations! You've cleared the board!");
                break;
            }
        }

        scanner.close(); // Scanner band kar diya
    }
}
