package main.project3;

import main.DB;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class Staff {

    private String email;
    private String password;
    private String staff_id;

    private Staff(String email, String password, String staff_id) {
        this.email = email;
        this.password = password;
        this.staff_id = staff_id;
    }

    public static Staff login(String email, String password) {
        ArrayList<String> paras = new ArrayList<>();
        paras.add(email);
        JSONObject targetPswds = DB.listAccess(
                "SELECT staff_id, password " +
                        "FROM staffs " +
                        "WHERE email = ?;", paras);
        if (targetPswds.size() > 0) {
            JSONObject targetPswd = (JSONObject) targetPswds.get(0);
            // verify encrypted password
            if (new StrongPasswordEncryptor().checkPassword(password, targetPswd.get("password").toString())) {
                return new Staff(email, password, targetPswd.get("staff_id").toString());
            }
        }
        return null;
    }

}
