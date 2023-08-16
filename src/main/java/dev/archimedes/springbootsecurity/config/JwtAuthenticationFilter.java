package dev.archimedes.springbootsecurity.config;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private Logger logger = LoggerFactory.getLogger(OncePerRequestFilter.class);

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestHeader = request.getHeader("Authorization");
        System.out.println(requestHeader);
        logger.info("Header: " + requestHeader);

        String username = null;
        String token = null;

        if(requestHeader != null && requestHeader.startsWith("Bearer")){
            token = requestHeader.substring(7);
            logger.info(token);

            try {
                username = jwtHelper.getUsernameFromToken(token);
            }catch (IllegalArgumentException e){
                logger.info("Illegal Argument Exception While Fetching !!");
                System.out.println(e.getMessage());
            }
            catch (ExpiredJwtException e){
                logger.info("Given token is expired !1");
                System.out.println(e.getMessage());
            }
            catch (MalformedJwtException e){
                logger.info("Some change or illegal modification has been done to token !!");
                System.out.println(e.getMessage());
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        else {
            logger.info("Invalid Header Value !!");
        }

        //
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {


            //fetch user detail from username
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            logger.info(userDetails.toString());
            Boolean validateToken = jwtHelper.validateToken(token, userDetails);
            logger.info(validateToken.toString());
            if (validateToken) {

                //set the authentication
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } else {
                logger.info("Validation fails !!");
            }
        }

        filterChain.doFilter(request, response);


    }
}
