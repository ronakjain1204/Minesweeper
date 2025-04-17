#include <iostream> 

using namespace std;\

const int SIZE = 9;   // Grid kitne ki honi chaiye
const int MINES = 10; // No of Mines ko define kar deta h

// Ye function game board ko initialize karta hai, jo ki mines ko place karta hai
void initializeBoard(char board[SIZE][SIZE], int mineCount[SIZE][SIZE]) { // mineCount array mein neighboring mine ko count karta hai
    int placedMines = 0; // Kitni mines palced h
    while (placedMines < MINES) {  // Loop jab tak placed mines no of mines k barabar ho jae
        int x = rand() % SIZE; // Randomly select a row index
        int y = rand() % SIZE; // Randomly select a column index
        if (board[x][y] != '*') { // Check karta h ki kai usme pehle se mine to nhi h
            board[x][y] = '*'; // Place a mine in the selected cell
            placedMines++;     // Increment the counter for placed mines

            // Update neighboring mine counts
            for (int i = -1; i <= 1; i++) { // Loop through the rows around the mine
                for (int j = -1; j <= 1; j++) { // Loop through the columns around the mine
                    int nx = x + i; // Calculate neighboring row index
                    int ny = y + j; // Calculate neighboring column index
                    // Check if the neighbor index is valid and not a mine
                    if (nx >= 0 && nx < SIZE && ny >= 0 && ny < SIZE && board[nx][ny] != '*') {
                        mineCount[nx][ny]++; // Increment the mine count for the neighboring cell
                    }
                }
            }
        }
    }
}

// Function to display the board
void displayBoard(char board[SIZE][SIZE], bool revealMines) {
    cout << "   ";                 // Print an initial space for alignment of column headers
    for (int i = 0; i < SIZE; i++) // Loop to print column headers (0 to SIZE-1)
        cout << i << " ";          // Print the column index followed by a space
    cout << endl;                  // Move to the next line after printing headers

    for (int i = 0; i < SIZE; i++) { // Loop through each row
        cout << i << " | "; // Print the row header (row index) followed by a vertical separator
        for (int j = 0; j < SIZE; j++) { // Loop through each column in the row
            if (board[i][j] == '*' && !revealMines) {  // Check if the cell is a mine and mines should be hidden
                cout << '.'; // Display a dot for hidden mines
            }
            else {
                cout << board[i][j]; // Display the value of the revealed cell
            }
            cout << " "; // Space between cells for better readability
        }
        cout << endl; // Move to the next line after finishing the row
    }
}

// Function to reveal a cell on the board
bool revealCell(char board[SIZE][SIZE], int mineCount[SIZE][SIZE], int x, int y) {
    if (board[x][y] == '*') { // Check if the revealed cell is a mine
        return false; // Return false indicating a mine was hit
    }

    board[x][y] = '0' + mineCount[x][y]; // Reveal the cell by showing the count of neighboring mines
    return true; // Return true indicating the cell was successfully revealed
}

int main() {
    char board[SIZE][SIZE] = {'.'};  // Initialize the board with hidden cells ('.')
    int mineCount[SIZE][SIZE] = {0}; // Array jo ki neighboring mine ko count karta hai sabse pehle 0 hogi

    // Sabse pehle board banaenge or mines ko rakhenge
    initializeBoard(board, mineCount);

    // Board ko fill karnege with blank spaces ('.') for non-mine cells
    for (int i = 0; i < SIZE; i++)
        for (int j = 0; j < SIZE; j++)
            if (board[i][j] != '*') // Agar board array not equal to '*' then it is a blank cell
                board[i][j] = '.';  // Set the cell to a blank space

    int revealedCells = 0; // Kitne cells apan dekh chuke h
    int totalCells = SIZE * SIZE - MINES; // kitne cells abhi bache h bina mines wale cells k

    while (true) {  // Main game loop
        displayBoard(board, false); // Show the board without revealing mines
        int x, y; // Variables to hold user input coordinates
        cout << "Enter coordinates (row and column) to reveal (e.g., '2 3'): ";
        cin >> x >> y; // Get user input for coordinates

        // Check for valid input
        if (x < 0 || x >= SIZE || y < 0 || y >= SIZE) {   // Validate input range
            cout << "Invalid coordinates! Please try again." << endl; // Error message for invalid input
            continue;  // Restart the loop for new input
        }

        // Check if the revealed cell is a mine
        if (!revealCell(board, mineCount, x, y)) {  // Attempt to reveal the cell
            cout << "Game Over! You hit a mine!" << endl; // Game over message
            displayBoard(board, true); // Reveal the board including mines
            break;  // Exit the game loop
        }

        revealedCells++; // Increment the counter for revealed cells
        // Check if all non-mine cells have been revealed
        if (revealedCells == totalCells) {
            cout << "Congratulations! You've cleared the board!" << endl; // Winning message
            break; // Exit the game loop
        }
    }
    return 0; // End of the program
}
