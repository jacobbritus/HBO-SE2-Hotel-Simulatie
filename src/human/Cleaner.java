package human;

import enums.FacilityType;
import enums.Role;
import enums.RoomStatus;
import events.HotelEvent;
import facility.Facility;
import facility.Room;
import facility.Tile;
import layout.Layout;

import java.awt.*;

public class Cleaner extends Human {

    public Cleaner(Tile tile, Layout layout, int id) {
        super(tile, layout, Role.CLEANER, id);
        this.getTile().setBackground(Color.BLUE);
    }

    @Override
    public void setTile(Tile newTile, Color color) {
        super.setTile(newTile, Color.BLUE);
    }

    @Override
    public void onFacilityInteract(Facility facility) {
        super.onFacilityInteract(facility);
    }


    @Override
    public boolean moveFilter(Tile neighbour) {
        return false;
    }

    @Override
    public void assignRoom(Room room) {
        this.setAssignedRoom(room);
        room.setOccupant(this, RoomStatus.CLEANING);
    }

    @Override
    public void removeRoom(Room room) {
        this.setAssignedRoom(null);
        room.removeOccupant(RoomStatus.AVAILABLE);
        this.getTile().setBackground(Color.BLUE);
    }

    @Override
    public boolean roomFilter(Room room) {
        return room.getStatus() == RoomStatus.DIRTY;
    }

    @Override
    public void notify(HotelEvent hotelEvent) {
        if (hotelEvent.getHumanId() != null && hotelEvent.getHumanId() != this.getId() && hotelEvent.getData() != 255) return;

        if (this.getDestination() != null) {
            this.getEventQueue().add(hotelEvent);
            return;
        }

        switch (hotelEvent.getEventType()) {
            case GO_DIRTY_ROOM -> {
                Room room = this.getLayout().getRooms()
                        .stream().filter(r -> r.getStatus() == RoomStatus.DIRTY).findFirst().orElse(null);

                if (room != null) {
                    this.assignRoom(room);
                    this.setDestination(this.getLayout().getCenterTile(room));
                }
            }
            case CLEAN_ROOM -> {
                if (this.getAssignedRoom() == null) return;
                this.setDestination(this.getLayout().getRandomTile(this.getLayout().getLobbies().getFirst()));
                this.removeRoom(this.getAssignedRoom());
            }
        }
    }
}


