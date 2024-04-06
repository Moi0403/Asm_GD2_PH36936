package fpoly.anhntph36936.asm_api.Model;

public class userModel {
    private String _id;
    private String username;
    private String name;
    private String pass;

    public userModel(String _id, String username, String name, String pass) {
        this._id = _id;
        this.username = username;
        this.name = name;
        this.pass = pass;
    }

    public userModel() {
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
