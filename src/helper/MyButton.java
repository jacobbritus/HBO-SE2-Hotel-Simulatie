package helper;

import enums.FontWeight;
import settings.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MyButton extends JButton {
    private boolean selected;

    public MyButton(String text, ActionListener actionListener) {
        this.selected = false;
        this.setText(text);
        this.setForeground(Settings.textColor);
        this.setFont(FontHelper.getFont(FontWeight.MEDIUM).deriveFont(12f));
        this.setBorderPainted(false);
        this.setFocusPainted(false);
        this.setFocusable(false);
        this.addActionListener(actionListener);

        this.setMaximumSize(new Dimension(200, 50));
        this.setBackground(Settings.themeColor2);
        this.setOpaque(true);

        JButton button = this;
        this.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(Settings.themeColor3);
            }
            public void mouseExited(MouseEvent e) {
                if (selected) button.setBackground(Settings.themeColor);
                else button.setBackground(Settings.themeColor2);
            }
        });
    }

    public void addIcon(Image image) {
        this.setIcon(new ImageIcon(image));
    }

    @Override
    public void setSelected(boolean selected) {
        if (selected) this.setBackground(Settings.themeColor);
        else this.setBackground(Settings.themeColor2);
        this.selected = selected;
    }
}
