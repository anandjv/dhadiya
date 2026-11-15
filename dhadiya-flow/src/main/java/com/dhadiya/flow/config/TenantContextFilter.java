package com.dhadiya.flow.config;

import com.dhadiya.flow.model.Constants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class TenantContextFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String tenantId = request.getHeader(Constants.X_TENANT_ID);
        if (tenantId == null || tenantId.isBlank()) {
            tenantId = request.getHeader(Constants.TENANT_ID);
        }
        if (tenantId == null || tenantId.isBlank()) {
            tenantId = request.getParameter("tenantId");
        }
        if (tenantId == null || tenantId.isBlank()) {
            tenantId = request.getParameter("realmCode");
        }
        if (tenantId == null || tenantId.isBlank()) {
            tenantId = Constants.DEFAULT_TENANT;
        }

        // Set in ThreadLocal
        TenantContext.setTenantId(tenantId);

        // Also set in RequestContextHolder if available
        RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
        if (attrs != null) {
            attrs.setAttribute(Constants.TENANT_ID, tenantId, RequestAttributes.SCOPE_REQUEST);
        }
        request.setAttribute(Constants.TENANT_ID, tenantId);

        log.info("[TENANT FILTER] Intercepted request URI: {} -> Target Tenant ID: [{}]",
                request.getRequestURI(), tenantId);

        try {
            filterChain.doFilter(request, response);
        } finally {
            TenantContext.clear();
        }
    }
}
