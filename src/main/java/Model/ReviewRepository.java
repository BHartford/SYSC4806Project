package Model;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends CrudRepository<Review, Long> {

	List<Review> findById(@Param("id") long id);
	
	List<Review> findByUserId(@Param("id") long id);
	
	List<Review> findByBookId(@Param("id") long id);
	
}
