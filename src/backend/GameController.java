// Vad Bertalan | Computer Science | UBB | 2nd year | vbim1780

package backend;

import models.Field;
import utils.ImageServer;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameController {
    private Container contentPane;
    private int n;

    Field[][] fields;

    public GameController(Container contentPane, int n) {
        this.contentPane = contentPane;
        this.n = n;
    }

    public void initFields() {
        fields = new Field[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                JButton button = new JButton();
                button.setBorder(new BevelBorder(BevelBorder.RAISED));
                button.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        button.setIcon(new ImageIcon(ImageServer.getImage()));
                        button.removeMouseListener(this);
                    }
                });
                contentPane.add(button);
                fields[i][j] = new Field(button, i, j);
            }
        }
    }
}
