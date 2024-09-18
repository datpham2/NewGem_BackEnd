package project.source.services;

import project.source.requests.SignInRequest;
import project.source.respones.TokenResponse;

public interface AuthService {
    public TokenResponse signIn(SignInRequest request);
}
