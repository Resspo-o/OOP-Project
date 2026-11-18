package model;

public abstract class EquipmentItem implements Chargeable {

    private final String itemId;
    private final String name;
    private final String category;
    private final double dailyFee;
    private boolean available;

    public EquipmentItem(String itemId, String name, double dailyFee) {
        this(itemId, name, "General", dailyFee);
    }

    public EquipmentItem(String itemId, String name, String category, double dailyFee) {
        if (itemId == null || itemId.isBlank()) {
            throw new IllegalArgumentException("Item ID cannot be empty.");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Item name cannot be empty.");
        }
        if (dailyFee < 0) {
            throw new IllegalArgumentException("Daily fee cannot be negative: " + dailyFee);
        }
        this.itemId = itemId;
        this.name = name;
        this.category = (category == null || category.isBlank()) ? "General" : category;
        this.dailyFee = dailyFee;
        this.available = true;
    }

    public String getItemId() {
        return itemId;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public double getDailyFee() {
        return dailyFee;
    }

    public boolean isAvailable() {
        return available;
    }

    public void markBorrowed() {
        available = false;
    }

    public void markReturned() {
        available = true;
    }

    public String getAvailabilityText() {
        return available ? "Available" : "Borrowed";
    }

    public String getDisplayLabel() {
        return "[" + itemId + "] " + name + " | " + category + " | " + getDailyFee() + " MAD/day | " + getAvailabilityText();
    }

    public String getDisplayLabel(boolean includeAvailability) {
        if (!includeAvailability) {
            return getDisplayLabel();
        }
        return getDisplayLabel() + " | available=" + isAvailable();
    }

    public void displaySummary() {
        System.out.println(getDisplayLabel());
    }

    @Override
    public abstract double calculateFee(int numberOfDays);
}
