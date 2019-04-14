// Vad Bertalan | Computer Science | UBB | 2nd year | vbim1780

package backend;

import AI.Bot;
import models.Field;
import utils.ConsolePrinter;
import utils.Coord;
import utils.ImageServer;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ClickListener extends MouseAdapter {

    private GameController gameController;
    private Field field;

    private char playerSign;
    private char opponentSign;

    private Bot bot;
    private char[][] board;

    public ClickListener(GameController gameController, Field field) {
        this.gameController = gameController;
        this.field = field;

        playerSign = gameController.getPlayerSign();
        opponentSign = (playerSign == 'x') ? 'o' : 'x';

        bot = gameController.getOpponent();
        board = gameController.getBoard();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // creating a local copy of the gameOver flag
        boolean gameOver = gameController.isGameOver();
        if (gameOver) return;
        board[field.getX()][field.getY()] = playerSign;

        field.getButton().setIcon(new ImageIcon(ImageServer.getImage(playerSign)));
        if (gameController.checkIfLastMoveWasLast()) return;
        field.getButton().removeMouseListener(this);

        ConsolePrinter.printBoardConsole(board);
        ConsolePrinter.drawLine();

        Coord bestMove = bot.findBestMove(board);

        // if move was possible to make (i.e. the bestMove's default -100, -100 values were changed)
        if (bestMove.getX() != -100) {
            board[bestMove.getX()][bestMove.getY()] = opponentSign;

            JButton clickedButtonByOp = gameController.getFields()[bestMove.getX()][bestMove.getY()].getButton();
            clickedButtonByOp.setIcon(new ImageIcon(ImageServer.getImage(opponentSign)));
            if (gameController.checkIfLastMoveWasLast()) return;
            clickedButtonByOp.removeMouseListener(clickedButtonByOp.getMouseListeners()[1]);

            ConsolePrinter.printBoardConsole(board);
            ConsolePrinter.drawLine();
        }

    }
}
