package com.iscsantoscastillo.ventapp;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

//import com.itextpdf.io.codec.TIFFFaxDecoder;
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
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

//import com.itextpdf.io.image.ImageData;
//import com.itextpdf.io.image.ImageDataFactory;
//import com.itextpdf.layout.element.Image;

public class TemplatePDF {
    private Context context;
    private File pdfFile;
    private Document document;
    private PdfWriter pdfWriter;
    private Paragraph paragraph;
    private Font fTitle = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD);
    private Font fSubTitle = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    private Font fText = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
    private Font fHighText = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, BaseColor.RED);



    private String imageFile;

    public void setImageLogo(String imageLogo) {
        this.imageLogo = imageLogo;
    }

    private String imageLogo;

    public String getNombrePDF() {
        return nombrePDF;
    }

    public void setNombrePDF(String nombrePDF) {
        this.nombrePDF = nombrePDF;
    }

    private String nombrePDF;

    public String getPieFirma() {
        return pieFirma;
    }

    public void setPieFirma(String pieFirma) {
        this.pieFirma = pieFirma;
    }

    private String pieFirma;

    public TemplatePDF(Context context) {
        this.context=context;
    }
    public void openDocument(){
        createFile();
        try{
            document=new Document(PageSize.A4);
            pdfWriter= PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
            document.open();
        }catch(Exception e){
            Log.e("openDocument",e.toString());
        }
    }
    private void createFile(){
        File folder = new File(Environment.getExternalStorageDirectory().toString(), "PDF");
        if(!folder.exists()){
            folder.mkdirs();
        }
        pdfFile = new File(folder, this.nombrePDF);
    }

    public void closeDocument(){
        document.close();
    }

    public void addMetaData(String title, String subject, String author){
        document.addTitle(title);
        document.addSubject(subject);
        document.addAuthor(author);
    }

    public void addTitles(String title, String subTitle, String date, String nombreCliente){
        try {
            paragraph = new Paragraph();
            addLogo();
            //addChildP(new Paragraph(title, fTitle));
            addChildP(new Paragraph(subTitle, fSubTitle));
            addChildP(new Paragraph("Fecha y hora " + date, fHighText));
            addChildP(new Paragraph(nombreCliente, fTitle));
            paragraph.setSpacingAfter(30);
            document.add(paragraph);

        }catch(Exception e){
            Log.e("addTitles",e.toString());
        }
    }

    private void addChildP(Paragraph childParagraph){
        childParagraph.setAlignment(Element.ALIGN_CENTER);
        paragraph.add(childParagraph);
    }

    public void addPieFirma(String str){
        addChildP(new Paragraph(str, fTitle));
    }

    public void addParagraph(String text){
        paragraph=new Paragraph(text, fText);
        paragraph.setSpacingAfter(5);
        paragraph.setSpacingBefore(5);
        try {
            document.add(paragraph);
        } catch (DocumentException e) {
            Log.e("addParagraph",e.toString());
        }
    }

    public void createTable(String[]header, ArrayList<String[]>clients){
        paragraph = new Paragraph();
        paragraph.setFont(fText);
        PdfPTable pdfPTable = new PdfPTable(header.length);
        pdfPTable.setWidthPercentage(100);
        PdfPCell pdfPCell;
        int indexC=0;
        while(indexC < header.length){
            pdfPCell=new PdfPCell(new Phrase(header[indexC++],fSubTitle));
            pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            pdfPCell.setBackgroundColor(BaseColor.GREEN);
            pdfPTable.addCell(pdfPCell);
        }

        for(int indexR=0; indexR< clients.size(); indexR++){
            String[] row = clients.get(indexR);
            for(indexC=0; indexC< header.length; indexC++){
                pdfPCell=new PdfPCell(new Phrase(row[indexC]));
                pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfPCell.setFixedHeight(20);
                pdfPTable.addCell(pdfPCell);
            }
        }

        paragraph.add(pdfPTable);

        try {
            document.add(paragraph);
            //Falta la leyenda FIRMA DEL CLIENTE
            addImage();
        } catch (DocumentException e) {
            Log.e("createTable",e.toString());
        }
    }

    public void addLogo(){
        try {
            Paragraph p = new Paragraph();
            //Paragraph p2 = new Paragraph(pieFirma, fText);
            //p2.setAlignment(Element.ALIGN_CENTER);
            Image img = Image.getInstance(imageLogo);
            img.scalePercent(50, 50); //scaleAbsolute(120f, 100f);
            img.setAlignment(Element.ALIGN_CENTER);
            p.add(img);
            //p.add(p2);
            document.add(p);
        } catch (BadElementException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    public void addImage(){
        try {
            Paragraph p = new Paragraph();
            Paragraph p2 = new Paragraph(pieFirma, fText);
            p2.setAlignment(Element.ALIGN_CENTER);
            Image img = Image.getInstance(imageFile);
            img.scaleAbsolute(120f, 100f);
            img.setAlignment(Element.ALIGN_CENTER);
            p.add(img);
            p.add(p2);
            document.add(p);
        } catch (BadElementException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    public String getImageFile() {
        return imageFile;
    }

    public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
    }

    public void viewPDF(){
        Intent intent=new Intent(context, ViewPDFActivity.class);
        intent.putExtra("path", pdfFile.getAbsolutePath());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
