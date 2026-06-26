package bank.model;

import java.time.LocalDate;

/** Applicant age must be 18-25. Minimum opening deposit: UGX 10,000. */
public class StudentAccount extends Account {

    public StudentAccount(String firstName, String lastName, String nin, String email,
                           String phoneNumber, String pin, LocalDate dateOfBirth,
                           Branch branch, long openingDeposit) {
        super(firstName, lastName, nin, email, phoneNumber, pin, dateOfBirth, branch, openingDeposit);
    }

    @Override
    public long minimumDeposit() { return 10_000L; }

    @Override
    public String accountTypeName() { return AccountType.STUDENT.getDisplayName(); }
}
