package handler;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tamamura.qisen.R;

public class appHandler {

    public Activity activity;
    TextView toastMessage;
    public appHandler(Activity activity)
    {
        this.activity = activity;
    }

    public void showToast(String message)
    {
        Toast toast = new Toast(this.activity);

        View view = LayoutInflater.from(this.activity).inflate(R.layout.custom_toast, null);
        toastMessage = view.findViewById(R.id.toastMessage);
        toastMessage.setText(message);


        toast.setView(view);
        toast.show();
    }



}
