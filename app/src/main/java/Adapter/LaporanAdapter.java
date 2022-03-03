package Adapter;

import android.app.Activity;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.tamamura.qisen.R;

import java.util.ArrayList;

import ClassModel.ModelLaporan;
import Client.UserAction;

public class LaporanAdapter extends RecyclerView.Adapter<LaporanAdapter.MyViewHolder> {
    Activity activity;
    ArrayList<ModelLaporan> data;
    UserAction userAction;
    public LaporanAdapter(ArrayList<ModelLaporan> data, Activity activity) {
        this.data = data;
        this.activity = activity;
        this.userAction = new UserAction(this.activity);
    }

    @NonNull
    @Override
    public LaporanAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.activity).inflate(R.layout.laporan_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LaporanAdapter.MyViewHolder holder, int position) {
        holder.absen_tanggal.setText(data.get(position).getTanggal_absen());
        holder.absen_status.setText(data.get(position).getStatus_absen());
        holder.laporan_cardview_1.setOnClickListener( View ->{
            if( holder.expandable_cardview.getVisibility() == View.VISIBLE){
                TransitionManager.beginDelayedTransition(holder.laporan_cardview_1, new AutoTransition());
                holder.expandable_cardview.setVisibility(View.GONE);
            }else{
                TransitionManager.beginDelayedTransition(holder.laporan_cardview_1, new AutoTransition());
                holder.expandable_cardview.setVisibility(View.VISIBLE);
            }
        });
        Picasso.get().load(userAction.img_api + data.get(position).getPath()).into(holder.img_absen);
        holder.img_date.setText(data.get(position).getImg_date());
        holder.img_time.setText(data.get(position).getImg_time());

    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView absen_tanggal, absen_status, img_date, img_time;
        CardView laporan_cardview_1, expandable_cardview;

        ImageView img_absen;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            absen_tanggal = itemView.findViewById(R.id.absen_tanggal);
            absen_status = itemView.findViewById(R.id.absen_status);
            laporan_cardview_1 = itemView.findViewById(R.id.laporan_cardview_1);
            expandable_cardview = itemView.findViewById(R.id.expandable_cardview);

            img_date = itemView.findViewById(R.id.img_date);
            img_time = itemView.findViewById(R.id.img_time);

            img_absen = itemView.findViewById(R.id.img_absen);
        }
    }
}
