package vn.vanquang239dn.model.claims;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AccessTokenClaims implements Serializable {

        private final String username;

        private final List<String> authorities;

}
