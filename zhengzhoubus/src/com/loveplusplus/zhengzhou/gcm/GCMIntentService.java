package com.loveplusplus.zhengzhou.gcm;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gcm.GCMBaseIntentService;
import com.loveplusplus.zhengzhou.Config;
import com.loveplusplus.zhengzhou.gcm.command.TestCommand;

import static com.loveplusplus.zhengzhou.util.LogUtils.makeLogTag;
import static com.loveplusplus.zhengzhou.util.LogUtils.LOGD;
import static com.loveplusplus.zhengzhou.util.LogUtils.LOGI;
import static com.loveplusplus.zhengzhou.util.LogUtils.LOGE;
import static com.loveplusplus.zhengzhou.util.LogUtils.LOGW;

public class GCMIntentService extends GCMBaseIntentService {

    private static final String TAG = makeLogTag("GCM");

    private static final Map<String, GCMCommand> MESSAGE_RECEIVERS;
    static {
        // Known messages and their GCM message receivers
        Map <String, GCMCommand> receivers = new HashMap<String, GCMCommand>();
        receivers.put("test", new TestCommand());
        
        MESSAGE_RECEIVERS = Collections.unmodifiableMap(receivers);
    }

    public GCMIntentService() {
        super(Config.GCM_SENDER_ID);
    }

    @Override
    protected void onRegistered(Context context, String regId) {
        LOGI(TAG, "Device registered: regId=" + regId);
        ServerUtilities.register(context, regId);
    }

    @Override
    protected void onUnregistered(Context context, String regId) {
        LOGI(TAG, "Device unregistered");
        if (ServerUtilities.isRegisteredOnServer(context)) {
            ServerUtilities.unregister(context, regId);
        } else {
            // This callback results from the call to unregister made on
            // ServerUtilities when the registration to the server failed.
            LOGD(TAG, "Ignoring unregister callback");
        }
    }

    @Override
    protected void onMessage(Context context, Intent intent) {
        String action = intent.getStringExtra("action");
        
        Bundle extras = intent.getExtras();
        LOGD(TAG, extras.toString());
        
        String extraData = intent.getStringExtra("extraData");
        
        if (action == null) {
            LOGE(TAG, "Message received without command action");
            return;
        }

        action = action.toLowerCase();
        GCMCommand command = MESSAGE_RECEIVERS.get(action);
        
        if (command == null) {
            LOGE(TAG, "Unknown command received: " + action);
        } else {
            command.execute(this, action, extraData);
        }

    }

    @Override
    public void onError(Context context, String errorId) {
        LOGE(TAG, "Received error: " + errorId);
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        // log message
        LOGW(TAG, "Received recoverable error: " + errorId);
        return super.onRecoverableError(context, errorId);
    }
}