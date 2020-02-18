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
				<s:form cssClass="form-horizontal" action="emissioneOrdinativiPagamentoElenco_completeStep2" novalidate="novalidate" method="post">
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
									<b>Provvedimento: </b><span class="infoTitle"><s:property value="descrizioneCompletaAttoAmministrativo" /></span>
								</p>
								<h4 class="step-pane">
									<span class="num_result"><s:property value="numeroElenchi"/></span> Elenchi trovati -
									Totale da emettere: <s:property value="totaleSpeseCollegate" /> -
									Totale spese collegate: <s:property value="totaleElenchi" />
								</h4>
								<s:hidden id="HIDDEN_anno_datepicker" name="bilancio.anno"/>
								<table class="table table-hover tab_left dataTable" id="tabellaElenchi">
									<thead>
										<tr>
											<th class="span1">
												<input type="checkbox" class="tooltip-test" data-original-title="Seleziona tutti" id="selezionaTuttiElenchi" />
											</th>
											<th>Anno</th>
											<th>Numero</th>
											<th>Stato</th>
											<th>Data trasmissione</th>
											<th>Rich.DURC</th>
											<th>Data fine validita DURC</th>
											<th class="tab_Right">Importo Elenco</th>
											<th class="tab_Right">Importo da Emettere</th>
										</tr>
									</thead>
									<tbody>
									</tbody>
									<tfoot>
										<tr>
											<th colspan="5">Totali</th>
											<th class="tab_Right"><s:property value="totaleElenchi" /></th>
											<th class="tab_Right"><s:property value="totaleSpeseCollegate" /></th>
										</tr>
									</tfoot>
								</table>
								<h4>DATI COMPLESSIVI</h4>
								<table class="table table-hover tab_left dataTable">
									<thead>
										<tr>
											<th class="ReportTable">Totale</th>
											<td class="ReportTable"><span class="marginLeft2" id="totaleElenchiSelezionati"></span></td>
											<td class="ReportTable tab_Right">Spese collegate: <span class="marginLeft2" id="totaleSpeseCollegateSelezionati"></span></td>
											<td class="ReportTable tab_Right">Differenza: <span class="marginLeft2" id="differenzaSelezionati"></span></td>
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
														<s:select list="listaContoTesoreria" name="contoTesoreria.uid" id="contoTesoreria" cssClass="lbTextSmall span9"
															listKey="uid" listValue="%{codice + ' - ' + descrizione}" headerKey="0" headerValue="" />
													</div>
												</div>
												<div class="span12 margin-medium no-margin-left">
													<div class="span6">
														<span class="al span3">
															<label class="radio inline" for="listaBollo">Bollo</label>
														</span>
														<s:select list="listaBollo" name="codiceBollo.uid" listKey="uid" listValue="%{codice + ' - ' + descrizione}"
															headerKey="0" headerValue="" cssClass="lbTextSmall span9" id="codiceBollo" />
													</div>
													<div class="span6">
														<span class="al span3">
															<label class="radio inline" for="listaCommissioni">Commis<wbr/>sioni</label>
														</span>
														<s:select list="listaCommissioni" name="commissioneDocumento" id="commissioneDocumento" cssClass="lbTextSmall span9"
															listValue="%{descrizione}" headerKey="" headerValue="" />
													</div>
												</div>
												<div class="span12 margin-medium no-margin-left">
													<div class="span6">
														<span class="al span3">
															<label class="radio inline" for="dataScadenza">Data esecuzione pagamento</label>
														</span>
														<s:textfield id="dataScadenza" name="dataScadenza" cssClass="lbTextSmall span9 datepicker" size="10" />														
													</div>
													<div class="span3">
														<span class="al span6">
															<label class="radio inline" for="flagNoDataScadenza">Nessuna Data</label>
														</span>	
														<s:checkbox id="flagNoDataScadenza" name="flagNoDataScadenza"  />
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
						<s:a cssClass="btn" action="emissioneOrdinativiPagamentoElenco_backToStep1" >indietro</s:a>
						<button type="button" class="btn btn-primary pull-right hide" id="pulsanteEmissione" data-toggle="modal" data-target="#confermaEmissioneModal">
							emetti
						</button>
						<button type="button" class="btn btn-secondary pull-right hide" id="pulsanteControlloDisponibilitaCassa">
							controlla disponibilit&agrave; cassa
						</button>
					</p>
					<s:include value="/jsp/ordinativi/modaleConferma.jsp" />
				</s:form>
			</div>
		</div>
	</div>
	
	<%-- Caricamento del footer --%>
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="${jspath}ordinativo/emissionePagamentoStep2Elenco.js"></script>

</body>
</html>