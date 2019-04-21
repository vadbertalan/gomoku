// Vad Bertalan | Computer Science | UBB | 2nd year | vbim1780

import backend.GameController;

public class Main {
    public static void main(String[] args) {
        GameController gameController = new GameController(8, 5);
        gameController.initGame('x');
        gameController.startGame();
//
//        Bot bot = new Bot5(8,'o');
//        long startTime = new Date().getTime();
//        char[][] board = {
//                {'.', 'o', '.', '.', '.', '.', '.', '.' },
//                {'.', '.', 'x', 'x', 'x', '.', '.', '.' },
//                {'.', '.', '.', 'x', 'x', 'o', '.', '.' },
//                {'.', '.', 'x', 'o', 'x', 'x', '.', '.' },
//                {'x', 'o', 'o', 'o', 'o', 'x', '.', '.' },
//                {'.', '.', '.', 'o', 'o', 'x', 'o', '.' },
//                {'.', '.', '.', 'o', '.', '.', '.', '.' },
//                {'.', '.', '.', 'x', '.', '.', '.', '.' }
//        };
//        System.out.println(bot.findBestMove(board));
//        long endTime = new Date().getTime();
//        System.out.println("Got result in: " + ((double)(endTime - startTime))  + " milliseconds.");
    }
}
