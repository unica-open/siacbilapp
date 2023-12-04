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
			    <s:include value="/jsp/include/messaggi.jsp" />
				<s:form id="formConsultazioneAllegatoAtto" cssClass="form-horizontal" novalidate="novalidate" action="#" method="post">
					<h4 class="step-pane">Consulta atto contabile / allegato <s:property value="allegatoAtto.attoAmministrativo.anno"/>/<s:property value="allegatoAtto.attoAmministrativo.numero"/>
						<span class="pull-right">Versione <s:property value="allegatoAtto.versioneInvioFirmaNotNull"/>&nbsp;
							<s:if test="%{allegatoAtto.dataVersioneInvioFirma != null}">del <s:property value="allegatoAtto.dataVersioneInvioFirma"/>&nbsp;</s:if>
							<s:if test="%{allegatoAtto.utenteVersioneInvioFirma != null}">inviata da <s:property value="allegatoAtto.utenteVersioneInvioFirma"/>&nbsp;</s:if>
						</span>
					</h4>
					<s:hidden id="HIDDEN_uidAllegatoAtto" name="allegatoAtto.uid" />
					<s:hidden id="HIDDEN_allegatoAttoConvalidato" value="%{convalidato}" />
					<fieldset class="form-horizontal">
						<div class="boxOrSpan2">
							<div class="boxOrInLeft">
								<p>Dati principali</p>
								<ul class="htmlelt">
									<li>
										<dfn>Tipo</dfn>
										<dl><s:property value="allegatoAtto.attoAmministrativo.tipoAtto.codice"/> - <s:property value="allegatoAtto.attoAmministrativo.tipoAtto.descrizione"/>&nbsp;</dl>
									</li>
									<li>
										<dfn>Struttura</dfn>
										<dl>
											<s:if test="%{allegatoAtto.attoAmministrativo.strutturaAmmContabile != null}">
												<s:property value="allegatoAtto.attoAmministrativo.strutturaAmmContabile.codice"/> - <s:property value="allegatoAtto.attoAmministrativo.strutturaAmmContabile.descrizione"/>
											</s:if>&nbsp;
										</dl>
									</li>
									<li>
										<dfn>Oggetto</dfn>
										<dl><s:property value="allegatoAtto.attoAmministrativo.oggetto"/>&nbsp;</dl>
									</li>
									<li>
										<dfn>Causale</dfn>
										<dl><s:property value="allegatoAtto.causale"/>&nbsp;</dl>
									</li>
									<li>
										<dfn>Data di scadenza</dfn>
										<dl><s:property value="allegatoAtto.dataScadenza"/>&nbsp;</dl>
									</li>
									<li>
										<dfn>Contiene ritenute</dfn>
										<dl><s:if test="allegatoAtto.flagRitenute">S&igrave;</s:if><s:else>No</s:else>&nbsp;</dl>
									</li>
									<li>
										<dfn>Stato allegato</dfn>
										<dl>
											<s:property value="allegatoAtto.statoOperativoAllegatoAtto.descrizione"/>&nbsp;-&nbsp;dal <s:property value="allegatoAtto.dataInizioValiditaStato"/>&nbsp;
											<s:if test="convalidato">
												<br/>
												Completato il <s:property value="allegatoAtto.dataCompletamento"/>
											</s:if>
										</dl>
									</li>
								</ul>
							</div>
							<div class="boxOrInRight ">
								<p>Altri dati</p>
								<ul class="htmlelt">
									<li>
										<dfn>Responsabile contabile</dfn>
										<dl><s:property value="allegatoAtto.responsabileContabile"/>&nbsp;</dl>
									</li>
									<li>
										<dfn>Annotazioni</dfn>
										<dl><s:property value="allegatoAtto.annotazioni"/>&nbsp;</dl>
									</li>
									<li>
										<dfn>Responsabile amministrativo</dfn>
										<dl><s:property value="allegatoAtto.responsabileAmministrativo"/>&nbsp;</dl>
									</li>
									<li>
										<dfn>Pratica numero</dfn>
										<dl><s:property value="allegatoAtto.pratica"/>&nbsp;</dl>
									</li>
									<li>
										<dfn>Altri allegati</dfn>
										<dl><s:property value="allegatoAtto.altriAllegati"/>&nbsp;</dl>
									</li>
									<li>
										<dfn>Note in Allegato</dfn>
										<dl><s:property value="allegatoAtto.note"/>&nbsp;</dl>
									</li>
									<li>
										<dfn>Contiene dati sensibili</dfn>
										<dl>
											<s:if test="%{allegatoAtto.datiSensibili}">
												S&igrave;
											</s:if><s:else>
												No
											</s:else>&nbsp;
										</dl>
									</li>
								</ul>
							</div>
						</div>

						<h4>Elenchi collegati</h4>
						<div id="accordionElenco" class="accordion">
							<div class="accordion-group">
								<div class="accordion-heading">
									<a href="#divElenchi" data-parent="#accordionElenco" data-toggle="collapse" class="accordion-toggle collapsed">
										Elenchi collegati:
										<span class="accNumInfo" id="numeroElenchiCollegatiAllegatoAtto">
											<s:property value="%{listaElencoDocumentiAllegato.size()}"/>
										</span>
										- Totale Entrate:
										<span class="accNumInfo" id="totaleEntrataAllegatoAtto">
											<s:property value="totaleEntrataListaElencoDocumentiAllegato" />
										</span>
										- Totale Spese:
										<span class="accNumInfo" id="totaleSpesaAllegatoAtto">
											<s:property value="totaleSpesaListaElencoDocumentiAllegato" />
										</span>
										- Totale Netto:
										<span class="accNumInfo" id="totaleNettoAllegatoAtto">
											<s:property value="totaleNettoListaElencoDocumentiAllegato" />
										</span>
										- Totale Incassato:
										<span class="accNumInfo" id="totaleIncassato">
											<s:property value="totaleIncassatoListaElencoDocumentiAllegato" />
										</span>
										- Totale Pagato:
										<span class="accNumInfo" id="totalePagato">
											<s:property value="totalePagatoListaElencoDocumentiAllegato" />
										</span>
										<span class="icon">&nbsp;</span>
									</a>
								</div>
								<div class="accordion-body collapse" id="divElenchi">
									<div class="accordion-inner">
										<fieldset class="form-horizontal">
											<table class="table table-hover tab_left" id="tabellaElencoDocumentiAllegato">
												<thead>
													<tr>
														<th></th>
														<th>Anno</th>
														<th>Numero</th>
														<th>Stato</th>
														<th>Anno/Numero fonte</th>
														<th>Data trasmissione</th>
														<th>Documenti/Quote</th>
														<th class="tab_Right">Importo Entrate</th>
														<th class="tab_Right">Importo Spese</th>
														<th class="tab_Right">Differenza</th>
														<th class="tab_Right">Importo Incassato</th>
														<th class="tab_Right">Importo Pagato</th>
													</tr>
												</thead>
												<tbody>
													<s:iterator value="listaElencoDocumentiAllegato" var="eda">
														<tr>
															<td>
																<input type='radio' class='checkboxElencoDocumentiAllegato' name='uidElenco' value='<s:property value="%{#eda.uid}"/>'>
															</td>
															<td><s:property value="%{#eda.anno}" /></td>
															<td><s:property value="%{#eda.numero}" /></td>
															<td>
																<a data-original-title="Stato" href="#" data-trigger="hover" data-toggle="popover" data-content="<s:property value='%{#eda.statoOperativoElencoDocumenti.descrizione}'/>">
																	<s:property value='%{#eda.statoOperativoElencoDocumenti.codice}'/>
																</a>
															</td>
															<td>
																<a data-original-title="Fonte dati" href="#" data-trigger="hover" data-toggle="popover" data-content="<s:property value='%{#eda.sysEsterno}'/>">
																	<s:property value='%{#eda.annoSysEsterno}'/>
																</a>
															</td>
															<td><s:property value="%{#eda.dataTrasmissione}" /></td>
															<td><s:property value="%{#eda.numeroQuoteInElenco}" /></td>
															<td class="tab_Right"><s:property value="%{#eda.totaleQuoteEntrate}" /></td>
															<td class="tab_Right"><s:property value="%{#eda.totaleQuoteSpese}" /></td>
															<td class="tab_Right"><s:property value="%{#eda.totaleQuoteNetti}" /></td>
															<td class="tab_Right"><s:property value="%{#eda.totaleIncassato}" /></td>
															<td class="tab_Right"><s:property value="%{#eda.totalePagato}" /></td>
														</tr>
													</s:iterator>
												</tbody>
											</table>
										</fieldset>
									</div>
								</div>
							</div>
						</div>
						
						<div id="dettaglioElementiCollegati" class="hide">
							<h4>Dettaglio elementi collegati</h4>
							<fieldset class="form-horizontal">
								<s:if test="convalidato">
									<table class="table table-hover tab_left" id="tabellaDettaglioElementiCollegati">
										<thead>
											<tr>
												<th>Elenco</th>
												<th>Documento-Quota</th>
												<th><abbr title="Modalità pagamento soggetto">Mod.Pag.</abbr></th>
												<th>Soggetto</th>
												<th>Movimento</th>
												<th>Capitolo</th>
												<th>Provv. movimento</th>
												<th>IVA</th>
												<th>Annotazioni</th>
												<th><abbr title="Liquidazione">Liq.</abbr></th>
												<th><abbr title="Ordinativo">Ord.</abbr></th>
												<th><abbr title="Stato ordinativo">St.Ord.</abbr></th>
												<th class="tab_Right">Importo in atto</th>
											</tr>
										</thead>
										<tbody>
										</tbody>
									</table>
								</s:if><s:else>
									<table class="table table-hover tab_left" id="tabellaDettaglioElementiCollegati">
										<thead>
											<tr>
												<th>Elenco</th>
												<th>Documento-Quota</th>
												<th><abbr title="Modalità pagamento soggetto">Mod.Pag.</abbr></th>
												<th>Soggetto</th>
												<th>Movimento</th>
												<th>Capitolo</th>
												<th>Provv. movimento</th>
												<th>IVA</th>
												<th>Annotazioni</th>
												<th class="tab_Right">Importo in atto</th>
												<th><abbr title="Ordinativo">Ord.</abbr></th>
											</tr>
										</thead>
										<tbody>
										</tbody>
									</table>
								</s:else>
								<s:include value="/jsp/allegatoAtto/include/ricercaQuoteNellElenco_consultazione.jsp"/>
							</fieldset>
						</div>
						
					</fieldset>

					<div class="Border_line"></div>
					<p>
						<s:include value="/jsp/include/indietro.jsp" />
					</p>
				</s:form>
			</div>
		</div>
	</div>

	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	
	<!-- SIAC-6243: FIN - consulta allegato atto -->
	<script type="text/javascript" src="/siacbilapp/js/local/soggetto/ricerca.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/allegatoAtto/gestioneElenco.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/allegatoAtto/consulta.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/soggetto/ricerca.new2.js"></script>


</body>
</html>