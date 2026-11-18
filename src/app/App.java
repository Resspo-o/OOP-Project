package app;

import exception.InvalidLoanException;
import exception.ItemUnavailableException;
import exception.UnknownItemException;
import exception.UnknownLoanException;
import exception.UnknownMemberException;
import model.Camera;
import model.CampusMember;
import model.Chargeable;
import model.EquipmentItem;
import model.Laptop;
import model.Loan;
import model.Projector;
import model.Tablet;
import service.LendingService;

public class App {

    public static void main(String[] args) {

        // -- Setup --
        CampusMember member1 = new CampusMember("M001", "Amin", "amina@univ.ma",
                "Artificial Intelligence", 2);

        Laptop    laptop    = new Laptop("EQ002", "Dell Laptop",    80.0, 16,   true);
        Camera    camera    = new Camera("EQ001", "Canon Camera",   50.0, 20.1, true);
        Projector projector = new Projector("EQ003", "Epson Projector", 60.0, 3000, true);

        CampusMember[]  members   = { member1 };
        EquipmentItem[] inventory = { camera, laptop, projector };
        Loan[]          loans     = new Loan[10];

        LendingService lendingService = new LendingService(members, inventory, loans);

        // -- Initial state --
        System.out.println("=== Initial state ===");
        member1.displayProfile();
        for (EquipmentItem item : inventory) {
            printEquipment(item);
        }

        // -- Lab 02: constructor validation and updateEmail --
        System.out.println("\n--- Lab 02: constructor validation and updateEmail ---");
        System.out.println("updateEmail(\"not-an-email\") -> " + member1.updateEmail("not-an-email")
                + " (email kept: " + member1.getEmail() + ")");
        System.out.println("updateEmail(\"amin@univ.ma\") -> " + member1.updateEmail("amin@univ.ma")
                + " (email now: " + member1.getEmail() + ")");
        try {
            new Laptop("EQ-9999", "Bad Laptop", -10.0, 8, false);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid equipment fee rejected: " + e.getMessage());
        }
        try {
            new CampusMember("MB-999", "Bad", "no-email", "AI", 2);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid member email rejected: " + e.getMessage());
        }
        System.out.println("Auto-ID member: " + new CampusMember("Test", "test@univ.ma", "AI", 2).getMemberId());

        // -- Chargeable polymorphism --
        System.out.println("\n=== Chargeable interface ===");
        for (EquipmentItem item : inventory) {
            if (item instanceof Laptop) {
                Laptop current = (Laptop) item;
                System.out.println(current.getName() + " - RAM: " + current.getRamGb()
                        + " GB | GPU: " + current.isHasGpu());
            } else if (item instanceof Camera) {
                Camera current = (Camera) item;
                System.out.println(current.getName() + " - " + current.getMegaPixels()
                        + " MP | Video: " + current.isHasVideoMode());
            } else if (item instanceof Projector) {
                Projector current = (Projector) item;
                System.out.println(current.getName() + " - " + current.getLumens()
                        + " lm | WiFi: " + current.isHasWifi());
            }
        }

        Chargeable[] chargeables = { camera, laptop, projector };
        System.out.println("\n=== Fees for 3 days ===");
        for (Chargeable c : chargeables) {
            System.out.println(((EquipmentItem) c).getName() + ": " + c.calculateFee(3)
                    + " MAD/3 days @ " + ((EquipmentItem) c).getDailyFee() + " MAD/day");
        }
        System.out.println("  (Laptop: +10 DH/day GPU surcharge; Camera: +15 DH flat video kit;"
                + " Projector: 10% discount from 3 days.)");

        // -- Step 1: borrow camera - expects SUCCESS --
        System.out.println("\n=== Interactions ===");
        System.out.println("Step 1: Borrow camera (expect SUCCESS)");
        Loan cameraLoan = null;
        try {
            cameraLoan = lendingService.borrowItem("M001", "EQ001", 3);
            printBorrowState(camera, loans);
        } catch (UnknownMemberException | UnknownItemException
                 | ItemUnavailableException | InvalidLoanException e) {
            System.out.println("Borrowing rejected: " + e.getMessage());
        }

        // -- Step 2: borrow laptop - expects SUCCESS --
        System.out.println("Step 2: Borrow laptop (expect SUCCESS)");
        Loan laptopLoan = null;
        try {
            laptopLoan = lendingService.borrowItem("M001", "EQ002", 2);
            printBorrowState(laptop, loans);
        } catch (UnknownMemberException e) {
            System.out.println("Borrowing rejected: " + e.getMessage());
        } catch (UnknownItemException | ItemUnavailableException
                 | InvalidLoanException e) {
            System.out.println("Borrowing rejected: " + e.getMessage());
        }
        if (cameraLoan != null && laptopLoan != null) {
            System.out.println("Reference check: cameraLoan.getMember() == member1 -> "
                    + (cameraLoan.getMember() == member1));
            System.out.println("Reference check: cameraLoan.getItem() == camera -> "
                    + (cameraLoan.getItem() == camera));
        }

        // -- Step 3: borrow projector - expects FAIL (limit reached) --
        System.out.println("Step 3: Borrow projector (expect FAIL - limit)");
        try {
            lendingService.borrowItem("M001", "EQ003", 1);
            printBorrowState(projector, loans);
        } catch (UnknownItemException e) {
            System.out.println("Borrowing rejected: " + e.getMessage());
        } catch (UnknownMemberException | ItemUnavailableException
                 | InvalidLoanException e) {
            System.out.println("Borrowing rejected: " + e.getMessage());
        }

        // -- Step 4: borrow unknown item - expects FAIL (UnknownItem) --
        System.out.println("Step 4: Borrow unknown item (expect FAIL - unknown item)");
        try {
            lendingService.borrowItem("M001", "EQ999", 1);
        } catch (ItemUnavailableException e) {
            System.out.println("Borrowing rejected: " + e.getMessage());
        } catch (UnknownMemberException | UnknownItemException
                 | InvalidLoanException e) {
            System.out.println("Borrowing rejected: " + e.getMessage());
        }

        // -- Step 5: borrow with invalid days - expects FAIL --
        System.out.println("Step 5: Borrow with invalid days (expect FAIL - invalid)");
        try {
            lendingService.borrowItem("M001", "EQ003", -1);
        } catch (InvalidLoanException e) {
            System.out.println("Borrowing rejected: " + e.getMessage());
        } catch (UnknownMemberException | UnknownItemException
                 | ItemUnavailableException e) {
            System.out.println("Borrowing rejected: " + e.getMessage());
        }

        // -- Step 6: return camera - expects SUCCESS --
        System.out.println("Step 6: Return camera (expect SUCCESS)");
        try {
            lendingService.returnItem(cameraLoan.getLoanId());
            printReturnState(camera, member1, loans);
        } catch (UnknownLoanException | InvalidLoanException e) {
            System.out.println("Return rejected: " + e.getMessage());
        }

        // -- Step 7: return camera again - expects FAIL (already returned) --
        System.out.println("Step 7: Return camera again (expect FAIL - already returned)");
        try {
            lendingService.returnItem(cameraLoan.getLoanId());
        } catch (UnknownLoanException e) {
            System.out.println("Return rejected: " + e.getMessage());
        } catch (InvalidLoanException e) {
            System.out.println("Return rejected: " + e.getMessage());
        }

        // -- Step 8: return null loan - expects FAIL (UnknownLoan) --
        System.out.println("Step 8: Return null loan (expect FAIL - unknown)");
        try {
            lendingService.returnItem((String) null);
        } catch (UnknownLoanException | InvalidLoanException e) {
            System.out.println("Return rejected: " + e.getMessage());
        }

        // -- Step 9: borrow projector after camera freed - expects SUCCESS --
        System.out.println("Step 9: Borrow projector after camera freed (expect SUCCESS)");
        try {
            Loan projectorLoan = lendingService.borrowItem("M001", "EQ003", 1);
            System.out.println("  -> Projector borrowed, loan " + projectorLoan.getLoanId());
            printBorrowState(projector, loans);
        } catch (UnknownMemberException | UnknownItemException | ItemUnavailableException
                 | InvalidLoanException e) {
            System.out.println("Borrowing rejected: " + e.getMessage());
        }

        // -- Final state --
        System.out.println("\n=== Final state ===");
        for (EquipmentItem item : inventory) {
            printEquipment(item);
        }

        System.out.println("\n=== All loans ===");
        printAllLoans(loans);

        System.out.println("\n=== Active loans ===");
        printActiveLoans(loans);

        System.out.println("\n=== Total active fees ===");
        System.out.println("Total: " + calculateActiveLoanFees(loans) + " MAD");

        System.out.println();
        printCompactReport(member1, camera, laptop, projector);

        // =====================
        // LAB 5 EXERCISES
        // =====================

        // -- Exercise 1: Constructor Chaining --
        System.out.println("\n=== LAB5 Ex1: Constructor Chaining verification ===");
        Laptop    ex1Laptop    = new Laptop("EX1-L", "Lab5 Laptop",    10.0, 8,    false);
        Camera    ex1Camera    = new Camera("EX1-C", "Lab5 Camera",    15.0, 12.0, false);
        Projector ex1Projector = new Projector("EX1-P", "Lab5 Projector", 20.0, 1500, false);
        Tablet    ex1Tablet    = new Tablet("EX1-T",  "Lab5 Tablet",    8.0, 128);
        System.out.println("All concrete equipment objects created successfully.");

        // -- Exercise 2: Override getDisplayLabel() --
        System.out.println("\n=== LAB5 Ex2: Overridden getDisplayLabel() ===");
        EquipmentItem[] ex2Items = { ex1Laptop, ex1Camera, ex1Projector, ex1Tablet };
        for (EquipmentItem item : ex2Items) {
            System.out.println(item.getDisplayLabel());
        }

        // -- Exercise 3: Overloaded getDisplayLabel(boolean) --
        System.out.println("\n=== LAB5 Ex3: Overloaded getDisplayLabel(boolean) ===");
        System.out.println("Laptop  (no availability)   : " + ex1Laptop.getDisplayLabel(false));
        System.out.println("Laptop  (with availability) : " + ex1Laptop.getDisplayLabel(true));
        System.out.println("Camera  (no availability)   : " + ex1Camera.getDisplayLabel(false));
        System.out.println("Camera  (with availability) : " + ex1Camera.getDisplayLabel(true));

        // -- Exercise 4: Polymorphic Inventory Processing --
        System.out.println("\n=== LAB5 Ex4: Polymorphic inventory processing ===");
        EquipmentItem[] fullInventory = { camera, laptop, projector, ex1Tablet };
        for (EquipmentItem item : fullInventory) {
            printItemLabel(item);
        }

        // -- Exercise 5: Safe Downcasting for Subclass-Specific Data --
        System.out.println("\n=== LAB5 Ex5: Safe downcasting ===");
        for (EquipmentItem item : fullInventory) {
            if (item instanceof Laptop) {
                Laptop l = (Laptop) item;
                System.out.println(l.getName() + " -> RAM: " + l.getRamGb() + " GB, GPU: " + l.isHasGpu());
            } else if (item instanceof Camera) {
                Camera c = (Camera) item;
                System.out.println(c.getName() + " -> " + c.getMegaPixels() + " MP, Video: " + c.isHasVideoMode());
            } else if (item instanceof Projector) {
                Projector p = (Projector) item;
                System.out.println(p.getName() + " -> " + p.getLumens() + " lm, WiFi: " + p.isHasWifi());
            } else if (item instanceof Tablet) {
                Tablet t = (Tablet) item;
                System.out.println(t.getName() + " -> Storage: " + t.getStorageGb() + " GB");
            }
        }

        // -- Exercise 6: Lending Transactions + Polymorphism --
        System.out.println("\n=== LAB5 Ex6: Lending + Polymorphism ===");
        Loan[] lab5Loans = new Loan[10];
        EquipmentItem[] lab5Inventory = { ex1Laptop, ex1Camera, ex1Projector, ex1Tablet };
        CampusMember member2 = new CampusMember("M002", "Sara", "sara@univ.ma",
                "Artificial Intelligence", 2);
        CampusMember[] lab5Members = { member2 };
        LendingService lab5Service = new LendingService(lab5Members, lab5Inventory, lab5Loans);

        System.out.println("-- Before transaction --");
        for (EquipmentItem item : lab5Inventory) {
            System.out.println(item.getDisplayLabel());
        }

        Loan lab5Loan = null;
        try {
            lab5Loan = lab5Service.borrowItem("M002", "EX1-C", 2);
            System.out.println("-- During transaction (Camera borrowed) --");
            for (EquipmentItem item : lab5Inventory) {
                System.out.println(item.getDisplayLabel());
            }
        } catch (UnknownMemberException | UnknownItemException
                 | ItemUnavailableException | InvalidLoanException e) {
            System.out.println("Borrow failed: " + e.getMessage());
        }

        try {
            if (lab5Loan != null) {
                lab5Service.returnItem(lab5Loan.getLoanId());
            }
            System.out.println("-- After transaction (Camera returned) --");
            for (EquipmentItem item : lab5Inventory) {
                System.out.println(item.getDisplayLabel());
            }
        } catch (UnknownLoanException | InvalidLoanException e) {
            System.out.println("Return failed: " + e.getMessage());
        }

        // -- Exercise 7: New Tablet subclass in inventory --
        System.out.println("\n=== LAB5 Ex7: Tablet in polymorphic inventory ===");
        for (EquipmentItem item : lab5Inventory) {
            printItemLabel(item);
            if (item instanceof Tablet) {
                Tablet t = (Tablet) item;
                System.out.println("   >> Tablet storage: " + t.getStorageGb() + " GB");
            }
        }
    }

    // -- Helper methods --

    private static void printEquipment(EquipmentItem item) {
        item.displaySummary();
    }

    private static void printItemLabel(EquipmentItem item) {
        System.out.println(item.getDisplayLabel());
    }

    private static void printBorrowState(EquipmentItem item, Loan[] loans) {
        System.out.println("  -> " + item.getName() + " is now " + item.getAvailabilityText());
        System.out.println("  -> Stored loans: " + countStoredLoans(loans));
    }

    private static void printReturnState(EquipmentItem item, CampusMember member, Loan[] loans) {
        System.out.println("  -> " + item.getName() + " is now " + item.getAvailabilityText());
        System.out.println("  -> " + member.getFullName() + " active loans: " + member.getLoanStatus());
        System.out.println("  -> Stored loans: " + countStoredLoans(loans));
    }

    private static int countStoredLoans(Loan[] loans) {
        int count = 0;
        for (Loan loan : loans) {
            if (loan != null) {
                count++;
            }
        }
        return count;
    }

    private static void printAllLoans(Loan[] loans) {
        for (Loan loan : loans) {
            if (loan != null) {
                System.out.println(loan.summaryLine());
            }
        }
    }

    private static void printActiveLoans(Loan[] loans) {
        for (Loan loan : loans) {
            if (loan != null && loan.isActive()) {
                System.out.println(loan.summaryLine());
            }
        }
    }

    private static double calculateActiveLoanFees(Loan[] loans) {
        double total = 0.0;
        for (Loan loan : loans) {
            if (loan != null && loan.isActive()) {
                total += loan.getTotalFee();
            }
        }
        return total;
    }

    private static void printCompactReport(CampusMember member, EquipmentItem camera,
                                           EquipmentItem laptop, EquipmentItem projector) {
        System.out.println("Compact Status Report");
        System.out.println("Member: " + member.getFullName());
        System.out.println("Active loans: " + member.getLoanStatus());
        System.out.println(camera.getItemId()    + " - " + camera.getName()    + ": " + camera.getAvailabilityText());
        System.out.println(laptop.getItemId()    + " - " + laptop.getName()    + ": " + laptop.getAvailabilityText());
        System.out.println(projector.getItemId() + " - " + projector.getName() + ": " + projector.getAvailabilityText());
    }
}
