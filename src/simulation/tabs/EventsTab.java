package simulation.tabs;

import enums.FontWeight;
import enums.TextSize;
import events.HotelEvent;
import helper.MyLabel;
import helper.MyScrollPane;
import settings.Settings;
import simulation.HotelEventManager;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.util.HashMap;

public class EventsTab extends SidebarTab {
    private JPanel eventsPanel;
    private final HashMap<HotelEvent, JPanel> eventHistory;

    public EventsTab(HotelEventManager hotelEventManager) {
        super(hotelEventManager);
        addHeaderSection("Events");
        addUIdesign();
        this.eventHistory = new HashMap<>();
        JScrollPane scrollPane = createScrollPanel();
        this.add(scrollPane);

        for (HotelEvent hotelEvent : hotelEventManager.getHotelEvents()){
            reactToEvent(hotelEvent);
        }

        this.repaint();
        this.revalidate();
    }

    public MyScrollPane createScrollPanel() {
        this.eventsPanel = new JPanel();
        eventsPanel.setLayout(new BoxLayout(eventsPanel, BoxLayout.Y_AXIS)); // vertical list
        eventsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        eventsPanel.setBackground(Settings.themeColor3);
        eventsPanel.setOpaque(true);
        MyScrollPane scrollPane = new MyScrollPane(eventsPanel);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        return scrollPane;
    }

    @Override
    public void init() {}

    @Override
    public void reactToEvent(HotelEvent hotelEvent) {
        JPanel eventPanel = createEventPanel(hotelEvent);
        if (!eventHistory.containsKey(hotelEvent)) {
            this.eventsPanel.add(eventPanel);
        } else {
            JPanel pastPanel = eventHistory.get(hotelEvent);
            this.eventsPanel.remove(pastPanel);
            pastEvent(pastPanel);
            this.eventsPanel.add(pastPanel);
        }
        eventHistory.put(hotelEvent, eventPanel);
        eventsPanel.revalidate();
        eventsPanel.repaint();

    }

    public void pastEvent(JPanel eventPanel) {
        eventPanel.setBackground(Settings.themeColor2);
        for (Component label: eventPanel.getComponents()) {
            label.setForeground(Settings.textColor2);
        }
    }

    public JPanel createEventPanel(HotelEvent hotelEvent) {
        JPanel eventPanel = new JPanel();
        eventPanel.setLayout(new BoxLayout(eventPanel, BoxLayout.X_AXIS));
        eventPanel.setBorder(new LineBorder(Color.RED, 4));

        eventPanel.setOpaque(true);
        eventPanel.setBackground(Settings.themeColor);

        JLabel title = new MyLabel(hotelEvent.getEventType().getTitle(), FontWeight.MEDIUM, TextSize.SMALL);
        eventPanel.add(title);
        title.setPreferredSize(new Dimension(this.getPreferredSize().width   , 40));

        eventPanel.add(Box.createHorizontalGlue());
        JLabel a = new MyLabel("ID: " +hotelEvent.getHumanId().toString(), FontWeight.MEDIUM, TextSize.SMALL);
        eventPanel.add(a);

        eventPanel.add(Box.createHorizontalGlue());
        JLabel b = new MyLabel("Tick: " + String.valueOf(hotelEvent.getTime()), FontWeight.MEDIUM, TextSize.SMALL);
        eventPanel.add(b);

        eventPanel.setBorder(BorderFactory.createCompoundBorder(new MatteBorder(0, 0, 1, 0,
                Settings.themeColor2), new EmptyBorder(20, 20, 20, 20)));

        return eventPanel;
    }

}

