package com.example.mobileplatformsandprogramming;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class BaseAdapterTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_adapter_test);

        /*              KAKO MOZI DA SE ZEMI PREKU INTENT ARRAYLIST             */
        List<MovieModel> challenge = (List<MovieModel>) this.getIntent().getExtras().getParcelableArrayList("moviesList");
        Toast.makeText(BaseAdapterTestActivity.this, "Movies: " + myList, Toast.LENGTH_SHORT).show();
    }


    public class MovieModel {
        String name;
        String year;
        String director;
        String imdbId;
    }

/*
    public class MovieAdapter extends BaseAdapter {
        List<MovieModel> moviesList = moviesToBeShown;

        @Override
        public int getCount() {
            return moviesList.size();
        }

        @Override
        public MovieModel getItem(int position) {
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
                convertView = inflater.inflate(R.layout.list_of_movies, (ViewGroup) convertView, false);
            }

            TextView movieName = (TextView) convertView.findViewById(R.id.movieName);
            TextView movieDirector = (TextView) convertView.findViewById(R.id.movieDirector);
            TextView movieYear = (TextView) convertView.findViewById(R.id.movieYear);

            MovieModel movieModel = moviesList.get(position);

            movieName.setText(movieModel.name);
            movieDirector.setText(movieModel.director);
            movieYear.setText(movieModel.year);

            return convertView;
        }
    }*/
}
