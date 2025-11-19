package repository;

import entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * JpaRepository automatically generate SQL to call data
 *
 * User: show that this is the repo for the User Entity
 * Long: show that the primary key (id) is Long
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

// this is a sample signature
//  /**
//   * Finding a user by their username
//   *
//   * @param username the unique username of the user
//   * @return an Optional containing the User if found, otherwise empty
//   */
//  Optional<User> findByUsername(String username);


}
