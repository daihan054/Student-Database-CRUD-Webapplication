<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<title>CRUD web app</title>
</head>

<body>
<h2>MBSTU Student database</h2>


<input type="button" value="Add student"
	onclick="window.location.href='add-student-form.jsp'; return false;" />

<table>
	<tr>
		<th>First Name</th>
		<th>Last Name</th>
		<th>Email</th>
		<th>Action</th>
	</tr>

	<c:forEach var="tempStudent" items="${STUDENT_LIST}">
	    
	    <c:url var="tempLink" value="StudentControllerServlet" >
	       <c:param name="command" value="LOAD"/>
	       <c:param name="studentId" value="${tempStudent.id}" />
	    </c:url>
	    
	    <c:url var="delLink" value="StudentControllerServlet">
          <c:param name="command" value="DELETE"/>
	      <c:param name="studentId" value="${tempStudent.id}" />	      
	    </c:url>
	    
		<tr>
			<td>${tempStudent.firstName}</td> 
			<td>${tempStudent.lastName } </td>
			<td>${tempStudent.email} </td>
			<td><a href="${tempLink}" >Update</a> | 
			<a href="${delLink}" onclick="if(!confirm('Are you sure you want to delete this student?'))return false;">Delete</a></td>
		</tr>
	</c:forEach>

</table>

<a href="StudentControllerServlet">HOME BUTTON</a>
</body>

</html>