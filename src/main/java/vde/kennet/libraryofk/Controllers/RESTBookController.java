package vde.kennet.libraryofk.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vde.kennet.libraryofk.Models.Lecteur;
import vde.kennet.libraryofk.Models.Livre;
import vde.kennet.libraryofk.Service.BookService;

import java.util.Optional;

@RestController
public class RESTBookController {
    @Autowired private BookService bookService;

    @GetMapping ("/books")
    public Iterable<Livre> getBooks() {return bookService.getLivres();}

    @GetMapping("/books/{ISBN}")
    public Livre getBook (@PathVariable("ISBN") String ISBN) { return bookService.getLivre(ISBN);}

    @PutMapping("/books")
    public Livre putBook (Livre livre) { return bookService.saveLivre(livre); }

    @PostMapping("/books")
    public Livre postBook (Livre livre) { return bookService.saveLivre(livre); }

    @PutMapping("/books/{ISBN}")
    public Livre putBook (@PathVariable("ISBN") String ISBN) {
        Livre livre = bookService.getLivre(ISBN);
        return bookService.saveLivre(livre);}

    @DeleteMapping("/books/{ISBN}")
    public void deleteBook (@PathVariable("ISBN") String ISBN) { bookService.deleteLivre(ISBN);}

    @GetMapping("/books/{ISBN}/users")
    public Lecteur getBorrower (@PathVariable("ISBN") String ISBN) { return bookService.quiEmprunte(bookService.getLivre(ISBN));}
}
