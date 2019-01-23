package com.simorgh.mahbanoo.View.main;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simorgh.cycleview.CycleView;
import com.simorgh.mahbanoo.R;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CycleInfoFragment extends Fragment {

    private final static String yellowInfoString = "<h6><p>پیش از ان که دوره ی قاعدگی شما اغاز گردد تغییراتی همچون فیزیکی، عاطفی یا رفتاری در شما پدید م آید که پس از آغاز\n" +
            "    دوره\n" +
            "    ی قاعدگی، آنها از بین می روند. این نشانه ها برای خانم ها متفاوت می باشد.\n" +
            "</p></h6>\n" +
            "<h5>برخی از نشانه های فیزیکی:</h5>\n" +
            "<br>\n" +
            "<h6><p>\n" +
            "    متورم شدن شکم، دست ها و پاها\n" +
            "</h6></p>\n" +
            "<h6><p>\n" +
            "    گرفتگی و دردهای عضلانی\n" +
            "</h6></p>\n" +
            "<h6><p>\n" +
            "    حساسیت سینه ها\n" +
            "</h6></p>\n" +
            "<h6><p>\n" +
            "    احساس گرسنگی\n" +
            "</h6></p>\n" +
            "<h6><p>\n" +
            "    سردرد\n" +
            "</h6></p>\n" +
            "<h6><p>\n" +
            "    جوش زدن\n" +
            "</h6></p>\n" +
            "<h6><p>\n" +
            "    افزایش وزن\n" +
            "</h6></p>\n" +
            "<h6><p>\n" +
            "    یبوست یا اسهال\n" +
            "</h6></p>\n";
    private Typeface typeface;
    private final static String redString = "<h6><p>\n" +
            "    یک دوره\u200C پریود (قاعدگی) مدت زمان اولین روز خونریزی در یک ماه تا اولین روز خونریزی در ماه بعد است. مثلاً اگر اولین خونریزی در ۸ اردیبهشت باشد و روز اول خونریزی بعدی وی در ۲ خرداد باشد دوره قاعدگی او ۲۴ روز طول کشیده است (از ۸ اردیبهشت تا ۱ خرداد).\n" +
            "</p></h6>\n" +
            "<br>\n" +
            "<h6><p>\n" +
            "    با بلوغ دختر، هورمونهایی از هیپوفیز (یکی از غدد موجود در مغز) ترشح می\u200Cشود که این هورمونها تخمدانها را وادار به ساخت هورمونهای زنانه می\u200Cکنند. این هورمونهای زنانه استروژن و پروژسترون نامیده می\u200Cشوند که اثرات زیادی در بدن دختر دارند و به رشد جسمی و بالغ شدن او کمک می کنند.\n" +
            "</p></h6>\n" +
            "<br>\n" +
            "<h6><p>\n" +
            "    پس شروع دوره های پریود (قاعدگی ) ، در هر ماه یک تخمک بسیار کوچک از درون تخمدان آزاد \u200Cشده و وارد لوله\u200Cهای فالوپ می\u200Cشود و از طریق آن به سمت رحم حرکت می\u200Cکند. اگر در طی این مسیر تخمک توسط یک اسپرم مرد بارور شود تخم ایجاد می\u200Cشود و تخم به سمت رحم رفته و در آنجا لانه گزینی می\u200Cکند و با چسبیدن به دیواره رحم شروع به بزرگ شدن و رشد می\u200Cنماید تا در پایان ۹ماه به یک نوزاد کامل تبدیل شود.\n" +
            "</p></h6>\n" +
            "<br>\n" +
            "<p><h6>\n" +
            "    در صورتیکه تخمک با اسپرم برخورد نداشته باشد – مسأله\u200Cای که در اکثر دروه\u200Cهای قاعدگی اتفاق می\u200Cافتد- تخمک بارور نمی\u200Cشود و از بدن دفع می\u200Cشود. در این صورت رحم که خود را آماده پذیرایی از تخم بارور کرده بود، با تشکیل نشدن تخم (بارور نشدن تخمک) به حالت اول خود برمی\u200Cگردد و برای این کار مخاطات و بافت اضافی رحم شروع به ریزش می\u200Cکند.\n" +
            "</h6></p>\n" +
            "<br>\n" +
            "<p><h6>\n" +
            "    ریزش بافت و مخاطات که خونی است همان خونریزی قاعدگی است. باید توجه کرد که خونریزی قاعدگی فقط شامل خون نیست بلکه بافت اضافی و مخاطات خونی است که از رحم به بیرون ریخته می\u200Cشود.\n" +
            "</p></h6>";
    private final static String greenString = "<h6><p>\n" +
            "    دوره ی تخمک گذاری زمانی است که یک تخمک بالغ از تخمدان آزاد شده و لوله رحمی را تحت فشار قرار می دهد و در دسترس برای\n" +
            "    بارور شدن است. تقریبا هر ماه یک تخمک در یکی از تخمدان ها رشد می کند و پس از بلوغ توسط تخمدان آزاد می شود.\n" +
            "</p></h6><br>\n" +
            "<h5><p>برخی از حقایق که در ارتباط با تخمک و تخمک گذاری:</p></h5>\n" +
            "<h6><p>هر تخمک بعد از 12 الی 24 بعد از ازاد شدن ، زنده می ماند.</p></h6>\n" +
            "<h6><p>در هر ماه تنها یک تخمک ازاد می گردد.</p></h6>\n" +
            "<h6><p>تخمک گذاری می تواند تحت تاثیر استرس، مریضی و غیره باشد.</p></h6>\n" +
            "<h6><p>برخی از زنان ممکن است در دوره ی تخمک گذاری خون لکه ای و یا نقطه ای مشاهده کنند و همراه با احساس درد باشد. </p></h6>\n" +
            "<h6><p>حتی اگر یک دوره قاعدگی رخ نداده باشدT تخمک گذاری ممکن است رخ دهد.</p></h6>\n" +
            "<h6><p>در صورت عدم رهایی تخمک، مجددا به داخل رحم جذب می شود.</p></h6>\n";
    private HtmlTextView tvInfo;

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

            tvInfo.setHtml(text);
        }

        return v;
    }

    @Override
    public void onDestroyView() {
        tvInfo = null;
        super.onDestroyView();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}
