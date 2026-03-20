package events;

public enum HotelEventType {
    SPAWN_GUEST("Guest Arrived"),
    CHECK_IN("Guest Checked In"),
    CHECK_OUT("Guest Checked Out"),
    ASSIGN_ROOM("Room Assigned"),
    GO_ROOM("Heading to Room"),
    SPAWN_CLEANER("Cleaner On Duty"),
    CLEAN_ROOM("Room Service Complete"),
    GO_DIRTY_ROOM("Cleaning in Progress"),
    GO_RESTAURANT("Heading to Restaurant"),
    EVACUATE("Evacuate Everyone");

    private final String title;

    HotelEventType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }
}
