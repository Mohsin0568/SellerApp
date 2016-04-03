package com.example.mohmurtu.registration.imagesUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import java.net.URL;

/**
 * Created by Gil on 07/06/2014.
 */
public class ImageInternalFetcher extends ImageResizer {

    Context mContext;

    public ImageInternalFetcher(Context context, int imageWidth, int imageHeight) {
        super(context, imageWidth, imageHeight);
        init(context);
    }

    public ImageInternalFetcher(Context context, int imageSize) {
        super(context, imageSize);
        init(context);
    }

    private void init(Context context){
        mContext = context;
    }



    protected Bitmap processBitmap(Uri uri, int source){
        if(source == 0) // from file
        return decodeSampledBitmapFromFile(uri.getPath(), mImageWidth, mImageHeight, getImageCache());
        else
            return  decodeSampledBitmapFromUri(mContext, uri.toString(), mImageWidth, mImageHeight, getImageCache());
    } // Modified by Mohsin

    @Override
    protected Bitmap processBitmap(Object data, int source) {
        if(source == 0)
        return processBitmap((Uri)data, source);
        else
            return processBitmap(data.toString());
    } // Modified by Mohsin

    protected  Bitmap processBitmap(String url){
        return  decodeSampledBitmapFromUri(mContext, url, mImageWidth, mImageHeight, getImageCache());
    }
}
