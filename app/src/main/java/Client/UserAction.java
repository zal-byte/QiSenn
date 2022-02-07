package Client;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import UserSession.Session;

public class UserAction extends UserClient{
    Session session;
    Activity activity;
    public UserAction(Activity activity)
    {
        this.activity = activity;
        this.session = new Session(this.activity);
    }


    //start-login

    public String userLogin(HashMap<String, String> map) throws Exception {
        return post(api, map);
    }

    //end-login
    public String userProfile(String dest) throws Exception {
        return get(api + "?request=userProfile");
    }


    public String imgToBase64( Bitmap bitmap)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        byte[] res = baos.toByteArray();

        return String.valueOf(Base64.encodeToString(res, Base64.DEFAULT));
    }


}
