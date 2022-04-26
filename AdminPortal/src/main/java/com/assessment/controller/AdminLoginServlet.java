package com.assessment.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.assessment.model.Admin;
import com.assessment.utils.HibernateUtil;

/**
 * Servlet implementation class AdminLoginServlet
 */
public class AdminLoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminLoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Inside service method");
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();
		System.out.println("after Inside service method");
		String enteredUserName = request.getParameter("adminUsername");
		String enteredPassword = request.getParameter("adminPassword");
		@SuppressWarnings("unchecked")
		List<Admin> adminDb = session.createQuery("from Admin").list();
		System.out.println("admin DB : " + adminDb);
		RequestDispatcher adminLoginDispatcher = null;
		if (adminDb.isEmpty()) {
			Admin admin = new Admin(enteredUserName, enteredPassword);
			request.setAttribute("admin", adminDb);
			session.save(admin);
			transaction.commit();
			session.close();
			adminLoginDispatcher = request.getRequestDispatcher("showAdminPage");
			PrintWriter printWriter = response.getWriter();
			printWriter.print("<html><body><h1>You have successfully logged in</h1></body></html>");
		} else {
			Admin admin = adminDb.get(0);
			String currentAdminPassword = admin.getAdminPassword();
			if (currentAdminPassword.equals(enteredPassword)) {
				request.setAttribute("admin", adminDb);
				session.close();
				adminLoginDispatcher = request.getRequestDispatcher("showAdminPage.jsp");
				adminLoginDispatcher.forward(request, response);
			} else {
				session.close();
				PrintWriter writer = response.getWriter();
				adminLoginDispatcher = request.getRequestDispatcher("adminlogin.jsp");
				adminLoginDispatcher.include(request, response);
				writer.println("<center> <span style='color:red'> Invalid Credentials!!, current Admin password is : " + currentAdminPassword + "</span></center>");
			}

		}

	}

}
