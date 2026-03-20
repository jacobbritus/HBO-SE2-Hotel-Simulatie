package facility;
import enums.FacilityType;
import simulation.HotelEventManager;

import javax.swing.*;

public class Lobby extends Facility {
    public Lobby(FacilityType type, int row, int column, HotelEventManager simC) {
        super(type, row, column, simC);
    }

//    @Override
//    public void onInteract(Human human) {
//        if ((human.getTile().getFacility() == this)) {
//            if (human.getLifeTime() == null) human.assignRoom(layout);
//            else human.checkOut(layout);
//        }
//    }

}
