package enums;
import events.HotelEventType;
import java.util.Map;

import static java.util.Map.entry;

public enum Statistic {
    GUEST_COUNT(StatisticSection.Hotel, "Guests", UnitType.NUMERICAL, Map.ofEntries(
            entry(HotelEventType.SPAWN_GUEST, +1),
            entry(HotelEventType.CHECK_OUT, -1)
    )),
    ROOMS_OCCUPIED(StatisticSection.Hotel, "Occupied Rooms", UnitType.PERCENTAGE_NEGATIVE,  Map.ofEntries(
            entry(HotelEventType.CHECK_IN, +1),
            entry(HotelEventType.CHECK_OUT, -1)
    )),
    ROOMS_AVAILABLE(StatisticSection.Hotel, "Available Rooms", UnitType.PERCENTAGE_POSITIVE,  Map.ofEntries(
            entry(HotelEventType.CHECK_IN, -1),
            entry(HotelEventType.CHECK_OUT, +1)
    )),

    CLEANER_COUNT(StatisticSection.CLEANING, "Cleaners", UnitType.NUMERICAL,  Map.ofEntries(
            entry(HotelEventType.SPAWN_CLEANER, +1)
    )),
    DIRTY_ROOM_COUNT(StatisticSection.CLEANING, "Dirty Rooms", UnitType.NUMERICAL,  Map.ofEntries(
            entry(HotelEventType.CHECK_OUT, +1)
    )),
    ROOMS_CLEANED_COUNT(StatisticSection.CLEANING, "Rooms Cleaned", UnitType.NUMERICAL,  Map.ofEntries(
            entry(HotelEventType.CLEAN_ROOM, +1)
    )),

    CHECK_INS(StatisticSection.Activity, "Check-ins", UnitType.NUMERICAL,  Map.ofEntries(
            entry(HotelEventType.CHECK_IN, +1)
    )),
    CHECK_OUTS(StatisticSection.Activity, "Check-outs", UnitType.NUMERICAL,  Map.ofEntries(
            entry(HotelEventType.CHECK_OUT, +1)
    )),
    PENDING_EVENTS(StatisticSection.Activity, "Pending Events", UnitType.NUMERICAL, Map.ofEntries(
            entry(HotelEventType.SPAWN_GUEST, -1)
            // add all, im too lazy now
    ));

    private final StatisticSection section;
    private final String title;
    private final UnitType unit;
    private final Map<HotelEventType, Integer> connectedEvents;

    Statistic(StatisticSection section, String title, UnitType unit, Map<HotelEventType, Integer> map) {
        this.connectedEvents = map;
        this.section = section;
        this.title = title;
        this.unit = unit;
    }

    public String getTitle() {
        return this.title;
    }
    public UnitType getUnit() {
        return this.unit;
    }
    public StatisticSection getSection() {
        return this.section;
    }

    public Integer getImpact(HotelEventType eventType) {
        return this.connectedEvents.get(eventType);
    }
}
