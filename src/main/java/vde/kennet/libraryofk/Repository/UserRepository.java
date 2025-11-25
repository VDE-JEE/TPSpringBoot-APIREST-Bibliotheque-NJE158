package vde.kennet.libraryofk.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vde.kennet.libraryofk.Models.Lecteur;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<Lecteur,Integer> {
    Optional<Lecteur> findByMail(String mail);
    @Query("SELECT u FROM Lecteur u WHERE u.password = :hash")
    Optional<Lecteur> findByPasswordHash(@Param("hash") String hash);

}
