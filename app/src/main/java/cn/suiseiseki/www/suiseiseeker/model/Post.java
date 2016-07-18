package cn.suiseiseki.www.suiseiseeker.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Suiseiseki/shuikeyi on 2016/3/15.
 */
public class Post extends BaseObservable implements Parcelable,Serializable{

    private static final long serialVersionUID = 6128300212L;

    private String mTitle;
    private String mContent;
    private String mThumbnailUrl; // The url of thumbnail in Post
    private String mFeaturedImageUrl = "" ;// Some webside may has another image Url form
    private String mViewCount;
    private String mDate; // The Date of Post(into String)
    private String mAuthor;
    private String mUrl;
    private String mExcerpt;
    private int mId; // The Id of Post
    private int mCommentCount;
    private ArrayList<String> mCategories; // One Post may belong to several Categories.

    /**
     * data binding
     */
    @Bindable
    public String getAuthor() {
        return mAuthor;
    }

    @Bindable
    public String getTitle() {
        return mTitle;
    }

    @Bindable
    public String getDate() {
        return mDate;
    }


    public Post(){}
    /**
     * Write to Parcel,so it can be delivered by intent or binder
     */
    public int describeContents()
    {
        return 0;
    }
    public void writeToParcel(Parcel out,int flags)
    {
        out.writeString(mTitle);
        out.writeString(mContent);
        out.writeString(mThumbnailUrl);
        out.writeString(mFeaturedImageUrl);
        out.writeString(mViewCount);
        out.writeString(mDate);
        out.writeString(mAuthor);
        out.writeString(mUrl);
        out.writeString(mExcerpt);
        out.writeInt(mId);
        out.writeInt(mCommentCount);
        out.writeStringList(mCategories);
    }
    public static final Parcelable.Creator<Post> CREATOR = new Parcelable.Creator<Post>()
    {
        public Post createFromParcel(Parcel in)
        {
            return new Post(in);
        }
        public Post[] newArray(int size)
        {
            return new Post[size];
        }
    };
    private Post(Parcel in)
    {
        setTitle(in.readString());
        setContent(in.readString());
        setThumbnailUrl(in.readString());
        setFeaturedImageUrl(in.readString());
        setViewCount(in.readString());
        setDate(in.readString());
        setAuthor(in.readString());
        setUrl(in.readString());
        setExcerpt(in.readString());
        setId(in.readInt());
        setCommentCount(in.readInt());
        in.readStringList(mCategories);
    }
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

    public String getExcerpt() {
        return mExcerpt;
    }

    public void setExcerpt(String excerpt) {
        mExcerpt = excerpt;
    }

}
