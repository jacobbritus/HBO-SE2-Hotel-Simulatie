package simulation;

import enums.FacilityType;
import events.HotelEvent;
import events.HotelEventListener;
import facility.Facility;
import facility.Tile;
import human.Cleaner;
import human.Guest;
import human.Human;
import layout.Layout;
import settings.Settings;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;

public class Simulation extends JPanel implements HotelEventListener {
     Layout layout;
     HotelEventManager hotelEventManager;
     Sidebar sidebar;
     private ArrayList<Human> humans;
     private final JPanel layoutBagroundPanel;
     private final String[][] rauweGrid;

//
    public Simulation(String[][] rauweGrid) {
        this.setLayout(new BorderLayout()); // Zet layout in het midden
        this.setSize(new Dimension(Settings.schermBreedte, Settings.schermHoogte));
        this.rauweGrid = rauweGrid;
        layoutBagroundPanel = new JPanel(new GridBagLayout());
        layoutBagroundPanel.setBackground(Settings.achtergrondKleur);
        layoutBagroundPanel.setOpaque(true);
        layoutBagroundPanel.setPreferredSize(new Dimension(Settings.schermBreedte , Settings.schermHoogte - 128 ));
    }

    public HotelEventManager getSimulationController() {
        return hotelEventManager;
    }

    public Layout returnLayout() {
        return this.layout;
    }

    public ArrayList<Human> getHumans() {
        return humans;
    }



    public void init() {
        this.layout = new Layout(rauweGrid, this.hotelEventManager);
        layoutBagroundPanel.add(layout);
        this.add(layoutBagroundPanel);
        this.humans = new ArrayList<>();
    }

    @Override
    public void notify(HotelEvent hotelEvent) {
        switch (hotelEvent.getEventType()) {
            case SPAWN_GUEST -> {
                Tile tile = this.layout.getRandomTile(layout.getFacilitiesByType(FacilityType.LOBBY).getFirst());
                Guest guest = new Guest(tile, this.layout, hotelEvent.getHumanId());
                humans.add(guest);
            }
            case SPAWN_CLEANER -> {
                Tile tile = this.layout.getRandomTile(layout.getFacilitiesByType(FacilityType.LOBBY).getFirst());
                Cleaner cleaner = new Cleaner(tile, this.layout, hotelEvent.getHumanId());
                humans.add(cleaner);
            }
        }
    }

    public void reset() {
        layoutBagroundPanel.removeAll();
        this.init();
        this.revalidate();
        this.repaint();
        this.layout.reload();

    }


    public void setSimulationController(HotelEventManager simulatieController) {
        this.hotelEventManager = simulatieController;
    }

    public void setSimulationSidebar(Sidebar sidebar) {
        this.sidebar = sidebar;
    }

    public void updateHumans() {
        for (Human human : this.humans) {
            human.update();
        }

        humans.removeIf(h -> {
            if (h.isReadyToDespawn()) {
                removeHuman(h);
                return true;
            }
            return false;
        });
    }

    public void removeHuman(Human h) {
        this.hotelEventManager.removeListener(h);
    }

    public void zoom(int aantal) {
        Settings.oppervlakGrootte += aantal;

        // Nieuwe preferred size op basis van huidige grid
        Facility[][] r = layout.getFacilities();
        int hoogte = r.length;
        int breedte = r[0].length;

        layoutBagroundPanel.setPreferredSize(new Dimension(
                Settings.oppervlakGrootte * breedte *3,
                Settings.oppervlakGrootte * hoogte *2));
        layout.setPreferredSize(new Dimension(
                Settings.oppervlakGrootte * breedte,
                Settings.oppervlakGrootte * hoogte));

        this.layout.reload();
        this.layout.revalidate();
    }


}