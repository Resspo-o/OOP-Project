package model;

public class CampusMember {

    private static int nextMemberNumber = 501;

    private final String memberId;
    private final String fullName;
    private String email;
    private final String department;
    private int activeLoans;
    private final int borrowingLimit;

    public CampusMember(String fullName, String email, String department, int borrowingLimit) {
        this("MB-" + (nextMemberNumber++), fullName, email, department, borrowingLimit);
    }

    public CampusMember(String memberId, String fullName, String email,
                        String department, int borrowingLimit) {
        if (memberId == null || memberId.isBlank()) {
            throw new IllegalArgumentException("Member ID cannot be empty.");
        }
        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException("Full name cannot be empty.");
        }
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email: " + email);
        }
        if (department == null || department.isBlank()) {
            throw new IllegalArgumentException("Department cannot be empty.");
        }
        if (borrowingLimit < 0) {
            throw new IllegalArgumentException("Borrowing limit cannot be negative: " + borrowingLimit);
        }
        this.memberId = memberId;
        this.fullName = fullName;
        this.email = email;
        this.department = department;
        this.borrowingLimit = borrowingLimit;
        this.activeLoans = 0;
    }

    private static boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }
        int at = email.indexOf('@');
        return at > 0 && email.indexOf('.', at) > at + 1;
    }

    public boolean updateEmail(String newEmail) {
        if (!isValidEmail(newEmail)) {
            return false;
        }
        this.email = newEmail;
        return true;
    }

    public String getMemberId() {
        return memberId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getDepartment() {
        return department;
    }

    public int getActiveLoans() {
        return activeLoans;
    }

    public int getBorrowingLimit() {
        return borrowingLimit;
    }

    public boolean canBorrow() {
        return activeLoans < borrowingLimit;
    }

    public void recordBorrowing() {
        if (canBorrow()) {
            activeLoans++;
        }
    }

    public void recordReturn() {
        if (activeLoans > 0) {
            activeLoans--;
        }
    }

    public String getLoanStatus() {
        return activeLoans + "/" + borrowingLimit;
    }

    public void displayProfile() {
        System.out.println("Member ID    : " + memberId);
        System.out.println("Name         : " + fullName);
        System.out.println("Email        : " + email);
        System.out.println("Department   : " + department);
        System.out.println("Active loans : " + activeLoans + " / " + borrowingLimit);
    }
}
