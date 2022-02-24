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

    String Kelas = "Kelas";


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

    public void setKelas(String kelas) {
        editor.putString(this.Kelas, kelas);
        editor.commit();
    }

    public String getKelas() {
        return sharedPreferences.getString(this.Kelas, "");
    }

    public void logout() {
        editor.putBoolean(this.Status, false);
        editor.putString(this.NIS, "");
        editor.putString(this.NIK, "");
        editor.putString(this.Kelas, "");
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
    String dateToday = "dateToday";

    public String getDateToday() {
        return sharedPreferences.getString(this.dateToday, "");
    }

    public boolean getIsTodayAbsen() {
        return sharedPreferences.getBoolean(this.isTodayAbsen, true);
    }

    public void setDateToday(String date) {
        editor.putString(this.dateToday, date);
        editor.commit();
    }

    public void setIsTodayAbsen(boolean status) {
        editor.putBoolean(this.isTodayAbsen, status);
        editor.commit();
    }


}
