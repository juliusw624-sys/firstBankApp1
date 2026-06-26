package bank.util;

import bank.model.Account;
import bank.model.AccountType;
import bank.model.Branch;

import java.time.LocalDate;
import java.util.regex.Pattern;

// Checks each form field against the validation rules from the assignment.
public final class Validator {

    private static final Pattern NAME_PATTERN = Pattern.compile("^[A-Za-z]{2,30}$");
    private static final Pattern NIN_PATTERN = Pattern.compile("^[A-Z0-9]{14}$");
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+256\\d{9}$");
    private static final Pattern PIN_PATTERN = Pattern.compile("^\\d{4,6}$");

    private Validator() { }

    public static String validateName(String fieldLabel, String value) {
        if (value == null || value.trim().isEmpty()) {
            return fieldLabel + " is required.";
        }
        String trimmed = value.trim();
        if (!NAME_PATTERN.matcher(trimmed).matches()) {
            return fieldLabel + " must be 2-30 letters only.";
        }
        return null;
    }

    public static String validateNin(String value) {
        if (value == null || value.trim().isEmpty()) {
            return "National ID (NIN) is required.";
        }
        String trimmed = value.trim();
        if (!NIN_PATTERN.matcher(trimmed).matches()) {
            return "NIN must be exactly 14 uppercase alphanumeric characters.";
        }
        return null;
    }

    public static String validateEmail(String email, String confirmEmail) {
        if (email == null || email.trim().isEmpty()) {
            return "Email is required.";
        }
        String trimmed = email.trim();
        if (!EMAIL_PATTERN.matcher(trimmed).matches()) {
            return "Email is not a valid email address.";
        }
        if (confirmEmail == null || !trimmed.equalsIgnoreCase(confirmEmail.trim())) {
            return "Email and Confirm Email must match.";
        }
        return null;
    }

    public static String validatePhone(String value) {
        if (value == null || value.trim().isEmpty()) {
            return "Phone Number is required.";
        }
        if (!PHONE_PATTERN.matcher(value.trim()).matches()) {
            return "Phone must be in the format +256XXXXXXXXX (9 digits after +256).";
        }
        return null;
    }

    public static String validatePin(String pin, String confirmPin) {
        if (pin == null || pin.trim().isEmpty()) {
            return "PIN is required.";
        }
        String trimmed = pin.trim();
        if (!PIN_PATTERN.matcher(trimmed).matches()) {
            return "PIN must be numeric, 4-6 digits.";
        }
        if (isAllIdenticalDigits(trimmed)) {
            return "PIN must not be all-identical digits (e.g., 0000).";
        }
        if (confirmPin == null || !trimmed.equals(confirmPin.trim())) {
            return "PIN and Confirm PIN must match.";
        }
        return null;
    }

    private static boolean isAllIdenticalDigits(String digits) {
        char first = digits.charAt(0);
        for (int i = 1; i < digits.length(); i++) {
            if (digits.charAt(i) != first) {
                return false;
            }
        }
        return true;
    }

    public static String validateAge(LocalDate dateOfBirth, AccountType accountType) {
        int age = DateUtil.calculateAge(dateOfBirth);
        if (age < 18 || age > 75) {
            return "Applicant age must be 18-75 (was " + age + ").";
        }
        if (accountType == AccountType.STUDENT && (age < 18 || age > 25)) {
            return "Student accounts require the applicant to be aged 18-25 (was " + age + ").";
        }
        return null;
    }

    public static String validateAccountType(AccountType accountType) {
        return accountType == null ? "Please select exactly one Account Type." : null;
    }

    public static String validateBranch(Branch branch) {
        return branch == null ? "Please select exactly one Branch." : null;
    }

    public static String validateDeposit(long deposit, Account account) {
        if (deposit <= 0) {
            return "Opening Deposit must be a positive number.";
        }
        if (account != null && deposit < account.minimumDeposit()) {
            return String.format("Opening Deposit must be at least UGX %,d for %s accounts.",
                    account.minimumDeposit(), account.accountTypeName());
        }
        return null;
    }

    /** Parses a numeric deposit string; returns -1 if it is not a valid non-negative number. */
    public static long parseDeposit(String value) {
        if (value == null || value.trim().isEmpty()) {
            return -1;
        }
        try {
            return Long.parseLong(value.trim().replace(",", ""));
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
