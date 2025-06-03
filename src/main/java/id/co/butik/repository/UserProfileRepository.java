package id.co.butik.repository;

import id.co.butik.entity.users.UserProfile;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserProfileRepository extends PagingAndSortingRepository<UserProfile, Long>, JpaSpecificationExecutor<UserProfile> {

    UserProfile findFirstByEmail(String email);
}
