<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<%-- Inclusione head e CSS --%>
<s:include value="/jsp/include/head.jsp" />
</head>
<body>
	<%-- Inclusione header --%>
	<s:include value="/jsp/include/header.jsp" />
	
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12 contentPage">
				<s:form cssClass="form-horizontal" action="%{urlStep3}" novalidate="novalidate" id="formRichiestaEconomale">
					<s:include value="/jsp/include/messaggi.jsp" />
					<h3>Anticipo spese per missione - Rendiconto</h3>
					<s:if test="isAggiornamento">
						<h3><s:property value="stringaRiepilogoRichiestaEconomale" /></h3>
					</s:if>
					<div class="wizard" id="MyWizard">
						<ul class="steps">
							<li data-target="#step1"><span class="badge badge-success">1</span>Dati richiesta<span class="chevron"></span></li>
							<li data-target="#step2"><span class="badge badge-success">2</span>Dettaglio<span class="chevron"></span></li>
							<li data-target="#step3" class="active"><span class="badge">3</span>Riepilogo<span class="chevron"></span></li>
						</ul>
					</div>
					<div class="step-content">
						<div id="step3" class="step-pane active">
							<s:include value="/jsp/cassaEconomale/richieste/anticipoSpesePerMissione/rendiconto/fieldsetRiassuntivo.jsp" />
						</div>
					</div>
					<p class="margin-medium">
						<s:include value="/jsp/include/indietro.jsp" />
						<button type="button"   class="btn btn-primary pull-right" id="pulsanteStampaRicevuta">stampa ricevuta</button>
					</p>
				</s:form>
			</div>
		</div>
	</div>
	<s:include value="/jsp/cassaEconomale/stampe/modaleConfermaStampaRicevuta.jsp" />
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/cassaEconomale/richieste/anticipoSpesePerMissione/rendiconto.step3.js"></script>
	
</body>
</html>