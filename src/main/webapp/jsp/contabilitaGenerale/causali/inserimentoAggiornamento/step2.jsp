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
				<s:hidden name="tipoCausaleIntegrata" id="HIDDEN_tipoCausaleIntegrata" />
				<s:hidden name="tipoCausaleDiRaccordo" id="HIDDEN_tipoCausaleDiRaccordo" />
				
				<s:form cssClass="form-horizontal" action="%{urlStep2}" novalidate="novalidate" id="formCausaleEP">
					<s:include value="/jsp/include/messaggi.jsp" />
					<s:hidden name="causaleEP.uid" id="uidCausaleEP" />
					<s:hidden name="richiestaEconomale.statoOperativoCausaleEP" id="statoOperativoCausaleEPCausaleEP" />
					<h3><s:property value="denominazioneWizard"/></h3>
					<s:if test="isAggiornamento">
						<h3><s:property value="stringaRiepilogoRichiestaEconomale" /></h3>
					</s:if>
					<div class="wizard" id="MyWizard">
						<ul class="steps">
							<li class="complete" data-target="#step1"><span class="badge badge-success">1</span>Dati principali<span class="chevron"></span></li>
							<li class="active" data-target="#step2"><span class="badge">2</span>Dettaglio conti associati<span class="chevron"></span></li>
							<li data-target="#step3"><span class="badge">3</span>Riepilogo<span class="chevron"></span></li>
						</ul>
					</div>
					<div class="step-content">
						<div id="step2" class="step-pane active">
							<fieldset class="form-horizontal">
								<h4>Lista conti associati alla causale</h4>
								<table class="table table-hover tab_left" id="tabellaConti">
									<thead>
										<tr>
											<th>Classe</th>
											<th>Codice conto</th>
											<th>Segno Conto</th>
											<th>Utilizzo Conto</th>
											<th>Utilizzo Importo</th>
											<th>Tipo Importo</th>
											<th>&nbsp;</th>
										</tr>
									</thead>
									<tbody>
									</tbody>
								</table>
								<p>
									<button type="button" class="btn btn-secondary" id="pulsanteApriCollapseConti">inserisci dati in elenco</button>
								</p>
								<s:include value="/jsp/contabilitaGenerale/causali/include/collapseInserimentoConto.jsp" />
							</fieldset>
						</div>
					</div>
					
					<p class="margin-medium">
						<s:a href="%{urlBackToStep1}" cssClass="btn" id="pulsanteRedirezioneIndietro">indietro</s:a>
						<s:a href="%{urlAnnullaStep2}" cssClass="btn btn-secondary" id="pulsanteAnnullaStep2">annulla</s:a>
						<s:submit value="prosegui" cssClass="btn btn-primary pull-right" />
					</p>
				</s:form>
				<s:include value="/jsp/contabilitaGenerale/include/modaleRicercaConto.jsp" />
				<s:include value="/jsp/contabilitaGenerale/causali/include/modaleAggiornamentoConto.jsp" />
				<s:include value="/jsp/contabilitaGenerale/causali/include/modaleEliminazione.jsp" />
			</div>
		</div>
	</div>
	
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/contabilitaGenerale/ricercaConto.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/contabilitaGenerale/causali/inserisci.aggiorna.step2.js"></script>
	
</body>
</html>