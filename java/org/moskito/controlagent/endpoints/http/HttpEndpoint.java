package org.moskito.controlagent.endpoints.http;

import org.moskito.controlagent.Agent;
import org.moskito.controlagent.ThresholdStatusHolder;
import org.moskito.controlagent.endpoints.EndpointUtility;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.OutputStream;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 15.04.13 20:43
 */
@WebFilter(description = "MoSKito Control Endpoint", filterName = "MoSKitoControlEndpoint",
		urlPatterns = {
		"/moskito-control-agent/thresholdStatus"
	})
public class HttpEndpoint implements Filter {
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)servletRequest;
		ThresholdStatusHolder status = Agent.getInstance().getThresholdStatus();
		byte[] data = EndpointUtility.object2JSON(status);
		OutputStream out = servletResponse.getOutputStream();
		out.write(data);
		out.flush();
		out.close();
		return ;
	}

	@Override
	public void destroy() {
	}
}
