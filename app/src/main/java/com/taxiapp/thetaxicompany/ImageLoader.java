package com.taxiapp.thetaxicompany;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

/**
 * A utility that loads images on a separate thread to provide efficiency.
 * Created by merve on 22.03.2016.
 */
public class ImageLoader extends AsyncTask<Integer, Void, Bitmap> {

    private WeakReference<ImageView> weakImgReference;
    private Integer id;
    private Resources resources;

    public ImageLoader(ImageView imgView, Resources r){
        weakImgReference = new WeakReference<ImageView>(imgView);
        this.resources = r;
    }

    @Override
    protected Bitmap doInBackground(Integer... params) {
        id = params[0];
        int size = params[1];
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = size;
        Bitmap bitmap = BitmapFactory.decodeResource(resources, id, options);
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap){
        if(weakImgReference != null & bitmap != null) {
            final ImageView imgView = weakImgReference.get();
            if (imgView != null) {
                Drawable drawable=new BitmapDrawable(bitmap);

                imgView.setBackground(drawable);
           }
        }
    }

}
