package fpoly.anhntph36936.asm_api.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import javax.xml.transform.sax.SAXResult;

import fpoly.anhntph36936.asm_api.APIService;
import fpoly.anhntph36936.asm_api.Model.productModel;
import fpoly.anhntph36936.asm_api.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Product_ADT extends BaseAdapter {
    Context context;
    List<productModel> list;
    public Product_ADT(Context context, List<productModel> list){
        this.context = context;
        this.list = list;
    }
    public void setData(List<productModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("ViewHolder") View view = inflater.inflate(R.layout.item_product, parent, false);

        TextView tv_name = view.findViewById(R.id.tv_name);
        TextView tv_price = view.findViewById(R.id.tv_price);
        TextView tv_ploai = view.findViewById(R.id.tv_ploai);
        ImageView imv_avt = view.findViewById(R.id.imv_item);
        ImageView imv_del = view.findViewById(R.id.imv_del);
        ImageView imv_edit = view.findViewById(R.id.imv_edit);

        Picasso.get().load(list.get(position).getImg()).into(imv_avt);
        tv_name.setText(String.valueOf("Name: "+list.get(position).getName()));
        tv_price.setText(String.valueOf("Price: "+list.get(position).getPrice()+ ".000 đ"));
        tv_ploai.setText(String.valueOf("Type: "+list.get(position).getPloai()));

        imv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                @SuppressLint("ViewHolder") View view = LayoutInflater.from(context).inflate(R.layout.dialog_edit, null);
                builder.setView(view);
                builder.setCancelable(false);
                AlertDialog alertDialog= builder.create();

                EditText tv_id_up = view.findViewById(R.id.edt_id_up);
                EditText tv_name_up = view.findViewById(R.id.edt_name_up);
                EditText tv_gia_up = view.findViewById(R.id.edt_price_up);
                EditText tv_type_up = view.findViewById(R.id.edt_type_up);
                EditText tv_img_up = view.findViewById(R.id.edt_img_up);

                tv_id_up.setText(String.valueOf(list.get(position).get_id()));
                tv_name_up.setText(String.valueOf(list.get(position).getName()));
                tv_gia_up.setText(String.valueOf(list.get(position).getPrice()));
                tv_type_up.setText(String.valueOf(list.get(position).getPloai()));
                tv_img_up.setText(String.valueOf(list.get(position).getImg()));

                view.findViewById(R.id.btn_exit).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                view.findViewById(R.id.btn_update).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String id = tv_id_up.getText().toString();
                        String name = tv_name_up.getText().toString();
                        String gia = tv_gia_up.getText().toString();
                        String type = tv_type_up.getText().toString();
                        String img = tv_img_up.getText().toString();

                        productModel model = new productModel();
                        model.setName(name);
                        model.setPrice(Integer.parseInt(gia));
                        model.setPloai(type);
                        model.setImg(img);


                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(APIService.DOMAIN)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        // get data
                        APIService apiService = retrofit.create(APIService.class);
                        Call<List<productModel>> call = apiService.upPro(id, model);
                        call.enqueue(new Callback<List<productModel>>() {
                            @Override
                            public void onResponse(Call<List<productModel>> call, Response<List<productModel>> response) {
                                if (response.isSuccessful()) {
                                    List<productModel> updatedData = response.body();
                                    reloadData();
                                    Toast.makeText(context, "Update thành công", Toast.LENGTH_SHORT).show();
                                    alertDialog.dismiss();
                                } else {
                                    Toast.makeText(context, "Cập nhật không thành công: " + response.message(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<List<productModel>> call, Throwable t) {
                                Toast.makeText(context, "Cập nhật thất bại: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();
            }
        });

        imv_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Bạn muốn xóa không ?");
                builder.setCancelable(false);


                builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String id = list.get(position).get_id();

                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(APIService.DOMAIN)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        // get data
                        APIService apiService = retrofit.create(APIService.class);
                        Call<List<productModel>> call = apiService.delPro(id);

                        call.enqueue(new Callback<List<productModel>>() {
                            @Override
                            public void onResponse(Call<List<productModel>> call, Response<List<productModel>> response) {
                                if (response.isSuccessful()){
                                    Toast.makeText(context, "Xóa Thành công", Toast.LENGTH_SHORT).show();
                                    List<productModel> updatedData = response.body();
                                    reloadData();
                                } else {
                                    Toast.makeText(context, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<List<productModel>> call, Throwable t) {
                            }
                        });
                    }
                });

                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        return view;
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
                    setData(productList);
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
}
