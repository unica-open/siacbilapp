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
				<input type="hidden" id="DISABLED"/>
				<s:hidden id="asyncId" name="idOperazioneAsincrona" />
				<s:form cssClass="form-horizontal" action="emissioneOrdinativiPagamentoQuota_completeStep2" novalidate="novalidate" method="post">
					<s:include value="/jsp/include/messaggi.jsp" />
					<h3>Emissione ordinativi di pagamento</h3>
					<div class="wizard">
						<ul class="steps">
							<li class="complete" data-target="#step1"><span class="badge badge-success">1</span>Ricerca dettagli<span class="chevron"></span></li>
							<li class="active" data-target="#step2"><span class="badge">2</span>Emissione<span class="chevron"></span></li>
						</ul>
					</div>
					<div class="step-content">
						<div id="step2" class="step-pane active">
							<fieldset class="form-horizontal">
								<p class="margin-medium">
									<s:property value="parametriRicerca" escapeHtml="false" />
								</p>
								<h4 class="step-pane">
									<span class="num_result"><s:property value="numeroQuote"/></span> Dettagli trovati -
									Importo totale da emettere: <s:property value="totaleQuote" />
								</h4>
								<table class="table table-hover tab_left dataTable" id="tabellaQuote">
									<thead>
										<tr>
											<th class="span1">
												<input type="checkbox" class="tooltip-test" data-original-title="Seleziona tutti" id="selezionaTuttiQuote" disabled />
											</th>
											<th>&nbsp;</th>
											<th>Provvedimento</th>
											<th>Elenco</th>
											<th>Soggetto</th>
											<th><abbr title="Modalità di pagamento">MdP</abbr></th>
											<th class="tab_Right">Documento-Quota</th>
											<th>&nbsp;</th>
											<th>Liquidazione</th>
											<th>Capitolo</th>
											<th>Movimento</th>
											<th>Distinta</th>
											<th>Conto Tesoreria</th>
											<th>Data Esec. Pag.</th>
											<th class="tab_Right">Importo</th>
										</tr>
									</thead>
									<tbody>
									</tbody>
									<tfoot>
										<tr>
											<th colspan="14">Totale</th>
											<th class="tab_Right"><s:property value="totaleQuote" /></th>
										</tr>
									</tfoot>
								</table>
								<h4>DATI COMPLESSIVI</h4>
								<table class="table table-hover tab_left dataTable">
									<thead>
										<tr>
											<th class="ReportTable">Totale</th>
											<td class="ReportTable"><span class="marginLeft2" id="totaleQuoteSelezionate"></span></td>
										</tr>
										<tr>
											<th class="ReportTable span5">Nota dell'Ordinativo</th>
											<td colspan="3" class="span7">
												<div>
													<span class="marginLeft2">
														<s:textfield id="nota" name="nota" cssClass="lbTextSmall span11" disabled="true" />
													</span>
												</div>
											</td>
										</tr>
										<tr>
											<th class="ReportTable span5">Eventuale sostituzione informazioni presenti</th>
											<td colspan="3" class="span7">
												<div class="span12">
													<div class="span6">
														<span class="al span3">
															<label class="radio inline" for="distinta">Distinta</label>
														</span>
														<s:select list="listaDistinta" name="distinta.uid" listKey="uid" listValue="%{codice + ' - ' + descrizione}"
															headerKey="0" headerValue="" cssClass="lbTextSmall span9" id="distinta" disabled="true" />
													</div>
													<div class="span6">
														<span class="al span3">
															<label class="radio inline" for="contoTesoreria"> Conto Tesoriere</label>
														</span>
														<s:select list="listaContoTesoreria" name="contoTesoreria.uid" id="contoTesoreria" cssClass="lbTextSmall span9"
															listKey="uid" listValue="%{codice + ' - ' + descrizione}" headerKey="0" headerValue="" disabled="true" />
													</div>
												</div>
												<div class="span12 margin-medium no-margin-left">
													<div class="span6">
														<span class="al span3">
															<label class="radio inline" for="listaBollo">Bollo</label>
														</span>
														<s:select list="listaBollo" name="codiceBollo.uid" listKey="uid" listValue="%{codice + ' - ' + descrizione}"
															headerKey="0" headerValue="" cssClass="lbTextSmall span9" id="codiceBollo" disabled="true" />
													</div>
													<div class="span6">
														<span class="al span3">
															<label class="radio inline" for="listaCommissioni">Commis<wbr/>sioni</label>
														</span>
														<!-- task-291 -->
														<s:select list="listaCommissioniDocumento" name="commissioneDocumento.descrizione" listKey="descrizione" listValue="%{codice + ' - ' + descrizione}"
															headerKey="" headerValue="" cssClass="lbTextSmall span9" id="commissioneDocumento" disabled="true"/>
													</div>
												</div>
												<div class="span12 margin-medium no-margin-left">
													<div class="span6">
														<span class="al span3">
															<label class="radio inline" for="dataScadenza">Data esecuzione pagamento</label>
														</span>
														<s:textfield id="dataScadenza" name="dataScadenza" cssClass="lbTextSmall span9 datepicker" size="10" disabled="true" />
													</div>
													<span class="span3">
														<span class="al span6">
															<label class="radio inline" for="flagNoDataScadenza">Nessuna Data</label>
														</span>	
														<s:checkbox id="flagNoDataScadenza" name="flagNoDataScadenza" disabled="true" />
													</span>	
													<span class="span3">
														<span class="al span6">
															<label class="radio inline" for="flagDaTrasmettere">Da trasmettere</label>
														</span>	
														<s:checkbox id="flagDaTrasmettere" name="flagDaTrasmettere" disabled="true" />
													</span>
												</div>
												<div class="span12 margin-medium no-margin-left">
													<div class="span6">
														<span class="al span3">
															<label class="radio inline" for="dataScadenza"><s:property value="etichettaClassificatoreStipendi"/></label>
														</span>
														<s:select list="listaClassificatoreStipendi" name="classificatoreStipendi.uid" listKey="uid" id="classificatoreStipendi" disabled="true"cssClass="lbTextSmall span9"
															listValue="%{descrizione}" headerKey="" headerValue="" />
													</div>
												</div>
											</td>
										</tr>
									</thead>
								</table>
							</fieldset>
						</div>
					</div>
					<p class="margin-medium">
						<s:a cssClass="btn" action="emissioneOrdinativiPagamentoQuota_backToStep1" >indietro</s:a>
					</p>
				</s:form>
			</div>
		</div>
	</div>
	
	<%-- Caricamento del footer --%>
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/ordinativo/emissionePagamentoStep2Quota.js"></script>

</body>
</html>