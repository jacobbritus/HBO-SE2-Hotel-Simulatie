package simulation;

import enums.SidebarTabType;
import helper.ImageHelper;
import helper.MyButton;
import settings.Settings;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.util.ArrayList;

public class SidebarNavigationPanel extends JPanel {
    ArrayList<MyButton> buttons;
    public SidebarNavigationPanel(Sidebar sidebar) {
        this.setOpaque(true);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBackground(Settings.themeColor2);
        this.setPreferredSize(new Dimension(Settings.sidebarWidth / 8, Settings.schermHoogte));

        this.setBorder(new MatteBorder(0, 1, 1, 1, Settings.themeColor3));

        MyButton button =new MyButton(null, _ -> sidebar.openTab(SidebarTabType.EVENTS));


        MyButton button2 = new MyButton(null, _ -> {
            sidebar.openTab(SidebarTabType.OVERVIEW);
        });

        button.addIcon(ImageHelper.getImage(
                String.format("../images/%s/event.png", Settings.colorTheme)
        ));
        button2.addIcon(ImageHelper.getImage(
                String.format("../images/%s/home.png", Settings.colorTheme)
        ));

        this.buttons = new ArrayList<>();
        buttons.add(button);
        buttons.add(button2);
        button2.setSelected(true);
        customizeButtons();
        this.add(button2);
//        this.add(Box.createVerticalStrut(10));
        this.add(button);
    }

    public void customizeButtons() {
        for (MyButton b : this.buttons) {
            b.setPreferredSize(new Dimension(32, 32));
            b.addActionListener(_ -> {
                b.setSelected(true);
                for (MyButton other : this.buttons) {
                    if (b == other) continue;
                    other.setSelected(false);
                }
            });
        }
    }
}
