package simulation.tabs;
import enums.FontWeight;
import enums.TextSize;
import events.HotelEvent;
import helper.MyLabel;
import settings.Settings;
import simulation.HotelEventManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;

public abstract class SidebarTab extends JPanel {
    JPanel topSection;
    HotelEventManager hotelEventManager;
    JLabel titleLabel;


    public SidebarTab(HotelEventManager hotelEventManager) {
        this.hotelEventManager = hotelEventManager;
    }

    public void addUIdesign() {
        this.setOpaque(false);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    public HotelEventManager getHotelEventManager() {
        return hotelEventManager;
    }

    public void addHeaderSection(String title, int axis) {
        this.topSection = new JPanel();
        this.topSection.setOpaque(false);
        topSection.setLayout(new BoxLayout(topSection, axis));

        titleLabel = new MyLabel(title, FontWeight.SEMIBOLD, TextSize.LARGE);
        this.topSection.setBorder(BorderFactory.createCompoundBorder( new MatteBorder(0, 0, 1, 0,
                Settings.themeColor2), new EmptyBorder(20, 20, 20, 20)));
        Dimension size = titleLabel.getPreferredSize();
        this.topSection.setAlignmentX(Component.LEFT_ALIGNMENT);
//        titleLabel.setBorder(new EmptyBorder(30, 20, 20, 0));
//        titleLabel.setPreferredSize(new Dimension(Integer.MAX_VALUE, 50));
        titleLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        topSection.add(titleLabel);
        this.add(topSection);
    }


    public abstract void reactToEvent(HotelEvent hotelEvent);
    public abstract void init();

}
