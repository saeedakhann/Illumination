package com.example.illumination;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class UserReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        context.startService(new Intent(context, UserService.class));
    }
}