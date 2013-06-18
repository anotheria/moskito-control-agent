package org.moskito.controlagent.endpoints.http;

import net.anotheria.moskito.core.accumulation.Accumulator;
import net.anotheria.moskito.core.accumulation.AccumulatorRepository;
import net.anotheria.util.StringUtils;
import org.moskito.controlagent.AccumulatorListItem;
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
import java.util.LinkedList;
import java.util.List;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 15.04.13 20:43
 */
@WebFilter(description = "MoSKito Control Endpoint", filterName = "MoSKitoControlEndpoint",
		urlPatterns = {
		"/moskito-control-agent/*"
	})
public class HttpEndpoint implements Filter {

	static enum COMMAND{
		/**
		 * Requests the status of this component. The status is calculated based on worst threshold.
		 */
		STATUS,
		/**
		 * Requests the list of the accumulators.
		 */
		ACCUMULATORS,
		/**
		 * Requests the data for one or multiple accumulators.
		 */
		ACCUMULATOR
	};

	public static final String MAPPED_NAME = "moskito-control-agent";

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)servletRequest;
		String requestURI = req.getRequestURI();
		//we ignore the case that request uri could be null
		boolean handled = false;
		String myPath = requestURI.substring(requestURI.indexOf(MAPPED_NAME) +MAPPED_NAME.length() + 1);
		String tokens[] = StringUtils.tokenize(myPath, '/');
		COMMAND command = COMMAND.valueOf(tokens[0].toUpperCase());
		switch(command){
			case STATUS:
				status(servletRequest, servletResponse, tokens);
				break;
			case ACCUMULATORS:
				accumulators(servletRequest, servletResponse, tokens);
				break;
			default:
				throw new AssertionError("Unrecognized command "+command);
		}
	}

	private void accumulators(ServletRequest servletRequest, ServletResponse servletResponse, String parameters[]) throws IOException{
		System.out.println("parameters: "+parameters.length);
		List<Accumulator> accumulators = AccumulatorRepository.getInstance ().getAccumulators();
		List<AccumulatorListItem> ret = new LinkedList<AccumulatorListItem>();
		for (Accumulator acc : accumulators){
			ret.add(new AccumulatorListItem(acc.getName(), acc.getValues().size()));
		}
		writeReply(servletResponse, ret);
	}

	private void accumulator(ServletRequest servletRequest, ServletResponse servletResponse, String parameters[]) throws IOException{
		System.out.println("parameters: "+parameters.length);
		List<Accumulator> accumulators = AccumulatorRepository.getInstance ().getAccumulators();
		List<AccumulatorListItem> ret = new LinkedList<AccumulatorListItem>();
		for (Accumulator acc : accumulators){
			ret.add(new AccumulatorListItem(acc.getName(), acc.getValues().size()));
		}
		writeReply(servletResponse, ret);
	}

	void writeReply(ServletResponse servletResponse, Object parameter) throws IOException{
		byte[] data = EndpointUtility.object2JSON(parameter);
		OutputStream out = servletResponse.getOutputStream();
		out.write(data);
		out.flush();
		out.close();
	}

	private void status(ServletRequest servletRequest, ServletResponse servletResponse, String parameters[]) throws IOException{
		ThresholdStatusHolder status = Agent.getInstance().getThresholdStatus();
		writeReply(servletResponse, status);

	}

	@Override
	public void destroy() {
	}
}
