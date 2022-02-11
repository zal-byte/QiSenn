package UserSession;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class Session {
    public Activity activity;
    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;


    private String shared_name = "session";

    public Session(Activity activity) {

        this.activity = activity;
        this.sharedPreferences = activity.getSharedPreferences(this.shared_name, Context.MODE_PRIVATE);
        this.editor = this.sharedPreferences.edit();

    }


    String NIS = "NIS";
    String NIK = "NIK";
    String Status = "Status";

    String whoami = "whoami";

    public void setWhoami(String who) {
        this.editor.putString(this.whoami, who);
        editor.commit();
    }

    public void setLogin(Boolean status) {
        editor.putBoolean(Status, status);
        editor.commit();
    }

    public void setNIS(String NIS) {
        editor.putString(this.NIS, NIS);
        editor.commit();
    }

    public void setNIK(String NIK) {
        editor.putString(this.NIK, NIK);
        editor.commit();
    }

    public void logout() {
        editor.putBoolean(this.Status, false);
        editor.putString(this.NIS, "");
        editor.putString(this.NIK, "");
        editor.putString(this.whoami, "");
        editor.commit();
    }

    public String getNIS() {
        return sharedPreferences.getString(this.NIS, "");
    }

    public String getNIK() {
        return sharedPreferences.getString(this.NIK, "");
    }

    public Boolean isLogin() {
        return sharedPreferences.getBoolean(this.Status, false);
    }

    public String getWhoami() {
        return sharedPreferences.getString(this.whoami, "");
    }

    String isTodayAbsen = "isTodayAbsen";

    public Boolean getIsTodayAbsen() {
        return this.sharedPreferences.getBoolean(this.isTodayAbsen, false);
    }

    public void setIsTodayAbsen(Boolean status) {
        this.editor.putBoolean(this.isTodayAbsen, status);
    }


}
