package com.revature.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.revature.pojos.Request;
import com.revature.util.ConnectionFactory;

public class FileDaoPostgres implements FileDao {

	Connection conn = ConnectionFactory.getConnectionFactory().createConnection();

	@Override
	public void makeFile(File file, Request req) {

		try {
			FileInputStream fis = new FileInputStream(file);
			PreparedStatement ps = conn.prepareStatement("insert into rmsfile (requestid, file) values (?, ?)");
			ps.setInt(1, req.getRequestId());
			ps.setBinaryStream(2, fis, file.length());
			ps.executeUpdate();
			ps.close();
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public File readFile(int id) {

		try {
			
			PreparedStatement ps = conn.prepareStatement("select * from rmsfile where fileid = ?");
			ps.setInt(1, id);
			
			ResultSet rs = ps.executeQuery();
			if(rs != null) {
				while(rs.next()) {
					byte[] fileBytes = rs.getBytes(1);
					FileOutputStream fos = new FileOutputStream("file");
					fos.write(fileBytes);
				}
				rs.close();
			}
			ps.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public File readAllForRequest(int reqId) {
		// TODO Auto-generated method stub
		return null;
	}

}
