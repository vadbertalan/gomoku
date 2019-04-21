// Vad Bertalan | Computer Science | UBB | 2nd year | vbim1780

package backend;

import AI.Bot;
import AI.Bot3;
import AI.Bot4;
import AI.Bot5;
import frontend.GameFrame;
import models.Field;
import utils.ConsolePrinter;
import utils.Coord;
import utils.ImageServer;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class GameController {
    private JFrame gameFrame;
    private int n;
    private int goal;

    private int unclickedFieldNumber;
    private char playerSign;
    private char opponentSign;
    private boolean playerGoesFirst;

    private Field[][] fields;

    private char[][] board;

    private boolean gameOver;

    private boolean gameInitialized;

    private Bot opponent;

    public GameController(int n) {
        this.gameFrame = new GameFrame(n);
        this.n = this.goal = n;
        gameInitialized = false;
        board = new char[n][n];
    }
    
    public GameController(int n, int goal) {
        this(n);
        this.goal = goal;
    }

    public void initGame(char playerSign) {
        Container contentPane = gameFrame.getContentPane();

        contentPane.removeAll();
        contentPane.revalidate();
        contentPane.repaint();

        unclickedFieldNumber = n * n;
        this.playerSign = playerSign;
        opponentSign = getOtherSign(playerSign);
        playerGoesFirst = playerSign == 'x';
        fields = new Field[n][n];
        gameOver = false;
        resetBoard(board);

        switch (n) {
            case 3: opponent = new Bot3(opponentSign); break;
            case 4: opponent = new Bot4(opponentSign); break;
            default: opponent = new Bot5(n, opponentSign); break;
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                JButton button = new JButton();
                button.setBorder(new BevelBorder(BevelBorder.RAISED));
                gameFrame.getContentPane().add(button);
                fields[i][j] = new Field(button, i, j);
                button.addMouseListener(new ClickListener(this, fields[i][j]));
            }
        }

        gameInitialized = true;
    }

    private void resetBoard(char[][] board) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                board[i][j] = '.';
            }
        }
    }

    public void startGame() {
        if (!gameInitialized) {
            throw new GameNotInitializedException();
        }
        gameFrame.setVisible(true);
        if (!playerGoesFirst) {
            makeOpponentMove();
        }
    }

    public void makeOpponentMove() {
        Coord bestMove = opponent.findBestMove(board);

        // if move was possible to make (i.e. the bestMove's default -100, -100 values were changed)
        if (bestMove.getX() != -100) {
            board[bestMove.getX()][bestMove.getY()] = opponentSign;

            JButton clickedButtonByOp = fields[bestMove.getX()][bestMove.getY()].getButton();
            clickedButtonByOp.setIcon(new ImageIcon(ImageServer.getImage(opponentSign).getScaledInstance(clickedButtonByOp.getWidth(), clickedButtonByOp.getHeight(), Image.SCALE_SMOOTH)));
            if (checkIfLastMoveWasLast()) return;
            clickedButtonByOp.removeMouseListener(clickedButtonByOp.getMouseListeners()[1]);

            ConsolePrinter.printBoardConsole(board);
            ConsolePrinter.drawLine();
        }
    }

    public boolean checkIfLastMoveWasLast() {
        unclickedFieldNumber--;

        boolean ret = false; // ret will be modified anyway, or it won't, exception will stop the program
        try {
            String methodName = ((n == 3 && goal == 3) || (n == 4 && goal == 4)) ? "checkForGameOver" + n : "checkForGameOverLargeBoard";
            Method checkerMethod;
            checkerMethod = GameController.class.getDeclaredMethod(methodName);
            ret = (boolean) checkerMethod.invoke(this);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return ret;
    }

    private boolean checkForGameOver3() {
        char winner = 'N';
        Coord[] winnerCoords = new Coord[n];

        for (int i = 0; i < n; i++) {
            // checking rows
            if (board[i][0] != '.' && board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
                winner = board[i][0];
                for (int j = 0; j < n; j++) {
                    winnerCoords[j] = new Coord(i, j);
                }
                break;
            }
            // checking columns
            if (board[0][i] != '.' && board[0][i] == board[1][i] && board[1][i] == board[2][i]) {
                winner = board[0][i];
                for (int j = 0; j < n; j++) {
                    winnerCoords[j] = new Coord(j, i);
                }
                break;
            }
        }

        // checking main diagonal
        if (board[0][0] != '.' && board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            winner = board[0][0];
            for (int j = 0; j < n; j++) {
                winnerCoords[j] = new Coord(j, j);
            }
        } else {
            // checking secondary diagonal
            if (board[0][2] != '.' && board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
                winner = board[0][2];
                for (int j = 0; j < n; j++) {
                    winnerCoords[j] = new Coord(j, n - j - 1);
                }
            }
        }

        return processOutcome(winner, winnerCoords);
    }

    private boolean checkForGameOver4() {
        char winner = 'N';
        Coord[] winnerCoords = new Coord[n];

        for (int i = 0; i < n; i++) {
            // checking rows
            if (board[i][0] != '.' && board[i][0] == board[i][1] && board[i][1] == board[i][2] && board[i][2] == board[i][3]) {
                winner = board[i][0];
                for (int j = 0; j < n; j++) {
                    winnerCoords[j] = new Coord(i, j);
                }
                break;
            }

            // checking columns
            if (board[0][i] != '.' && board[0][i] == board[1][i] && board[1][i] == board[2][i] && board[2][i] == board[3][i]) {
                winner = board[0][i];
                for (int j = 0; j < n; j++) {
                    winnerCoords[j] = new Coord(j, i);
                }
                break;
            }
        }

        // checking main diagonal
        if (board[0][0] != '.' && board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[2][2] == board[3][3]) {
            winner = board[0][0];
            for (int j = 0; j < n; j++) {
                winnerCoords[j] = new Coord(j, j);
            }
        } else {
            // checking secondary diagonal
            if (board[0][3] != '.' && board[0][3] == board[1][2] && board[1][2] == board[2][1] && board[2][1] == board[3][0]) {
                winner = board[0][2];
                for (int j = 0; j < n; j++) {
                    winnerCoords[j] = new Coord(j, n - j - 1);
                }
            }
        }

        return processOutcome(winner, winnerCoords);
    }

    private boolean checkForGameOverLargeBoard() {
        char winner = 'N';
        Coord[] winnerCoords = new Coord[goal];

        // checking rows
        for (int rowIndex = 0; rowIndex < n; rowIndex++) {
            for (int startMask = 0; startMask < n - goal + 1; startMask++) {
                boolean maskVictory = true;
                char maskSign = board[rowIndex][startMask];
                if (maskSign != '.') {
                    for (int i = startMask; i < startMask + goal; i++) {
                        if (board[rowIndex][i] != maskSign) {
                            maskVictory = false;
                            break;
                        }
                    }
                    if (maskVictory) {
                        winner = maskSign;

                        int winnerCoordsIndex = 0;
                        for (int i = startMask; i < startMask + goal; i++) {
                            winnerCoords[winnerCoordsIndex++] = new Coord(rowIndex, i);
                            }

                        return processOutcome(winner, winnerCoords);
                    }
                }
            }
        }

        // checking colums
        for (int colIndex = 0; colIndex < n; colIndex++) {
            for (int startMask = 0; startMask < n - goal + 1; startMask++) {
                boolean maskVictory = true;
                char maskSign = board[startMask][colIndex];
                if (maskSign != '.') {
                    for (int i = startMask; i < startMask + goal; i++) {
                        if (board[i][colIndex] != maskSign) {
                            maskVictory = false;
                            break;
                        }
                    }
                    if (maskVictory) {
                        winner = maskSign;
                        int winnerCoordsIndex = 0;
                        for (int i = startMask; i < startMask + goal; i++) {
                            winnerCoords[winnerCoordsIndex++] = new Coord(i, colIndex);
                        }
                        return processOutcome(winner, winnerCoords);
                    }
                }
            }
        }

        // checking diagonals above the main diagonal (contains the main diagonal too)
        for (int startDiagonalI = 0, startDiagonalJ = 0; startDiagonalJ < n - goal + 1; startDiagonalJ++) {
            int startMaskI = startDiagonalI;
            int startMaskJ = startDiagonalJ;

            while (startMaskJ < n - goal + 1) {
                boolean maskVictory = true;
                char maskSign = board[startMaskI][startMaskJ];
                if (maskSign != '.') {
                    for (int i = startMaskI, j = startMaskJ; j < startMaskJ + goal; i++, j++) {
                        if (board[i][j] != maskSign) {
                            maskVictory = false;
                            break;
                        }
                    }
                    if (maskVictory) {
                        winner = maskSign; // todo refactor winner to winnerSign

                        int winnerCoordsIndex = 0;
                        for (int i = startMaskI, j = startMaskJ; j < startMaskJ + goal; i++, j++) {
                            winnerCoords[winnerCoordsIndex++] = new Coord(i, j);
                        }
                        return processOutcome(winner, winnerCoords);
                    }
                }
                startMaskI++;
                startMaskJ++;
            }
        }

        // checking diagonals below the main diagonal
        for (int startDiagonalI = 1, startDiagonalJ = 0; startDiagonalI < n - goal + 1; startDiagonalI++) {
            int startMaskI = startDiagonalI;
            int startMaskJ = startDiagonalJ;

            while (startMaskI < n - goal + 1) {
                boolean maskVictory = true; // todo remove this flag entirely!
                char maskSign = board[startMaskI][startMaskJ];
                if (maskSign != '.') {
                    for (int i = startMaskI, j = startMaskJ; j < startMaskJ + goal; i++, j++) {
                        if (board[i][j] != maskSign) {
                            maskVictory = false;
                            break;
                        }
                    }
                    if (maskVictory) {
                        winner = maskSign;

                        int winnerCoordsIndex = 0;
                        for (int i = startMaskI, j = startMaskJ; j < startMaskJ + goal; i++, j++) {
                            winnerCoords[winnerCoordsIndex++] = new Coord(i, j);
                        }
                        return processOutcome(winner, winnerCoords);
                    }
                }
                startMaskI++;
                startMaskJ++;
            }
        }

        // checking diagonals above the secondary diagonal (contains the secondary diagonal too)
        for (int startDiagonalI = 0, startDiagonalJ = goal - 1; startDiagonalJ < n ; startDiagonalJ++) {
            int startMaskI = startDiagonalI;
            int startMaskJ = startDiagonalJ;

            while (startMaskJ >= goal - 1) {
                boolean maskVictory = true;
                char maskSign = board[startMaskI][startMaskJ];
                if (maskSign != '.') {
                    for (int i = startMaskI, j = startMaskJ; j > startMaskJ - goal; i++, j--) {
                        if (board[i][j] != maskSign) {
                            maskVictory = false;
                            break;
                        }
                    }
                    if (maskVictory) {
                        winner = maskSign;

                        int winnerCoordsIndex = 0;
                        for (int i = startMaskI, j = startMaskJ; j > startMaskJ - goal; i++, j--) {
                            winnerCoords[winnerCoordsIndex++] = new Coord(i, j);
                        }
                        return processOutcome(winner, winnerCoords);
                    }
                }
                startMaskI++;
                startMaskJ--;
            }
        }

        // checking diagonals below the secondary diagonal
        for (int startDiagonalI = 1, startDiagonalJ = n - 1; startDiagonalI < n - goal + 1; startDiagonalI++) {
            int startMaskI = startDiagonalI;
            int startMaskJ = startDiagonalJ;

            while (startMaskI <= n - goal) {
                boolean maskVictory = true;
                char maskSign = board[startMaskI][startMaskJ];
                if (maskSign != '.') {
                    for (int i = startMaskI, j = startMaskJ; j > startMaskJ - goal; i++, j--) {
                        if (board[i][j] != maskSign) {
                            maskVictory = false;
                            break;
                        }
                    }
                    if (maskVictory) {
                        winner = maskSign;

                        int winnerCoordsIndex = 0;
                        for (int i = startMaskI, j = startMaskJ; j > startMaskJ - goal; i++, j--) {
                            winnerCoords[winnerCoordsIndex++] = new Coord(i, j);
                        }
                        return processOutcome(winner, winnerCoords);
                    }
                }

                startMaskI++;
                startMaskJ--;
            }
        }

        return processOutcome(winner, winnerCoords);
    }

    private boolean processOutcome(char winner, Coord[] winnerCoords) {
        String outputMessage = "";

        // if we have a clear winner
        if (winner != 'N') {
            // current game is over
            gameOver = true;

            // make it visible on the ui
            for (int i = 0; i < goal; i++) {
                Coord winnerCoord = winnerCoords[i];
                fields[winnerCoord.getX()][winnerCoord.getY()].getButton().setBorder(new LineBorder(Color.RED, 5));
            }
            outputMessage = Character.toUpperCase(winner) + " won!";
        } else {
            if (unclickedFieldNumber == 0) {
                // current game is over
                gameOver = true;
                outputMessage = "Draw!";
            }
        }

        // gameOver flag might get changed in next code fragment so we return the current value safely
        boolean returnableGameOverFlag = gameOver;
        if (gameOver) {
            int input = JOptionPane.showConfirmDialog(null,
                    outputMessage + "\nOne more?",
                    "Game ended",
                    JOptionPane.YES_NO_OPTION);

            if (input == 0) { // if answer was yes
                System.out.println("input was 0");
                initGame(getOtherSign(playerSign));
                startGame();
            }
        }
        return returnableGameOverFlag;
    }

    public char getOtherSign(char sign) {
        return (sign == 'x') ? 'o' : 'x';
    }

    public char getPlayerSign() {
        return playerSign;
    }

    public char[][] getBoard() {
        return board;
    }

    public void setBoard(char[][] board) {
        this.board = board;
    }

    public Bot getOpponent() {
        return opponent;
    }

    public Field[][] getFields() {
        return fields;
    }

    public boolean isGameOver() {
        return gameOver;
    }
}
