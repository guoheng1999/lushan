//package edu.cuit.lushan.filter;
//
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
////@Component
//public class CORSFilter extends OncePerRequestFilter {
//
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//
//        response.setContentType("text/html;charset=UTF-8");
//
//        response.setHeader("Access-Control-Allow-Origin", "*");
//
//        response.setHeader("Access-Control-Allow-Methods", "*");
//
//        response.setHeader("Access-Control-Max-Age", "0");
//
//        response.setHeader("Access-Control-Allow-Headers", "Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,userId,token");
//
//        response.setHeader("Access-Control-Allow-Credentials", "true");
//
//        response.setHeader("XDomainRequestAllowed","1");
//
//        filterChain.doFilter(request, response);
//    }
//}