<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>

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
					<s:form id="formInserisciTipoBene" cssClass="form-horizontal" novalidate="novalidate" action="ricercaCategoriaCespiti_effettuaRicerca">
						<h3 id="titolo">Consulta registro prime note definitive verso inventario contabile <s:property value="descrizionePrimaNota"/> </h3>
						<s:include value="/jsp/include/messaggi.jsp" />
						<s:hidden id="uidPrimaNota" name="primaNota.uid"/>
						<div class="step-content">
							<br/>
							<div class="step-pane active" id="step1">
								<fieldset class="form-horizontal">
									
								</fieldset>
								<s:include value="/jsp/cespiti/registroa/tabellaMovimentiDettaglioRegistroA.jsp"/>																
							</div>
						</div>
						<p class="margin-medium">
							<s:include value="/jsp/include/indietro.jsp" />
						</p>
					</s:form>
				</div>
			</div>
		</div>
	</div>
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="${jspath}cespiti/registroa/gestioneRegistroA.js"></script>
	<script type="text/javascript" src="${jspath}cespiti/registroa/consultaRegistroA.js"></script>
</body>
</html>