package simulation;

import enums.FontWeight;
import enums.TextSize;
import enums.UnitType;
import helper.MyLabel;
import settings.Settings;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.util.function.Supplier;

public class StatRow extends JPanel {
    JLabel valueLabel;
    JProgressBar progressBar;
    UnitType unit;
    Supplier<Integer> supplier;
    public StatRow(String statTitle, JPanel parent, UnitType unit, Supplier<Integer> supplier) {
        parent.add(this);
        this.unit = unit;
        this.supplier = supplier;
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.setOpaque(false);
        JLabel titleLabel = new MyLabel(statTitle, FontWeight.MEDIUM, TextSize.SMALL);

        Dimension size = titleLabel.getPreferredSize();
        titleLabel.setPreferredSize(new Dimension(size.width + 20, size.height));

        this.add(titleLabel);
        this.add(Box.createHorizontalGlue());
        if (unit == null) return;


        if (this.unit == UnitType.PERCENTAGE_POSITIVE || this.unit == UnitType.PERCENTAGE_NEGATIVE) {
            createProgressBar();
        }
        createNumericalValueLabel();

        this.setBorder(new MatteBorder(0, 0, 1, 0, Settings.themeColor2));

        this.setMaximumSize(new Dimension(Short.MAX_VALUE, this.getPreferredSize().height));
        this.setAlignmentX(Component.LEFT_ALIGNMENT);


    }

    public void createNumericalValueLabel() {
        this.valueLabel = new MyLabel("0", FontWeight.MEDIUM, TextSize.SMALL);
        this.valueLabel.setForeground(Settings.textColor2);
        this.valueLabel.setPreferredSize(new Dimension(50, 50));
        this.valueLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        this.add(this.valueLabel);
    }

    public void createProgressBar() {

        JProgressBar bar = new JProgressBar();
        bar.setValue(0);
        bar.setUI(new javax.swing.plaf.basic.BasicProgressBarUI() {
            protected Color getSelectionBackground() { return Color.black; } // Text color over background
            protected Color getSelectionForeground() { return Color.white; } // Text color over progress
        });

        bar.setPreferredSize(new Dimension(100, 3));
        bar.setMaximumSize(new Dimension(50, 3));
        bar.setForeground(new Color(46, 204, 113));
        bar.setBackground(Settings.themeColor2);
        bar.setOpaque(true);
        bar.setBorderPainted(false);

        this.progressBar = bar;
        this.add(this.progressBar);
    }

    public void update() {
         if (this.unit == UnitType.PERCENTAGE_POSITIVE || this.unit == UnitType.PERCENTAGE_NEGATIVE) {
             int percentage = this.supplier.get();
             Color color;
             if (percentage > 90) {
                 if (this.unit == UnitType.PERCENTAGE_POSITIVE) color = Color.GREEN;
                 else color = Color.RED;
             } else if (percentage > 50) {color = Color.YELLOW;
             } else {
                 if (this.unit == UnitType.PERCENTAGE_POSITIVE) color = Color.RED;
                 else color = Color.GREEN;
             }
             this.progressBar.setForeground(color);
             this.progressBar.setValue(percentage);
             this.valueLabel.setText(" " + percentage + "%");
         } else {
             this.valueLabel.setText(String.valueOf(this.supplier.get()));
         }

        this.revalidate();
        this.repaint();
    }
}
