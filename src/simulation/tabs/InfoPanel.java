package simulation.tabs;

import enums.FontWeight;
import enums.TextSize;
import helper.MyLabel;
import settings.Settings;

import javax.swing.*;
import javax.swing.border.MatteBorder;

public class InfoPanel extends JPanel {
    MyLabel textLabel;
    public InfoPanel() {
        this.setBackground(Settings.themeColor);
        this.setBorder(new MatteBorder(0, 0, 1, 0, Settings.themeColor2));
        this.setOpaque(true);

        this.textLabel = new MyLabel(" ", FontWeight.REGULAR, TextSize.SMALL);
        this.add(textLabel);
    }

    public void setText(String text) {
        this.textLabel.setText(text);
    }
}
