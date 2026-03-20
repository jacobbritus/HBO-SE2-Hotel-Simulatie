package simulation.tabs;

import events.HotelEvent;
import events.HotelEventListener;
import events.HotelEventType;
import helper.MyButton;
import helper.MyScrollPane;
import human.Human;
import settings.Settings;
import simulation.HotelEventManager;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class EventsTab extends SidebarTab {
    private JPanel eventsContainer;
    private HashMap<HotelEvent, JPanel> eventHistory;
    private HashMap<HotelEventType, JPanel> eventForm;
    private boolean customizing;
    private MyButton customizeButton;
    private Human selectedHuman;

    public EventsTab(HotelEventManager hotelEventManager) {
        super(hotelEventManager);
        addHeaderSection("Events", BoxLayout.X_AXIS);
        addUIdesign();
        eventForm = new HashMap<>();

        customizeButton = new MyButton("Add", null);
        customizeButton.addActionListener(_ -> {
            eventsContainer.removeAll();

            if (customizing) {
                customizeButton.setText("Add");
                titleLabel.setText("Events");
                if(hotelEventManager.isStarted()) hotelEventManager.getHTEtimer().start();
                customizing = false;
                selectedHuman = null;
                addExistingEvents();
            } else {
                if (selectedHuman == null) titleLabel.setText("New Event");
                else titleLabel.setText(selectedHuman.getRole() + " " + selectedHuman.getId());
                customizeButton.setText("Cancel");
                if(hotelEventManager.isStarted()) hotelEventManager.getHTEtimer().stop();
                customizing = true;
                addNewEventOptions();
            }



            eventsContainer.revalidate();
            eventsContainer.repaint();
        });

        customizeButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
        this.topSection.add(customizeButton
        );

        JScrollPane scrollPane = createScrollPanel();
        scrollPane.setPreferredSize(new Dimension(320, 320));
        this.add(scrollPane);
        addExistingEvents();

        this.repaint();
        this.revalidate();
    }

    public void assignEvent(Human human) {
        this.selectedHuman = human;
        customizing = false;
        customizeButton.doClick();
    }

    public void addNewEventOptions() {
        int id = selectedHuman != null ? selectedHuman.getId() : 0;
        for (HotelEventType type : HotelEventType.values()) {
            if (selectedHuman != null && type.getRelatedRole() != selectedHuman.getRole()) continue;
            HotelEvent event = new HotelEvent(type,
                    hotelEventManager.getEventTicks() + 20,
                    id,
                    0);
            EventPanel eventPanel = new EventPanel (event, true, this, selectedHuman);
            eventsContainer.add(eventPanel);
            eventForm.put(type, eventPanel);
        }
    }

    public void addNewEvent(HotelEvent event) {
        if(hotelEventManager.isStarted()) hotelEventManager.getHTEtimer().start();
        hotelEventManager.addHotelEvent(event);
        eventsContainer.removeAll();
        customizeButton.doClick();
        SwingUtilities.getWindowAncestor(this).requestFocus();
    }

    @Override
    public HotelEventManager getHotelEventManager() {
        return super.getHotelEventManager();
    }

    public void removeEvent(HotelEvent event) {
        hotelEventManager.removeHotelEvent(event);
        eventsContainer.removeAll();
        addExistingEvents();
        eventsContainer.revalidate();
        eventsContainer.repaint();
    }

    public void addExistingEvents() {
        this.eventHistory = new HashMap<>();
        for (HotelEvent e : hotelEventManager.getHotelEvents()){
            reactToEvent(e);
        }
    }

    public MyScrollPane createScrollPanel() {
        this.eventsContainer = new JPanel();
        eventsContainer.setLayout(new BoxLayout(eventsContainer, BoxLayout.Y_AXIS)); // vertical list
        eventsContainer.setAlignmentX(Component.LEFT_ALIGNMENT);

        eventsContainer.setBackground(Settings.themeColor3);
        eventsContainer.setOpaque(true);
        MyScrollPane scrollPane = new MyScrollPane(eventsContainer);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);


        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        return scrollPane;
    }

    @Override
    public void init() {}

    @Override
    public void reactToEvent(HotelEvent hotelEvent) {
        JPanel eventPanel = new EventPanel(hotelEvent, false, this, selectedHuman);
        if (!eventHistory.containsKey(hotelEvent) && hotelEvent.getTime() >= getHotelEventManager().getEventTicks() ) {
            this.eventsContainer.add(eventPanel);
        } else {
            JPanel pastPanel;
            if (eventHistory.containsKey(hotelEvent)) {
                pastPanel = eventHistory.get(hotelEvent);
                this.eventsContainer.remove(pastPanel);
            } {
                pastEvent(eventPanel);
                pastPanel = eventPanel;
            }
            pastEvent(pastPanel);
            this.eventsContainer.add(pastPanel);
        }
        eventHistory.put(hotelEvent, eventPanel);


        eventsContainer.revalidate();
        eventsContainer.repaint();

    }

    public void pastEvent(JPanel eventPanel) {
        eventPanel.setBackground(Settings.themeColor2);
        for (Component label: eventPanel.getComponents()) {
            label.setForeground(Settings.textColor2);
        }
    }


}

