package Omok;

import java.awt.*;
public class Board {
    public static final int EMPTY = 0;
    public static final int BLACK = 1;
    public static final int WHITE = 2;

    private int[][] cells;
    private int lastMoveRow;
    private int lastMoveCol;

    public Board() {
        cells = new int[15][15];
        lastMoveRow = -1;
        lastMoveCol = -1;
    }

    public int getCell(int row, int col) {
        return cells[row][col];
    }

    public void setCell(int row, int col, int value) {
        cells[row][col] = value;
        lastMoveRow = row;
        lastMoveCol = col;
    }

    public int getLastMoveRow() {
        return lastMoveRow;
    }

    public int getLastMoveCol() {
        return lastMoveCol;
    }

    public boolean isGameOver() {
        int lastColor = cells[lastMoveRow][lastMoveCol];

        // Check horizontal line
        int count = 1;
        int left = lastMoveCol - 1;
        while (left >= 0 && cells[lastMoveRow][left] == lastColor) {
            count++;
            left--;
        }
        int right = lastMoveCol + 1;
        while (right < 15 && cells[lastMoveRow][right] == lastColor) {
            count++;
            right++;
        }
        if (count >= 5) {
            return true;
        }

        // Check vertical line
        count = 1;
        int top = lastMoveRow - 1;
        while (top >= 0 && cells[top][lastMoveCol] == lastColor) {
            count++;
            top--;
        }
        int bottom = lastMoveRow + 1;
        while (bottom < 15 && cells[bottom][lastMoveCol] == lastColor) {
            count++;
            bottom++;
        }
        if (count >= 5) {
            return true;
        }

        // Check diagonal lines
        count = 1;
        int topLeftRow = lastMoveRow - 1;
        int topLeftCol = lastMoveCol - 1;
        while (topLeftRow >= 0 && topLeftCol >= 0 && cells[topLeftRow][topLeftCol] == lastColor) {
            count++;
            topLeftRow--;
            topLeftCol--;
        }
        int bottomRightRow = lastMoveRow + 1;
        int bottomRightCol = lastMoveCol + 1;
        while (bottomRightRow < 15 && bottomRightCol < 15 && cells[bottomRightRow][bottomRightCol] == lastColor) {
            count++;
            bottomRightRow++;
            bottomRightCol++;
        }
        if (count >= 5) {
            return true;
        }

        count = 1;
        int topRightRow = lastMoveRow - 1;
        int topRightCol = lastMoveCol + 1;
        while (topRightRow >= 0 && topRightCol < 15 && cells[topRightRow][topRightCol] == lastColor) {
            count++;
            topRightRow--;
            topRightCol++;
        }
        int bottomLeftRow = lastMoveRow + 1;
        int bottomLeftCol = lastMoveCol - 1;
        while (bottomLeftRow < 15 && bottomLeftCol >= 0 && cells[bottomLeftRow][bottomLeftCol] == lastColor) {
            count++;
            bottomLeftRow++;
            bottomLeftCol--;
        }
        if (count >= 5) {
            return true;
        }

        return false;
    }
}
