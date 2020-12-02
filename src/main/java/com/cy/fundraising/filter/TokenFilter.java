package com.cy.fundraising.filter;

import org.springframework.core.Ordered;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter(filterName = "TokenFilter",urlPatterns = {"/userSide/launch"})
public class TokenFilter implements Filter, Ordered {

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String token =  ((HttpServletRequest)servletRequest).getHeader("AUTHORIZATION");
        if(token == null || !token.matches("Bearer.*")){
            servletRequest.getRequestDispatcher("/tokenFailed").forward(servletRequest, servletResponse);
        }
        else{
            filterChain.doFilter(servletRequest, servletResponse);
        }

    }

    @Override
    public void destroy() {

    }

    @Override
    public int getOrder() {
        return 1;
    }
}
