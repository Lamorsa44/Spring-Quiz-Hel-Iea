package engine.business;


import engine.business.DTO.RegisterRequest;
import engine.business.exceptions.InvalidFieldModException;
import engine.business.exceptions.NotFoundModException;
import engine.model.MyUser;
import engine.repository.MyUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MyUserService implements UserDetailsService {

    private final MyUserRepository myUserRepository;
    private final PasswordEncoder passwordEncoder;

    public MyUserService(MyUserRepository myUserRepository, PasswordEncoder passwordEncoder) {
        this.myUserRepository = myUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void saveUser(RegisterRequest registerRequest) {
        if (myUserRepository.existsByEmail(registerRequest.email())) {
            throw new InvalidFieldModException("Email is already in use");
        }

        myUserRepository.save(new MyUser(
                registerRequest.email(),
                passwordEncoder.encode(registerRequest.password())
        ));
    }

    public List<MyUser> getAllUsersYia() {
        return myUserRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new MyUserAdapter(myUserRepository.findByEmail(username)
                .orElseThrow(() -> new NotFoundModException("No user found with username " + username)));
    }
}
