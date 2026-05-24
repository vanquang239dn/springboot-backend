package vn.vanquang239dn.service.impl;

import java.time.Instant;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.vanquang239dn.repository.RefreshTokenRepository;
import vn.vanquang239dn.service.SecurityEventService;

@Service
@RequiredArgsConstructor
@Slf4j
public class SecurityEventServiceImpl implements SecurityEventService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void revokeBySessionId(String sessionId, Instant now, String reason) {

        refreshTokenRepository.revokeBySessionID(sessionId, now, reason);
    }
}
