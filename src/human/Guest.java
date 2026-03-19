package human;

import enums.FacilityType;
import enums.GuestStatus;
import enums.Role;
import enums.RoomStatus;
import events.HotelEvent;
import events.HotelEventType;
import facility.Facility;
import facility.Restaurant;
import facility.Room;
import facility.Tile;
import layout.Layout;

import java.awt.*;
import java.util.ArrayDeque;


public class Guest extends Human {
    private GuestStatus status;

    public Guest(Tile tile, Layout layout, int id) {
        super(tile, layout, Role.GUEST, id);
        this.status = GuestStatus.ARRIVED;
        this.getTile().setBackground(this.status.getColor());

    }

    @Override
    public void setTile(Tile newTile, Color color) {
        super.setTile(newTile, this.status.getColor());
    }

    @Override
    public void onFacilityInteract(Facility facility) {
        if (facility.getType() == FacilityType.LOBBY && this.status == GuestStatus.CHECKING_OUT) {
            this.setReadyToDespawn();
            this.getTile().revertColor();
        }

        super.onFacilityInteract(facility);
    }

    @Override
    public boolean moveFilter(Tile neighbour) {
        return false;
    }

    @Override
    public void assignRoom(Room room) {
        this.setAssignedRoom(room);
        room.setOccupant(this, RoomStatus.UNAVAILABLE);
        this.status = GuestStatus.CHECKED_IN;
    }

    public void removeRoom(Room room) {
        this.setAssignedRoom(null);
        room.removeOccupant(RoomStatus.DIRTY);
        this.status = GuestStatus.CHECKING_OUT;
    }

    @Override
    public boolean roomFilter(Room room) {
        return (room.getStatus() == RoomStatus.AVAILABLE);
    }

    @Override
    public void notify(HotelEvent hotelEvent) {
        if (hotelEvent.getHumanId() != null && hotelEvent.getHumanId() != this.getId() && hotelEvent.getData() != 255) return;

        if (this.getDestination() != null) {
            this.getEventQueue().add(hotelEvent);
            return;
        }

        switch (hotelEvent.getEventType()) {
            case ASSIGN_ROOM, CHECK_IN -> {
                Room nearestRoom = this.getLayout().getNearestRoom(this);
                if (nearestRoom == null) {
                    return;
                }
                this.assignRoom(nearestRoom);
            }
            case GO_ROOM -> {
                if (this.getAssignedRoom() == null) return;
                this.setDestination(this.getLayout().getCenterTile(this.getAssignedRoom()));
            }
            case GO_RESTAURANT -> {
                Facility restaurant = this.getLayout().getFacilitiesByType(FacilityType.RESTAURANT).getFirst();
                this.setDestination(this.getLayout().getCenterTile(restaurant));
                System.out.println(this.getDestination().getFacility());
            }
            case CHECK_OUT -> {
                if (this.getAssignedRoom() == null) return;
                this.setDestination(this.getLayout().getRandomTile(this.getLayout().getFacilitiesByType(FacilityType.LOBBY).getFirst()));
                this.removeRoom(this.getAssignedRoom());
            }
        }
    }
}
