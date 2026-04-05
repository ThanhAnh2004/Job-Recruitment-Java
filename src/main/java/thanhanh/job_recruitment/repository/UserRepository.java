package thanhanh.job_recruitment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import thanhanh.job_recruitment.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
