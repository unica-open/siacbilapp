<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/siac-tags" prefix="siac"%>
<%@ taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>

<%-- Inclusione head e CSS --%>
<s:include value="/jsp/include/head.jsp" />
<link rel="stylesheet" href="/siacbilapp/css/dubbiaEsigibilita/inserisciConfigurazioneStampaDubbiaEsigibilita.css">

</head>
<body>
	<%-- Inclusione header --%>
	<s:include value="/jsp/include/header.jsp" />

	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12 ">
				<div class="contentPage">
					<s:hidden id="gest" value="GESTIONE" />
					<s:form  method="post" action="inserisciConfigurazioneStampaDubbiaEsigibilitaGestione_salvaAttributi">
						<s:include value="/jsp/include/messaggi.jsp" />
						<s:hidden name="gestioneUEB" id="HIDDEN_gestioneUEB" data-maintain="" />
						<h3>
							Configurazione FCDE Gestione
							<s:if test="accantonamentoFondiDubbiaEsigibilitaAttributiBilancio != null">
								&mdash; Versione: <s:property value="accantonamentoFondiDubbiaEsigibilitaAttributiBilancio.versione" />
								<s:if test="accantonamentoFondiDubbiaEsigibilitaAttributiBilancio.dataCreazione != null">
									del <s:property value="accantonamentoFondiDubbiaEsigibilitaAttributiBilancio.dataCreazione" />
								</s:if>
								<s:hidden id="n_versione" name="accantonamentoFondiDubbiaEsigibilitaAttributiBilancio.versione" />
								<s:if test="accantonamentoFondiDubbiaEsigibilitaAttributiBilancio.statoAccantonamentoFondiDubbiaEsigibilita != null">
									&mdash; Stato: <s:property value="accantonamentoFondiDubbiaEsigibilitaAttributiBilancio.statoAccantonamentoFondiDubbiaEsigibilita.codice" />
								</s:if>
							</s:if>
						</h3>
						<fieldset class="form-horizontal"> 
							<br />
							<s:hidden name="accantonamentoFondiDubbiaEsigibilitaAttributiBilancio.uid" />
							<s:hidden name="accantonamentoFondiDubbiaEsigibilitaAttributiBilancio.versione" />
							<s:hidden id="fcde-tipo" value="GESTIONE" />

							<div class="row span8 ml-0">
								<div class="span12">
									<h4 class="step-pane">Selezione parametri</h4>
									<div class="control-group">
										<label class="control-label">Elaborazione FCDE per il triennio</label>
										<div class="controls">
											<input class="span3" type="text" readonly value="<s:property value="%{sessionHandler.bilancio.anno}" /> - <s:property value="%{sessionHandler.bilancio.anno + 2}" />">
										</div>
									</div>
									<div class="control-group">
									
									
										<label class="control-label">Accantonamento graduale enti locali</label>
										<div class="controls">
											<s:textfield cssClass="parametri-non-editabili lbTextSmall span2 text-right soloNumeri decimale" id="accantonamentoGraduale" 
												name="accantonamentoFondiDubbiaEsigibilitaAttributiBilancio.accantonamentoGraduale" readonly="accantonamentoFondiDubbiaEsigibilitaAttributiBilancio.accantonamentoGraduale != null" />%

										</div>
									</div>
								</div>
							</div>
							<div class="row span4">
								<h4 class="step-pane">Altre operazioni</h4>
								<div class="control-group">
									<label class="control-label">Elenco versioni</label>
									<div class="controls">
										<s:select list="listaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioDistinctVersion" headerKey="" headerValue="Imposta la versione" cssClass="w-100"
											id="listaElaborazionePrecedente" listKey="uid" listValue="%{'Vers. #' + versione + ' del ' + formatDate(dataCreazione) + ' in stato ' + statoAccantonamentoFondiDubbiaEsigibilita.codice}"/>
									</div>
								</div>
								<div class="row span12">
									<button type="button" class="btn btn-secondary span4" id="pulsanteCaricaElaborazioneSelezionata">Carica capitoli da versione selezionata</button>
									<button type="button" class="btn btn-secondary span4" id="pulsanteSelezionaElaborazione">Seleziona versione</button>
									<button type="button" class="btn btn-secondary span4" id="creaNuovaVersione">Crea nuova versione</button>
								</div>
							</div>
						</fieldset>
					</s:form>
						
					<s:if test="accantonamentoFondiDubbiaEsigibilitaAttributiBilancio != null">
						<h4 class="step-pane">Selezione capitoli</h4>
						<div class="row margin-large">
							<div class="span6 row">
								<div class="span6">
									<button type="button" class="btn btn-secondary btn-block btn-previsione" id="pulsantePopolaDaAnnoPrecedentePrevisione">copia Capitoli da ultimo Bilancio di Previsione</button>
								</div>
								<div class="span6 ml-0">
									<button type="button" class="btn btn-secondary btn-block btn-gestione" id="pulsantePopolaDaAnnoPrecedenteGestione" >copia Capitoli da ultimo Bilancio di Assestamento</button>
								</div>
								<div class="span6 ml-0">
									<button type="button" class="btn btn-secondary btn-block btn-rendiconto" id="pulsantePopolaDaAnnoPrecedenteRendiconto">copia Capitoli da ultimo Bilancio in Rendiconto</button>
								</div>
								<div class="span6 ml-0">
									<button type="button" class="btn btn-secondary btn-block btn-anagrafica" id="pulsantePopolaDaAnnoPrecedenteAnagraficaCapitoli">copia Capitoli da anagrafica capitoli</button>
								</div>
							</div>
							<div class="span6 row v-middle">
								<div class="span12"></div>
								<div class="span6">
									<button type="button" id="pulsanteApriModaleCapitoloDubbiaEsigibilita" class="btn btn-secondary btn-block">ricerca capitoli</button>
								</div>
							</div>
						</div>
						<br/>
						<br/>
						<br/>
						<div>
							<h4>Elaborazione accantonamento FCDE</h4>
							<s:hidden id="statoAccantonamento" value="%{accantonamentoFondiDubbiaEsigibilitaAttributiBilancio.statoAccantonamentoFondiDubbiaEsigibilita.codice}" />
							<fieldset class="row">
								<div class="span10 row">
									<div class="span12">
										<div class="span1"></div>
										<div class="span3"><strong>Filtro Capitolo</strong></div>
										<div class="span3"><strong>Filtro Titolo</strong></div>
										<div class="span4"><strong>Filtro Tipologia</strong></div>
										<div class="span1"></div>
									</div>
									<div class="span12 ml-0">
										<div class="span1"></div>
										<div class="span3"><input data-filtro="Capitolo" type="text" class="span12" /></div>
										<div class="span3">
											<s:select list="elencoTitoloEntrataAccantonamenti" cssClass="span12" headerKey="" headerValue="Nessun filtro" listKey="codice" listValue="%{codice + ' - ' + descrizione}" data-filtro="Titolo" />
										</div>
										<div class="span4">
											<s:select list="elencoTipologiaTitoloAccantonamenti" cssClass="span12" headerKey="" headerValue="Nessun filtro" listKey="codice" listValue="%{codice + ' - ' + descrizione}" data-filtro="Tipologia" />
										</div>
										<div class="span1">
											<label>
												<input type="checkbox" id="filtroCapitoliAttivo" name="filtroCapitoliAttivo" class="hide" />
												<span class="tooltip-test" data-title="Attiva/disattiva filtro">
													<i class="icon-filter icon-2x"></i>
												</span>
											</label>
										</div>
									</div>
								</div>
								<div class="span2 row">
									<div class="span12">
										<button type="button" id="pulsante-modifica-importi" class="btn btn-block btn-secondary">
											<i class="icon-pencil marginLeft1"></i> Modifica accantonamenti
										</button>
										<button type="button" id="pulsante-salva-importi" class="btn hidden btn-block btn-primary">
											<i class="icon-save marginLeft1"></i> Salva accantonamenti
										</button>
									</div>
								</div>
							</fieldset>
							
							<div class="dataTable-scroll-x">
								<table id="riepilogoCapitoliGiaAssociati" class="table table-hover ">
									<thead>
										<tr role="row">
											<th scope="col" width="6%" role="columnheader" colspan="1" rowspan="2" class="sorting_disabled v-middle text-center">Capitolo</th>
											<%-- SIAC-8421 --%>
											<%-- <th scope="col" width="6%" role="columnheader" colspan="1" rowspan="2" class="sorting_disabled v-middle text-center">&nbsp;</th> --%>
											<%-- <th scope="col" width="7%" role="columnheader" colspan="1" rowspan="2" class="sorting_disabled v-middle text-center"><s:property value="%{sessionHandler.bilancio.anno}" /></th> --%>
											<th scope="col" width="7%" role="columnheader" colspan="1" rowspan="1" class="sorting_disabled v-middle text-center"><s:property value="%{sessionHandler.bilancio.anno}" /></th>
											<%-- <th scope="col" width="6%" role="columnheader" colspan="1" rowspan="2" class="sorting_disabled v-middle text-center">&nbsp;</th> --%>
											<%-- <th scope="col" width="7%" role="columnheader" colspan="1" rowspan="2" class="sorting_disabled v-middle text-center"><s:property value="%{sessionHandler.bilancio.anno}" /></th> --%>
											<th scope="col" width="7%" role="columnheader" colspan="1" rowspan="1" class="sorting_disabled v-middle text-center"><s:property value="%{sessionHandler.bilancio.anno}" /></th>
											<%-- <th scope="col" width="7%" role="columnheader" colspan="1" rowspan="2" class="sorting_disabled v-middle text-center">&nbsp;</th> --%>
											<%-- <th scope="col" width="7%" role="columnheader" colspan="1" rowspan="2" class="sorting_disabled v-middle text-center"><s:property value="%{sessionHandler.bilancio.anno}" /></th> --%>
											<th scope="col" width="7%" role="columnheader" colspan="1" rowspan="1" class="sorting_disabled v-middle text-center"><s:property value="%{sessionHandler.bilancio.anno}" /></th>
											<th scope="col" width="7%" role="columnheader" colspan="1" rowspan="1" class="sorting_disabled v-middle text-center"><s:property value="%{sessionHandler.bilancio.anno}" /></th>
											<th scope="col" width="7%" role="columnheader" colspan="1" rowspan="1" class="sorting_disabled v-middle text-center"><s:property value="%{sessionHandler.bilancio.anno}" /></th>
											<%-- SIAC-8421 --%>
											<th scope="col" width="10%" role="columnheader" colspan="1" rowspan="2" class="sorting_disabled v-middle text-center">Media di confronto</th>
											<th scope="col" width="6%" role="columnheader" colspan="1" rowspan="2" class="sorting_disabled v-middle text-center">Media utente</th>
											<th scope="col" width="18%" role="columnheader" colspan="2" rowspan="1" class="sorting_disabled text-center">Accantonamento al FCDE</th>
											<th scope="col" width="5%" role="columnheader" colspan="2" rowspan="2" class="sorting_disabled">&nbsp;</th>
										</tr>

										<tr role="row">
											<%-- SIAC-8421 --%>
											<th scope="col" width="7%" role="columnheader" rowspan="1" class="sorting_disabled v-middle text-center">Incassi c/ competenza</th>
											<th scope="col" width="7%" role="columnheader" rowspan="1" class="sorting_disabled v-middle text-center">Accertamenti</th>
											<th scope="col" width="7%" role="columnheader" rowspan="1" class="sorting_disabled v-middle text-center">% su Accertamenti</th>
											<th scope="col" width="7%" role="columnheader" rowspan="1" class="sorting_disabled v-middle text-center">Stanziamento</th>
											<th scope="col" width="7%" role="columnheader" rowspan="1" class="sorting_disabled v-middle text-center">% su Stanziamento</th>
											<%-- SIAC-8421 --%>
											<th scope="col" width="5%" role="columnheader" rowspan="1" class="sorting_disabled text-center">%</th>
											<th scope="col" width="13%" role="columnheader" rowspan="1" class="sorting_disabled">
												<div class="pull-left">Anno</div>
												<div class="pull-right text-center">EURO</div>
											</th>
										</tr>

									</thead>
									<s:if test="listaAccantonamentoFondiDubbiaEsigibilitaGestione.empty">
										<tbody>
											<tr>
												<td colspan="15">Nessun capitolo collegato all'accantonamento</td>
											</tr>
										</tbody>
									</s:if><s:else>
										<s:iterator value="listaAccantonamentoFondiDubbiaEsigibilitaGestione" status="status">
											<tbody class="capitolo bc-titolo-<s:property value="capitolo.titoloEntrata.codice"/>"
													data-index="<s:property value="#status.index" />"
													data-uid="<s:property value="uid"/>"
													data-filtro-capitolo="<s:property value="capitolo.numeroCapitolo"/>/<s:property value="capitolo.numeroArticolo"/>/<s:property value="capitolo.numeroUEB"/>"
													data-filtro-titolo="<s:property value="capitolo.titoloEntrata.codice"/>"
													data-filtro-tipologia="<s:property value="capitolo.tipologiaTitolo.codice"/>">
												<tr class="incassi" role="row">
													<td scope="col" id="info-td" width="6%" >
														<span data-popover data-container="body" data-placement="right" data-trigger="hover" data-title="Descrizione" data-content="<s:property value="capitolo.descrizione" />">
															<s:property value="capitolo.numeroCapitolo"/> / <s:property value="capitolo.numeroArticolo"/>
															<s:if test="gestioneUEB">
																/ <s:property value="capitolo.numeroUEB"/>
															</s:if>
														</span>
														<br/>
														<s:property value="capitolo.categoriaTipologiaTitolo.codice"/>
														<br/>
														<i class="icon-save icon-2x icon-only-modificato"></i>
														<br/>
														<div class="div-tooltip-medie">
															<s:if test="%{note != null && note != ''}">
																<a class="tooltip-test" data-html="true" data-placement="right" title="<s:property value="noteAsHtml" escapeHtml="false" />" href="#">
																	<i class="icon-warning-sign first icon-2x purple"></i>
																</a>
															</s:if>
														</div>
													</td>
													<%-- SIAC-8421 --%>
													<%-- <td scope="col" width="6%" class="text-right v-middle">Incassi c/ competenza</td> --%>
													<%-- <td scope="col" width="7%" class="v-middle"><input readonly data-peso="0.35" data-original="<siac:plainstringproperty value="numeratoreOriginale"  />" class="importo modificabile soloNumeri w-95 text-right <s:if test="numeratoreModificaUtente" >modificato</s:if> numeratore" type="text" value="<s:property value="numeratore"/>"></td> --%>
													<td scope="col" width="7%" class="v-middle"><input readonly data-peso="0.35" data-original="<siac:plainstringproperty value="numeratoreOriginale"  />" class="importo modificabile soloNumeri w-95 text-right <s:if test="numeratoreModificaUtente" >modificato</s:if> numeratore" type="text" value="<s:property value="numeratore"/>"></td>
													<%-- <td scope="col" width="6%" class="text-right v-middle">Accertamenti</td> --%>
													<%-- <td scope="col" width="7%" class="v-middle"><input readonly data-peso="0.35" data-original="<siac:plainstringproperty value="denominatoreOriginale"  />" class="importo modificabile soloNumeri w-95 text-right <s:if test="denominatoreModificaUtente" >modificato</s:if> denominatore" type="text" value="<s:property value="denominatore"/>"></td> --%>
													<td scope="col" width="7%" class="v-middle"><input readonly data-peso="0.35" data-original="<siac:plainstringproperty value="denominatoreOriginale"  />" class="importo modificabile soloNumeri w-95 text-right <s:if test="denominatoreModificaUtente" >modificato</s:if> denominatore" type="text" value="<s:property value="denominatore"/>"></td>
													<%-- <td scope="col" width="7%" class="text-right v-middle">% incasso</td> --%>
													<%-- <td scope="col" width="7%" class="text-center v-middle perc-incassi"><span data-indice-anno="" data-peso="0.35" class="perc"><siac:numeric value="percentualeAccantonamentoFondi"  decimalPlaces="2"/></span></td> --%>
													<td scope="col" width="7%" class="text-center v-middle perc-incassi"><span data-indice-anno="" data-peso="0.35" class="perc"><siac:numeric value="percentualeAccantonamentoFondi"  decimalPlaces="2"/></span></td>
													<td scope="col" width="7%" class="text-center v-middle"><span data-indice-anno="" data-peso="0.35" class="stanziamento-capitolo"><s:property value="sommaStanziamentoVariazioni" /></span> <s:if test="presentiVariazioni">*</s:if> </td>
													<td scope="col" width="7%" class="text-center v-middle"><span data-indice-anno="" data-peso="0.35" class="perc-stanziamento"><siac:numeric value="percentualeAccantonamentoFondi"  decimalPlaces="2"/></span></td>
													<%-- SIAC-8421 --%>
													<td scope="col" width="6%"  class="v-middle text-center confronto">
														<s:if test="mediaConfronto != null">
															<div>
																<span class="tipo-media-confronto"><s:property value="tipoMediaConfronto.descrizione" /></span>
															</div>
															&nbsp;&nbsp;&nbsp;&nbsp;
															<div>
																<span class="media-confronto"><siac:numeric value="mediaConfronto" decimalPlaces="2"/></span>&nbsp;%
															</div>
														</s:if>
														<%-- SIAC-8513 campo di appoggio per percentuale utilizzata --%>
														<span class="hide perc-effettiva"></span>
													</td>
													<td scope="col" width="10%" class="v-middle text-center">
														<%-- SIAC-8513 data-decimal-places="2" --%>
														<input readonly class="media-utente modificabile soloNumeri w-55 text-right <s:if test="mediaUtenteModificaUtente">modificato</s:if> decimale" data-original="<siac:plainstringproperty value="mediaUtenteOriginale" />" data-decimal-places="2" type="text" value="<siac:numeric value="mediaUtente" decimalPlaces="2"/>">
														<input checked name="mediaSel-<s:property value="#status.index" />" data-media="utente" class="applica-media applica-media-utente hide" type="radio" />
														<span class="prev-media soloNumeri w-55 text-right hide"><siac:numeric value="mediaUtente" decimalPlaces="2"/></span>
													</td> 
													<td scope="col" width="4%"  class="v-middle text-center"><span class="perc-accantonamento"><siac:numeric value="%{100 - percentualeAccantonamentoFondi}" decimalPlaces="2"/></span></td>
													<%-- SIAC-8421 --%>
													<%-- <td scope="col" width="12%" class="v-middle whitespace-nowrap" data-popover data-container="body" data-placement="left" data-trigger="hover" data-title="Stanziamento" data-content="<s:property value="stanziamentoCapitolo" />"> --%>
													<td scope="col" width="12%" class="v-middle whitespace-nowrap" >
														<span><s:property value="%{sessionHandler.bilancio.anno + 0}"/>:</span>
														<span class="w-70 d-inline-block text-right accantonamento" data-anno="0" data-importo="<s:property value="stanziamentoCapitolo" />"></span>
													</td>
													<td scope="col" width="5%"  class="v-middle whitespace-nowrap">
														<button type="button" class="ripristinaAccantonamento"><i class="icon-time  icon-2x"></i></button>
														<button type="button" class="eliminaAccantonamento"   ><i class="icon-trash icon-2x"></i></button>
														<span class="prevChoose hide"></span>
													</td>
												</tr>
												
											</tbody>
										</s:iterator>
									</s:else>
								</table>
								
								<div>
									<s:form action="inserisciConfigurazioneStampaDubbiaEsigibilitaGestione_estraiInFoglioDiCalcolo" method="post">
										<button type="submit" class="btn btn-primary" id="eseguiStampa">Estrai in foglio di calcolo</button>
									</s:form>
								</div>
							</div>
						</div>
					</s:if>
					<div class="Border_line"></div>
					<p class="margin-large">
						<s:include value="/jsp/include/indietro.jsp" />
						<a class="btn btn-secondary" id="pulsanteReset" href="
							<s:url action="inserisciConfigurazioneStampaDubbiaEsigibilitaGestione" >
								<s:param name="accantonamentoFondiDubbiaEsigibilitaAttributiBilancio.versione" value="%{accantonamentoFondiDubbiaEsigibilitaAttributiBilancio.versione}" />
							</s:url>">reset</a> 
						<a class="btn btn-secondary <s:if test="statoAccantonamentoFondiDubbiaEsigibilitaDefinitivo">hidden</s:if>"     id="pulsanteModificaStatoInDefinitivo" href="<s:url action="inserisciConfigurazioneStampaDubbiaEsigibilitaGestione_modificaStatoInDefinitivo" />">passa in definitivo</a>
						<s:if test="isConsentitoTornaInBozza()"><a class="btn btn-secondary <s:if test="not statoAccantonamentoFondiDubbiaEsigibilitaDefinitivo">hidden</s:if>" id="pulsanteModificaStatoInBozza"      href="<s:url action="inserisciConfigurazioneStampaDubbiaEsigibilitaGestione_modificaStatoInBozza" />"     >torna in bozza</a></s:if>
					</p>
					
					<s:form id="form-salvaCapitoli"                   cssClass="hide" novalidate="novalidate" method="post" action="inserisciConfigurazioneStampaDubbiaEsigibilitaGestione_salvaCapitoli" />
					<s:form id="formRipristinaAccantonamento_hidden"  cssClass="hide" novalidate="novalidate" method="post" action="inserisciConfigurazioneStampaDubbiaEsigibilitaGestione_ripristinaAccantonamento" />
					<s:form id="formEliminaAccantonamento_hidden"     cssClass="hide" novalidate="novalidate" method="post" action="inserisciConfigurazioneStampaDubbiaEsigibilitaGestione_eliminaAccantonamento" />
					<s:form id="formModificaStatoInDefinitivo_hidden" cssClass="hide" novalidate="novalidate" method="post" action="inserisciConfigurazioneStampaDubbiaEsigibilitaGestione_modificaStatoInDefinitivo" />
					<s:form id="formModificaStatoInBozza_hidden"      cssClass="hide" novalidate="novalidate" method="post" action="inserisciConfigurazioneStampaDubbiaEsigibilitaGestione_modificaStatoInBozza" />
					<s:form id="formSelezionaVersione_hidden"         cssClass="hide" novalidate="novalidate" method="post" action="inserisciConfigurazioneStampaDubbiaEsigibilitaGestione_selezionaVersione" />
					<s:form id="formNuovaVersione_hidden"             cssClass="hide" novalidate="novalidate" method="post" action="inserisciConfigurazioneStampaDubbiaEsigibilitaGestione_nuovaVersione" />
				</div>
			</div>
		</div>
	</div>
	<s:include value="/jsp/dubbiaEsigibilita/popolaDaCapitoloDubbiaEsigibilta_modale.jsp">
		<s:param name="baseActionName">inserisciConfigurazioneStampaDubbiaEsigibilitaGestione</s:param>
	</s:include>
	<s:include value="/jsp/dubbiaEsigibilita/selezionaCapitoloDubbiaEsigibilita_modale.jsp">
		<s:param name="baseActionName">inserisciConfigurazioneStampaDubbiaEsigibilitaGestione</s:param>
	</s:include>
	<s:include value="/jsp/dubbiaEsigibilita/aggiornaAccantonamentoDubbiaEsigibilita_modale.jsp" />
	<s:include value="/jsp/include/modaleConfermaEliminazione.jsp" />

	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />

	<script type="text/javascript" src="/siacbilapp/js/external/fileSaver/fileSaver-2.0.4.min.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/external/exceljs/exceljs-4.2.1.min.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/excel.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/capitolo/capitolo.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/capitolo/capitoloEntrata.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/dubbiaEsigibilita/inserisciConfigurazioneStampaDubbiaEsigibilita_utils.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/dubbiaEsigibilita/InserisciConfigurazioneStampaDubbiaEsigibilita_common.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/dubbiaEsigibilita/inserisciConfigurazioneStampaDubbiaEsigibilitaGestione.js"></script>

</body>
</html>