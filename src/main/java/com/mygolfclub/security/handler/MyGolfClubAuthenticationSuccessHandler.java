package com.mygolfclub.security.handler;

import com.mygolfclub.entity.user.User;
import com.mygolfclub.service.user.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.mygolfclub.utils.constants.ConstantProvider.HOME;

@Component
public class MyGolfClubAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final UserService userService;

    @Autowired
    public MyGolfClubAuthenticationSuccessHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        String username = authentication.getName();
        User byUserName = userService.findByUserName(username);

        // CREATING USER SESSION
        HttpSession session = request.getSession();
        session.setAttribute("user", byUserName);

        // HOME PAGE FORWARDING
        response.sendRedirect(request.getContextPath() + HOME);
    }
}
