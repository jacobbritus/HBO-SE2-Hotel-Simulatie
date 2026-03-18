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


    public SidebarTab(HotelEventManager hotelEventManager) {
        this.hotelEventManager = hotelEventManager;
    }

    public void addUIdesign() {
        this.setOpaque(false);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    public void addHeaderSection(String title) {
        this.topSection = new JPanel();
        this.topSection.setOpaque(false);
        topSection.setLayout(new BoxLayout(topSection, BoxLayout.Y_AXIS));

        JLabel titleLabel = new MyLabel(title, FontWeight.SEMIBOLD, TextSize.LARGE);
        this.topSection.setBorder(BorderFactory.createCompoundBorder( new MatteBorder(0, 0, 1, 0,
                Settings.themeColor2), new EmptyBorder(20, 20, 20, 0)));
        Dimension size = titleLabel.getPreferredSize();
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        titleLabel.setPreferredSize(new Dimension(size.width + 50, size.height));
        titleLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, size.height));

        topSection.add(titleLabel);
        this.add(topSection);
    }


    public abstract void reactToEvent(HotelEvent hotelEvent);
    public abstract void init();

}
