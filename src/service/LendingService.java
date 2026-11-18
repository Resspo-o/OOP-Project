package service;

import exception.InvalidLoanException;
import exception.ItemUnavailableException;
import exception.UnknownItemException;
import exception.UnknownLoanException;
import exception.UnknownMemberException;
import model.CampusMember;
import model.EquipmentItem;
import model.Loan;

public class LendingService {

    private final CampusMember[] members;
    private final EquipmentItem[] inventory;
    private final Loan[] loans;
    private int loanCount;
    private int loanIdCounter;

    public LendingService(CampusMember[] members, EquipmentItem[] inventory, Loan[] loans) {
        this.members = members;
        this.inventory = inventory;
        this.loans = loans;
        this.loanCount = 0;
        this.loanIdCounter = 1;
    }

    public Loan borrowItem(String memberId, String itemId, int numberOfDays)
            throws UnknownMemberException, UnknownItemException,
                   ItemUnavailableException, InvalidLoanException {

        CampusMember member = findMember(memberId);
        EquipmentItem item = findItem(itemId);

        if (numberOfDays < 1 || numberOfDays > 30) {
            throw new InvalidLoanException("Number of days must be between 1 and 30 (got " + numberOfDays + ").");
        }
        if (!item.isAvailable()) {
            throw new ItemUnavailableException("Item '" + item.getName() + "' is already borrowed.");
        }
        if (!member.canBorrow()) {
            throw new InvalidLoanException("Member '" + member.getFullName()
                    + "' has reached borrowing limit ("
                    + member.getActiveLoans() + "/" + member.getBorrowingLimit() + ").");
        }
        if (loanCount >= loans.length) {
            throw new InvalidLoanException("Loan storage is full.");
        }

        double fee = item.calculateFee(numberOfDays);
        String loanId = "L" + String.format("%03d", loanIdCounter++);
        Loan loan = new Loan(loanId, member, item, numberOfDays, fee);

        item.markBorrowed();
        member.recordBorrowing();
        loans[loanCount++] = loan;

        return loan;
    }

    public void returnItem(String loanId)
            throws UnknownLoanException, InvalidLoanException {
        if (loanId == null || loanId.isBlank()) {
            throw new UnknownLoanException("Loan ID is null or empty.");
        }
        Loan loan = null;
        for (int i = 0; i < loanCount; i++) {
            if (loans[i].getLoanId().equals(loanId)) {
                loan = loans[i];
                break;
            }
        }
        if (loan == null) {
            throw new UnknownLoanException("Loan '" + loanId + "' not found in the system.");
        }
        if (!loan.isActive()) {
            throw new InvalidLoanException("Loan '" + loanId + "' is already closed.");
        }
        loan.closeLoan();
        loan.getItem().markReturned();
        loan.getMember().recordReturn();
    }

    public void returnItem(Loan loan)
            throws UnknownLoanException, InvalidLoanException {
        if (loan == null) {
            throw new UnknownLoanException("Loan reference is null.");
        }
        returnItem(loan.getLoanId());
    }

    private CampusMember findMember(String memberId) throws UnknownMemberException {
        for (CampusMember m : members) {
            if (m.getMemberId().equals(memberId)) {
                return m;
            }
        }
        throw new UnknownMemberException("Member with ID '" + memberId + "' not found.");
    }

    private EquipmentItem findItem(String itemId) throws UnknownItemException {
        for (EquipmentItem eq : inventory) {
            if (eq.getItemId().equals(itemId)) {
                return eq;
            }
        }
        throw new UnknownItemException("Item with ID '" + itemId + "' not found.");
    }

    public Loan[] getLoans() {
        Loan[] copy = new Loan[loanCount];
        for (int i = 0; i < loanCount; i++) {
            copy[i] = loans[i];
        }
        return copy;
    }

    public int getLoanCount() {
        return loanCount;
    }
}
