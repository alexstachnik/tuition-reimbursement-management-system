
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

public class Setup {
		
	public static void main(String[] args) {
		SQL_DB db = new SQL_DB();
		Employee employee1 = new Employee();
		employee1.setName("alice");
		employee1.setRole(EmployeeRole.BENCO);
		employee1.setPassword("bar");
		employee1.setSuperuser(true);
		
		Employee employee2 = new Employee();
		employee2.setName("bob");
		employee2.setPassword("bar");
		employee2.setRole(EmployeeRole.NORMAL);
		
		Employee employee4 = new Employee();
		employee4.setName("charlie");
		employee4.setPassword("bar");
		employee4.setRole(EmployeeRole.NORMAL);
		
		Employee employee5 = new Employee();
		employee5.setName("dan");
		employee5.setPassword("bar");
		employee5.setRole(EmployeeRole.DEPT_HEAD);
		
		
		try {
			db.connect();
			int employee1ID = db.createEmployee(employee1);
			int employee2ID = db.createEmployee(employee2);
			int employee4ID = db.createEmployee(employee4);
			int employee5ID = db.createEmployee(employee5);

			{
				BenefitsRequest request = new BenefitsRequest();
				request.setEmployeeID(employee2ID);
				Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
				request.setTimestamp(timestamp);
				Timestamp eventTime = Timestamp.valueOf(LocalDateTime.of(2020, 11, 12, 10, 0));
				request.setEventTime(eventTime);
				request.setLocation("Springfield");
				request.setDescription("Party time");
				request.setAmount(2.5);
				request.setGradingFormat(GradingFormat.GRADED);
				request.setMinGrade("C-");
				request.setEventType(EventType.UNIVERSITY_COURSE);
				request.setJustification("Because it's cool");
				request.setWorkTimeMissed("A day");
				request.setStatus(RequestStatus.OPEN);
				db.createRequest(request);
			}
			
			{
				BenefitsRequest request = new BenefitsRequest();
				request.setEmployeeID(employee2ID);
				Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
				request.setTimestamp(timestamp);
				Timestamp eventTime = Timestamp.valueOf(LocalDateTime.of(2020, 4, 12, 10, 0));
				request.setEventTime(eventTime);
				request.setLocation("Philadelphia");
				request.setDescription("Somber study");
				request.setAmount(500);
				request.setGradingFormat(GradingFormat.GRADED);
				request.setMinGrade("C");
				request.setEventType(EventType.CERTIFICATION);
				request.setJustification("Reasons");
				request.setWorkTimeMissed("A day");
				request.setStatus(RequestStatus.CLOSED);
				db.createRequest(request);
			}
			
			{
				BenefitsRequest request = new BenefitsRequest();
				request.setEmployeeID(employee2ID);
				Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
				request.setTimestamp(timestamp);
				Timestamp eventTime = Timestamp.valueOf(LocalDateTime.of(2019, 11, 12, 10, 0));
				request.setEventTime(eventTime);
				request.setLocation("Moscow");
				request.setDescription("Cold");
				request.setAmount(450);
				request.setGradingFormat(GradingFormat.PRESENTATION);
				request.setMinGrade("");
				request.setEventType(EventType.TECHNICAL_TRAINING);
				request.setJustification("Because it's really cool");
				request.setWorkTimeMissed("A week");
				request.setStatus(RequestStatus.OPEN);
				db.createRequest(request);
			}
			
			{
				BenefitsRequest request = new BenefitsRequest();
				request.setEmployeeID(employee2ID);
				Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
				request.setTimestamp(timestamp);
				Timestamp eventTime = Timestamp.valueOf(LocalDateTime.of(2020, 7, 12, 10, 0));
				request.setEventTime(eventTime);
				request.setLocation("Los Angeles");
				request.setDescription("American Standards and Measurement");
				request.setAmount(200);
				request.setGradingFormat(GradingFormat.GRADED);
				request.setMinGrade("C-");
				request.setEventType(EventType.SEMINAR);
				request.setJustification("Because it's ASTM");
				request.setWorkTimeMissed("3 Days");
				request.setStatus(RequestStatus.OPEN);
				db.createRequest(request);
			}
			
			
			db.setManager(employee1ID, employee5ID);
			db.setManager(employee2ID, employee4ID);
			db.setManager(employee4ID, employee5ID);
			
			db.disconnect();
		} catch (TRMSSQLException e) {
			e.printStackTrace();
		}
		System.out.println("test");
	}

}