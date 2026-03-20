package facility;

import enums.FacilityState;
import enums.FacilityType;
import simulation.HotelEventManager;

import javax.swing.*;
import javax.swing.border.LineBorder;

public class Hall extends Facility {
    public Hall(JPanel superPanel, FacilityType type, int row, int column, HotelEventManager simC) {
        super(superPanel, type, row, column, simC);

        setBorder(null);
    }

    @Override
    public void mouseExited () {this.setBorder(new LineBorder(this.getColor(FacilityState.DEFAULT1), 2));}



}