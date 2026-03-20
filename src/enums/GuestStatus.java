package enums;

import java.awt.*;

public enum GuestStatus {
    ARRIVED(new Color(255, 0, 0)),
    CHECKED_IN(new Color(0, 255, 0)),
    CHECKING_OUT(new Color(170, 0, 255));

    public final Color color;

    GuestStatus(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
