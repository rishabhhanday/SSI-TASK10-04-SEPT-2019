import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.Scanner;

public class EmployeeJDBC {
	int eno;
	String ename;
	int salary;
	String desg;
	String dept;

	static Scanner sc = new Scanner(System.in);

	public void addEmp(Connection con) throws Exception {

		System.out.println("Enter  emp no");
		eno = sc.nextInt();
		System.out.println("Enter  emp name");
		ename = sc.next();
		System.out.println("Enter  emp salary");
		salary = sc.nextInt();
		System.out.println("Enter  emp designation");
		desg = sc.next();
		System.out.println("Enter  emp Dept");
		dept = sc.next();
		// String sql = "INSERT INTO EMP VALUES(?,?,?,?,?)";
		CallableStatement cs = con.prepareCall("{call insert_emp(?,?,?,?,?)}");

		cs.setInt(1, eno);
		cs.setString(2, ename);
		cs.setInt(3, salary);
		cs.setString(4, desg);
		cs.setString(5, dept);
		cs.execute();
		System.out.println("Employee added to database");

	}

	public static void searchDeptWise(String dept, Connection con) throws Exception {
		String sql = "select * from EMP where EMPDEPT =?";
		PreparedStatement st = con.prepareStatement(sql);
		st.setString(1, dept);
		ResultSet rs = st.executeQuery();
		while (rs.next()) {
			System.out.println("Emp No : " + rs.getString(1));
			System.out.println("Emp name : " + rs.getString(2));
			System.out.println("Emp salary : " + rs.getString(3));
			System.out.println("Emp designation : " + rs.getString(4));
			System.out.println("Emp deptartment : " + rs.getString(5));

		}
	}

	public static void searchEmp(int eno, Connection con) throws Exception {
		String sql = "select * from EMP where EMPNO =?";
		PreparedStatement st = con.prepareStatement(sql);
		st.setInt(1, eno);
		ResultSet rs = st.executeQuery();
		if (rs.next()) {
			System.out.println("Emp No : " + rs.getString(1));
			System.out.println("Emp name : " + rs.getString(2));
			System.out.println("Emp salary : " + rs.getString(3));
			System.out.println("Emp designation : " + rs.getString(4));
			System.out.println("Emp deptartment : " + rs.getString(5));

		}
	}

	public static void viewAllEmp(Connection con) throws Exception {
		String sql = "select * from EMP";
		PreparedStatement st = con.prepareStatement(sql);
		ResultSet rs = st.executeQuery();
		while (rs.next()) {
			System.out.println("Emp No : " + rs.getString(1));
			System.out.println("Emp name : " + rs.getString(2));
			System.out.println("Emp salary : " + rs.getString(3));
			System.out.println("Emp designation : " + rs.getString(4));
			System.out.println("Emp deptartment : " + rs.getString(5));

		}
	}

	public static void updateEmpSalary(int eno, Connection con) throws Exception {
		CallableStatement cs = con.prepareCall("{? = call sal_emp(?)}");
		cs.setInt(2, eno);
		cs.registerOutParameter(1, Types.INTEGER);
		cs.execute();
		System.out.println("Old salary :" + cs.getInt(1));
		System.out.println("Enter new salary");
		int newSal = sc.nextInt();
		String sql = "UPDATE EMP SET EMPSALARY = ? WHERE EMPNO = ?";
		PreparedStatement st = con.prepareStatement(sql);
		st.setInt(1, newSal);
		st.setInt(2, eno);
		st.executeUpdate();
		System.out.println("Salary updated successfully");

	}

	public static void removeEmp(int eno, Connection con) throws Exception {
		String sql = "DELETE FROM EMP WHERE EMPNO = ?";
		PreparedStatement st = con.prepareStatement(sql);
		st.setInt(1, eno);
		st.executeUpdate();
		System.out.println("Employee deleted succesfully");
	}

	public static void deleteAll(Connection con) throws Exception {
		String sql = "delete from emp";
		PreparedStatement st = con.prepareStatement(sql);
		st.executeUpdate();
		System.out.println("ALL employee deleted");

	}

	public static void main(String... sd) throws Exception {
		Class.forName("oracle.jdbc.driver.OracleDriver");
		System.out.println("Driver Loaded");
		// step-2 (Connection Establishment)
		Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "rishabh", "abcd1234");
		System.out.println("Connected Successfully");
		EmployeeJDBC e = new EmployeeJDBC();
		outer: while (true) {

			System.out.println(
					"1. Add Emp \n 2.View All Emp \n 3.Remove Emp \n 4. Clear Data \n 5. Change Sal \n 6.Search Emp \n 7. View dept Wise \n 8. EXIT");
			int option = sc.nextInt();
			switch (option) {
			case 1:

				e.addEmp(con);
				break;
			case 2:
				viewAllEmp(con);
				break;
			case 3:
				System.out.println("Enter emp no to delete");
				int empno = sc.nextInt();
				removeEmp(empno, con);
				break;
			case 4:
				deleteAll(con);
				break;
			case 5:
				System.out.println("Enter emp no to update salary");
				int eno = sc.nextInt();
				updateEmpSalary(eno, con);
				break;
			case 6:
				System.out.println("Enter emp no to search");
				int employeeNo = sc.nextInt();
				searchEmp(employeeNo, con);

				break;
			case 7:
				System.out.println("Enter dept to search");
				String dept = sc.next();
				searchDeptWise(dept, con);
				break;

			case 8:
				
				break outer;

			}

		}

	}

}
