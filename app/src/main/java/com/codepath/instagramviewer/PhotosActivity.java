package com.codepath.instagramviewer;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class PhotosActivity extends ActionBarActivity {
    public static final String CLIENT_ID = "30dc8972e6ac41a2b90272e8c73da126";
    private ArrayList<InstagramPhoto> photos;
    private InstagramPhotosAdapter aPhotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        fetchPopularPhotos();
    }

    private void fetchPopularPhotos() {
        photos = new ArrayList<InstagramPhoto>(); //initialize arraylist
        //Create adapter bind it to the data in arraylist
        aPhotos = new InstagramPhotosAdapter(this, photos);
        //Populate the data into the listview
        ListView lvPhotos = (ListView) findViewById(R.id.lvPhotos);
        //set the adapter tp the listview (population of items
        lvPhotos.setAdapter(aPhotos);

        //https://api.instagram.com/v1/media/popular?client_id=30dc8972e6ac41a2b90272e8c73da126
        // { "data" => [x] => "images" => "standard_resolution" => "url" }
        //Set up popular url endpoint
        String popularUrl = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;
        //Create the network client
        AsyncHttpClient client = new AsyncHttpClient();
        //Trigger the network request
         client.get(popularUrl, new JsonHttpResponseHandler() {
             //define success and failure callbacks

             @Override
             public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                 //fired once the successful response back
                 //response is == popular photos json
                 // { "data" => [x] => "images" => "standard_resolution" => "url" }
                 //Log.i("INFO", response.toString());
                 // { "data" => [x] => "user" => "username" }
                 // { "data" => [x] => "caption" => "text"}
                 // { "data" => [x] => "images" => "standard_resolution" => "url" }
                 // { "data" => [x] => "images" => "standard_resolution" => "height" }
                 JSONArray photosJSON = null;
                 try {
                     aPhotos.clear();
                     photosJSON = response.getJSONArray("data");
                     for (int i = 0; i < photosJSON.length(); i++) {
                         JSONObject photoJSON = photosJSON.getJSONObject(i);
                         InstagramPhoto photo = new InstagramPhoto();
                         photo.username = photoJSON.getJSONObject("user").getString("username");
                         if (photoJSON.getJSONObject("caption") != null) {
                             photo.caption = photoJSON.getJSONObject("caption").getString("text");
                         }
                         photo.imageUrl = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                         photo.imageHeight = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
                         photo.likes_count = photoJSON.getJSONObject("likes").getInt("count");

                         photo.profile_picture_url = photoJSON.getJSONObject("user").getString("profile_picture");
                         photo.comments_count = photoJSON.getJSONObject("comments").getInt("count");
                         JSONArray jsonArray = photoJSON.getJSONObject("comments").getJSONArray("data");
                         photo.lastcomment = jsonArray.getJSONObject(jsonArray.length()-1).getString("text");
                         photo.lastcomment_user = jsonArray.getJSONObject(jsonArray.length()-1).getJSONObject("from").getString("username");

                         photos.add(photo);
                     }
                     //notify the adapter that it should populate the new changes into the listview
                     aPhotos.notifyDataSetChanged();
                 } catch (JSONException e) {
                     //Fire if things fail, json parsing is invalid
                     e.printStackTrace();
                 }
             }

             @Override
             public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                 super.onFailure(statusCode, headers, responseString, throwable);
             }
         });
        //Handle the successful response(popular photos JSON)

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
