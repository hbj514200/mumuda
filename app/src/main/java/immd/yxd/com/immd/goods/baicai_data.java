package immd.yxd.com.immd.goods;

public class baicai_data {

    String GoodsID = "";
    String Title = "";//(商品名称)
    String ali_click = "";//(购买url)
    String Pic = "";//(封面图)
    String Org_Price = "";//(原价)
    String Price = "";//(折扣价)
    String Quan_price = "";//(券值)
    String Quan_time = "";//(有效期)
    String status = "";//(状态)
    String IsTmall = "";//(是否天猫)

    public String getGoodsID() {
        return GoodsID;
    }

    public void setGoodsID(String goodsID) {
        GoodsID = goodsID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getAli_click() {
        return ali_click;
    }

    public void setAli_click(String ali_click) {
        this.ali_click = ali_click;
    }

    public String getPic() {
        return Pic;
    }

    public void setPic(String pic) {
        Pic = pic;
    }

    public String getOrg_Price() {
        return Org_Price;
    }

    public void setOrg_Price(String org_Price) {
        Org_Price = org_Price;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getQuan_price() {
        return Quan_price;
    }

    public void setQuan_price(String quan_price) {
        Quan_price = quan_price;
    }

    public String getQuan_time() {
        return Quan_time;
    }

    public void setQuan_time(String quan_time) {
        Quan_time = quan_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsTmall() {
        return IsTmall;
    }

    public void setIsTmall(String isTmall) {
        IsTmall = isTmall;
    }
}
