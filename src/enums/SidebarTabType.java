package enums;


public enum SidebarTabType {
//    ROOM("Room "),
//    ROOMS("Rooms Overview"),
//    GUESTS("Guests"),
//    CLEANERS("Cleaners"),
    OVERVIEW("Hotel Overview"),
//    SETTINGS("Settings"),
//    QUIT("Quit Simulation");
    EVENTS("Events");

    public final String title;

    SidebarTabType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }
}
