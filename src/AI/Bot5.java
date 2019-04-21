// Vad Bertalan | Computer Science | UBB | 2nd year | vbim1780

package AI;

import utils.Coord;

public class Bot5 implements Bot {
    private static final int GOAL = 5;
    private static final int GAME_NOT_OVER = -555555;
    private static final int NEG_INF = -100000000;
    private static final int INF = 100000000;
    private static final int MAX_DEPTH = 3;
    private static final int VICTORY_SCORE = 1000000;

    private final int N;
    private char ownSign, opponentSign;

    public Bot5(int N, char sign) {
        this.N = N;
        this.ownSign = sign;
        opponentSign = (sign == 'x') ? 'o' : 'x';
    }

    @Override
    public Coord findBestMove(char[][] board) {
        System.out.println(evaluateBoard(board, ownSign));
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
            return evaluateBoard(board, sign);
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

    private int evaluateBoard(char[][] board, char whoIsNext) {
        int boardValue = 0;
        int nrX, nrO;

        // checking rows
        for (int rowIndex = 0; rowIndex < N; rowIndex++) {
            for (int startMask = 0; startMask < N - GOAL + 1; startMask++) {
                nrX = nrO = 0;
                for (int i = startMask; i < startMask + GOAL; i++) {
                    switch (board[rowIndex][i]) {
                        case 'x': nrX++; break;
                        case 'o': nrO++; break;
                    }
                }
                if (nrX > 0 && nrO == 0) boardValue += Math.pow(10, nrX) * ((whoIsNext == 'x') ? 10 : 1);
                if (nrO > 0 && nrX == 0) boardValue -= Math.pow(10, nrO) * ((whoIsNext == 'o') ? 10 : 1);
            }
        }

        // checking colums
        for (int colIndex = 0; colIndex < N; colIndex++) {
            for (int startMask = 0; startMask < N - GOAL + 1; startMask++) {
                nrX = nrO = 0;
                for (int i = startMask; i < startMask + GOAL; i++) {
                    switch (board[i][colIndex]) {
                        case 'x': nrX++; break;
                        case 'o': nrO++; break;
                    }
                }
                if (nrX > 0 && nrO == 0) boardValue += Math.pow(10, nrX) * ((whoIsNext == 'x') ? 10 : 1);
                if (nrO > 0 && nrX == 0) boardValue -= Math.pow(10, nrO) * ((whoIsNext == 'o') ? 10 : 1);
            }
        }

        // checking diagonals above the main diagonal (contains the main diagonal too)
        for (int startDiagonalI = 0, startDiagonalJ = 0; startDiagonalJ < N - GOAL + 1; startDiagonalJ++) {
            int startMaskI = startDiagonalI;
            int startMaskJ = startDiagonalJ;

            while (startMaskJ <= N - GOAL) {
                nrX = nrO = 0;
                for (int i = startMaskI, j = startMaskJ; j < startMaskJ + GOAL; i++, j++) {
                    switch (board[i][j]) {
                        case 'x': nrX++; break;
                        case 'o': nrO++; break;
                    }
                }
                if (nrX > 0 && nrO == 0) boardValue += Math.pow(10, nrX) * ((whoIsNext == 'x') ? 10 : 1);
                if (nrO > 0 && nrX == 0) boardValue -= Math.pow(10, nrO) * ((whoIsNext == 'o') ? 10 : 1);

                startMaskI++;
                startMaskJ++;
            }
        }

        // checking diagonals below the main diagonal
        for (int startDiagonalI = 1, startDiagonalJ = 0; startDiagonalI < N - GOAL + 1; startDiagonalI++) {
            int startMaskI = startDiagonalI;
            int startMaskJ = startDiagonalJ;

            while (startMaskI <= N - GOAL) {
                nrX = nrO = 0;
                for (int i = startMaskI, j = startMaskJ; j < startMaskJ + GOAL; i++, j++) {
                    switch (board[i][j]) {
                        case 'x': nrX++; break;
                        case 'o': nrO++; break;
                    }
                }
                if (nrX > 0 && nrO == 0) boardValue += Math.pow(10, nrX) * ((whoIsNext == 'x') ? 10 : 1);
                if (nrO > 0 && nrX == 0) boardValue -= Math.pow(10, nrO) * ((whoIsNext == 'o') ? 10 : 1);

                startMaskI++;
                startMaskJ++;
            }
        }

        // todo check secondary diagonals
        // checking diagonals above the secondary diagonal (contains the secondary diagonal too)
        for (int startDiagonalI = 0, startDiagonalJ = GOAL - 1; startDiagonalJ < N ; startDiagonalJ++) {
            int startMaskI = startDiagonalI;
            int startMaskJ = startDiagonalJ;

            while (startMaskJ >= GOAL - 1) {
                nrX = nrO = 0;
                for (int i = startMaskI, j = startMaskJ; j > startMaskJ - GOAL; i++, j--) {
                    switch (board[i][j]) {
                        case 'x': nrX++; break;
                        case 'o': nrO++; break;
                    }
                }
                if (nrX > 0 && nrO == 0) boardValue += Math.pow(10, nrX) * ((whoIsNext == 'x') ? 10 : 1);
                if (nrO > 0 && nrX == 0) boardValue -= Math.pow(10, nrO) * ((whoIsNext == 'o') ? 10 : 1);

                startMaskI++;
                startMaskJ--;
            }
        }

        // checking diagonals below the secondary diagonal
        for (int startDiagonalI = 1, startDiagonalJ = N - 1; startDiagonalI < N - GOAL + 1; startDiagonalI++) {
            int startMaskI = startDiagonalI;
            int startMaskJ = startDiagonalJ;

            while (startMaskI <= N - GOAL) {
                nrX = nrO = 0;
                for (int i = startMaskI, j = startMaskJ; j > startMaskJ - GOAL; i++, j--) {
                    switch (board[i][j]) {
                        case 'x': nrX++; break;
                        case 'o': nrO++; break;
                    }
                }
                if (nrX > 0 && nrO == 0) boardValue += Math.pow(10, nrX) * ((whoIsNext == 'x') ? 10 : 1);
                if (nrO > 0 && nrX == 0) boardValue -= Math.pow(10, nrO) * ((whoIsNext == 'o') ? 10 : 1);

                startMaskI++;
                startMaskJ--;
            }
        }


        return (ownSign == 'x') ? boardValue : -boardValue;
    }

    private int checkForVictory(char[][] board) {
        // checking rows
        for (int rowIndex = 0; rowIndex < N; rowIndex++) {
            for (int startMask = 0; startMask < N - GOAL + 1; startMask++) {
                boolean maskVictory = true;
                char maskSign = board[rowIndex][startMask];
                if (maskSign != '.') {
                    for (int i = startMask + 1; i < startMask + GOAL; i++) {
                        if (board[rowIndex][i] != maskSign) {
                            maskVictory = false;
                            break;
                        }
                    }
                    if (maskVictory) {
                        return (maskSign == ownSign) ? VICTORY_SCORE : -VICTORY_SCORE;
                    }
                }
            }
        }

        // checking colums
        for (int colIndex = 0; colIndex < N; colIndex++) {
            for (int startMask = 0; startMask < N - GOAL + 1; startMask++) {
                boolean maskVictory = true;
                char maskSign = board[startMask][colIndex];
                if (maskSign != '.') {
                    for (int i = startMask + 1; i < startMask + GOAL; i++) {
                        if (board[i][colIndex] != maskSign) {
                            maskVictory = false;
                            break;
                        }
                    }
                    if (maskVictory) {
                        return (maskSign == ownSign) ? VICTORY_SCORE : -VICTORY_SCORE;
                    }
                }
            }
        }

        // checking diagonals above the main diagonal (contains the main diagonal too)
        for (int startDiagonalI = 0, startDiagonalJ = 0; startDiagonalJ < N - GOAL + 1; startDiagonalJ++) {
            int startMaskI = startDiagonalI;
            int startMaskJ = startDiagonalJ;

            while (startMaskJ < N - GOAL + 1) {
                boolean maskVictory = true;
                char maskSign = board[startMaskI][startMaskJ];
                if (maskSign != '.') {
                    for (int i = startMaskI, j = startMaskJ; j < startMaskJ + GOAL; i++, j++) {
                        if (board[i][j] != maskSign) {
                            maskVictory = false;
                            break;
                        }
                    }
                    if (maskVictory) {
                        return (maskSign == ownSign) ? VICTORY_SCORE : -VICTORY_SCORE;
                    }
                }
                startMaskI++;
                startMaskJ++;
            }
        }

        // checking diagonals below the main diagonal
        for (int startDiagonalI = 1, startDiagonalJ = 0; startDiagonalI < N - GOAL + 1; startDiagonalI++) {
            int startMaskI = startDiagonalI;
            int startMaskJ = startDiagonalJ;

            while (startMaskI < N - GOAL + 1) {
                boolean maskVictory = true;
                char maskSign = board[startMaskI][startMaskJ];
                if (maskSign != '.') {
                    for (int i = startMaskI, j = startMaskJ; j < startMaskJ + GOAL; i++, j++) {
                        if (board[i][j] != maskSign) {
                            maskVictory = false;
                            break;
                        }
                    }
                    if (maskVictory) {
                        return (maskSign == ownSign) ? VICTORY_SCORE : -VICTORY_SCORE;
                    }
                }
                startMaskI++;
                startMaskJ++;
            }
        }

        // checking diagonals above the secondary diagonal (contains the secondary diagonal too)
        for (int startDiagonalI = 0, startDiagonalJ = GOAL - 1; startDiagonalJ < N ; startDiagonalJ++) {
            int startMaskI = startDiagonalI;
            int startMaskJ = startDiagonalJ;

            while (startMaskJ >= GOAL - 1) {
                boolean maskVictory = true;
                char maskSign = board[startMaskI][startMaskJ];
                if (maskSign != '.') {
                    for (int i = startMaskI, j = startMaskJ; j > startMaskJ - GOAL; i++, j--) {
                        if (board[i][j] != maskSign) {
                            maskVictory = false;
                            break;
                        }
                    }
                    if (maskVictory) {
                        return (maskSign == ownSign) ? VICTORY_SCORE : -VICTORY_SCORE;
                    }
                }
                startMaskI++;
                startMaskJ--;
            }
        }

        // checking diagonals below the secondary diagonal
        for (int startDiagonalI = 1, startDiagonalJ = N - 1; startDiagonalI < N - GOAL + 1; startDiagonalI++) {
            int startMaskI = startDiagonalI;
            int startMaskJ = startDiagonalJ;

            while (startMaskI <= N - GOAL) {
                boolean maskVictory = true;
                char maskSign = board[startMaskI][startMaskJ];
                if (maskSign != '.') {
                    for (int i = startMaskI, j = startMaskJ; j > startMaskJ - GOAL; i++, j--) {
                        if (board[i][j] != maskSign) {
                            maskVictory = false;
                            break;
                        }
                    }
                    if (maskVictory) {
                        return (maskSign == ownSign) ? VICTORY_SCORE : -VICTORY_SCORE;
                    }
                }

                startMaskI++;
                startMaskJ--;
            }
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
