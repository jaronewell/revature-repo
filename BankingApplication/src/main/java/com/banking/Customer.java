package com.banking;

import java.time.LocalDate;
import java.util.ArrayList;

public class Customer extends User {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4870562534874301188L;
	private String firstName;
	private String lastName;
	private String phoneNumber;
	private String address;
	private String email;
	private LocalDate dob;
	private String ssn;
	private ArrayList<BankAccount> accounts;
	private ArrayList<Application> pendingApplications;

	public Customer(String firstName, String lastName, String phoneNumber, String address, String email, LocalDate dob,
			String ssn) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.address = address;
		this.email = email;
		this.dob = dob;
		this.ssn = ssn;
		this.accounts = new ArrayList<BankAccount>();
		setType(UserType.Customer);
		this.pendingApplications = new ArrayList<Application>();

	}

	@Override
	public String toString() {
		return "Customer [firstName=" + firstName + ", lastName=" + lastName + ", phoneNumber=" + phoneNumber
				+ ", address=" + address + ", email=" + email + ", dob=" + dob + ", ssn=" + ssn + ", username=" + getUsername() + ", password=" + getPassword() + "]";
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

		Application newApplication = null;
		boolean validInput = false;

		while (!validInput) {
			System.out.println("Would you like to apply for an individual or joint account?");
			System.out.println("1. Individual");
			System.out.println("2. Joint");

			String choice = Main.input.nextLine();
			
			switch (choice) {
			case "quit":
				Main.currentUser = null;
				return;
			case "1":
				newApplication = new Application(this);
				validInput = true;
				break;
			case "2":
				newApplication = new Application(this, selectJointCustomer());
				validInput = true;
				break;
			default:
				System.out.println(
						"That is not a valid choice. Please enter 1 for an individual account or 2 for a joint account.");
			}
		}

		Main.applicationList.add(newApplication);
		this.pendingApplications.add(newApplication);
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
		for (int i = 0; i < Main.customerList.size(); i++) {
			if (i != thisIndex) {
				Customer c = Main.customerList.get(i);
				System.out.println(i + 1 + ". " + c.getFirstName() + " " + c.getLastName());
			}
		}

		int index;
		
		while (true) {
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
		}
		if (index == 0)
			return Main.newCustomer();
		else if (index < 0 || index > Main.customerList.size() || index == thisIndex)
			System.out.println(
					"That is not a valid number. Please enter the number of one of the available customers or 0 to register a new customer.");
		else
			return Main.customerList.get(index - 1);

		return null;
	}

	public ArrayList<Application> getPendingApplications() {
		return pendingApplications;
	}

	public void setPendingApplications(ArrayList<Application> pendingApplications) {
		this.pendingApplications = pendingApplications;
	}

	@Override
	public void showOptions() {

		while (Main.currentUser == this) {
			if (this.accounts.isEmpty()) {
				System.out.println("You currently have no bank accounts and " + pendingApplications.size() + " pending applications. Would you like to apply for an account now? ");
				System.out.println("1. Yes, apply for a new account.");
				System.out.println("2. No, don't apply for an account now.");
				String choice = Main.input.nextLine();

				switch (choice) {
				case "quit":
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
				System.out.println(
						"Please enter the number of the choice you would like or type \"quit\" to return to user selection.");
				System.out.println("1. Apply for a new account");
				System.out.println("2. View Accounts");

				String choice = Main.input.nextLine();

				switch (choice) {
				case "quit":
					Main.currentUser = null;
					break;
				case "1":
					applyForAccount();
					break;
				case "2":
					showAccounts();
					break;
				default:
					System.out.println("That is not a valid input please enter 1, 2, or \"quit\".");
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

		// loop will continue to run until the user quits
		while (true) {
			System.out.print("Enter the number of the account you would like to edit or type \"quit\" to quit: ");

			String choice = Main.input.nextLine();

			if (choice.equals("quit"))
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

				// loop will continue to run until the user quits
				while (true) {
					System.out.println(
							"Select an option below or type \"back\" to return to the acccount selection or \"quit\" to quit.");
					System.out.println("1. Check Account Balance");
					System.out.println("2. Deposit");
					System.out.println("3. Withdraw");
					System.out.println("4. Transfer");

					choice = Main.input.nextLine();

					switch (choice) {
					case "quit":
						Main.currentUser = null;
						return;
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
						}catch(Exception e) {
							System.out.println("That input is invalid please be sure to enter a decimal number only.");
						}
						if(amount != 0)
							account.deposit(amount);
						break;
					case "3":
						System.out.println("How much money would you like to withdraw? ");
						amount = 0;
						try {
							amount = Double.parseDouble(Main.input.nextLine());
						}catch(Exception e) {
							System.out.println("That input is invalid please be sure to enter a decimal number only.");
						}
						if(amount != 0)
							account.withdraw(amount);
						break;
					case "4":
						System.out.println("How much money would you like to transfer? ");
						amount = 0;
						try {
							amount = Double.parseDouble(Main.input.nextLine());
						}catch(Exception e) {
							System.out.println("That input is invalid please be sure to enter a decimal number only.");
						}
						BankAccount transferAccount = chooseAccount(account);
						if (transferAccount != null && amount != 0)
							account.transfer(amount, transferAccount);
						break;
					}

				}

			}
		}

	}

	// Choose a bank account to transfer money to. Parameter specifies to current
	// account that is used since it cannot be transferred to itself
	private BankAccount chooseAccount(BankAccount currentAccount) {

		if (accounts.size() == 1) {
			System.out.println("There are no other accounts to transfer to.");
			return null;
		}

		System.out.println("Please enter the number of the account that you would like to transfer to or 0 to go back: ");
		while (true) {
			System.out.println();
			int thisIndex = accounts.indexOf(currentAccount);

			// Print each bank account other than the currently used account
			for (int i = 0; i < accounts.size(); i++) {
				if (i != thisIndex) {
					BankAccount b = accounts.get(i);
					System.out.println(i + 1 + ". Account Number: " + b.getAccountNumber() + "    Account Balance: " + b.getAccountBalance());
				}
			}

			int choice = Main.input.nextInt();
			Main.input.nextLine();
			
			if (choice == 0)
				return null;
			
			if (choice < 0 || choice > Main.accountList.size() || choice == thisIndex)
				System.out.println(
						"That is not a valid number. Please enter the number of one of the available accounts or 0 to choose none.");
			else
				return accounts.get(choice - 1);

		}
	}

	
	//Adds an account to the customer's list of accounts
	public void addAccount(BankAccount account) {
		accounts.add(account);
	}

}
