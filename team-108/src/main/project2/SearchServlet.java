package main.project2;

import main.DB;
import org.json.simple.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;

@WebServlet(name = "SearchServlet", urlPatterns = "/api/search")
public class SearchServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        long TSstart = System.nanoTime();
        String baseSQL = "SELECT DISTINCT p.product_id, p.product_name, c.category_name, b.brand_name, p.model_year, p.list_price, r.rating " +
                "FROM products AS p, categories AS c, brands AS b, stores AS s, stocks AS stk, ratings AS r " +
                "WHERE p.product_id = stk.product_id AND s.store_id = stk.store_id AND " +
                "p.brand_id = b.brand_id AND p.category_id = c.category_id AND p.product_id = r.bike_id ";

        ArrayList<String> params = new ArrayList<>();

        String product_name = request.getParameter("product_name");
        System.out.println(product_name);
        if (product_name != null && !product_name.equals("%%")) {
            String[] prefixes = product_name.substring(1, product_name.length()-1).split(" ");
            for (String i: prefixes) {
                System.out.println(i);
            }
            if (product_name.equals("*%")) {
                baseSQL += "AND (p.product_name LIKE '?%' OR p.product_name LIKE '!%' " +
                        "OR p.product_name LIKE '@%' " +
                        "OR p.product_name LIKE '#%' " +
                        "OR p.product_name LIKE '$%' " +
                        "OR p.product_name LIKE '^%' " +
                        "OR p.product_name LIKE '&%' " +
                        "OR p.product_name LIKE '*%' " +
                        "OR p.product_name LIKE '(%' " +
                        "OR p.product_name LIKE ')%' " +
                        "OR p.product_name LIKE '-%' " +
                        "OR p.product_name LIKE '=%' " +
                        "OR p.product_name LIKE '+%' " +
                        "OR p.product_name LIKE '`%' " +
                        "OR p.product_name LIKE '~%' " +
                        "OR p.product_name LIKE '[%' " +
                        "OR p.product_name LIKE ']%' " +
                        "OR p.product_name LIKE '{%' " +
                        "OR p.product_name LIKE '}%' " +
                        "OR p.product_name LIKE ';%' " +
                        "OR p.product_name LIKE ':%' " +
                        "OR p.product_name LIKE '|%' " +
                        "OR p.product_name LIKE ',%' " +
                        "OR p.product_name LIKE '<%' " +
                        "OR p.product_name LIKE '.%' " +
                        "OR p.product_name LIKE '>%' " +
                        "OR p.product_name LIKE '/%') ";
            } else {
                baseSQL += "AND MATCH(product_name) AGAINST ('";
                for (String p: prefixes) {
                    baseSQL += "+" + p + "* ";
                }
                baseSQL += "' IN BOOLEAN MODE) ";
            }
        }
        String brand = request.getParameter("brand");
        if (brand != null) {
            baseSQL += "AND b.brand_name LIKE ? ";
            params.add("%" + brand + "%");
        }
        String year = request.getParameter("year");
        if (year != null) {
            baseSQL += "AND p.model_year = ? ";
            params.add(year);
        }
        String store = request.getParameter("store");
        if (store != null) {
            baseSQL += "AND s.store_name LIKE ? ";
            params.add("%" + store + "%");
        }
        String category = request.getParameter("category");
        if (category != null) {
            baseSQL += "AND c.category_name = ? ";
            params.add(category);
        }
        String sorted = request.getParameter("sorted");
        if (sorted != null) {
            String firstSeq = sorted.split(";")[0].substring(0, 1);
            String firstAttr = sorted.split(";")[0].substring(1);
            String secondSeq = sorted.split(";")[1].substring(0, 1);
            String secondAttr = sorted.split(";")[1].substring(1);

            ArrayList<String> check = new ArrayList<>(); check.add("rating"); check.add("product_name");
            if (!check.contains(firstAttr) || !check.contains(secondAttr)) {
                return;
            }

            if (firstSeq.equals("-")) {
                baseSQL += "ORDER BY " + firstAttr.substring(0, 1) + "." +
                        firstAttr + " DESC, " + secondAttr.substring(0, 1) + "." +
                        secondAttr + " ";
            } else {
                baseSQL += "ORDER BY " + firstAttr.substring(0, 1) + "." +
                        firstAttr + ", "  + secondAttr.substring(0, 1) + "." +
                        secondAttr + " ";
            }
            if (secondSeq.equals("-")) {
                baseSQL += "DESC ";
            }
        }

        String limit= request.getParameter("limit");
        baseSQL += "LIMIT ? ";
        params.add(limit);

        String offset = request.getParameter("offset");
        if (offset != null) {
            baseSQL += "OFFSET ? ";
            params.add(offset);
        }

        baseSQL += ";";

        long TJstart = System.nanoTime();
        JSONObject result = DB.listAccess(baseSQL, params);
        double TJ = (double)(System.nanoTime() - TJstart) / 1000000000;

        for (int i = 0; i < result.size(); ++i) {
            JSONObject item = (JSONObject) result.get(i);
            String pId = (String) item.get("product_id");
            ArrayList<String> p = new ArrayList<>();
            p.add(pId);

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
//                    "LIMIT 3;", p);
//            item.put("first3stores", topStores);

            JSONObject topStores = DB.listAccess("SELECT st.store_id, s.store_name " +
                    "FROM products AS p, stocks AS st, stores AS s " +
                    "WHERE p.product_id = ? AND " +
                    "p.product_id = st.product_id AND " +
                    "st.store_id = s.store_id " +
                    "GROUP BY st.store_id, s.store_name " +
                    "ORDER BY COUNT(st.product_id) DESC " +
                    "LIMIT 3;", p);
            item.put("first3stores", topStores);
        }

        PrintWriter webpage = response.getWriter();
        webpage.write(String.valueOf(result));
        webpage.close();

        double TS = (double)(System.nanoTime() - TSstart) / 1000000000;


        String path = getServletContext().getRealPath("/WEB-INF") + "/log.txt";
        File file = new File(path);
        System.out.println("writing at " + path);
        BufferedWriter log = new BufferedWriter(new FileWriter(file, true));
        log.write(""+TJ+","+TS+"\n");
        log.close();
    }
}
