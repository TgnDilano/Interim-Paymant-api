package com.iwomi.service;

import com.iwomi.model.AuthorizedAccounts;
import com.iwomi.model.ServiceAccounts;
import com.iwomi.repository.UserRepository;
import java.sql.Timestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;

@Service
public class JwtUserDetailsService implements UserDetailsService {
	@Autowired
	private UserRepository authorizedUser;

	@Autowired
	private PasswordEncoder bcryptEncoder;

	@Override
	public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
		AuthorizedAccounts user = authorizedUser.findByName(name);
		if (user == null) {
			throw new UsernameNotFoundException("Client not found with username: " + name);
		}
		return new org.springframework.security.core.userdetails.User(user.getName(), user.getPwd(),
				new ArrayList<>());
	}

	public AuthorizedAccounts save(ServiceAccounts user) {
		AuthorizedAccounts newUser = new AuthorizedAccounts();
		newUser.setName(user.getName());
		newUser.setPwd(bcryptEncoder.encode(user.getPwd()));
                newUser.setCode(user.getCode());
                newUser.setCrdt(new Date());
                newUser.setMdt(new Date());
                newUser.setDescp(user.getDescp());
                
		return authorizedUser.save(newUser);
	}

    public AuthorizedAccounts findRoleByUserDetails(String username) {
        AuthorizedAccounts user = authorizedUser.findByName(username);
        return user;
    }
}