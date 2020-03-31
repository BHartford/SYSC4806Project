package Model;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceiptRepository extends CrudRepository<Receipt, Long> {

    List<Receipt> findByUser(@Param("user") User user);

    List<Receipt> findByDate(@Param("date") Date date);

    Receipt findById(long id);


}
