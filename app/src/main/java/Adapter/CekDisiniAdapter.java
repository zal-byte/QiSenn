package Adapter;

import android.app.Activity;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.tamamura.qisen.R;

import java.util.ArrayList;

import ClassModel.ModelCekDisini;
import Client.UserAction;

public class CekDisiniAdapter extends RecyclerView.Adapter<CekDisiniAdapter.MyViewHolder> {

    Activity activity;
    ArrayList<ModelCekDisini> data;
    UserAction userAction;

    public CekDisiniAdapter(Activity activity, ArrayList<ModelCekDisini> data) {
        this.activity = activity;
        this.data = data;
        this.userAction = new UserAction(this.activity);

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.activity).inflate(R.layout.cek_disini_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.absen_nama.setText(data.get(position).getNama());
        holder.absen_status.setText(data.get(position).getStatus_absen());
        holder.img_date.setText(data.get(position).getImg_date());
        holder.img_time.setText(data.get(position).getImg_time());


        System.out.println("UserImg : " + userAction.img_api + data.get(position).getPath());
        Picasso.get().load(userAction.img_api + data.get(position).getPath()).into(holder.img_absen);


        holder.laporan_cardview_1.setOnClickListener( View -> {
            Toast.makeText(activity, "ss", Toast.LENGTH_SHORT).show();
            if(holder.expandable_cardview.getVisibility() == View.GONE)
            {
                TransitionManager.beginDelayedTransition(holder.laporan_cardview_1, new AutoTransition());
                holder.expandable_cardview.setVisibility(View.VISIBLE);
            }else
            {
                TransitionManager.beginDelayedTransition(holder.laporan_cardview_1, new AutoTransition());
                holder.expandable_cardview.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView absen_nama, img_date, img_time, absen_status;
        ImageView img_absen;

        CardView expandable_cardview, laporan_cardview_1;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            absen_nama = itemView.findViewById(R.id.absen_nama);
            absen_status = itemView.findViewById(R.id.absen_status);

            img_time = itemView.findViewById(R.id.img_time);
            img_date = itemView.findViewById(R.id.img_date);

            img_absen = itemView.findViewById(R.id.img_absen);

            expandable_cardview = itemView.findViewById(R.id.expandable_cardview);
            laporan_cardview_1 = itemView.findViewById(R.id.laporan_cardview_1);
        }
    }

}
