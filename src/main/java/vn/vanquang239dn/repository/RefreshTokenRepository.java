package vn.vanquang239dn.repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;
import vn.vanquang239dn.model.entity.RefreshTokenEntity;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, UUID> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<RefreshTokenEntity> findByJwtId(String jwtId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = """
            UPDATE refresh_tokens
            SET revoked = true,
                revoked_at = :revokedAt,
                revoke_reason = :reason
            WHERE session_id = :sessionId
              AND revoked = false
            """, nativeQuery = true)
    int revokeBySessionID(
            @Param("sessionId") String sessionId,
            @Param("revokedAt") Instant revokedAt,
            @Param("reason") String reason);

}
