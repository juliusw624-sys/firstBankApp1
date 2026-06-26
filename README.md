# First Bank Uganda — New Account Opening Form

A Java Swing desktop application for OOP Coursework Question One. Lets a new
client fill in a bank account opening form, validates every field, opens the
correct polymorphic `Account` subtype, generates a sequential account
number, and persists the record to a MySQL database served by XAMPP.

## Project Layout

```
src/main/java/bank/
  model/   Account (abstract) + SavingsAccount, CurrentAccount, FixedDepositAccount,
           StudentAccount, JointAccount, AccountFactory, AccountType, Branch
  util/    Validator, DateUtil, PinHasher
  db/      DatabaseManager (JDBC access to the firstbankdb MySQL database)
  gui/     BankAccountForm.java + BankAccountForm.form (the NetBeans JFrame
           Form), Main.java (entry point)
src/main/resources/images/
           First Bank3.png (the bank logo used in the header)
lib/       MySQL Connector/J jar (downloaded from Maven Central)
pom.xml    Maven build descriptor (only needed if you build with Maven)
```

`BankAccountForm` is a genuine NetBeans JFrame Form — every field, label,
combo box, and button was placed visually in NetBeans' Design view (not
hand-coded), and the paired `.form` file lets you keep editing the layout
there. The validation, persistence, and event-handling logic in this README
lives in the parts of `BankAccountForm.java` *outside* the IDE-generated
`initComponents()` block, so it's safe to keep adjusting the layout in
Design view without losing the wiring described below.

## Database Setup (XAMPP / MySQL)

> **Note on the coursework spec:** the assignment brief asks for an MS
> Access database. This project uses MySQL via XAMPP instead, as a
> functionally equivalent relational JDBC datastore — same SQL semantics,
> same JDBC `PreparedStatement`/`ResultSet` API, just a different driver
> connecting to `jdbc:mysql://...` instead of `jdbc:ucanaccess://...`. The
> `DatabaseManager` class is the only place this matters; swapping back to
> MS Access would mean changing its connection string and the one
> `CREATE TABLE` statement, nothing else in the codebase. Mention this
> substitution explicitly in your Word answer sheet.

1. Start XAMPP and make sure **MySQL** is running (Apache is only needed if
   you also want phpMyAdmin in the browser).
2. The app connects to a database named **`firstbankdb`** on
   `localhost:3306` as user `root` with no password — XAMPP's defaults. If
   `firstbankdb` doesn't exist yet, create it once via phpMyAdmin
   (`http://localhost/phpmyadmin` → New → name it `firstbankdb`) or:
   ```bash
   /Applications/XAMPP/xamppfiles/bin/mysql -u root -e "CREATE DATABASE IF NOT EXISTS firstbankdb;"
   ```
3. The app creates its own table, **`bank_accounts`**, automatically the
   first time it runs (`CREATE TABLE IF NOT EXISTS`) — no manual schema
   setup needed. Columns: `account_number` (PK), `first_name`, `last_name`,
   `nin`, `email`, `phone_number`, `pin_hash`, `date_of_birth`,
   `account_type`, `branch`, `opening_deposit`, `second_nin`, `created_at`.

   Note: the table is named `bank_accounts`, not `accounts` — an earlier
   `accounts` table in this database had a corrupted InnoDB tablespace file
   that couldn't be dropped without filesystem root access, so the app uses
   a clean table name to avoid it.
4. If your MySQL `root` user has a password, or you're not using the
   XAMPP defaults, edit `DEFAULT_JDBC_URL`/`DEFAULT_USER`/`DEFAULT_PASSWORD`
   at the top of `src/main/java/bank/db/DatabaseManager.java`.

## Option A — Open in NetBeans (recommended, matches the assignment)

1. Open NetBeans → **File → Open Project** → select this folder. NetBeans
   recognises `pom.xml` and imports it as a Maven project automatically.
2. Right-click the project → **Resolve Project Problems** (if prompted) so
   NetBeans downloads the MySQL Connector/J dependency listed in `pom.xml`.
   Offline alternative: add `lib/mysql-connector-j-9.7.0.jar` to the
   project's Libraries node manually.
3. Open `BankAccountForm.java` — the **Design** tab shows the live,
   editable layout (header, every field, the footer). The **Source** tab
   shows the wiring logic.
4. Make sure XAMPP's MySQL is running, then click **Run Project** (F6) or
   right-click `Main.java` → **Run File**.

## Option B — Compile and run from the terminal (no Maven/NetBeans needed)

```bash
cd "Grp Course work"

# Compile every source file against the jar in lib/
find src/main/java -name "*.java" > sources.txt
javac -encoding UTF-8 -cp "lib/*" -d out @sources.txt

# Copy the logo onto the runtime classpath (Maven/NetBeans do this
# automatically via the resources folder; plain javac does not)
mkdir -p out/images
cp "src/main/resources/images/First Bank3.png" out/images/

# Run the GUI (make sure XAMPP MySQL is running first)
java -cp "lib/*:out" bank.gui.Main
```

On Windows, replace `lib/*:out` with `lib/*;out` (semicolon classpath
separator) in the `java` command.

## Option C — Build with Maven

```bash
mvn package
java -jar target/first-bank-account-form.jar
```

## Using the Application

1. Fill in First Name, Last Name, National ID (NIN, 14 alphanumeric
   characters — lowercase is automatically uppercased before validation),
   Email + Confirm Email, Phone Number (`+256XXXXXXXXX`, 9 digits after the
   prefix), PIN + Confirm PIN (4-6 digits, not all identical).
2. Pick Date of Birth from the Year/Month/Day combo boxes — the Day list
   automatically reflects the chosen month's length, including 29 days for
   February in leap years.
3. Choose Account Type and Branch, and enter the Opening Deposit (UGX).
   - **Joint** accounts also require the "Second NIN" field (14
     alphanumeric characters) for the co-holder.
   - **Student** accounts require the applicant to be aged 18-25; all
     accounts require 18-75.
4. Click **Submit**.
   - If anything is invalid, the specific field's red inline error label
     fills in (e.g. under "First Name", under "PIN") *and* a dialog lists
     every problem found.
   - If everything is valid, a formatted record appears in the "Account
     Summary is Below:" area, e.g.:
     `ACC: KLA-2026-000001 | Okello Allan | Savings | Kampala | DOB 2004-02-29 | +256772123456 | Deposit 50,000 | okello.allan@firstbank.co.ug`
   - The record is appended to the `bank_accounts` table in `firstbankdb`.
     Account numbers follow `BRANCHCODE-YYYY-xxxxxx` (e.g. `KLA-2026-000001`)
     using a running counter per branch and year.
5. Click **Reset** to clear the form (including error labels) and start a
   new application.

If the packed window is taller than your screen, the whole form is wrapped
in a scroll pane — just scroll down to reach the footer/Submit/Reset area.

## Inspecting the Database

View saved accounts directly in phpMyAdmin
(`http://localhost/phpmyadmin/index.php?route=/database/structure&db=firstbankdb`
→ `bank_accounts` table), or from the terminal:

```bash
/Applications/XAMPP/xamppfiles/bin/mysql -u root -h 127.0.0.1 -P 3306 firstbankdb \
  -e "SELECT * FROM bank_accounts;"
```

## Object-Oriented Design Notes

- `Account` is abstract and declares `minimumDeposit()` and
  `accountTypeName()` as abstract methods, plus a hook
  `extraEligibilityError(age)` for subtype-specific eligibility rules.
- `SavingsAccount`, `CurrentAccount`, `FixedDepositAccount`,
  `StudentAccount`, and `JointAccount` each override `minimumDeposit()` with
  their own UGX threshold from the spec table.
- `AccountFactory` picks the right concrete subclass from the GUI's
  selected Account Type; `BankAccountForm` then calls
  `account.minimumDeposit()` polymorphically to validate the entered
  deposit — it never branches on account type itself to decide the minimum.
- PINs are hashed with SHA-256 (`PinHasher`) before being persisted; the
  plain PIN is never written to the database.

## Dependencies

`lib/mysql-connector-j-9.7.0.jar` — MySQL JDBC driver, downloaded from
Maven Central.
