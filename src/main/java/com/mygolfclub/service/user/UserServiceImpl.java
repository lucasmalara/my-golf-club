package com.mygolfclub.service.user;

import com.mygolfclub.entity.user.User;
import com.mygolfclub.model.user.UserModel;
import com.mygolfclub.persistence.dao.user.UserDAO;
import com.mygolfclub.service.role.RoleService;
import com.mygolfclub.utils.mapping.user.UserMapper;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService, UserMapper {

    private final UserDAO userDao;
    private final RoleService roleService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserDAO userDao,
                           RoleService roleService,
                           BCryptPasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public @Nullable User findByUserName(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    public void save(UserModel userModel) {
        if (findByUserName(userModel.getUsername()) == null) {
            userModel.setPassword(passwordEncoder
                    .encode(userModel.getPassword()));
            userDao.save(mapTo(userModel));
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User byUsername = userDao.findByUsername(username);
        if (byUsername == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        // return User class identified by a package name to avoid confusion with User entity class
        // (com.mygolfclub.entity.user.User)
        return org.springframework.security.core.userdetails.User
                .builder()
                .username(byUsername.getUsername())
                .password(byUsername.getPassword())
                .authorities(roleService
                        .mapRolesToAuthorities(byUsername.getRoles()))
                .build();
    }

    @Override
    public User mapTo(UserModel userModel) {
        User user = new User();
        user.setUsername(userModel.getUsername());
        user.setPassword(userModel.getPassword());
        user.setRoles(roleService
                .getUserRoles(userModel.getRole())
                .stream()
                .map(roleService::findByName)
                .toList());
        user.setEnabled(true);
        return user;
    }
}
