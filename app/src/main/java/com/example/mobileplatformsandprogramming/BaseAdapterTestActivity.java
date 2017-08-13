package com.example.mobileplatformsandprogramming;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BaseAdapterTestActivity extends AppCompatActivity {
    MovieAdapter movieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_adapter_test);

        /*              KAKO MOZI DA SE ZEMI PREKU INTENT ARRAYLIST             */
        ArrayList<MovieModel> challenge = (ArrayList<MovieModel>) getIntent().getSerializableExtra("listedMovies");
        Toast.makeText(BaseAdapterTestActivity.this, "Movies: " + challenge.get(0).getYear(), Toast.LENGTH_LONG).show();

        movieAdapter = new MovieAdapter();
        final ListView exampleView = (ListView) findViewById(R.id.movieListItems);
        exampleView.setAdapter(movieAdapter);
        movieAdapter.changeListItem(challenge);
    }

    public class MovieAdapter extends BaseAdapter {
//        List<MovieModel> moviesList = getUsersList();
        List<MovieModel> moviesList;

        public MovieAdapter() {
            this.moviesList = new ArrayList<MovieModel>();
        }

        public void addItem(MovieModel item) {
            this.moviesList.add(item);
            notifyDataSetChanged();
        }

        public void changeListItem(ArrayList<MovieModel> itemList) {
            this.moviesList.addAll(itemList);
            notifyDataSetChanged();
        }

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

            movieName.setText(movieModel.getName());
            movieDirector.setText(movieModel.getDirector());
            movieYear.setText(movieModel.getYear());

            return convertView;
        }
    }

    public List<MovieModel> getUsersList() {
        List<MovieModel> userList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            MovieModel user = new MovieModel();
            user.setName("Dime " + i);
            user.setDirector("Stefanovski " + i);
            user.setYear("Year " + i);
            userList.add(user);
        }

        return userList;
    }
}
