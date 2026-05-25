package vn.vanquang239dn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.vanquang239dn.model.entity.PermissionEntity;

@Repository
public interface PermissionRepository extends JpaRepository<PermissionEntity, Long> {

       @Query(value = """
                     SELECT DISTINCT p.permission
                     FROM permissions p

                     JOIN role_has_permission rp
                          ON rp.permission_id = p.permission_id

                     JOIN roles r
                          ON r.role_id = rp.role_id

                     LEFT JOIN user_has_role ur
                            ON ur.role_id = r.role_id

                     LEFT JOIN group_has_role gr
                            ON gr.role_id = r.role_id

                     LEFT JOIN group_has_user gu
                            ON gu.group_id = gr.group_id

                     WHERE ur.user_id = :userId
                        OR gu.user_id = :userId
                     """, nativeQuery = true)
       List<String> findAllPermissionsByUserId(@Param("userId") Long userId);
}
