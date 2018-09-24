package mono.hg.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PackageChangesReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();

        if (intent.getAction() != null && intent.getData() != null) {
            String packageName = intent.getData().getEncodedSchemeSpecificPart();
            
            // Receive intent from broadcast and simply let the launcher know it needs a refresh.
            if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")
                    || intent.getAction().equals("android.intent.action.PACKAGE_CHANGED")
                    && !packageName.contains(context.getPackageName())) {
                editor.putBoolean("addApp", true).apply();
                editor.putString("added_app", packageName).apply();
            }

            if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")
                    && !packageName.contains(context.getPackageName())) {
                editor.putBoolean("removedApp", true).apply();
                editor.putString("removed_app", packageName).apply();
            }
        }

        // HACK: Unregister immediately because we don't need the receiver anymore.
        // We shouldn't need this, but I don't like the log spam that comes with _not_ doing this.
        context.unregisterReceiver(this);
    }
}
