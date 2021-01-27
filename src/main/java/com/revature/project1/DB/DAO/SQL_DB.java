package com.revature.project1.DB.DAO;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.revature.project1.DB.types.Approval;
import com.revature.project1.DB.types.ApprovalType;
import com.revature.project1.DB.types.Attachment;
import com.revature.project1.DB.types.BenefitsRequest;
import com.revature.project1.DB.types.Employee;
import com.revature.project1.DB.types.EmployeeRole;
import com.revature.project1.DB.types.EventType;
import com.revature.project1.DB.types.GradingFormat;
import com.revature.project1.DB.types.RequestStatus;
import com.revature.project1.Util.TRMSSQLException;

public class SQL_DB implements DBInterface {
	
	Connection conn;
	
	public SQL_DB( ) {
		this.conn=null;
	}
	
	public void connect() throws TRMSSQLException {
		String url;
		String password;
		String username;
		if (this.conn == null) {
			try {
				//Class.forName("oracle.jdbc.driver.OracleDriver");
				Properties props = new Properties();
				FileInputStream input = new FileInputStream(SQL_DB.class.getClassLoader().getResource("connection.properties").getFile());
				props.load(input);
				url=props.getProperty("url");
				password=props.getProperty("password");
				username=props.getProperty("username");
			} catch (Exception e) {
				throw new TRMSSQLException("Could not load credentials");
			}
			try {
				this.conn=DriverManager.getConnection(url,username,password);
			} catch (SQLException e) {
				throw new TRMSSQLException("Could not connect to db",e);
			}
		}
	}
	
	public void disconnect() throws TRMSSQLException {
		try {
			if (this.conn != null) {
				this.conn.close();
			}
		} catch (SQLException e) {
			throw new TRMSSQLException("Error disconnecting from db",e);
		}
	}

	private int getNewId() throws TRMSSQLException {
		try {
			PreparedStatement idStmt = conn.prepareStatement("select id_sequence.nextval from dual");
			ResultSet idrs = idStmt.executeQuery();
			if (!idrs.next()) {
				throw new TRMSSQLException("Could not get new id number");
			}
			return idrs.getInt(1);
		} catch (SQLException e) {
			throw new TRMSSQLException("Could not fetch new id number",e);
		}
	}
	
	@Override
	public int createRequest(BenefitsRequest request) throws TRMSSQLException {
		try {
			int id = getNewId();
			
			PreparedStatement stmt = conn.prepareStatement(
					"insert into requests (id,employee,time,location,description,amount,gradingformat,mingrade,typeofevent,justification,worktimemissed,status) values (?,?,?,?,?,?,?,?,?,?,?,?)");
			stmt.setInt(1,id);
			stmt.setInt(2, request.getEmployeeID());
			stmt.setTimestamp(3, request.getTimestamp());
			stmt.setString(4, request.getLocation());
			stmt.setString(5, request.getDescription());
			stmt.setDouble(6,request.getAmount());
			stmt.setString(7,request.getGradingFormat().toString());
			stmt.setString(8,request.getMinGrade());
			stmt.setString(9,request.getEventType().toString());
			stmt.setString(10,request.getJustification());
			stmt.setString(11, request.getWorkTimeMissed());
			stmt.setString(12, request.getStatus().toString());
			System.out.println(request);
			stmt.execute();
			
			return id;
		} catch (SQLException e) {
			throw new TRMSSQLException("Could not create benefits request",e);
		}
	}

	@Override
	public BenefitsRequest lookupRequest(int requestID) throws TRMSSQLException {
		try {
			PreparedStatement stmt = conn.prepareStatement("select employee,time,location,description,amount,gradingformat,mingrade,typeofevent,justification,worktimemissed,status from requests where id=?");
			stmt.setInt(1, requestID);
			ResultSet rs = stmt.executeQuery();
			if (!rs.next()) {
				return null;
			}
			BenefitsRequest retval=new BenefitsRequest();
			retval.setRequestID(requestID);
			retval.setEmployeeID(rs.getInt(1));
			retval.setTimestamp(rs.getTimestamp(2));
			retval.setLocation(rs.getString(3));
			retval.setDescription(rs.getString(4));
			retval.setAmount(rs.getDouble(5));
			retval.setGradingFormat(GradingFormat.valueOf(rs.getString(6)));
			retval.setMinGrade(rs.getString(7));
			retval.setEventType(EventType.valueOf(rs.getString(8)));
			retval.setJustification(rs.getString(9));
			retval.setWorkTimeMissed(rs.getString(10));
			retval.setStatus(RequestStatus.valueOf(rs.getString(11)));
			return retval;
		} catch (SQLException e) {
			throw new TRMSSQLException("Could not lookup request",e);
		}
		
	}

	@Override
	public int createEmployee(Employee employee) throws TRMSSQLException {
		try {
			int id = getNewId();
			PreparedStatement stmt = conn.prepareStatement("insert into employees (id,name,role) values (?,?,?)");
			stmt.setInt(1, id);
			stmt.setString(2, employee.getName());
			stmt.setString(3,employee.getRole().toString());
			stmt.execute();
			return id;
		} catch (SQLException e) {
			throw new TRMSSQLException("Could not create employee",e);
		}
	}

	@Override
	public Employee lookupEmployee(int employeeID) throws TRMSSQLException {
		try {
			Employee retval = new Employee();
			PreparedStatement stmt = conn.prepareStatement("select name,role from employees where id=?");
			stmt.setInt(1, employeeID);
			ResultSet rs = stmt.executeQuery();
			if (!rs.next()) {
				return null;
			}
			retval.setEmployeeID(employeeID);
			retval.setName(rs.getString(1));
			retval.setRole(EmployeeRole.valueOf(rs.getString(2)));
			return retval;
		} catch (SQLException e) {
			throw new TRMSSQLException("Could not lookup employee",e);
		}
	}

	@Override
	public void setManager(int employeeID, int managerID) throws TRMSSQLException {
		try {
			PreparedStatement stmt = conn.prepareStatement("insert into management (employee,manager) values (?,?)");
			stmt.setInt(1, employeeID);
			stmt.setInt(2, managerID);
			stmt.execute();
		} catch (SQLException e) {
			throw new TRMSSQLException("Could not set manager",e);
		}
		
	}

	@Override
	public int lookupManager(int employeeID) throws TRMSSQLException {
		try {
			PreparedStatement stmt = conn.prepareStatement("select manager from management where employee=?");
			stmt.setInt(1, employeeID);
			ResultSet rs = stmt.executeQuery();
			if (!rs.next()) {
				return -1;
			}
			return rs.getInt(1);
		} catch (SQLException e) {
			throw new TRMSSQLException("Could not lookup manager",e);
		}
	}

	@Override
	public int lookupDepartmentHead(int employeeID) throws TRMSSQLException {
		int employeePtr=employeeID;
		while (employeePtr >= 0) {
			Employee employee = this.lookupEmployee(employeePtr);
			if (	employee.getRole() == EmployeeRole.BENCO_AND_DEPT_HEAD ||
					employee.getRole() == EmployeeRole.DEPT_HEAD) {
				return employeePtr;
			}
			employeePtr = lookupManager(employeePtr);
		}
		return -1;
	}

	@Override
	public int addApproval(Approval approval) throws TRMSSQLException {
		try {
			int id = getNewId();
			PreparedStatement stmt = conn.prepareStatement("insert into approvals (id,requestid,approvaldate,approvaltype) values (?,?,?,?)");
			stmt.setInt(1, id);
			stmt.setInt(2, approval.getRequestID());
			stmt.setTimestamp(3, approval.getApprovalDate());
			stmt.setString(4, approval.getApprovalType().toString());
			stmt.execute();
			if (approval.isPreApproval()) {
				addPreapproval(id, approval.getFileReference());
			}
			return id;
		} catch (SQLException e) {
			throw new TRMSSQLException("Could not insert approval",e);
		}
	}

	@Override
	public List<Approval> getApprovals(int requestID) throws TRMSSQLException {
		try {
			List<Approval> retval = new ArrayList<Approval>();
			PreparedStatement stmt = conn.prepareStatement("select id,approvaldate,approvaltype from approvals where requestid=?");
			stmt.setInt(1, requestID);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Approval approval = new Approval();
				approval.setRequestID(requestID);
				int approvalID = rs.getInt(1);
				approval.setApprovalID(approvalID);
				approval.setApprovalDate(rs.getTimestamp(2));
				approval.setApprovalType(ApprovalType.valueOf(rs.getString(3)));
				
				PreparedStatement stmt2 = conn.prepareStatement("select fileID from preapprovals where approvalid=?");
				stmt2.setInt(1, approvalID);
				ResultSet rs2 = stmt2.executeQuery();
				if (rs2.next()) {
					approval.setPreApproval(true);
					approval.setFileReference(rs2.getInt(1));
				}
				retval.add(approval);
			}
			return retval;
		} catch (SQLException e) {
			throw new TRMSSQLException("Could not get approval list",e);
		}
	}

	
	
	@Override
	public int uploadAttachment(Attachment attachment, InputStream is) throws TRMSSQLException {
		try {
			int id = getNewId();
			PreparedStatement stmt = conn.prepareStatement("insert into attachments (fileid,requestid,filetype,filedata) values (?,?,?,?)");
			stmt.setInt(1, id);
			stmt.setInt(2, attachment.getRequestID());
			stmt.setString(3,attachment.getFiletype());
			
			Blob blob = conn.createBlob();
			OutputStream os=blob.setBinaryStream(0);
			is.transferTo(os);
			os.flush();
			stmt.setBlob(4,blob);
			stmt.execute();
			return id;
		} catch (SQLException e) {
			throw new TRMSSQLException("Could not upload attachment",e);
		} catch (IOException e) {
			throw new TRMSSQLException("Error creating blob");
		}
	}

	@Override
	public Attachment fetchAttachmentMetadata(int fileID) throws TRMSSQLException {
		try {
			Attachment retval = new Attachment();
			PreparedStatement stmt = conn.prepareStatement("select requestid,filetype from attachments where fileid=?");
			stmt.setInt(1, fileID);
			ResultSet rs = stmt.executeQuery();
			if (!rs.next()) {
				return null;
			}
			retval.setFileID(fileID);
			retval.setRequestID(rs.getInt(1));
			retval.setFiletype(rs.getString(2));
			return retval;
		} catch (SQLException e) {
			throw new TRMSSQLException("Could not fetch metadata",e);
		}
	}

	@Override
	public Attachment fetchAttachment(int fileID) throws TRMSSQLException {
		try {
			Attachment retval = new Attachment();
			PreparedStatement stmt = conn.prepareStatement("select requestid,filetype,fileData from attachments where fileid=?");
			stmt.setInt(1, fileID);
			ResultSet rs = stmt.executeQuery();
			if (!rs.next()) {
				return null;
			}
			retval.setFileID(fileID);
			retval.setRequestID(rs.getInt(1));
			retval.setFiletype(rs.getString(2));
			Blob blob = rs.getBlob(3);
			retval.setFiledata(blob.getBytes(1,(int) blob.length()));
			return retval;
		} catch (SQLException e) {
			throw new TRMSSQLException("Could not fetch file",e);
		}
	}

	private void addPreapproval(int approvalID, int fileid) throws TRMSSQLException {
		try {
			PreparedStatement stmt = conn.prepareStatement(
					"insert into preapprovals (approvalid,fileid) values (?,?)");
			stmt.setInt(1, approvalID);
			stmt.setInt(2, fileid);
			stmt.execute();
		} catch (SQLException e) {
			throw new TRMSSQLException("Could not insert preapproval",e);
		}
	}

	@Override
	public void setGrade(int requestID, String grade) throws TRMSSQLException {
		try {
			PreparedStatement stmt = conn.prepareStatement("insert into grades (requestid,grade) values (?,?)");
			stmt.setInt(1, requestID);
			stmt.setString(2, grade);
			stmt.execute();
		} catch (SQLException e) {
			throw new TRMSSQLException("Could not set grade",e);
		}
		
	}

	@Override
	public String lookupGrade(int requestID) throws TRMSSQLException {
		try {
			PreparedStatement stmt = conn.prepareStatement("select grade from grades where requestid=?");
			stmt.setInt(1, requestID);
			ResultSet rs = stmt.executeQuery();
			if (!rs.next()) {
				return null;
			}
			return rs.getString(1);
		} catch (SQLException e) {
			throw new TRMSSQLException("Could not lookup grade",e);
		}
	}

	@Override
	public void addPresentation(int requestID, int fileID) throws TRMSSQLException {
		try {
			PreparedStatement stmt = conn.prepareStatement("insert into presentations (requestid,fileid) values (?,?)");
			stmt.setInt(1, requestID);
			stmt.setInt(2, fileID);
			stmt.execute();
		} catch (SQLException e) {
			throw new TRMSSQLException("Could not add presentation",e);
		}
	}

	@Override
	public List<Integer> getPresentations(int requestID) throws TRMSSQLException {
		try {
			List<Integer> retval = new ArrayList<Integer>();
			PreparedStatement stmt = conn.prepareStatement("select fileID from presentations where requestid=?");
			stmt.setInt(1, requestID);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				retval.add(rs.getInt(1));
			}
			return retval;
		} catch (SQLException e) {
			throw new TRMSSQLException("Could not get presentations",e);
		}
	}
	

}
