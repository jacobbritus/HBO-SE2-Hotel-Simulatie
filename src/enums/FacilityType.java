package enums;

import facility.Facility;
import layout.Layout;

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

    // Check if the provided string value has an associated Facility type
    public static FacilityType getSafe(String name) {
        return Arrays.stream(values())
                .filter(type -> type.name().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

}
