package com.Banking;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.banking.BankAccount;

public class BankingTest {

	@Test
	public void testWithrawal() {
		BankAccount account = new BankAccount();
		account.setAccountBalance(234);
		account.withdraw(120);
		assertEquals(114.0, account.getAccountBalance(), .001);
	}
	
	@Test
	public void testDeposit() {
		BankAccount account = new BankAccount();
		account.setAccountBalance(1234);
		account.deposit(1200);
		assertEquals(2434, account.getAccountBalance(), .001);
	}
	
	@Test
	public void testTransfer() {
		BankAccount account1 = new BankAccount();
		BankAccount account2 = new BankAccount();
		account1.setAccountBalance(3422);
		account2.setAccountBalance(5233);
		account1.transfer(1000, account2);
		assertEquals(2422, account1.getAccountBalance(), .001);
		assertEquals(6233, account2.getAccountBalance(), .001);
	}
	
	@Before
	public void beforeTest() {
		System.out.println();
		System.out.println("Test");
	}
	
}
