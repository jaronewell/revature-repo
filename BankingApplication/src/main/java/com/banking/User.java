package com.banking;

import java.io.Serializable;

public abstract class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3454272138700399244L;

	enum UserType {
		Customer, Employee, Admin
	}

	private UserType type;

	private String username;
	private String password;

	public abstract void showOptions();

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public UserType getType() {
		return type;
	}

	public void setType(UserType type) {
		this.type = type;
	}

	// Return the bank account with the account number equal to the parameter or
	// null if there is none.
	protected BankAccount findAccount(long accountNumber) {

		for (BankAccount account : Main.accountList) {
			if (account.getAccountNumber() == accountNumber)
				return account;
		}

		return null;
	}
	
	

}
