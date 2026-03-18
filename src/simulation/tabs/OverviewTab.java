package simulation.tabs;

import enums.*;
import events.HotelEvent;
import helper.MyLabel;
import simulation.HotelEventManager;
import simulation.SidebarSection;
import simulation.StatRow;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class OverviewTab extends SidebarTab {
    private HashMap<Statistic, StatRow> statRows;
    private MyLabel emptyLabel;
    private HashMap<Statistic, Supplier<Integer>> statisticSupplierHashMap;
    private final Map<Statistic, Integer> statsMap = new EnumMap<>(Statistic.class);

    public OverviewTab(HotelEventManager hotelEventManager) {
        super(hotelEventManager);
        addHeaderSection(SidebarPageType.OVERVIEW.getTitle());
        addUIdesign();
        labelOnPreStart();
        initStatValues();
        initializeStatisticSupplierMap();
    }

    public void initializeStatisticSupplierMap() {
        /* 2. Connect stat to supplier which returns the current value for the UI */
        this.statisticSupplierHashMap = new HashMap<>();
        for (Statistic statistic : Statistic.values()) {
            this.statisticSupplierHashMap.put(statistic, () -> statsMap.get(statistic));
        }
        this.statisticSupplierHashMap.put(Statistic.ROOMS_OCCUPIED, () -> validateRoomCountStat(this.hotelEventManager.getRooms().size(), statsMap.get(Statistic.ROOMS_OCCUPIED)));
        this.statisticSupplierHashMap.put(Statistic.ROOMS_AVAILABLE, () -> validateRoomCountStat(this.hotelEventManager.getRooms().size(), statsMap.get(Statistic.ROOMS_AVAILABLE)));
    }

    public void initStatValues() {
        /* 1. Connect stat with value. */
        for (Statistic statistic : Statistic.values()) {
            statsMap.put(statistic, 0);
        }
        statsMap.put(Statistic.ROOMS_AVAILABLE, this.hotelEventManager.getRooms().size());
        statsMap.put(Statistic.PENDING_EVENTS, this.hotelEventManager.getHotelEvents().size());
    }

    public void labelOnPreStart() {
        // Simulation not started
        this.emptyLabel = new MyLabel("Nothing to show yet.", FontWeight.REGULAR, TextSize.SMALL);
        emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
        emptyLabel.setPreferredSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        emptyLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        this.add(emptyLabel);
    }

    @Override
    public void init() {
        initStatValues();
        this.remove(this.emptyLabel);
        this.statRows = new HashMap<>();

        for (StatisticSection section : StatisticSection.values()) {
            JPanel sectionPanel = new SidebarSection(section.getString());
            sectionPanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
            sectionPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
            this.add(sectionPanel);
            sectionPanel.add(Box.createVerticalGlue());
            for (Statistic statistic : Statistic.values()) {
                if (statistic.getSection() != section) continue;
                String title = statistic.getTitle() ;
                Supplier<Integer> supplier = statisticSupplierHashMap.get(statistic);
                StatRow row = new StatRow(title, this, statistic.getUnit(), supplier);
                this.statRows.put(statistic, row);
                sectionPanel.add(row);
                sectionPanel.add(Box.createVerticalGlue());
                for (StatRow stat : statRows.values()) {
                    stat.update();
                }
            }
        }
        this.revalidate();
        this.repaint();
    }

    public int validateRoomCountStat(int allRooms, int statusRooms) {
        if (statusRooms == 0) {
            return 0;
        } else {
            return  statusRooms * 100 / allRooms;
        }

    }

    @Override
    public void reactToEvent(HotelEvent hotelEvent) {
        /* 3. Find stats related to the event, apply the impact on the value, update value in map and update UI */
        for (Statistic stat : Statistic.values()) {
            Integer impact = stat.getImpact(hotelEvent.getEventType());
            if (impact != null) {
                statsMap.merge(stat, impact, Integer::sum);
                statRows.get(stat).update();
            }
        }
    }
}
