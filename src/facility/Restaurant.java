package facility;

import enums.FacilityType;
import simulation.HotelEventManager;

import javax.swing.*;

public class Restaurant extends Facility {
    public Restaurant(FacilityType type, int row, int column, HotelEventManager simC) {
        super(type, row, column, simC);
    }
}