#include <iostream>
using namespace std;

const int SIZE = 9;   // Size of the grid (9x9)
const int MINES = 10; // Total number of mines to place

// This function initializes the game board by placing mines
// and calculating the number of neighboring mines for each cell
void initializeBoard(char board[SIZE][SIZE], int mineCount[SIZE][SIZE]) {
    int placedMines = 0; // Counter to track how many mines have been placed

    // Keep placing mines until the required number is reached
    while (placedMines < MINES) {
        int x = rand() % SIZE; // Random row index
        int y = rand() % SIZE; // Random column index

        // Only place a mine if there's not already one at that location
        if (board[x][y] != '*') {
            board[x][y] = '*'; // Place a mine
            placedMines++;     // Increment the placed mines counter

            // Update the mine count for all neighboring cells
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    int nx = x + i; // Neighboring row index
                    int ny = y + j; // Neighboring column index

                    // Make sure the neighbor is within bounds and not a mine
                    if (nx >= 0 && nx < SIZE && ny >= 0 && ny < SIZE && board[nx][ny] != '*') {
                        mineCount[nx][ny]++; // Increment neighboring mine count
                    }
                }
            }
        }
    }
}

// Function to display the board on the console
void displayBoard(char board[SIZE][SIZE], bool revealMines) {
    cout << "   "; // Padding before column headers
    for (int i = 0; i < SIZE; i++)
        cout << i << " "; // Print column numbers
    cout << endl;

    for (int i = 0; i < SIZE; i++) {
        cout << i << " | "; // Print row number
        for (int j = 0; j < SIZE; j++) {
            if (board[i][j] == '*' && !revealMines) {
                cout << '.'; // Hide mines during normal gameplay
            } else {
                cout << board[i][j]; // Show cell content
            }
            cout << " "; // Space between cells
        }
        cout << endl;
    }
}

// Function to reveal a selected cell
// Returns false if the cell is a mine (game over), true otherwise
bool revealCell(char board[SIZE][SIZE], int mineCount[SIZE][SIZE], int x, int y) {
    if (board[x][y] == '*') {
        return false; // Mine was hit
    }

    board[x][y] = '0' + mineCount[x][y]; // Display number of neighboring mines
    return true; // Cell successfully revealed
}

int main() {
    char board[SIZE][SIZE] = {'.'};  // Initialize the board with '.' to represent unrevealed cells
    int mineCount[SIZE][SIZE] = {0}; // Initialize mine count for all cells to 0

    // Generate board and place mines
    initializeBoard(board, mineCount);

    // Fill non-mine cells with '.' (hidden)
    for (int i = 0; i < SIZE; i++)
        for (int j = 0; j < SIZE; j++)
            if (board[i][j] != '*')
                board[i][j] = '.';

    int revealedCells = 0;                     // Track how many safe cells have been revealed
    int totalCells = SIZE * SIZE - MINES;      // Total number of safe (non-mine) cells

    // Game loop
    while (true) {
        displayBoard(board, false); // Show the board (hide mines)
        int x, y;
        cout << "Enter coordinates (row and column) to reveal (e.g., '2 3'): ";
        cin >> x >> y;

        // Check if input is within valid range
        if (x < 0 || x >= SIZE || y < 0 || y >= SIZE) {
            cout << "Invalid coordinates! Please try again." << endl;
            continue; // Ask again
        }

        // Try to reveal the cell
        if (!revealCell(board, mineCount, x, y)) {
            cout << "Game Over! You hit a mine!" << endl;
            displayBoard(board, true); // Show all mines
            break;
        }

        revealedCells++; // Successfully revealed a safe cell

        // Check for win condition
        if (revealedCells == totalCells) {
            cout << "Congratulations! You've cleared the board!" << endl;
            break;
        }
    }

    return 0;
}
