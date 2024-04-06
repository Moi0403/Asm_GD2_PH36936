package fpoly.anhntph36936.asm_api.Main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import fpoly.anhntph36936.asm_api.APIService;
import fpoly.anhntph36936.asm_api.MainActivity;
import fpoly.anhntph36936.asm_api.Model.LoginRequest;
import fpoly.anhntph36936.asm_api.Model.APIResponse;
import fpoly.anhntph36936.asm_api.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Login extends AppCompatActivity {
    EditText edt_email_login, edt_pass_login;
    Button btn_login, btn_sign_login;
    private APIService apiService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edt_email_login = findViewById(R.id.edt_email_login);
        edt_pass_login = findViewById(R.id.edt_pass_login);
        btn_login = findViewById(R.id.btn_login);
        btn_sign_login = findViewById(R.id.btn_sign_login);


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = edt_email_login.getText().toString();
                String pass = edt_pass_login.getText().toString();

                if (user.isEmpty() || pass.isEmpty()){
                    Toast.makeText(Login.this, "Không được bỏ trống", Toast.LENGTH_SHORT).show();
                } else {
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(APIService.DOMAIN)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    apiService = retrofit.create(APIService.class);
                    LoginRequest request = new LoginRequest(user, pass);
                    Call<APIResponse> call = apiService.DangNhap(request);
                    call.enqueue(new Callback<APIResponse>() {
                        @Override
                        public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                            if (response.isSuccessful()) {
                                String message = response.body().getMessage();
                                Toast.makeText(Login.this, message, Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Login.this, MainActivity.class));
                            } else {
                                if (response.code() == 401) {
                                    Toast.makeText(Login.this, "Sai tên người dùng hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(Login.this, "Đăng nhập không thành công. Mã lỗi: " + response.code(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<APIResponse> call, Throwable t) {
                            Toast.makeText(Login.this, "Lỗi kết nối đến máy chủ", Toast.LENGTH_SHORT).show();
                            Log.e("LoginActivity", "Error: " + t.getMessage());
                        }
                    });

                }
            }
        });
        btn_sign_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, SignUp.class));
            }
        });
    }
}