package cn.suiseiseki.www.suiseiseeker.tools.MyImageLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Looper;
import android.support.v4.util.LruCache;
import android.webkit.URLUtil;

import com.jakewharton.disklrucache.DiskLruCache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Suiseiseki/shuikeyi on 2016/5/31.
 */
public class MyImageLoader {
    private static final String TAG = "MyImageLoader";

    private static final long DISK_CACHE_SIZE = 1024 * 1024 *48;
    private static final int DISK_CACHE_INDEX = 0;
    private static final int IO_BUFFER_SIZE = 1024 * 8;

    /**
     *  The memory Cache and disk Cache
     */
    private LruCache<String,Bitmap> mMemoryCache;
    private DiskLruCache mDiskLruCache;
    private boolean mDiskLruCacheExist;
    private Context mContext;
    private ImageResizer mImageResizer = new ImageResizer();

    /**
     *  Method - Transfer url to key
     */
    private String hashKeyFromUrl(String url)
    {
        String key;
        try
        {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(url.getBytes());
            key = bytesToHexString(mDigest.digest());
        }
        catch (NoSuchAlgorithmException ex)
        {
            key = String.valueOf(url.hashCode());
        }
        return  key;
    }
    private String bytesToHexString(byte[] bytes)
    {
        StringBuilder builder = new StringBuilder();
        for(int i = 0;i<bytes.length;i++)
        {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if(hex.length() == 1)
                builder.append('0');
            builder.append(hex);
        }
        return builder.toString();
    }

    /**
     * Create the Cache
     * * @param context
     */
    private MyImageLoader(Context context)
    {
         mContext = context.getApplicationContext();
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        int cacheSize = maxMemory / 8;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize)
        {
            @Override
            protected int sizeOf(String key,Bitmap bitmap)
            {
                return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
            }
        };
        File DiskLruCachePath =getDiskCacheDir(context,"bitmapcache");
        if(!DiskLruCachePath.exists())
            DiskLruCachePath.mkdirs();
        if(getUsableSpace(DiskLruCachePath) > DISK_CACHE_SIZE)
            try
            {
                mDiskLruCache = DiskLruCache.open(DiskLruCachePath,1,1,DISK_CACHE_SIZE);
                mDiskLruCacheExist = true;
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
    }
    public File getDiskCacheDir(Context context,String uniqueName)
    {
        boolean externalStorageAvailable = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        final String cachePath;
        if(externalStorageAvailable)
            cachePath = context.getExternalCacheDir().getPath();
        else
            cachePath = context.getCacheDir().getPath();
        return new File(cachePath + File.separator + uniqueName);
    }

    private long getUsableSpace(File path)
    {
        return path.getUsableSpace();
    }

    // get and put from memory cache
    private void addBitmapToMemoryCache(String key,Bitmap bitmap)
    {
        if( mMemoryCache.get(key) == null)
            mMemoryCache.put(key,bitmap);
    }
    private Bitmap getBitmapFromMemoryChache(String key)
    {
        return mMemoryCache.get(key);
    }

    /**
     *  loadBitmap from Http connect
     * @param url
     * @param reqWidth
     * @param reqHeight
     * @return
     * @throws IOException
     */
    private Bitmap loadBitmapFromHttp(String url,int reqWidth,int reqHeight) throws IOException
    {
        if(Looper.myLooper() == Looper.getMainLooper())
            throw new RuntimeException("Please stop visiting Network in UI Thread");
        if(mDiskLruCache == null)
        {
            return null;
        }
        String key = hashKeyFromUrl(url);
        DiskLruCache.Editor editor = mDiskLruCache.edit(key);
        if(editor!=null)
        {
            OutputStream outputStream = editor.newOutputStream(DISK_CACHE_INDEX);
        }
        return null;
    }

    public boolean downloadUrlToStream(String urlString,OutputStream outputStream)
    {
        HttpURLConnection httpURLConnection = null;
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        try
        {
            URL url  = new URL(urlString);
            httpURLConnection = (HttpURLConnection)url.openConnection();
            in = new BufferedInputStream(httpURLConnection.getInputStream(),IO_BUFFER_SIZE);
            out = new BufferedOutputStream(httpURLConnection.getOutputStream(),IO_BUFFER_SIZE);
            int b;
            while((b = in.read())!=-1 )
            {
                out.write(b);
            }
            return  true;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        finally {
            if(httpURLConnection != null)
                httpURLConnection.disconnect();
        }
        return false;
    }


}
