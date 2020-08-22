package main.project3;

import main.project2.User;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "staffLoginServlet", urlPatterns = "/api/staffLogin")
public class StaffLogin extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter webpage = response.getWriter();
        JSONObject status = new JSONObject();

        HttpSession session = request.getSession(true);

        String email = String.valueOf(request.getParameter("email"));
        String password = String.valueOf(request.getParameter("password"));

        System.out.println(email);
        System.out.println(password);

        Staff staff = Staff.login(email, password);

        if (staff != null) {
            status.put("Status", "success");
            session.setAttribute("Staff", staff);
        } else {
            status.put("Status", "fail");
        }

        webpage.write(String.valueOf(status));
        webpage.close();
    }
}
