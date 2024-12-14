import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.annotation.*;


@WebServlet("/jobposts")
public class jobposts extends HttpServlet {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/jobportal";
    

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try 
        {
            
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            String username = request.getParameter("username");
            
            List<Job> jobs = new ArrayList<>();
            
            
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection connection = DriverManager.getConnection(JDBC_URL, "root", "")) {
                String query = "SELECT * FROM jobs";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                
                while (resultSet.next()) 
                {
                    int id = resultSet.getInt("id");
                    String title = resultSet.getString("title");
                    String company = resultSet.getString("company");
                    String location = resultSet.getString("location");
                    String description = resultSet.getString("description");
                    
                    jobs.add(new Job(id, title, company, location, description));
                }
                
            } catch (SQLException e) 
            {
            }
            
           
            out.println("<!DOCTYPE html>");
            out.println("<html lang='en'>");
            out.println("<head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
            out.println("<title>Job Portal</title>");
            out.println("<style>");
            out.println("/* Basic Reset */");
            out.println("* { margin: 0; padding: 0; box-sizing: border-box; }");
            out.println("body { font-family: Arial, sans-serif; background-color: #f4f7f6; color: #333; line-height: 1.6; }");
            out.println("header { background-color: #333; color: white; text-align: center; padding: 20px; }");
            out.println("header h1 { font-size: 36px; }");
            out.println(".container { width: 100%; max-width: 1200px; margin: 50px auto; }");
            out.println(".job-listings { display: grid; grid-template-columns: repeat(auto-fill, minmax(300px, 1fr)); gap: 60px; }");
            out.println(".job-post { background-color: powderblue; border: 3px solid #07a9d8; border-radius: 8px; padding: 20px; transition: transform 0.3s ease, box-shadow 0.3s ease; }");
            out.println(".job-post:hover { transform: translateY(-10px); box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1); }");
            out.println(".job-post h3 { font-size: 22px; color: #333; margin-bottom: 10px; }");
            out.println(".job-post p { font-size: 16px; color: #555; margin-bottom: 15px; }");
            out.println(".job-post .company-name { font-weight: bold; color: #333; }");
            out.println(".job-post .location { font-style: italic; color: #d80764; }");
            out.println(".job-post .apply-btn { display: inline-block; padding: 10px 20px; background-color: #007bff; color: white; border-radius: 4px; text-decoration: none; font-weight: bold; transition: background-color 0.3s; }");
            out.println(".job-post .apply-btn:hover { background-color: #0056b3; }");
            out.println("footer { background-color: #333; color: white; text-align: center; padding: 10px; margin-top: 50px; }");
            out.println("</style>");
            out.println("</head>");
            out.println("<body>");
            
            
            out.println("<header>");
            out.println("<h1>Job Portal</h1>");
            out.println("<p>Your gateway to the best job opportunities</p>");
            out.println("<br><a href='jobsearch.html'><label style='color:red'>SEARCH HERE</label></a>");
            out.println("</header>");
            
            
            out.println("<div class='container'>");
            out.println("<div class='job-listings'>");
            
            
            for (Job job : jobs) {
                out.println("<div class='job-post'>");
                out.println("<center><h3>" + job.getTitle().toUpperCase() + "</h3></center>");
                out.println("<center><p class='company-name'>" + job.getCompany() + "</p></center>");
                out.println("<p class='location'>Location:- " + job.getLocation() + "</p>");
                out.println("<p>" + job.getDescription() + "</p>");
                out.println("<a href='JobSearchServlet?search=" + job.getTitle() + "' class='apply-btn'>Apply Now</a>");
                out.println("</div>");
            }
            
            out.println("</div>");
            out.println("</div>");
            
           
            out.println("<footer>");
            out.println("<p>@MIK Job Portal</p>");
            out.println("</footer>");
            
            
            out.println("</body>");
            out.println("</html>");
        } catch (ClassNotFoundException ex) {
            System.out.println(ex);
        }
    }

    
    public static class Job 
    {
        private final int id;
        private final String title;
        private final String company;
        private final String location;
        private final String description;

        public Job(int id, String title, String company, String location, String description) {
            this.id = id;
            this.title = title;
            this.company = company;
            this.location = location;
            this.description = description;
        }

        public int getId() 
        {
            return id;
        }

        public String getTitle() 
        {
            return title;
        }

        public String getCompany() 
        {
            return company;
        }

        public String getLocation() 
        {
            return location;
        }

        public String getDescription() 
        {
            return description;
        }
    }
}
