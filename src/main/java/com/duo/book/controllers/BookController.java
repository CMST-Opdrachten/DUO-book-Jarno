package com.duo.book.controllers;

import com.duo.book.objects.Book;
import com.duo.book.service.BookService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

@RestController
public class BookController {

    private final BookService service;

    private static final String FILEPATH = "src\\pdfFiles\\";
    private static final  String FILENAME = "BooksPdf";

    BookController(BookService service)
    {
        this.service = service;
    }

    @GetMapping(value = "/api/boeken")
    public List<Book> findAllBook()
    {
        return  service.findAllThread();
    }

    @GetMapping(value = "/api/boeken/{id}")
    public Book getAllBooks(@PathVariable Long id)
    {
        return  service.findBookThread(id);
    }

    @PostMapping("/api/boeken")
    public Book addBooks(@RequestBody Book newBook)
    {
        return service.addBookThread(newBook);
    }

    @DeleteMapping("/api/boeken/{id}")
    public void deleteBooks(@PathVariable Long id)
    {
        service.deleteBookThread(id);
    }

    @PutMapping("api/boeken/{id}")
    public Book putBooks(@RequestBody Book newBook, @PathVariable Long id)
    {
        return service.putBookThread(id, newBook);
    }

    //Get all books and makes a pdf of the data in a table
    @GetMapping("/getBookPdf")
    public void createPDF(HttpServletResponse response) throws IOException, DocumentException
    {
        response.setContentType("application/pdf");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename =" + FILENAME+ ".pdf";
        response.setHeader(headerKey,headerValue);

        String timeStamp = new SimpleDateFormat("dd-MM-yyyy").format(new Date());

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(FILEPATH + FILENAME+ "-" + timeStamp + ".pdf"));
        PdfWriter.getInstance(document, response.getOutputStream());
        
        document.open();
        PdfPTable table = new PdfPTable(3);
        addTableHeader(table);
        addRows(table);

        document.add(table);
        document.close();

    }

    //Creates the header titels for the pdf table
    private void addTableHeader(PdfPTable table)
    {
        Stream.of("id", "titel", "uitgever")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(1);
                    header.setPhrase(new Phrase(columnTitle));
                    table.addCell(header);
                });
    }

    //Adds the data to the table
    private void addRows(PdfPTable table)
    {
        List<Book> books = service.findAllThread();
        for (Book book : books)
        {
            table.addCell(book.getId().toString());
            table.addCell(book.getTitel());
            table.addCell(book.getUitgever());
        }
    }


}
