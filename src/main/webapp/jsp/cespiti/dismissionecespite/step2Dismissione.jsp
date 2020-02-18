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
					<s:form id="formInserisciDismissioneCespite" cssClass="form-horizontal" novalidate="novalidate" action="%{baseUrl}_backToStep1">
						<s:hidden name="ambito" id="ambito" />
						<s:hidden name="ambitoCausaleInventario" id="ambitoCausaleInventario" />
						<s:include value="/jsp/include/messaggi.jsp" />
						
						
						<h3>Gestione dismissione <s:property value="chiaveDismissioneCespite"/></h3>
						
						<div class="wizard" id="MyWizard">
							<ul class="steps">
								<li data-target="#step1">
									<span class="badge">1</span>Anagrafica<span class="chevron"></span>
								</li>
								<li class="active" data-target="#step2">
									<span class="badge">2</span>Collega cespiti<span class="chevron"></span>
								</li>
							</ul>
						</div>
						
						<div class="step-content">
							<div class="step-pane active" id="step2">
							<s:hidden id="HIDDEN_baseUrl" name="baseUrl"/>
								<fieldset class="form-horizontal">
									<s:include value="/jsp/cespiti/dismissionecespite/include/tabellaCespitiCollegati.jsp"/>
								<p>
									<button id="pulsanteAperturaModaleCespite" type ="button" class="btn btn-primary pull-right">collega</button>
								</p>
								</fieldset>
							</div>
						</div>
						<p class="margin-medium">
						<s:submit id="pulsanteRedirezioneIndietro" cssClass="btn" value="indietro" />
							<button id="pulsanteEffettuascritture" type="button" class="btn btn-primary pull-right">effettua scritture</button>
						</p>
					</s:form>
				</div>
			</div>
		</div>
	</div>
	<s:include value="/jsp/cespiti/include/modaleEliminazione.jsp"/>
	<s:include value="/jsp/cespiti/cespite/include/modaleRicercaCespite.jsp"/>
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="${jspath}cespiti/cespite/ricercaCespite_modale.js"></script>
	<script type="text/javascript" src="${jspath}cespiti/dismissionecespite/tabellaCespitiCollegati.js"></script>
	<script type="text/javascript" src="${jspath}cespiti/dismissionecespite/step2Dismissione.js"></script>
</body>
</html>