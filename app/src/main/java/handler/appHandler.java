package handler;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tamamura.qisen.R;

import java.io.ByteArrayOutputStream;

public class appHandler {

    public Activity activity;
    TextView toastMessage;

    public appHandler(Activity activity) {
        this.activity = activity;
    }

    public void showToast(String message) {
        Toast toast = new Toast(this.activity);

        View view = LayoutInflater.from(this.activity).inflate(R.layout.custom_toast, null);
        toastMessage = view.findViewById(R.id.toastMessage);
        toastMessage.setText(message);


        toast.setView(view);
        toast.show();
    }

    public String imgToBase64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        byte[] res = baos.toByteArray();

        return String.valueOf(Base64.encodeToString(res, Base64.DEFAULT));
    }


}
