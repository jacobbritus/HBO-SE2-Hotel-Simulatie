package events;

import enums.Role;
import human.Guest;

public enum HotelEventType {
    SPAWN_GUEST("Spawn a Guest", null),
    SPAWN_CLEANER("Spawn a Cleaner", null),

    CHECK_IN("Check-In", Role.GUEST),
    CHECK_OUT("Check-Out", Role.GUEST),
    GO_ROOM("Send To Room", Role.GUEST),
    GO_RESTAURANT("Send To Restaurant", Role.GUEST),

    CLEAN_ROOM("Clean Room", Role.CLEANER),
    GO_DIRTY_ROOM("Go To a Dirty Room", Role.CLEANER),
    EVACUATE("Evacuate Everyone", null);

    private final String title;
    private final Role relatedRole;

    HotelEventType(String title, Role role) {
        this.title = title;
        this.relatedRole = role;
    }

    public Role getRelatedRole() {
        return relatedRole;
    }

    public String getTitle() {
        return this.title;
    }
}
