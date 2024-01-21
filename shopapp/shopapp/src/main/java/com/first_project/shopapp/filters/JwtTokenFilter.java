package com.first_project.shopapp.filters;

import com.first_project.shopapp.components.JwtTokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {
    @Value("${api.prefix}")
    private String apiPrefix;
    @Autowired
    JwtTokenUtil jwtTokenUtil;
    @Autowired
    UserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        try{
            if(isByPassToken(request)){
                filterChain.doFilter(request,response);//enable bypass
                return;
            }
            final String authHeader=request.getHeader("Authorization");
            if(authHeader==null||!authHeader.startsWith("Bearer ")){
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
            final String token=authHeader.substring(7);
            final String phoneNumber=jwtTokenUtil.extraPhoneNumber(token);
            if(phoneNumber!=null&& SecurityContextHolder.getContext().getAuthentication()==null){
                UserDetails userDetails=userDetailsService.loadUserByUsername(phoneNumber);
                if(jwtTokenUtil.validToken(token,userDetails)){
                    UsernamePasswordAuthenticationToken authenticationToken=
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,null,userDetails.getAuthorities()
                            );
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
            filterChain.doFilter(request,response);
        }catch (Exception e){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Unauthorized");
        }
    }
    private boolean isByPassToken(@NonNull HttpServletRequest request) {
        final List<Pair<String, String>> byPassTokens = Arrays.asList(
                Pair.of(apiPrefix + "/products", "GET"),
                Pair.of(apiPrefix+"/role","GET"),
                Pair.of(apiPrefix + "/categories", "GET"),
                Pair.of(apiPrefix + "/user/login", "POST"),
                Pair.of(String.format("%s/user/register", apiPrefix), "POST")
                );
        for(Pair<String,String> byPassToken:byPassTokens){
            if(request.getServletPath().contains(byPassToken.getFirst())
                    &&request.getMethod().equals(byPassToken.getSecond())){
                return true;
            }
        }
        return false;
    }
}
