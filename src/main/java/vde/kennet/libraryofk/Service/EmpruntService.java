package vde.kennet.libraryofk.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import vde.kennet.libraryofk.Models.*;
import vde.kennet.libraryofk.Repository.EmpruntRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class EmpruntService {
    @Autowired LecteurService lService;
    @Autowired BookService bService;
    @Autowired private EmpruntRepository eRepo;

    public Iterable<Livre> getBooksEmpruntes (final Integer idLecteur) {
        return lService.getLecteur(idLecteur).get().getEmprunts().stream()
                .map(Emprunt::getLivre)   //on est passé de Emprunt à Livre
                .filter(livre -> livre.getEtat()!=EtatLivre.DISPONIBLE)
                .toList()
                ;
    }

    public boolean emprunterLivre (Lecteur lecteur, Livre livre, LocalDate retourAttendu) {
        boolean livreDispo = false;
        if (livre.getEtat()== EtatLivre.DISPONIBLE) {
            Emprunt emprunt =new Emprunt(lecteur, livre, retourAttendu);

            System.out.println("Le livre"+ emprunt.getLivre().getTitre()
                    + " a été " + emprunt.getLivre().getEtat()
                    + " par " + emprunt.getLecteur().getPrenomLecteur()
                    + " jusqu'au " + emprunt.getDateRetourAttendue());
            //le livre est déclaré comme emprunté dans la construction de l'objet
            livre.setEtat(EtatLivre.EMPRUNTE);
            eRepo.save(emprunt);
            bService.saveLivre(livre);
            livreDispo = true;
            System.out.println("Le livre a été "+ livre.getEtat() + " avec succès.");
        }else {
            System.out.println("Le livre n'est pas disponible.");
        }
        return livreDispo;
    }

    public void retournerLivre (Emprunt emprunt) {
        emprunt.getLivre().setEtat(EtatLivre.DISPONIBLE);
        bService.saveLivre(emprunt.getLivre());
        emprunt.setDateRenduReelle(LocalDate.now());
        System.out.println("Livre retourné avec succès");
        eRepo.save(emprunt);
    }

    public Emprunt thisEmprunt (int id, String ISBN) {
        Lecteur lecteur = lService.getLecteur(id).get();
        Livre livre = bService.getLivre(ISBN);
        List<Emprunt> emprunts = livre.getEmprunts().stream()
                        .filter(emprunt -> emprunt.getLivre().getEtat()!=EtatLivre.DISPONIBLE) .toList();

        System.out.println("emprunts de " + lecteur.getPrenomLecteur() + " : " + lecteur.getEmprunts().stream()
                .filter(emprunt -> emprunt.getLivre().getEtat()!=EtatLivre.DISPONIBLE).toList() .size());
        Emprunt e = emprunts.get(emprunts.size()-1);
        System.out.println("taille emprunts : " + emprunts.size()); //return emprunts.get(emprunts.size()-1);
        return e;
    }


    public void allongerEmprunt (Emprunt emprunt, LocalDate dateEtendue) {
        emprunt.setDateRetourAttendue(dateEtendue);
        eRepo.save(emprunt);
    }

    @PersistenceContext @Autowired private EntityManager entityManager;

    //Ceci va s'exécuter chaque 0s, 30min, 8h de *jours, *mois, *jourdelasemaine
    //@Scheduled(cron = "0 30 8 * * *")
    @Scheduled(cron = "5 * * * * *")
    public void retardRetourEmprunt () {
        List<Emprunt> emprunts = (List<Emprunt>) eRepo.findAll();
        //Liste d'emprunts, j'ai créé une requete avec la classe en classe, et j'ai demandé le result en list
        System.out.println("Emprunts : " + emprunts.size());

       List<Emprunt> retards = emprunts.stream() //un petit stream sur notre liste d'emprunts
                .filter(emprunt -> emprunt.getLivre().getEtat()==EtatLivre.EMPRUNTE)
                //je veux que les empruntés
                .filter(emprunt -> emprunt.getDateRetourAttendue().isBefore(LocalDate.now()))
                //je veux que les emprunts qui ont une date de retour dépassée
                .toList();

       List<Livre> livresEnRetard = retards.stream().map(Emprunt::getLivre).toList();
        System.out.println("Livres en retard : " + livresEnRetard.size());
        livresEnRetard.stream().forEach(livre -> livre.setEtat(EtatLivre.EN_RETARD));
                //on les change tous en EN_RETARD
        livresEnRetard.stream().forEach(livre -> bService.saveLivre(livre));
    }
}
