package cn.suiseiseki.www.suiseiseeker.tools;

import org.json.JSONException;
import org.json.JSONObject;

import cn.suiseiseki.www.suiseiseeker.model.Post;

/**
 * Created by Suiseiseki/shuikeyi on 2016/3/25.
 */
public class MyJSONBuilder {

    public static JSONObject CreatePost() throws JSONException
    {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("controller","posts");
        jsonObject.put("method","create_post");
        return jsonObject;
    }
}
