import helper.MyScrollPane;
import settings.FacilityColors;
import settings.Settings;
import simulation.Simulation;
import simulation.HotelEventManager;
import simulation.Sidebar;
import simulation.tabs.InfoPanel;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Applicatie extends JFrame implements KeyListener {
    Simulation simulation;
    boolean sidebarVisible;

    public Applicatie() {
        this.setSize(new Dimension(Settings.schermBreedte, Settings.schermHoogte));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.addKeyListener(this);

        this.setBackground(Color.BLACK);
        FacilityColors.setup();

    }

    public void startSimulatie(String[][] rauweGrid) {
        simulation = new Simulation(rauweGrid);
        sidebarVisible = true;
        this.setLayout(new BorderLayout());


        JScrollPane scrollPane = new MyScrollPane(simulation);

        this.getContentPane().add(scrollPane);

        Sidebar sidebar = new Sidebar();
        simulation.setSimulationSidebar(sidebar);

        InfoPanel infoPanel = new InfoPanel();

        this.add(infoPanel, BorderLayout.SOUTH);
        scrollPane.setColumnHeaderView(infoPanel);

        HotelEventManager hotelEventManager = new HotelEventManager(simulation, sidebar, infoPanel);
        simulation.setSimulationController(hotelEventManager);
        this.add(hotelEventManager, BorderLayout.NORTH);

        this.add(sidebar, BorderLayout.WEST);
        this.setVisible(true);
        
        simulation.init();
        sidebar.init(hotelEventManager);

        simulation.zoom(Settings.facilityTilesSize*  -3);
    }



    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyChar() == '+') {
            simulation.zoom(Settings.facilityTilesSize);
        } else if (e.getKeyChar() == '-') {
            simulation.zoom(Settings.facilityTilesSize * -1);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override public void keyTyped(KeyEvent e) {}
}