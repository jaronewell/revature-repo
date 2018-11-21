package com.banking;

import java.io.Serializable;

public class Application implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3029590756484476997L;
	private Customer customer;
	private int applicationID;
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	private Customer jointCustomer;
	private StatusState status;

	public enum StatusState {
		Pending, Approved, Denied
	}

	public Application(Customer customer) {
		this.customer = customer;
		this.jointCustomer = null;
		setStatus(StatusState.Pending);
	}

	public Application(int applicationID, Customer customer1, Customer customer2) {
		this.applicationID = applicationID;
		this.customer = customer1;
		this.jointCustomer = customer2;
	}
	
	public Application(Customer customer1, Customer customer2) {
		this.customer = customer1;
		this.jointCustomer = customer2;
		setStatus(StatusState.Pending);
	}

	public void approve() {
		setStatus(StatusState.Approved);
		BankAccount newAccount;
		newAccount = new BankAccount(customer, jointCustomer);

		// After the application is approved the new account is added to the list of
		// accounts, the pending application is removed, and the user is notified of the approval
		customer.addAccount(newAccount);
		customer.setPendingApplication(null);
		customer.setApplicationApproved(true);
		
		Main.accountDao.create(newAccount);
		Main.logDao.create("Account " + newAccount.getAccountNumber() + " has been approved and is now active.");
		
		Main.accountList.add(newAccount);
		Main.writeObject(Main.accountFile, Main.accountList);
		
		updateStatus();
		
		customer.setApplicationApproved(true);
		
		//If there is a joint customer the joint customer also adds the new account and the pending
		//application is removed
		if(jointCustomer != null) {
			jointCustomer.setApplicationApproved(true);
			jointCustomer.addAccount(newAccount);
			jointCustomer.setPendingApplication(null);
		}
	}

	public Customer getJointCustomer() {
		return jointCustomer;
	}

	public void setJointCustomer(Customer jointCustomer) {
		this.jointCustomer = jointCustomer;
	}

	public void deny() {
		setStatus(StatusState.Denied);
		updateStatus();
		
		customer.setApplicationDenied(true);
	}

	private void updateStatus() {
		Main.writeObject(Main.applicationFile, Main.applicationList);
		Main.applicationDao.update(this);
	}

	public StatusState getStatus() {
		return status;
	}

	public void setStatus(StatusState status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Customer: " + customer.getFirstName() + " " + customer.getLastName() + ", jointCustomer: " + jointCustomer.getFirstName() + " " + jointCustomer.getLastName() 
				+ ", status:" + status;
	}

	public int getApplicationID() {
		return applicationID;
	}

	public void setApplicationID(int applicationID) {
		this.applicationID = applicationID;
	}

}
