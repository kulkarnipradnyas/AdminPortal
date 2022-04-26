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
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.assessment.model.AirLine;
import com.assessment.model.Flight;
import com.assessment.utils.HibernateUtil;

/**
 * Servlet implementation class FlightServlet
 */
public class FlightServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FlightServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		PrintWriter writer = resp.getWriter();
		RequestDispatcher requestispatcher = null;
		SessionFactory factory = HibernateUtil.getSessionFactory();
		Session session = factory.openSession();
		Transaction transaction = session.beginTransaction();
		String flightName = req.getParameter("flightName");
		String source = req.getParameter("source");
		String destination = req.getParameter("destination");
		String airline = req.getParameter("airlines");
		int price = Integer.parseInt(req.getParameter("price").isEmpty() ? "0" : req.getParameter("price"));
		if (flightName.isEmpty() || source.isEmpty() || destination.isEmpty() || airline.isEmpty() || price == 0) {
			writer = resp.getWriter();
			requestispatcher = req.getRequestDispatcher("insertFlightDetails.jsp");
			requestispatcher.include(req, resp);
			writer.println(
					"<center> <span style='color:green; font-family: cursive; font-weight: bold;'>Enter Valid Flight Details</span></center>");
		}else {
			AirLine airlineCreated = new AirLine(airline);
			System.out.println("airlineCreated : " + airlineCreated.toString());
			session.save(airlineCreated);
			Flight flightCreated = new Flight(flightName, source, destination, airlineCreated.getAirlineName(), price);
			System.out.println("flightCreated : " + flightCreated.toString());
			session.save(flightCreated);
			transaction.commit();
			List<Flight> flights = session.createQuery("from Flight").list();
			req.setAttribute("flights", flights);
			req.getRequestDispatcher("showAllFlightDetails.jsp").forward(req, resp);
		}

	}

}
