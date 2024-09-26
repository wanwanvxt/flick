package vn.edu.eaut.flick.services;

import android.content.Context;
import androidx.annotation.NonNull;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import vn.edu.eaut.flick.R;

public final class RetrofitClient {
  private static Retrofit retrofit;

  public static Retrofit getRetrofit(Context context) {
    if (retrofit == null) {
      String apiBaseUrl = context.getString(R.string.api_base_url);

      File cacheDir = new File(context.getCacheDir(), "http_cache");
      Cache cache = new Cache(cacheDir, 50 * 1024 * 1024);
      OkHttpClient client = new OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .cache(cache)
        .addInterceptor(new CacheInterceptor())
        .addInterceptor(new RetryInterceptor(5))
        .build();

      retrofit = new Retrofit.Builder()
        .baseUrl(apiBaseUrl)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build();
    }
    return retrofit;
  }

  private static class CacheInterceptor implements Interceptor {
    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {
      Request request = chain.request()
        .newBuilder()
        .cacheControl(new CacheControl.Builder()
          .maxStale(10, TimeUnit.MINUTES)
          .build())
        .build();

      Response response = chain.proceed(request);

      return response.newBuilder()
        .header("Cache-Control", "public, max-age=60")
        .build();
    }
  }

  private static class RetryInterceptor implements Interceptor {
    private final int maxRetries;
    private int retryCount = 0;

    public RetryInterceptor(int maxRetries) {
      this.maxRetries = maxRetries;
    }

    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {
      Request request = chain.request();
      Response response = chain.proceed(request);
      while (!response.isSuccessful() && retryCount < maxRetries) {
        retryCount++;
        response.close();
        response = chain.proceed(request);
      }
      return response;
    }
  }
}
