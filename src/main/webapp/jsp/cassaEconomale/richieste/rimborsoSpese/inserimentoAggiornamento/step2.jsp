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
				<s:hidden name="baseUrl" id="HIDDEN_baseUrl" />
				<s:hidden name="uidValutaEuro" id="HIDDEN_uidValutaEuro" />
				<s:form cssClass="form-horizontal" action="%{urlStep2}" novalidate="novalidate" id="formRichiestaEconomale">
					<s:include value="/jsp/include/messaggi.jsp" />
					<h3>Rimborso spese</h3>
					<s:if test="isAggiornamento">
						<h3><s:property value="stringaRiepilogoRichiestaEconomale" /></h3>
					</s:if>
					<div class="wizard" id="MyWizard">
						<ul class="steps">
							<li data-target="#step1"><span class="badge badge-success">1</span>Dati richiesta<span class="chevron"></span></li>
							<li data-target="#step2" class="active"><span class="badge">2</span>Dettaglio<span class="chevron"></span></li>
							<li data-target="#step3"><span class="badge">3</span>Riepilogo<span class="chevron"></span></li>
						</ul>
					</div>
					<div class="step-content">
						<div id="step2" class="step-pane active">
							<fieldset class="form-horizontal">
								<s:include value="/jsp/cassaEconomale/richieste/include/fieldsetImpegnoStep2.jsp" />
								<div class="clear"></div>
								<br/>
								<s:if test="%{!isAggiornamento || richiestaEconomalePrenotata}">
									<s:include value="/jsp/cassaEconomale/richieste/include/fieldsetMovimentoStep2Richiesta.jsp" />
								</s:if>
							</fieldset>
						</div>
					</div>
					<p class="margin-medium">
						<!--  TODO ACTION  -->
						<s:a cssClass="btn" action="%{urlBackToStep1}" id="pulsanteRedirezioneIndietro">indietro</s:a>
						<a data-href="<s:property value="urlAnnullaStep2"/>" class="btn secondary" id="pulsanteAnnullaStep2">annulla</a>
						<s:submit value="salva" cssClass="btn btn-primary pull-right" />
					</p>
				</s:form>
				<s:include value="/jsp/movimentoGestione/modaleImpegno.jsp"></s:include>
				<s:include value="/jsp/cassaEconomale/richieste/include/modaleSelezioneIban.jsp"></s:include>
			</div>
		</div>
	</div>
	
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="${jspath}movimentoGestione/ricercaImpegnoOttimizzato.js"></script>
	<script type="text/javascript" src="${jspath}cassaEconomale/richieste/rimborsoSpese/inserisci.aggiorna.step2.js"></script>
	
</body>
</html>