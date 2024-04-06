package fpoly.anhntph36936.asm_api.Model;

public class productModel {
    private String _id;
    private String name;
    private int price;
    private String ploai;
    private String img;

    public productModel() {
    }

    public productModel(String _id, String name, int price, String ploai, String img) {
        this._id = _id;
        this.name = name;
        this.price = price;
        this.ploai = ploai;
        this.img = img;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getPloai() {
        return ploai;
    }

    public void setPloai(String ploai) {
        this.ploai = ploai;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
