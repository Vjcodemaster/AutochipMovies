package app_utility;


public class DataBaseHelper {

    //private variables
    private int _id;

    private int _account_odoo_id;
    private String _account_name;
    private String _account_login_url;
    private String _user_account_id;
    private String _user_account_password;


    public DataBaseHelper(){

    }

    public DataBaseHelper(int _account_odoo_id, String _account_name, String _account_login_url){
        this._account_odoo_id = _account_odoo_id;
        this._account_name = _account_name;
        this._account_login_url = _account_login_url;
    }

    public DataBaseHelper(String _user_account_id, String _user_account_password){
        this._user_account_id = _user_account_id;
        this._user_account_password = _user_account_password;
    }

    public int get_account_odoo_id(){
        return this._account_odoo_id;
    }

    public void set_account_odoo_id(int account_odoo_id){
        this._account_odoo_id = account_odoo_id;
    }

    public String get_account_name(){
        return this._account_name;
    }

    public void set_account_name(String account_name){
        this._account_name = account_name;
    }

    public String get_account_login_url(){
        return this._account_login_url;
    }

    public void set_account_login_url(String account_login_url){
        this._account_login_url = account_login_url;
    }

    public String get_user_account_id(){
        return this._user_account_id;
    }

    public void set_user_account_id(String user_account_id){
        this._user_account_id = user_account_id;
    }

    public String get_user_account_password(){
        return this._user_account_password;
    }

    public void set_user_account_password(String user_account_password){
        this._user_account_password = user_account_password;
    }

}
