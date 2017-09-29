
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import org.reactivestreams.Subscriber;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.CookieHandler;
import java.net.CookiePolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DefaultSubscriber;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.JavaNetCookieJar;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Query;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import HelloCookieJar;

import java.util.Collection;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import tgm.andoid.tourguidemarket.vo.ResponseCustomerStatsData;
import tgm.andoid.tourguidemarket.vo.ResponseFriendMessagesData;
import tgm.andoid.tourguidemarket.vo.ResponseFriendsData;
import tgm.andoid.tourguidemarket.vo.ResponseLoginData;


public interface HelloFriendMessagesService
{
    @GET("hellogetmessage")
    public Single<ResponseFriendMessagesData> getFriendMessages(@Query("hello_friends_id") String to_id, @Query("hello_start_time") String start_time);


    @GET("hellogetmessage")
    public Call<ResponseFriendMessagesData> getBlockingFriendMessages(@Query("hello_friends_id") String to_id, @Query("hello_start_time") String start_time);

}



public interface HelloCategoryProductsService
{
    @GET("dataslist")
    public Single<ResponseProductsData> fetchData(@Query("category_var") String category_var, @Query("per_page") String per_page, @Query("page_num") String page_num);



}


public Class someClass
{
	private static final String TAG = HelloWebManager.class.getSimpleName();
	private static final String WEB_IP = "http://100.100.100.100";
	private static final String API_URL = WEB_IP + "/plugin/subfolder/";
	private static final int DEFAULT_TIMEOUT = 15;

	private	Context mContext;
    private HelloCookieJar mHelloCookieJar;

	public class ResponseFriendMessagesData implements Comparable<Object>
	{
		@SerializedName("result")
		private String mResult;

		@SerializedName("data")
		private FriendMessages mData;

		public String getResult()
		{
			return mResult;
		}

		public FriendMessages getData()
		{
			return mData;
		}

		@Override
		public int compareTo(Object another)
		{
			ResponseFriendMessagesData f = (ResponseFriendMessagesData) another;
			return mResult.compareTo(f.mResult);
		}
	}

	public OkHttpClient createOkHttpClient(boolean isCookieCopy)
    {
        CookieJar cookieJar = mHelloCookieJar;

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        //interceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
		interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(interceptor);
		builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        builder.cookieJar(cookieJar);

        return builder.build();
    }
	
	
	public CompositeDisposable getDatas(String perPage, String pageNum, Consumer<ResponseProductsData> responseHandler, Consumer<Throwable> errorHandler, CompositeDisposable disposables)
	{
		HelloCategoryProductsService SERVICE = new Retrofit.Builder()
				.baseUrl(API_URL)
				.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
				.addConverterFactory(GsonConverterFactory.create())
				.client(createOkHttpClient(false))
				.build()
				.create(HelloCategoryProductsService.class);

		disposables.add(SERVICE
				.fetchViewedData(perPage, pageNum)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(responseHandler, errorHandler)
		);
		return disposables;

	}
	
	public ResponseFriendMessagesData Blocking_getFriendMessages(String friend_id, String startDateTime)
	{
		HelloFriendMessagesService SERVICE = new Retrofit.Builder()
				.baseUrl(API_URL)
				.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
				.addConverterFactory(GsonConverterFactory.create())
				.client(createOkHttpClient(false))
				.build()
				.create(HelloFriendMessagesService.class);

		Call<ResponseFriendMessagesData> call = SERVICE.getBlockingFriendMessages(friend_id, startDateTime);

		try
		{
			ResponseFriendMessagesData singleResult = call.execute().body();
			return singleResult;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public HashMap<String, String> getSessionVars()
	{
		return mHelloCookieJar.getCookieStore();
	}
}