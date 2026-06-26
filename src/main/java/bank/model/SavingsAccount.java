package bank.model;

import java.time.LocalDate;

/** Earns interest, no overdraft. Minimum opening deposit: UGX 50,000. */
public class SavingsAccount extends Account {

    public SavingsAccount(String firstName, String lastName, String nin, String email,
                           String phoneNumber, String pin, LocalDate dateOfBirth,
                           Branch branch, long openingDeposit) {
        super(firstName, lastName, nin, email, phoneNumber, pin, dateOfBirth, branch, openingDeposit);
    }

    @Override
    public long minimumDeposit() { return 50_000L; }

    @Override
    public String accountTypeName() { return AccountType.SAVINGS.getDisplayName(); }
}
