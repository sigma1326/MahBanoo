package com.simorgh.reportutil;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.simorgh.databaseutils.model.Cycle;

import java.io.File;
import java.io.FileOutputStream;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

public class ReportUtils {
    public static void createReport(@NonNull Activity activity, @NonNull Cycle cycle) {
        try {
            Document document = new Document();

            String path = Environment.getExternalStorageDirectory() + "/" + cycle.getBirthYear() + ".pdf";

            // Location to save
            PdfWriter.getInstance(document, new FileOutputStream(path));

            // Open to write
            document.open();

            // Document Settings
            document.setPageSize(PageSize.A4);
            document.addCreationDate();
            document.addAuthor("Red Calendar");
            document.addCreator("Red Calendar");

            BaseColor mColorAccent = new BaseColor(0, 153, 204, 255);
            float mHeadingFontSize = 20.0f;
            float mValueFontSize = 26.0f;
//        BaseFont urName = BaseFont.createFont("fonts/brandon_medium.otf", "UTF-8", BaseFont.EMBEDDED);
            BaseFont urName = BaseFont.createFont(BaseFont.COURIER_OBLIQUE, "UTF-8", BaseFont.EMBEDDED);


            // LINE SEPARATOR
            LineSeparator lineSeparator = new LineSeparator();
            lineSeparator.setLineColor(new BaseColor(0, 0, 0, 68));


            // Title Order Details...
            // Adding Title....
            Font mOrderDetailsTitleFont = new Font(urName, 36.0f, Font.NORMAL, BaseColor.BLACK);
            // Creating Chunk
            Chunk mOrderDetailsTitleChunk = new Chunk("Report", mOrderDetailsTitleFont);
            // Creating Paragraph to add...
            Paragraph mOrderDetailsTitleParagraph = new Paragraph(mOrderDetailsTitleChunk);
            // Setting Alignment for Heading
            mOrderDetailsTitleParagraph.setAlignment(Element.ALIGN_CENTER);
            // Finally Adding that Chunk
            document.add(mOrderDetailsTitleParagraph);

            // Fields of Order Details...
            // Adding Chunks for Title and value
            Font mOrderIdFont = new Font(urName, mHeadingFontSize, Font.NORMAL, mColorAccent);
            for (int i = 0; i < 100; i++) {
                Chunk mOrderIdChunk = new Chunk("" + (i + 1), mOrderIdFont);
                Paragraph mOrderIdParagraph = new Paragraph(mOrderIdChunk);

                document.add(mOrderIdParagraph);
            }

            document.add(new Paragraph(""));
            document.add(new Chunk(lineSeparator));
            document.add(new Paragraph(""));

            document.close();

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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
