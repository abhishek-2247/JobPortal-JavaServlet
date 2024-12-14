
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author abhis
 */
@WebServlet(urlPatterns = {"/Main"})
public class Main extends HttpServlet 
{
    
    private static final String DB_URL = "jdbc:mysql://localhost:3306/jobportal";
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException 
    {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<html><head><title>Job Posts</title></head><body>");
        out.println("<h1>Available Job Posts</h1>");
        out.println("<table border='1'>");
        out.println("<tr><th>Title</th><th>Description</th><th>Company</th><th>Location</th></tr>");

        try (Connection conn = DriverManager.getConnection(DB_URL, "root", "");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM job_posts")) {

            while (rs.next()) 
            {
                String title = rs.getString("title");
                String description = rs.getString("description");
                String company = rs.getString("company");
                String location = rs.getString("location");

                out.println("<tr>");
                out.println("<td>" + title + "</td>");
                out.println("<td>" + description + "</td>");
                out.println("<td>" + company + "</td>");
                out.println("<td>" + location + "</td>");
                out.println("</tr>");
            }

        } catch (SQLException e) {
            out.println("<tr><td colspan='4'>Error fetching job posts: " + e.getMessage() + "</td></tr>");
        }

        out.println("</table>");
        out.println("</body></html>");
    }

    
}
