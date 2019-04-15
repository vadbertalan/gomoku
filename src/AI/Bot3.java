// Vad Bertalan | Computer Science | UBB | 2nd year | vbim1780

package AI;

import utils.Coord;

public class Bot3 implements Bot {

    private final int N = 3;
    private final int GAME_NOT_OVER = -5555;
    private char ownSign, opponentSign;

    public Bot3(char sign) {
        this.ownSign = sign;
        opponentSign = (sign == 'x') ? 'o' : 'x';
    }

    @Override
    public Coord findBestMove(char[][] board) {
        Coord bestMove = new Coord(-100, -100);
        int bestScore = -10000;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (board[i][j] == '.') {
                    board[i][j] = ownSign;
                    // setting the opponent the minimizer
                    int score = minmax(board, false, opponentSign, 1);
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

    private int evaluateBoard(char[][] board) {
        for (int i = 0; i < N; i++) {
            // checking rows
            if (board[i][0] != '.' && board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
                return (board[i][0] == ownSign) ? 10 : -10;
            }
            // checking columns
            if (board[0][i] != '.' && board[0][i] == board[1][i] && board[1][i] == board[2][i]) {
                return (board[0][i] == ownSign) ? 10 : -10;
            }
        }

        // checking main diagonal
        if (board[0][0] != '.' && board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            return (board[0][0] == ownSign) ? 10 : -10;
        }

        // checking secondary diagonal
        if (board[0][2] != '.' && board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            return (board[0][2] == ownSign) ? 10 : -10;
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

    private int minmax(char[][] board, boolean isMax, char sign, int depth) {
        // if we hit a leaf in game tree, we return the board's value
        int boardValue = evaluateBoard(board);
        if (boardValue != GAME_NOT_OVER) {
            int depthCost = (isMax) ? depth : -depth;
            return boardValue + depthCost;
        }

        int gameValue;

        if (isMax) {
            gameValue = -10000;

            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    if (board[i][j] == '.') {
                        board[i][j] = sign;

                        int currentGameValue = minmax(board, false, (sign == 'x') ? 'o' : 'x', depth + 1);
                        if (currentGameValue > gameValue) {
                            gameValue = currentGameValue;
                        }

                        board[i][j] = '.';
                    }
                }
            }
        } else {
            gameValue = 10000;

            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    if (board[i][j] == '.') {
                        board[i][j] = sign;

                        int currentGameValue = minmax(board, true, (sign == 'x') ? 'o' : 'x', depth + 1);
                        if (currentGameValue < gameValue) {
                            gameValue = currentGameValue;
                        }

                        board[i][j] = '.';
                    }
                }
            }
        }
        return gameValue;
    }
}
