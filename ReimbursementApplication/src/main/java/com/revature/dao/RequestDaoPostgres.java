package com.revature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.revature.pojos.Employee;
import com.revature.pojos.Request;
import com.revature.pojos.Request.EventType;
import com.revature.pojos.Request.GradingFormat;
import com.revature.util.ConnectionFactory;

public class RequestDaoPostgres implements RequestDao {

	Connection conn = ConnectionFactory.getConnectionFactory().createConnection();
	EmployeeDao eDao = new EmployeeDaoPostgres();
	
	@Override
	public void create(Request req) {
		System.out.println("in RequestDao create");
		if (req != null) {
			try {
				String sql = "insert into reimbursementrequest (employeeid, requestdate, startdate, enddate, location, description, cost, gradingformatid, typeid, justification, status, comments, awardedamount, missedhours) values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				PreparedStatement stmt = conn.prepareStatement(sql);
				stmt.setInt(1, req.getEmployee().getEmployeeId());
				stmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
				stmt.setTimestamp(3, Timestamp.valueOf(req.getStartDate().atStartOfDay()));
				stmt.setTimestamp(4, Timestamp.valueOf(req.getEndDate().atStartOfDay()));
				stmt.setString(5, req.getLocation());
				stmt.setString(6, req.getDescription());
				stmt.setDouble(7, req.getCost());
				stmt.setInt(8, req.getFormat().getValue());
				stmt.setInt(9, req.getType().getValue());
				stmt.setString(10, req.getJustification());
				stmt.setInt(11, req.getStatus());
				stmt.setString(12, req.getComments());
				stmt.setDouble(13, 0);
				stmt.setInt(14, req.getMissedHours());
				stmt.executeUpdate();
				
				sql = "select max(requestid) from reimbursementrequest";
				stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery();
				if (rs.next()) {
					req.setRequestId(rs.getInt(1));
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

	public List<Request> readAll(){
		
		String sql = "select * from reimbursementrequest";
		List<Request> requests = new ArrayList<Request>();

		try {
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {

				Employee emp = eDao.getEmployeeById(rs.getInt(2));
				GradingFormat format = GradingFormat.values()[rs.getInt(9) - 1];
				EventType type = EventType.values()[rs.getInt(10) - 1];
				
				Request request = new Request(rs.getInt(1), emp, rs.getDate(3).toLocalDate(),
						rs.getDate(4).toLocalDate(), rs.getDate(5).toLocalDate(), rs.getString(6), rs.getString(7),
						rs.getDouble(8), format, type, rs.getString(11), rs.getInt(12), rs.getString(13), rs.getInt(14));

				requests.add(request);

			}

			return requests;

		} catch (SQLException e) {
			System.out.println("Something went wrong when reading the requests by employee.");
			e.printStackTrace();
		}

		return null;
	}
	
	@Override
	public List<Request> readAllForEmployee(Employee emp) {
		String sql = "select * from reimbursementrequest where employeeid = ?";
		List<Request> requests = new ArrayList<Request>();

		try {
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, emp.getEmployeeId());
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {

				GradingFormat format = GradingFormat.values()[rs.getInt(9) - 1];
				EventType type = EventType.values()[rs.getInt(10) - 1];
				
				Request request = new Request(rs.getInt(1), emp, rs.getDate(3).toLocalDate(),
						rs.getDate(4).toLocalDate(), rs.getDate(5).toLocalDate(), rs.getString(6), rs.getString(7),
						rs.getDouble(8), format, type, rs.getString(11), rs.getInt(12), rs.getString(13), rs.getInt(14));

				requests.add(request);

			}

			return requests;

		} catch (SQLException e) {
			System.out.println("Something went wrong when reading the requests by employee.");
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public Request getRequestById(int requestId) {
		String sql = "select * from reimbursementrequest where requestid = ?";

		try {
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, requestId);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				
				Employee emp = eDao.getEmployeeById(rs.getInt(2));
				GradingFormat format = GradingFormat.values()[rs.getInt(9)];
				EventType type = EventType.values()[rs.getInt(10)];
				
				return new Request(rs.getInt(1), emp, rs.getDate(3).toLocalDate(),
						rs.getDate(4).toLocalDate(), rs.getDate(5).toLocalDate(), rs.getString(6), rs.getString(7),
						rs.getDouble(8), format, type, rs.getString(11), rs.getInt(12), rs.getString(13), rs.getInt(14));
			}

		} catch (SQLException e) {
			System.out.println("Something went wrong when trying to get the request.");
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public void updateRequest(Request req) {
		
		String sql = "update reimbursementrequest set awardedamount = ?, status = ?, comments = ? where requestid = ?";

		try {
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setDouble(1, req.getAwardedAmount());
			stmt.setInt(2, req.getStatus());
			stmt.setString(3, req.getComments());
			stmt.setInt(4, req.getRequestId());
			stmt.executeUpdate();


		} catch (SQLException e) {
			System.out.println("Something went wrong when trying to update the request.");
			e.printStackTrace();
		}
		
	}

}
