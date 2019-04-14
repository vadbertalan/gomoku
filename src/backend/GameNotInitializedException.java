// Vad Bertalan | Computer Science | UBB | 2nd year | vbim1780

package backend;

public class GameNotInitializedException extends RuntimeException {
    public GameNotInitializedException() {
        super("Game not initialized!");
    }

    public GameNotInitializedException(String message) {
        super(message);
    }
}
