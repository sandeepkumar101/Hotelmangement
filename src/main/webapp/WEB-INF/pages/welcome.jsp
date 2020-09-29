<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

 <ul class="sidebar-menu">
     <li class="active">
         <a href="#/home.html">
             <i class="ion-home"></i> <span>Home</span>
         </a>
     </li>
     
      <li class="treeview">
          <a href="#student.html">
          	<i class="ion-android-contacts"></i> <span>Room Details</span>
           	<i class="fa fa-angle-left pull-right"></i>
          </a>
          <ul class="treeview-menu">
                <c:if test="${it =='authenticated'}">
                    <li><a href="#addstudent.html"><i class="fa fa-angle-double-right"></i> Add Registration</a></li>
                    <li><a href="#student.html"><i class="fa fa-angle-double-right"></i> Room List</a></li>
                </c:if>
           	
           	<c:if test="${it =='administrator'}">
                        <li><a href="#teacherView.html"><i class="fa fa-angle-double-right"></i> Room List</a></li>
           		<li><a href="#studentReport.html"><i class="fa fa-angle-double-right"></i> Revenue Report</a></li>
           	</c:if>
       	</ul>
      </li>
      <li>
          <a href="#settings.html">
              <i class="ion-gear-a"></i> <span>Settings</span>
          </a>
      </li>
     <li>
         <a href="#editProfile.html">
             <i class="ion-edit"></i> <span>Profile</span>
         </a>
     </li>
     
     <li>
         <a href="#" ng-click="logout()">
             <i class="ion-power"></i> <span>Sign Out</span>
         </a>
     </li>
  </ul>
