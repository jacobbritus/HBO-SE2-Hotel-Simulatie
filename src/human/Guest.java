package human;

import enums.*;
import events.HotelEvent;
import events.HotelEventType;
import facility.Facility;
import facility.Room;
import facility.Tile;
import helper.MyLabel;
import layout.Layout;
import simulation.HotelEventManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayDeque;


public class Guest extends Human {
    private GuestStatus status;

    public Guest(Tile tile, Layout layout, int id, HotelEventManager hotelEventManager) {
        super(tile, layout, Role.GUEST, id, hotelEventManager);
        this.status = GuestStatus.ARRIVED;
        this.getTile().setBackground(this.status.getColor());

    }

    public void addInfo() {
        MyLabel statusLabel = new MyLabel("STATUS: " + this.status, FontWeight.REGULAR, TextSize.SMALL);
        getHotelEventManager().addInfo(statusLabel);
    }

    @Override
    public void mouseEntered() {
        super.mouseEntered();
        addInfo();
    }

    @Override
    public void mouseExited() {
        super.mouseExited();
        this.getTile().setBackground(this.status.getColor());
    }

    @Override
    public void mouseClicked() {
        super.mouseClicked();
        addInfo();
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
    public boolean applyRandomMovement() {
        return this.status != GuestStatus.ARRIVED;
    }

    @Override
    public void assignRoom(Room room) {
        this.setAssignedRoom(room);
        room.setOccupant(this, RoomStatus.UNAVAILABLE);
        this.status = GuestStatus.CHECKED_IN;
        this.getTile().setBackground(Color.GREEN);
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
            setCooldown(100);
            this.getEventQueue().add(hotelEvent);
            return;
        }

        switch (hotelEvent.getEventType()) {
            case CHECK_IN -> {
                if (this.getAssignedRoom() != null) return;
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
            case EVACUATE -> {
                setCooldown(500);
                this.setDestination(this.getLayout().getRandomTile(this.getLayout().getFacilitiesByType(FacilityType.LOBBY).getFirst()));
            }
            default -> {
                return;
            }
        }
        setLatestEvent(hotelEvent);
    }


}
