package vn.vanquang239dn.service;

import java.time.Instant;

public interface SecurityEventService {

    public void revokeBySessionId(String sessionId, Instant now, String reason);

}
