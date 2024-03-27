package Omok;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AIPlayer {
    private int player;
    private int opponent;
    private Random random;
    private int color;

   
    public AIPlayer(int player) {
        this.player = player;
        this.opponent = player == 1 ? 2 : 1;
        this.random = new Random();
        this.color = color;
    }

    public void makeMove(Board board) {
        
        int row, col;
        Random random = new Random();
        do {
            row = (int) (Math.random() * 15);
            col = (int) (Math.random() * 15);
        } while (board.getCell(row, col) != Board.EMPTY);

        board.setCell(row, col, player);
    }

    public int[] selectBestMove(Board board) {
        List<int[]> availableMoves = generateAvailableMoves(board);
        int maxScore = Integer.MIN_VALUE;
        List<int[]> bestMoves = new ArrayList<>();

        for (int[] move : availableMoves) {
            int score = evaluateMove(board, move[0], move[1]);
            if (score > maxScore) {
                maxScore = score;
                bestMoves.clear();
                bestMoves.add(move);
            } else if (score == maxScore) {
                bestMoves.add(move);
            }
        }

        // Randomly select one of the best moves
        int[] selectedMove = bestMoves.get(random.nextInt(bestMoves.size()));
        return selectedMove;
    }

    private List<int[]> generateAvailableMoves(Board board) {
        List<int[]> availableMoves = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (board.getCell(i, j) == 0) {
                    availableMoves.add(new int[]{i, j});
                }
            }
        }
        return availableMoves;
    }

    private int evaluateMove(Board board, int row, int col) {
        int score = 0;
        board.setCell(row, col, player);

        // Evaluate rows
        for (int i = 0; i < 10; i++) {
            int count = 0;
            for (int j = 0; j < 10; j++) {
                if (board.getCell(i, j) == player) {
                    count++;
                    if (count == 5) {
                        board.setCell(row, col, 0); // Undo move
                        return 5;
                    }
                } else {
                    count = 0;
                }
            }
        }

        // Evaluate columns
        for (int j = 0; j < 10; j++) {
            int count = 0;
            for (int i = 0; i < 10; i++) {
                if (board.getCell(i, j) == player) {
                    count++;
                    if (count == 5) {
                        board.setCell(row, col, 0); // Undo move
                        return 5;
                    }
                } else {
                    count = 0;
                }
            }
        }

        // Undo move
        board.setCell(row, col, 0);

        return 0; // If neither row nor column creates 5, return 0
    }
}
