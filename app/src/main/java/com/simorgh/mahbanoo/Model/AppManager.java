package com.simorgh.mahbanoo.Model;

import android.os.Handler;
import android.os.StrictMode;

import com.facebook.stetho.Stetho;
import com.simorgh.databaseutils.CycleRepository;
import com.simorgh.mahbanoo.BuildConfig;
import com.simorgh.mahbanoo.R;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import androidx.multidex.MultiDexApplication;
import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;

public class AppManager extends MultiDexApplication {
    public static final String TAG = "debug13";
    public static Calendar minDate;
    public static Calendar maxDate;
    public static Locale mLocale;

    public static volatile Handler applicationHandler;
    private static CycleRepository cycleRepository;

    private static final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(
            NUMBER_OF_CORES * 2,
            NUMBER_OF_CORES * 2,
            60L,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>()
    );

    public static CycleRepository getCycleRepository() {
        return cycleRepository;
    }

    public static ThreadPoolExecutor getExecutor() {
        return executor;
    }

    @Override
    public void onCreate() {
        super.onCreate();


        mLocale = getResources().getConfiguration().locale;

        minDate = getCalendarInstance();
        minDate.add(Calendar.YEAR, -1);

        maxDate = getCalendarInstance();
        maxDate.add(Calendar.YEAR, 1);


        executor.execute(() -> {
            if (BuildConfig.BUILD_TYPE.equals("debug")) {
//            if (LeakCanary.isInAnalyzerProcess(this)) {
//                // This process is dedicated to LeakCanary for heap analysis.
//                // You should not init your app in this process.
//                return;
//            }
//            LeakCanary.install(this);
//            // Normal app init code...
//
                applicationHandler = new Handler(getApplicationContext().getMainLooper());
                cycleRepository = new CycleRepository(this);


                StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                        .detectDiskReads()
                        .detectDiskWrites()
                        .detectAll()
                        .penaltyLog()
                        .build());
                StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                        .detectLeakedSqlLiteObjects()
                        .detectLeakedClosableObjects()
                        .penaltyLog()
                        .penaltyDeath()
                        .build());


                ViewPump.init(ViewPump.builder()
                        .addInterceptor(new CalligraphyInterceptor(
                                new CalligraphyConfig.Builder()
                                        .setDefaultFontPath("fonts/iransans_medium.ttf")
                                        .setFontAttrId(R.attr.fontPath)
                                        .build()))
                        .build());

                Stetho.initializeWithDefaults(this);
            } else {
                getUncaughtExceptions();
            }
        });


    }

    private void getUncaughtExceptions() {
        // Setup handler for uncaught exceptions.
        try {
            Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
                Logger.d("Uncaught Exception thread: " + t.getName() + "" + Arrays.toString(e.getStackTrace()));

            });
        } catch (Exception e) {
            Logger.d("Could not set the Default Uncaught Exception Handler:" + Arrays.toString(e.getStackTrace()));
        }
    }

    public static Calendar getCalendarInstance() {
        return Calendar.getInstance(TimeZone.getTimeZone("Asia/Tehran"), mLocale);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        executor.shutdown();
    }
}
