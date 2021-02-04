import com.revature.project1.DB.DAO.SQL_DB;
import com.revature.project1.DB.types.Employee;

public class Passwords {
	public static void main(String[] args) {
		System.out.println(Employee.hash("bar"));
		
		SQL_DB db = new SQL_DB();
		try {
			db.connect();
			Employee employee = db.lookupEmployeeByName("alice");
			System.out.println(employee);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
