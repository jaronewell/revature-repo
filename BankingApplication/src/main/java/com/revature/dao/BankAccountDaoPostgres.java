package com.revature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.banking.BankAccount;
import com.banking.Customer;
import com.revature.util.ConnectionFactory;

public class BankAccountDaoPostgres implements BankAccountDao {

	private static Connection conn = ConnectionFactory.getConnectionFactory().createConnection();
	private static CustomerDao customerDao = new CustomerDaoPostgres();
	
	@Override
	public void create(BankAccount b) {
		if (b != null) {
			try {
				String sql = "Insert into bankaccount (accountnumber, accountbalance, active)" + " values (?, ?, ?)";
				PreparedStatement stmt = conn.prepareStatement(sql);
				stmt.setLong(1, b.getAccountNumber());
				stmt.setDouble(2, b.getAccountBalance());
				stmt.setBoolean(3, true);
				stmt.executeUpdate();
				addToJoinTable(b);
			} catch (SQLException e) {
				System.out.println("Problem creating Bank Account");
				e.printStackTrace();
			}
		} else {
			System.out.println("The bank account is null.");
			throw new IllegalArgumentException();
		}
	}

	private void addToJoinTable(BankAccount b) {

		try {
			//find the account id of the newly created account
			String sql = "select max(accountid) from bankaccount";
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				b.setAccountid(rs.getInt(1));
			}
			else {
				throw new SQLException();
			}
			
			sql = "insert into accountcustomer values (?, ?)";
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, b.getAccountid());
			stmt.setInt(2, b.getCustomer().getCustomerID());
			stmt.executeUpdate();
			if(b.getJointCustomer() != null) {
				stmt.setInt(2, b.getJointCustomer().getCustomerID());
				stmt.executeUpdate();
			}
		}catch (SQLException e) {
			System.out.println("Problem adding to account-customer join table.");
			e.printStackTrace();
		}
	}

	
	@Override
	public void deactivate(BankAccount b) {
		try {
			String sql = "update bankaccount set active = false where accountnumber = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setLong(1, b.getAccountNumber());
			stmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Problem deleting Bank Account");
			e.printStackTrace();
		}
	}

	@Override
	public void update(BankAccount b) {
		if (b != null) {
			try {
				String sql = "update bankaccount set accountbalance = ? where accountnumber = ?";
				PreparedStatement stmt = conn.prepareStatement(sql);
				stmt.setDouble(1, b.getAccountBalance());
				stmt.setLong(2, b.getAccountNumber());
				stmt.executeUpdate();
			} catch (SQLException e) {
				System.out.println("Problem updating Bank Account");
				e.printStackTrace();
			}
		} else {
			System.out.println("The bank account is null.");
			throw new IllegalArgumentException();
		}
	}

	@Override
	public BankAccount read(int id) {
		try {
			String sql = "select * from bankaccount where accountid = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			BankAccount account = null;

			List<Customer> customers = customerDao.readCustomers(id);

			if (rs.next() && customers != null) {
				account = new BankAccount(id, customers.get(0), customers.get(1), rs.getLong(2), rs.getDouble(3));
			}

			return account;

		} catch (SQLException e) {
			System.out.println("Problem reading Bank Account");
			e.printStackTrace();
		}

		return null;
	}

	
	@Override
	public List<BankAccount> readAll() {

		String sql = "select * from bankaccount";
		List<BankAccount> accounts = new ArrayList<BankAccount>();

		try {
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				if (rs.getBoolean(4)) {
					List<Customer> customers = customerDao.readCustomers(rs.getInt(1));
					BankAccount account = new BankAccount(rs.getInt(1), customers.get(0), customers.get(1),
							rs.getLong(2), rs.getDouble(3));
					accounts.add(account);
				}
			}
			
			return accounts;
			
		} catch (SQLException e) {
			System.out.println("Something went wrong when reading the accounts.");
			e.printStackTrace();
		}

		return new ArrayList<BankAccount>();
	}

}
