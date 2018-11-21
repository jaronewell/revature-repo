package com.revature.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.banking.Customer;
import com.revature.util.ConnectionFactory;

public class CustomerDaoPostgres implements CustomerDao {

	private static Connection conn = ConnectionFactory.getConnectionFactory().createConnection();

	@Override
	public void create(Customer c) {
		if (c != null) {
			try {
				String sql = "Insert into customer (firstname, lastname, phone, address, email, "
						+ "dob, ssn, username, password) values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
				PreparedStatement stmt = conn.prepareStatement(sql);
				stmt.setString(1, c.getFirstName());
				stmt.setString(2, c.getLastName());
				stmt.setString(3, c.getPhoneNumber());
				stmt.setString(4, c.getAddress());
				stmt.setString(5, c.getEmail());
				stmt.setTimestamp(6, Timestamp.valueOf(c.getDob().atStartOfDay()));
				stmt.setString(7, c.getSsn());
				stmt.setString(8, c.getUsername());
				stmt.setString(9, c.getPassword());
				stmt.executeUpdate();

				sql = "select max(customerid) from customer";
				stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery();
				if (rs.next()) {
					c.setCustomerID(rs.getInt(1));
				} else {
					throw new SQLException();
				}

			} catch (SQLException e) {
				System.out.println("Problem creating Customer");
				e.printStackTrace();
			}
		} else {
			System.out.println("The customer is null.");
			throw new IllegalArgumentException();
		}

	}

	@Override
	public void update(Customer c) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Customer> readAccountCustomers(int accountId) {
		String sql = "{call get_account_customers(?)}";
		List<Customer> customerList = new ArrayList<Customer>();
		customerList.add(null);
		customerList.add(null);

		try {
			conn.setAutoCommit(false);
			CallableStatement cstmt = conn.prepareCall(sql);
			cstmt.setInt(1, accountId);
			ResultSet rs = cstmt.executeQuery();
			rs.next();

			Object rs2 = rs.getObject(1);
			int indexToSet = 0;
			if (rs2 instanceof ResultSet) {
				ResultSet resultSet = (ResultSet) rs2;
				while (((ResultSet) rs2).next()) {
					int id = resultSet.getInt(1);
					String firstName = resultSet.getString(2);
					String lastName = resultSet.getString(3);
					String phoneNumber = resultSet.getString(4);
					String address = resultSet.getString(5);
					String email = resultSet.getString(6);
					LocalDate dob = resultSet.getDate(7).toLocalDate();
					String ssn = resultSet.getString(8);
					String username = resultSet.getString(9);
					String password = resultSet.getString(10);

					
					customerList.set(indexToSet, (new Customer(id, firstName, lastName, phoneNumber, address, email, dob, ssn,
							username, password)));
					indexToSet++;
				}
				resultSet.close();
				conn.setAutoCommit(true);
			}
			return customerList;
		} catch (SQLException e) {
			System.out.println("There was a problem reading customers");
			e.printStackTrace();

		}

		return null;
	}

	@Override
	public List<Customer> readAll() {

		String sql = "select * from customer";
		List<Customer> customers = new ArrayList<Customer>();

		try {
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Customer customer = new Customer(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
						rs.getString(6), rs.getTimestamp(7).toLocalDateTime().toLocalDate(), rs.getString(8), rs.getString(9), rs.getString(10));
				customers.add(customer);

			}

			return customers;

		} catch (SQLException e) {
			System.out.println("Something went wrong when reading the accounts.");
			e.printStackTrace();
		}

		return new ArrayList<Customer>();
	}

	@Override
	public List<Customer> readApplicationCustomers(int applicationID) {
		String sql = "{call get_application_customers(?)}";
		List<Customer> customerList = new ArrayList<Customer>();
		customerList.add(null);
		customerList.add(null);

		try {
			conn.setAutoCommit(false);
			CallableStatement cstmt = conn.prepareCall(sql);
			cstmt.setInt(1, applicationID);
			ResultSet rs = cstmt.executeQuery();
			rs.next();

			Object rs2 = rs.getObject(1);
			int indexToSet = 0;
			if (rs2 instanceof ResultSet) {
				ResultSet resultSet = (ResultSet) rs2;
				while (((ResultSet) rs2).next()) {
					int id = resultSet.getInt(1);
					String firstName = resultSet.getString(2);
					String lastName = resultSet.getString(3);
					String phoneNumber = resultSet.getString(4);
					String address = resultSet.getString(5);
					String email = resultSet.getString(6);
					LocalDate dob = resultSet.getDate(7).toLocalDate();
					String ssn = resultSet.getString(8);
					String username = resultSet.getString(9);
					String password = resultSet.getString(10);

					
					customerList.set(indexToSet, (new Customer(id, firstName, lastName, phoneNumber, address, email, dob, ssn,
							username, password)));
					indexToSet++;
				}
				resultSet.close();
				conn.setAutoCommit(true);
			}
			return customerList;
		} catch (SQLException e) {
			System.out.println("There was a problem reading customers");
			e.printStackTrace();

		}
		
		return null;
	}

}
