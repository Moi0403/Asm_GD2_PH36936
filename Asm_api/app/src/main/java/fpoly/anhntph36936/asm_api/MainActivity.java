package fpoly.anhntph36936.asm_api;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.List;

import fpoly.anhntph36936.asm_api.Adapter.Product_ADT;
import fpoly.anhntph36936.asm_api.Model.productModel;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    ListView lv_view;
    RecyclerView rc_view;
    EditText edt_name, edt_price, edt_type, edt_img, edt_tim;
    ImageView imv_anh, imv_tim;
    Button btn_add, btn_exit, btn_tang, btn_giam;
    List<productModel> list_pro;
    Product_ADT productAdt;
    private static final int REQUEST_CODE_PICK_IMAGE = 1;
    private Uri selectedImageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(MainActivity.this);
        setContentView(R.layout.activity_main);
        lv_view = findViewById(R.id.lv_view);
        edt_tim= findViewById(R.id.edt_tim);
        imv_tim = findViewById(R.id.imv_tim);
        btn_giam = findViewById(R.id.btn_giam);
        btn_tang = findViewById(R.id.btn_tang);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIService.DOMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // get data
        APIService apiService = retrofit.create(APIService.class);
        Call<List<productModel>> call = apiService.getProduct();

        call.enqueue(new Callback<List<productModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<productModel>> call, @NonNull Response<List<productModel>> response) {
                if (response.isSuccessful()){
                    list_pro = response.body();
                    productAdt = new Product_ADT(MainActivity.this, list_pro);
                    lv_view.setAdapter(productAdt);
                }
            }

            @Override
            public void onFailure(Call<List<productModel>> call, Throwable t) {

            }
        });

        imv_tim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timData();
            }
        });

        btn_tang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sapXep("asc");
            }
        });
        btn_giam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sapXep("desc");
            }
        });

        findViewById(R.id.fab_them).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAdd();
            }
        });
    }

    private void sapXep(String sort){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIService.DOMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService apiService = retrofit.create(APIService.class);
        Call<List<productModel>> call = apiService.sapxep(sort);
        call.enqueue(new Callback<List<productModel>>() {
            @Override
            public void onResponse(Call<List<productModel>> call, Response<List<productModel>> response) {
                if (response.isSuccessful()){
                    list_pro = response.body();
                    productAdt = new Product_ADT(MainActivity.this, list_pro);
                    lv_view.setAdapter(productAdt);
                } else {
                    Toast.makeText(MainActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<productModel>> call, Throwable t) {

            }
        });
    }
    private void timData(){
        String timname = edt_tim.getText().toString();
        if (timname.isEmpty()){
            Toast.makeText(MainActivity.this, "Vui lòng không bỏ trống", Toast.LENGTH_SHORT).show();
            reloadData();
            productAdt = new Product_ADT(MainActivity.this, list_pro);
            lv_view.setAdapter(productAdt);
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIService.DOMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // get data
        APIService apiService = retrofit.create(APIService.class);
        Call<List<productModel>> call = apiService.tim(timname);
        call.enqueue(new Callback<List<productModel>>() {
            @Override
            public void onResponse(Call<List<productModel>> call, Response<List<productModel>> response) {
                if (response.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Tìm thành công", Toast.LENGTH_SHORT).show();
                    list_pro.clear();
                    list_pro = response.body();
                    productAdt = new Product_ADT(MainActivity.this, list_pro);
                    lv_view.setAdapter(productAdt);
                } else {
                    Toast.makeText(MainActivity.this, "Tìm không thành công", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<productModel>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Lỗi tìm", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void showAdd(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_them, null);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();

        edt_name = view.findViewById(R.id.edt_name);
        edt_price = view.findViewById(R.id.edt_price);
        edt_img = view.findViewById(R.id.edt_img);
        edt_type = view.findViewById(R.id.edt_type);
        btn_add = view.findViewById(R.id.btn_add);
        btn_exit = view.findViewById(R.id.btn_out);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edt_name.getText().toString();
                String price = edt_price.getText().toString();
                String img = edt_img.getText().toString();
                String type = edt_type.getText().toString();

                productModel model = new productModel();
                model.setName(name);
                model.setPrice(Integer.parseInt(price));
                model.setImg(img);
                model.setPloai(type);

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(APIService.DOMAIN)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                // get data
                APIService apiService = retrofit.create(APIService.class);
                Call<List<productModel>> call = apiService.addPro(model);

                call.enqueue(new Callback<List<productModel>>() {
                    @Override
                    public void onResponse(Call<List<productModel>> call, Response<List<productModel>> response) {
                        if (response.isSuccessful()){
                            list_pro = response.body();
                            Toast.makeText(MainActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();

                            productAdt = new Product_ADT(MainActivity.this, list_pro);
                            reloadData();
                            lv_view.setAdapter(productAdt);
                            alertDialog.dismiss();
                        } else {
                            Toast.makeText(MainActivity.this, "Thêm không thành công", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<productModel>> call, Throwable t) {
                        Log.e("Main", t.getMessage());
                    }
                });


            }
        });

        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });


        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }
    public void reloadData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIService.DOMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService apiService = retrofit.create(APIService.class);
        Call<List<productModel>> call = apiService.getProduct();

        call.enqueue(new Callback<List<productModel>>() {
            @Override
            public void onResponse(Call<List<productModel>> call, Response<List<productModel>> response) {
                if (response.isSuccessful()) {
                    List<productModel> productList = response.body();
                    productAdt.setData(productList);
                } else {
                    // Xử lý lỗi
                }
            }

            @Override
            public void onFailure(Call<List<productModel>> call, Throwable t) {
                // Xử lý lỗi
            }
        });
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {

            selectedImageUri = data.getData();

            imv_anh.setImageURI(selectedImageUri);
        }
    }
    private byte[] readBytesFromUri(Uri uri) throws IOException {
        InputStream inputStream = getContentResolver().openInputStream(uri);
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, bytesRead);
        }
        return byteBuffer.toByteArray();
    }

}