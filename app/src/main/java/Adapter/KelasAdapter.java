package Adapter;

import android.app.Activity;
import android.content.Intent;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.tamamura.qisen.KelasActivity;
import com.tamamura.qisen.R;
import com.tamamura.qisen.UserProfile;

import java.util.ArrayList;
import java.util.List;

import ClassModel.ModelKelas;
import Client.UserAction;
import de.hdodenhof.circleimageview.CircleImageView;

public class KelasAdapter extends RecyclerView.Adapter<KelasAdapter.MyViewHolder>  {
    ArrayList<ModelKelas> modelKelas;
    ArrayList<ModelKelas> modelKelasPenuh;
    Activity activity;
    UserAction userAction;

    public KelasAdapter(ArrayList<ModelKelas> modelKelas, Activity activity) {
        this.modelKelas = modelKelas;
        this.modelKelasPenuh = new ArrayList<>(modelKelas);
        this.activity = activity;
        this.userAction = new UserAction(this.activity);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.activity).inflate(R.layout.list_kelas_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        holder.user_nama.setText(modelKelas.get(position).getNama());
        holder.num_list.setText(String.valueOf(position  + 1));
        Picasso.get().load(userAction.img_api + modelKelas.get(position).getFoto()).placeholder(R.drawable.ic_img_black_24).into(holder.user_img);
        holder.open_profile_button.setOnClickListener( View -> {
            this.activity.startActivity(new Intent(this.activity, UserProfile.class).putExtra("view_as","another").putExtra("user","siswa").putExtra("identifier", modelKelas.get(position).getIdentifier()));
        });

        holder.kelas_cardview.setOnClickListener( View -> {
            if( holder.expand_cardview.getVisibility() == View.VISIBLE )
            {
                TransitionManager.beginDelayedTransition(holder.expandable_linearlayout, new AutoTransition());
                holder.expand_cardview.setVisibility(View.GONE);
            }else
            {
                TransitionManager.beginDelayedTransition(holder.expandable_linearlayout, new AutoTransition());
                holder.expand_cardview.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public int getItemCount() {
        System.out.println("Adapter size : " + this.modelKelas.size());
        return modelKelas.size();
    }
    public void filterList(ArrayList<ModelKelas> filteredList)
    {
        modelKelas = filteredList;
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView user_img;
        TextView user_nama, num_list;
        ImageButton open_profile_button;
        CardView kelas_cardview, expand_cardview;
        LinearLayout expandable_linearlayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            user_img = itemView.findViewById(R.id.user_img);
            user_nama = itemView.findViewById(R.id.user_nama);
            open_profile_button = itemView.findViewById(R.id.open_profile_button);
            kelas_cardview = itemView.findViewById(R.id.kelas_cardview);
            expand_cardview = itemView.findViewById(R.id.expand_cardview);
            expandable_linearlayout = itemView.findViewById(R.id.expandable_linearlayout);
            num_list = itemView.findViewById(R.id.num_list);

        }

    }


}
