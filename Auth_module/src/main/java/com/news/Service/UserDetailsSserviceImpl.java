package com.news.Service;

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


    private static Logger logger = LoggerFactory.getLogger(UserDetailsSserviceImpl.class);

    @Autowired
    private UserRepo repo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user1 = repo.findByUsername(username);
        if(user1 == null){
            logger.info("user doesn't exist");
            throw new UsernameNotFoundException("username not found");
        }
        return new UserDetailsImpl(user1);
    }
}
