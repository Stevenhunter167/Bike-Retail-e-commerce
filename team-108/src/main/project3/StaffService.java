package main.project3;


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


@WebServlet(name = "staffServiceServlet", urlPatterns = "/api/staffService")
public class StaffService extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(true);
        PrintWriter webpage = response.getWriter();
        JSONObject status = new JSONObject();
        if (session.getAttribute("Staff") == null) {
            status.put("status", "fail");
        }
        if (request.getParameter("action").equals("checkLogin") && session.getAttribute("Staff") != null) {
            status.put("loggedin", true);
        }
        if (request.getParameter("action").equals("logout")) {
            session.setAttribute("Staff", null);
        }
        if (request.getParameter("action").equals("meta")) {
            status.put("meta", DB.listAccess("CALL meta()", true));
        }
        if (request.getParameter("action").equals("add_bike")) {
            try {
                String query = "CALL add_bike(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                ArrayList<String> params = new ArrayList<>();
                params.add(request.getParameter("product_name"));
                params.add(request.getParameter("store_name"));
                params.add(request.getParameter("store_phone"));
                params.add(request.getParameter("store_email"));
                params.add(request.getParameter("store_address"));
                params.add(request.getParameter("brand_name"));
                params.add(request.getParameter("category_name"));
                params.add(request.getParameter("model_year"));
                params.add(request.getParameter("list_price"));
                params.add(request.getParameter("rating"));
                params.add(request.getParameter("quantity"));

                for (String p: params) {
                    if (p.equals("")) {
                        status.put("add_bike", "all input fields are required");
                        webpage.write(status.toString());
                        return;
                    }
                }

                status.put("add_bike", DB.listAccess(query, params, true));
            } catch (Exception e) {
                status.put("add_bike", e.toString());
            }
        }
        if (request.getParameter("action").equals("add_store")) {
            try {
                String query = "CALL add_store(?, ?, ?, ?)";
                ArrayList<String> params = new ArrayList<>();
                params.add(request.getParameter("store_name"));
                params.add(request.getParameter("store_phone"));
                params.add(request.getParameter("store_email"));
                params.add(request.getParameter("store_address"));

                for (String p: params) {
                    if (p.equals("")) {
                        status.put("add_store", "all input fields are required");
                        webpage.write(status.toString());
                        return;
                    }
                }

                status.put("add_store", DB.listAccess(query, params, true));
            } catch (Exception e) {
                status.put("add_store", e.toString());
            }
        }

        webpage.write(status.toString());
    }
}
