package vde.kennet.libraryofk.Repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vde.kennet.libraryofk.Models.Lecteur;

@Repository
public interface UserRepository extends CrudRepository<Lecteur,Integer> {
}
