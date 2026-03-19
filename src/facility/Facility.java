package facility;

import enums.FacilityState;
import enums.FacilityType;
import human.Human;
import settings.FacilityColors;
import settings.Settings;
import simulation.HotelEventManager;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.util.HashMap;

public abstract class Facility extends JPanel  {
    private final FacilityType type;
    private final int row;
    private final int column;
    private final HotelEventManager hotelEventManager;
    private final int level;
    private MouseAdapter mouseEvents;
    Tile[][] tiles;

    public Facility(JPanel superPanel, FacilityType type, int row, int column, HotelEventManager hotelEventManager) {
        this.type = type;
        this.level = 1;
        this.row = row;
        this.column = column;
        this.setBorder(new LineBorder(this.getColor(FacilityState.DEFAULT2), 2));
        this.setBackground(this.getColor(FacilityState.DEFAULT1));
        this.setOpaque(true);
        this.hotelEventManager = hotelEventManager;
        this.addTiles();
        superPanel.add(this);
        onMouseEvents();
    }

    public void onMouseEvents() {
        Facility facility = this;
        this.mouseEvents = new MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                facility.mouseEntered();
                hotelEventManager.setInfoText(facility.getType().toString());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                facility.mouseExited();
            }
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                facility.mouseClicked();
            }
        };
        this.addMouseListener(this.mouseEvents);
    }

    public void mouseExited () {this.setBorder(new LineBorder(this.getColor(FacilityState.DEFAULT2), 2));}
    public void mouseEntered () { this.setBorder(new LineBorder(Color.YELLOW, 4));}
    public void mouseClicked() {}


    public FacilityType getType() {
        return type;
    }

    public boolean notConnectedTo(Facility facility) {
        return this.getType() == FacilityType.ROOM && facility.getType() == FacilityType.ROOM;
    }

    public int getLevel() {
        return level;
    }

    public boolean isAccessible(Human human) {
        return true;
    }

    public void onInteract(Human human) {}

    public HotelEventManager getSimulationController() {
        return this.hotelEventManager;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public Tile[][] getTiles() {
        return this.tiles;
    }

    public void addTiles() {
        this.setLayout(new GridLayout(Settings.facilityTilesSize, Settings.facilityTilesSize));
        tiles = new Tile[Settings.facilityTilesSize][Settings.facilityTilesSize];

        for (int r = 0; r < Settings.facilityTilesSize; r++) {
            for (int c = 0; c < Settings.facilityTilesSize; c++) {
                Tile tile = new Tile(this,
                        (c + r % 2) % 2 == 0,
                        r, c);
                tiles[r][c] = tile;
            }
        }
    }

    public Color getColor(FacilityState state) {
        HashMap<FacilityState, Color> map = FacilityColors.map.get(this.getType());

        return map.get(state);
    }

    public void reload() {
        for (int r = 0; r < Settings.facilityTilesSize; r++) {
            for (int c = 0; c < Settings.facilityTilesSize; c++) {
                this.add(tiles[r][c]);
            }
        }
    }
}
