package model;

public class Projector extends EquipmentItem {

    private final int lumens;
    private final boolean hasWifi;

    public Projector(String itemId, String name, double dailyFee, int lumens, boolean hasWifi) {
        super(itemId, name, "Presentation", dailyFee);
        this.lumens = lumens;
        this.hasWifi = hasWifi;
    }

    public Projector(String itemId, String name, String category, double dailyFee, int lumens, boolean hasWifi) {
        super(itemId, name, category, dailyFee);
        this.lumens = lumens;
        this.hasWifi = hasWifi;
    }

    public int getLumens() {
        return lumens;
    }

    public boolean isHasWifi() {
        return hasWifi;
    }

    @Override
    public double calculateFee(int numberOfDays) {
        if (numberOfDays <= 0) {
            return 0.0;
        }
        double fee = getDailyFee() * numberOfDays;
        if (numberOfDays >= 3) {
            fee = fee * 0.9;
        }
        return fee;
    }

    @Override
    public void displaySummary() {
        System.out.println("[Projector] " + getItemId() + " | " + getName()
                + " | " + lumens + " lm | WiFi: " + hasWifi
                + " | " + getDailyFee() + " MAD/day | " + getAvailabilityText());
    }

    @Override
    public String getDisplayLabel() {
        return "[Projector] " + getItemId() + " | " + getName()
                + " | " + lumens + " lm | WiFi: " + hasWifi
                + " | " + getDailyFee() + " MAD/day | " + getAvailabilityText();
    }
}
