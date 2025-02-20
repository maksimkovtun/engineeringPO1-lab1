package org.project;

public class Board {
    private final Checker[][] board;
    private String currentPlayer = "white";
    private boolean mustCapture = false; 
    private int mandatoryRow = -1, mandatoryCol = -1;
    public Board() {
        board = new Checker[8][8];
        initializeBoard();
    }
    private void initializeBoard() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 8; col++) {
                if ((row + col) % 2 != 0) board[row][col] = new Checker("black");
            }
        }
        for (int row = 5; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if ((row + col) % 2 != 0) board[row][col] = new Checker("white");
            }
        }
    }
    public Checker[][] getBoard() {
        return board;
    }
    public Checker getChecker(int row, int col) {
        if (isValidCell(row, col)) return board[row][col];
        return null;
    }
    public String getCurrentPlayer() {
        return currentPlayer;
    }
    public boolean mustCapture() {
        return mustCapture;
    }
    public boolean canCapture(int row, int col) {
        Checker checker = board[row][col];
        if (checker == null || !checker.color().equals(currentPlayer)) return false;
        int[] directions = {-2, 2};
        for (int dr : directions) {
            for (int dc : directions) {
                int targetRow = row + dr;
                int targetCol = col + dc;
                if (isValidCell(targetRow, targetCol) && board[targetRow][targetCol] == null) {
                    int midRow = row + dr / 2;
                    int midCol = col + dc / 2;
                    Checker middleChecker = board[midRow][midCol];
                    if (middleChecker != null && !middleChecker.color().equals(currentPlayer)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public boolean moveChecker(int startRow, int startCol, int endRow, int endCol) {
        Checker checker = board[startRow][startCol];
        if (checker == null || !checker.color().equals(currentPlayer)) return false;
        if (mustCapture && (startRow != mandatoryRow || startCol != mandatoryCol)) return false;
        int dRow = Math.abs(endRow - startRow);
        int dCol = Math.abs(endCol - startCol);
        if (dRow == 1 && dCol == 1 && !mustCapture && board[endRow][endCol] == null) {
            board[endRow][endCol] = checker;
            board[startRow][startCol] = null;
            switchPlayer();
            return true;
        }
        if (dRow == 2 && dCol == 2 && board[endRow][endCol] == null) {
            int midRow = (startRow + endRow) / 2;
            int midCol = (startCol + endCol) / 2;
            Checker middleChecker = board[midRow][midCol];
            if (middleChecker != null && !middleChecker.color().equals(currentPlayer)) {
                board[endRow][endCol] = checker;
                board[startRow][startCol] = null;
                board[midRow][midCol] = null;
                if (canCapture(endRow, endCol)) {
                    mustCapture = true;
                    mandatoryRow = endRow;
                    mandatoryCol = endCol;
                } else {
                    mustCapture = false;
                    mandatoryRow = -1;
                    mandatoryCol = -1;
                    switchPlayer();
                }
                return true;
            }
        }
        return false;
    }
    public boolean hasMoves(String player) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Checker checker = board[row][col];
                if (checker != null && checker.color().equals(player)) {
                    int[] directions = {-1, 1};
                    for (int dr : directions) {
                        for (int dc : directions) {
                            int newRow = row + dr;
                            int newCol = col + dc;
                            if (isValidCell(newRow, newCol) && board[newRow][newCol] == null) return true;
                        }
                    }
                    if (canCapture(row, col)) return true;
                }
            }
        }
        return false;
    }
    private void switchPlayer() {
        currentPlayer = currentPlayer.equals("white") ? "black" : "white";
    }
    private boolean isValidCell(int row, int col) {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }
    public boolean isGameOver() {
        return !hasMoves("white") || !hasMoves("black");
    }
}
