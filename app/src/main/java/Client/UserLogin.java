package Client;

import android.app.Activity;

import java.util.HashMap;

import UserSession.Session;

public class UserLogin extends UserClient implements Interface.qis_face{
    Session session;
    Activity activity;
    public UserLogin(Activity activity)
    {
        this.activity = activity;
        this.session = new Session(this.activity);
    }
    @Override
    public String get(String url )
    {
        return get(url);
    }

    @Override
    public String post(String url, HashMap<String, String> param)
    {
        return post(url, param);
    }

    @Override
    public HashMap<String, String> userLogin() {
        HashMap<String, String> map = new HashMap<>();

        map.put("request", "userLogin");




        return map;
    }
}
