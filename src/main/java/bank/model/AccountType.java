package bank.model;

/** Enumerates the account categories selectable on the form. */
public enum AccountType {
    SAVINGS("Savings"),
    CURRENT("Current"),
    FIXED_DEPOSIT("Fixed Deposit"),
    STUDENT("Student"),
    JOINT("Joint");

    private final String displayName;

    AccountType(String displayName) { this.displayName = displayName; }

    public String getDisplayName() { return displayName; }

    @Override
    public String toString() { return displayName; }
}
