package cn.suiseiseki.www.suiseiseeker.tools;

/**
 * Created by Suiseiseki/shuikeyi on 2016/3/15.
 */
public class Settings {
    /**
     * You may set your own URL and other things here
     */
    public static final String MAIN_URL = "http://www.suiseiseki.cn/";
    public static final String CATEGORY_INDEX_URL = MAIN_URL + "?json=get_category_index";
    public static final String DEFAULT_THUMBNAIL_URL = "http://www.iconpng.com/png/freepikons-business/business96.png";
    public static final String NONCE_URL = MAIN_URL + "api/get_nonce/?controller=posts&method=create_post";
    /**
     * The Nonce, DO NOT EDIT OR EAT!
     */
    public static String Nonce = null;
}
