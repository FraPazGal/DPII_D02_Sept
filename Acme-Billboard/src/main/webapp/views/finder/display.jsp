<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>


<security:authorize access="hasRole('MANAGER')">

	<table class="displayStyle" style="width: 50%">

		<tr>
			<td style="text-align: right"><script
					src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.6.0/Chart.min.js"></script>

				<canvas id="myChart" width="100" height="100"></canvas> <script>
					var pack = ${packages};
					var cpack = ${requests};
					var packs = []
					for ( var n = 0; n < pack.length; n++) {
						packs.push(pack[n].toString());
					}
					function pushColor(packs) {
						var colors = [];
						var len = pack;
						for ( var i = 0; i < len.length; i++) {
							colors.push(getRandomColor());
						}
						return colors;
					}
					function getRandomColor() {
						var letters = '789ABCD'.split('');
						var color = '#';
						for ( var i = 0; i < 6; i++) {
							color += letters[Math.round(Math.random() * 6)];
						}
						return color;
					}

					new Chart(document.getElementById("myChart"), {
						type : 'bar',
						data : {
							labels : packs,
							datasets : [
								{
									label : "Result finders per package",
									backgroundColor : pushColor(packs),
									data : cpack,
									borderColor : pushColor(),
									borderWidth : 1
								}
							]
						},
						options : {
							title : {
								display : true,
								text : 'How many times it is the package displayed a finder'
							},scales : {
								yAxes : [ {
									ticks : {
										beginAtZero : true
									}
								} ]
							},
							
							maintainAspectRatio : true,
							responsive : true,

							legend : {
								display : true,
								position : 'right',
								labels : {
									fontColor : '#000'
								}
							},
							layout : {
								padding : {
									left : 0,
									right : 0,
									bottom : 300,
									top : 0
								}
							},
							tooltips : {
								enabled : true
							}
						}
					});
				</script></td>
		</tr>
	</table>
</security:authorize>
