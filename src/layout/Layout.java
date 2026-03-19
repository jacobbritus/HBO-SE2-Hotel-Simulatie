package layout;

import enums.Direction;
import enums.FacilityType;
import enums.RoomStatus;
import facility.*;
import human.Human;
import settings.Settings;
import simulation.HotelEventManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public class Layout extends JPanel {
    private final Facility[][] facilities;
    private Map<FacilityType, ArrayList<Facility>> facilitiesMap;


    public Layout(String[][] rawGrid, HotelEventManager hotelEventManager) {
        int height = rawGrid.length;
        int width = rawGrid[0].length;
        facilities = new Facility[height][width];
        facilitiesMap = new EnumMap<>(FacilityType.class);
        for (FacilityType facilityType : FacilityType.values()) {
            facilitiesMap.put(facilityType, new ArrayList<>());
        }
        // GridLayout voor de oppervlakten
        this.setLayout(new GridLayout(height, width));

        // Zichtbare grootte
        this.setPreferredSize(new Dimension(Settings.oppervlakGrootte * this.facilities[0].length,
                Settings.oppervlakGrootte * this.facilities.length));

        // Add all facilities and connect their tiles
        addFacilities(rawGrid, hotelEventManager);
        connectTiles();
        revalidate();
        repaint();
    }

    public ArrayList<Facility> getFacilitiesByType(FacilityType facilityType) {
        return facilitiesMap.get(facilityType);
    }

    public Room getNearestRoom(Human human) { // Current applies manhattan distance and filters depending on their role.
        Integer lowestDistance = null;
        Room nearestRoom= null;
        Facility current = human.getTile().getFacility();

        for (Facility room : getFacilitiesByType(FacilityType.ROOM)) {
            int c = Math.abs(current.getRow() - room.getRow()) + Math.abs(current.getColumn() - room.getColumn());

            if (human.roomFilter((Room) room) && (lowestDistance == null || c < lowestDistance)) {
                nearestRoom = (Room) room;
                lowestDistance = c;
            }
        }
        return nearestRoom;
    }

    public Facility[][] getFacilities() {
        return facilities;
    }

    public Tile getCenterTile(Facility facility) {
        return facility.getTiles()[Settings.facilityTilesSize/ 2][Settings.facilityTilesSize/ 2];
    }

    public Tile getRandomTile(Facility facility) {
        Facility randomFacility = facility;
        if (facility == null) {
            int r = (int) (Math.random() * this.facilities.length);
            int c = (int) (Math.random() * this.facilities.length);
            randomFacility = this.facilities[r][c];

            while (randomFacility == null) {
                r = (int) (Math.random() * this.facilities.length);
                c = (int) (Math.random() * this.facilities.length);
                randomFacility = this.facilities[r][c];
            }
        }

        int dr = (int) (Math.random() * Settings.facilityTilesSize);
        int dc = (int) (Math.random() * Settings.facilityTilesSize);
        return randomFacility.getTiles()[dr][dc];
    }

    private void addFacilities(String[][] grid, HotelEventManager hotelEventManager) {
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[0].length; c++) {

                String string = grid[r][c];
                FacilityType type = FacilityType.getSafe(string.toUpperCase());
                Facility o = switch (FacilityType.getSafe(string.toUpperCase())) {
                    case ROOM -> new Room(this, type, r, c, hotelEventManager);
                    case LIFT -> new Lift(this, type, r, c, hotelEventManager);
                    case STAIRS -> new Stairs(this, type, r, c, hotelEventManager);
                    case LOBBY -> new Lobby(this, type, r, c, hotelEventManager);
                    case HALL -> new Hall(this, type, r, c, hotelEventManager);
                    default -> null;
                };

                if (o == null) {
                    JPanel inaccessible = new JPanel();
                    inaccessible.setBackground(Settings.achtergrondKleur);
                    this.add(inaccessible);
                    continue;
                } else {
                    facilitiesMap.get(type).add(o);
                }

                facilities[r][c] = o;
                this.add(o);
            }
        }
    }

    public void reload() {
        this.setPreferredSize(new Dimension(Settings.oppervlakGrootte * this.facilities[0].length,
                Settings.oppervlakGrootte * this.facilities.length));
        for (int r = 0; r < this.facilities.length; r++) {
            for (int c = 0; c < this.facilities[0].length; c++) {
                Facility facility = this.facilities[r][c];
                if (facility == null) continue;
                facility.removeAll();
                facility.reload();
                facility.revalidate();

            }
        }

    }
    
    private void connectTiles() {
        for (int r = 0; r < this.facilities.length; r++) {
            for (int c = 0; c < this.facilities[0].length; c++) {
                Facility facility = this.facilities[r][c];
                if (facility == null) continue;
                Tile[][] tiles = facility.getTiles();

                for (int dr = 0; dr < tiles.length; dr++) {
                    for (int dc = 0; dc < tiles[0].length; dc++) {
                        Tile tile = tiles[dr][dc];

                        if (r > 0 && dr == 0) {
                            Facility neighbour = this.facilities[r - 1][c];
                            if (neighbour == null || neighbour.notConnectedTo(facility)) continue; // Handling empty spots
                            tile.setNeighbour(Direction.UP, this.facilities[r - 1][c].getTiles()[tiles.length - 1][dc]);
                        } else if (dr > 0) tile.setNeighbour(Direction.UP, tiles[dr - 1][dc]);

                        if (dr == tiles.length - 1 && r < this.facilities.length - 1) {
                            Facility neighbour = this.facilities[r + 1][c];
                            if (neighbour == null || neighbour.notConnectedTo(facility)) continue;
                            tile.setNeighbour(Direction.DOWN, this.facilities[r + 1][c].getTiles()[0][dc]);
                        }  else if (dr < tiles.length - 1) tile.setNeighbour(Direction.DOWN, tiles[dr +1][dc]);

                        if (c > 0 && dc == 0) {
                            Facility neighbour = this.facilities[r][c - 1];
                            if ((this.facilities[r][c - 1]) == null || neighbour.notConnectedTo(facility)) continue;
                            tile.setNeighbour(Direction.LEFT, this.facilities[r][c - 1].getTiles()[dr][tiles[0].length - 1]);
                        } else if (dc > 0) tile.setNeighbour(Direction.LEFT, tiles[dr][dc - 1]);

                        if (c < this.facilities[0].length - 1 && dc == tiles[0].length - 1) {
                            Facility neighbour = this.facilities[r][c + 1];
                            if (neighbour == null || neighbour.notConnectedTo(facility)) continue;
                            tile.setNeighbour(Direction.RIGHT, facilities[r][c+1].getTiles()[dr][0]);
                        }  else if (dc < tiles[0].length - 1) tile.setNeighbour(Direction.RIGHT, tiles[dr][dc + 1]);
                    }
                }
            }
        }
    }
}