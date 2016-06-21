package com.andela.webservice;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.andela.webservice.model.Flower;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.okhttp.OkHttpClient;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by Spykins on 21/06/16.
 */
public class FlowerAdapter extends ArrayAdapter<Flower> {
    private Context context;
    private List<Flower> flowerList;
    private OkHttpClient client = new OkHttpClient();
    private RequestQueue queue;

    private LruCache<Integer, Bitmap> imageCache;

    public FlowerAdapter(Context context, int resource, List<Flower> objects) {
        super(context, resource, objects);
        this.context = context;
        this.flowerList = objects;

        final int maxMemory = (int)(Runtime.getRuntime().maxMemory() /1024);
        final int cacheSize = maxMemory / 8;
        imageCache = new LruCache<>(cacheSize);
        queue = Volley.newRequestQueue(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_flower, parent, false);

        //Display flower name in the TextView widget
        final Flower flower = flowerList.get(position);
        TextView tv = (TextView) view.findViewById(R.id.textView1);
        tv.setText(flower.getName());

        //Display flower photo in ImageView widget
        Bitmap bitmap = imageCache.get(flower.getProductId());
        final ImageView image = (ImageView) view.findViewById(R.id.imageView1);
        if (bitmap != null) {
            image.setImageBitmap(bitmap);
        }
        else {
            /*FlowerAndView container = new FlowerAndView();
            container.flower = flower;
            container.view = view;

            ImageLoader loader = new ImageLoader();
            loader.execute(container);*/
            String imageUrl = MainActivity.PHOTOS_BASE_URL + flower.getPhoto();
            //getting the image url, now we are ready for volley code
            //First we need to create a request queue, the request queue will be a field
            //of the current class.  so that it persisit for the life time of the class
            //these will allow us add multiple request to the queue and volley will manage the queue
            ImageRequest request = new ImageRequest(imageUrl,
                    new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap bitmap) {
                            image.setImageBitmap(bitmap);
                            imageCache.put(flower.getProductId(), bitmap);
                            //displaying the image n saving it in the cache ,
                            //in order not to download it again
                        }
                    },
                    80,
                    80,
                    Bitmap.Config.ARGB_8888,
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Log.d("FlowerAdapter", "wale check this first " +volleyError.getMessage());
                        }
                    }

            );
            /*
                    The second n third argument, maxWidth and maxHeight allows you to set the dimension of the image
                     we are receiving and it will transform the Image during the download process
                     This will save memory and storage if we're working with i,mages that starts up larger
                      but don't need to be that large when we display them..
                      The four argument is a BitMap config object.. There are some constants we can use

             */
            queue.add(request);
        }


        return view;
    }

/*    class FlowerAndView {
        public Flower flower;
        public View view;
        public Bitmap bitmap;
    }*/

    /*private class ImageLoader extends AsyncTask<FlowerAndView, Void, FlowerAndView> {

        @Override
        protected FlowerAndView doInBackground(FlowerAndView... params) {

            FlowerAndView container = params[0];
            Flower flower = container.flower;

            try {
                String imageUrl = MainActivity.PHOTOS_BASE_URL + flower.getPhoto();
                HttpURLConnection con = client.open(new URL(imageUrl));

                //InputStream in = (InputStream) new URL(imageUrl).getContent();
                InputStream in = con.getInputStream();

                Bitmap bitmap = BitmapFactory.decodeStream(in);
                flower.setBitmap(bitmap);
                in.close();
                container.bitmap = bitmap;
                return container;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(FlowerAndView result) {
            ImageView image = (ImageView) result.view.findViewById(R.id.imageView1);
            image.setImageBitmap(result.bitmap);
//			result.flower.setBitmap(result.bitmap);
            imageCache.put(result.flower.getProductId(), result.bitmap);
        }

    }*/
}
