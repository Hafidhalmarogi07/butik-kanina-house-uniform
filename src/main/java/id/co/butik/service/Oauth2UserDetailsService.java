package id.co.butik.service;

import id.co.butik.entity.users.User;
import id.co.butik.repository.UserRepository;
import id.co.butik.util.CacheObjectInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@EnableCaching
public class Oauth2UserDetailsService implements UserDetailsService, CacheObjectInterface {

    @Autowired
    private UserRepository userRepository;

    @Cacheable(value = "oauth_username", unless = "#result == null")
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.findOneByUsername(s);
        if (null == user) {
            throw new UsernameNotFoundException(String.format("Username %s is not found", s));
        }

        return user;
    }

    @CacheEvict("oauth_username")
    public void clearCache(String s) {
    }

    @CacheEvict(value = "oauth_username", allEntries = true)
    @Override
    public void clearCache() {

    }
}
