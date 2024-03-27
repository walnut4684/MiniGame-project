package Omok;

import java.util.Scanner;

public class UserPlayer {
    private int color;
    private int player;
    private Scanner scanner;

    public UserPlayer(int color) {
        this.color = color;
        this.player = player;
        scanner = new Scanner(System.in);
        
    }
    

    public void makeMove(Board board) {
        Scanner scanner = new Scanner(System.in);
        int row, col;

        System.out.print("Enter row (0-14): ");
        row = scanner.nextInt();
        System.out.print("Enter column (0-14): ");
        col = scanner.nextInt();

         while (board.getCell(row, col) != Board.EMPTY) {
            System.out.println("Invalid move! Cell is already occupied. Try again.");
            System.out.print("Enter row (0-14): ");
            row = scanner.nextInt();
            System.out.print("Enter column (0-14): ");
            col = scanner.nextInt();
        }
        board.setCell(row, col, player);
    }

    private boolean isValidMove(Board board, int row, int col) {
        if (row < 0 || row >= 15 || col < 0 || col >= 15) {
            return false;
        }

        return board.getCell(row, col) == Board.EMPTY;
    }
}
