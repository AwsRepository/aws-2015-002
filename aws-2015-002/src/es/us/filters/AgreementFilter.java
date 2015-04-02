package es.us.filters;

import es.us.isa.aml.AgreementManager;
import es.us.isa.aml.model.AgreementOffer;
import es.us.util.Helper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Applied Software Engineering Research Group (ISA Group)
 * University of Sevilla, Spain
 *
 * @author Antonio Gamez <agamez2@us.es>
 * @version 1.0
 */

public class AgreementFilter implements Filter {

    private static final Logger LOG = Logger.getLogger(AgreementFilter.class.getName());
    private AgreementManager agr = Helper.getInstance().getAgreementManager();
    private String clientId;

    @Override
    public void init(FilterConfig fConfig) throws ServletException {
        ServletContext context = fConfig.getServletContext();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        clientId = req.getParameter("user");
        requestDone(req);
        if (authorizeRequest(req)) {
            chain.doFilter(request, response);
            LOG.log(Level.INFO, "Request accepted");
        } else {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.sendRedirect("/error.html");
            LOG.log(Level.INFO, "Request rejected");
        }
    }

    @Override
    public void destroy() {

    }

    private boolean authorizeRequest(HttpServletRequest req) {
        AgreementOffer offer = agr.getStoreManager().getAgreementOffer(clientId);
        boolean isReadMethod = isReadMethod(req.getMethod());
        if (offer != null) {
        	boolean isReadOnly = offer.getProperty("PlanReadOnly").booleanValue();
        	if (isReadOnly)
        		return offer.evaluateGT("RequestTerm") && isReadMethod;
        	else
        		return offer.evaluateGT("RequestTerm");
        }
        return false;
    }

    private void requestDone(HttpServletRequest req) {
        Integer numReq;
        AgreementOffer offer = agr.getStoreManager().getAgreementOffer(clientId);
        if (offer != null) {
            if (offer.getProperty("Requests").getExpression() == null) {
                offer.setProperty("Requests", 0);
            }
            numReq = offer.getProperty("Requests").intValue();
            numReq++;
            offer.setProperty("Requests", numReq);
        }
    }
    
    private boolean isReadMethod(String method)
    {
    	if ("POST".equals(method) || "PUT".equals(method) || "DELETE".equals(method))
    		return false;
    	else
    		return true;
    }
}