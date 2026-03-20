package facility;

import enums.FacilityType;
import human.Human;
import simulation.HotelEventManager;

import javax.swing.*;

public class Lift extends Facility {
    public Lift(FacilityType type, int row, int column, HotelEventManager simC) {
        super(type, row, column, simC);
    }

    @Override
    public boolean isAccessible(Human human) {
        return human.getDestination().getFacility().getLevel() != human.getTile().getFacility().getLevel();
    }

}
