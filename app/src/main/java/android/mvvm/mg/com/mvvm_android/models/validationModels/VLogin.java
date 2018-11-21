package android.mvvm.mg.com.mvvm_android.models.validationModels;

public class VLogin {

    private String email;
    private String password;

    public VLogin(final String email, final String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
