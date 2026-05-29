package vn.vanquang239dn.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.vanquang239dn.model.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsername(String username);

    UserEntity findByEmail(String email);

    boolean existsByEmailAndUserIdNot(String email, long userId);

    @Query("""
            SELECT u
            FROM UserEntity u
            WHERE u.status = 'NONE'
              AND (
                    LOWER(u.firstName) LIKE LOWER(CONCAT('%', :keyword, '%'))
                 OR LOWER(u.lastName)  LIKE LOWER(CONCAT('%', :keyword, '%'))
                 OR LOWER(u.email)     LIKE LOWER(CONCAT('%', :keyword, '%'))
                 OR LOWER(u.phone)     LIKE LOWER(CONCAT('%', :keyword, '%'))
              )
            """)
    Page<UserEntity> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
