package com.banking;

import java.io.Serializable;
import java.util.ArrayList;

public class Application implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3029590756484476997L;
	private Customer customer;
	private boolean isJoint;
	private Customer jointCustomer;
	private StatusState status;

	public enum StatusState {
		Pending, Approved, Denied
	}

	public Application(Customer customer) {
		this.customer = customer;
		this.isJoint = false;
		this.jointCustomer = null;
		setStatus(StatusState.Pending);
	}

	public Application(Customer customer1, Customer customer2) {
		this.customer = customer1;
		this.isJoint = true;
		this.jointCustomer = customer2;
		setStatus(StatusState.Pending);
	}

	public void approve() {
		setStatus(StatusState.Approved);
		BankAccount newAccount;
		if (!isJoint)
			newAccount = new BankAccount(customer);
		else
			newAccount = new BankAccount(customer, jointCustomer);

		// After the application is approved the new account is added to the list of
		// accounts
		ArrayList<BankAccount> accountList = customer.getAccounts();
		accountList.add(newAccount);
		customer.setAccounts(accountList);
		
		ArrayList<Application> customerPendingApplications = customer.getPendingApplications();
		customerPendingApplications.remove(this);
		customer.setPendingApplications(customerPendingApplications);
		
		Main.accountList.add(newAccount);
		Main.writeObject(Main.accountFile, Main.accountList);
		
		//The pending application is removed from the customer's pending application list
		ArrayList<Application> pendingApplications = customer.getPendingApplications();
		pendingApplications.remove(this);
		customer.setPendingApplications(pendingApplications);
		Main.writeObject(Main.applicationFile, Main.applicationList);
	}

	public void deny() {
		setStatus(StatusState.Denied);
		Main.writeObject(Main.applicationFile, Main.applicationList);
	}

	public StatusState getStatus() {
		return status;
	}

	public void setStatus(StatusState status) {
		this.status = status;
		Main.writeObject(Main.applicationFile, Main.applicationList);
	}

	@Override
	public String toString() {
		return "Application [customer=" + customer + ", isJoint=" + isJoint + ", jointCustomer=" + jointCustomer
				+ ", status=" + status + "]";
	}

}
