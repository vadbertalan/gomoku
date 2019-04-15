// Vad Bertalan | Computer Science | UBB | 2nd year | vbim1780

package backend;

import AI.Bot;
import AI.Bot3;
import AI.Bot4;
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
    private static final char[][] EMPTY_BOARD3 = {{'.', '.', '.'}, {'.', '.', '.'}, {'.', '.', '.'}};
    private static final char[][] EMPTY_BOARD4 = {{'.', '.', '.', '.'}, {'.', '.', '.', '.'}, {'.', '.', '.', '.'}, {'.', '.', '.', '.'}};

    private JFrame gameFrame;
    private int n;

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
        this.n = n;
        gameInitialized = false;
        board = new char[n][n];
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
//            case 5: opponent = new Bot4(opponentSign); break;
            default: throw new IllegalArgumentException();
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
                try {
                    java.lang.reflect.Field emptyBoardField = GameController.class.getDeclaredField("EMPTY_BOARD" + n);
                    board[i][j] = ((char[][]) emptyBoardField.get(this))[i][j];
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
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
            clickedButtonByOp.setIcon(new ImageIcon(ImageServer.getImage(opponentSign)));
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
            Method checkerMethod = null;
            checkerMethod = GameController.class.getDeclaredMethod("checkForGameOver" + n);
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

    private boolean processOutcome(char winner, Coord[] winnerCoords) {
        String outputMessage = "";

        // if we have a clear winner
        if (winner != 'N') {
            // current game is over
            gameOver = true;

            // make it visible on the ui
            for (int i = 0; i < n; i++) {
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

    public void setPlayerSign(char playerSign) {
        this.playerSign = playerSign;
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
