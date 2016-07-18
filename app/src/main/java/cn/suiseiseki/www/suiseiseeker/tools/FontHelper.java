package cn.suiseiseki.www.suiseiseeker.tools;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.reflect.Field;

/**
 * Created by Shuikeyi on 2016/7/5.
 * address:shuikeyi92@gmail.com
 */
public class FontHelper {

    private static final String TAG = FontHelper.class.getSimpleName();

    /**
     * Apply specified font for all text views (including nested ones) in the specified
     * root view.
     *
     * @param context
     *            Context to fetch font asset.
     * @param root
     *            Root view that should have specified font for all it's nested text
     *            views.
     * @param fontPath
     *            Font path related to the assets folder (e.g. "fonts/YourFontName.ttf").
     */
    public static void applyFont(final Context context, final View root, final String fontPath) {
        try {
            if (root instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) root;
                int childCount = viewGroup.getChildCount();
                for (int i = 0; i < childCount; i++)
                    applyFont(context, viewGroup.getChildAt(i), fontPath);
            } else if (root instanceof TextView)
                ((TextView) root).setTypeface(Typeface.createFromAsset(context.getAssets(), fontPath));
        } catch (Exception e) {
            Log.e(TAG, String.format("Error occured when trying to apply %s font for %s view", fontPath, root));
            e.printStackTrace();
        }
    }

    public static void setDefaultFont(Context context,
                                      String staticTypefaceFieldName, String fontAssetName) {
        final Typeface regular = Typeface.createFromAsset(context.getAssets(),
                fontAssetName);
        replaceFont(staticTypefaceFieldName, regular);
    }

    protected static void replaceFont(String staticTypefaceFieldName,
                                      final Typeface newTypeface) {
        try {
            final Field staticField = Typeface.class
                    .getDeclaredField(staticTypefaceFieldName);
            staticField.setAccessible(true);
            staticField.set(null, newTypeface);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


}
