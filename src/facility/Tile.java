package facility;

import enums.Direction;
import enums.FacilityState;
import human.Human;
import settings.Settings;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Tile extends JLabel {
    private final Facility facility;
    private final int row;
    private final int column;
    private Color color;
    private final boolean isEven;
    private Human human;
    private int gCost = 0;
    private int hCost;
    private final HashMap<Direction, Tile> neighbours;

    public Tile(Facility facility, boolean isEven, int row, int column) {
        this.gCost = 0;
        this.hCost = 0;
        this.row = row;
        this.column = column;
        this.facility = facility;
        this.neighbours = new HashMap<>();
        this.neighbours.put(Direction.UP, null);
        this.neighbours.put(Direction.DOWN, null);
        this.neighbours.put(Direction.LEFT, null);
        this.neighbours.put(Direction.RIGHT, null);
        this.setOpaque(true);
        this.isEven = isEven;
        if (isEven || !Settings.setSquaresAlternatingColors) {
            this.setBackground(facility.getColor(FacilityState.DEFAULT1));
            this.color = facility.getColor(FacilityState.DEFAULT1);
        } else {
            this.setBackground(facility.getColor(FacilityState.DEFAULT2));
            this.color = facility.getColor(FacilityState.DEFAULT2);
        }
    }

    public void increaseGCost(int currentCost) {
        this.gCost += currentCost;
    }

    public void resetGcost() {
        gCost = 0;
    }

    public int getgCost() {
        return gCost;
    }

    public boolean isEven() {
        return isEven;
    }

    public Human getHuman() {
        return this.human;
    }

    public void setActiveColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return this.color;
    }

    public boolean isWalkable(Human newHuman) {
        return
//                human == null || this.facility == null ||
                         newHuman.getDestination().getFacility().getLevel() != this.getFacility().getLevel();
    }

    public int[] getGlobalPosition() {
        return new int[]{
                (this.getFacility().getRow() * Settings.facilityTilesSize) + this.row,
                (this.getFacility().getColumn() * Settings.facilityTilesSize) + this.column
        };
    }

    public int getfcost(Tile destination) {
        int hCost = Math.abs(this.getGlobalPosition()[0] - destination.getGlobalPosition()[0]) + Math.abs(this.getGlobalPosition()[1] - destination.getGlobalPosition()[1]);

        return this.gCost + hCost;
    }

    public int getRow() {
        return row;
    }

    public void revertColor() {
        this.setBackground(this.color);
    }

    public int getColumn() {
        return column;
    }

    public HashSet<Tile> getNeighbours() {

        return new HashSet<>(neighbours.values());
    }


    public Facility getFacility() {
        return facility;
    }

    public void setHuman(Human human) {
        if (human == null) {
            revertColor();
        }
        this.human = human;
    }

    public void setNeighbour(Direction direction, Tile tile) {
            neighbours.put(direction, tile);
    }
}
