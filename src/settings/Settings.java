package settings;

import java.awt.Color;

public final class Settings {
    private Settings() {} // Geen instanties kunnen gemaakt worden.

    // Scherm Dimensies
    public static int schermBreedte = 1080;
    public static int schermHoogte = 720;

    // Sidebar width
    public static int sidebarWidth = 400;

    // Theme
    // Default = Color.WHITE
//    public static String colorTheme = "light";
//    public static Color achtergrondKleur = new Color(250, 250, 250, 255);
//    public static Color themeColor = new Color(255, 255, 255,255);
//    public static Color themeColor2 = new Color(240, 240, 240,255);
//    public static Color themeColor3 = new Color(247, 247, 247,255);
//    public static Color textColor = Color.BLACK;
//    public static Color textColor2 = Color.GRAY;
    public static String colorTheme = "dark";
    public static Color achtergrondKleur = new Color(23, 22, 22,255);
    public static Color themeColor = new Color(30, 30, 30,255);
    public static Color themeColor2 = new Color(40, 40, 40,255);
    public static Color themeColor3 = new Color(50, 50, 50,255);
    public static Color textColor = Color.WHITE;
public static Color textColor2 = Color.LIGHT_GRAY;

    // Bepaald hoe hoog en breed elk Oppervlakte is
    // Default = 100
    public static int oppervlakGrootte = 100;

    // Bepaald hoeveel vakjes in een Oppervlakte staan
    // Default = 16
    public static int facilityTilesSize = 16;

    // Vakjes View
    public static boolean setSquaresAlternatingColors = false;

    // Oppervlakte Kleuren

    // Milliseconden per tik
    public static int delay = 250;

    // Guest times

    public static int guestBaseStayTime = 60 * 60 * 3;
    public static int guestBaseSpawnTime = 60 * 10;
    public static int guestBaseCheckInTime = 60 * 15;

    // Cleaner times
    public static int cleanerBaseCleaningTime = 60 * 30 ;

    public static String convertTime(int milliseconds) {
        int seconds = milliseconds % 60;
        int minutes = (milliseconds / 60) % 60;
        int hours = milliseconds / 3600;

        return String.format(
                "%02d:%02d:%02d",
                hours, minutes, seconds
        );
    }
}
