package Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.tamamura.qisen.R;

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


        Picasso.get().load(userAction.img_api + data.get(position).getFoto()).into(holder.user_img);
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView identifier, nama;
        CircleImageView user_img;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            identifier = itemView.findViewById(R.id.identifier);
            nama = itemView.findViewById(R.id.nama);
            user_img = itemView.findViewById(R.id.user_img);
        }
    }
}
