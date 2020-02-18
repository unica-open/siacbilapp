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
				<s:form cssClass="form-horizontal" action="#" novalidate="novalidate" id="formFatturaElettronica">
					<s:include value="/jsp/include/messaggi.jsp" />
					<h3>Dettaglio FEL selezionato <s:property value="fatturaFEL.numero" /></h3>
					<div class="step-content">
						<div id="step1" class="step-pane active">
							<fieldset class="form-horizontal">
								<div class="boxOrSpan2">
									<div class="boxOrInLeft">
										<p>Estremi della fattura</p>
										<ul class="htmlelt">
											<li>
												<dfn>Tipo</dfn>
												<dl><s:property value="estremiDellaFatturaTipo"/>&nbsp;</dl>
											</li>
											<li>
												<dfn>Data ricezione</dfn>
												<dl><s:property value="fatturaFEL.portaleFattureFEL.dataRicezione"/>&nbsp;</dl>
											</li>
											<li>
												<dfn>Data</dfn>
												<dl><s:property value="fatturaFEL.data"/>&nbsp;</dl>
											</li>
											<li>
												<dfn>Anno/Numero - Data (Protocollo)</dfn>
												<dl><s:property value="estremiDellaFatturaAnnoNumeroDataProtocollo" /></dl>
											</li>
											<li>
												<dfn>Codice ufficio destinatario</dfn>
												<dl><s:property value="fatturaFEL.codiceDestinatario" />&nbsp;</dl>
											</li>
											<li>
												<dfn>Causale</dfn>
												<dl><s:property value="estremiDellaFatturaCausale" />&nbsp;</dl>
											</li>
											<li>
												<dfn>Stato sul sistema FEL</dfn>
												<dl><s:property value="fatturaFEL.portaleFattureFEL.esitoStatoFattura"/>&nbsp;</dl>
											</li>
											<li>
												<dfn>Stato acquisizione</dfn>
												<dl><s:property value="fatturaFEL.statoAcquisizioneFEL.descrizione" />&nbsp;</dl>
											</li>
											<li>
												<dfn>Note</dfn>
												<dl><s:property value="fatturaFEL.note" />&nbsp;</dl>
											</li>
										</ul>
									</div>
									<div class="boxOrInRight">
										<p>Dati fornitore</p>
										<ul class="htmlelt">
											<li>
												<dfn>Denominazione</dfn>
												<dl><s:property value="datiFornitoreDenominazione" />&nbsp;</dl>
											</li>
											<li>
												<dfn>Codice fiscale</dfn>
												<dl><s:property value="datiFornitoreCodiceFiscale"/>&nbsp;</dl>
											</li>
											<li>
												<dfn>Partita iva</dfn>
												<dl><s:property value="datiFornitorePartitaIva"/>&nbsp;</dl>
											</li>
											<li>
												<dfn>Indirizzo</dfn>
												<dl><s:property value="datiFornitoreIndirizzo"/>&nbsp;</dl>
											</li>
											<li>
												<dfn><abbr title="Codice di Avviamento Postale">CAP</abbr>, Comune, Provincia</dfn>
												<dl><s:property value="datiFornitoreCAPComuneProvincia" />&nbsp;</dl>
											</li>
											<li>
												<dfn>Telefono, Fax</dfn>
												<dl><s:property value="datiFornitoreTelefonoFax"/>&nbsp;</dl>
											</li>
											<li>
												<dfn>Email</dfn>
												<dl><s:property value="fatturaFEL.prestatore.emailPrestatore"/>&nbsp;</dl>
											</li>
											<li>
												<dfn>Data</dfn>
												<dl><s:property value="fatturaFEL.prestatore.dataInserimento"/>&nbsp;</dl>
											</li>
										</ul>
									</div>
								</div>
								<div class="boxOrSpan2">
									<div class="boxOrInLeft">
										<p>Dati modalit&agrave; di pagamento</p>
										<ul class="htmlelt">
											<li>
												<dfn>Codice modalit&agrave; di pagamento</dfn>
												<dl><s:property value="datiModalitaDiPagamentoCodiceModalitaDiPagamento" />&nbsp;</dl>
											</li>
											<li>
												<dfn>Beneficiario</dfn>
												<dl><s:property value="dettaglioPagamentoFEL.beneficiario" /></dl>
											</li>
											<li>
												<dfn>Data riferimento, Giorni</dfn>
												<dl><s:property value="datiModalitaDiPagamentoDataRiferimentoGiorni"/>&nbsp;</dl>
											</li>
											<li>
												<dfn>Data scadenza</dfn>
												<dl><s:property value="dettaglioPagamentoFEL.dataScadenzaPagamento"/>&nbsp;</dl>
											</li>
										</ul>
										<s:if test="conMoltepliciDettaglioPagamentoFel">
											<h5>SEGUONO ULTERIORI MODALIT&Agrave; DI PAGAMENTO SUL SISTEMA FEL</h5>
										</s:if>
									</div>
									<div class="boxOrInRight">
										<p>Importi della fattura</p>
										<ul class="htmlelt">
											<li>
												<dfn>Importo totale</dfn>
												<dl><s:property value="fatturaFEL.importoTotaleDocumento" />&nbsp;</dl>
											</li>
											<li>
												<dfn>Arrotondamento</dfn>
												<dl><s:property value="fatturaFEL.arrotondamento" />&nbsp;</dl>
											</li>
											<li>
												<dfn>Importo netto</dfn>
												<dl><s:property value="fatturaFEL.importoTotaleNetto" />&nbsp;</dl>
											</li>
										</ul>
									</div>
								</div>
								<div class="boxOrSpan2">
									<div class="boxOrInLeft">
										<p>Dati Ritenute</p>
										<ul class="htmlelt">
											<li>
												<dfn>Tipo ritenuta</dfn>
												<dl><s:property value="fatturaFEL.tipoRitenuta" />&nbsp;</dl>
											</li>
											<li>
												<dfn>Aliquota</dfn>
												<dl><s:property value="fatturaFEL.aliquotaRitenuta" />&nbsp;</dl>
											</li>
											<li>
												<dfn>Importo ritenuta</dfn>
												<dl><s:property value="fatturaFEL.importoRitenuta" />&nbsp;</dl>
											</li>
										</ul>
									</div>
									<div class="boxOrInRight">
										<p>Dati relativi al bollo</p>
										<ul class="htmlelt">
											<li>
												<dfn>Codice</dfn>
												<dl><s:property value="fatturaFEL.bolloVirtuale" />&nbsp;</dl>
											</li>
											<li>
												<dfn>Importo</dfn>
												<dl><s:property value="fatturaFEL.importoBollo" />&nbsp;</dl>
											</li>
										</ul>
									</div>
								</div>
								<div class="clear"></div>
								<br/>
								<div id="fattureCollegateDiv" class="step-pane active">
									<div class="accordion">
										<div class="accordion-group">
											<div class="accordion-heading">
												<a data-target="#fattureCollegateCollapse" data-parent="#fattureCollegateDiv" data-toggle="collapse" class="accordion-toggle collapsed">
													Elenco delle fatture collegate<span class="icon">&nbsp;</span>
												</a>
											</div>
											<div class="accordion-body collapse" id="fattureCollegateCollapse">
												<div class="accordion-inner">
													<table class="table table-hover tab_left" id="fattureCollegateTable">
														<thead>
															<tr>
																<th>Numero fattura collegata</th>
																<th>Data fattura collegata</th>
															</tr>
														</thead>
														<tbody>
															<s:iterator value="fatturaFEL.fattureCollegate" var="fc">
																<tr>
																	<td><s:property value="#fc.numero" /></td>
																	<td><s:property value="#fc.data" /></td>
																</tr>
															</s:iterator>
														</tbody>
													</table>
												</div>
											</div>
										</div>
									</div>
								</div>
								<div id="datiIvaDiv" class="step-pane active">
									<div class="accordion">
										<div class="accordion-group">
											<div class="accordion-heading">
												<a data-target="#datiIvaCollapse" data-parent="#datiIvaDiv" data-toggle="collapse" class="accordion-toggle collapsed">
													Elenco dati iva<span class="icon">&nbsp;</span>
												</a>
											</div>
											<div class="accordion-body collapse" id="datiIvaCollapse">
												<div class="accordion-inner">
													<table class="table table-hover tab_left" id="datiIvaTable">
														<thead>
															<tr>
																<th>Aliquota</th>
																<th class="tab_Right">Imponibile / Esigibilit&agrave;</th>
																<th class="tab_Right">Imposta</th>
																<th class="tab_Right">Arrotondamenti</th>
																<th class="tab_Right">Spese accessorie</th>
															</tr>
														</thead>
														<tbody>
															<s:iterator value="fatturaFEL.riepiloghiBeni" var="rb">
																<tr>
																	<td>
																		<s:property value="#rb.aliquotaIva" />
																		<s:if test="%{#rb.esigibilitaIva != null && #rb.esigibilitaIva != ''}">
																			/ <s:property value="#rb.esigibilitaIva" />
																		</s:if>
																		
																	</td>
																	<td class="tab_Right"><s:property value="#rb.imponibileImportoNotNull" /></td>
																	<td class="tab_Right"><s:property value="#rb.impostaNotNull" /></td>
																	<td class="tab_Right"><s:property value="#rb.arrotondamentoNotNull" /></td>
																	<td class="tab_Right"><s:property value="#rb.speseAccessorieNotNull" /></td>
																</tr>
															</s:iterator>
														</tbody>
														<tfoot>
															<tr>
																<th>Totale</th>
																<th class="tab_Right"><s:property value="totaleImponibile" /></th>
																<th class="tab_Right"><s:property value="totaleImposta" /></th>
																<th class="tab_Right"><s:property value="totaleArrotondamenti" /></th>
																<th class="tab_Right"><s:property value="totaleSpeseAccessorie" /></th>
															</tr>
														</tfoot>
													</table>
												</div>
											</div>
										</div>
									</div>
								</div>
							</fieldset>
						</div>
					</div>
					<p class="margin-medium">
						<s:include value="/jsp/include/indietro.jsp" />
					</p>
				</s:form>
			</div>
		</div>
	</div>
	
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="${jspath}fatturaElettronica/consulta.js"></script>
	
</body>
</html>