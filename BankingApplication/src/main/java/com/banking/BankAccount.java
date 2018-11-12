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

	public String cleanString() {
		if(jointCustomer == null)
			return "Customer: " + customer.getFirstName() + " " + customer.getLastName() + "    Account Number: " + accountNumber + "    Account Balance: " + "$" + df.format(accountBalance);
		else
			return "Customer1: " + customer.getFirstName() + " " + customer.getLastName() + "   Customer2: " + jointCustomer.getFirstName() + " " + jointCustomer.getLastName() + "    Account Number: " + accountNumber + " Account Balance: " + "$" + df.format(accountBalance);
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
		System.out.println("Your account balance is $" + df.format(accountBalance));
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
		System.out.println("$" + df.format(amount) + " has been withdrawn from your account.");
		JavaLog4j.logger.info("$" + df.format(amount) + " has been withdrawn from account " + accountNumber);
		updateAccountList();
	}

	// deposits specified amount into account
	public void deposit(double amount) {

		if (amount < 0) {
			System.out.println("The entered amount cannot be less than zero.");
			return;
		}

		accountBalance += amount;
		System.out.println("$" + df.format(amount) + " has been deposited to your account.");
		JavaLog4j.logger.info("$" + df.format(amount) + " has been deposited in account " + accountNumber);
		updateAccountList();
	}

	// Transfers the specified amount from this account to the account specified as
	// a parameter
	public void transfer(double amount, BankAccount account) {
		if (amount > accountBalance)
			System.out.println("The entered amount is more than the account balance and cannot be transfered.");
		if (amount < 0)
			System.out.println("The entered amount cannot be less than zero.");

		this.accountBalance -= amount;
		account.accountBalance += amount;
		System.out.println("$" + df.format(amount) + " has been transferred from account " + this.accountNumber + " to account " + account.accountNumber);
		JavaLog4j.logger.info("$" + df.format(amount) + " has been transferred from account " + this.accountNumber + " to account " + account.accountNumber);
		updateAccountList();
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
	}
}
