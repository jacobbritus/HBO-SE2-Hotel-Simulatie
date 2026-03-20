package facility;

import enums.FacilityState;
import enums.FacilityType;
import simulation.HotelEventManager;

import javax.swing.*;
import javax.swing.border.LineBorder;

public class Hall extends Facility {
    public Hall(FacilityType type, int row, int column, HotelEventManager simC) {
        super(type, row, column, simC);

        setBorder(new LineBorder(this.getColor(FacilityState.DEFAULT2), 2));
        setBackground(this.getColor(FacilityState.DEFAULT1));
    }
}