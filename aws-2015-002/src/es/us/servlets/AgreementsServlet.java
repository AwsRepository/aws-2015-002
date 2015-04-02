package es.us.servlets;

import es.us.isa.aml.AgreementManager;
import es.us.isa.aml.model.AgreementModel;
import es.us.isa.aml.translator.Translator;
import es.us.util.Helper;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Applied Software Engineering Research Group (ISA Group)
 * University of Sevilla, Spain
 *
 * @author Antonio Gamez <agamez2@us.es>
 * @version 1.0
 */

public class AgreementsServlet extends HttpServlet {
    private static final Logger LOG = Logger.getLogger(AgreementsServlet.class.getName());

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        AgreementManager agr = Helper.getInstance().getAgreementManager();
        Translator t = Helper.getInstance().getIAgreeTranslator();

        String resourcePath = req.getPathInfo();
        String agreement = "";

        try {
            if (resourcePath != null && !resourcePath.equals("/")) {
                String[] resources = resourcePath.split("/");
                if (resources.length > 2) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Check URI");
                } else {
                    String clientId = resources[1];
                    if (agr.getStoreManager().getAgreementMap().containsKey(clientId)) {
                        agreement = t.export(agr.getStoreManager().getAgreementMap().get(clientId));
                    } else {
                        resp.sendError(HttpServletResponse.SC_NO_CONTENT, "No data");
                    }
                }
            } else {
                StringBuilder sb = new StringBuilder();
                List<AgreementModel> models = new LinkedList<>(agr.getStoreManager().getAgreementMap().values());

                Collections.sort(models, new Comparator<AgreementModel>() {
                    @Override
                    public int compare(AgreementModel o1, AgreementModel o2) {
                        return o1.getDocType().toString().compareToIgnoreCase(o2.getDocType().toString());
                    }
                });

                for (AgreementModel am : models) {
                    sb.append(t.export(am)).append("\n-------------------------------------------------------\n");
                }
                agreement = sb.toString();

            }
            resp.setStatus(HttpServletResponse.SC_FOUND);
            resp.setContentType("text/plain");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write(agreement);

        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            LOG.log(Level.WARNING, null, e);
        }
    }
}

