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
			<div class="span12 contentPage">
				<s:form id="formAssociaMovimentoAllegatoAttoStep1" cssClass="form-horizontal" novalidate="novalidate" action="associaMovimentoAllegatoAtto_completeStep1" method="post">
					<s:hidden id="nomeAzioneDecentrata" value="%{nomeAzioneDecentrata}" data-maintain="" />
					<s:hidden name="uidAllegatoAtto" data-maintain="" />
					<s:include value="/jsp/include/messaggi.jsp" />
					<h3><s:property value="denominazioneAllegatoAtto"/></h3>
					<h4>Associa movimenti</h4>
					<div class="wizard">
						<ul class="steps">
							<li class="active" data-target="#step1"><span class="badge">1</span>Ricerca soggetto<span class="chevron"></span></li>
							<li data-target="#step2"><span class="badge">2</span>Associa movimenti<span class="chevron"></span></li>
						</ul>
					</div>
					<div class="step-content">
						<div id="step1" class="step-pane active">
							<h4 class="step-pane">Soggetto
								<span id="datiRiferimentoSoggettoSpan">
									<s:property value="descrizioneCompletaSoggetto" />
								</span>
							</h4>
							<s:hidden id="HIDDEN_soggettoDenominazione" name="soggetto.denominazione" />
							<s:hidden id="HIDDEN_soggettoCodiceFiscale" name="soggetto.codiceFiscale" />
							<fieldset class="form-horizontal">
								<div class="control-group">
									<label class="control-label" for="codiceSoggettoSoggetto">Codice *</label>
									<div class="controls">
										<s:textfield id="codiceSoggettoSoggetto" cssClass="lbTextSmall span2" name="soggetto.codiceSoggetto" maxlength="20" placeholder="codice" required="required" />
										<span class="radio guidata">
											<a href="#" class="btn btn-primary" id="pulsanteAperturaCompilazioneGuidataSoggetto">compilazione guidata</a>
										</span>
									</div>
								</div>
							</fieldset>
							
							<div id="accordionSedeSecondariaSoggetto" class="accordion hide">
								<div class="accordion-group">
									<div class="accordion-heading">
										<a href="#collapseSedeSecondariaSoggetto" data-parent="#accordionSedeSecondariaSoggetto" data-toggle="collapse" class="accordion-toggle collapsed" data-overlay="">
											Sedi secondarie <span id="SPAN_sediSecondarieSoggetto"></span><span class="icon">&nbsp;</span>
										</a>
									</div>
									<div class="accordion-body collapse" id="collapseSedeSecondariaSoggetto">
										<s:include value="/jsp/soggetto/accordionSedeSecondariaSoggetto.jsp" />
									</div>
								</div>
							</div>
							<div id="accordionModalitaPagamentoSoggetto" class="accordion hide">
								<div class="accordion-group">
									<div class="accordion-heading">
										<a href="#collapseModalitaPagamento" data-parent="#accordionModalitaPagamentoSoggetto" data-toggle="collapse" class="accordion-toggle collapsed" data-overlay="">
											Modalit&agrave; pagamento <span class="datiPagamento" id="SPAN_modalitaPagamentoSoggetto"></span><span class="icon">&nbsp;</span>
										</a>
									</div>
									<div class="accordion-body collapse" id="collapseModalitaPagamento">
										<s:include value="/jsp/soggetto/accordionModalitaPagamentoSoggetto.jsp" />
									</div>
								</div>
							</div>
						</div>
					</div>
					<p class="margin-medium">
						<s:include value="/jsp/include/indietro.jsp" />
						<button type="button" class="btn btn-secondary reset">annulla</button>
						<span class="pull-right">
							<s:submit cssClass="btn btn-primary" value="prosegui" />
						</span>
					</p>
					<s:include value="/jsp/soggetto/selezionaSoggetto_modale.jsp" />
				</s:form>
				<input type="hidden" id="oldSedeSecondariaSoggetto" value="<s:property value="sedeSecondariaSoggetto.uid" />" />
				<s:if test="%{modalitaPagamentoSoggetto != null && modalitaPagamentoSoggetto.modalitaPagamentoSoggettoCessione2 != null && modalitaPagamentoSoggetto.modalitaPagamentoSoggettoCessione2.uid != 0}">
					<input type="hidden" id="oldModalitaPagamentoSoggetto" value="<s:property value="modalitaPagamentoSoggetto.modalitaPagamentoSoggettoCessione2.uid" />" />
				</s:if><s:else>
					<input type="hidden" id="oldModalitaPagamentoSoggetto" value="<s:property value="modalitaPagamentoSoggetto.uid" />" />
				</s:else>
			</div>
		</div>
	</div>

	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/codiceFiscale.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/soggetto/ricerca.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/allegatoAtto/associaMovimento_step1.js"></script>
	
	
</body>
</html>