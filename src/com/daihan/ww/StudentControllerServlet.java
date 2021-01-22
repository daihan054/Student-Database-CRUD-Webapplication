package com.daihan.ww;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;


import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/StudentControllerServlet")
public class StudentControllerServlet extends HttpServlet {

	private StudentDbUtil studentDbUtil;

	@Resource(name = "jdbc/web_student_tracker")
	private DataSource dataSource;

	@Override
	public void init() throws ServletException {
		super.init();
		// create our student db util ... and pass in the connection pool
		try {
			studentDbUtil = new StudentDbUtil(dataSource);
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			// read the "command" parameter
			String theCommand = request.getParameter("command");

			// if the command is missing , then default to listing the students
			if (theCommand == null) {
				theCommand = "LIST";
			}

			// route to the appropriate method
			switch (theCommand) {
			case "ADD":
				addStudent(request, response);
				break;
			
			case "LOAD":
				loadStudent(request,response);
                break;
             
			case "UPDATE":
				updateStudent(request,response);
                break;
            
			case "DELETE":
				deleteStudent(request,response);
				break;
				
			default:
				listStudents(request, response);
				break;
			}

		} catch (Exception e) {
			throw new ServletException(e);
		}

	}

	private void updateStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//read student info from form data
		int id=Integer.parseInt(request.getParameter("studentId"));
		String firstName=request.getParameter("firstName");
		String lastName=request.getParameter("lastName");
		String email=request.getParameter("email");
		
	   // create a new student object
		Student theStudent=new Student(id, firstName, lastName, email);
		studentDbUtil.updateStudent(theStudent);
		listStudents(request, response);
	}
	
	private void deleteStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
	   //read student id from form data
		String studentId=request.getParameter("studentId");
		int id=Integer.parseInt(studentId);
	   
	   //delete student from database
	    studentDbUtil.deleteStudent(id);
	    
	    //send them back to "list students" page
	    listStudents(request, response);
	}

	private void loadStudent(HttpServletRequest request, HttpServletResponse response) throws Exception{
		// read student from form data
		String theStudentID=request.getParameter("studentId");
		
		//get student from database (db util)
		Student theStudent=studentDbUtil.getStudent(theStudentID);
		
		//place student in the request attribute
		request.setAttribute("THE_STUDENT",theStudent);
		
		//send to jsp page: update-student-form.jsp
		RequestDispatcher requestDispatcher=request.getRequestDispatcher("/update-student-form.jsp");
		requestDispatcher.forward(request, response);
	}

	private void addStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// read student info from form data
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");

		// create a new student object
		Student theStudent = new Student(firstName, lastName, email);

		// add the student to the database
		studentDbUtil.addStudent(theStudent);

		// send back to main page (the student list)
		listStudents(request, response);
	}

	private void listStudents(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// get students from db util
		List<Student> students = studentDbUtil.getStudents();

		// add students to the request
		request.setAttribute("STUDENT_LIST", students);

		// send to JSP page(view)
		RequestDispatcher dispatcher = request.getRequestDispatcher("/list-students.jsp");
		dispatcher.forward(request, response);
	}
	
	

}
