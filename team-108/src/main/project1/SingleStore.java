package main.project1;

import main.DB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@WebServlet(name = "SingleStoreServlet", urlPatterns = "/api/singlestore")
public class SingleStore extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter webpage = response.getWriter();
        String storeid = request.getParameter("storeid");
        ArrayList<String> paras = new ArrayList<>();
        paras.add(storeid);
        String jsonString = DB.listAccess(
                "select s.store_id, s.store_name, s.phone, s.email, s.address\n" +
                        "from stores as s\n" +
                        "where s.store_id = ?", paras).toString();
        webpage.write(jsonString);
        webpage.close();
    }
}