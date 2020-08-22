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
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.HashMap;

@WebServlet(name = "PaymentServlet", urlPatterns = "/api/payment")
public class PaymentServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(true);
        User currentUser = (User) session.getAttribute("User");
        String cId = currentUser.getCustomerId();
        HashMap<String, Integer> cart = currentUser.getCart();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDateTime now = LocalDateTime.now();

        String firstName = request.getParameter("first_name");
        String lastName = request.getParameter("last_name");
        String id = request.getParameter("id");
        String expiration = request.getParameter("expiration");
        ArrayList<String> paras = new ArrayList<>();
        paras.add(firstName);
        paras.add(lastName);
        paras.add(id);
        paras.add(expiration);

        JSONObject resp = new JSONObject();
        PrintWriter webpage = response.getWriter();

        if (!expiration.matches("\\d\\d\\d\\d/\\d\\d/\\d\\d")) {
            resp.put("success", false);
            resp.put("confirmation", null);
            webpage.write(String.valueOf(resp));
            webpage.close();
            return;
        }


        JSONObject ccInfo = DB.listAccess("SELECT * " +
                "FROM creditcards " +
                "WHERE first_name = ? AND last_name = ? AND " +
                "id = ? AND expiration = ?;", paras);



        if (ccInfo.size() != 0) {
            resp.put("success", true);
            JSONObject confirmation = new JSONObject();

            // insert the order to db
            ArrayList<String> insertQueryParams = new ArrayList<>();
            insertQueryParams.add(cId);
            insertQueryParams.add(dtf.format(now));
            insertQueryParams.add(dtf.format(now));
            DB.update("INSERT INTO orders(customer_id, order_status, order_date, required_date, shipped_date, store_id, staff_id) VALUES " +
                    "(?, 2, ?, ?, null, null, null);", insertQueryParams);


            // get order id query
            ArrayList<String> orderIdQuery = new ArrayList<>();
            orderIdQuery.add(cId);
            String orderId = ((JSONObject)DB.listAccess("SELECT order_id " +
                    "FROM orders " +
                    "WHERE customer_id = ? " +
                    "ORDER BY order_id DESC;", orderIdQuery).get(0)).get("order_id").toString();
            confirmation.put("order_id", orderId);

            int itemId = 1;
            // insert the order_items to db
            for (String pId: cart.keySet()) {
                ArrayList<String> listPriceQuery = new ArrayList<>();
                listPriceQuery.add(pId);
                String listPrice = ((JSONObject)DB.listAccess("SELECT list_price " +
                        "FROM products " +
                        "WHERE product_id = ?;", listPriceQuery).get(0)).get("list_price").toString();

                ArrayList<String> insertOrder_items = new ArrayList<>();
                insertOrder_items.add(orderId);
                insertOrder_items.add(Integer.toString(itemId));
                insertOrder_items.add(pId);
                insertOrder_items.add(Integer.toString(cart.get(pId)));
                insertOrder_items.add(listPrice);
                DB.update("INSERT INTO order_items(order_id, item_id, product_id, quantity, list_price,discount) VALUES " +
                        "(?, ?, ?, ?, ?, 0);", insertOrder_items);
                ++itemId;
            }

            confirmation.put("cart_info", currentUser.getCartInfo());


            resp.put("confirmation", confirmation);

            currentUser.setCart(new HashMap<String, Integer>());
        }
        else {
            resp.put("success", false);
            resp.put("confirmation", null);
        }



        webpage.write(String.valueOf(resp));
        webpage.close();
    }
}
