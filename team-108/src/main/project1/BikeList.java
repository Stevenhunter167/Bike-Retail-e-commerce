package main.project1;

import main.DB;
import org.json.simple.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@WebServlet(name = "BikeListServlet", urlPatterns = "/api/bikelist")
public class BikeList extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int limit = 20;
        PrintWriter webpage = response.getWriter();
        JSONObject result = DB.listAccess(
                "select p.product_id, p.product_name, b.brand_name, c.category_name, p.list_price, p.model_year, r.rating " +
                        "from products as p, brands as b, categories as c, ratings as r " +
                        "where b.brand_id = p.brand_id and c.category_id = p.category_id and p.product_id = r.bike_id order by r.rating desc " +
                        "limit " + limit + ";");
        for (int i = 0; i < limit; ++i) {
            JSONObject item = (JSONObject) result.get(i);
            String pId = (String) item.get("product_id");
            ArrayList<String> paras = new ArrayList<>();
            paras.add(pId);


//            JSONObject topStores = DB.listAccess("SELECT st.store_id, st.store_name " +
//                    "FROM products AS p, stocks AS s, stores AS st, (SELECT s.store_id AS sID, COUNT(o.order_id) AS sellBikes " +
//                    "FROM orders AS o, stores AS s " +
//                    "WHERE o.store_id = s.store_id " +
//                    "GROUP BY s.store_id) AS ss " +
//                    "WHERE p.product_id = s.product_id AND " +
//                    "s.store_id = st.store_id AND " +
//                    "ss.sId = st.store_id AND " +
//                    "p.product_id = ? " +
//                    "ORDER BY ss.sellBikes DESC " +
//                    "LIMIT 3;", paras);
//            item.put("first3stores", topStores);

            JSONObject topStores = DB.listAccess("SELECT st.store_id, s.store_name " +
                    "FROM products AS p, stocks AS st, stores AS s " +
                    "WHERE p.product_id = ? AND " +
                    "p.product_id = st.product_id AND " +
                    "st.store_id = s.store_id " +
                    "GROUP BY st.store_id, s.store_name " +
                    "ORDER BY COUNT(st.product_id) DESC " +
                    "LIMIT 3;", paras);
            item.put("first3stores", topStores);
        }
        webpage.write(String.valueOf(result));
        webpage.close();
    }
}