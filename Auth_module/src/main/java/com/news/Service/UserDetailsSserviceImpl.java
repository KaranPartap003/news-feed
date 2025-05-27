package com.news.Service;

import com.news.Model.UserDetailsImpl;
import com.news.Model.Users;
import com.news.Repository.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsSserviceImpl implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserDetailsSserviceImpl.class);

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepo.findByUsername(username);
        if (user == null) {
            logger.info("username not found");
            throw new UsernameNotFoundException("user not found");
        }
        return new UserDetailsImpl(user);
    }
}
