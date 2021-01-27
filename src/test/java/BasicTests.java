import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import com.revature.project1.DB.DAO.SQL_DB;
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

public class BasicTests {
	
	public static void main(String[] args) {
		SQL_DB db = new SQL_DB();
		Employee employee1 = new Employee();
		employee1.setName("alice");
		employee1.setRole(EmployeeRole.BENCO_AND_DEPT_HEAD);
		Employee employee2 = new Employee();
		employee2.setName("bob");
		employee2.setRole(EmployeeRole.NORMAL);
		Employee employee3 = new Employee();

		Employee employee4 = new Employee();
		employee4.setName("charlie");
		employee4.setRole(EmployeeRole.NORMAL);
		Employee employee5 = new Employee();
		employee5.setName("dan");
		employee5.setRole(EmployeeRole.NORMAL);
		
		
		try {
			db.connect();
			int employee1ID = db.createEmployee(employee1);
			int employee2ID = db.createEmployee(employee2);
			int employee4ID = db.createEmployee(employee4);
			int employee5ID = db.createEmployee(employee5);
			employee3 = db.lookupEmployee(employee2ID);
			System.out.println(employee3.getName());

			BenefitsRequest request = new BenefitsRequest();
			request.setEmployeeID(employee2ID);
			Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
			request.setTimestamp(timestamp);
			request.setLocation("Springfield");
			request.setDescription("Party time");
			request.setAmount(2.5);
			request.setGradingFormat(GradingFormat.GRADED);
			request.setMinGrade("C-");
			request.setEventType(EventType.UNIVERSITY_COURSE);
			request.setJustification("Because it's cool");
			request.setWorkTimeMissed("A day");
			request.setStatus(RequestStatus.OPEN);
			int requestID=db.createRequest(request);
			
			BenefitsRequest otherRequest = db.lookupRequest(requestID);
			System.out.println(otherRequest);
			
			db.setManager(employee2ID, employee1ID);
			db.setManager(employee5ID, employee4ID);
			db.setManager(employee4ID, employee2ID);
			System.out.println(db.lookupManager(employee2ID));
			System.out.println(db.lookupManager(employee1ID));
			System.out.println(db.lookupDepartmentHead(employee5ID));
			System.out.println(db.lookupDepartmentHead(employee1ID));
			
			
			Attachment attachment = new Attachment();
			attachment.setFiletype("png");
			attachment.setRequestID(requestID);

			FileInputStream is = new FileInputStream("C:\\Users\\alexs\\Documents\\project1_erd.png");
			int fileID=db.uploadAttachment(attachment,is);
			is.close();
			
			is = new FileInputStream("C:\\Users\\alexs\\Documents\\project1_erd.png");
			byte img[] = is.readAllBytes();
			is.close();
			
			Attachment metadata = db.fetchAttachmentMetadata(fileID);
			System.out.println(metadata);
			
			Attachment fullAttachment = db.fetchAttachment(fileID);
			for (int i=0;i<img.length;++i) {
				if (img[i] != fullAttachment.getFiledata()[i]) {
					System.out.println("Mismatch");
				}
			}
			System.out.printf("0x%02X%02X%02X%02X\n", img[0],img[1],img[2],img[3]);
			System.out.printf("0x%02X%02X%02X%02X\n", fullAttachment.getFiledata()[0],fullAttachment.getFiledata()[1],fullAttachment.getFiledata()[2],fullAttachment.getFiledata()[3]);
			System.out.println(fullAttachment.getFiledata().length);
			System.out.println(img.length);
			
			OutputStream os = new FileOutputStream("C:\\Users\\alexs\\Documents\\foo2.png");
			os.write(fullAttachment.getFiledata());
			os.flush();
			os.close();
			
			Approval approval = new Approval();
			approval.setApprovalDate(Timestamp.valueOf(LocalDateTime.now()));
			approval.setApprovalType(ApprovalType.BENCO);
			approval.setPreApproval(false);
			approval.setRequestID(requestID);
			int approvalID=db.addApproval(approval);
			System.out.println(approvalID);
			
			Approval approval2 = new Approval();
			approval2.setApprovalDate(Timestamp.valueOf(LocalDateTime.now()));
			approval2.setApprovalType(ApprovalType.BENCO);
			approval2.setFileReference(fileID);
			approval2.setPreApproval(true);
			approval2.setRequestID(requestID);
			int approvalID2=db.addApproval(approval2);
			System.out.println(approvalID2);
			
			
			List<Approval> approvals=db.getApprovals(requestID);
			System.out.println(approvals.size());
			for (int i=0;i<approvals.size();++i) {
				System.out.println(approvals.get(i));
			}
			
			db.setGrade(requestID,"A+");
			System.out.println(db.lookupGrade(requestID));
			
			db.addPresentation(requestID, fileID);
			List<Integer> presentations = db.getPresentations(requestID);
			for (int i=0;i<presentations.size();++i) {
				System.out.println(presentations.get(i));
			}
			
			db.disconnect();
		} catch (TRMSSQLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("test");
	}

}
