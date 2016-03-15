package cn.suiseiseki.www.suiseiseeker.model;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by Suiseiseki/shuikeyi on 2016/3/15.
 */
public class Post {

    private String mTitle;
    private String mContent;
    private String mThumbnailUrl; // The url of thumbnail in Post
    private String mFeaturedImageUrl = "" ;// Some webside may has another image Url form
    private String mViewCount;
    private String mDate; // The Date of Post(into String)
    private String mAuthor;
    private String mUrl;
    private int mId; // The Id of Post
    private int mCommentCount;
    private ArrayList<String> mCategories; // One Post may belong to several Categories.
    /**
     * Override equals to identify two Post,And we must secure same Post has same hashCode
     */
    @Override
    public boolean equals(Object post)
    {
        if(post instanceof Post && this.mId == ( (Post) post).getId() )
        {
                return  true;
        }
        else
            return  false;
    }
    @Override
    public int hashCode()
    {
        return Integer.valueOf(this.mId).hashCode();
    }

    /**
     *
     * Wow a lot of getter and setter
     */
    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String author) {
        mAuthor = author;
    }

    public ArrayList<String> getCategories() {
        return mCategories;
    }

    public void setCategories(ArrayList<String> categories) {
        mCategories = categories;
    }

    public int getCommentCount() {
        return mCommentCount;
    }

    public void setCommentCount(int commentCount) {
        mCommentCount = commentCount;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getFeaturedImageUrl() {
        return mFeaturedImageUrl;
    }

    public void setFeaturedImageUrl(String featuredImageUrl) {
        mFeaturedImageUrl = featuredImageUrl;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getThumbnailUrl() {
        return mThumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        mThumbnailUrl = thumbnailUrl;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getViewCount() {
        return mViewCount;
    }

    public void setViewCount(String viewCount) {
        mViewCount = viewCount;
    }

}
