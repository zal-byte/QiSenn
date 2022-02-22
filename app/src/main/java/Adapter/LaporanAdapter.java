package Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tamamura.qisen.R;

import java.util.ArrayList;

import ClassModel.ModelLaporan;

public class LaporanAdapter extends RecyclerView.Adapter<LaporanAdapter.MyViewHolder> {
    Activity activity;
    ArrayList<ModelLaporan> data;

    public LaporanAdapter(ArrayList<ModelLaporan> data, Activity activity) {
        this.data = data;
        this.activity = activity;
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
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView absen_tanggal, absen_status;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            absen_tanggal = itemView.findViewById(R.id.absen_tanggal);
            absen_status = itemView.findViewById(R.id.absen_status);
        }
    }
}
