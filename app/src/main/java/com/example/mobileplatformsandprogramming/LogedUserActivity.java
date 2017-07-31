package com.example.mobileplatformsandprogramming;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class LogedUserActivity extends Activity {
    Map<String, String> allReturnedMovies = null;
    List<MovieModel> moviesToBeShown = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loged_user);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String userUsername = extras.getString("logedUserUsername");
            String userId = extras.getString("logedUserId");
            Toast.makeText(LogedUserActivity.this, "User: " + userUsername + " is logged with id: " + userId, Toast.LENGTH_SHORT).show();
        }

        try {
            makeAsyncCall();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void makeAsyncCall() throws JSONException {
        Button btnAsync = (Button) findViewById(R.id.btnAsync);
        EditText movieName = (EditText) findViewById(R.id.editTextMovieName);

        btnAsync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TwitterRestClient.get("inception", null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        // If the response is JSONObject instead of expected JSONArray
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                        JSONObject firstEvent = null;
//                            Map<String, String> allReturnedMovies = new HashMap();
                        moviesToBeShown = new ArrayList<MovieModel>();
                        try {
                            firstEvent = (JSONObject) timeline.get(0);
                            for (int i = 0; i < timeline.length(); i++) {
                                MovieModel tempMovie = new MovieModel();
                                tempMovie.name = ((JSONObject) timeline.get(i)).getString("title");
                                tempMovie.director = ((JSONObject) timeline.get(i)).getString("director");
                                tempMovie.year = ((JSONObject) timeline.get(i)).getString("release_date");
                                tempMovie.imdbId = ((JSONObject) timeline.get(i)).getString("imdb_id");
                                moviesToBeShown.add(tempMovie);
//                                    allReturnedMovies.put(((JSONObject)timeline.get(i)).getString("title"), ((JSONObject)timeline.get(i)).getString("imdb_id"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String tweetText = null;
                        try {
                            System.out.println(firstEvent);
                            tweetText = firstEvent.getString("email");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // Do something with the response
                        System.out.println(tweetText);

                        /*              TUKA DA SE PRATI ARRAYLIST VO DRUGOTO ACTIVITY ILI DA SE POVRZI SO BASEADAPTER NEKAKO...        */
                        Intent intent = new Intent(LogedUserActivity.this, BaseAdapterTestActivity.class);
                        intent.putParcelableArrayListExtra("listedMovies", moviesToBeShown);
                        startActivity(intent);
                    }
                });
            }
        });

    }

    private void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(LogedUserActivity.this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    /*              BASE ADAPTER                */
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
                LayoutInflater inflater = (LayoutInflater) LogedUserActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
