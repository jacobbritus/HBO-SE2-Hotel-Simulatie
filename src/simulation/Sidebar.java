package simulation;

import enums.SidebarPageType;
import events.HotelEvent;
import events.HotelEventListener;
import settings.Settings;
import simulation.tabs.EventsTab;
import simulation.tabs.OverviewTab;
import simulation.tabs.SidebarTab;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.util.HashMap;

public class Sidebar extends JPanel implements HotelEventListener {
    private JLabel emptyLabel;
    private final JPanel pageHolder;
    private HashMap<SidebarPageType, SidebarTab> pages;
    private SidebarPageType activePage;
    boolean visible;

    public Sidebar() {
        this.setBackground(Settings.themeColor);
        this.setBorder(new MatteBorder(0, 0, 0, 1, Settings.themeColor2));
        this.setPreferredSize(new Dimension(Settings.sidebarWidth, Settings.schermHoogte));
        this.visible = true;
        this.setLayout(new BorderLayout());
        this.pageHolder = new JPanel(new BorderLayout());
        this.pageHolder.setOpaque(false);
        this.add(pageHolder);

        SidebarNavigationPanel sidebarNavigationPanel = new SidebarNavigationPanel(this);
        this.add(sidebarNavigationPanel, BorderLayout.WEST);
    }

    public boolean toggle() {
        if (this.visible) {
            this.setPreferredSize(new Dimension(0, Settings.schermHoogte));
            this.visible = false;
        } else {
            this.visible = true;
            this.setPreferredSize(new Dimension(Settings.sidebarWidth, Settings.schermHoogte));
        }
        this.revalidate();
        this.repaint();
        return this.visible;
    }

    public void init(HotelEventManager hotelEventManager) {
        this.pages = new HashMap<>();
        this.pages.put(SidebarPageType.EVENTS, new EventsTab(hotelEventManager));
        this.pages.put(SidebarPageType.OVERVIEW, new OverviewTab(hotelEventManager));

        // Show same page one reset
        if (activePage != null) this.openTab(activePage);
        else this.openTab(SidebarPageType.OVERVIEW);
    }

    public void reset() {
        pageHolder.removeAll();
        this.revalidate();
        this.repaint();
    }

    public void start() {
        this.pages.get(SidebarPageType.OVERVIEW).init();
    }

    public void openTab(SidebarPageType page) {
        this.activePage = page;
        pageHolder.removeAll();
        pageHolder.add(this.pages.get(page));

        this.repaint();
        this.revalidate();
    }

    @Override
    public void notify(HotelEvent hotelEvent) {
        this.pages.get(SidebarPageType.OVERVIEW).reactToEvent(hotelEvent);
        this.pages.get(SidebarPageType.EVENTS).reactToEvent(hotelEvent);
    }
}
