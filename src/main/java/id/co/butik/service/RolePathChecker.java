package id.co.butik.service;

import id.co.butik.entity.users.RolePath;
import id.co.butik.repository.RolePathRepository;
import id.co.butik.util.CacheObjectInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@EnableCaching
public class RolePathChecker implements CacheObjectInterface {

    @Autowired
    private RolePathRepository rolePathRepository;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Cacheable("oauth_role_path_checker")
    public <T extends UserDetails> boolean isAllow(T user, String xUri, String method) {
        if (StringUtils.isEmpty(xUri)) {
            return false;
        }

        List<RolePath> rolePaths = rolePathRepository.findByUser(user);

        if (rolePaths.size() < 1) {
            return false;
        }

        boolean allowed = false;

        try {
            for (RolePath path: rolePaths) {
                logger.info("Checking regex: {} {} with string: {} {}", path.getMethod(), path.getPattern(), method, xUri);
                if (xUri.matches(path.getPattern()) && path.getMethod().equalsIgnoreCase(method)) {
                    allowed = true;
                    break;
                }
            }
        } catch (Exception e) {
            logger.warn("Error: {}", e.getMessage());
        }

        return allowed;
    }

    @CacheEvict(value = "oauth_role_path_checker", allEntries = true)
    @Override
    public void clearCache() {
    }
}
