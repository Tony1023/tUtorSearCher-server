package edu.usc.csci310.team16.tutorsearcher.server;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthInterceptor implements HandlerInterceptor {

    UserDAO dao = new UserDAO();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("access-token");
        String idStr = request.getHeader("user-id");
        if (token == null || idStr == null) {
            response.setStatus(403);
            return false;
        }
        Integer id = Integer.valueOf(idStr);
        if (dao.validateUserToken(id, token)) {
            return true;
        }
        response.setStatus(403);
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception { }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception { }
}
