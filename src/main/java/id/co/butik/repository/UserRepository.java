package id.co.butik.repository;

import id.co.butik.entity.users.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface UserRepository extends PagingAndSortingRepository<User, Long>, JpaSpecificationExecutor<User> {

    @Query("FROM User u WHERE LOWER(u.username) = LOWER(?1)")
    User findOneByUsername(String username);

    /**
     * Find a user by email
     * @param email the email address
     * @return the user if found
     */
    @Query("FROM User u WHERE LOWER(u.email) = LOWER(?1)")
    Optional<User> findByEmail(String email);
}
