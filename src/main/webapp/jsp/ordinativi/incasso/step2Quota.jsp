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
				<s:form cssClass="form-horizontal" action="emissioneOrdinativiIncassoQuota_completeStep2" novalidate="novalidate" method="post">
					<s:include value="/jsp/include/messaggi.jsp" />
					<h3>Emissione ordinativi di incasso</h3>
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
								<s:hidden id="HIDDEN_anno_datepicker" name="bilancio.anno"/>
								<table class="table table-hover tab_left dataTable" id="tabellaQuote">
									<thead>
										<tr>
											<th class="span1">
												<input type="checkbox" class="tooltip-test" data-original-title="Seleziona tutti" id="selezionaTuttiQuote">
											</th>
											<th>Provvedimento</th>
											<th>Elenco</th>
											<th>Soggetto</th>
											<th>Documento-Quota</th>
											<th><abbr title="Provvisorio di cassa">Provv.</abbr></th>
											<th class="tab_Right">Capitolo</th>
											<th class="tab_Right">Movimento</th>
											<th class="tab_Right">Data Scad.</th>
											<th class="tab_Right">Importo</th>
										</tr>
									</thead>
									<tbody>
									</tbody>
									<tfoot>
										<tr>
											<th colspan="9">Totale</th>
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
											<th class="ReportTable span5">Note al tesoriere</th>
											<td colspan="3" class="span7">
												<div>
													<span class="marginLeft2">
														<s:textfield id="nota" name="nota" cssClass="lbTextSmall span11" />
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
															headerKey="0" headerValue="" cssClass="lbTextSmall span9" id="distinta" />
													</div>
													<div class="span6">
														<span class="al span3">
															<label class="radio inline" for="contoTesoreria"> Conto Tesoriere</label>
														</span>
														<select name="contoTesoreria.uid" id="contoTesoreria" class="lbTextSmall span9">
															<option value="0"></option>
															<s:iterator value="listaContoTesoreria" var="ct">
																<option value="<s:property value="%{#ct.uid}"/>" data-vincolato="<s:property value="%{#ct.vincolato}"/>"><s:property value="%{codice + ' - ' + descrizione}"/></option>
															</s:iterator>
														</select>
													</div>
												</div>
												<div class="span12 margin-medium no-margin-left">

													<div class="span6">
														<span class="al span3">
															<label class="radio inline" for="listaBollo">Bollo</label>
														</span>
														<s:select list="listaBollo" name="codiceBollo.uid" listKey="uid" listValue="%{codice + ' - ' + descrizione}"headerKey="0" headerValue="" cssClass="lbTextSmall span9" id="codiceBollo" />
													</div>
												</div>
												<div class="span12 margin-medium no-margin-left">

													<div class="span6">
														<span class="al span3">
															<label class="radio inline" for="dataScadenza">Data scadenza </label>
														</span>
														<s:textfield id="dataScadenza" name="dataScadenza" cssClass="lbTextSmall span9 datepicker" size="10" />
													</div>
													<div class="span3">
														<span class="al span6">
															<label class="radio inline" for="flagNoDataScadenza">Nessuna Data</label>
														</span>	
														<s:checkbox id="flagNoDataScadenza" name="flagNoDataScadenza" />
													</div>
													<div class="span3">
														<span class="al span6">
															<label class="radio inline" for="flagDaTrasmettere">Da trasmettere</label>
														</span>	
														<s:checkbox id="flagDaTrasmettere" name="flagDaTrasmettere" value="true" />
													</div>
												</div>
												<div class="span12 margin-medium no-margin-left">
													<div class="span6">
														<span class="al span3">
															<label class="radio inline" for="dataScadenza"><s:property value="etichettaClassificatoreStipendi"/></label>
														</span>
														<s:select list="listaClassificatoreStipendi" name="classificatoreStipendi.uid" listKey="uid" id="classificatoreStipendi" cssClass="lbTextSmall span9"
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
						<s:a cssClass="btn" action="emissioneOrdinativiIncassoQuota_backToStep1" >indietro</s:a>
						<button type="button" class="btn btn-primary pull-right hide" id="pulsanteEmissione" data-toggle="modal" data-target="#confermaEmissioneModal">
							emetti
						</button>
						<s:if test="gestioneContiVincolati">
							<input type="hidden" value="true" id ="enteGestioneContiVincolati" name="enteGestioneContiVincolati"/>
							<button type="button" class="btn btn-secondary pull-right hide" id="pulsanteControlloDisponibilitaConto">
								controlla disponibilit&agrave; sottoconto vincolato
							</button>
						</s:if>
					</p>
					<s:include value="/jsp/ordinativi/modaleConferma.jsp" />
				</s:form>
			</div>
		</div>
	</div>
	
	<%-- Caricamento del footer --%>
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/ordinativo/emissioneIncassoStep2Quota.js"></script>

</body>
</html>