package main.project1;

import com.google.gson.JsonObject;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "IndexServlet", urlPatterns = "/api/index")
public class TestServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String item = request.getParameter("item");
        System.out.println(item);

        JsonObject json = new JsonObject();
        json.addProperty("name", "Jonathan");
        json.addProperty("age", 20);
        json.addProperty("success", true);

        PrintWriter webpage = response.getWriter();
        webpage.write(json.toString());
        webpage.close();

    }

}
