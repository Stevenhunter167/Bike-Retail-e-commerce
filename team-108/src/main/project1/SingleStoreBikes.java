package main.project1;

import main.DB;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@WebServlet(name = "SingleStoreBikesServlet", urlPatterns = "/api/singlestorebikes")
public class SingleStoreBikes extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter webpage = response.getWriter();
        String storeid = request.getParameter("storeid");
        String offset = request.getParameter("offset");
        ArrayList<String> paras = new ArrayList<>();
        paras.add(storeid);
        paras.add(storeid);
        paras.add(offset);
        String jsonString = DB.listAccess(
                "select stores.store_id, stores.store_name, products.product_id, products.product_name, products.list_price, products.model_year, stocks.quantity, categories.category_name, brands.brand_name, ratings.rating\n" +
                        "from stores, stocks, products, brands, categories, ratings\n" +
                        "where stores.store_id = ?\n" +
                        "and stocks.store_id = ?\n" +
                        "and brands.brand_id = products.brand_id\n" +
                        "and products.category_id = categories.category_id\n" +
                        "and products.product_id = ratings.bike_id\n" +
                        "and products.product_id = stocks.product_id\n" +
                        "order by products.model_year DESC, products.product_name " +
                        "limit 25 " +
                        "offset ?;", paras).toString();
        webpage.write(jsonString);
        webpage.close();
    }
}
