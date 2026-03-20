package facility;

import enums.FacilityState;
import enums.FacilityType;
import enums.RoomStatus;
import human.Human;
import settings.Settings;
import simulation.HotelEventManager;
import simulation.Sidebar;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.event.MouseListener;

//this.onHover = new MouseAdapter() {
//    public void mouseEntered(java.awt.event.MouseEvent evt) {
//        kamer.setBorder(new LineBorder(Color.YELLOW, 2));
//        kamer.getSimulationController().setShowRoom(kamer);
//
//    }
//    public void mouseExited(java.awt.event.MouseEvent evt) {
//        kamer.setBorder(new LineBorder(kamer.getColor(FacilityState.UNAVAILABLE2), 2));
//        kamer.getSimulationController().setShowRoom(null);
//    }
//};
//            this.addMouseListener(this.onHover);

public class Room extends Facility {
    private Human occupant;
    private int roomNumber;
    private RoomStatus status;
    private MouseListener onHover;
    private Color color1;
    private Color color2;

    public Room(FacilityType type, int row, int column, HotelEventManager hotelEventManager) {
        super(type, row, column, hotelEventManager);
        this.setStatus(RoomStatus.AVAILABLE);
    }

    @Override
    public boolean isAccessible(Human human) {
        return  human == this.occupant || human.getTile().getFacility() == this;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public void setStatus(RoomStatus status) {
        this.status = status;
        this.color1 = getColor(FacilityState.getSafe(status + "1"));
        this.color2 = getColor(FacilityState.getSafe(status + "2"));
        this.applyColor();
    }

    public RoomStatus getStatus() {
        return status;
    }

    public Color[] getActiveColors() {
        return new Color[]{this.color1, this.color2};
    }

    public void setOccupant(Human human, RoomStatus status) {
        this.occupant = human;
        this.setStatus(status);
    }

    public void removeOccupant(RoomStatus status) {
        this.occupant = null;
        this.setStatus(status);
    }

    public void applyColor() {
        this.setBorder(new LineBorder(color2, 2));
        this.setBackground(color1);
        for (int r = 0; r < this.tiles.length; r++) {
            for (int c = 0; c <this.tiles[0].length ; c++) {
                Color activeColor;
                Tile tile = this.tiles[r][c];
                if (tile.isEven() || !Settings.setSquaresAlternatingColors) {
                    activeColor = color1;
                } else {
                    activeColor = color2;
                }
                tile.setActiveColor(activeColor);
                tile.setBackground(activeColor);

            }
        }
    }

    @Override
    public void mouseExited() {
        this.setBorder(new LineBorder(color2, 2));
    }

    @Override
    public void mouseClicked() {
        Sidebar sidebar = this.getSimulationController().getSimulationSidebar();

//        if (!sidebar.getOpenedPages().getLast().equals(SidebarPage.ROOM.getTitle()+this.roomNumber)) {
//            sidebar.openNewPage(SidebarPage.ROOM.getTitle()+this.roomNumber);
//        }
    }
}


