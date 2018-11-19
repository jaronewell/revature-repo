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
		// accounts, the pending application is removed, and the user is notified of the approval
		customer.addAccount(newAccount);
		customer.setPendingApplication(null);
		customer.setApplicationApproved(true);
		
		Main.accountDao.create(newAccount);
		
		Main.accountList.add(newAccount);
		Main.writeObject(Main.accountFile, Main.accountList);
		
		Main.writeObject(Main.applicationFile, Main.applicationList);
		customer.setApplicationApproved(true);
		
		//If there is a joint customer the joint customer also adds the new account and the pending
		//application is removed
		if(isJoint) {
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
		Main.writeObject(Main.applicationFile, Main.applicationList);
		
		customer.setApplicationDenied(true);
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

	public int getApplicationID() {
		return applicationID;
	}

	public void setApplicationID(int applicationID) {
		this.applicationID = applicationID;
	}

}
