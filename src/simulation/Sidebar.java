package simulation;

import enums.SidebarTabType;
import events.HotelEvent;
import events.HotelEventListener;
import human.Human;
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
    private HashMap<SidebarTabType, SidebarTab> pages;
    private SidebarTabType activePage;
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
        this.pages.put(SidebarTabType.EVENTS, new EventsTab(hotelEventManager));
        this.pages.put(SidebarTabType.OVERVIEW, new OverviewTab(hotelEventManager));

        // Show same page one reset
        if (activePage != null) this.openTab(activePage);
        else this.openTab(SidebarTabType.OVERVIEW);
    }

    public void assignEvent(Human human) {
        if (this.activePage != SidebarTabType.EVENTS) this.openTab(SidebarTabType.EVENTS);
        ((EventsTab) this.pages.get(SidebarTabType.EVENTS)).assignEvent(human);
    }

    public void reset() {
        pageHolder.removeAll();
        this.revalidate();
        this.repaint();
    }

    public void start() {
        this.pages.get(SidebarTabType.OVERVIEW).init();
    }

    public void openTab(SidebarTabType page) {
        this.activePage = page;
        pageHolder.removeAll();
        pageHolder.add(this.pages.get(page));

        this.repaint();
        this.revalidate();
    }

    @Override
    public void notify(HotelEvent hotelEvent) {
        this.pages.get(SidebarTabType.OVERVIEW).reactToEvent(hotelEvent);
        this.pages.get(SidebarTabType.EVENTS).reactToEvent(hotelEvent);
    }
}
