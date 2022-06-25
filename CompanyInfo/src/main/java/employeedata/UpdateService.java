package employeedata;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.json.JSONObject;

import com.mysql.cj.xdevapi.Statement;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class UpdateService extends HttpServlet{
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
response.setContentType("application/json");
        
        String  employeeName= request.getParameter("employeeName");
        String  employeePassword= request.getParameter("employeePassword");
        String  employeeId= request.getParameter("employeeID");
        int employeeID=Integer.parseInt(employeeId);

        PrintWriter prt = response.getWriter();
        
        HttpSession ses = request.getSession(false);
        if(ses !=null) {
        	
        	String departmentID= (String) ses.getAttribute("departmentID");
		
		Connection c=null;
		PreparedStatement s =null;
		ResultSet r=null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			c=DriverManager.getConnection("jdbc:mysql://localhost:3306/employee", "root", "123456789");
			
			String update = "UPDATE employee_table SET employeeName =?, employeePassword=? WHERE employeeID =?";
			s=c.prepareStatement(update);
			s.setString(1,employeeName);
			s.setString(2, employeePassword);
			s.executeUpdate();
			
			prt.println("Details are updated sucessfully");
            s= c.prepareStatement("SELECT * FROM employee.employee_table  where employeeName=?");   
            s.setString(1,employeeName);
             r=s.executeQuery();
		
	
			
		
             while (r.next()) {
					JSONObject employee = new JSONObject();

					employee.put("employeeID", r.getInt("employeeID"));
					employee.put("employeeName", r.getString("employeeName"));
					employee.put("employeePassword", r.getString("employeePassword"));
					employee.put("departmentID", r.getInt("departmentID"));
					
					prt.print(employee);
					
			
		}
		} catch(Exception e) {
            
            e.printStackTrace();
        }
	}
}
}





