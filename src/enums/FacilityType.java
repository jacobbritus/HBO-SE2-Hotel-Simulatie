package enums;

import facility.*;
import layout.Layout;
import simulation.HotelEventManager;

import java.util.Arrays;
import java.util.Optional;

public enum FacilityType {
    LIFT,
    HALL,
    ROOM,
    LOBBY,
    STAIRS,
    RESTAURANT,
    EMPTY;

    public static Facility constructFacility(String name, Layout parent, int r, int c, HotelEventManager hotelEventManager) {
        FacilityType type = FacilityType.getSafe(name.toUpperCase());

        return switch (type) {
            case ROOM -> new Room(type, r, c, hotelEventManager);
            case LIFT -> new Lift(type, r, c, hotelEventManager);
            case STAIRS -> new Stairs(type, r, c, hotelEventManager);
            case LOBBY -> new Lobby(type, r, c, hotelEventManager);
            case HALL -> new Hall(type, r, c, hotelEventManager);
            case RESTAURANT -> new Restaurant(type, r, c, hotelEventManager);
            default -> null;
        };

    }

    // Check if the provided string value has an associated Facility type
    public static FacilityType getSafe(String name) {
        return Arrays.stream(values())
                .filter(type -> type.name().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

}
