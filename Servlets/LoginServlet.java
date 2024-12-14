import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.Class.forName;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet(urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet 
{
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        try
        {
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        String username = request.getParameter("uname");
        String password = request.getParameter("password");
        
           Class.forName("com.mysql.jdbc.Driver");
           Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/jobportal","root","");
           PreparedStatement stm = con.prepareStatement("select * from jobseekers where Username=? and Password=?");
           stm.setString(1, username);
           stm.setString(2, password);
           
           ResultSet rs = stm.executeQuery();
           if(rs.next())
           {
               response.sendRedirect("jobposts");
           }
          else
           {
               out.println("<center>Login Failed</center>");
               out.println("<center><br>Go Back and Try Again.......</center>");
           }

           con.close();
        }
        catch(IOException | ClassNotFoundException | SQLException e)
        {
            System.out.println(e.getMessage());
        }
           
       
    }

}
