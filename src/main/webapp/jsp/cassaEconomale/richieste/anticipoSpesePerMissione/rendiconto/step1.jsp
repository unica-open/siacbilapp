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
				<s:hidden name="uidFattura" id="HIDDEN_uidFattura" />
				<s:hidden name="codiceAmbito" id="HIDDEN_codiceAmbito" />
				<s:hidden name="baseUrl" id="HIDDEN_baseUrl" />
				<s:hidden name="restituzioneTotale" id="HIDDEN_flagRestituzioneTotale" />
				<s:hidden name="urlRestituzioneTotale" id="HIDDEN_urlRestituzioneTotale" />
				<s:hidden name="urlRestituzioneAltroUfficio" id="HIDDEN_urlRestituzioneAltroUfficio" />
				
				<s:form cssClass="form-horizontal" action="%{urlStep1}" novalidate="novalidate" id="formRichiestaEconomale">
					<s:include value="/jsp/include/messaggi.jsp" />
					<s:hidden name="rendicontoRichiesta.richiestaEconomale.uid" id="uidRichiestaEconomale" />
					<s:hidden name="rendicontoRichiesta.uid" id="uidRendicontoRichiesta" />
					<s:hidden name="rendicontoRichiesta.richiestaEconomale.statoOperativoRichiestaEconomale" id="statoOperativoRichiestaEconomaleRichiestaEconomale" />
					<s:hidden name="rendicontoRichiesta.richiestaEconomale.numeroRichiesta" id="numeroRichiestaRichiestaEconomale" />
					<s:hidden name="rendicontoRichiesta.numeroRendicontoStampato" id="numeroRendicontoStampatoRendicontoRichiesta" />
					<s:hidden name="rendicontoRichiesta.dataRendicontoStampato" id="dataRendicontoStampatoRendicontoRichiesta" />
					<h3>Anticipo spese per missione - Rendiconto</h3>
					<s:if test="isAggiornamento">
						<h3><s:property value="stringaRiepilogoRichiestaEconomale" /></h3>
					</s:if>
					<div class="wizard" id="MyWizard">
						<ul class="steps">
							<li class="active" data-target="#step1"><span class="badge">1</span>Dati richiesta<span class="chevron"></span></li>
							<li data-target="#step2"><span class="badge">2</span>Dettaglio<span class="chevron"></span></li>
							<li data-target="#step3"><span class="badge">3</span>Riepilogo<span class="chevron"></span></li>
						</ul>
					</div>
					<div class="step-content">
						<div id="step1" class="step-pane active">
							<fieldset class="form-horizontal">
								<s:if test="!isAggiornamento">
									<h4><s:property value="stringaRiepilogoRichiestaEconomale" /></h4>
								</s:if>
								<s:include value="/jsp/cassaEconomale/richieste/include/datiRichiestaRendiconto.jsp" />
								
								<h4>Altri dati</h4>
								<div class="control-group">
									<label class="control-label" for="descrizioneDellaRichiestaRichiestaEconomale">Descrizione *</label>
									<div class="controls">
										<s:textfield id="descrizioneDellaRichiestaRichiestaEconomale" name="rendicontoRichiesta.richiestaEconomale.datiTrasfertaMissione.motivo" cssClass="span6" required="true" />
									</div>
								</div>
								<div class="control-group">
									<label class="control-label"for="noteRendicontoRichiestaRichiestaEconomale">Note</label>
									<div class="controls">
										<s:textarea id="noteRichiestaEconomale" name="rendicontoRichiesta.note" rows="2" cols="15" cssClass="span9"></s:textarea>
									</div>
								</div>
								
								<h4>Giustificativi</h4>
								<table class="table table-hover tab_left" id="tabellaGiustificativi">
									<thead>
										<tr>
											<th class="span9">Tipo</th>
											<th class="span3">Anno</th>
											<th class="span3">N. Protocollo</th>
											<th class="span4">Data emissione</th>
											<th class="span3">Numero</th>
											<th class="span1"></th>
											<th class="tab_Right span4">Importo</th>
											<th>&nbsp;</th>
										</tr>
									</thead>
									<tbody>
									</tbody>
									<tfoot>
										<tr class="borderBottomLight">
											<th colspan="6">Totale</th>
											<th class="tab_Right" id="totaleGiustificativi"></th>
											<th>&nbsp;</th>
										</tr>
										<tr class="borderBottomLight">
											<th colspan="6">Importo da restituire</th>
											<th class="tab_Right" id="importoDaRestituire"></th>
											<th>&nbsp;</th>
										</tr>
										<tr class="borderBottomLight">
											<th colspan="6">Importo da integrare</th>
											<th class="tab_Right" id="importoDaIntegrare"></th>
											<th>&nbsp;</th>
										</tr>
									</tfoot>
								</table>
								<p class="margin-medium">
									<button type="button" class="btn btn-secondary" id="pulsanteInserimentoDati">inserisci dati in elenco</button>
									<span class="pull-right clear">
										<button type="button" class="btn btn-primary" data-target="#modaleRestituzioneTotale" data-toggle="modal">restituzione totale</button>
									</span>
									<span class="pull-right clear">
										<button type="button" class="btn btn-primary" data-target="#modaleRestituzioneAltroUfficio" data-toggle="modal" <s:if test="cassaContanti"> disabled="true"</s:if>>restituzione altro ufficio</button>
									</span>
								</p>
								<div class="clear"></div>
								<br/>
							</fieldset>
						</div>
					</div>
					<p class="margin-medium">
						<s:include value="/jsp/include/indietro.jsp" />
						<a data-href="<s:property value="urlAnnullaStep1"/>" class="btn secondary" id="pulsanteAnnullaStep1">annulla</a>
						<s:submit value="prosegui" cssClass="btn btn-primary pull-right" />
					</p>
				</s:form>
				<s:include value="/jsp/cassaEconomale/tabelle/modaleAnnullamento.jsp" />
				<s:include value="/jsp/cassaEconomale/richieste/include/modaleGiustificativoRendiconto.jsp" />
				<s:include value="/jsp/cassaEconomale/richieste/include/modaleRestituzioneTotale.jsp" />
				<s:include value="/jsp/cassaEconomale/richieste/include/modaleRestituzioneAltroUfficio.jsp" />
			</div>
		</div>
	</div>
	
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/cassaEconomale/richieste/anticipoSpesePerMissione/rendiconto.step1.js"></script>
	
</body>
</html>