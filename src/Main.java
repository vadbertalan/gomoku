// Vad Bertalan | Computer Science | UBB | 2nd year | vbim1780

import AI.Bot;
import AI.Bot3;
import AI.Bot4;
import backend.GameController;

import java.util.Date;

public class Main {
    public static void main(String[] args) {

        GameController gameController = new GameController(4);
        gameController.initGame('x');
        gameController.startGame();

        Bot bot = new Bot4('o');
        long startTime = new Date().getTime();
        char[][] board = {
                { 'o', '.', '.', 'x' },
                { '.', '.', 'o', 'o' },
                { '.', 'x', '.', '.' },
                { 'x', 'x', 'o', 'x' }
        };
        System.out.println(bot.findBestMove(board));
        long endTime = new Date().getTime();
        System.out.println("Got result in: " + ((double)(endTime - startTime))  + " seconds.");
    }
}
