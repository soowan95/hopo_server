package com.hopo._filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerMapping;

import jakarta.servlet.FilterChain;
import jakarta.servlet.GenericFilter;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class PathRedirectFilter extends GenericFilter {

	@Qualifier("requestMappingHandlerMapping")
	@Autowired
	private HandlerMapping handlerMapping;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws
		IOException,
		ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		String uri = httpRequest.getRequestURI();

		if (!isApiRequest(uri) || isRequestUriExist(httpRequest))
			chain.doFilter(request, response);
		else
			httpResponse.sendRedirect(makeRedirectUri(uri.split("/")));
	}

	private boolean isRequestUriExist(HttpServletRequest request) {
		try {
			return handlerMapping.getHandler(request) != null;
		} catch (Exception e) {
			return false;
		}
	}

	private String makeRedirectUri(String[] paths) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < paths.length; i++) {
			if (i == 3)
				sb.append("/hopo");
			sb.append("/").append(paths[i]);
		}
		return sb.substring(1);
	}

	private boolean isApiRequest(String uri) {
		return uri.matches("/api[/[^/]+]+");
	}
}
