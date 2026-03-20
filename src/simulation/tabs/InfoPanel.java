package simulation.tabs;

import enums.FontWeight;
import enums.TextSize;
import helper.MyLabel;
import settings.Settings;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;

public class InfoPanel extends JPanel {
    JPanel activePanel;
    public InfoPanel() {
        this.setBackground(Settings.themeColor);
        this.setBorder(new MatteBorder(0, 0, 1, 0, Settings.themeColor2));
        this.setLayout(new BorderLayout());
        this.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        this.setPreferredSize(new Dimension(300, 32));
        this.setOpaque(true);
    }

    public void setInfo(JPanel panel) {
        if (panel != null) {
            activePanel = panel;
            this.add(activePanel);
        } else {
            removeAll();
        }
        this.revalidate();
        this.repaint();
    }

    public void addInfo(MyLabel myLabel) {
        if (activePanel != null) {
            activePanel.add(myLabel);
        }
    }
}
