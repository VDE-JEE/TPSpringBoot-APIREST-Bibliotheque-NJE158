package vde.kennet.libraryofk.Repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vde.kennet.libraryofk.Models.Livre;

@Repository
public interface BookRepository extends CrudRepository<Livre, String> {

}
