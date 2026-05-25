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
@Table(name = "group_has_user", uniqueConstraints = {
                @UniqueConstraint(name = "uk_group_has_user_group_user", columnNames = { "group_id", "user_id" })
}, indexes = {
                @Index(name = "idx_group_has_user_group_id", columnList = "group_id"),
                @Index(name = "idx_group_has_user_user_id", columnList = "user_id")
})
public class GroupHasUserEntity implements Serializable {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "group_user_id")
        private Long groupUserId;

        @Column(name = "group_id", nullable = false, unique = false)
        private Long groupId;

        @Column(name = "user_id", nullable = false, unique = false)
        private Long userId;

        @CreationTimestamp
        @Column(name = "created_at", nullable = false, updatable = false)
        private Instant createdAt;

        @UpdateTimestamp
        @Column(name = "updated_at", nullable = false, updatable = true)
        private Instant updatedAt;

}
