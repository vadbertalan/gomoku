// Vad Bertalan | Computer Science | UBB | 2nd year | vbim1780

package frontend;

import backend.GameController;
import models.Field;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {

    Field[][] fields;
    GameController gameController;

    public GameFrame(int n) {
        setBounds(400, 100, 800, 800);

        Container contentPane = getContentPane();
        contentPane.setLayout(new GridLayout(n,n));

//        gameController = new GameController(contentPane, n);
//        gameController.initGame();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}
