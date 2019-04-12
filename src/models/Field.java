// Vad Bertalan | Computer Science | UBB | 2nd year | vbim1780

package models;

import javax.swing.*;

public class Field {
    private JButton button;
    private int x;
    private int y;

    public Field(JButton button, int x, int y) {
        this.button = button;
        this.x = x;
        this.y = y;
    }

    public JButton getButton() {
        return button;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
