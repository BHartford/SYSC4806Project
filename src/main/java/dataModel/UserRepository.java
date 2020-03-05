package dataModel;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {

    List<User> findByUsername(@Param("username") String username);

    List<User> findByTypeOfUser(@Param("typeOfUser") int typeOfUser);

    User findById(long id);
}
