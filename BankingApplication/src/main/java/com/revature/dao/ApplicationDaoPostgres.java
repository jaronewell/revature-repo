package com.revature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.banking.Application;
import com.banking.Customer;
import com.revature.util.ConnectionFactory;


public class ApplicationDaoPostgres implements ApplicationDao {

	private static Connection conn = ConnectionFactory.getConnectionFactory().createConnection();
	private static CustomerDao customerDao = new CustomerDaoPostgres();

	@Override
	public void create(Application a) {
		if (a != null) {
			try {
				String sql = "Insert into application (applicationstatus) values ('pending')";
				PreparedStatement stmt = conn.prepareStatement(sql);
				stmt.executeUpdate();
				addToJoinTable(a);
			} catch (SQLException e) {
				System.out.println("Problem creating Application");
				e.printStackTrace();
			}
		} else {
			System.out.println("The bank account is null.");
			throw new IllegalArgumentException();
		}
	}

	private void addToJoinTable(Application a) {

		try {
			// find the account id of the newly created account
			String sql = "select max(applicationid) from application";
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				a.setApplicationID(rs.getInt(1));
			} else {
				throw new SQLException();
			}

			sql = "insert into applicationcustomer values (?, ?)";
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, a.getApplicationID());
			stmt.setInt(2, a.getCustomer().getCustomerID());
			stmt.executeUpdate();
			if (a.getJointCustomer() != null) {
				stmt.setInt(2, a.getJointCustomer().getCustomerID());
				stmt.executeUpdate();
			}
		} catch (SQLException e) {
			System.out.println("Problem adding to application-customer join table.");
			e.printStackTrace();
		}
	}

	@Override
	public void update(Application a) {
		if (a != null) {
			try {
				String status = a.getStatus().toString().toLowerCase();
				String sql = "update application set applicationstatus = '" + status + "' where applicationid = ?";
				PreparedStatement stmt = conn.prepareStatement(sql);
				stmt.setLong(1, a.getApplicationID());
				stmt.executeUpdate();
			} catch (SQLException e) {
				System.out.println("Problem updating application");
				e.printStackTrace();
			}
		} else {
			System.out.println("The application is null.");
			throw new IllegalArgumentException();
		}
	}

	@Override
	public List<Application> readAll() {

		String sql = "select * from application";
		List<Application> applications = new ArrayList<Application>();

		try {
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {

				List<Customer> customers = customerDao.readApplicationCustomers(rs.getInt(1));
				Application application = new Application(rs.getInt(1), customers.get(0), customers.get(1));
				switch(rs.getString(2)) {
				case("pending"):
					application.setStatus(Application.StatusState.Pending);
					break;
				case("approved"):
					application.setStatus(Application.StatusState.Approved);
					break;
				case("denied"):
					application.setStatus(Application.StatusState.Denied);
				}
				applications.add(application);

			}

			return applications;

		} catch (SQLException e) {
			System.out.println("Something went wrong when reading the applications.");
			e.printStackTrace();
		}

		return new ArrayList<Application>();
	}
}
