package cn.suiseiseki.www.suiseiseeker.tools.MyImageLoader;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.FileDescriptor;

/**
 * Created by Suiseiseki/shuikeyi on 2016/5/31.
 */
public class ImageResizer {

    private static final String tag = "ImageResizer";

    public ImageResizer(){};


    // load Bitmap From resource
    public Bitmap decodeSampledBitmapFromResource(Resources res,int resId,int width,int height)
    {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        // just read, don't edit
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res,resId,options);
        options.inSampleSize = calculateInSampleSize(options,width,height);

        // decode bitmap
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res,resId,options);
    }

    // load Bitmap from Files
    public Bitmap decodeSampledBitmapFromFileDescriptor(FileDescriptor fileDescriptor,int reqWidth,int reqHeight)
    {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fileDescriptor,null,options);
        options.inSampleSize = calculateInSampleSize(options,reqWidth,reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFileDescriptor(fileDescriptor,null,options);
    }

    public int calculateInSampleSize(BitmapFactory.Options options,int reqWidth,int reqHeight)
    {
        // read the source height and width
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;

        while(height > reqHeight && width > reqWidth)
        {
            inSampleSize *= 2;
            height *= 0.5;
            width *= 0.5;
        }
        return  inSampleSize;

    }
}
