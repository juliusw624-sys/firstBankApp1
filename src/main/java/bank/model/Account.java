package bank.model;

import java.time.LocalDate;

// Base class for all account types. Each subclass sets its own minimum deposit.
public abstract class Account {

    private String accountNumber;
    private String firstName;
    private String lastName;
    private String nin;
    private String email;
    private String phoneNumber;
    private String pin;
    private LocalDate dateOfBirth;
    private Branch branch;
    private long openingDeposit;
    private String secondNin; // only meaningful for JointAccount

    public Account(String firstName, String lastName, String nin, String email,
                   String phoneNumber, String pin, LocalDate dateOfBirth,
                   Branch branch, long openingDeposit) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.nin = nin;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.pin = pin;
        this.dateOfBirth = dateOfBirth;
        this.branch = branch;
        this.openingDeposit = openingDeposit;
    }

    /** Minimum deposit (UGX) required to open this account subtype. */
    public abstract long minimumDeposit();

    /** Human readable name shown in the GUI/database, e.g. "Savings". */
    public abstract String accountTypeName();

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getNin() { return nin; }
    public String getEmail() { return email; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getPin() { return pin; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public Branch getBranch() { return branch; }
    public long getOpeningDeposit() { return openingDeposit; }
    public String getSecondNin() { return secondNin; }
    public void setSecondNin(String secondNin) { this.secondNin = secondNin; }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
