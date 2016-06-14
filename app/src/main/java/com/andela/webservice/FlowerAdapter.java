package com.andela.webservice;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.andela.webservice.model.Flower;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * Created by Spykins on 11/06/16.
 */
public class FlowerAdapter extends ArrayAdapter<Flower> {
    private Context context;
    @SuppressWarnings("unused")
    private List<Flower> flowerList;

    public FlowerAdapter(Context context, int resource, List<Flower> objects) {
        super(context, resource, objects);
        this.context = context;
        this.flowerList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_flower, parent, false);

        //Display flower name in the TextView widget
        Flower flower = flowerList.get(position);
        TextView tv = (TextView) view.findViewById(R.id.textView1);
        tv.setText(flower.getName());

        if(flower.getBitmap() != null) {
            ImageView img = (ImageView) view.findViewById(R.id.imageView1);
            img.setImageBitmap(flower.getBitmap());
        } else {
            FlowerAndView container = new FlowerAndView();
            container.flower = flower;
            container.view = view;

            ImageLoader loader = new ImageLoader();
            loader.execute(container);
        }


        return view;
    }

    /*
        To achitect this, we will be passing an instance of the Flower object, We need
        to pass an instance of the View... We also need to pass the BitMap

        But the Async task class can only handle one object at a time for input and result

        so we will create a new class that will act as a container for the object...
     */

    class FlowerAndView {
        public Flower flower;
        public View view;
        public Bitmap bitmap;
    }

    private class ImageLoader extends AsyncTask<FlowerAndView, Void, FlowerAndView> {

        @Override
        protected FlowerAndView doInBackground(FlowerAndView... params) {

            FlowerAndView container = params[0];
            Flower flower = container.flower;
            // now we are ready to get the image from the web
            try {
                String imageUrl = MainActivity.PHOTO_BASE_URL + flower.getPhoto();
                InputStream in = (InputStream) new URL(imageUrl).getContent();
                //This retrieves the entire content of the location in one request
                //It comes back as a blub of data n we can access it through the input stream
                Bitmap bitmap = BitmapFactory.decodeStream(in);
                flower.setBitmap(bitmap);
                in.close();
                container.bitmap = bitmap;
                return container;

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(FlowerAndView result) {
            ImageView img = (ImageView) result.view.findViewById(R.id.imageView1);
            img.setImageBitmap(result.bitmap);
            result.flower.setBitmap(result.bitmap);

        }
    }
}
