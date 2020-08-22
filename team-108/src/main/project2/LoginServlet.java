package main.project2;
import main.project3.RecaptchaVerifyUtils;
import org.json.simple.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "LoginServlet", urlPatterns = "/api/login")
public class LoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        PrintWriter webpage = response.getWriter();
        JSONObject status = new JSONObject();

        System.out.println("line 21 " + request.getParameter("mobile"));
        String email = String.valueOf(request.getParameter("email"));
        String password = String.valueOf(request.getParameter("password"));
        System.out.println(email);
        System.out.println(password);

        if (request.getParameter("mobile") == null) {
            // check reCaptcha result
            String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
            System.out.println(gRecaptchaResponse);
            try {
                RecaptchaVerifyUtils.verify(gRecaptchaResponse);
            } catch (Exception e) {
                status.put("Status", "reCaptcha_fail");
                webpage.write(String.valueOf(status));
                return;
            }
        }

        HttpSession session = request.getSession(true);


        User user = User.login(email, password);

        if (user != null) {
            status.put("Status", "success");
            session.setAttribute("User", user);
        } else {
            status.put("Status", "fail");
        }


        webpage.write(String.valueOf(status));
        webpage.close();
    }
}
