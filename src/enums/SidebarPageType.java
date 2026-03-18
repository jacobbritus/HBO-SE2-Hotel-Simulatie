package enums;


public enum SidebarPageType {
//    ROOM("Room "),
//    ROOMS("Rooms Overview"),
//    GUESTS("Guests"),
//    CLEANERS("Cleaners"),
    OVERVIEW("Hotel Overview"),
//    SETTINGS("Settings"),
//    QUIT("Quit Simulation");
    EVENTS("Events");

    public final String title;

    SidebarPageType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }
}
