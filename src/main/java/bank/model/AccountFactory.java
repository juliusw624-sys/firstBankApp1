package bank.model;

import java.time.LocalDate;

/** Creates the concrete Account subtype matching the chosen AccountType. */
public final class AccountFactory {

    private AccountFactory() { }

    public static Account create(AccountType type, String firstName, String lastName, String nin,
                                  String email, String phoneNumber, String pin,
                                  LocalDate dateOfBirth, Branch branch, long openingDeposit) {
        switch (type) {
            case SAVINGS:
                return new SavingsAccount(firstName, lastName, nin, email, phoneNumber, pin, dateOfBirth, branch, openingDeposit);
            case CURRENT:
                return new CurrentAccount(firstName, lastName, nin, email, phoneNumber, pin, dateOfBirth, branch, openingDeposit);
            case FIXED_DEPOSIT:
                return new FixedDepositAccount(firstName, lastName, nin, email, phoneNumber, pin, dateOfBirth, branch, openingDeposit);
            case STUDENT:
                return new StudentAccount(firstName, lastName, nin, email, phoneNumber, pin, dateOfBirth, branch, openingDeposit);
            case JOINT:
                return new JointAccount(firstName, lastName, nin, email, phoneNumber, pin, dateOfBirth, branch, openingDeposit);
            default:
                throw new IllegalArgumentException("Unsupported account type: " + type);
        }
    }
}
