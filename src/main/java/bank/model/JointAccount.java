package bank.model;

import java.time.LocalDate;

/** Requires a second NIN. Minimum opening deposit: UGX 100,000. */
public class JointAccount extends Account {

    public JointAccount(String firstName, String lastName, String nin, String email,
                         String phoneNumber, String pin, LocalDate dateOfBirth,
                         Branch branch, long openingDeposit) {
        super(firstName, lastName, nin, email, phoneNumber, pin, dateOfBirth, branch, openingDeposit);
    }

    @Override
    public long minimumDeposit() { return 100_000L; }

    @Override
    public String accountTypeName() { return AccountType.JOINT.getDisplayName(); }
}
