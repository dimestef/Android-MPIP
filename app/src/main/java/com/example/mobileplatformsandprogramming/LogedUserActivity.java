package com.example.mobileplatformsandprogramming;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.*;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class LogedUserActivity extends Activity {
    ArrayList<MovieModel> moviesToBeShown = null;
    MovieAdapter movieAdapter;
    ListView movieBaseAdapterView;
    DataHelper dh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loged_user);

        dh = new DataHelper(this);

        showInstructions("Instructions:", "1. Search a movie by name\n2. Select a movie ");

        movieAdapter = new MovieAdapter();

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

        movieBaseAdapterView = (ListView) findViewById(R.id.listItems);
        movieBaseAdapterView.setAdapter(movieAdapter);

        movieBaseAdapterView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MovieModel movie = movieAdapter.getItem(position);
                Toast.makeText(LogedUserActivity.this, movie.getName(), Toast.LENGTH_SHORT).show();
                showMessage("Movie title: ", movie);
            }
        });

        ImageView imageSearch = (ImageView) findViewById(R.id.imageSearch);
        imageSearch.setImageResource(R.drawable.search);
        
        showWishlistList();
    }

    private void showWishlistList() {
        Button btnShowWishlist = (Button) findViewById(R.id.btnShowWishlist);
        btnShowWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogedUserActivity.this, BaseAdapterTestActivity.class);
                intent.putExtra("listedMovies", moviesToBeShown);
                startActivity(intent);
            }
        });
    }

    private void makeAsyncCall() throws JSONException {
        ImageView imageAsync = (ImageView) findViewById(R.id.imageSearch);
        final EditText movieName = (EditText) findViewById(R.id.editTextMovieName);

        imageAsync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TwitterRestClient.get(movieName.getText().toString(), null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        moviesToBeShown = new ArrayList<MovieModel>();

                        MovieModel tempMovie = new MovieModel();
                        try {
                            JSONObject iOSObject = response.getJSONObject("data");
                            tempMovie.setName(iOSObject.getString("name"));
                            tempMovie.setDirector(iOSObject.getString("director"));
                            tempMovie.setYear(iOSObject.getString("year"));
                            tempMovie.setImdbId(iOSObject.getString("imdb_id"));
                            tempMovie.setImageUrl(iOSObject.getString("poster_url"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        moviesToBeShown.add(tempMovie);

                        movieAdapter.changeListItem(moviesToBeShown);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                        JSONObject firstEvent = null;
                        moviesToBeShown = new ArrayList<MovieModel>();
                        try {
                            firstEvent = (JSONObject) timeline.get(0);
                            for (int i = 0; i < timeline.length(); i++) {
                                MovieModel tempMovie = new MovieModel();
                                tempMovie.setName(((JSONObject) timeline.get(i)).getString("title"));
                                tempMovie.setDirector(((JSONObject) timeline.get(i)).getString("director"));
                                tempMovie.setYear(((JSONObject) timeline.get(i)).getString("release_date"));
                                tempMovie.setImdbId(((JSONObject) timeline.get(i)).getString("imdb_id"));
                                JSONObject jObject = (JSONObject) timeline.get(i);
                                JSONObject iOSObject = jObject.getJSONObject("poster");
                                tempMovie.setImageUrl(iOSObject.getString("thumb"));
                                moviesToBeShown.add(tempMovie);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        movieAdapter.changeListItem(moviesToBeShown);
                    }
                });
            }
        });

    }

    private void showMessage(String title, final MovieModel movie) {
        AlertDialog.Builder builder = new AlertDialog.Builder(LogedUserActivity.this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setNeutralButton("Add to Wishlist", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Boolean canInsert = true;
                List<String> checkDuplicates = new ArrayList<String>();

                Cursor res = dh.getAllWishlistData();
                while (res.moveToNext()) {
                    checkDuplicates.add(res.getString(1));
                }

                for (String s : checkDuplicates) {
                    if (s.equals(movie.getName())) {
                        canInsert = false;
                        showInstructions("Title", "The movie with name " + movie.getName() + " exists!");
                        break;
                    }
                }

                if (canInsert) {
                    dh.addMovieToWishlist(movie.getName(), movie.getImdbId());
                }

                res = dh.getAllWishlistData();
                StringBuffer sb = new StringBuffer();
                while (res.moveToNext()) {
                    sb.append("Name " + res.getString(1) + "\n");
                    checkDuplicates.add(res.getString(1));
                }

                Toast.makeText(LogedUserActivity.this, sb, Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.setMessage(movie.getName());
        builder.show();
    }

    private void showInstructions(String title, final String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(LogedUserActivity.this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.setMessage(message);
        builder.show();
    }

    public class MovieAdapter extends BaseAdapter {
        List<MovieModel> moviesList;
        ImageView imageView;

        public MovieAdapter() {
            this.moviesList = new ArrayList<MovieModel>();
        }

        public void addItem(MovieModel item) {
            this.moviesList.add(item);
            notifyDataSetChanged();
        }

        public void changeListItem(ArrayList<MovieModel> itemList) {
            this.moviesList = new ArrayList<MovieModel>();
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
                LayoutInflater inflater = (LayoutInflater) LogedUserActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.list_of_movies, (ViewGroup) convertView, false);
            }

            TextView movieName = (TextView) convertView.findViewById(R.id.movieName);
            TextView movieDirector = (TextView) convertView.findViewById(R.id.movieDirector);
            TextView movieYear = (TextView) convertView.findViewById(R.id.movieYear);

            MovieModel movieModel = moviesList.get(position);

            movieName.setText(movieModel.getName());
            movieDirector.setText(movieModel.getDirector());
            movieYear.setText(movieModel.getYear());

            imageView = (ImageView) convertView.findViewById(R.id.imageView1);

            if (movieModel.getImageUrl().equals("")) {
                imageView.setImageResource(R.drawable.error);
            } else {
                Picasso.with(LogedUserActivity.this)
                        .load(movieModel.getImageUrl())
                        .error(R.drawable.error)
                        .into(imageView);
            }

            return convertView;
        }
    }
}
