package simulation.tabs;

import enums.FontWeight;
import enums.TextSize;
import events.HotelEvent;
import events.HotelEventType;
import helper.ImageHelper;
import helper.MyButton;
import helper.MyLabel;
import helper.MyScrollPane;
import settings.Settings;
import simulation.HotelEventManager;



import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.util.HashMap;

public class EventsTab extends SidebarTab {
    private JPanel eventsContainer;
    private HashMap<HotelEvent, JPanel> eventHistory;
    private HashMap<HotelEventType, JPanel> eventForm;
    private boolean customizing;

    public EventsTab(HotelEventManager hotelEventManager) {
        super(hotelEventManager);
        addHeaderSection("Events", BoxLayout.X_AXIS);
        addUIdesign();
        eventForm = new HashMap<>();

        MyButton customizeButton = new MyButton("Add", _ -> {
            titleLabel.setText("New Event");
            eventsContainer.removeAll();

            if (customizing) {
                getHotelEventManager().getHTEtimer().start();
                customizing = false;
                addExistingEvents();
            } else {
                hotelEventManager.getHTEtimer().stop();
                customizing = true;
                addNewEvent();
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

    public void addNewEvent() {
        for (HotelEventType type : HotelEventType.values()) {
            HotelEvent event = new HotelEvent(type,
                    hotelEventManager.getEventTicks() + 20,
                    0,
                    0);
            EventPanel eventPanel = new EventPanel (event, true);

            eventPanel.add(Box.createHorizontalGlue());
            MyButton addbutton = new MyButton(null, _ -> {
                hotelEventManager.getHTEtimer().start();
                hotelEventManager.addHotelEvent(eventPanel.returnEvent());
                eventsContainer.removeAll();
                customizing = false;
                addExistingEvents();
            });
            addbutton.setPreferredSize(new Dimension(28, 28));
            addbutton.setMaximumSize(new Dimension(28, 28));
            addbutton.addIcon(ImageHelper.getImage(
                    String.format("../images/%s/addEvent.png", Settings.colorTheme)
            ));
            eventPanel.add(addbutton);
            eventsContainer.add(eventPanel);
            eventForm.put(type, eventPanel);
        }
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
        JPanel eventPanel = new EventPanel(hotelEvent, false);
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

