package dataModel;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface ReceiptRepository extends CrudRepository<Receipt, Long> {

    List<Receipt> findByUser(@Param("user") User user);

    List<Receipt> findByDate(@Param("date") Date date);

    Receipt findById(long id);


}
