package com.banking;

import java.time.LocalDate;
import java.util.ArrayList;

public class Customer extends User {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4870562534874301188L;
	private int customerID;
	private String firstName;
	private String lastName;
	private String phoneNumber;
	private String address;
	private String email;
	private LocalDate dob;
	private String ssn;
	private ArrayList<BankAccount> accounts;
	private Application pendingApplication;
	private boolean applicationDenied;
	private boolean applicationApproved;

	public Customer(int customerID, String firstName, String lastName, String phoneNumber, String address, String email, LocalDate dob,
			String ssn, String username, String password) {
		super();
		this.setCustomerID(customerID);
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.address = address;
		this.email = email;
		this.dob = dob;
		this.ssn = ssn;
		setUsername(username);
		setPassword(password);
		this.accounts = new ArrayList<BankAccount>();
		setType(UserType.Customer);
	}

	public Customer(String firstName, String lastName, String phoneNumber, String address, String email, LocalDate dob,
			String ssn, String username, String password) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.address = address;
		this.email = email;
		this.dob = dob;
		this.ssn = ssn;
		setUsername(username);
		setPassword(password);
		this.accounts = new ArrayList<BankAccount>();
		setType(UserType.Customer);
	}
	
	public LocalDate getDob() {
		return dob;
	}

	public void setDob(LocalDate dob) {
		this.dob = dob;
	}

	@Override
	public String toString() {
		return "Name: " + firstName + " " + lastName + "  PhoneNumber: " + phoneNumber + "  Address: " + address
				+ "  Email: " + email + "  DOB: " + dob + "  SSN: " + ssn + "  Username: " + getUsername()
				+ "  Password: " + getPassword();
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	public ArrayList<BankAccount> getAccounts() {
		return accounts;
	}

	public void setAccounts(ArrayList<BankAccount> accounts) {
		this.accounts = accounts;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void applyForAccount() {

		if (pendingApplication != null) {
			System.out.println(
					"You already have a pending application. Please wait for that application to be approved or denied before applying for a new account.");
			return;
		}

		applicationDenied = false;
		applicationApproved = false;

		Application newApplication = null;
		boolean validInput = false;

		while (!validInput) {
			System.out.println("Would you like to apply for an individual or joint account?");
			System.out.println("1. Individual");
			System.out.println("2. Joint");

			String choice = Main.input.nextLine();

			switch (choice) {
			case "back":
				Main.currentUser = null;
				return;
			case "1":
				newApplication = new Application(this);
				validInput = true;
				break;
			case "2":
				Customer jointCustomer = selectJointCustomer();
				if(jointCustomer != null)
					newApplication = new Application(this, jointCustomer);
				else
					System.out.println("Something was wrong with your application. Please try again.");
				validInput = true;
				break;
			default:
				System.out.println(
						"That is not a valid choice. Please enter 1 for an individual account or 2 for a joint account.");
			}
		}

		Main.applicationList.add(newApplication);
		this.setPendingApplication(newApplication);

		if (newApplication.getJointCustomer() != null) {
			newApplication.getJointCustomer().setPendingApplication(newApplication);
		}
		Main.writeObject(Main.applicationFile, Main.applicationList);
	}

	private Customer selectJointCustomer() {

		// Find the index of the current customer so they are not available to be chosen
		// as the
		// joint customer
		int thisIndex = Main.customerList.indexOf(this);

		// If the current customer is the only registered customer then a new customer
		// must be
		// registered to apply for a joint bank account
		if (Main.customerList.size() == 1) {
			System.out
					.println("There are currently no other registered customers. Please register a new customer now.");
			return Main.newCustomer();
		}

		// Print the names of each registered customer other than the current customer

		ArrayList<Customer> availableJointCustomers = new ArrayList<Customer>();
		int numCustomers = 0;

		for (int i = 0; i < Main.customerList.size(); i++) {
			if (i != thisIndex) {
				Customer c = Main.customerList.get(i);
				availableJointCustomers.add(c);
				System.out.println(numCustomers + 1 + ". " + c.getFirstName() + " " + c.getLastName());
				numCustomers++;
			}
		}

		int index;

		do {
			System.out.print(
					"Enter the number of the customer that would be the joint customer or 0 to register a new customer: ");
			String choice = Main.input.nextLine();

			try {
				index = Integer.parseInt(choice);
				break;
			} catch (Exception e) {
				System.out.println(
						"Please be sure to enter the index for the customer you would like for the joint customer.");
				continue;
			}
		} while (true);

		
		if (index == 0)
			return Main.newCustomer();
		else if (index < 0 || index > availableJointCustomers.size())
			System.out.println(
					"That is not a valid number. Please enter the number of one of the available customers or 0 to register a new customer.");
		else
			return availableJointCustomers.get(index - 1);

		return null;
	}

	@Override
	public void showOptions() {

		System.out.println();
		System.out.println("Welcome " + firstName + " " + lastName);

		if (applicationDenied) {
			System.out.println();
			System.out.println("Your application was denied. You may try to apply for another account.");
			System.out.println();
			applicationDenied = false;
		} else if (applicationApproved) {
			System.out.println();
			System.out.println("Your application has been approved. You may now view your accounts and make changes.");
			System.out.println();
			applicationApproved = false;
		}

		while (Main.currentUser == this) {
			if (this.accounts.isEmpty()) {
				if (this.pendingApplication == null) {
					System.out.println(
							"You currently have no bank accounts. Would you like to apply for an account now? ");
					System.out.println("1. Yes, apply for a new account.");
					System.out.println("2. No, don't apply for an account now.");
					String choice = Main.input.nextLine();

					switch (choice) {
					case "":
						Main.currentUser = null;
						break;
					case "1":
						applyForAccount();
						break;
					case "2":
						Main.currentUser = null;
						return;
					}
				} else {
					System.out.println();
					System.out.println(
							"You currently have a pending application. Please check later to see if your application has been approved.");
					System.out.println();
					Main.currentUser = null;
				}
			} else {
				System.out.println();
				System.out.println(
						"Please enter the number of the choice you would like or type \"back\" to return to user selection.");
				System.out.println("1. Apply for a new account");
				System.out.println("2. View Accounts");

				String choice = Main.input.nextLine();

				switch (choice) {
				case "back":
					Main.currentUser = null;
					break;
				case "1":
					applyForAccount();
					break;
				case "2":
					showAccounts();
					break;
				default:
					System.out.println("That is not a valid input please enter 1, 2, or \"back\".");
					break;
				}
			}
		}

	}

	private void showAccounts() {
		System.out.println();
		System.out.println("Accounts");
		for (int i = 0; i < accounts.size(); i++) {
			BankAccount account = accounts.get(i);
			System.out.println(i + 1 + ". " + account.cleanString());
		}

		// loop will continue to run until the user s
		while (true) {
			System.out.println();
			System.out.print("Enter the number of the account you would like to edit or type \"back\" to go back: ");

			String choice = Main.input.nextLine();

			if (choice.equals("back"))
				return;

			int index;

			try {
				index = Integer.parseInt(choice);
			} catch (Exception e) {
				System.out.println("Please be sure to enter the index for the account you would like to edit.");
				continue;
			}

			if (index < 1 || index > accounts.size()) {
				System.out.println(
						"That input is invalid. Please enter the index of the account you would like to edit starting at 1 for the first account.");
			} else {
				BankAccount account = accounts.get(index - 1);

				// loop will continue to run until the user goes back
				while (true) {
					System.out.println();
					System.out.println("Select an option below or type \"back\" to return to the acccount selection.");
					System.out.println("1. Check Account Balance");
					System.out.println("2. Deposit");
					System.out.println("3. Withdraw");
					System.out.println("4. Transfer");

					choice = Main.input.nextLine();

					switch (choice) {
					case "back":
						return;
					case "1":
						account.checkBalance();
						break;
					case "2":
						System.out.println("How much money would you like to deposit? ");
						double amount = 0;
						try {
							amount = Double.parseDouble(Main.input.nextLine());
						} catch (Exception e) {
							System.out.println("That input is invalid please be sure to enter a decimal number only.");
						}
						if (amount != 0)
							account.deposit(amount);
						break;
					case "3":
						System.out.println("How much money would you like to withdraw? ");
						amount = 0;
						try {
							amount = Double.parseDouble(Main.input.nextLine());
						} catch (Exception e) {
							System.out.println("That input is invalid please be sure to enter a decimal number only.");
						}
						if (amount != 0)
							account.withdraw(amount);
						break;
					case "4":
						if (Main.accountList.size() == 1) {
							System.out.println("There are no other accounts to transfer to.");
							break;
						}

						// select the account to transfer the money to
						BankAccount transferAccount = chooseAccount(account);
						System.out.println("How much money would you like to transfer? ");
						amount = 0;
						try {
							amount = Double.parseDouble(Main.input.nextLine());
						} catch (Exception e) {
							System.out.println("That input is invalid please be sure to enter a decimal number only.");
						}
						if (transferAccount != null && amount != 0)
							account.transfer(amount, transferAccount);
						break;
					default:
						System.out.println("That is not a valid input.");
						break;
					}

				}

			}
		}

	}

	// Choose a bank account to transfer money to. Parameter specifies to current
	// account that is used since it cannot be transferred to itself
	private BankAccount chooseAccount(BankAccount currentAccount) {

		System.out.println(
				"Please enter the account number of the account that you would like to transfer to or \"back\" to go back: ");
		while (true) {
			System.out.println();
			int thisIndex = accounts.indexOf(currentAccount);

			// Print each bank account other than the currently used account
			for (int i = 0; i < accounts.size(); i++) {
				if (i != thisIndex) {
					BankAccount b = accounts.get(i);
					System.out.println(b.cleanString());
//					System.out.println(i + 1 + ". Account Number: " + b.getAccountNumber() + "    Account Balance: "
//							+ b.getAccountBalance());
				}
			}

			String choice = Main.input.nextLine();

			if (choice.equals("back"))
				return null;

			long accountNumber = 0;

			try {
				accountNumber = Long.parseLong(choice);
			} catch (Exception e) {
				System.out.println("That input is invalid. Please only enter numbers or \"back\". ");
			}

			BankAccount accountToTransfer = findAccount(accountNumber);

			if (accountToTransfer != null && accountToTransfer.equals(currentAccount)) {
				System.out
						.println("You can't transfer to the same account. You must type in a differnt account number.");
				return null;
			}

			return accountToTransfer;
		}
	}

	// Adds an account to the customer's list of accounts
	public void addAccount(BankAccount account) {
		accounts.add(account);
	}

	public boolean isApplicationDenied() {
		return applicationDenied;
	}

	public void setApplicationDenied(boolean applicationDenied) {
		this.applicationDenied = applicationDenied;
	}

	public boolean isApplicationApproved() {
		return applicationApproved;
	}

	public void setApplicationApproved(boolean applicationApproved) {
		this.applicationApproved = applicationApproved;
	}

	public void removeAccount(BankAccount account) {
		accounts.remove(account);
	}

	public Application getPendingApplication() {
		return pendingApplication;
	}

	public void setPendingApplication(Application pendingApplication) {
		this.pendingApplication = pendingApplication;
	}

	public void clearAccounts() {
		accounts.clear();
	}

	public int getCustomerID() {
		return customerID;
	}

	public void setCustomerID(int customerID) {
		this.customerID = customerID;
	}
}
