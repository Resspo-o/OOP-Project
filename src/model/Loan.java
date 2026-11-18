package model;

public class Loan {

    private final String loanId;
    private final CampusMember member;
    private final EquipmentItem item;
    private final int numberOfDays;
    private final double totalFee;
    private boolean active;

    public Loan(String loanId, CampusMember member, EquipmentItem item, int numberOfDays, double totalFee) {
        if (loanId == null || loanId.isBlank()) {
            throw new IllegalArgumentException("Loan ID cannot be empty.");
        }
        if (member == null) {
            throw new IllegalArgumentException("Loan member cannot be null.");
        }
        if (item == null) {
            throw new IllegalArgumentException("Loan item cannot be null.");
        }
        if (numberOfDays <= 0) {
            throw new IllegalArgumentException("Number of days must be positive: " + numberOfDays);
        }
        this.loanId = loanId;
        this.member = member;
        this.item = item;
        this.numberOfDays = numberOfDays;
        this.totalFee = totalFee;
        this.active = true;
    }

    public String getLoanId() {
        return loanId;
    }

    public CampusMember getMember() {
        return member;
    }

    public EquipmentItem getItem() {
        return item;
    }

    public String getMemberId() {
        return member.getMemberId();
    }

    public String getItemId() {
        return item.getItemId();
    }

    public int getNumberOfDays() {
        return numberOfDays;
    }

    public double getTotalFee() {
        return totalFee;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isForItem(EquipmentItem other) {
        return item == other;
    }

    public boolean belongsTo(CampusMember other) {
        return member == other;
    }

    public void closeLoan() {
        this.active = false;
    }

    public void close() {
        closeLoan();
    }

    public String summaryLine() {
        return "Loan[" + loanId + "] " + member.getFullName() + " (" + member.getMemberId() + ")"
                + " borrowed " + item.getName() + " (" + item.getItemId() + ")"
                + " for " + numberOfDays + " day(s), fee " + totalFee + " MAD"
                + (active ? " (active)" : " (closed)");
    }

    @Override
    public String toString() {
        return summaryLine();
    }
}
