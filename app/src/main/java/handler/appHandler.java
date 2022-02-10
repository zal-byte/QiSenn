package handler;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tamamura.qisen.LoginActivity;
import com.tamamura.qisen.R;

import java.io.ByteArrayOutputStream;

import UserSession.Session;

public class appHandler {

    public Activity activity;
    TextView toastMessage;

    Session session;

    public appHandler(Activity activity) {
        this.activity = activity;
        this.session = new Session(this.activity);
    }



    public String imgToBase64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        byte[] res = baos.toByteArray();

        return String.valueOf(Base64.encodeToString(res, Base64.DEFAULT));
    }

    public void userLogout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.activity);
        builder.setMessage("Apakah kamu yakin ingin keluar?");
        builder.setPositiveButton("Ya", (dialog, which) -> {
            session.logout();
            appHandler.this.activity.finish();
            appHandler.this.activity.startActivity(new Intent(appHandler.this.activity, LoginActivity.class));
        });
        builder.setNegativeButton("Batal", (dialog, which) -> {
            dialog.dismiss();
        });
        builder.show();
    }


}
