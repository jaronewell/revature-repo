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

		if (Main.accountList.isEmpty()) {
			System.out.println();
			System.out.println("There are currently no opened accounts.");
			System.out.println();
			return;
		}
		if (getType().equals(UserType.Admin))
			allowAccountChanges();
		else
			// Print each bank account
			for (int i = 0; i < Main.accountList.size(); i++) {
				BankAccount b = Main.accountList.get(i);
				System.out.println(i + 1 + ". " + b.cleanString());
			}
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
				if (Main.accountList.size() == 1) {
					System.out.println("There are no other accounts to transfer to.");
					break;
				}

				// select the account to transfer the money to
				BankAccount transferAccount = chooseTransferAccount(accountToEdit);
				System.out.println("How much money would you like to transfer? ");
				amount = 0;
				try {
					amount = Double.parseDouble(Main.input.nextLine());
				} catch (Exception e) {
					System.out.println("That input is invalid please be sure to enter a decimal number only.");
				}
				if (transferAccount != null && amount != 0)
					accountToEdit.transfer(amount, transferAccount);
				break;
			case "5":
				System.out.println();
				System.out.println("Account " + accountToEdit.getAccountNumber() + " has been cancelled.");
				System.out.println();
				JavaLog4j.logger.info("Account " + accountToEdit.getAccountNumber() + " has been cancelled.");
				removeAccountFromCustomers(accountToEdit);
				Main.accountDao.deactivate(accountToEdit);
				Main.accountList.remove(accountToEdit);
				Main.writeObject(Main.accountFile, Main.accountList);
				return;
			}
		}
	}

	private void removeAccountFromCustomers(BankAccount accountToEdit) {
		for (Customer customer : Main.customerList) {
			if (accountToEdit.getCustomer().getUsername().equals(customer.getUsername())) {
				customer.removeAccount(accountToEdit);
			}
			if (accountToEdit.getJointCustomer() != null
					&& accountToEdit.getJointCustomer().getUsername().equals(customer.getUsername())) {
				customer.removeAccount(accountToEdit);
			}
		}

	}

	private BankAccount chooseTransferAccount(BankAccount currentAccount) {

		System.out.println(
				"Please enter the account number of the account that you would like to transfer to or \"back\" to go back: ");
		while (true) {
			System.out.println();
			int thisIndex = Main.accountList.indexOf(currentAccount);

			// Print each bank account other than the currently used account
			for (int i = 0; i < Main.accountList.size(); i++) {
				if (i != thisIndex) {
					BankAccount b = Main.accountList.get(i);
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

	private BankAccount chooseAccount() {
		if (Main.accountList.size() == 0) {
			System.out.println("There are no currently opened bank accounts.");
			return null;
		}

		System.out.println("Please enter the number of the account that you would like to edit or \"back\" to go back: ");
		while (true) {
			System.out.println();

			// Print each bank account
			for (int i = 0; i < Main.accountList.size(); i++) {
				BankAccount b = Main.accountList.get(i);
				System.out.println(i + 1 + ". " + b.cleanString());
			}

			String choice = Main.input.nextLine();
			

			if (choice.equals("back") || choice.equals("quit"))
				return null;

			int index = 0;
			try {
				index = Integer.parseInt(choice);
			}catch(Exception e) {
				System.out.println("That input is invalid. Please be sure to input a number only or \"back\".");
			}
			
			if (index <= 0 || index > Main.accountList.size())
				System.out.println(
						"That is not a valid number. Please enter the number of one of the available accounts or \"back\" to choose none.");
			else
				return Main.accountList.get(index - 1);
		}
	}

	// Employee can view all pending applications and approve or deny them
	public void viewPendingApplications() {

		ArrayList<Application> pendingApplications = new ArrayList<Application>();

		while (true) {
			int numPending = 0;

			System.out.println();
			System.out.println("Pending Applications");
			for (int i = 0; i < Main.applicationList.size(); i++) {
				Application a = Main.applicationList.get(i);
				if (a.getStatus() == StatusState.Pending) {
					pendingApplications.add(a);
					numPending++;
					System.out.println(numPending + ". " + a);
				}
			}

			if (numPending == 0) {
				System.out.println("There are no pending applications.");
				System.out.println();
				return;
			}
			System.out.println();
			System.out.print(
					"Enter the number of the application you would like to approve or deny or enter \"back\" to go back: ");

			String choice = Main.input.nextLine();

			if (choice.equals("back"))
				return;

			int index = 0;
			
			try {
			index = Integer.parseInt(choice);
			}catch(Exception e) {
				System.out.println("That input is invalid. Please only enter numbers or \"back\".");
			}
			
			
			if (index > Main.applicationList.size() || index < 0) {
				System.out.println("That is not a valid application number. Please enter a different number. ");
			} else {
				// The current application to edit is the application from the list with the
				// index that was entered minus one since the first application is actually 0
				// index
				Application a = pendingApplications.get(index - 1);

				if (a.getStatus() != StatusState.Pending) {
					System.out.println("The application number you entered has already been approved or denied.");
				} else
					System.out.println(a);
				System.out.println("1. Approve");
				System.out.println("2. Deny");

				choice = Main.input.nextLine();

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
			System.out.println();
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
