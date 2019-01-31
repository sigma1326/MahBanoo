package com.simorgh.mahbanoo.Model;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

@Keep
public final class AndroidUtils {
    public static void runOnUIThread(@NonNull Runnable runnable) {
        runOnUIThread(runnable, 0);
    }

    public static void runOnUIThread(@NonNull Runnable runnable, final long delay) {
        if (delay == 0) {
            AppManager.applicationHandler.post(runnable);
        } else {
            AppManager.applicationHandler.postDelayed(runnable, delay);
        }
    }

    public static void cancelRunOnUIThread(@NonNull Runnable runnable) {
        AppManager.applicationHandler.removeCallbacks(runnable);
    }


    public static void runOnBackgroundThread(@NonNull Runnable runnable) {
        new Thread(runnable).run();
    }

}
