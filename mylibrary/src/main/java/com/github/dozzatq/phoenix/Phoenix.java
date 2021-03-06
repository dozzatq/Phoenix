package com.github.dozzatq.phoenix;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.AnimRes;
import android.support.annotation.AnyThread;
import android.support.annotation.ArrayRes;
import android.support.annotation.BoolRes;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RawRes;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.github.dozzatq.phoenix.activity.StreetPolice;
import com.github.dozzatq.phoenix.notification.HandlerCore;
import com.github.dozzatq.phoenix.fonts.PhoenixTypeface;
import com.github.dozzatq.phoenix.notification.OnActionComplete;
import com.github.dozzatq.phoenix.notification.PhoenixCenter;
import com.github.dozzatq.phoenix.notification.PhoenixNotification;
import com.github.dozzatq.phoenix.prefs.PhoenixPreferences;
import com.github.dozzatq.phoenix.util.PhoenixDeviceIdGenerator;
import com.github.dozzatq.phoenix.util.PhoenixUtilities;

import java.io.File;
import java.util.Locale;

/**
 * Created by Rodion Bartoshik on 05.12.2016.
 */

public class Phoenix {

    private static Phoenix ourInstance = null;

    public static Phoenix getInstance() {
        Phoenix localInstance = ourInstance;
        if (localInstance == null) {
            synchronized (Phoenix.class) {
                localInstance = ourInstance;
                if (localInstance == null) {
                    ourInstance = localInstance = new Phoenix();
                }
            }
        }
        return localInstance;
    }

    private Phoenix()
    {
        userId = null;
    }

    private String userId;
    private Context applicationContext;

    public String getUserId()
    {
        if (userId==null)
        {
            userId = PhoenixDeviceIdGenerator.readDeviceId(getContext());
        }
        return userId;
    }

    private RequestQueue requestQueue;

    public PhoenixCenter getCenter()
    {
        return PhoenixCenter.getInstance();
    }

    public HandlerCore getCore() {
        return HandlerCore.getInstance();
    }

    public void init(Context applicationContext)
    {
        ourInstance.setContext(applicationContext);
    }

    public String getString(@StringRes int resId)
    {
        return getContext().getString(resId);
    }

    public Drawable getDrawable(@DrawableRes int resId)
    {
        return ContextCompat.getDrawable(getContext(), resId);
    }

    public boolean getBoolean(@BoolRes int resId)
    {
        return getResources().getBoolean(resId);
    }

    public Animation getAnimation(@AnimRes int resId)
    {
        return AnimationUtils.loadAnimation(getContext(), resId);
    }

    public int getInteger(@IntegerRes int resId)
    {
        return getResources().getInteger(resId);
    }

    public int getDimensionPixelSize(@DimenRes int resId)
    {
        return getResources().getDimensionPixelSize(resId);
    }

    public int getDimensionPixelOffset(@DimenRes int resId)
    {
        return getResources().getDimensionPixelOffset(resId);
    }

    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return getContext().getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);
    }

    public int checkSelfPermission(@NonNull String permission)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return getContext().checkSelfPermission(permission);
        }
        else return PackageManager.PERMISSION_GRANTED;
    }

    public PackageManager getPackageManager()
    {
        return getContext().getPackageManager();
    }

    public Resources getResources()
    {
        return getContext().getResources();
    }

    public Context getApplicationContext()
    {
        return getContext().getApplicationContext();
    }

    public File getCacheDir()
    {
        return getContext().getCacheDir();
    }

    public File getExternalCacheDir(){
        return getContext().getExternalCacheDir();
    }

    public Looper getMainLooper()
    {
        return getContext().getMainLooper();
    }

    public Phoenix startService(@NonNull Intent intentService)
    {
        getContext().startService(intentService);
        return this;
    }

    public boolean isTablet()
    {
        return PhoenixUtilities.isTablet();
    }

    @NonNull
    public PhoenixUtilities getUtilities()
    {
        return new PhoenixUtilities();
    }

    public Phoenix stopService(Intent intentService)
    {
        getContext().stopService(intentService);
        return this;
    }

    public LayoutInflater getLayoutInflater()
    {
        return LayoutInflater.from(getContext());
    }

    public Typeface getTypeface(@NonNull String assetPath)
    {
        return PhoenixTypeface.getTypeface(assetPath);
    }

    @AnyThread
    public PhoenixCenter addListener(String notificationKey, PhoenixNotification phoenixNotification)
    {
        return getCenter().addListener(notificationKey, phoenixNotification);
    }

    @AnyThread
    public PhoenixCenter addAction(@NonNull String actionKey, @NonNull OnActionComplete actionComplete)
    {
        return getCenter().addAction(actionKey, actionComplete);
    }

    @AnyThread
    public PhoenixCenter addAction(@NonNull Activity activity, @NonNull String actionKey,
                                   @NonNull OnActionComplete actionComplete)
    {
        return addAction(activity, actionKey, actionComplete, new StreetPolice() {
            @Override
            public void onDestroy() {
                destroy();
            }
        });
    }

    @AnyThread
    public PhoenixCenter addAction(@NonNull Activity activity, @NonNull String actionKey,
                                   @NonNull OnActionComplete actionComplete, @NonNull StreetPolice streetPolice)
    {
        return getCenter().addAction(activity, actionKey, actionComplete,streetPolice);
    }

    @AnyThread
    public PhoenixCenter callAction(@NonNull String actionKey, @Nullable Object...values)
    {
        return getCenter().callAction(actionKey, values);
    }

    @AnyThread
    public PhoenixCenter removeAction(@NonNull String actionKey)
    {
        return getCenter().removeAction(actionKey);
    }

    @AnyThread
    public PhoenixCenter removeAllListeners(@NonNull String notificationKey)
    {
        return getCenter().removeAllListeners(notificationKey);
    }

    @AnyThread
    public PhoenixCenter removeListener(@NonNull String notificationKey, @NonNull PhoenixNotification phoenixNotification)
    {
        return getCenter().removeListener(notificationKey, phoenixNotification);
    }

    @AnyThread
    public PhoenixCenter addListener(@Nullable Activity activity, @NonNull String notificationKey, @NonNull PhoenixNotification phoenixNotification) {
        return getCenter().addListener(activity, notificationKey, phoenixNotification);
    }

    @AnyThread
    public PhoenixCenter addListener(@Nullable Activity activity, @NonNull String notificationKey,
                                     @NonNull PhoenixNotification phoenixNotification,
                                     @NonNull StreetPolice streetPolice) {
        return getCenter().addListener(activity, notificationKey, phoenixNotification, streetPolice);
    }

    @AnyThread
    public PhoenixCenter postNotificationDelayed(@NonNull final String notificationKey, int delay, final Object... values)
    {
        getCenter().postNotificationDelayed(notificationKey, delay, values);
        return getCenter();
    }

    @AnyThread
    public PhoenixCenter postNotification(@NonNull final String notificationKey,final Object... values)
    {
        getCenter().postNotification(notificationKey, values);
        return getCenter();
    }

    public int getColor(@ColorRes int resId)
    {
        return ContextCompat.getColor(getContext(), resId);
    }

    public File getExternalFilesDir(String res)
    {
        return getContext().getExternalFilesDir(res);
    }

    public Phoenix startActivity(Intent shallStart)
    {
        ActivityCompat.startActivity(getContext(), shallStart, null);
        return this;
    }

    public PhoenixPreferences getPreferences()
    {
        return PhoenixPreferences.getInstance();
    }

    public AssetManager getAssets()
    {
        return getContext().getAssets();
    }

    public String getPackageName()
    {
        return getContext().getPackageName();
    }

    public Object getSystemService(@NonNull String service)
    {
        return getContext().getSystemService(service);
    }

    public String[] getStringArray(@ArrayRes int resId)
    {
        return getContext().getResources().getStringArray(resId);
    }

    public Context getContext() {
        return applicationContext;
    }

    public void setContext(@NonNull Context applicationContext) {
        if (applicationContext==null)
            throw new NullPointerException("Context must not be null!!");
        this.applicationContext = applicationContext;
        requestQueue = Volley.newRequestQueue(applicationContext);
        new PhoenixUtilities();
    }

    public Phoenix putString(@NonNull String key, String value)
    {
        PhoenixPreferences.getInstance().putString(key, value);
        return this;
    }

    public boolean putStringFuture(@NonNull String key, @NonNull String value)
    {
        return PhoenixPreferences.getInstance().putStringFuture(key, value);
    }

    public String getString(@NonNull String key)
    {
        return PhoenixPreferences.getInstance().getString(key, null);
    }

    public Phoenix putBoolean(@NonNull String key, boolean value)
    {
        PhoenixPreferences.getInstance().putBoolean(key, value);
        return this;
    }

    public boolean putBooleanFuture(@NonNull String key, boolean value)
    {
        return PhoenixPreferences.getInstance().putBooleanFuture(key, value);
    }

    public boolean getBoolean(@NonNull String key, boolean default_bool)
    {
        return PhoenixPreferences.getInstance().getBoolean(key, default_bool);
    }

    public Phoenix removeValue(@NonNull String key)
    {
        PhoenixPreferences.getInstance().removeValue(key);
        return this;
    }

    public Phoenix putLong(@NonNull String key, Long value)
    {
        PhoenixPreferences.getInstance().putLongFuture(key, value);
        return this;
    }

    public boolean putLongFuture(@NonNull String key, Long value)
    {
        return PhoenixPreferences.getInstance().putLongFuture(key, value);
    }

    public Phoenix runOnUIThread(@NonNull Runnable runnable, int delay)
    {
        Handler handler = new Handler(Looper.getMainLooper());
        if (delay>=0)
            handler.postDelayed(runnable, delay);
        else
            handler.post(runnable);
        return this;
    }

    public Phoenix runOnUIThread(@NonNull Runnable runnable)
    {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(runnable);
        return this;
    }

    public Uri getRawPath(@RawRes int resId)
    {
        return Uri.parse(String.format(Locale.US,"android.resource://%s/", getPackageName()) + resId);
    }

    public long getLong(@NonNull String key, long defValue) {
        return PhoenixPreferences.getInstance().getLong(key, defValue);
    }

    public Phoenix putFloat(@NonNull String key, Float value)
    {
        PhoenixPreferences.getInstance().putFloat(key, value);
        return this;
    }

    public boolean putFloatFuture(@NonNull String key, Float value)
    {
        return PhoenixPreferences.getInstance().putFloatFuture(key, value);
    }

    public float getFloat(@NonNull String key, float defValue) {
        return PhoenixPreferences.getInstance().getFloat(key, defValue);
    }

    public Phoenix putInt(@NonNull String key, Integer value)
    {
        PhoenixPreferences.getInstance().putInt(key, value);
        return this;
    }

    public boolean putIntFuture(@NonNull String key, Integer value)
    {
        return PhoenixPreferences.getInstance().putIntFuture(key, value);
    }

    public int getInt(@NonNull String key, int defValue) {
        return PhoenixPreferences.getInstance().getInt(key, defValue);
    }

    public String getString(@NonNull String key, String defValue) {
        return PhoenixPreferences.getInstance().getString(key, defValue);
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }
}
