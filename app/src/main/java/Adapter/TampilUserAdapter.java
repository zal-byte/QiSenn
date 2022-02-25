package Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.tamamura.qisen.R;
import com.tamamura.qisen.admin.TampilUser;
import com.tamamura.qisen.admin.UbahUser;

import java.util.ArrayList;

import ClassModel.ModelTampilUser;
import Client.UserAction;
import de.hdodenhof.circleimageview.CircleImageView;

public class TampilUserAdapter extends RecyclerView.Adapter<TampilUserAdapter.MyViewHolder> {
    Activity activity;
    ArrayList<ModelTampilUser> data;

    UserAction userAction;

    public TampilUserAdapter(ArrayList<ModelTampilUser> data, Activity activity) {
        this.data = data;
        this.activity = activity;
        this.userAction = new UserAction(this.activity);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.activity).inflate(R.layout.tampil_user_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.identifier.setText(data.get(position).getIdentifier());
        holder.nama.setText(data.get(position).getNama());

        holder.btn_edit.setOnClickListener(View -> {
            Intent intent = new Intent(this.activity, UbahUser.class);
            intent.putExtra("identifier", data.get(position).getIdentifier());
            if (this.activity.getIntent().getStringExtra("user").equals("siswa")) {
                intent.putExtra("user", "siswa");
            } else if (this.activity.getIntent().getStringExtra("user").equals("guru")) {
                intent.putExtra("user", "guru");
            }
            this.activity.startActivity(intent);
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(this.activity);

        holder.btn_delete.setOnClickListener(View -> {

            builder.setMessage("Apakah kamu ingin menghapus ( " + data.get(position).getIdentifier() + " ) ?");
            builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    TampilUser.removeUser(data.get(position).getIdentifier());
                }
            });
            builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();

        });

        Picasso.get().load(userAction.img_api + data.get(position).getFoto()).into(holder.user_img);
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView identifier, nama;
        CircleImageView user_img;
        ImageButton btn_edit, btn_delete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            identifier = itemView.findViewById(R.id.identifier);
            nama = itemView.findViewById(R.id.nama);
            user_img = itemView.findViewById(R.id.user_img);
            btn_edit = itemView.findViewById(R.id.btn_edit);
            btn_delete = itemView.findViewById(R.id.btn_delete);
        }
    }
}
