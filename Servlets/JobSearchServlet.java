import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.annotation.WebServlet;

@WebServlet("/JobSearchServlet")
public class JobSearchServlet extends HttpServlet {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/jobportal";
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html");

        String searchQuery = request.getParameter("search");

        PrintWriter out = response.getWriter();
        out.println("<html><head><title>Job Search</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f4f4f9; }");
        out.println("h1 { text-align: center; color: #4CAF50; padding: 20px; }");
        out.println("h2 { color: #333; font-size: 24px; }");
        out.println("p { font-size: 16px; color: #555; }");
        out.println("form { background-color: #fff; padding: 20px; margin: 20px auto; width: 50%; border-radius: 8px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }");
        out.println("label { font-size: 14px; color: #333; }");
        out.println("input[type='text'], textarea { width: 100%; padding: 10px; margin: 8px 0 20px 0; border-radius: 4px; border: 1px solid #ddd; }");
        out.println("textarea { height: 100px; }");
        out.println("input[type='submit'] { background-color: #4CAF50; color: white; padding: 10px 15px; border: none; border-radius: 4px; cursor: pointer; font-size: 16px; }");
        out.println("input[type='submit']:hover { background-color: #45a049; }");
        out.println(".job-details { background-color: #fff; padding: 20px; margin: 20px auto; width: 60%; border-radius: 8px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }");
        out.println(".job-details h2 { color: #333; font-size: 20px; }");
        out.println(".job-details p { font-size: 14px; color: #666; }");
        out.println("</style>");
        out.println("</head><body>");
        out.println("<h1>Job Search</h1>");

        if (searchQuery != null && !searchQuery.isEmpty()) {
            
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(JobSearchServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            try (Connection connection = DriverManager.getConnection(DB_URL, "root", "")) {
                String sql = "SELECT * FROM jobs WHERE title LIKE ? OR description LIKE ?";
                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setString(1, "%" + searchQuery + "%");
                    stmt.setString(2, "%" + searchQuery + "%");

                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            do {
                                String title = rs.getString("title");
                                String company = rs.getString("company");
                                String location = rs.getString("location");
                                String description = rs.getString("description");

                                
                                out.println("<div class='job-details'>");
                                out.println("<h2>" + title + "</h2>");
                                out.println("<p><strong>Company:</strong> " + company + "</p>");
                                out.println("<p><strong>Location:</strong> " + location + "</p>");
                                out.println("<p><strong>Description:</strong> " + description + "</p>");

                                
                                out.println("<h3>Apply for this Job</h3>");
                                out.println("<form action='JobSearchServlet' method='POST'>");
                                out.println("<input type='hidden' name='jobTitle' value='" + title + "'>");
                                out.println("<input type='hidden' name='company' value='" + company + "'>");
                                out.println("<label for='qualification'>Qualification: </label><input type='text' name='qualification'><br>");
                                out.println("<label for='skills'>Skills: </label><input type='text' name='skills'><br>");
                                out.println("<label for='fitForJob'>Why are you fit for this job? </label><textarea name='fitForJob'></textarea><br>");
                                out.println("<input type='submit' value='Apply Job'>");
                                out.println("</form>");
                                out.println("</div>");
                            } while (rs.next());
                        } else {
                            out.println("<p>No jobs found for your search query.</p>");
                        }
                    }
                }
            } catch (SQLException e) {
             
                out.println("<p>Error fetching job details: " + e.getMessage() + "</p>");
            }
        } else {
            out.println("<p>Please enter a search query.</p>");
        }

        out.println("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        String qualification = request.getParameter("qualification");
        String skills = request.getParameter("skills");
        String fitForJob = request.getParameter("fitForJob");
        String jobTitle = request.getParameter("jobTitle");
        String company = request.getParameter("company");
        int n=0;
        try
        {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection(DB_URL, "root", "");
        
        PreparedStatement pst = con.prepareStatement("insert into jobapprov values(?,?,?,?,?)");
        pst.setString(1, jobTitle);
        pst.setString(2, company);
        pst.setString(3, qualification);
        pst.setString(4, skills);
        pst.setString(5, fitForJob);

        n = pst.executeUpdate();
        }
        catch(ClassNotFoundException | SQLException e)
        {
            
        }
        if(n>0)
        response.sendRedirect("confirmation.html");
    }
}
