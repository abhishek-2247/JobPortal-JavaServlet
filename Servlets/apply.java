

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.UnknownHostException;

@WebServlet(urlPatterns = {"/apply"})
public class apply extends HttpServlet
{
    
   private static Socket socket;
    private static PrintWriter socketWriter;
    private static BufferedReader socketReader;

    // Initialize the connection to the client
    @Override
    public void init() throws ServletException {
        super.init();
        try {
            // Connecting to the client on localhost:12345
            socket = new Socket("localhost", 12345);
            socketWriter = new PrintWriter(socket.getOutputStream(), true);
            socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Connected to client via socket.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Handle incoming GET request
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Send a message to the client via socket
        String message = "Message from server at " + System.currentTimeMillis();

        if (socket != null && socketWriter != null) {
            socketWriter.println(message);  // Send the message to the client
            System.out.println("Sent message to client: " + message);
        }

        // Response to HTTP request
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h3>Server sent message to client: " + message + "</h3>");
        out.println("<p>Check client console for the message received.</p>");
        out.println("</body></html>");

        // Start a background thread to listen to the client
        new Thread(new ClientMessageListener(socketReader)).start();
    }

    // Method to listen to the client
    private static class ClientMessageListener implements Runnable {
        private BufferedReader reader;

        public ClientMessageListener(BufferedReader reader) {
            this.reader = reader;
        }

        @Override
        public void run() {
            try {
                String message;
                while ((message = reader.readLine()) != null) {
                    System.out.println("Received from client: " + message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Clean up on destroy
    @Override
    public void destroy() {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.destroy();
    }
}
