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
@Table(name = "group_has_role", uniqueConstraints = {
                @UniqueConstraint(name = "uk_group_has_role_group_role", columnNames = { "group_id", "role_id" })
}, indexes = {
                @Index(name = "idx_group_has_role_group_id", columnList = "group_id"),
                @Index(name = "idx_group_has_role_role_id", columnList = "role_id")
})
public class GroupHasRoleEntity implements Serializable {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "group_role_id")
        private Long groupRoleId;

        @Column(name = "group_id", nullable = false, unique = false)
        private Long groupId;

        @Column(name = "role_id", nullable = false, unique = false)
        private Long roleId;

        @CreationTimestamp
        @Column(name = "created_at", nullable = false, updatable = false)
        private Instant createdAt;

        @UpdateTimestamp
        @Column(name = "updated_at", nullable = false, updatable = true)
        private Instant updatedAt;

}