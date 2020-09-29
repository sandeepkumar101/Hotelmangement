<%@ page language="java" contentType="application/json; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
{"paths": {
<c:forEach var="paths" items="${it}" varStatus="loop" begin="1">
    "p${loop.index}": {
        "color": "green",
        "weight": 8,
        "latlngs": [
        	<c:forEach var="latlongs" items="${paths.latlongs}" varStatus="arrIndex">
            { "lat": ${latlongs[0]}, "lng": ${latlongs[1]} }<c:if test="${!arrIndex.last}">,</c:if>
            </c:forEach>
        ],
        "message": "${paths.message}"
      }
</c:forEach>      
 },
 
 "markers": {
            <c:forEach var="paths" items="${it}" varStatus="loop" begin="1">
            <c:forEach var="latlongs" items="${paths.latlongs}" varStatus="arrIndex">
            "${arrIndex.index}": {
                "lat": ${latlongs[0]},
                "lng": ${latlongs[1]},
                "icon": {
                    "iconUrl": "icons/map-marker.svg",
                    "iconSize": [24, 24],
                    "iconAnchor": [12, 24],
                    "popupAnchor": [0, 0],
                    "shadowSize": [0, 0],
                    "shadowAnchor": [0, 0]
                }
            }${!arrIndex.last ? ',' : ''}
            </c:forEach>
            </c:forEach>
            
            <c:forEach var="latlongs" items="${it.get(0).currentLatlongs}" varStatus="arrIndex">
                ,"cur${arrIndex.index}":{"lat":${latlongs[0]}, "lng":${latlongs[1]}, "icon":{"iconUrl": "icons/bus.png", "iconSize": [24, 24], "iconAnchor": [12, 24], "popupAnchor": [0, 0], "shadowSize": [0, 0], "shadowAnchor": [0, 0]}}
            </c:forEach>
    }
}
