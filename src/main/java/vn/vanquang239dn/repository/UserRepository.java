package vn.vanquang239dn.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.vanquang239dn.model.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByUsername(String username);

    UserEntity findByEmail(String email);

    boolean existsByEmailAndIdNot(String email, long userId);

    @Query("""
                SELECT u
                FROM UserEntity u
                WHERE u.status = 'NONE'
                    AND (
                        lower(u.firstName) LIKE lower(concat('%', :keyword, '%'))
                     OR lower(u.lastName)  LIKE lower(concat('%', :keyword, '%'))
                     OR lower(u.email)     LIKE lower(concat('%', :keyword, '%'))
                     OR lower(u.phone)     LIKE lower(concat('%', :keyword, '%'))
                    )
            """)
    Page<UserEntity> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

}
