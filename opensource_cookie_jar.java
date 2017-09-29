
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

public class HelloCookieJar implements CookieJar
{
    private final HashMap<String, String> cookieStore = new HashMap<>();

    private boolean mCookieSaveEndbled = false;

    private List<Cookie> mRequestCookies = new ArrayList<>();

    private Object mSyncObj = new Object();

    public HashMap<String, String> getCookieStore()
    {
        return cookieStore;
    }

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies)
    {
        if(isCookieSaveEnabled() == true)
        {
            for (int x = 0; x < cookies.size(); x++)
            {
                Cookie tmpCookie = cookies.get(x);
                String rawCookieStr = tmpCookie.toString();
                String rawCookieStrArr[] = rawCookieStr.split(";");
                if (rawCookieStrArr.length > 0)
                {
                    if (rawCookieStrArr[0].contains("some_cookie_tag") == true)
                    {
                        String keyStr = url.host();

                        String frontEndCookieArr[] = rawCookieStrArr[0].split("=");

                        if(frontEndCookieArr.length > 1)
                        {
                            cookieStore.put(keyStr, frontEndCookieArr[1]);
                        }
                    }
                }

            }
        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url)
    {
        synchronized(mSyncObj)
        {
            if (mRequestCookies.size() == 0)
            {
                Iterator it = cookieStore.entrySet().iterator();
                while (it.hasNext()) 
				{
                    Map.Entry pair = (Map.Entry) it.next();

                    Cookie tmpCookie = new Cookie.Builder()
                            .domain(pair.getKey().toString())
                            .path("/")
                            .name("frontend")
                            .value(pair.getValue().toString())
                            .httpOnly()
                            .build();

                    mRequestCookies.add(tmpCookie);
                }
            }
        }
        return mRequestCookies;
    }
	
    public void setCookieSaveEnabled(boolean flag)
    {
        mCookieSaveEndbled = flag;
    }

    public boolean isCookieSaveEnabled()
    {
        return mCookieSaveEndbled;
    }

    public void clearCookies()
    {
        cookieStore.clear();
        mRequestCookies.clear();
    }
}