package fpoly.anhntph36936.asm_api.Main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import fpoly.anhntph36936.asm_api.APIService;
import fpoly.anhntph36936.asm_api.Model.userModel;
import fpoly.anhntph36936.asm_api.R;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUp extends AppCompatActivity {
    EditText edt_email_sign, edt_pass_sign, edt_name_sign;
    ImageView imv_back;
    Button btn_sign;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        edt_email_sign = findViewById(R.id.edt_email_sign);
        edt_pass_sign = findViewById(R.id.edt_pass_sign);
        edt_name_sign = findViewById(R.id.edt_name_sign);
        btn_sign = findViewById(R.id.btn_sign);
        imv_back = findViewById(R.id.imv_back);




        btn_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edt_email_sign.getText().toString().trim();
                String pass = edt_pass_sign.getText().toString().trim();
                String name = edt_name_sign.getText().toString().trim();
                if (username.isEmpty() || pass.isEmpty() || name.isEmpty()){
                    Toast.makeText(SignUp.this, "Vui lòng không để trống", Toast.LENGTH_SHORT).show();
                } else {

                    userModel model = new userModel();
                    model.setUsername(username);
                    model.setName(name);
                    model.setPass(pass);
                    Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(APIService.DOMAIN)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                APIService apiService = retrofit.create(APIService.class);


                Call<List<userModel>> call = apiService.DangKi(model);
                call.enqueue(new Callback<List<userModel>>() {
                    @Override
                    public void onResponse(Call<List<userModel>> call, Response<List<userModel>> response) {
                        if (response.isSuccessful()){
                            List<userModel> userList = response.body();
                            Toast.makeText(SignUp.this, "Đăng kí thành công", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignUp.this, Login.class));
                        } else {
                            Toast.makeText(SignUp.this, "Đăng kí thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<userModel>> call, Throwable t) {

                    }
                });
                }

            }
        });

        imv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUp.this, Login.class));
            }
        });
    }
}