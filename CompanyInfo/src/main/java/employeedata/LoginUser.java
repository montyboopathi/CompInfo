package employeedata;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Logger;

import org.json.JSONObject;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


public class LoginUser extends HttpServlet {
	
	static Logger log = Logger.getLogger(LoginUser.class.getName());

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String employeeName = request.getParameter("employeeName");
		String employeePassword = request.getParameter("employeePassword");
		String departmentId = request.getParameter("departmentID");
		int departmentID=Integer.parseInt(departmentId);
		Connection con = null;
		Statement stat = null;
		ResultSet res = null;
		LoginValidation d = new LoginValidation();
		try {

			PrintWriter prt = response.getWriter();
			response.setContentType("application/json");
			
			if (d.validate(employeeName, employeePassword, departmentID)) {
				HttpSession ses = request.getSession();
				ses.setAttribute("departmentID", departmentID);
				
			
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/employee", "root", "123456789");
			stat = con.createStatement();
			
			if (departmentID==11) {
				// login person is user means this block of code will execute
				res = stat.executeQuery("select * from employee_table");

				JSONObject jObj = new JSONObject();

				while (res.next()) {
					JSONObject employee = new JSONObject();

					employee.put("employeeID", res.getInt("employeeID"));
					employee.put("employeeName", res.getString("employeeName"));
					employee.put("employeePassword", res.getString("employeePassword"));
					employee.put("departmentID", res.getInt("departmentID"));
					
					prt.print(employee);
					
				}
				res = stat.executeQuery("select * from department");
				JSONObject jObj1 = new JSONObject();
				while (res.next()) {
					JSONObject department = new JSONObject();

					department.put("departmentID", res.getInt("departmentID"));
					department.put("departmentName", res.getString("departmentName"));
					
				}

			} else {
				PreparedStatement st = con.prepareStatement("SELECT * FROM employee_table where employeeName=?");
				st.setString(1, employeeName);
				res = st.executeQuery();
				while (res.next()) {
					JSONObject employee = new JSONObject();

					employee.put("employeeID", res.getInt("employeeID"));
					employee.put("employeeName", res.getString("employeeName"));
					employee.put("employeePassword", res.getString("employeePassword"));
					employee.put("departmentID", res.getInt("departmentID"));
				
					prt.print(employee);
					
				}
				res = stat.executeQuery("select * from department");
				JSONObject jObj1 = new JSONObject();
				while (res.next()) {
					JSONObject department = new JSONObject();

					department.put("departmentID", res.getInt("departmentID"));
					department.put("departmentName", res.getString("departmentName"));
					
					
					
					
				}
				RequestDispatcher rd = request.getRequestDispatcher("UpdateService");
	               rd.include(request, response);
				}
			
				con.close();
				
			

		}else {
			prt.println("please type the correct username and password");
		}
		}
		catch (

		Exception e) {
			e.printStackTrace();
		}

	}

}

