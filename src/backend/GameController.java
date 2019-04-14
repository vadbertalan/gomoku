// Vad Bertalan | Computer Science | UBB | 2nd year | vbim1780

package backend;

import AI.Bot;
import AI.Bot3;
import models.Field;
import utils.Coord;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class GameController {
    private Container contentPane;
    private int n;

    private int unclickedFieldNumber;
    private char playerSign;

    private Field[][] fields;

    private char[][] board = {{'.', '.', '.'}, {'.', '.', '.'}, {'.', '.', '.'}};

    private Bot opponent;

    public GameController(Container contentPane, int n) {
        this.contentPane = contentPane;
        this.n = n;
        unclickedFieldNumber = n * n;
        playerSign = 'x';
        fields = new Field[n][n];

        opponent = new Bot3('o');
    }

    public void initFields() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                JButton button = new JButton();
                button.setBorder(new BevelBorder(BevelBorder.RAISED));
                contentPane.add(button);
                fields[i][j] = new Field(button, i, j);
                button.addMouseListener(new ClickListener(this, fields[i][j]));
            }
        }
    }

    public void moveMade() {
        unclickedFieldNumber--;

        char winner = 'N';
        Coord[] winnerCoords = new Coord[3];
        
        for (int i = 0; i < n; i++) {
            // checking rows
            if (board[i][0] != '.' && board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
                winner = board[i][0];
                for (int j = 0; j < 3; j++) {
                    winnerCoords[j] = new Coord(i, j);
                }
                break;
            }
            // checking columns
            if (board[0][i] != '.' && board[0][i] == board[1][i] && board[1][i] == board[2][i]) {
                winner = board[0][i];
                for (int j = 0; j < 3; j++) {
                    winnerCoords[j] = new Coord(j, i);
                }
                break;
            }
        }

        // checking main diagonal
        if (board[0][0] != '.' && board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            winner = board[0][0];
            for (int j = 0; j < 3; j++) {
                winnerCoords[j] = new Coord(j, j);
            }
        } else {
            // checking secondary diagonal
            if (board[0][2] != '.' && board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
                winner = board[0][2];
                for (int j = 0; j < 3; j++) {
                    winnerCoords[j] = new Coord(j, n - j - 1);
                }
            }
        }

        // if we have a clear winner
        if (winner != 'N') {
            // make it visible on the ui
            for (int i = 0; i < 3; i++) {
                Coord winnerCoord = winnerCoords[i];
                fields[winnerCoord.getX()][winnerCoord.getY()].getButton().setBorder(new LineBorder(Color.RED, 5));
            }
            JOptionPane.showInputDialog(Character.toUpperCase(winner) + " won!");
        } else {
            if (unclickedFieldNumber == 0) {
                JOptionPane.showInputDialog("Draw!");
            }
        }
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
}
