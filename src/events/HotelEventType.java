package events;

public enum HotelEventType {
    CHECK_IN("Guest Checked In"),
    CHECK_OUT("Guest Checked Out"),
    SPAWN_GUEST("Guest Arrived"),
    ASSIGN_ROOM("Room Assigned"),
    GO_ROOM("Heading to Room"),
    SPAWN_CLEANER("Cleaner On Duty"),
    CLEAN_ROOM("Room Service Complete"),
    GO_DIRTY_ROOM("Cleaning in Progress"),
    GO_RESTAURANT("Heading to Restaurant");

    private final String title;

    HotelEventType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }
}
