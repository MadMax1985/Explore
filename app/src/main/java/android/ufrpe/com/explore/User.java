package android.ufrpe.com.explore;

/**
 * Created by Marcelo on 05/08/2018.
 */

public class User {
    private String email;
    private  String imgUrl;

    public User(){

    }

    public User(String email){
        this.email = email;
    }

    public User(String email,String imgUrl){
        this.email = email;
        this.imgUrl = imgUrl;
    }

    public String getEmail(){
        return this.email;
    }
    public String getImgUrl(){
        return this.imgUrl;
    }
}
