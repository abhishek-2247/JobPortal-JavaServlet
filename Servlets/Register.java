import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class Register extends HttpServlet 
{    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException 
    {
            
        response.setContentType("text/html");
        String name = request.getParameter("name");
        String username = request.getParameter("username");
        String gmail = request.getParameter("gmail");
        String password = request.getParameter("password");
        PrintWriter pw = response.getWriter();
        
        String url = "jdbc:mysql://localhost:3306/jobportal";
        
        try
        {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection(url, "root", "");
        
        pw.println(gmail);
        PreparedStatement pst = con.prepareStatement("select * from jobseekers where Email=?");
        pst.setString(1, gmail);
        ResultSet rs = pst.executeQuery();
            
            if(rs.next())
            {
               response.sendRedirect("RegToLogin.html");
            }
        
        else
            {
         pst = con.prepareStatement("Insert into jobseekers(Name,Username,Email,Password) values(?,?,?,?)");
        pst.setString(1, name);
        pst.setString(2, username);
        pst.setString(3, gmail);
        pst.setString(4, password);
        
        int n = pst.executeUpdate();
        if(n>0)
        {
            response.sendRedirect("jobposts?username="+username);
        }
        else
        {
            pw.println("data not inserted");
        }
        
        }
        }
        catch(IOException | ClassNotFoundException | SQLException e)
        {
            System.out.println(e.getMessage());
        }
        
    }

    
}
