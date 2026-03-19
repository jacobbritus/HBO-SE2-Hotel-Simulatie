package human;

import enums.Role;
import events.HotelEvent;
import events.HotelEventListener;
import facility.Facility;
import facility.Room;
import facility.Tile;
import layout.Layout;

import java.awt.Color;
import java.util.*;

public abstract class Human implements RoomOccupant, HotelEventListener {
    private Tile tile;
    private final Layout layout;
    private int stepsTaken;
    private ArrayList<Tile> destinationPath;
    private Tile destination;
    private Room assignedRoom;
    private final Role role;
    private boolean isReadyToDespawn;
    private final int id;
    private final ArrayDeque<HotelEvent> eventQueue;

    public Human(Tile tile, Layout layout, Role role, int id) {
        this.role = role;
        this.id = id;
        this.layout = layout;
        this.isReadyToDespawn = false;
        this.stepsTaken = 0;
        this.eventQueue = new ArrayDeque<>();
        this.tile = tile;
        this.tile.setHuman(this);
    }

    public ArrayDeque<HotelEvent> getEventQueue() {
        return eventQueue;
    }


    public int getId() {
        return id;
    }

    public boolean isReadyToDespawn() {
        return this.isReadyToDespawn;
    }

    public void setReadyToDespawn() {
        this.isReadyToDespawn = true;
    }

    public Role getRole() {
        return this.role;
    }

    public Room getAssignedRoom() {
        return this.assignedRoom;
    }

    public void setAssignedRoom(Room assignedRoom) {
        this.assignedRoom = assignedRoom;
    }

    public Layout getLayout() {
        return layout;
    }

    public Tile getTile() {
        return tile;
    }


    public Tile getDestination() {
        return destination;
    }

    public void setDestinationPath(ArrayList<Tile> destinationPath) {
        this.destinationPath = destinationPath;
    }

    public void setDestination(Tile destination) {
        this.destination = destination;
        this.bfs(destination);
    }

    public void setTile(Tile newTile, Color color) {
        this.tile.setHuman(null);
        this.tile = newTile;
        this.tile.setBackground(color);
        this.tile.setHuman(this);
    }

    public void update() {
        if (this.getDestinationPath() != null) {
            this.move();
        }
    }

    public void onFacilityInteract(Facility facility) {
        if (!this.getEventQueue().isEmpty()) {
            HotelEvent nextEvent = this.getEventQueue().removeFirst();
            this.notify(nextEvent);
        }
    };
    public void move() {
        if (stepsTaken < destinationPath.size() - 1) {
            Tile tile = destinationPath.get(stepsTaken);
            this.setTile(tile, null);
            stepsTaken++;
            stepsTaken++;
            stepsTaken++;
            stepsTaken++;
        } else {
            Facility facility = destination.getFacility();
            this.stepsTaken = 0;
            this.destination = null;
            this.destinationPath = null;
            this.onFacilityInteract(facility);
        }
    }

    public Tile returnLowestfCost(HashSet<Tile> tiles) {
        Integer lowest = null;
        Tile tile = null;
        for (Tile neighbour : tiles) {
            if (neighbour != null) {
                int f_cost = neighbour.getfcost(this.destination);
                if (lowest == null || tile.getfcost(this.destination) < lowest) {
                    tile = neighbour;
                    lowest = f_cost;
                }
            }
        }
        return tile;
    }

    public ArrayList<Tile> getDestinationPath() {
        return destinationPath;
    }

    public void bfs(Tile destination) {
        this.destinationPath = new ArrayList<>();

        HashSet<Tile> open = new HashSet<>();
        HashSet<Tile> closed = new HashSet<>();
        HashMap<Tile, Tile> breadcrumbs = new HashMap<>();

        open.add(this.tile);

        int g_cost = 0;

        while (!open.isEmpty()) {
            Tile current = returnLowestfCost(open);
            open.remove(current);
            closed.add(current);

            if (current == destination) {
                retraceSteps(destination, breadcrumbs);
                break;
            }
            Integer f = null;
            for (Tile neighbour : current.getNeighbours()) {
                if (neighbour == null || closed.contains(neighbour)
                        || neighbour.isWalkable(this)
                        || !accessibleFacility(neighbour) || this.moveFilter(neighbour)) {
                    continue;
                }

                int fneighbour = neighbour.getfcost(destination);

                if (!open.contains(neighbour) && (f == null || f > fneighbour)) {
                    neighbour.increaseGCost(current.getgCost());
                    f = fneighbour;
                    open.add(neighbour);
                    breadcrumbs.put(neighbour, current);
                }
            }

            for (Tile tile : closed) {
                tile.resetGcost();
            }
        }
    }

    public abstract boolean moveFilter(Tile neighbour);

    public boolean accessibleFacility(Tile neighbour) {
        Facility facility = neighbour.getFacility();
        return facility.isAccessible(this);
    }

    public void retraceSteps(Tile destination, HashMap<Tile, Tile> breadcrumbs) {
        Tile step = destination;
        while (step != this.getTile()) {
            step = breadcrumbs.get(step);
            this.destinationPath.add(step);
        }
        Collections.reverse(this.destinationPath);
    }
}


