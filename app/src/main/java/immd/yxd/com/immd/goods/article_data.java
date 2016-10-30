package immd.yxd.com.immd.goods;

/**
 * 这是优品区的文章结构体类
 */
public class article_data {

    String id = "";
    String title = "";//(商品名称)
    String pic = "";//(封面图)
    String desc = "";//简介

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
