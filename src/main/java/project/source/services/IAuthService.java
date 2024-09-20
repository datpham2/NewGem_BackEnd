package project.source.services;

import project.source.requests.SignInRequest;
import project.source.respones.TokenResponse;

public interface IAuthService {
    public TokenResponse signIn(SignInRequest request);
}
