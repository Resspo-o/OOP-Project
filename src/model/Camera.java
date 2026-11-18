package model;

public class Camera extends EquipmentItem {

    private final double megaPixels;
    private final boolean hasVideoMode;

    public Camera(String itemId, String name, double dailyFee, double megaPixels, boolean hasVideoMode) {
        super(itemId, name, "Media", dailyFee);
        this.megaPixels = megaPixels;
        this.hasVideoMode = hasVideoMode;
    }

    public Camera(String itemId, String name, String category, double dailyFee, double megaPixels, boolean hasVideoMode) {
        super(itemId, name, category, dailyFee);
        this.megaPixels = megaPixels;
        this.hasVideoMode = hasVideoMode;
    }

    public double getMegaPixels() {
        return megaPixels;
    }

    public boolean isHasVideoMode() {
        return hasVideoMode;
    }

    @Override
    public double calculateFee(int numberOfDays) {
        if (numberOfDays <= 0) {
            return 0.0;
        }
        double fee = getDailyFee() * numberOfDays;
        if (hasVideoMode) {
            fee += 15.0;
        }
        return fee;
    }

    @Override
    public void displaySummary() {
        System.out.println("[Camera] " + getItemId() + " | " + getName()
                + " | " + megaPixels + " MP | Video: " + hasVideoMode
                + " | " + getDailyFee() + " MAD/day | " + getAvailabilityText());
    }

    @Override
    public String getDisplayLabel() {
        return "[Camera] " + getItemId() + " | " + getName()
                + " | " + megaPixels + " MP | Video: " + hasVideoMode
                + " | " + getDailyFee() + " MAD/day | " + getAvailabilityText();
    }
}
