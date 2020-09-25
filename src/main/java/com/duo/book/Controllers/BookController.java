package com.duo.book.Controllers;

import com.duo.book.Objects.Book;
import com.duo.book.Repositories.BookRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@RestController
public class BookController {

    private BookRepository repository;
    public Book noBook;

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
    public Optional<Book> findBookByID(@PathVariable Long id)
    {
        return repository.findById(id);
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
    @GetMapping("api/boeken/pdf")
    public void createPDF() throws IOException, DocumentException
    {

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("test.pdf"));

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
