package dataModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ReviewRepository extends CrudRepository<Review, Long> {

	List<Review> findById(@Param("id") long id);
	
	List<Review> findByUserId(@Param("id") long id);
	
	List<Review> findByBookId(@Param("id") long id);
	
}
