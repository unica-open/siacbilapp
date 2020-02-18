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
								<th scope="row">
									<a id="competenzaTotaleAnno0" href="#tabellaComponentiAnno0" class="disabled">Competenza</a>
								</th>
								<td id="competenza0Entrata" class="text-right">
									<s:property value="totaliStanziamentiCompetenzaEntrata0"/>
								</td>
								<td id="competenza0Spesa" class="text-right">
									<s:property value="totaliStanziamentiCompetenzaSpesa0"/>
								</td>
								<td id="competenza0Differenza" class="text-right">
									<s:property value="differenzaStanziamentiCompetenza0"/>
								</td>
							</tr>
							<s:iterator value="%{componenteImportiAnno0}" var="compCapitolo" status="incr">
								<tr class="componentiCompetenzaRow0 hide">
									<td scope="row" class="componenti-competenza">
										<s:property value="#compCapitolo.descrizione"/>
									</td>
									<td class="text-right">
										<%--<s:property value="#compCapitolo.importoEntrata"/>--%>
									</td>
									<td class="text-right">
										<s:property value="#compCapitolo.importoSpesa"/>
									</td>
									<td class="text-right">
										<%--<s:property value="#compCapitolo.importoDifferenza"/>--%>
									</td>
								</tr>
							</s:iterator>
							<tr>
								<th scope="row">Residuo</th>
								<td id="residui0Entrata" class="text-right">
									<s:property value="totaliStanziamentiResiduiEntrata0"/>
								</td>
								<td id="residui0Spesa" class="text-right">
									<s:property value="totaliStanziamentiResiduiSpesa0"/>
								</td>
								<td id="residui0Differenza" class="text-right">
									<s:property value="differenzaStanziamentiResidui0"/>
								</td>
							</tr>
							<tr>
								<th scope="row">Cassa</th>
								<td id="cassa0Entrata" class="text-right">
									<s:property value="totaliStanziamentiCassaEntrata0"/>
								</td>
								<td id="cassa0Spesa" class="text-right">
									<s:property value="totaliStanziamentiCassaSpesa0"/>
								</td>
								<td id="cassa0Differenza" class="text-right">
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
								<th scope="row">
									<a id="competenzaTotaleAnno1" href="#tabellaComponentiAnno1"  class="disabled" >Competenza</a>
								</th>
								<td id="competenza1Entrata" class="text-right">
									<s:property value="totaliStanziamentiCompetenzaEntrata1"/>
								</td>
								<td id="competenza1Spesa" class="text-right">
									<s:property value="totaliStanziamentiCompetenzaSpesa1"/>
								</td>
								<td id="competenza1Differenza" class="text-right">
									<s:property value="differenzaStanziamentiCompetenza1"/>
								</td>
							</tr>
							<s:iterator value="%{componenteImportiAnno1}" var="compCapitolo" status="incr">
								<tr class="componentiCompetenzaRow1 hide">
									<td scope="row" class="componenti-competenza">
										<s:property value="#compCapitolo.descrizione"/>
									</td>
									<td class="text-right">
										<%--<s:property value="#compCapitolo.importoEntrata"/>--%>
									</td>
									<td class="text-right">
										<s:property value="#compCapitolo.importoSpesa"/>
									</td>
									<td class="text-right">
										<%--<s:property value="#compCapitolo.importoDifferenza"/>--%>
									</td>
								</tr>
							</s:iterator>
						</table>
						<table class="table table-bordered">
	
							<tr>
								<th width="25%" scope="col">Esercizio <s:property value="%{annoEsercizioInt + 2}"/></th>
								<th width="25%" scope="col">Entrata</th>
								<th width="25%" scope="col">Spesa</th>
								<th width="25%" scope="col">Differenza</th>
							</tr>
							<tr>
								<th scope="row">
									<a id="competenzaTotaleAnno2" href="#tabellaComponentiAnno2"  class="disabled" >Competenza</a>
								</th>
								<td id="competenza2Entrata" class="text-right">
									<s:property value="totaliStanziamentiCompetenzaEntrata2"/>
								</td>
								<td id="competenza2Spesa" class="text-right">
									<s:property value="totaliStanziamentiCompetenzaSpesa2"/>
								</td>
								<td id="competenza2Differenza" class="text-right">
									<s:property value="differenzaStanziamentiCompetenza2"/>
								</td>
							</tr>
							<s:iterator value="%{componenteImportiAnno2}" var="compCapitolo" status="incr">
								<tr class="componentiCompetenzaRow2 hide">
									<td scope="row" class="componenti-competenza">
										<s:property value="#compCapitolo.descrizione"/>
									</td>
									<td class="text-right">
										<%--<s:property value="#compCapitolo.importoEntrata"/>--%>
									</td>
									<td class="text-right">
										<s:property value="#compCapitolo.importoSpesa"/>
									</td>
									<td class="text-right">
										<%--<s:property value="#compCapitolo.importoDifferenza"/>--%>
									</td>
								</tr>
							</s:iterator>
	
						</table>
						<p>
							<%--<s:url var="surl" action="%{currentCrumb.action}" namespace="%{currentCrumb.namespace}" includeContext="false" />
							<c:url var="url" value="${surl}">
								<c:forEach var="p" items="${currentCrumb.params}">
									<c:forEach var="v" items="${p.value}">${v}
										<c:param name="${p.key}" value="${v}" />
									</c:forEach>
								</c:forEach>
							</c:url>
							<s:a cssClass="btn" href="%{#attr['url']}">indietro</s:a>--%>
							
							<s:a cssClass="btn" action="aggiornaCapUscitaPrevisione.do" id="pulsanteRedirezioneIndietro">
								<s:param name="uidDaAggiornare" value="%{capitoloUscitaPrevisione.uid}"/>
								<s:param name="daAggiornamento" value="true"/>
								indietro
							</s:a>
						</p>
					</form>
				</div>
			</div>
		</div>
	</div>
	
	
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="${jspath}common.js"></script>
	<script type="text/javascript" src="${jspath}capitoloUscitaPrevisione/visualizzaTotaliPrevisione.js"></script>
		
</body>
</html>