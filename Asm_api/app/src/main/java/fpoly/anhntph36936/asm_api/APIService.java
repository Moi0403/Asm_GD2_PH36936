package fpoly.anhntph36936.asm_api;

import java.util.List;

import fpoly.anhntph36936.asm_api.Model.LoginRequest;
import fpoly.anhntph36936.asm_api.Model.APIResponse;
import fpoly.anhntph36936.asm_api.Model.productModel;
import fpoly.anhntph36936.asm_api.Model.userModel;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIService {


    // cap nhat ipv4
    String DOMAIN = "http://192.168.12.20:3000/";
    // get data
    @GET("/api/list")
    Call<List<productModel>> getProduct();

    @POST("/api/add")
    Call<List<productModel>> addPro(@Body productModel model);


    @DELETE("/api/xoa/{id}")
    Call<List<productModel>> delPro(@Path("id") String id);

    @PUT("/api/update/{id}")
    Call<List<productModel>> upPro(@Path("id") String id,@Body productModel model);

    @POST("/api/dangki")
    Call<List<userModel>> DangKi(@Body userModel model);
//    @POST("/api/dangki")
//    Call<APIResponse> DangKi(@Body RegisterRequest request);

    @POST("/api/login")
    Call<APIResponse> DangNhap(@Body LoginRequest request);

    @GET("/api/search/{name}")
    Call<List<productModel>> tim(@Path("name") String name);

    @GET("/api/sapxep")
    Call<List<productModel>> sapxep(@Query("sort") String sort);






}
