package model;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import negocio.ViewPdfActivity;

import com.example.smcontrol.R;

public class PDF {

    private Context context;
    private File pdfFile;
    private Document document;
    private PdfWriter pdfwriter;
    private Paragraph paragraph;
    private Font ftittle = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD);
    private Font fSubtittle = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    private Font fText = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD);
    private Font fHightittle = new Font(Font.FontFamily.TIMES_ROMAN, 15, Font.BOLD, BaseColor.RED);

    public PDF(Context context) {
        this.context = context;
    }

    public void openDocument() {
        createPDF();
        try {
            document = new Document(PageSize.A4);
            pdfwriter = PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
            addFooter();
            document.open();
            addHeader();
        } catch (Exception e) {
            Log.e("openDocument", e.toString());
        }
    }

    public void addFooter() throws IOException, DocumentException {
        Font footerN = new Font(baseFont(), 15,Font.BOLD,new BaseColor(65,168,136));
        Font footerE = new Font(baseFont(), 12,Font.NORMAL,BaseColor.BLACK);

        PdfPTable tableFooter = new PdfPTable(1);
        tableFooter.setTotalWidth(523);

        PdfPCell footerName = new PdfPCell(new Phrase("SMCONTROL",footerN));
        PdfPCell footerEmail = new PdfPCell(new Phrase("info@smcontrol.com",footerE));

        PdfPCell footerEmpty = new PdfPCell(new Phrase(""));

        footerName.setBorder(Rectangle.NO_BORDER);
        footerEmpty.setBorder(Rectangle.NO_BORDER);
        footerEmail.setBorder(Rectangle.NO_BORDER);

        PdfPCell preBorderBlue = new PdfPCell(new Phrase(""));
        preBorderBlue.setMinimumHeight(5f);
        preBorderBlue.setUseVariableBorders(true);
        preBorderBlue.setBorder(Rectangle.TOP);
        preBorderBlue.setBorderColorTop(new BaseColor(65,168,136));
        preBorderBlue.setBorderWidthTop(3);
        tableFooter.addCell(preBorderBlue);
        tableFooter.addCell(footerName);
        tableFooter.addCell(footerEmail);

        pdfwriter.setPageEvent(new MyPageEventListener(tableFooter));
    }

    public void addHeader() throws IOException, DocumentException {
        Font regularReport = new Font(baseFont(), 30,Font.BOLD,new BaseColor(65,168,136));
        PdfPCell preReport = new PdfPCell(new Phrase("REPORTE",regularReport));

        preReport.setHorizontalAlignment(Element.ALIGN_RIGHT);
        preReport.setVerticalAlignment(Element.ALIGN_CENTER);
        preReport.setBorder(Rectangle.NO_BORDER);
        PdfPTable tableHeader = new PdfPTable(2);
        tableHeader.setWidthPercentage(100);

        try {
            Drawable d = context.getResources().getDrawable(R.drawable.smcontrol_logo);
            BitmapDrawable bitDw = ((BitmapDrawable) d);
            Bitmap bmp = bitDw.getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Image image = Image.getInstance(stream.toByteArray());
            //image.scaleToFit(10,10);
            PdfPCell preImage = new PdfPCell(image, true);
            preImage.setHorizontalAlignment(Element.ALIGN_LEFT);
            preImage.setFixedHeight(100);
            preImage.setBorder(Rectangle.NO_BORDER);

            tableHeader.addCell(preImage);
            tableHeader.addCell(preReport);

            document.add(tableHeader);

        } catch (BadElementException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

    }

    private void createPDF() {
        File folder = new File(Environment.getExternalStorageDirectory().toString(), "PDF");
        if (!folder.exists())
            folder.mkdirs();
        pdfFile = new File(folder, new Foto().autogenerarCodigo("reporte","pdf"));
    }

    public void closeDocument() {
        document.close();
    }

    public void addMetaData(String tittle, String subject, String author) {
        document.addTitle(tittle);
        document.addSubject(subject);
        document.addAuthor(author);
    }

    public void addTittles(String tittle, String subtittle, String date) throws IOException, DocumentException {
        Font ftittle = new Font(baseFont(), 20, Font.BOLD);
        Font fSubtittle = new Font(baseFont(), 18, Font.BOLD);
        Font fHightittle = new Font(baseFont(), 15, Font.BOLD, BaseColor.RED);

        try {
            paragraph = new Paragraph();
            addChildP(new Paragraph(tittle, ftittle));
            addChildP(new Paragraph(subtittle, fSubtittle));
            addChildP(new Paragraph("Generado: " + date, fHightittle));
            paragraph.setSpacingAfter(15);
            document.add(paragraph);
        } catch (Exception e) {
            Log.e("addTittles", e.toString());
        }
    }

    private void addChildP(Paragraph childParagraph) {
        childParagraph.setAlignment(Element.ALIGN_CENTER);
        paragraph.add(childParagraph);
    }

    public void addParagraph(String text) {
        try {
            paragraph = new Paragraph(text, fText);
            paragraph.setSpacingAfter(5);
            paragraph.setSpacingBefore(5);
            document.add(paragraph);
        } catch (Exception e) {
            Log.e("addParagraph", e.toString());
        }
    }

    public void createTable(String[] header, ArrayList<String[]> clients) {
        try {
            Font fText = new Font(baseFont(), 11, Font.BOLD);
            paragraph = new Paragraph();
            paragraph.setFont(fText);
            PdfPTable pdfPTable = new PdfPTable(header.length);
            pdfPTable.setWidthPercentage(100);
            pdfPTable.setSpacingBefore(30);
            PdfPCell pdfPCell;
            int indexC = 0;
            while (indexC < header.length) {
                pdfPCell = new PdfPCell(new Phrase(header[indexC++], new Font(baseFont(), 14, Font.BOLD, BaseColor.WHITE)));
                pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfPCell.setBackgroundColor(new BaseColor(65,168,136));
                pdfPTable.addCell(pdfPCell);
            }
            for (int indexR = 0; indexR < clients.size(); indexR++) {
                String[] row = clients.get(indexR);
                for (indexC = 0; indexC < header.length; indexC++) {
                    pdfPCell = new PdfPCell(new Phrase(row[indexC]));
                    pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    pdfPCell.setFixedHeight(40);
                    pdfPTable.addCell(pdfPCell);
                }
            }

            paragraph.add(pdfPTable);
            document.add(paragraph);
        } catch (Exception e) {
            Log.e("createTable", e.toString());
        }
    }

    public void viewPDF() {
        Intent intent = new Intent(context, ViewPdfActivity.class);
        intent.putExtra("patch", pdfFile.getAbsoluteFile().toString());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public BaseFont baseFont() throws IOException, DocumentException {
        BaseFont baseFont = null;
        baseFont = BaseFont.createFont("res/font/reem_kufii.ttf", "UTF-8",BaseFont.EMBEDDED);
        return baseFont;
    }
}