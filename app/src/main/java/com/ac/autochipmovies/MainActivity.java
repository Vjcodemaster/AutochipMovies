package com.ac.autochipmovies;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

import app_utility.DataBaseHelper;
import app_utility.DatabaseHandler;
import app_utility.MovieData;
import app_utility.MoviesAsyncTask;
import app_utility.OnAsyncTaskInterfaceListener;
import app_utility.SharedPreferencesClass;

public class MainActivity extends AppCompatActivity implements OnAsyncTaskInterfaceListener {

    //Movie name
    //Language
    //Video link
    //Description
    //Photo
    public static OnAsyncTaskInterfaceListener onAsyncTaskInterfaceListener;
    AutoCompleteTextView actvSearch;
    ArrayList<MovieData> alMovieData;
    HashMap<String, Bitmap> hsMovieImage = new HashMap<>();
    ArrayList<Bitmap> alMovieImage = new ArrayList<>();
    ArrayList<String> alMovieName = new ArrayList<>();
    FilterAdapter filterAdapter;
    WebView wvVideoPlayer;
    ImageView ivSettings;
    DatabaseHandler dbHandler;
    SharedPreferencesClass sharedPreferencesClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        onAsyncTaskInterfaceListener = this;
        sharedPreferencesClass = new SharedPreferencesClass(MainActivity.this);
        dbHandler = new DatabaseHandler(MainActivity.this);
        initViews();
        if(!sharedPreferencesClass.getUserLogStatus())
        getTypesOfAccountsAvailable();
        MoviesAsyncTask moviesAsyncTask = new MoviesAsyncTask(MainActivity.this);
        moviesAsyncTask.execute("1");

        /*runOnUiThread(new Runnable(){
            @Override
            public void run(){
                // change UI elements here
            }
        });*/
    }

    @SuppressLint("SetJavaScriptEnabled")
    void initViews() {
        actvSearch = findViewById(R.id.actv_search);
        actvSearch.setThreshold(1);

        wvVideoPlayer = findViewById(R.id.wv_video_player);
        wvVideoPlayer.getSettings().setJavaScriptEnabled(true);
        wvVideoPlayer.getSettings().setUseWideViewPort(false);

        ivSettings = findViewById(R.id.iv_settings);
        ivSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboardFrom(actvSearch);
                Intent in = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(in);
            }
        });
        //wvVideoPlayer.getSettings().setMediaPlaybackRequiresUserGesture(true);

        actvSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String sMovieName = parent.getItemAtPosition(position).toString().trim();
                String sVideoLink = findVideoLink(sMovieName);
                if (sVideoLink != null) {
                    wvVideoPlayer.loadUrl(sVideoLink);
                    //http://www.youtube.com/embed/bIPcobKMB94
                    //String playVideo = "<html><body>Youtube video .. <br> <iframe class=\"youtube-player\" type=\"text/html\" width=\"640\" height=\"385\" src=\"https://www.youtube.com/embed/hKsD08idGug\" frameborder=\"0\"></body></html>";
                    //wvVideoPlayer.loadData(playVideo, "text/html", "utf-8");
                }
                //wvVideoPlayer.loadUrl(sVideoLink);
                else
                    Toast.makeText(MainActivity.this, "Link missing", Toast.LENGTH_SHORT).show();
            }
        });

       /* actvSearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String sVideoLink = findVideoLink(parent.getItemAtPosition(position).toString().trim());
                if(sVideoLink!=null)
                wvVideoPlayer.loadUrl(sVideoLink);
                else
                    Toast.makeText(MainActivity.this, "Link missing", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
        /*Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);*/
    }

    private void getTypesOfAccountsAvailable(){
        //DataBaseHelper dbHelper = new DataBaseHelper();
        ArrayList<String> alAccountNames = new ArrayList<>();
        alAccountNames.add("Amazon Prime");
        alAccountNames.add("HotStar");
        alAccountNames.add("NetFlix");
        for (int i=0; i<alAccountNames.size(); i++){
            dbHandler.addDataToAccountsTable(new DataBaseHelper(i+1,alAccountNames.get(i), ""));
        }
        if(alAccountNames.size()>=1)
            sharedPreferencesClass.setUserLogStatus(true);
    }

    public String findVideoLink(String item) {
        String sLink = null;
        for (int i = 0; i < alMovieData.size(); i++) {
            String sMovieName = alMovieData.get(i).getMovieName();
            if (sMovieName.equalsIgnoreCase(item)) {
                sLink = alMovieData.get(i).getVideoLink();
                return sLink;
            }
        }
        return sLink;
    }

    @Override
    public void onResult(String sCase, ArrayList<MovieData> alMovieData) {
        byte[] decodedString;
        Bitmap decodedByte;
        switch (sCase) {
            case "MOVIE":
                this.alMovieData = alMovieData;
                for (int i = 0; i < alMovieData.size(); i++) {
                    String sMovieName = alMovieData.get(i).getMovieName();
                    alMovieName.add(sMovieName);
                    String encodedBitmap = alMovieData.get(i).getPhoto();
                    decodedString = Base64.decode(encodedBitmap, Base64.DEFAULT);
                    decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    hsMovieImage.put(sMovieName, decodedByte);
                }
                filterAdapter = new FilterAdapter(MainActivity.this, android.R.layout.simple_dropdown_item_1line, alMovieName);
                /*ArrayAdapter arrayAdapter = new ArrayAdapter<>(this,
                        android.R.layout.simple_dropdown_item_1line, alMovieName);*/

                actvSearch.setAdapter(filterAdapter);
                break;
        }
    }

    public class FilterAdapter extends ArrayAdapter<String> implements Filterable {

        ArrayList<String> originalList;
        ArrayList<String> filteredList;

        public FilterAdapter(Context context, int textViewResourceId, ArrayList<String> item) {
            super(context, textViewResourceId, item);
            filteredList = item;
            originalList = new ArrayList<>(filteredList);
        }

        @Override
        public int getCount() {
            return filteredList.size();
        }

        @Override
        public String getItem(int position) {
            return filteredList.get(position);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            //super.getView(position, convertView, parent);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.actv_custom, parent, false);
            } else {
                TextView tvMovieName = convertView.findViewById(R.id.tv_movie_name);
                tvMovieName.setText(filteredList.get(position));

                final ImageView ivMovieImage = convertView.findViewById(R.id.iv_movie_image);
                /*String encodedBitmap = alMovieData.get(position).getPhoto();

                byte[] decodedString = Base64.decode(encodedBitmap, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);*/
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ivMovieImage.setImageBitmap(hsMovieImage.get(filteredList.get(position)));
                        // change UI elements here
                    }
                });

            }
            /*TextView tv;

            if(convertView!= null)
                tv = (TextView)convertView;
            else
                tv = new TextView(MainActivity.this);

            //changing text size and adding icons to sightseer and destination heading
            if(position == 0)
            {
                tv.setText(filteredList.get(position));
                tv.setTextSize(autoCompleteTextView.getTextSize() - 1);
                Drawable drawable = ContextCompat.getDrawable(getApplicationContext(),R.drawable.cognito);
                tv.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, drawable, null);
                tv.setTextColor(Color.parseColor("#999999"));
            }
            else if(filteredList.get(position).contains("Destination"))
            {
                tv.setText(filteredList.get(position));
                tv.setTextSize(autoCompleteTextView.getTextSize() - 1);
                Drawable drawable = ContextCompat.getDrawable(getApplicationContext(),R.drawable.favicon);
                tv.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, drawable, null);
                tv.setTextColor(Color.parseColor("#999999"));
            }
            else{
                tv.setText(filteredList.get(position));
                tv.setTextSize(autoCompleteTextView.getTextSize() - 5);
            }*/
            return convertView;
        }

        @Override
        public Filter getFilter() {
            return nameFilter;
        }

        Filter nameFilter = new Filter() {
            @Override
            public CharSequence convertResultToString(Object resultValue) {
                return (String) resultValue;
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                if (constraint != null) {
                    filteredList.clear();
                    for (String item : originalList) {
                        if (item.toLowerCase().contains(constraint.toString().toLowerCase())) {
                            filteredList.add(item);
                        }
                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = filteredList;
                    filterResults.count = filteredList.size();
                    return filterResults;
                } else {
                    //filteredList = originalList;
                    return new FilterResults();
                }
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    filterAdapter = new FilterAdapter(MainActivity.this, android.R.layout.simple_dropdown_item_1line, originalList);
                    actvSearch.setAdapter(filterAdapter);
                    actvSearch.showDropDown();
                }
            }
        };
    }

    public void hideKeyboardFrom(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
