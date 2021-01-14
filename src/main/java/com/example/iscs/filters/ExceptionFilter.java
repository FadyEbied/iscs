package com.example.iscs.filters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Random;

@Slf4j
@Component
@Order(1)
public class ExceptionFilter extends GenericFilter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("Filter");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;



        if (request.getHeader("statulful-id") == null) {
            int id = new Random().nextInt(100);
            response.addHeader("stateful-id", id+"");
        }


       filterChain.doFilter(servletRequest,servletResponse);
    }
}
