package vde.kennet.libraryofk.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vde.kennet.libraryofk.Models.*;
import vde.kennet.libraryofk.Repository.BookRepository;

import java.util.Optional;

@Service
public class BookService {
/*
Récupérer un livre par son ISBN
Ajouter un nouveau livre
Mettre à jour l'état d'un livre existant
Supprimer un livre par son ISBN
 */
    @Autowired private BookRepository bookRepo;

    public Optional<Livre> isLivre (final String ISBN) {return bookRepo.findById(ISBN); }
    public Livre getLivre (final String ISBN) {
        if (isLivre(ISBN).isPresent()) return isLivre(ISBN).get();
        else return isLivre(ISBN).orElse(null);
    }
    public Iterable<Livre> getLivres () {return bookRepo.findAll();}
    public Livre           saveLivre (Livre livre) {return bookRepo.save(livre);}
    public void            deleteLivre (final String ISBN) { bookRepo.deleteById(ISBN);}

    public Lecteur         quiEmprunte (Livre livre) {
        if (livre.getEtat() != EtatLivre.DISPONIBLE) {
            return livre.getEmprunts().get(livre.getEmprunts().size()-1).getLecteur();
        } else return null;
    }
}
