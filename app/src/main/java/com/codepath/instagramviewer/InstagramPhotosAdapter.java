package com.codepath.instagramviewer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by lzhan on 1/21/15.
 */
public class InstagramPhotosAdapter extends ArrayAdapter<InstagramPhoto> {
    public InstagramPhotosAdapter(Context context, List<InstagramPhoto> photos) {
        super(context, R.layout.item_photo, photos);
    }

    //Takes a data item at a position, converts it to a row in the listview
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //take the data source at position(i.e. 0)
        //get the data item
        InstagramPhoto photo = getItem(position);
        //check if we are using a recycled view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
        }
        //lookup the subview within the template
        TextView tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
        ImageView imgPhoto = (ImageView) convertView.findViewById(R.id.imagePhoto);
        //Populate teh subviews(textfield, imageview) with the correct data
        tvCaption.setText(photo.caption);
        //set the image height before loading
        imgPhoto.getLayoutParams().height = photo.imageHeight;
        //reset the image from the recycled view
        imgPhoto.setImageResource(0);
        //ask for the photo to be added to the imageview based oon the url
        //Background: Send a network request to the url, download the image bytes, convert into bitmap, resize image,  insert bitmap to imageview
        Picasso.with(getContext()).load(photo.imageUrl).into(imgPhoto);
        //return the view for that data item
        return convertView;
    }
    //getView method (int position)
    //Default, takes the model (Instagram) toString()
}
