package simulation;

import enums.FontWeight;
import enums.TextSize;
import helper.FontHelper;
import helper.MyLabel;
import settings.Settings;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.util.ArrayList;

public class SidebarSection extends JPanel {
    private ArrayList<JPanel> statRows;

    public SidebarSection(String title) {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setOpaque(false);
        this.setMaximumSize(new Dimension(Integer.MAX_VALUE, 0));

        this.setBorder(BorderFactory.createCompoundBorder(new MatteBorder(1, 0, 1, 0,
        Settings.themeColor2), new EmptyBorder(0, 20, 0, 20)));

        // --- Title ---
        JLabel titleLabel = new MyLabel(title, FontWeight.SEMIBOLD, TextSize.MEDIUM);
//        titleLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        titleLabel.setBorder(new EmptyBorder(0, 0, 10, 0));


        this.add(titleLabel);
    }
}
