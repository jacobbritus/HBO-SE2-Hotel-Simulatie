package simulation;

import enums.FacilityType;
import enums.FontWeight;
import enums.TextSize;
import events.HotelEvent;
import events.HotelEventListener;
import events.HotelEventType;
import facility.Facility;
import helper.ImageHelper;
import helper.MyButton;
import helper.MyLabel;
import settings.Settings;
import simulation.tabs.InfoPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class HotelEventManager extends JPanel {
    private final Simulation simulation;
    private final Sidebar sidebar;
    private final InfoPanel infoPanel;

    private Timer HTEtimer;
    private MyLabel speedMultiplierLabel;
    private final JLabel timeLabel;
    private final JLabel ticksLabel;
    private int clockTime;
    private boolean paused;
    private boolean started;
    public final int[] speedMultipliers = {1, 2, 4, 8, 16, 32};
    public int activeSpeedMultiplier = 0;
    private ArrayList<HotelEvent> hotelEvents;

    private int eventTicks;
    private final ArrayList<HotelEventListener> hotelEventListeners;

    public HotelEventManager(Simulation simulation, Sidebar sidebar, InfoPanel infoPanel) {
        this.setBackground(Settings.themeColor);
        this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        this.setPreferredSize(new Dimension(0, 48));
        this.setBorder(BorderFactory.createCompoundBorder(new MatteBorder(0, 0, 1, 0,
                Settings.themeColor2), new EmptyBorder(10, 9, 10, 10)));
        this.clockTime = 60 * 60 * 12;
        this.eventTicks = 0;
        this.simulation = simulation;
        this.infoPanel = infoPanel;
        this.sidebar = sidebar;
        this.timeLabel = new MyLabel(Settings.convertTime(clockTime), FontWeight.MEDIUM, TextSize.SMALL);
        this.ticksLabel = new MyLabel("Ticks: 0", FontWeight.MEDIUM, TextSize.SMALL);
        ticksLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        ticksLabel.setHorizontalAlignment(SwingConstants.CENTER);

        hotelEventListeners  = new ArrayList<>();
        hotelEventListeners.add(this.simulation);
        hotelEventListeners.add(this.sidebar);

        initializeTimer();
        addControlButtons();
    }

    public ArrayList<HotelEvent> getHotelEvents() {
        return hotelEvents;
    }

    public Timer getHTEtimer() {
        return HTEtimer;
    }

    public int getEventTicks() {
        return eventTicks;
    }

    public void addHotelEvent(HotelEvent hotelEvent) {
        this.hotelEvents.add(hotelEvent);
        hotelEvents.sort(Comparator.comparing(HotelEvent::getTime));
    }

    public void setInfoText(String text) {
        this.infoPanel.setText(text);
    }


    public ArrayList<Facility> getRooms() {
        return this.simulation.returnLayout().getFacilitiesByType(FacilityType.ROOM);
    }

    public void initializeTimer() {
        this.hotelEvents = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            hotelEvents.add(new HotelEvent(HotelEventType.SPAWN_GUEST, 3, i, 0));

//
        }


        hotelEvents.sort(Comparator.comparing(HotelEvent::getTime));


        this.HTEtimer = new Timer(Settings.delay, e -> {
            this.clockTime += (1000 / Settings.delay);
            this.eventTicks++;
            this.timeLabel.setText(Settings.convertTime(this.clockTime));
            this.ticksLabel.setText("Ticks: " + this.eventTicks);

            for (HotelEvent event : hotelEvents) {
                if (event.getTime() == this.eventTicks) {
                    for (HotelEventListener listener : hotelEventListeners) {
                        listener.notify(event);
                    }
                    if (event.getEventType() == HotelEventType.SPAWN_GUEST || event.getEventType() == HotelEventType.SPAWN_CLEANER) {
                        simulation.getHumans().stream()
                                .filter(h -> !this.hotelEventListeners.contains(h))
                                .forEach(this::registerListener);
                    }
                }
            }
            simulation.updateHumans();

        });
    }

    public void registerListener(HotelEventListener hotelEventListener) {
        this.hotelEventListeners.add(hotelEventListener);
    }

    public void removeListener(HotelEventListener hotelEventListener) {
        this.hotelEventListeners.remove(hotelEventListener);
    }

    private void resetSimulation(MyButton startButton, MyButton pauseButton) {
        this.simulation.reset();
        this.clockTime = 0;
        this.eventTicks = 0;
        this.HTEtimer.stop();
        this.timeLabel.setText("00:00:00");
        this.ticksLabel.setText("Ticks: 0");
        this.sidebar.reset();
        this.sidebar.init(this);
        this.started = false;

        startButton.setForeground(new Color(99, 196, 74,255));
        pauseButton.setForeground(Color.DARK_GRAY);
        startButton.setText("Start");
    }

    private void startSimulation(MyButton startButton, MyButton pauseButton) {
        this.started = true;
        this.sidebar.start();
        this.HTEtimer.start();

        startButton.setText("Reset");
        startButton.setForeground(Color.RED);
        pauseButton.setForeground(Settings.textColor);
    }

    public void updateSpeed() {
        Settings.delay = 1000 / this.speedMultipliers[activeSpeedMultiplier];
        this.speedMultiplierLabel.setText(this.speedMultipliers[this.activeSpeedMultiplier] + "x");
        this.HTEtimer.setDelay(Settings.delay);
    }

    public void addControlButtons() {
        MyButton menuButton = new MyButton(null, null);

        menuButton.addIcon(ImageHelper.getImage(
                String.format("../images/%s/dockSidebar.png", Settings.colorTheme)
        ));
        menuButton.setPreferredSize(new Dimension(48, 48));

        menuButton.addActionListener( e -> sidebar.toggle());

        this.add(menuButton);
        this.add(Box.createVerticalStrut(20)); // 5px gap

        this.speedMultiplierLabel = new MyLabel(this.speedMultipliers[this.activeSpeedMultiplier] + "x",
                FontWeight.MEDIUM, TextSize.SMALL );

        MyButton decreaseSpeedButton = new MyButton("-", e -> {
            this.activeSpeedMultiplier = Math.max(this.activeSpeedMultiplier - 1, 0);
            updateSpeed();
        });
        decreaseSpeedButton.setPreferredSize(new Dimension( 50, 20));

        this.add(decreaseSpeedButton);

        this.add(Box.createHorizontalStrut(20));
        this.add(speedMultiplierLabel);
        this.add(Box.createHorizontalStrut(20));

        MyButton increaseSpeedButton = new MyButton("+", e -> {
            this.activeSpeedMultiplier = Math.min(this.activeSpeedMultiplier + 1, this.speedMultipliers.length - 1);
            updateSpeed();
        });
        increaseSpeedButton.setPreferredSize(new Dimension( 50, 20));
        this.add(increaseSpeedButton);



        MyButton pauseButton = createPauseButton();
        MyButton startButton = createStartButton(pauseButton);

        this.add(Box.createHorizontalStrut(20));
        this.add(ticksLabel);
        this.add(Box.createHorizontalStrut(20));
        this.add(timeLabel);
        this.add(Box.createHorizontalStrut(25));
        this.add(startButton);
        this.add(pauseButton);
    }

    private MyButton createPauseButton() {
        MyButton pauseButton = new MyButton("Pause", null);
        pauseButton.addActionListener(e -> {
            if (!started) return;
            if (this.paused) {
                pauseButton.setText("Pause");
                this.HTEtimer.start();
                this.paused = false;
            } else {
                pauseButton.setText("Resume");
                this.HTEtimer.stop();
                this.paused = true;
            }
        });
        pauseButton.setForeground(Color.DARK_GRAY);
        return pauseButton;
    }

    private MyButton createStartButton(MyButton pauseButton) {
        MyButton startButton = new MyButton("Start", null);
        startButton.setForeground(new Color(99, 196, 74,255));
        startButton.addActionListener(e -> {
            if (this.started) resetSimulation(startButton, pauseButton);
            else startSimulation(startButton, pauseButton);
        });
        return startButton;
    }

    public Sidebar getSimulationSidebar() {
        return sidebar;
    }
}





