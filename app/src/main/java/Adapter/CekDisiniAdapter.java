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

import ClassModel.ModelCekDisini;

public class CekDisiniAdapter extends RecyclerView.Adapter<CekDisiniAdapter.MyViewHolder> {

    Activity activity;
    ArrayList<ModelCekDisini> data;
    public CekDisiniAdapter(Activity activity, ArrayList<ModelCekDisini> data)
    {
        this.activity = activity;
        this.data = data;
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
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView absen_nama, absen_status;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            absen_nama = itemView.findViewById(R.id.absen_nama);
            absen_status = itemView.findViewById(R.id.absen_status);

        }
    }

}
