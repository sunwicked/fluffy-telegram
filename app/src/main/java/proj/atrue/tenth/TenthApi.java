package proj.atrue.tenth;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.Cache;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Url;

public class TenthApi {


    private static final long HTTP_RESPONSE_DISK_CACHE_MAX_SIZE = 10 * 1024 * 1024;
    private static final long CONNECTION_TIMEOUT = 10;
    private static TenthApiInterface sTenthApiInterface;

    public static TenthApiInterface getApi(Context context) {

        if (sTenthApiInterface == null) {
            Retrofit restAdapter = new Retrofit.Builder()
                    .baseUrl(BuildConfig.API_URL)
                    .client(getOkHttpClient(context))
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            sTenthApiInterface = restAdapter.create(TenthApiInterface.class);
        }
        return sTenthApiInterface;

    }

    private static OkHttpClient getOkHttpClient(Context context) {
            OkHttpClient.Builder okClientBuilder = new OkHttpClient.Builder();
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel( HttpLoggingInterceptor.Level.BASIC);
            okClientBuilder.addInterceptor(httpLoggingInterceptor);
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
        Observable<Object> executeFullUrlCall(@Url String url);

    }
}
