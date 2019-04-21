// Vad Bertalan | Computer Science | UBB | 2nd year | vbim1780

package frontend;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {

    public GameFrame(int n) {
        setBounds(400, 100, 800, 800);

        Container contentPane = getContentPane();
        contentPane.setLayout(new GridLayout(n,n));

        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}
