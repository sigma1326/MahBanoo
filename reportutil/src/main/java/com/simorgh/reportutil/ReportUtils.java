package com.simorgh.reportutil;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.simorgh.calendarutil.CalendarTool;
import com.simorgh.cyclecalendar.view.BaseMonthView;
import com.simorgh.cycleutils.CycleData;
import com.simorgh.cycleutils.CycleUtils;
import com.simorgh.databaseutils.CycleRepository;
import com.simorgh.databaseutils.model.Cycle;
import com.simorgh.databaseutils.model.DayMood;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentActivity;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

import static com.simorgh.cyclecalendar.view.BaseMonthView.toTimeInMillis;

public final class ReportUtils {
    private static CycleData cycleData;
    private static Calendar clearDate = Calendar.getInstance();


    @SuppressLint("CheckResult")
    public static void createReport(@NonNull final Activity activity
            , @NonNull final ThreadPoolExecutor executor
            , @NonNull final CycleRepository cycleRepository
            , @NonNull final Calendar startRange
            , @NonNull final Calendar endRange
            , final boolean reportBleeding
            , final boolean reportEmotion
            , final boolean reportPain
            , final boolean reportEatingDesire
            , final boolean reportHairStyle
            , final boolean reportWeight
            , final boolean reportDrugs) {

        final RxPermissions rxPermissions = new RxPermissions((FragmentActivity) activity);
        rxPermissions
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean granted) {
                        if (granted) {
                            executor.execute(() -> {
                                try {
                                    Document document = new Document();

                                    String path = Environment.getExternalStorageDirectory() + "/" + "report" + ".pdf";
                                    File pdfFile = new File(path);
                                    if (pdfFile.exists()) {
                                        boolean isDeleted = pdfFile.delete();
                                        if (isDeleted) {
                                            buildTable(document, path, startRange, endRange, cycleRepository, cycleRepository.getUserWithCycles().getCycles(), activity
                                                    , reportBleeding
                                                    , reportEmotion
                                                    , reportPain
                                                    , reportEatingDesire
                                                    , reportHairStyle
                                                    , reportWeight
                                                    , reportDrugs);
                                        }
                                    } else {
                                        buildTable(document, path, startRange, endRange, cycleRepository, cycleRepository.getUserWithCycles().getCycles(), activity
                                                , reportBleeding
                                                , reportEmotion
                                                , reportPain
                                                , reportEatingDesire
                                                , reportHairStyle
                                                , reportWeight
                                                , reportDrugs);
                                    }


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            });
                        } else {
                            Toast.makeText(activity, "برای ایجاد گزارش نیاز به دسترسی برای ایجاد فایل است", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private static void buildTable(Document document, String path, @NonNull Calendar startRange, @NonNull Calendar endRange, @NonNull final CycleRepository cycleRepository, @NonNull List<Cycle> cycles, @NonNull Activity activity, boolean reportBleeding, boolean reportEmotion, boolean reportPain, boolean reportEatingDesire, boolean reportHairStyle, boolean reportWeight, boolean reportDrugs) throws DocumentException, IOException {
        // Location to save
        PdfWriter.getInstance(document, new FileOutputStream(path));

        // Open to write
        document.open();

        // Document Settings
        document.setPageSize(PageSize.A4);
        document.addCreationDate();
        document.addAuthor("Mah banoo");
        document.addCreator("Mah Banoo");

        BaseColor mColorAccent = new BaseColor(0, 0, 0, 255);
        float mHeadingFontSize = 12.0f;
        float mValueFontSize = 26.0f;
        BaseFont urName = BaseFont.createFont("assets/fonts/iransans_medium.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);


        // LINE SEPARATOR
        LineSeparator lineSeparator = new LineSeparator();
        lineSeparator.setLineColor(new BaseColor(0, 0, 0, 68));


        // Title Order Details...
        // Adding Title....
        Font titleFont = new Font(urName, 12, Font.NORMAL, BaseColor.BLACK);

        // Fields of Order Details...
        // Adding Chunks for Title and value
        Font f = new Font(urName, mHeadingFontSize, Font.NORMAL, mColorAccent);
        PdfPTable table = new PdfPTable(4);
        table.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
        table.setExtendLastRow(false);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);


        PdfPCell title = new PdfPCell(new Paragraph("گزارش", titleFont));
        title.setPadding(10);
        title.setHorizontalAlignment(Element.ALIGN_CENTER);
        title.setColspan(4);
        title.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
        table.addCell(title);

        PdfPCell dateCol = new PdfPCell(new Paragraph("تاریخ", f));
        dateCol.setPadding(10);
        dateCol.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
        dateCol.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(dateCol);

        PdfPCell colorCol = new PdfPCell(new Paragraph("وضعیت جسمانی", f));
        colorCol.setPadding(10);
        colorCol.setHorizontalAlignment(Element.ALIGN_CENTER);
        colorCol.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
        table.addCell(colorCol);

        PdfPCell statusCol = new PdfPCell(new Paragraph("حالات ثبت شده", f));
        statusCol.setPadding(10);
        statusCol.setColspan(2);
        statusCol.setHorizontalAlignment(Element.ALIGN_CENTER);
        statusCol.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
        table.addCell(statusCol);


        List<DayMood> dayMoodList = cycleRepository.getDayMoodRange(startRange, endRange);
        if (dayMoodList != null) {
            PdfPCell cell;
            PdfPCell date;
            List<CycleData> cycleDataList = CycleUtils.getCycleDataList(cycles);

            for (int i = 0; i < dayMoodList.size(); i++) {
                Phrase phrase = new Phrase(CalendarTool.GregorianToPersian(dayMoodList.get(i).getId()).getPersianLongDate(), f);
                phrase.getChunks().get(0).setLineHeight(20);
                phrase.setLeading(30, 0);
                date = new PdfPCell(phrase);
                date.setPadding(10);
                date.setHorizontalAlignment(Element.ALIGN_CENTER);
                date.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);

                clearDate.clear();
                for (CycleData c : cycleDataList) {
                    if (toTimeInMillis(c.getEndDate()) == toTimeInMillis(clearDate)) {
                        if (toTimeInMillis(dayMoodList.get(i).getId()) >= toTimeInMillis(c.getStartDate())) {
                            cycleData = c;
                            break;
                        }
                    } else {
                        if (toTimeInMillis(dayMoodList.get(i).getId()) >= toTimeInMillis(c.getStartDate()) && toTimeInMillis(dayMoodList.get(i).getId()) <= toTimeInMillis(c.getEndDate())) {
                            cycleData = c;
                            break;
                        }
                    }
                }
                int dayType = getDayType(cycleData, dayMoodList.get(i).getId());
                String dayColor = "";
                switch (dayType) {
                    case BaseMonthView.TYPE_GRAY:
                        dayColor = "عادی";
                        break;
                    case BaseMonthView.TYPE_GREEN:
                    case BaseMonthView.TYPE_GREEN2:
                        dayColor = "تخمک گذاری";
                        break;
                    case BaseMonthView.TYPE_RED:
                        dayColor = "پریود";
                        break;
                    case BaseMonthView.TYPE_YELLOW:
                        dayColor = "pms";
                        break;
                }
                phrase = new Phrase(dayColor, f);
                phrase.getChunks().get(0).setLineHeight(20);
                cell = new PdfPCell(phrase);
                cell.setPadding(10);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);

                boolean addCell = false;
                StringBuilder loggedMoods = new StringBuilder();
                if (reportBleeding && dayMoodList.get(i).getTypeBleedingSelectedIndex() != -1) {
                    addCell = true;
                    loggedMoods.append("میزان خونریزی: ");
                    switch (dayMoodList.get(i).getTypeBleedingSelectedIndex()) {
                        case 0:
                            loggedMoods.append("لکه‌بینی");
                            break;
                        case 1:
                            loggedMoods.append("کم");
                            break;
                        case 2:
                            loggedMoods.append("متوسط");
                            break;
                        case 3:
                            loggedMoods.append("زیاد");
                            break;
                    }
                }
                if (reportEmotion && dayMoodList.get(i).getTypeEmotionSelectedIndices() != null && !dayMoodList.get(i).getTypeEmotionSelectedIndices().isEmpty()) {
                    loggedMoods.append(loggedMoods.length() > 0 ? "\n" : "");
                    loggedMoods.append("احساسات: ");
                    boolean hasFirst = false;
                    addCell = true;
                    for (int j = 0; j < dayMoodList.get(i).getTypeEmotionSelectedIndices().size(); j++) {
                        switch (dayMoodList.get(i).getTypeEmotionSelectedIndices().get(j)) {
                            case 0:
                                loggedMoods.append(hasFirst ? " ، " : "");
                                hasFirst = true;
                                loggedMoods.append("خوشحال");
                                break;
                            case 1:
                                loggedMoods.append(hasFirst ? " ، " : "");
                                hasFirst = true;
                                loggedMoods.append("ناراحت");
                                break;
                            case 2:
                                loggedMoods.append(hasFirst ? " ، " : "");
                                hasFirst = true;
                                loggedMoods.append("حساس");
                                break;
                            case 3:
                                loggedMoods.append(hasFirst ? " ، " : "");
                                hasFirst = true;
                                loggedMoods.append("عصبانی");
                                break;
                        }
                    }
                }

                if (reportPain && dayMoodList.get(i).getTypePainSelectedIndices() != null && !dayMoodList.get(i).getTypePainSelectedIndices().isEmpty()) {
                    loggedMoods.append(loggedMoods.length() > 0 ? "\n" : "");
                    loggedMoods.append("احساس درد: ");
                    boolean hasFirst = false;
                    addCell = true;
                    for (int j = 0; j < dayMoodList.get(i).getTypePainSelectedIndices().size(); j++) {
                        switch (dayMoodList.get(i).getTypePainSelectedIndices().get(j)) {
                            case 0:
                                loggedMoods.append(hasFirst ? " ، " : "");
                                hasFirst = true;
                                loggedMoods.append("درد شکم");
                                break;
                            case 1:
                                loggedMoods.append(hasFirst ? " ، " : "");
                                hasFirst = true;
                                loggedMoods.append("حساس شدن سینه‌ها");
                                break;
                            case 2:
                                loggedMoods.append(hasFirst ? " ، " : "");
                                hasFirst = true;
                                loggedMoods.append("سر درد");
                                break;
                            case 3:
                                loggedMoods.append(hasFirst ? " ، " : "");
                                hasFirst = true;
                                loggedMoods.append("کمر درد");
                                break;
                        }
                    }
                }

                if (reportEatingDesire && dayMoodList.get(i).getTypeEatingDesireSelectedIndices() != null && !dayMoodList.get(i).getTypeEatingDesireSelectedIndices().isEmpty()) {
                    loggedMoods.append(loggedMoods.length() > 0 ? "\n" : "");
                    loggedMoods.append("میل به خوردن: ");
                    boolean hasFirst = false;
                    addCell = true;
                    for (int j = 0; j < dayMoodList.get(i).getTypeEatingDesireSelectedIndices().size(); j++) {
                        switch (dayMoodList.get(i).getTypeEatingDesireSelectedIndices().get(j)) {
                            case 0:
                                loggedMoods.append(hasFirst ? " ، " : "");
                                hasFirst = true;
                                loggedMoods.append("نمک");
                                break;
                            case 1:
                                loggedMoods.append(hasFirst ? " ، " : "");
                                hasFirst = true;
                                loggedMoods.append("شکلات");
                                break;
                            case 2:
                                loggedMoods.append(hasFirst ? " ، " : "");
                                hasFirst = true;
                                loggedMoods.append("شیرینی");
                                break;
                            case 3:
                                loggedMoods.append(hasFirst ? " ، " : "");
                                hasFirst = true;
                                loggedMoods.append("ترشی");
                                break;
                        }
                    }
                }

                if (reportHairStyle && dayMoodList.get(i).getTypeHairStyleSelectedIndices() != null && !dayMoodList.get(i).getTypeHairStyleSelectedIndices().isEmpty()) {
                    loggedMoods.append(loggedMoods.length() > 0 ? "\n" : "");
                    loggedMoods.append("حالت موها: ");
                    boolean hasFirst = false;
                    addCell = true;
                    for (int j = 0; j < dayMoodList.get(i).getTypeHairStyleSelectedIndices().size(); j++) {
                        switch (dayMoodList.get(i).getTypeHairStyleSelectedIndices().get(j)) {
                            case 0:
                                loggedMoods.append(hasFirst ? " ، " : "");
                                hasFirst = true;
                                loggedMoods.append("خوب");
                                break;
                            case 1:
                                loggedMoods.append(hasFirst ? " ، " : "");
                                hasFirst = true;
                                loggedMoods.append("بد");
                                break;
                            case 2:
                                loggedMoods.append(hasFirst ? " ، " : "");
                                hasFirst = true;
                                loggedMoods.append("چرب");
                                break;
                            case 3:
                                loggedMoods.append(hasFirst ? " ، " : "");
                                hasFirst = true;
                                loggedMoods.append("خشک");
                                break;
                        }
                    }
                }

                if (reportWeight && dayMoodList.get(i).getWeight() != 0) {
                    loggedMoods.append(loggedMoods.length() > 0 ? "\n" : "");
                    loggedMoods.append("وزن: ");
                    loggedMoods.append(dayMoodList.get(i).getWeight());
                    addCell = true;
                }

                if (reportDrugs && dayMoodList.get(i).getDrugs() != null && !dayMoodList.get(i).getDrugs().isEmpty()) {
                    loggedMoods.append(loggedMoods.length() > 0 ? "\n" : "");
                    loggedMoods.append("داروهای مصرفی: ");
                    addCell = true;
                    for (int j = 0; j < dayMoodList.get(i).getDrugs().size(); j++) {
                        loggedMoods.append(dayMoodList.get(i).getDrugs().get(j));
                        if (j < dayMoodList.get(i).getDrugs().size() - 1) {
                            loggedMoods.append(" ، ");
                        }
                    }
                }

                if (addCell) {
                    table.addCell(date);
                    table.addCell(cell);

                    phrase = new Phrase(loggedMoods.toString(), f);
                    for (Chunk ch : phrase.getChunks()) {
                        ch.setLineHeight(20);
                    }
                    cell = new PdfPCell(phrase);
                    cell.setPadding(10);
                    cell.setColspan(2);
                    cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                    table.addCell(cell);
                }
            }
            document.add(table);

            document.add(new Paragraph(""));
            document.add(new Chunk(lineSeparator));
            document.add(new Paragraph(""));

            document.close();

            openFile(activity, path);
        }
    }

    private static int getDayType(CycleData cycleData, Calendar date) {
        if (cycleData == null) {
            return BaseMonthView.TYPE_GRAY;
        }
        long days;
        int day;
        days = CalendarTool.getDaysFromDiff(date, cycleData.getStartDate());
        Log.d("debug13", "start: " + CalendarTool.GregorianToPersian(cycleData.getStartDate()).getPersianLongDate());
        Log.d("debug13", "date: " + CalendarTool.GregorianToPersian(date));
        if (days >= 0) {
            day = (int) ((days) % cycleData.getTotalDays()) + 1;
        } else {
            return BaseMonthView.TYPE_GRAY;
        }
        if (day <= cycleData.getRedCount()) {
            return BaseMonthView.TYPE_RED;
        } else if (day <= cycleData.getTotalDays()) {
            if (day >= cycleData.getGreenStartIndex() && day <= cycleData.getGreenEndIndex()) {
                if (day == cycleData.getGreen2Index()) {
                    return BaseMonthView.TYPE_GREEN2;
                }
                return BaseMonthView.TYPE_GREEN;
            } else if (day >= cycleData.getYellowStartIndex() && day <= cycleData.getYellowEndIndex()) {
                return BaseMonthView.TYPE_YELLOW;
            }
        }
        return BaseMonthView.TYPE_GRAY;
    }

    private static void openFile(@NonNull Activity activity, String path) {
        File file = new File(path);

        Intent install = new Intent(Intent.ACTION_VIEW);
        install.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        Uri apkURI = FileProvider.getUriForFile(activity, activity.getApplicationContext().getPackageName() + ".provider", file);
        install.setDataAndType(apkURI, "application/pdf");
        install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            activity.startActivity(install);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

}
