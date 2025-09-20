package org.example.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.entities.UserInfo;
import org.example.model.UsrInfoDto;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

@Component
@Data
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        UserInfo user = userRepository.findByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException("Could not found user...!");

            }
        return new CustomUserDetails(user);
    }

    public UserInfo checkIfUserAlreadyExist(UsrInfoDto usrInfoDto){
        return userRepository.findByUsername(usrInfoDto.getUsername());
    }

    public Boolean signupUser(UsrInfoDto usrInfoDto){
        usrInfoDto.setPassword(passwordEncoder.encode(usrInfoDto.getPassword()));
        if(Objects.nonNull((checkIfUserAlreadyExist(usrInfoDto)))){
            return false;
        }
        String userId = UUID.randomUUID().toString();
        userRepository.save(new UserInfo(userId, usrInfoDto.getUsername()
                ,usrInfoDto.getPassword(),new HashSet<>()));
        return true;
    }

}
