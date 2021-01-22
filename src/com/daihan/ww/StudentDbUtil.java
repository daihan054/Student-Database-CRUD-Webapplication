package com.daihan.ww;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

public class StudentDbUtil {

	private DataSource dataSource;

	public StudentDbUtil(DataSource theDataSource) {
		dataSource = theDataSource;
	}

	public List<Student> getStudents() throws Exception {
		List<Student> students = new ArrayList<>();
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;

		try {
			// get a connection
			myConn = dataSource.getConnection();

			// create sql statement
			String sql = "select * from student order by first_name";
			myStmt = myConn.createStatement();

			// execute query
			myRs = myStmt.executeQuery(sql);

			// process resultset
			while (myRs.next()) {

				// retrieve data from resultset row
				int id = myRs.getInt("id");
				String firstName = myRs.getString("first_name");
				String lastName = myRs.getString("last_name");
				String email = myRs.getString("email");

				// create new student object
				Student tempStudent = new Student(id, firstName, lastName, email);

				// add it to the list of students
				students.add(tempStudent);
			}

			return students;

		} finally {
			// close JDBC object
			close(myConn, myStmt, myRs);
		}

	}

	public void addStudent(Student theStudent) throws Exception {
		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			// get db connection
			myConn = dataSource.getConnection();

			// create sql for insert
			String sql = "insert into student(first_name,last_name,email) values(?,?,?)";
			myStmt = myConn.prepareStatement(sql);

			// set the param values for the student
			myStmt.setString(1, theStudent.getFirstName());
			myStmt.setString(2, theStudent.getLastName());
			myStmt.setString(3, theStudent.getEmail());

			// execute sql insert
			myStmt.executeUpdate();
		} finally {
			// clean up JDBC objects
			close(myConn, myStmt, null);
		}

	}

	public Student getStudent(String theStudentID) throws SQLException {
		Student theStudent = null;
		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet rSet = null;

		try {
			// Convert student id to int
			int studentId = Integer.parseInt(theStudentID);

			// System.out.print("Student id hoilo: "+studentId);

			// get db connection
			myConn = dataSource.getConnection();

			// create sql to get selected student
			String sql = "select * from student where id=?";

			// create prepared statement
			myStmt = myConn.prepareStatement(sql);

			// set params
			myStmt.setInt(1, studentId);

			// execute statement
			rSet = myStmt.executeQuery();

			if (rSet.next()) {
				String firstName = rSet.getString("first_name");
				String lastName = rSet.getString("last_name");
				String email = rSet.getString("email");
				theStudent = new Student(studentId, firstName, lastName, email);
			} else {
				throw new RuntimeException("Could not find student id: " + studentId);
			}

			return theStudent;
		} finally {
			// clean up JDBC objects
			close(myConn, myStmt, null);
		}

	}

	public void updateStudent(Student theStudent) throws SQLException {
		Connection myConn = null;
		PreparedStatement myStatement = null;
		int id = theStudent.getId();
		String firstName = theStudent.getFirstName();
		String lastName = theStudent.getLastName();
		String email = theStudent.getEmail();
		try {
			// get db connection
			myConn = dataSource.getConnection();

			// create SQL update statement
			String sql = "update student set first_name=? , last_name=? , email=?  where id=?";

			// prepare statement
			myStatement = myConn.prepareStatement(sql);

			// set params
			myStatement.setString(1, firstName);
			myStatement.setString(2, lastName);
			myStatement.setString(3, email);
			myStatement.setInt(4, id);

			myStatement.executeUpdate();

		} finally {
			close(myConn, myStatement, null);
		}

	}

	private void close(Connection myConn, Statement myStmt, ResultSet myRs) {
		try {
			if (myRs != null) {
				myRs.close();
			}

			if (myStmt != null) {
				myStmt.close();
			}

			if (myConn != null) {
				myConn.close();
			}

		} catch (Exception exc) {
			exc.printStackTrace();
		}

	}

	public void deleteStudent(int id) throws SQLException {
		Connection myConnection = null;
		PreparedStatement pStatement = null;

		try {
			// get db connection
			myConnection = dataSource.getConnection();
			// write sql query
			String sql="delete from student where id=?";
			//prepare statement
			pStatement=myConnection.prepareStatement(sql);
			
			//set the parameters
			pStatement.setInt(1, id);
			
			//execute query
			pStatement.execute();

		} finally {
           close(myConnection, pStatement, null);
		}

	}

}
