// Vad Bertalan | Computer Science | UBB | 2nd year | vbim1780

import AI.Bot;
import AI.Bot3;
import backend.GameController;
import frontend.GameFrame;

public class Main {
    public static void main(String[] args) {

        GameController gameController = new GameController(3);
        gameController.initGame();
        gameController.startGame();

        //        Bot bot = new Bot3('o');
//        char[][] board = {
//                { 'x', 'o', '.' },
//                { 'x', '.', '.' },
//                { '.', '.', '.' }
//        };
//        System.out.println(bot.findBestMove(board));

    }
}
