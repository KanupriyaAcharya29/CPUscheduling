/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.tictactoeai;
import java.util.Scanner;
/**
 *
 * @author ASUS
 */
public class TicTacToeAI {
static char[][] board = {
        { ' ', ' ', ' ' },
        { ' ', ' ', ' ' },
        { ' ', ' ', ' ' }
    };
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        printBoard();

        while (true) {
            // Human Move
            boolean validInput = false;
            while (!validInput) {
                System.out.print("Enter your move (row and col from 0 to 2): ");
                int row = scanner.nextInt();
                int col = scanner.nextInt();

                if (row < 0 || row > 2 || col < 0 || col > 2) {
                    System.out.println("❌ Invalid coordinates. Try again.");
                } else if (board[row][col] != ' ') {
                    System.out.println("❌ Cell already occupied. Try again.");
                } else {
                    board[row][col] = 'X';
                    validInput = true;
                }
            }

            printBoard();
            if (isGameOver()) break;

            // AI Move
            System.out.println("🤖 AI is making a move...");
            int[] bestMove = findBestMove();
            board[bestMove[0]][bestMove[1]] = 'O';

            printBoard();
            if (isGameOver()) break;
        }

        scanner.close();
    }

    static void printBoard() {
        System.out.println("-------------");
        for (int i = 0; i < 3; i++) {
            System.out.print("| ");
            for (int j = 0; j < 3; j++) {
                System.out.print(board[i][j] + " | ");
            }
            System.out.println("\n-------------");
        }
    }

    static boolean isGameOver() {
        if (evaluate(board) == 10) {
            System.out.println("AI wins!");
            return true;
        } else if (evaluate(board) == -10) {
            System.out.println("You win!");
            return true;
        } else if (!isMovesLeft()) {
            System.out.println("It's a draw!");
            return true;
        }
        return false;
    }

    static int[] findBestMove() {
        int bestVal = Integer.MIN_VALUE;
        int[] bestMove = { -1, -1 };

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                // Check if cell is empty
                if (board[i][j] == ' ') {
                    board[i][j] = 'O';
                    int moveVal = minimax(0, false);
                    board[i][j] = ' ';
                    if (moveVal > bestVal) {
                        bestMove[0] = i;
                        bestMove[1] = j;
                        bestVal = moveVal;
                    }
                }
            }
        }

        return bestMove;
    }

    static int minimax(int depth, boolean isMax) {
        int score = evaluate(board);

        if (score == 10 || score == -10)
            return score;

        if (!isMovesLeft())
            return 0;

        if (isMax) {
            int best = Integer.MIN_VALUE;

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == ' ') {
                        board[i][j] = 'O';
                        best = Math.max(best, minimax(depth + 1, false));
                        board[i][j] = ' ';
                    }
                }
            }
            return best;
        } else {
            int best = Integer.MAX_VALUE;

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == ' ') {
                        board[i][j] = 'X';
                        best = Math.min(best, minimax(depth + 1, true));
                        board[i][j] = ' ';
                    }
                }
            }
            return best;
        }
    }

    static int evaluate(char[][] b) {
        // Rows
        for (int row = 0; row < 3; row++) {
            if (b[row][0] == b[row][1] && b[row][1] == b[row][2]) {
                if (b[row][0] == 'O') return 10;
                if (b[row][0] == 'X') return -10;
            }
        }

        // Columns
        for (int col = 0; col < 3; col++) {
            if (b[0][col] == b[1][col] && b[1][col] == b[2][col]) {
                if (b[0][col] == 'O') return 10;
                if (b[0][col] == 'X') return -10;
            }
        }

        // Diagonals
        if (b[0][0] == b[1][1] && b[1][1] == b[2][2]) {
            if (b[0][0] == 'O') return 10;
            if (b[0][0] == 'X') return -10;
        }

        if (b[0][2] == b[1][1] && b[1][1] == b[2][0]) {
            if (b[0][2] == 'O') return 10;
            if (b[0][2] == 'X') return -10;
        }

        return 0;
    }

    static boolean isMovesLeft() {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (board[i][j] == ' ')
                    return true;
        return false;
    }
}
