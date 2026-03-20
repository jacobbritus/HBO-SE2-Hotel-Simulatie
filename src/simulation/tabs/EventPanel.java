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

public class EventPanel extends JPanel {
    JTextField idField;
    JTextField ticksField;
    HotelEvent event;

    public EventPanel(HotelEvent hotelEvent, boolean isNew) {
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.setOpaque(true);
        this.setBackground(Settings.themeColor);

        event = hotelEvent;

        JLabel title = new MyLabel(hotelEvent.getEventType().getTitle(), FontWeight.MEDIUM, TextSize.SMALL);
        this.add(title);
        title.setPreferredSize(new Dimension(150   , 40));

        this.add(Box.createHorizontalGlue());
        JLabel idLabel = new MyLabel("ID", FontWeight.MEDIUM, TextSize.SMALL);
        this.add(idLabel);
        if (isNew) {
            this.idField = returnTextField(hotelEvent.getHumanId());
            this.add(idField);
        } else {
            idLabel.setText("ID: " + hotelEvent.getHumanId().toString());
        }


        this.add(Box.createHorizontalStrut(10));

        JLabel ticksLabel = new MyLabel("Tick" , FontWeight.MEDIUM, TextSize.SMALL);
        this.add(ticksLabel);
        if (isNew) {
            this.ticksField = returnTextField(hotelEvent.getTime());
            this.add(ticksField);
        } else {
            ticksLabel.setText("Tick: " +String.valueOf(hotelEvent.getTime()));
        }

        if (isNew) {
            this.setBorder(BorderFactory.createCompoundBorder(new MatteBorder(0, 0, 1, 0,
                    Settings.themeColor2), new EmptyBorder(10, 10, 10, 10)));
        } else {
            this.setBorder(BorderFactory.createCompoundBorder(new MatteBorder(0, 0, 1, 0,
                    Settings.themeColor2), new EmptyBorder(20, 20, 20, 20)));

        }
    }
    public JTextField returnTextField(Integer initialVal) {
        JTextField tf = new JTextField(String.valueOf(initialVal));
        tf.setPreferredSize(new Dimension(48, 32));
        tf.setMaximumSize(new Dimension(48, 16));
        return tf;
    }

    public HotelEvent returnEvent() {
        this.event.setHumanId(getId());
        this.event.setTime(getTicks());
        return this.event;
    }

    public Integer getId() {
        return  Integer.parseInt(this.idField.getText());
    }
    public Integer getTicks() {
        return  Integer.parseInt(this.ticksField.getText());
    }


}
