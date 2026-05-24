package vn.vanquang239dn.model.claims;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class RefreshTokenClaims {

    private final String jwtId;

    private final String sessionId;

}