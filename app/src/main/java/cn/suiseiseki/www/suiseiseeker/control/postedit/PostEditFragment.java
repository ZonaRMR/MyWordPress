package cn.suiseiseki.www.suiseiseeker.control.postedit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.net.URLEncoder;
import java.util.ArrayList;

import cn.suiseiseki.www.suiseiseeker.R;
import cn.suiseiseki.www.suiseiseeker.control.CoordinatorFragment;
import cn.suiseiseki.www.suiseiseeker.control.CoreControl;
import cn.suiseiseki.www.suiseiseeker.tools.FontHelper;
import cn.suiseiseki.www.suiseiseeker.model.Category;
import cn.suiseiseki.www.suiseiseeker.tools.Settings;

/**
 * Created by Suiseiseki/shuikeyi on 2016/3/25.
 */
public class PostEditFragment extends Fragment {

    Toolbar mToolbar;
    Spinner spinner;
    private Button mCommitButton;
    public static final  String TAG ="PostEditFragment";
    public static final String ALL_CLEAR = "AllClear_haha";
    MaterialEditText mTitle,mAuthor,mContent;
    MyBroadCastReceiver allclear_BR;

    private Callback mCallback;

    interface Callback
    {
        void onAllClearClicked();
    }

    /**
       Attach to an Activity to Callback
     */
    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if(context != null)
        mCallback = (Callback)context;
    }


    @Override
    public void onCreate(Bundle savedInstaceState)
    {
        super.onCreate(savedInstaceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        getNonce();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu,inflater);
        inflater.inflate(R.menu.post_edit_menu,menu);
    }

    @Override
     public boolean onOptionsItemSelected(MenuItem item)
    {
        switch ((item.getItemId()))
        {
            case android.R.id.home:
            {
                if(NavUtils.getParentActivityName(getActivity())!=null)
                {
                    NavUtils.navigateUpFromSameTask(getActivity());
                }
                return true;
            }
            case R.id.post_edit_allclear:
            {
                mCallback.onAllClearClicked();
            }
            default:return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup root,Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.post_edit_fragment,root,false);
        mToolbar = (Toolbar)v.findViewById(R.id.post_edit_toolbar);
        mToolbar.setTitle(getString(R.string.add_post));
//        mToolbar.setSubtitle(getString((R.string.app_name)));
        mToolbar.setSubtitleTextColor(getResources().getColor(R.color.seagreen));
        ((PostEditActivity)getActivity()).setSupportActionBar(mToolbar);
        ((PostEditActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        spinner = (Spinner)v.findViewById(R.id.spinner);
        ArrayList<String> categorylist = new ArrayList<>();
        if(CoordinatorFragment.mCategories != null) {
            for (Category mCategory : CoordinatorFragment.mCategories) {
                categorylist.add(mCategory.getName());
                categorylist.remove("Recent");
            }
        }
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,categorylist);
        spinner.setAdapter(categoryAdapter);

        //Find the Edit Text
        mTitle = (MaterialEditText)v.findViewById(R.id.edittext_title);
        mAuthor = (MaterialEditText) v.findViewById(R.id.edittext_author);
        mContent =(MaterialEditText) v.findViewById(R.id.post_edit_content);

        mCommitButton =(Button) v.findViewById(R.id.commit_button);
        mCommitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /****************** Create the Post ********/
                StringBuilder mStringBuilder = new StringBuilder();
                mStringBuilder.append(Settings.CREATE_POST_URL);
                mStringBuilder.append("?nonce=");
                mStringBuilder.append(Settings.Nonce);
                mStringBuilder.append("&title=");
                String title = mTitle.getText().toString();
                mStringBuilder.append(URLEncoder.encode(title));
                mStringBuilder.append("&author=");
                String author = mAuthor.getText().toString();
                mStringBuilder.append(URLEncoder.encode(author));
                String content = mContent.getText().toString();
                mStringBuilder.append("&content=").append(URLEncoder.encode(content));
                String mCategorySlugName = findCategorySlugName((String)spinner.getSelectedItem(),CoordinatorFragment.mCategories);
                mStringBuilder.append("&categories=").append(mCategorySlugName);
                mStringBuilder.append("&status=").append("publish");
                Log.d(TAG,"request:"+mStringBuilder.toString());
                StringRequest create_post_request = new StringRequest(Request.Method.POST, mStringBuilder.toString(), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getActivity(),getString(R.string.success),Toast.LENGTH_SHORT).show();
                        if(NavUtils.getParentActivityName(getActivity())!=null)
                        {
                            NavUtils.navigateUpFromSameTask(getActivity());
                        }
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d(TAG, "Volley Error in posting String Request");
                            }
                        });
                CoreControl.getInstance().addToRequestQueue(create_post_request);

            }
        });
        // Register a BroadCastReceiver
        IntentFilter mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(TAG);
        allclear_BR = new MyBroadCastReceiver();
        getActivity().registerReceiver(allclear_BR,mIntentFilter);
        FontHelper.applyFont(getActivity(),v,"fonts/myfont.ttf");
        return v;
    }

    @Override
    public void onDestroy()
    {
        Log.d(TAG,"on Destroy called");
        getActivity().unregisterReceiver(allclear_BR);
        super.onDestroy();
    }

    /**
     * All clear the Input
     */
    public void AllClear()
    {
       mTitle.setText("");
        mContent.setText("");
        mAuthor.setText("");
    }

    class MyBroadCastReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context,Intent intent)
        {
            if(intent.getIntExtra(ALL_CLEAR,0) == 1)
            AllClear();
        }
    }

    /**
     * A method to find the "Slug" name for categories
     * @param name:The name of categories
     * @param list: Categories list received from server
     * @return The slug name of category
     */

    private String findCategorySlugName(String name,ArrayList<Category> list)
    {
        for(Category a:list)
        {
            if(a.getName().equals(name))
            {
                return a.getSlugName();
            }
        }
        return "recent";
    }

    /**
     * Need a Nonce to create a post
     */
    private void getNonce()
    {
        CoreControl.getNonce(1);
    }


}
