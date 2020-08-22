package main.project2;


import main.DB;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class User {
    private String email;
    private String password;
    private String customerId;
    private HashMap<String, Integer> cart;
    private JSONObject cartInfo;

    public static User login(String email, String password) {
        ArrayList<String> paras = new ArrayList<>();
        paras.add(email);
        JSONObject targetPswds = DB.listAccess(
                "SELECT customer_id, password " +
                "FROM customers " +
                "WHERE email = ?;", paras);
        if (targetPswds.size() > 0) {
            JSONObject targetPswd = (JSONObject) targetPswds.get(0);
            // verify encrypted password
            if (new StrongPasswordEncryptor().checkPassword(password, targetPswd.get("password").toString())) {
                return new User(email, password, targetPswd.get("customer_id").toString());
            }
        }

        return null;
    }

    private User(String email, String password, String cId) {
        this.email = email;
        this.password = password;
        this.customerId = cId;
        this.cart = new HashMap<String, Integer>();
        this.cartInfo = new JSONObject();
    }


    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getCustomerId() {
        return customerId;
    }

    public HashMap<String, Integer> getCart() {
        return cart;
    }

    public JSONObject getCartInfo() {
        return cartInfo;
    }

    public void setCartInfo(JSONObject cartInfo) {
        this.cartInfo = cartInfo;
    }

    public void setCart(HashMap<String, Integer> cart) {
        this.cart = cart;
    }

    public void buy(String item) {
        Integer old = this.cart.get(item);
        if (old == null) {
            this.cart.put(item, 1);
        } else {
            this.cart.replace(item, old + 1);
        }
    }

    public boolean pop(String item) {
        Integer old = this.cart.get(item);
        if (old == null || old == 0) {
            return false;
        } else if (old == 1) {
            this.cart.remove(item);
            return false;
        }
        this.cart.replace(item, old - 1);
        return true;
    }

    public void remove(String item) {
        this.cart.remove(item);
    }

    public HashMap<String, Integer> lookup() {
        return this.cart;
    }


}
