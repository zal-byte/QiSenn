package com.tamamura.qisen.guru;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Spinner;

import com.tamamura.qisen.R;

public class LaporanSiswa extends AppCompatActivity {

    RecyclerView tampil_recyclerview;
    Spinner kelas_spinner;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_siswa);
    }
}