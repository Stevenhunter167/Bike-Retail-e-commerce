package main.project4;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import main.DB;
import org.json.simple.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "FullTextSearchServlet", urlPatterns = "/api/fullTextSearch")
public class FullTextSearchServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String query = request.getParameter("product_name");
        JsonArray jsonArray = new JsonArray();

        if (query == null || query.trim().isEmpty()) {
            response.getWriter().write(jsonArray.toString());
            return;
        }

        String[] prefixes = query.split(" ");
        for (String i: prefixes) {
            System.out.println(i);
        }
        String sql = "SELECT product_id, product_name " +
                "FROM products " +
                "WHERE MATCH(product_name) AGAINST ('";
        for (String p: prefixes) {
            sql += "+" + p + "* ";
        }
        sql += "' IN BOOLEAN MODE) LIMIT 10;";
        JSONObject rawData = DB.listAccess(sql);

        for (int i = 0; i < rawData.size(); ++i) {
            Integer product_id = Integer.valueOf(((JSONObject) rawData.get(i)).get("product_id").toString());
            String product_name = ((JSONObject) rawData.get(i)).get("product_name").toString();
            jsonArray.add(generateJsonObject(product_id, product_name));
        }


        PrintWriter webpage = response.getWriter();
        webpage.write(String.valueOf(jsonArray));
        webpage.close();
    }



    /*
     * Generate the JSON Object from hero to be like this format:
     * {
     *   "value": "fake Bike",
     *   "data": { "product_id": 11 }
     * }
     *
     */
    private static JsonObject generateJsonObject(Integer product_id, String product_name) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("value", product_name);

        JsonObject additionalDataJsonObject = new JsonObject();
        additionalDataJsonObject.addProperty("product_id", product_id);

        jsonObject.add("data", additionalDataJsonObject);
        return jsonObject;
    }
}
