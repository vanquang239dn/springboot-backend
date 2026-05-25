package vn.vanquang239dn.model.entity;

import java.io.Serializable;
import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Index;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AccessLevel;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "role_has_permission", uniqueConstraints = {
                @UniqueConstraint(name = "uk_role_has_permission_role_permission", columnNames = { "role_id",
                                "permission_id" })
}, indexes = {
                @Index(name = "idx_role_has_permission_role_id", columnList = "role_id"),
                @Index(name = "idx_role_has_permission_permission_id", columnList = "permission_id")
})
public class RoleHasPermissionEntity implements Serializable {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "role_permission_id")
        private Long rolePermissionId;

        @Column(name = "role_id", nullable = false, unique = false)
        private Long roleId;

        @Column(name = "permission_id", nullable = false, unique = false)
        private Long permissionId;

        @CreationTimestamp
        @Column(name = "created_at", nullable = false, updatable = false)
        private Instant createdAt;

        @UpdateTimestamp
        @Column(name = "updated_at", nullable = false, updatable = true)
        private Instant updatedAt;

}
