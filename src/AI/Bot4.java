// Vad Bertalan | Computer Science | UBB | 2nd year | vbim1780

package AI;

import utils.Coord;

import java.util.ArrayList;
import java.util.Arrays;

public class Bot4 implements Bot {
    private static final int N = 4;
    private static final int GAME_NOT_OVER = -5555;
    private static final int NEG_INF = -10000;
    private static final int INF = 10000;
    private static final int MAX_DEPTH = 6;
    private static final int VICTORY_SCORE = 10000;

    private char ownSign, opponentSign;

    public Bot4(char sign) {
        this.ownSign = sign;
        opponentSign = (sign == 'x') ? 'o' : 'x';
    }

    @Override
    public Coord findBestMove(char[][] board) {
        System.out.println(evaluateBoard(board));
        Coord bestMove = new Coord(-100, -100);
        int bestScore = NEG_INF;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (board[i][j] == '.') {
                    board[i][j] = ownSign;

                    int score = minmax(board, false, opponentSign, 1, NEG_INF, INF); // setting the opponent to be the minimizer
                    if (bestScore < score) {
                        bestScore = score;
                        bestMove.setX(i);
                        bestMove.setY(j);
                    }
                    board[i][j] = '.';
                }
            }
        }
        return bestMove;
    }

    private int minmax(char[][] board, boolean isMax, char sign, int depth, int alpha, int beta) {
        // if we hit a leaf in game tree, we return the board's value
        int boardValue = checkForVictory(board);
        if (boardValue != GAME_NOT_OVER) {
            int depthCost = (isMax) ? depth : -depth;
            return boardValue + depthCost;
        }
        if (depth == MAX_DEPTH) {
            return evaluateBoard(board);
        }

        int gameValue;

        if (isMax) {
            gameValue = NEG_INF;

            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    if (board[i][j] == '.') {
                        board[i][j] = sign;

                        int currentGameValue = minmax(board, false, (sign == 'x') ? 'o' : 'x', depth + 1, alpha, beta);
                        if (currentGameValue > gameValue) {
                            gameValue = currentGameValue;
                        }
                        if (currentGameValue > alpha) {
                            alpha = currentGameValue;
                        }

                        board[i][j] = '.';

                        if (beta <= alpha) break; // alpha beta pruning
                    }
                }
            }
        } else {
            gameValue = INF;

            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    if (board[i][j] == '.') {
                        board[i][j] = sign;

                        int currentGameValue = minmax(board, true, (sign == 'x') ? 'o' : 'x', depth + 1, alpha, beta);
                        if (currentGameValue < gameValue) {
                            gameValue = currentGameValue;
                        }
                        if (gameValue < beta) {
                            beta = gameValue;
                        }

                        board[i][j] = '.';

                        if (beta <= alpha) break; // alpha beta pruning
                    }
                }
            }
        }
        return gameValue;
    }

    private int evaluateBoard(char[][] board) {
        int boardValue = 0;

        int nrX, nrO;
        // checking rows
        for (char[] row : board) {
            nrX = nrO = 0;
            for (char item : row) {
                switch (item) {
                    case 'x': nrX++; break;
                    case 'o': nrO++; break;
                }
            }
            if (nrX > 0 && nrO == 0) boardValue += Math.pow(10, nrX);
            if (nrO > 0 && nrX == 0) boardValue -= Math.pow(10, nrO);
        }

        // checking columns
        for (int i = 0; i < N; i++) {
            nrX = nrO = 0;
            for (int j = 0; j < N; j++) {
                switch (board[j][i]) {
                    case 'x': nrX++; break;
                    case 'o': nrO++; break;
                }
            }
            if (nrX > 0 && nrO == 0) boardValue += Math.pow(10, nrX);
            if (nrO > 0 && nrX == 0) boardValue -= Math.pow(10, nrO);
        }

        // checking main diagonal
        nrX = nrO = 0;
        for (int i = 0; i < N; i++) {
            switch (board[i][i]) {
                case 'x': nrX++; break;
                case 'o': nrO++; break;
            }
        }
        if (nrX > 0 && nrO == 0) boardValue += Math.pow(10, nrX);
        if (nrO > 0 && nrX == 0) boardValue -= Math.pow(10, nrO);

        // checking secondary diagonal
        nrX = nrO = 0;
        for (int i = 0; i < N; i++) {
            switch (board[i][N - i - 1]) {
                case 'x': nrX++; break;
                case 'o': nrO++; break;
            }
        }
        if (nrX > 0 && nrO == 0) boardValue += Math.pow(10, nrX);
        if (nrO > 0 && nrX == 0) boardValue -= Math.pow(10, nrO);

        return (ownSign == 'x') ? boardValue : -boardValue;
    }

    private int checkForVictory(char[][] board) {
        for (int i = 0; i < N; i++) {
            // checking rows
            if (board[i][0] != '.' && board[i][0] == board[i][1] && board[i][1] == board[i][2] && board[i][2] == board[i][3]) {
                return (board[i][0] == ownSign) ? VICTORY_SCORE : -VICTORY_SCORE;
            }
            // checking columns
            if (board[0][i] != '.' && board[0][i] == board[1][i] && board[1][i] == board[2][i] && board[2][i] == board[3][i]) {
                return (board[0][i] == ownSign) ? VICTORY_SCORE : -VICTORY_SCORE;
            }
        }

        // checking main diagonal
        if (board[0][0] != '.' && board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[2][2] == board[3][3]) {
            return (board[0][0] == ownSign) ? VICTORY_SCORE : -VICTORY_SCORE;
        }

        // checking secondary diagonal
        if (board[0][3] != '.' && board[0][3] == board[1][2] && board[1][2] == board[2][1] && board[2][1] == board[3][0]) {
            return (board[0][3] == ownSign) ? VICTORY_SCORE : -VICTORY_SCORE;
        }

        // checking draw situation
        boolean draw = true;
        doublefor:
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (board[i][j] == '.') {
                    draw = false;
                    break doublefor;
                }
            }
        }
        return (draw) ? 0 : GAME_NOT_OVER;
    }
}
