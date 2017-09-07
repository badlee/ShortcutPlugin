//Copyright 2013 Jorge Cisneros jorgecis@gmail.com

package com.plugins.shortcut;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import android.util.Base64;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.*;
import android.content.Intent;
import android.content.Context;
import android.os.Parcelable;
import android.content.pm.ResolveInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

public class ShortcutPlugin extends CordovaPlugin {
    @Override
    public boolean execute(String action, JSONArray args,
                           CallbackContext callbackContext) throws JSONException {
        try {
            // Get params
            JSONObject arg_object = args.getJSONObject(0);

            // set param defaults
            String shortcutName = arg_object.getString("name");
            String shortcutUrl = arg_object.getString("url");
            String shortcutIcon = null;

            if (arg_object.has("icon")) {
                shortcutIcon = arg_object.getString("icon");
            }

            Context context = this.cordova.getActivity().getApplicationContext();
            PackageManager pm = context.getPackageManager();

            Intent intent = new Intent();

            // Get Shortcut URL
            Intent shortcutIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(shortcutUrl));
            intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);

            // Get Shortcut Name
            intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, shortcutName);

            // Get Shortcut Icon
            if (shortcutIcon == null) {
                Intent i = new Intent();
                i.setClassName(this.cordova.getActivity().getPackageName(),     
                               this.cordova.getActivity().getClass().getName());
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);     
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                ResolveInfo ri = pm.resolveActivity(i, 0);
                int iconId = ri.activityInfo.applicationInfo.icon;
                Parcelable icon = Intent.ShortcutIconResource.fromContext(context, iconId);
                intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
            } else {
                //Bitmap bmpIcon = decodeBase64(shortcutIcon);
                //Bitmap scaledBitmap = Bitmap.createScaledBitmap(bmpIcon, 128, 128, true);
                //intent.putExtra(Intent.EXTRA_SHORTCUT_ICON, scaledBitmap);
                intent.putExtra(Intent.EXTRA_SHORTCUT_ICON, decodeBase64(shortcutIcon));
            }

            intent.putExtra("duplicate", false);
            intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
            context.sendBroadcast(intent);

            callbackContext.success();
            return true;

        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
            callbackContext.error(e.getMessage());
            return false;
        }
    }

    private static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}
