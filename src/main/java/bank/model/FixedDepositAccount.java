package bank.model;

import java.time.LocalDate;

/** Locked term, highest interest. Minimum opening deposit: UGX 1,000,000. */
public class FixedDepositAccount extends Account {

    public FixedDepositAccount(String firstName, String lastName, String nin, String email,
                                String phoneNumber, String pin, LocalDate dateOfBirth,
                                Branch branch, long openingDeposit) {
        super(firstName, lastName, nin, email, phoneNumber, pin, dateOfBirth, branch, openingDeposit);
    }

    @Override
    public long minimumDeposit() { return 1_000_000L; }

    @Override
    public String accountTypeName() { return AccountType.FIXED_DEPOSIT.getDisplayName(); }
}
