package simulation.tabs;

import enums.FontWeight;
import enums.TextSize;
import events.HotelEvent;
import helper.MyLabel;
import settings.Settings;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class EventPanel extends JPanel {
    JTextField idField;
    JTextField ticksField;
    JTextField dataField;
    HotelEvent event;

    public EventPanel(HotelEvent hotelEvent, boolean isNew, EventsTab eventsTab) {
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.setOpaque(true);
        this.setBackground(Settings.themeColor);

        event = hotelEvent;

        JLabel title = new MyLabel(hotelEvent.getEventType().getTitle(), FontWeight.MEDIUM, TextSize.SMALL);
        this.add(title);
        title.setPreferredSize(new Dimension(150   , 40));

        this.add(Box.createHorizontalGlue());
        JLabel idLabel = new MyLabel("ID: ", FontWeight.MEDIUM, TextSize.SMALL);
        this.add(idLabel);
        if (isNew) {
            this.idField = returnTextField(hotelEvent.getHumanId());
            this.add(idField);
        } else {
            idLabel.setText("ID: " + hotelEvent.getHumanId().toString());
        }

        this.add(Box.createHorizontalStrut(10));

        JLabel ticksLabel = new MyLabel("Tick: " , FontWeight.MEDIUM, TextSize.SMALL);
        this.add(ticksLabel);
        if (isNew) {
            this.ticksField = returnTextField(hotelEvent.getTime());
            this.add(ticksField);
        } else {
            ticksLabel.setText("Tick: " +String.valueOf(hotelEvent.getTime()));
        }

        this.add(Box.createHorizontalStrut(10));

        JLabel data = new MyLabel("Data: " , FontWeight.MEDIUM, TextSize.SMALL);
        this.add(data);
        if (isNew) {
            this.dataField = returnTextField(hotelEvent.getData());
            this.add(dataField);
        } else {
            data.setText("Data: " +String.valueOf(hotelEvent.getData()));
        }


        if (isNew) {
            this.setBorder(BorderFactory.createCompoundBorder(new MatteBorder(0, 0, 1, 0,
                    Settings.themeColor2), new EmptyBorder(10, 10, 10, 10)));
        } else {
            this.setBorder(BorderFactory.createCompoundBorder(new MatteBorder(0, 0, 1, 0,
                    Settings.themeColor2), new EmptyBorder(20, 20, 20, 20)));

        }
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (event.getTime() < eventsTab.getHotelEventManager().getEventTicks()) return;
                if (isNew) {
                    event.setTime(Integer.parseInt(ticksField.getText()));
                    event.setHumanId(Integer.parseInt(idField.getText()));
                    event.setData(Integer.parseInt(dataField.getText()));
                    eventsTab.addNewEvent(event);
                }
                else eventsTab.removeEvent(event);

            }
            @Override
            public void mouseEntered(MouseEvent e) {
                if (event.getTime() < eventsTab.getHotelEventManager().getEventTicks()) return;
                if (isNew) setBackground(Settings.themeColor2);
                else setBackground(new Color(180, 26, 26, 207));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                if (event.getTime() < eventsTab.getHotelEventManager().getEventTicks()) return;
                setBackground(Settings.themeColor);
            }
        });
    }

    public JTextField returnTextField(Integer initialVal) {
        JTextField tf = new JTextField(String.valueOf(initialVal));
        tf.setPreferredSize(new Dimension(28, 32));
        tf.setBackground(Settings.themeColor2);
        tf.setBorder(null);
        tf.setForeground(Settings.textColor);
        tf.setMaximumSize(new Dimension(48, 16));
        tf.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                System.out.println(e.getKeyChar());
                if (tf.getText().length() >= 3 || !isNumeric(e.getKeyChar()) ) // limit textfield to 3 characters
                    e.consume();
            }
        });
        return tf;
    }

    public boolean isNumeric(Character number) {
        try {
            int check = Integer.parseInt(String.valueOf(number));
            System.out.println(true);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
