package Model;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends CrudRepository<Book, Long> {

    List<Book> findByTitle(@Param("title") String title);

    List<Book> findByAuthor(@Param("author") String author);

    List<Book> findByIdIn(@Param("ids") Long[] ids);

    Book findById(long id);
}
