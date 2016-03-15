package cn.suiseiseki.www.suiseiseeker.tools;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.suiseiseki.www.suiseiseeker.model.Category;
import cn.suiseiseki.www.suiseiseeker.model.Post;

/**
 * Created by Suiseiseki/shuikeyi on 2016/3/15.
 */
public class MyJSONParser {
    private static final String TAG = "MyJSONParser";
    /**
     * Parse JSON data in the webSide and return a List of category
     */
    public static ArrayList<Category> loadCategories(JSONObject jsonTotalObject)
    {
        ArrayList<Category> categoryArrayList = new ArrayList<>();
        try
        {
            JSONArray categories = jsonTotalObject.getJSONArray("categories");
            for(int i = 0;i<categories.length();i++)
            {
                JSONObject jsonObject = categories.getJSONObject(i);
                Category category = new Category();
                category.setName(jsonObject.getString("title"));
                category.setId(jsonObject.getInt("id"));
                categoryArrayList.add(category);
            }
            return categoryArrayList;
        } catch (JSONException e)
        {
            Log.e(TAG,"JSONException when loading categories",e);
            e.printStackTrace();
            return null;
        }
    }
    /**
     * Parse JSON data of Post
     */
    public ArrayList<Post> loadPosts(JSONObject jsonTotalObject)
    {
        ArrayList<Post> posts = new ArrayList<>();
        try
        {
            JSONArray postArray = jsonTotalObject.getJSONArray("posts");
            for(int i = 0 ;i<postArray.length();i++)
            {
                JSONObject object = postArray.getJSONObject(i);
                Post post = new Post();
                // Notice,use "opt" instead of "get" to avoid null value exception
                post.setTitle(object.optString("title"));
                post.setId(object.optInt("id"));
                post.setThumbnailUrl(Settings.DEFAULT_THUMBNAIL_URL);
                post.setUrl(object.optString("url"));
                // Set to zero if there's no comment.
                post.setCommentCount(object.optInt("comment_count", 0));
                post.setDate(object.optString("date", "N/A"));
                post.setContent(object.optString("content","N/A"));
                // Care that author is not a String,it is a JSONObject
                JSONObject author = object.getJSONObject("author");
                post.setAuthor(author.optString("name", "N/A"));
                // if the post has it's thumbnail image,use it,or just keep default
                JSONObject featuredImages = object.optJSONObject("thumbnail_images");
                if(featuredImages != null)
                {
                    post.setFeaturedImageUrl(featuredImages.optJSONObject("full")
                            .optString("url", Settings.DEFAULT_THUMBNAIL_URL));
                }
               posts.add(post);
            }
            return  posts;
        }
        catch (JSONException e)
        {
            Log.e(TAG,"JSONException when loading Posts",e);
            e.printStackTrace();
            return null;
        }
    }


}
