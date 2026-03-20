package human;

import enums.Role;
import events.HotelEvent;
import events.HotelEventListener;
import events.HotelEventType;
import facility.Facility;
import facility.Room;
import facility.Tile;
import facility.mouseInteractions;
import layout.Layout;
import settings.Settings;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.util.*;

public abstract class Human implements RoomOccupant, HotelEventListener, mouseInteractions {
    private Tile tile;
    private final Layout layout;
    private int stepsTaken;
    private ArrayList<Tile> destinationPath;
    private Tile destination;

    private final int id;
    private Room assignedRoom;
    private final Role role;
    private boolean isReadyToDespawn;
    private final ArrayDeque<HotelEvent> eventQueue;

    private int cooldown = 0;
    private boolean hover = false;
    private MouseAdapter mousevents;

    public Human(Tile tile, Layout layout, Role role, int id) {
        this.role = role;
        this.id = id;
        this.layout = layout;
        this.isReadyToDespawn = false;
        this.stepsTaken = 0;
        this.eventQueue = new ArrayDeque<>();
        this.tile = tile;
        initMouseEvents();
        applyTileMouseInteraction(true);
        this.tile.setHuman(this);
    }

    public ArrayDeque<HotelEvent> getEventQueue() {
        return eventQueue;
    }

    public MouseAdapter getMousevents() {
        return mousevents;
    }

    public void initMouseEvents() {
        Human human = this;
        this.mousevents = new MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                human.mouseEntered();

            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                human.mouseExited();

            }
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                human.mouseClicked();
            }
        };
    }

    @Override
    public void mouseExited() {
        this.hover = false;
    }

    @Override
    public void mouseEntered() {
        getTile().setBackground(Color.YELLOW);
        this.destinationPath = null;
        this.destination = null;
        this.hover = true;
    }

    @Override
    public void mouseClicked() {
        System.out.println("yes");
        hover = false;
        this.notify(new HotelEvent(HotelEventType.CHECK_IN, 0, this.id, 0));
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


    public void setDestination(Tile destination) {
        this.destination = destination;
        this.getPathToDestination(destination);
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    void applyTileMouseInteraction(boolean apply) {
        if (apply) {
            for (Tile tile : tile.getNeighbours()) {
                if (tile==null) continue;
                tile.addMouseListener(this.mousevents);
            }
            this.tile.addMouseListener(this.mousevents);
        } else {
            this.tile.removeMouseListener(this.mousevents);
            for (Tile tile : tile.getNeighbours()) {
                if (tile==null) continue;
                tile.removeMouseListener(this.mousevents);
            }
        }
    }

    public void setTile(Tile newTile, Color color) {
        applyTileMouseInteraction(false);
        this.tile.setHuman(null);
        this.tile = newTile;
        applyTileMouseInteraction(true);
        this.tile.setBackground(color);
        this.tile.setHuman(this);
    }

    public abstract boolean applyRandomMovement();

    public void update() {
        if (cooldown >= -2) {
            cooldown--;
            System.out.println(cooldown);
        }

        if (hover) return;

        if (this.getDestinationPath() != null) {
            this.move();
        } else {
            if (cooldown > 0 || !this.eventQueue.isEmpty() || !applyRandomMovement()) { return;}
            cooldown = Settings.delay * 5;
            this.setDestination(layout.getRandomTile(null));
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
//            stepsTaken++;
//            stepsTaken++;
//            stepsTaken++;
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

    public void getPathToDestination(Tile destination) {
        this.destinationPath = new ArrayList<>();

        HashSet<Tile> open = new HashSet<>();
        HashSet<Tile> closed = new HashSet<>();
        HashMap<Tile, Tile> breadcrumbs = new HashMap<>();

        open.add(this.tile);

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
                        || !accessibleFacility(neighbour)) {
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


