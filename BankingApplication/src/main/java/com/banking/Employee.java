package com.banking;

import java.util.ArrayList;

import com.banking.Application.StatusState;

public class Employee extends User {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6421231273659462731L;
	protected ArrayList<BankAccount> accessibleAccounts;

	public Employee(String username, String password) {
		this.setUsername(username);
		this.setPassword(password);
	}

	public void viewCustomers() {

		Main.printList(Main.customerList, "Customers");
	}

	public void viewAccounts() {

		if (getType().equals(UserType.Admin))
			allowAccountChanges();
		else
			Main.printList(Main.accountList, "Bank Accounts");
	}

	private void allowAccountChanges() {
		BankAccount accountToEdit;
		accountToEdit = chooseAccount();

		if (accountToEdit == null)
			return;

		while (true) {
			System.out.println(
					"Select an option below or type \"back\" to return to the acccount selection or \"quit\" to quit.");
			System.out.println("1. Check Account Balance");
			System.out.println("2. Deposit");
			System.out.println("3. Withdraw");
			System.out.println("4. Transfer");
			System.out.println("5. Cancel Account");

			String choice = Main.input.nextLine();

			switch (choice) {
			case "quit":
				Main.currentUser = null;
				return;
			case "back":
				return;
			case "1":
				accountToEdit.checkBalance();
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
					accountToEdit.deposit(amount);
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
					accountToEdit.withdraw(amount);
				break;
			case "4":
				System.out.println("How much money would you like to transfer? ");
				amount = 0;
				try {
					amount = Double.parseDouble(Main.input.nextLine());
				} catch (Exception e) {
					System.out.println("That input is invalid please be sure to enter a decimal number only.");
				}
				BankAccount transferAccount = chooseAccount();
				if(transferAccount.getAccountNumber() == accountToEdit.getAccountNumber())
					System.out.println("A different account must be selected");
				else if(transferAccount != null && amount > 0)
					accountToEdit.transfer(amount, transferAccount);
				break;
			case "5":
				Main.accountList.remove(accountToEdit);
				Main.writeObject(Main.accountFile, Main.accountFile);
				break;
			}
		}
	}

	private BankAccount chooseAccount() {
		if (Main.accountList.size() == 0) {
			System.out.println("There are no currently opened bank accounts.");
			return null;
		}

		System.out.println("Please enter the number of the account that you would like to edit or 0 to go back: ");
		while (true) {
			System.out.println();

			// Print each bank account
			for (int i = 0; i < Main.accountList.size(); i++) {
				BankAccount b = Main.accountList.get(i);
				System.out.println(i + 1 + ". " + b.cleanString());
			}

			int choice = Main.input.nextInt();
			Main.input.nextLine();

			if (choice == 0)
				return null;

			if (choice < 0 || choice > Main.accountList.size())
				System.out.println(
						"That is not a valid number. Please enter the number of one of the available accounts or 0 to choose none.");
			else
				return Main.accountList.get(choice - 1);
		}
	}

	// Employee can view all pending applications and approve or deny them
	public void viewPendingApplications() {

		while (true) {
			int numPending = 0;

			System.out.println();
			System.out.println("Pending Applications");
			for (int i = 0; i < Main.applicationList.size(); i++) {
				Application a = Main.applicationList.get(i);
				if (a.getStatus() == StatusState.Pending) {
					System.out.println(i + 1 + ". " + a);
					numPending++;
				}
			}

			if (numPending == 0) {
				System.out.println("There are no pending applications.");
				System.out.println();
				return;
			}
			System.out.print(
					"Enter the number of the application you would like to approve or deny or enter 0 to quit: ");

			int index = Main.input.nextInt();
			Main.input.nextLine();

			if (index == 0)
				return;

			if (index > Main.applicationList.size() || index < 0) {
				System.out.println("That is not a valid application number. Please enter a different number. ");
			} else {
				// The current application to edit is the application from the list with the
				// index that was entered minus one since the first application is actually 0
				// index
				Application a = Main.applicationList.get(index - 1);

				if (a.getStatus() != StatusState.Pending) {
					System.out.println("The application number you entered has already been approved or denied.");
				} else
					System.out.println(a);
				System.out.println("1. Approve");
				System.out.println("2. Deny");

				String choice = Main.input.nextLine();

				switch (choice) {
				case "quit":
					Main.currentUser = null;
					return;
				case "1":
					a.approve();
					System.out.println("The application has been approved.");
					System.out.println();
					break;
				case "2":
					a.deny();
					System.out.println("The application has been denied");
					System.out.println();
					break;
				default:
					System.out.println("That was not a valid input. Please enter 1 to approve or 2 to deny.");
				}

			}
		}

	}

	@Override
	public void showOptions() {
		while (Main.currentUser == this) {
			System.out.println(
					"Please enter the number of the choice you would like or type \"quit\" to return to user selection.");
			System.out.println("1. View Pending Applications");
			System.out.println("2. View Customers");
			System.out.println("3. View Accounts");

			String choice = Main.input.nextLine();

			switch (choice) {
			case "quit":
				Main.currentUser = null;
				return;
			case "1":
				viewPendingApplications();
				break;
			case "2":
				viewCustomers();
				break;
			case "3":
				viewAccounts();
				break;
			default:
				System.out.println("That is not a valid input please enter 1, 2, 3 or \"quit\".");
				break;
			}
		}

	}

}
