package com.revature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.revature.pojos.Employee;
import com.revature.util.ConnectionFactory;

public class EmployeeDaoPostgres implements EmployeeDao {

	private static Connection conn = ConnectionFactory.getConnectionFactory().createConnection();
	
	@Override
	public void create(Employee emp) {
		System.out.println("In create employee");
		System.out.println(conn);
		if (emp != null) {
			try {
				String sql = "insert into rmsemployee (firstname, lastname, username, password, supervisor, departmenthead, type) values ( ?, ?, ?, ?, ?, ?, ?)";
				PreparedStatement stmt = conn.prepareStatement(sql);
				stmt.setString(1, emp.getFirstName());
				stmt.setString(2, emp.getLastName());
				stmt.setString(3, emp.getUsername());
				stmt.setString(4, emp.getPassword());
				stmt.setInt(5, emp.getSupervisorId());
				stmt.setInt(6, emp.getDepartmentHeadId());
				
				sql = "select max(employeeid) from rmsemployee";
				stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery();
				if (rs.next()) {
					emp.setEmployeeId(rs.getInt(1));
				} else {
					throw new SQLException();
				}

			} catch (SQLException e) {
				System.out.println("Problem creating Employee");
				e.printStackTrace();
			}
		} else {
			System.out.println("The employee is null.");
			throw new IllegalArgumentException();
		}
	}

	@Override
	public List<Employee> readAll() {
		String sql = "select * from rmsemployee";
		List<Employee> employees = new ArrayList<Employee>();

		try {
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Employee employee = new Employee(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6), rs.getInt(7), Employee.EmployeeType.values()[rs.getInt(8)]);
				
				employees.add(employee);

			}

			return employees;

		} catch (SQLException e) {
			System.out.println("Something went wrong when reading the accounts.");
			e.printStackTrace();
		}

		return new ArrayList<Employee>();
	}

	@Override
	public Employee getEmployeeByUsername(String username) {
		
		String sql = "select * from rmsemployee where username = ?";

		try {
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				return new Employee(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6), rs.getInt(7), Employee.EmployeeType.values()[rs.getInt(8)]);
			}

		} catch (SQLException e) {
			System.out.println("Something went wrong when reading the accounts.");
			e.printStackTrace();
		}
		
		return null;
	}

	
	@Override
	public Employee getSupervisorByEmployee(Employee emp) {
		String sql = "select * from rmsemployee where employeeid = ?";

		try {
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, emp.getSupervisorId());
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				return new Employee(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6), rs.getInt(7), Employee.EmployeeType.values()[rs.getInt(8)]);
			}

		} catch (SQLException e) {
			System.out.println("Something went wrong when reading the accounts.");
			e.printStackTrace();
		}
		
		return null;
	}
	
	public Employee getDepartmentHeadByEmployee(Employee emp) {
		String sql = "select * from rmsemployee where departmentheadid = ?";

		try {
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, emp.getDepartmentHeadId());
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				return new Employee(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6), rs.getInt(7), Employee.EmployeeType.values()[rs.getInt(8)]);
			}

		} catch (SQLException e) {
			System.out.println("Something went wrong when reading the accounts.");
			e.printStackTrace();
		}
		
		return null;
	}


}
