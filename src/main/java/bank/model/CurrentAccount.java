package bank.model;

import java.time.LocalDate;

/** Overdraft allowed, no interest. Minimum opening deposit: UGX 200,000. */
public class CurrentAccount extends Account {

    public CurrentAccount(String firstName, String lastName, String nin, String email,
                           String phoneNumber, String pin, LocalDate dateOfBirth,
                           Branch branch, long openingDeposit) {
        super(firstName, lastName, nin, email, phoneNumber, pin, dateOfBirth, branch, openingDeposit);
    }

    @Override
    public long minimumDeposit() { return 200_000L; }

    @Override
    public String accountTypeName() { return AccountType.CURRENT.getDisplayName(); }
}
