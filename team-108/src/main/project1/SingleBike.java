package main.project1;

import com.google.gson.JsonObject;
import main.DB;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@WebServlet(name = "SingleBikeServlet", urlPatterns = "/api/singlebike")
public class SingleBike extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter webpage = response.getWriter();
        String bikeid = request.getParameter("bikeid");

        ArrayList<String> params = new ArrayList<>();
        params.add(bikeid);

        String jsonString = DB.listAccess(
                "select p.product_name, b.brand_name, c.category_name, p.list_price, p.model_year, r.rating \n" +
                "from products as p, brands as b, categories as c, ratings as r \n" +
                "where p.product_id = ? \n" +
                "and b.brand_id = p.brand_id \n" +
                "and c.category_id = p.category_id \n" +
                "and p.product_id = r.bike_id\n" +
                "order by r.rating desc;", params).toString();
        webpage.write(jsonString);
        webpage.close();
    }
}
