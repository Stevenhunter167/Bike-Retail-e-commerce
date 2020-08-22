package main.project2;

import main.DB;
import org.json.simple.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

@WebServlet(name = "CartServlet", urlPatterns = "/api/cart")
public class CartServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String action = request.getParameter("action");
        String productId = request.getParameter("product_id");
        JSONObject cartInfo = new JSONObject();

        HttpSession session = request.getSession(true);

        User currentUser = (User) session.getAttribute("User");
        if (currentUser != null) {
            if (action.equals("buy")) {
                currentUser.buy(productId);
            } else if (action.equals("pop")) {
                currentUser.pop(productId);
            } else if (action.equals("remove")) {
                currentUser.remove(productId);
            }

            HashMap<String, Integer> items = currentUser.lookup();

            for (String i: items.keySet()) {
                JSONObject itemInfo = new JSONObject();
                itemInfo.put("number", items.get(i));

                ArrayList<String> params = new ArrayList<>();
                params.add(i);

                itemInfo.put("itemInfo", DB.listAccess(
                        "SELECT product_name, brand_id, category_id, model_year, list_price " +
                                "FROM products " +
                                "WHERE product_id = ?;", params));
                cartInfo.put(i, itemInfo);
            }

            currentUser.setCartInfo(cartInfo);
            session.setAttribute("User", currentUser);
        }




        PrintWriter webpage = response.getWriter();
        webpage.write(String.valueOf(cartInfo));
        webpage.close();
    }
}
