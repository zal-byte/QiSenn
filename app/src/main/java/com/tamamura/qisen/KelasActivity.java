package com.tamamura.qisen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import Client.UserAction;
import UserSession.Session;

public class KelasActivity extends AppCompatActivity {

    UserAction userAction;
    Session session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kelas);


    }
    private void classInit()
    {

        userAction = new UserAction(this);
        session = new Session(this);


    }
    private void viewInit()
    {

    }
    private void logic()
    {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.kelas_option_menu, menu);

        MenuItem kembali = menu.findItem(R.id.kembali);
        MenuItem searchItem = menu.findItem(R.id.kelas_search);

        SearchManager searchManager = (SearchManager) KelasActivity.this.getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = null;
        if( searchItem != null )
        {
            searchView = (SearchView)  searchItem.getActionView();
        }
        if( searchView != null )
        {

            searchView.setSearchableInfo(searchManager.getSearchableInfo(KelasActivity.this.getComponentName()));

        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });


        return true;
    }
}