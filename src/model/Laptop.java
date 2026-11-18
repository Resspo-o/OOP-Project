package model;

public class Laptop extends EquipmentItem {

    private final int ramGb;
    private final boolean hasGpu;

    public Laptop(String itemId, String name, double dailyFee, int ramGb, boolean hasGpu) {
        super(itemId, name, "Computing", dailyFee);
        this.ramGb = ramGb;
        this.hasGpu = hasGpu;
    }

    public Laptop(String itemId, String name, String category, double dailyFee, int ramGb, boolean hasGpu) {
        super(itemId, name, category, dailyFee);
        this.ramGb = ramGb;
        this.hasGpu = hasGpu;
    }

    public int getRamGb() {
        return ramGb;
    }

    public boolean isHasGpu() {
        return hasGpu;
    }

    @Override
    public double calculateFee(int numberOfDays) {
        if (numberOfDays <= 0) {
            return 0.0;
        }
        double fee = getDailyFee() * numberOfDays;
        if (hasGpu) {
            fee += 10.0 * numberOfDays;
        }
        return fee;
    }

    @Override
    public void displaySummary() {
        System.out.println("[Laptop] " + getItemId() + " | " + getName()
                + " | " + ramGb + " GB RAM | GPU: " + hasGpu
                + " | " + getDailyFee() + " MAD/day | " + getAvailabilityText());
    }

    @Override
    public String getDisplayLabel() {
        return "[Laptop] " + getItemId() + " | " + getName()
                + " | " + ramGb + " GB RAM | GPU: " + hasGpu
                + " | " + getDailyFee() + " MAD/day | " + getAvailabilityText();
    }
}
