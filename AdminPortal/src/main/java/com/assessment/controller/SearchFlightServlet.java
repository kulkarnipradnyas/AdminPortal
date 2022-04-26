package com.assessment.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.assessment.model.Flight;
import com.assessment.utils.HibernateUtil;

/**
 * Servlet implementation class SearchFlightServlet
 */
public class SearchFlightServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchFlightServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		PrintWriter writer = resp.getWriter();
		RequestDispatcher requestispatcher = null;
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		String date = req.getParameter("date");
		System.out.println("date : " + date);
		String source = req.getParameter("source");
		String destination = req.getParameter("destination");
		int numberOfPass = Integer.parseInt(req.getParameter("numberOfPass").isEmpty() ? "0" : req.getParameter("numberOfPass"));
		if (source == null || destination == null || numberOfPass == 0 || date.isEmpty() || source.isEmpty() || destination.isEmpty()) {
			writer = resp.getWriter();
			requestispatcher = req.getRequestDispatcher("userHomePage.jsp");
			requestispatcher.include(req, resp);
			writer.println(
					"<center> <span style='color:green; font-family: cursive; font-weight: bold;'>Enter Valid Details</span></center>");
		} else {
			@SuppressWarnings("unchecked")
			List<Flight> flights = session.createQuery("from Flight").list();
			List<Flight> availableFlights = new ArrayList<Flight>();
			transaction.commit();
			for (Flight flight : flights) {
				if (flight.getSource().equalsIgnoreCase(source)
						&& flight.getDestination().equalsIgnoreCase(destination)) {
					availableFlights.add(flight);
				}
			}

			if (availableFlights.size() > 0) {
				req.getSession().setAttribute("numberOfPass", numberOfPass);
				req.setAttribute("isThisFromSearchServlet", true);
				req.setAttribute("flights", availableFlights);
				req.getRequestDispatcher("showAllFlightDetails.jsp").forward(req, resp);
			} else {
				writer = resp.getWriter();
				requestispatcher = req.getRequestDispatcher("userHomePage.jsp");
				requestispatcher.include(req, resp);
				writer.println(
						"<center> <span style='color:red'>No Flight available for the Entered Soure and Destination</span></center>");
			}

		}
	}

}
