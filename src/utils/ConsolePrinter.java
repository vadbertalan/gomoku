// Vad Bertalan | Computer Science | UBB | 2nd year | vbim1780

package utils;

public class ConsolePrinter {
    public static void printBoardConsole(char[][] board) {
        int n = board.length;
        for (char[] row : board) {
            for (char item : row) {
                System.out.print("" + item + ' ');
            }
            System.out.println();
        }
    }

    public static void drawLine() {
        System.out.println("---------------------");
    }
}
