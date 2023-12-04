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
					<s:form id="formInserisciVariazioneCespite_step2" cssClass="form-horizontal" novalidate="novalidate" action="%{baseUrl}_enterStep3">
						<h3 id="titolo"><s:property value="formTitle"/></h3>
						<s:include value="/jsp/include/messaggi.jsp" />
						<div class="wizard">
							<ul class="steps">
								<li data-target="#step1">
									<span class="badge badge-success">1</span>cerca cespite <span class="chevron"></span>
								</li>
								<li class="active" data-target="#step2">
									<span class="badge">2</span>seleziona cespite<span class="chevron"></span>
								</li>
								<li data-target="#step3">
									<span class="badge">3</span><s:property value="wizardStep3" /><span class="chevron"></span>
								</li>
							</ul>
						</div>
						<div class="step-content">
							<div class="step-pane active" id="step2">
								<table id="tabellaCespiti" class="table table-striped table-bordered table-hover dataTable">
									<thead>
										<tr>
											<th>Codice</th>
											<th>Descrizione</th>
											<th>Tipo bene</th>
											<th>Classificazione</th>
											<th>Inventario</th>
											<th>Stato</th>
											<th></th>
										</tr>
									</thead>
								</table>
							</div>
						</div>
						<p class="margin-medium">
							<s:a cssClass="btn" action="%{baseUrl}_backToStep1" id="pulsanteRedirezioneIndietro">indietro</s:a>
						</p>
					</s:form>
				</div>
			</div>
		</div>
	</div>
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/cespiti/variazionecespite/inserisciVariazioneCespite_step2.js"></script>
</body>
</html>