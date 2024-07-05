import java.util.Random;

public class SudokuSolver {

    private static final int SIZE = 9;
    private static final int SUBGRID_SIZE = 3;

    public static void main(String[] args) {
        int[][] board = new int[SIZE][SIZE];

        generateFullBoard(board);
        removeNumbers(board, 40); // Removing 40 numbers to create the puzzle

        System.out.println("Generated Sudoku Puzzle:");
        printBoard(board);

        if (solveSudoku(board)) {
            System.out.println("Solved Sudoku Puzzle:");
            printBoard(board);
        } else {
            System.out.println("Unsolvable Sudoku puzzle.");
        }
    }

    public static boolean solveSudoku(int[][] board) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] == 0) {
                    for (int num = 1; num <= SIZE; num++) {
                        if (isSafe(board, row, col, num)) {
                            board[row][col] = num;

                            if (solveSudoku(board)) {
                                return true;
                            }

                            board[row][col] = 0; // backtrack
                        }
                    }
                    return false; // no valid number found, trigger backtracking
                }
            }
        }
        return true; // puzzle solved
    }

    public static boolean isSafe(int[][] board, int row, int col, int num) {
        // Check row
        for (int x = 0; x < SIZE; x++) {
            if (board[row][x] == num) {
                return false;
            }
        }

        // Check column
        for (int x = 0; x < SIZE; x++) {
            if (board[x][col] == num) {
                return false;
            }
        }

        // Check 3x3 sub-grid
        int startRow = row - row % SUBGRID_SIZE;
        int startCol = col - col % SUBGRID_SIZE;
        for (int i = 0; i < SUBGRID_SIZE; i++) {
            for (int j = 0; j < SUBGRID_SIZE; j++) {
                if (board[i + startRow][j + startCol] == num) {
                    return false;
                }
            }
        }

        return true;
    }

    public static void generateFullBoard(int[][] board) {
        fillDiagonalSubGrids(board);
        fillRemaining(board, 0, SUBGRID_SIZE);
    }

    public static void fillDiagonalSubGrids(int[][] board) {
        for (int i = 0; i < SIZE; i += SUBGRID_SIZE) {
            fillSubGrid(board, i, i);
        }
    }

    public static void fillSubGrid(int[][] board, int row, int col) {
        Random random = new Random();
        for (int i = 0; i < SUBGRID_SIZE; i++) {
            for (int j = 0; j < SUBGRID_SIZE; j++) {
                int num;
                do {
                    num = random.nextInt(SIZE) + 1;
                } while (!isSafeInSubGrid(board, row, col, num));
                board[row + i][col + j] = num;
            }
        }
    }

    public static boolean isSafeInSubGrid(int[][] board, int row, int col, int num) {
        for (int i = 0; i < SUBGRID_SIZE; i++) {
            for (int j = 0; j < SUBGRID_SIZE; j++) {
                if (board[row + i][col + j] == num) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean fillRemaining(int[][] board, int i, int j) {
        if (j >= SIZE && i < SIZE - 1) {
            i++;
            j = 0;
        }
        if (i >= SIZE && j >= SIZE) {
            return true;
        }
        if (i < SUBGRID_SIZE) {
            if (j < SUBGRID_SIZE) {
                j = SUBGRID_SIZE;
            }
        } else if (i < SIZE - SUBGRID_SIZE) {
            if (j == (i / SUBGRID_SIZE) * SUBGRID_SIZE) {
                j += SUBGRID_SIZE;
            }
        } else {
            if (j == SIZE - SUBGRID_SIZE) {
                i++;
                j = 0;
                if (i >= SIZE) {
                    return true;
                }
            }
        }

        for (int num = 1; num <= SIZE; num++) {
            if (isSafe(board, i, j, num)) {
                board[i][j] = num;
                if (fillRemaining(board, i, j + 1)) {
                    return true;
                }
                board[i][j] = 0;
            }
        }
        return false;
    }

    public static void removeNumbers(int[][] board, int count) {
        Random random = new Random();
        while (count > 0) {
            int cellId = random.nextInt(SIZE * SIZE);
            int i = (cellId / SIZE);
            int j = cellId % SIZE;
            if (board[i][j] != 0) {
                board[i][j] = 0;
                count--;
            }
        }
    }

    public static void printBoard(int[][] board) {
        for (int r = 0; r < SIZE; r++) {
            for (int d = 0; d < SIZE; d++) {
                System.out.print(board[r][d]);
                System.out.print(" ");
            }
            System.out.print("\n");

            if ((r + 1) % SUBGRID_SIZE == 0 && r != SIZE - 1) {
                System.out.print("");
            }
        }
    }
}
