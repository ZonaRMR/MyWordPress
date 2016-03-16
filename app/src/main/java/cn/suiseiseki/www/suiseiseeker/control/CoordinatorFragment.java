package cn.suiseiseki.www.suiseiseeker.control;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.ArrayList;

import cn.suiseiseki.www.suiseiseeker.R;
import cn.suiseiseki.www.suiseiseeker.model.Category;
import cn.suiseiseki.www.suiseiseeker.tools.MyJSONParser;
import cn.suiseiseki.www.suiseiseeker.tools.Settings;

/**
 * Created by Suiseiseki/shuikeyi on 2016/3/16.
 */
public class CoordinatorFragment extends Fragment {
    private final static String TAG = "CoordinatorFragment";
    private ProgressDialog mProgressDialog;

    /**
     * The Work of onCreate() and onDestroy(),do some preparation work
     * Retain Instance and has optionsMenu
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }
    /**
     * When the activity onCreate() is applied
     * Load Categories
     */
    public ArrayList<Category> mCategories;
    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
    }

    private void loadCategories()
    {
        // Use a ProgressDialog
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage(getString(R.string.seek_categories));
        // Please hold your patience
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
        // Parse JSON for Categories
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                mProgressDialog.dismiss();
                mCategories = MyJSONParser.loadCategories(response);
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG,"Error: Requesting JSON for Categories" );
                mProgressDialog.dismiss();
                //may use Snackbar instead of Toast

            }
        };
        JsonObjectRequest jsonCategoryRequest = new JsonObjectRequest(Settings.MAIN_URL,listener,errorListener);
        //Send Request to CoreControl to Handle
        CoreControl.getInstance().addToRequestQueue(jsonCategoryRequest);

    }



}
