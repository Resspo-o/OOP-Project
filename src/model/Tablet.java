package model;

public class Tablet extends EquipmentItem {

    private final int storageGb;

    public Tablet(String itemId, String name, double dailyFee, int storageGb) {
        super(itemId, name, "Computing", dailyFee);
        this.storageGb = storageGb;
    }

    public Tablet(String itemId, String name, String category, double dailyFee, int storageGb) {
        super(itemId, name, category, dailyFee);
        this.storageGb = storageGb;
    }

    public int getStorageGb() {
        return storageGb;
    }

    @Override
    public double calculateFee(int numberOfDays) {
        if (numberOfDays <= 0) {
            return 0.0;
        }
        double fee = getDailyFee() * numberOfDays;
        if (storageGb >= 256) {
            fee += 5.0 * numberOfDays;
        }
        return fee;
    }

    @Override
    public void displaySummary() {
        System.out.println(getDisplayLabel());
    }

    @Override
    public String getDisplayLabel() {
        return "[Tablet] " + getItemId() + " | " + getName()
                + " | " + storageGb + " GB storage"
                + " | " + getDailyFee() + " MAD/day | " + getAvailabilityText();
    }
}
