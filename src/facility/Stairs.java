package facility;
import enums.FacilityType;
import simulation.HotelEventManager;

import javax.swing.*;


public class Stairs extends Facility {
    public Stairs(FacilityType type, int row, int column, HotelEventManager simC) {
        super(type, row, column, simC);
    }
}

