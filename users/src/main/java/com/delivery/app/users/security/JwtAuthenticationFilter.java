package com.delivery.app.users.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.SecurityContext;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    // first thing to be executed once per request : validate and check everything related to the jwt token
    // we check if we have jwt token or not
    //make call using user details service to fetch user info from database based on user email set as a claim or token subj that going to be extracted
    // getting info and check if user exists
    // user found then validation token based on the user
    //if token is expired or not valid then invalid jwt token message
    //else update security context holder and set connected user because when we fetch user details information from database
    //this user alors is authenticated and now update authen manager
    //everytime we check if the user is authenticated for this request
    //the sec context holder is updated it will dispatch the req and send dispatcher servlet
    //from dispatcher servlet it will be sent to controller
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final CustomUserDetailsService customUserDetailsService;
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        /*final String jwt;*/
        final String userEmail;
        final String token ;


        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request , response);
            return;
        }
        token = authHeader.substring(7);
        userEmail = jwtService.getUserName(token);

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication()== null){
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(userEmail); // get user from database once user not found or not authenticated

            if (jwtService.isTokenValid(token , userDetails)) { //valid token is a must
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                       userDetails.getUsername(),
                       null,
                       userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request,response);

    }
}
