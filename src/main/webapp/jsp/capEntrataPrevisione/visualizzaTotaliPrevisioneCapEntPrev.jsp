<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%-- Inclusione head e CSS --%>
<s:include value="/jsp/include/head.jsp" />
</head>
<body>
	<%-- Inclusione header --%>
	<s:include value="/jsp/include/header.jsp" />

	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12 ">
				<div class="contentPage">
					<form action="">
						<h3>Totali di previsione</h3>
						<table class="table table-bordered">
							<tr>
								<th width="25%" scope="col">Esercizio <s:property value="%{annoEsercizioInt}"/></th>
								<th width="25%" scope="col">Entrata</th>
								<th width="25%" scope="col">Spesa</th>
								<th width="25%" scope="col">Differenza</th>
							</tr>
							<tr>
								<th scope="row">Competenza</th>
								<td id="competenza0Entrata">
									<s:property value="totaliStanziamentiCompetenzaEntrata0"/>
								</td>
								<td id="competenza0Spesa">
									<s:property value="totaliStanziamentiCompetenzaSpesa0"/>
								</td>
								<td id="competenza0Differenza">
									<s:property value="differenzaStanziamentiCompetenza0"/>
								</td>
							</tr>
							<tr>
								<th scope="row">Residuo</th>
								<td id="residui0Entrata">
									<s:property value="totaliStanziamentiResiduiEntrata0"/>
								</td>
								<td id="residui0Spesa">
									<s:property value="totaliStanziamentiResiduiSpesa0"/>
								</td>
								<td id="residui0Differenza">
									<s:property value="differenzaStanziamentiResidui0"/>
								</td>
							</tr>
							<tr>
								<th scope="row">Cassa</th>
								<td id="cassa0Entrata">
									<s:property value="totaliStanziamentiCassaEntrata0"/>
								</td>
								<td id="cassa0Spesa">
									<s:property value="totaliStanziamentiCassaSpesa0"/>
								</td>
								<td id="cassa0Differenza">
									<s:property value="differenzaStanziamentiCassa0"/>
								</td>
							</tr>
						</table>
	
						<table class="table table-bordered">
	
							<tr>
								<th width="25%" scope="col">Esercizio <s:property value="%{annoEsercizioInt + 1}"/></th>
								<th width="25%" scope="col">Entrata</th>
								<th width="25%" scope="col">Spesa</th>
								<th width="25%" scope="col">Differenza</th>
							</tr>
							<tr>
								<th scope="row">Competenza</th>
								<td id="competenza1Entrata">
									<s:property value="totaliStanziamentiCompetenzaEntrata1"/>
								</td>
								<td id="competenza1Spesa">
									<s:property value="totaliStanziamentiCompetenzaSpesa1"/>
								</td>
								<td id="competenza1Differenza">
									<s:property value="differenzaStanziamentiCompetenza1"/>
								</td>
							</tr>
	
						</table>
						<table class="table table-bordered">
	
							<tr>
								<th width="25%" scope="col">Esercizio <s:property value="%{annoEsercizioInt + 2}"/></th>
								<th width="25%" scope="col">Entrata</th>
								<th width="25%" scope="col">Spesa</th>
								<th width="25%" scope="col">Differenza</th>
							</tr>
							<tr>
								<th scope="row">Competenza</th>
								<td id="competenza2Entrata">
									<s:property value="totaliStanziamentiCompetenzaEntrata2"/>
								</td>
								<td id="competenza2Spesa">
									<s:property value="totaliStanziamentiCompetenzaSpesa2"/>
								</td>
								<td id="competenza2Differenza">
									<s:property value="differenzaStanziamentiCompetenza2"/>
								</td>
							</tr>
	
						</table>
						<p>
							<s:url var="surl" action="%{currentCrumb.action}" namespace="%{currentCrumb.namespace}" includeContext="false" />
							<c:url var="url" value="${surl}">
								<c:forEach var="p" items="${currentCrumb.params}">
									<c:forEach var="v" items="${p.value}">${v}
										<c:param name="${p.key}" value="${v}" />
									</c:forEach>
								</c:forEach>
							</c:url>
							<s:a cssClass="btn" href="%{#attr['url']}">indietro</s:a>
						</p>
					</form>
				</div>
			</div>
		</div>
	</div>

	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="${jspath}common.js"></script>
		
</body>
</html>