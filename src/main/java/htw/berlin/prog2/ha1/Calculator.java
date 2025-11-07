package htw.berlin.prog2.ha1;

/**
 * Eine Klasse, die das Verhalten des Online Taschenrechners imitiert, welcher auf
 * https://www.online-calculator.com/ aufgerufen werden kann (ohne die Memory-Funktionen)
 * und dessen Bildschirm bis zu zehn Ziffern plus einem Dezimaltrennzeichen darstellen kann.
 * Enthält mit Absicht noch diverse Bugs oder unvollständige Funktionen.
 */
public class Calculator {

    private String screen = "0";
    private double latestValue;
    private String latestOperation = "";
    private boolean resultJustShown = false;

    /**
     * @return den aktuellen Bildschirminhalt als String
     */
    public String readScreen() {
        return screen;
    }

    /**
     * Empfängt den Wert einer gedrückten Zifferntaste (0–9)
     */
    public void pressDigitKey(int digit) {
        if (digit > 9 || digit < 0) throw new IllegalArgumentException();

        // Wenn "Error" angezeigt wird, zuerst löschen
        if (screen.equals("Error")) {
            screen = "";
        }

        // Wenn gerade ein Ergebnis angezeigt wurde, neu anfangen
        if (resultJustShown) {
            screen = "";
            resultJustShown = false;
        }

        if (screen.equals("0") || latestValue == Double.parseDouble(screen)) screen = "";

        screen = screen + digit;
    }

    /**
     * Empfängt den Befehl der C/CE-Taste (Clear).
     */
    public void pressClearKey() {
        screen = "0";
        latestOperation = "";
        latestValue = 0.0;
    }

    /**
     * Empfängt den Wert einer gedrückten binären Operationstaste (+, -, x, /)
     */
    public void pressBinaryOperationKey(String operation) {
        latestValue = Double.parseDouble(screen);
        latestOperation = operation;
        resultJustShown = false;
    }

    /**
     * Empfängt den Wert einer gedrückten unären Operationstaste (√, %, 1/x)
     */
    public void pressUnaryOperationKey(String operation) {
        latestValue = Double.parseDouble(screen);
        latestOperation = operation;

        var result = switch (operation) {
            case "√" -> Math.sqrt(Double.parseDouble(screen));
            case "%" -> Double.parseDouble(screen) / 100;
            case "1/x" -> 1 / Double.parseDouble(screen);
            default -> throw new IllegalArgumentException();
        };

        screen = Double.toString(result);

        if (screen.equals("NaN") || screen.equals("Infinity")) screen = "Error";
        if (screen.endsWith(".0")) screen = screen.substring(0, screen.length() - 2);
        if (screen.contains(".") && screen.length() > 11) screen = screen.substring(0, 10);

        resultJustShown = true;
    }

    /**
     * Empfängt den Befehl der Dezimaltrennzeichentaste (".")
     */
    public void pressDotKey() {
        if (!screen.contains(".")) screen = screen + ".";
    }

    /**
     * Empfängt den Befehl der Vorzeichenumkehrstaste ("+/-")
     */
    public void pressNegativeKey() {
        screen = screen.startsWith("-") ? screen.substring(1) : "-" + screen;
    }

    /**
     * Empfängt den Befehl der "=" Taste.
     */
    public void pressEqualsKey() {
        double result;

        try {
            result = switch (latestOperation) {
                case "+" -> latestValue + Double.parseDouble(screen);
                case "-" -> latestValue - Double.parseDouble(screen);
                case "x" -> latestValue * Double.parseDouble(screen);
                case "/" -> latestValue / Double.parseDouble(screen);
                default -> Double.parseDouble(screen);
            };
        } catch (ArithmeticException e) {
            screen = "Error";
            return;
        }

        if (Double.isInfinite(result) || Double.isNaN(result)) {
            screen = "Error";
        } else {
            screen = Double.toString(result);
            if (screen.endsWith(".0")) screen = screen.substring(0, screen.length() - 2);
            if (screen.contains(".") && screen.length() > 11) screen = screen.substring(0, 10);
        }

        resultJustShown = true;
    }
}

