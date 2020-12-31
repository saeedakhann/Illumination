package com.example.illumination;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class OrderListReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        context.startService(new Intent(context, OrderListService.class));
    }
}
