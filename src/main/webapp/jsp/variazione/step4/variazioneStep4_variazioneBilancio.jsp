<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
			<div class="span12 contentPage">
				<form class="form-horizontal">
					<s:include value="/jsp/include/messaggi.jsp" />
					<div id="MyWizard" class="wizard">
						<ul class="steps">
							<li data-target="#step1" class="complete"><span class="badge badge-success">1</span>Scegli<span class="chevron"></span></li>
							<li data-target="#step2" class="complete"><span class="badge badge-success">2</span>Definisci<span class="chevron"></span></li>
							<li data-target="#step3" class="complete"><span class="badge badge-success">3</span>Specifica<span class="chevron"></span></li>
							<li data-target="#step4" class="active"><span class="badge">4</span>Riepilogo<span class="chevron"></span></li>
						</ul>
					</div>
					<div class="step-content">
						<div class="step-pane active" id="step4">
							<dl class="dl-horizontal">
								<dt>Num. variazione</dt>
								<dd>&nbsp;<s:property value="riepilogo.numeroVariazione"/></dd>
								<dt>Stato</dt>
								<dd>&nbsp;<s:property value="riepilogo.statoVariazione.descrizione"/></dd>
								<dt>Applicazione</dt>
								<dd>&nbsp;<s:property value="riepilogo.applicazioneVariazione"/></dd>
								<dt>Descrizione</dt>
								<dd>&nbsp;<s:property value="riepilogo.descrizioneVariazione"/></dd>
								<dt>Tipo Variazione</dt>
								<dd>&nbsp;<s:property value="riepilogo.tipoVariazione.codice"/>&nbsp;-&nbsp;<s:property value="riepilogo.tipoVariazione.descrizione"/></dd>
								<s:if test="%{model.isDecentrato}">
									<dt>Data Apertura Proposta</dt>
									<dd>&nbsp;<s:property value="definisci.dataAperturaFormatted"/></dd>
									<dt>Direzione Proponente</dt>
									<dd>&nbsp;<s:property value="definisci.codiceDirezioneProponente"/></dd>
								</s:if>
								<dt>Note</dt>
								<dd>&nbsp;<s:property value="riepilogo.noteVariazione"/></dd>
							</dl>
							<div class="margin-large">
								<s:url action="redirectToCruscotto" var="indietroURL" />
								<s:a cssClass="btn" href="%{indietroURL}">Indietro</s:a>
							</div>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
	
	<s:include value="/jsp/include/footer.jsp"/>
	<s:include value="/jsp/include/javascript.jsp" />
</body>
</html>