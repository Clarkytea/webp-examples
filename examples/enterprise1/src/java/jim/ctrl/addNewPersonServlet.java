/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jim.ctrl;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jim.bus.BusinessException;
import jim.bus.PersonRegistration;
import jim.entities.Person;
import org.apache.commons.beanutils.BeanUtils;

/**
 *
 * @author briggsj
 */
public class addNewPersonServlet extends HttpServlet {
    @EJB
    private PersonRegistration personRegistration;
   
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String view = "personlist.jsp";
        List<String> messages = new ArrayList<String>();
        request.setAttribute("messages", messages);

        Person p = new Person();
        try {
            BeanUtils.populate(p, request.getParameterMap());
        } catch (IllegalAccessException ex) {
            Logger.getLogger(addNewPersonServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(addNewPersonServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            p = personRegistration.createNewPerson(p);
            messages.add("Person " + p.getFullName() + " added to database");
        } catch (BusinessException ex) {
            Logger.getLogger(addNewPersonServlet.class.getName()).log(Level.SEVERE, null, ex);
            messages.add("Person " + p.getFullName() + " cannot be added to database: " + ex.getMessage());
        }

        List<Person> plist = personRegistration.findAllPersons();
        request.setAttribute("plist", plist);

        RequestDispatcher rd = request.getRequestDispatcher(view);
        rd.forward(request, response);
    } 

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    } 

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
