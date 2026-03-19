package facility;

import enums.FacilityType;
import simulation.HotelEventManager;

import javax.swing.*;

public class Restaurant extends Facility {
    public Restaurant(JPanel superPanel, FacilityType type, int row, int column, HotelEventManager simC) {
        super(superPanel, type, row, column, simC);
    }
}