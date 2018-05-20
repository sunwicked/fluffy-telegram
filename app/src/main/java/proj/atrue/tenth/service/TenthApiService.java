package proj.atrue.tenth.service;

import android.content.Context;
import android.support.annotation.Nullable;


import java.io.File;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import proj.atrue.tenth.BuildConfig;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Url;

public class TenthApiService {


    private static final long HTTP_RESPONSE_DISK_CACHE_MAX_SIZE = 10 * 1024 * 1024;
    private static final long CONNECTION_TIMEOUT = 10;
    private static TenthApiInterface sTenthApiInterface;

    public static TenthApiInterface getApi(Context context) {

        if (sTenthApiInterface == null) {
            Retrofit restAdapter = new Retrofit.Builder()
                    .baseUrl(BuildConfig.API_URL)
                    .client(getOkHttpClient(context))
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
            sTenthApiInterface = restAdapter.create(TenthApiInterface.class);
        }
        return sTenthApiInterface;

    }

    private static OkHttpClient getOkHttpClient(Context context) {
            OkHttpClient.Builder okClientBuilder = new OkHttpClient.Builder();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okClientBuilder.addInterceptor(interceptor);
            final @Nullable File baseDir = context.getCacheDir();
            if (baseDir != null) {
                final File cacheDir = new File(baseDir, "HttpResponseCache");
                okClientBuilder.cache(new Cache(cacheDir, HTTP_RESPONSE_DISK_CACHE_MAX_SIZE));
            }
        okClientBuilder.connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS);
        okClientBuilder.readTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS);
        okClientBuilder.writeTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS);
            return okClientBuilder.build();
    }



    public interface TenthApiInterface {


        @GET
        Observable<Response<ResponseBody>> getUrLData(@Url String url);

    }
}
