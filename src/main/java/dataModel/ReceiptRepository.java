package dataModel;

import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface ReceiptRepository extends CrudRepository<Receipt, Long> {

    List<Receipt> findByUserId(long userId);

    List<Receipt> findAllByDate(Date Date);

    Receipt findByReceiptId(long id);


}
