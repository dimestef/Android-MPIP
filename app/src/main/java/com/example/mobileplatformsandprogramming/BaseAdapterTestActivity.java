package com.example.mobileplatformsandprogramming;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BaseAdapterTestActivity extends AppCompatActivity {
    MovieAdapter movieAdapter;
    DataHelper dh;
    ArrayList<String> checkDuplicates;
    HashMap<String, String> checkDuplicatesHash;
    ListView wishlistMovieBaseAdapterView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_adapter_test);

        dh = new DataHelper(this);

        checkDuplicates = new ArrayList<String>();
        checkDuplicatesHash = new HashMap<>();

        Cursor res = dh.getAllWishlistData();
        while (res.moveToNext()) {
            checkDuplicatesHash.put(res.getString(0), res.getString(1));
            checkDuplicates.add(res.getString(1));
        }

        movieAdapter = new MovieAdapter();
        final ListView exampleView = (ListView) findViewById(R.id.movieListItems);
        exampleView.setAdapter(movieAdapter);
        movieAdapter.changeListItem(checkDuplicates);

        wishlistMovieBaseAdapterView = (ListView) findViewById(R.id.movieListItems);
        wishlistMovieBaseAdapterView.setAdapter(movieAdapter);

        wishlistMovieBaseAdapterView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(BaseAdapterTestActivity.this, movieAdapter.getItem(position), Toast.LENGTH_SHORT).show();
                deleteMovieWishlist(position);
            }
        });
    }

    private void deleteMovieWishlist(final int moviePosition) {
        AlertDialog.Builder builder = new AlertDialog.Builder(BaseAdapterTestActivity.this);
        builder.setCancelable(true);
        builder.setTitle(movieAdapter.getItem(moviePosition));
        builder.setNeutralButton("Delete move", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                for (Map.Entry<String, String> entry : checkDuplicatesHash.entrySet())
                {
                    if(entry.getValue().equals(movieAdapter.getItem(moviePosition))){
                        dh.deleteMovieWishlistData(Integer.parseInt(entry.getKey()));
                        break;
                    }
                }

                checkDuplicates = new ArrayList<String>();

                Cursor res = dh.getAllWishlistData();
                while (res.moveToNext()) {
                    checkDuplicates.add(res.getString(1));
                }

                movieAdapter.changeListItem(checkDuplicates);
//                Toast.makeText(BaseAdapterTestActivity.this, "", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.setMessage(movieAdapter.getItem(moviePosition));
        builder.show();
    }

    public class MovieAdapter extends BaseAdapter {
        List<String> moviesList;

        public MovieAdapter() {
            this.moviesList = new ArrayList<String>();
        }

        public void addItem(String item) {
            this.moviesList.add(item);
            notifyDataSetChanged();
        }

        public void changeListItem(List<String> itemList) {
            this.moviesList = new ArrayList<String>();
            this.moviesList.addAll(itemList);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return moviesList.size();
        }

        @Override
        public String getItem(int position) {
            return moviesList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) BaseAdapterTestActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.list_of_wishlist, (ViewGroup) convertView, false);
            }

            TextView wishlistMovieName = (TextView) convertView.findViewById(R.id.wishlistMovieName);

            wishlistMovieName.setText(moviesList.get(position));

            return convertView;
        }
    }
}
