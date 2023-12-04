<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/struts-json-tags" prefix="json"%>
<%@ taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

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
					<s:include value="/jsp/include/messaggi.jsp" />
					<form method="get" action="#">
						<s:hidden name="testoZeroVariazioni" id="testoZeroVariazioni" />
						<h3>Capitolo <s:property value="capitolo.numeroCapitolo"/>&nbsp;/&nbsp;<s:property value="capitolo.numeroArticolo"/>
							<s:if test="gestioneUEB">
								/&nbsp;<s:property value="capitolo.numeroUEB"/>
							</s:if>
						</h3>
						
						<div class="accordion" id="accordionPadre">
							<table class="table-accordion" id="tabellaDettaglioVariazioni">
								<thead>
									<tr>
										<th></th>
									</tr>
								</thead>
								<tbody id="componentiCompetenzaDettaglioVar">
								</tbody>
							</table>
							<s:hidden id="idAzioneReportVariazioni" name="idAzioneReportVariazioni"/>
						</div>
						<p>
						  <s:url var="surl" action="%{currentCrumb.action}" namespace="%{currentCrumb.namespace}" includeContext="false" />
							<c:set var="openTabGiaPresente" value="false" />
							<c:url var="url" value="${surl}">
								<c:forEach var="p" items="${currentCrumb.params}">
									<c:forEach var="v" items="${p.value}">${v}
										<c:if test="${p.key != 'openTab'}">
											<c:param name="${p.key}" value="${v}" />
										</c:if>
									</c:forEach>
								</c:forEach>
								<c:param name="openTab" value="tabMovimenti" />
							</c:url>
							<s:a cssClass="btn" href="%{#attr['url']}">indietro</s:a>
						</p>
					</form>
				</div>
			</div>
		</div>
	</div>
	<%-- Caricamento del footer --%>
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/capitolo/dettaglioVariazioni.js"></script>
</body>
</html>