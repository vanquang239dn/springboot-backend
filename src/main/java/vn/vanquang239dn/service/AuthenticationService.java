package vn.vanquang239dn.service;

import vn.vanquang239dn.dto.request.SignInRequest;
import vn.vanquang239dn.dto.response.TokenResponse;

public interface AuthenticationService {

    TokenResponse authenticate(SignInRequest signInRequest);

    TokenResponse refreshToken(String refreshToken);
}
