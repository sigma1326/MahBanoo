package com.simorgh.redcalendar.View.main;

import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.simorgh.cycleview.CycleView;
import com.simorgh.redcalendar.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CycleInfoFragment extends Fragment {

    private TextView tvInfo;
    private Typeface typeface;

    private final static String yellowInfoString = "<h6><p>پیش از ان که دوره‌ی قاعدگی شما اغاز گردد تغییراتی همچون فیزیکی، عاطفی یا رفتاری در شما پدید می‌آید که پس از آغاز" +
            "    دوره‌ی" +
            " قاعدگی، آنها از بین می روند. این نشانه‌ها برای خانم‌ها متفاوت می‌باشد." +
            "</p></h6>" +
            "<br>" +
            "<h5>برخی از نشانه‌های فیزیکی:</h5>" +
            "<h6><p>" +
            "    متورم شدن شکم، دست‌ها و پاها" +
            "</h6></p>" +
            "<h6><p>" +
            "    گرفتگی و دردهای عضلانی" +
            "</h6></p>" +
            "<h6><p>" +
            "    حساسیت سینه‌ها" +
            "</h6></p>" +
            "<h6><p>" +
            "    احساس گرسنگی" +
            "</h6></p>" +
            "<h6><p>" +
            "    سردرد" +
            "</h6></p>" +
            "<h6><p>" +
            "    جوش زدن" +
            "</h6></p>" +
            "<h6><p>" +
            "    افزایش وزن" +
            "</h6></p>" +
            "<h6><p>" +
            "    یبوست یا اسهال" +
            "</h6></p>";

    private final static String redString = "<h6><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><p>" +
            "    یک دوره\u200C پریود (قاعدگی) مدت زمان اولین روز خونریزی در یک ماه تا اولین روز خونریزی در ماه بعد است. مثلاً اگر اولین خونریزی در ۸ اردیبهشت باشد و روز اول خونریزی بعدی وی در ۲ خرداد باشد دوره قاعدگی او ۲۴ روز طول کشیده است (از ۸ اردیبهشت تا ۱ خرداد)." +
            "</p></h6>" +
            "<br>" +
            "<h6><p>" +
            "    با بلوغ دختر، هورمون‌هایی از هیپوفیز (یکی از غدد موجود در مغز) ترشح می‌شود که این هورمون‌ها تخمدان‌ها را وادار به ساخت هورمون‌های زنانه می‌کنند. این هورمونهای زنانه استروژن و پروژسترون نامیده می‌شوند که اثرات زیادی در بدن دختر دارند و به رشد جسمی و بالغ شدن او کمک می‌کنند." +
            "</p></h6>" +
            "<br>" +
            "<h6><p>" +
            "    پس شروع دوره‌های پریود (قاعدگی) ، در هر ماه یک تخمک بسیار کوچک از درون تخمدان آزاد \u200Cشده و وارد لوله\u200Cهای فالوپ می\u200Cشود و از طریق آن به سمت رحم حرکت می‌کند. اگر در طی این مسیر تخمک توسط یک اسپرم مرد بارور شود تخم ایجاد می‌شود و تخم به سمت رحم رفته و در آنجا لانه گزینی می‌کند و با چسبیدن به دیواره رحم شروع به بزرگ شدن و رشد می‌نماید تا در پایان ۹ماه به یک نوزاد کامل تبدیل شود." +
            "</p></h6>" +
            "<br>" +
            "<p><h6>" +
            "    در صورتیکه تخمک با اسپرم برخورد نداشته باشد – مسأله‌ای که در اکثر دوره‌های قاعدگی اتفاق می‌افتد- تخمک بارور نمی‌شود و از بدن دفع می‌شود. در این صورت رحم که خود را آماده پذیرایی از تخم بارور کرده بود، با تشکیل نشدن تخم (بارور نشدن تخمک) به حالت اول خود برمی‌گردد و برای این کار مخاطات و بافت اضافی رحم شروع به ریزش می‌کند." +
            "</h6></p>" +
            "<br>" +
            "<p><h6>" +
            "    ریزش بافت و مخاطات که خونی است همان خونریزی قاعدگی است. باید توجه کرد که خونریزی قاعدگی فقط شامل خون نیست بلکه بافت اضافی و مخاطات خونی است که از رحم به بیرون ریخته می‌شود." +
            "</p></h6>";

    private final static String greenString = "<h6><br><p>" +
            "    دوره ی تخمک گذاری زمانی است که یک تخمک بالغ از تخمدان آزاد شده و لوله رحمی را تحت فشار قرار می‌دهد و در دسترس برای" +
            "    بارور شدن است. تقریبا هر ماه یک تخمک در یکی از تخمدان ها رشد می‌کند و پس از بلوغ توسط تخمدان آزاد می‌شود." +
            "</p></h6><br>" +
            "<h5><p>برخی از حقایق که در ارتباط با تخمک و تخمک گذاری:</p></h5>" +
            "<h6><p>هر تخمک بعد از 12 الی 24 بعد از ازاد شدن ، زنده می‌ماند.</p></h6>" +
            "<h6><p>در هر ماه تنها یک تخمک ازاد می گردد.</p></h6>" +
            "<h6><p>تخمک گذاری می‌تواند تحت تاثیر استرس، مریضی و غیره باشد.</p></h6>" +
            "<h6><p>برخی از زنان ممکن است در دوره‌ی تخمک گذاری خون لکه‌ای و یا نقطه ای مشاهده کنند و همراه با احساس درد باشد. </p></h6>" +
            "<h6><p>حتی اگر یک دوره قاعدگی رخ نداده باشد تخمک گذاری ممکن است رخ دهد.</p></h6>" +
            "<h6><p>در صورت عدم رهایی تخمک، مجددا به داخل رحم جذب می‌شود.</p></h6>";

    public static CycleInfoFragment newInstance() {
        return new CycleInfoFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() != null) {
            typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/iransans_medium.ttf");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.cycle_info_fragment, container, false);
        tvInfo = v.findViewById(R.id.tv_info);
        tvInfo.setTypeface(typeface);

        if (getArguments() != null) {
            int dayType;
            String text = "";
            dayType = CycleInfoFragmentArgs.fromBundle(getArguments()).getDayType();
            switch (dayType) {
                case CycleView.TYPE_RED:
                    text = redString;
                    break;
                case CycleView.TYPE_GREEN:
                case CycleView.TYPE_GREEN2:
                    text = greenString;
                    break;
                case CycleView.TYPE_YELLOW:
                    text = yellowInfoString;
                    break;
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                tvInfo.setText(Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT));
            } else {
                tvInfo.setText(Html.fromHtml(text));
            }
        }

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}
