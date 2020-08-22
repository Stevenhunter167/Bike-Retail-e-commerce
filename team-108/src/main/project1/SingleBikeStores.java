package main.project1;

import main.DB;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@WebServlet(name = "SingleBikeStoresServlet", urlPatterns = "/api/singlebikestores")
public class SingleBikeStores extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter webpage = response.getWriter();
        String bikeid = request.getParameter("bikeid");
        String offset = request.getParameter("offset");
        ArrayList<String> paras = new ArrayList<>();
        paras.add(bikeid);
        paras.add(offset);
        String jsonString = DB.listAccess(
                "select stores.store_id, stores.store_name, stocks.quantity, stores.address, stores.phone, stores.email " +
                        "from stocks, stores " +
                        "where stocks.product_id = ? " +
                        "and stocks.store_id = stores.store_id " +
                        "order by stores.store_name " +
                        "limit 25 " +
                        "offset ?;", paras).toString();
        webpage.write(jsonString);
        webpage.close();
    }
}
