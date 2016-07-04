package cn.suiseiseki.www.suiseiseeker.model;

/**
 * Created by Suiseiseki/shuikeyi on 2016/3/15.
 */
public class Category {
    public int getId() {
        return mId;
    }

    public void setId(int Id) {
        this.mId = Id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    /**
     * name:The name of category,id:the id of category
     */
    private int mId;
    private String mName;

    public String getSlugName() {
        return slugName;
    }

    public void setSlugName(String slagName) {
        this.slugName = slagName;
    }

    private String slugName;
}
