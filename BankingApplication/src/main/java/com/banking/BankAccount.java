package com.banking;

import java.io.Serializable;
import java.text.DecimalFormat;

public class BankAccount implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1875580490562562973L;

	DecimalFormat df = new DecimalFormat("###,###.##");

	private Customer customer;
	private Customer jointCustomer;
	private double accountBalance;
	private long accountNumber;
	private int accountID;

	public int getAccountid() {
		return accountID;
	}

	public void setAccountid(int accountid) {
		this.accountID = accountid;
	}

	public Customer getJointCustomer() {
		return jointCustomer;
	}

	public void setJointCustomer(Customer jointCustomer) {
		this.jointCustomer = jointCustomer;
	}

	public BankAccount() {
	}

	public BankAccount(Customer customer) {
		this.customer = customer;
		this.accountBalance = 0;
		this.accountNumber = generateAccountNumber();
		this.jointCustomer = null;

	}

	public BankAccount(Customer customer1, Customer customer2) {
		this.customer = customer1;
		this.jointCustomer = customer2;
		this.accountBalance = 0;
		this.accountNumber = generateAccountNumber();
	}

	public BankAccount(int id, Customer customer1, Customer customer2, long accountNumber, double accountBalance) {
		this.accountID = id;
		this.customer = customer1;
		this.jointCustomer = customer2;
		this.accountNumber = accountNumber;
		this.accountBalance = accountBalance;
	}

	public String cleanString() {
		if(jointCustomer == null)
			return "Customer: " + customer.getFirstName() + " " + customer.getLastName() + "    Account Number: " + accountNumber + "    Account Balance: " + "$" + df.format(accountBalance);
		else
			return "Customer1: " + customer.getFirstName() + " " + customer.getLastName() + "   Customer2: " + jointCustomer.getFirstName() + " " + jointCustomer.getLastName() + "    Account Number: " + accountNumber + "    Account Balance: " + "$" + df.format(accountBalance);
	}

	@Override
	public String toString() {
		return "BankAccount [customer=" + customer + ", jointCustomer=" + jointCustomer + ", accountBalance="
				+ accountBalance + ", accountNumber=" + accountNumber + "]";
	}

	// generate a random unique account number
	private long generateAccountNumber() {

		boolean unique = true;
		long number = 0;

		do {
			unique = true;
			number = (long)(Math.random() * 90000000L + 10000000L);
			if (Main.accountList.isEmpty())
				return number;
			for (BankAccount account : Main.accountList) {
				if (account.getAccountNumber() == number) {
					unique = false;
					break;
				}
			}

		}while (!unique);
		return number;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public double getAccountBalance() {
		return accountBalance;
	}

	public void setAccountBalance(double accountBalance) {
		this.accountBalance = accountBalance;
	}

	// prints the account balance for user to see
	public void checkBalance() {
		System.out.println("The account balance of account " + accountNumber + " is $" + df.format(accountBalance));
	}

	// withdraws specified amount from account
	public void withdraw(double amount) {

		if (amount > accountBalance) {
			System.out.println("The entered amount is more than the account balance and cannot be withdrawn.");
			return;
		}
		if (amount < 0) {
			System.out.println("The entered amount cannot be less than zero.");
			return;
		}

		accountBalance -= amount;
		String message = "$" + df.format(amount) + " has been withdrawn from account " + accountNumber;
		System.out.println(message);
		JavaLog4j.logger.info(message);
		Main.logDao.create(message);
		updateAccountList();
	}

	// deposits specified amount into account
	public void deposit(double amount) {

		if (amount < 0) {
			System.out.println("The entered amount cannot be less than zero.");
			return;
		}

		accountBalance += amount;
		String message = "$" + df.format(amount) + " has been deposited to account " + accountNumber + ".";
		System.out.println(message);
		JavaLog4j.logger.info(message);
		Main.logDao.create(message);
		updateAccountList();
	}

	// Transfers the specified amount from this account to the account specified as
	// a parameter
	public void transfer(double amount, BankAccount account) {
		if (amount > accountBalance) {
			System.out.println("The entered amount is more than the account balance and cannot be transfered.");
			return;
		}
		if (amount < 0) {
			System.out.println("The entered amount cannot be less than zero.");
			return;
		}

		this.accountBalance -= amount;
		account.accountBalance += amount;
		String message = "$" + df.format(amount) + " has been transferred from account " + this.accountNumber + " to account " + account.accountNumber;
		System.out.println(message);
		JavaLog4j.logger.info(message);
		Main.logDao.create(message);
		updateAccountList();
		Main.accountDao.update(account);
	}

	public long getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(int accountNumber) {
		this.accountNumber = accountNumber;
	}

	//serializes the list of accounts each time a change is made
	public void updateAccountList() {
		Main.writeObject(Main.accountFile, Main.accountList);
		Main.accountDao.update(this);
	}

	public void cancel() {
		System.out.println();
		String message = "Account " + getAccountNumber() + " has been cancelled.";
		System.out.println(message);
		System.out.println();
		JavaLog4j.logger.info(message);
		Main.logDao.create(message);
		removeAccountFromCustomers();
		Main.accountDao.deactivate(this);
		Main.accountList.remove(this);
		Main.writeObject(Main.accountFile, Main.accountList);
	}
	
	private void removeAccountFromCustomers() {
		for (Customer customer : Main.customerList) {
			if (this.getCustomer().getUsername().equals(customer.getUsername())) {
				customer.removeAccount(this);
			}
			if (this.getJointCustomer() != null
					&& this.getJointCustomer().getUsername().equals(customer.getUsername())) {
				customer.removeAccount(this);
			}
		}

	}
}
