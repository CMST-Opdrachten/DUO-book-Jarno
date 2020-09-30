package com.duo.book.controllers;

import com.duo.book.BookNotFoundExceptoin;
import com.duo.book.objects.Book;
import com.duo.book.repositories.BookRepository;
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

    public BookRepository repository;

    private static String filepath = "src\\pdfFiles\\";
    private static String filename = "BooksPdf";

    BookController(BookRepository repository)
    {
        this.repository = repository;
    }

    //get all books
    @GetMapping("/api/boeken")
    public List<Book> allBooks()
    {
        return repository.findAll();
    }

    //get single book by id
    @GetMapping("/api/boeken/{id}")
    public Book findBookByID(@PathVariable Long id)  {
        return repository.findById(id).orElseThrow(() -> new BookNotFoundExceptoin(id));
    }

    //add new book
    @PostMapping("/api/boeken")
    public Book addBook(@RequestBody Book newBook)
    {
       return repository.save(newBook);
    }

    //Update book by id
    @PutMapping("api/boeken/{id}")
    public Book updateBook(@RequestBody Book newBook, @PathVariable Long id)
    {
        return repository.findById(id)
                .map(book -> {
                    book.setTitel(newBook.getTitel());
                    book.setUitgever(newBook.getUitgever());
                    return repository.save(book);
                })
                .orElseGet(() -> {
                    newBook.setId(id);
                    return repository.save(newBook);
                });
    }

    //delete book with id
    @DeleteMapping("api/boeken/{id}")
    public void deleteBook(@PathVariable Long id)
    {
        repository.deleteById(id);
    }

    //Get all books and makes a pdf of the data in a table
    @GetMapping("/getBookPdf")
    public void createPDF(HttpServletResponse response) throws IOException, DocumentException
    {
        response.setContentType("application/pdf");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename =" + filename+ ".pdf";
        response.setHeader(headerKey,headerValue);

        String timeStamp = new SimpleDateFormat("dd-MM-yyyy").format(new Date());

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(filepath + filename+ "-" + timeStamp + ".pdf"));
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
        for(int i = 0; i < (repository.findAll().size()); i++  )
        {
            table.addCell(repository.findAll().get(i).getId().toString());
            table.addCell(repository.findAll().get(i).getTitel());
            table.addCell(repository.findAll().get(i).getUitgever());
        }
    }

}
