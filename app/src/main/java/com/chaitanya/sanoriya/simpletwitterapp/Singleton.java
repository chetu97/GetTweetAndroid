package com.chaitanya.sanoriya.simpletwitterapp;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

public class Singleton
{
    private static final Singleton ourInstance = new Singleton();
    private RequestQueue mRequestQueue;
    private static Context context;
    private static volatile Singleton mInstance;

    public Singleton(Context ctx)
    {
        this.context = ctx;
        mRequestQueue = getRequestQueue();
    }

    public RequestQueue getRequestQueue()
    {
        if (mRequestQueue == null)
        {
            // context is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req)
    {
        getRequestQueue().getCache().clear();
        req.setShouldCache(false);
        req.setRetryPolicy(new DefaultRetryPolicy(
                15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        getRequestQueue().add(req);
    }

    public static synchronized Singleton getInstance(Context context)
    {
        if (mInstance == null)
        {
            mInstance = new Singleton(context);
        }
        return mInstance;
    }

    private Singleton()
    {
    }

    public static void toast(Context context, String msg)
    {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void toasterror(Context context, VolleyError volleyError)
    {
        String message = "";
        if (volleyError instanceof NetworkError)
        {
            message = "Cannot connect to Internet. Please check your connection!";
        } else if (volleyError instanceof ServerError)
        {
            message = "Error! An account with this email already exists. Did you forget your password?";
        } else if (volleyError instanceof AuthFailureError)
        {
            message = "Email or Password mismatch.";
        } else if (volleyError instanceof ParseError)
        {
            message = "Parsing error! Please try again after some time!!";
        } else if (volleyError instanceof NoConnectionError)
        {
            message = "Cannot connect to Internet. Please check your connection!";
        } else if (volleyError instanceof TimeoutError)
        {
            message = "Connection TimeOut! Your internet connection may be too slow at the moment. Please try again.";
        }
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}