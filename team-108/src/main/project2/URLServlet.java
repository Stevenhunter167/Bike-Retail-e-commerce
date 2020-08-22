package main.project2;

import org.json.simple.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "URLServlet", urlPatterns = "/api/url")
public class URLServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(true);
        String url = request.getParameter("url");
        String action = request.getParameter("action");


        JSONObject resp = new JSONObject();


        if (action.equals("save")) {
            session.setAttribute("url", url);
            resp.put("action", "save");
            resp.put("status", "success");
        } else if (action.equals("lookup")) {
            resp.put("action", "lookup");
            resp.put("url", session.getAttribute("url").toString());
        }


        PrintWriter webpage = response.getWriter();
        webpage.write(String.valueOf(resp));
        webpage.close();
    }
}
