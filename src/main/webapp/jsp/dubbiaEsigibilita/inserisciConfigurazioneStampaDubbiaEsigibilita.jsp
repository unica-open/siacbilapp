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
					<s:form  method="post" action="inserisciConfigurazioneStampaDubbiaEsigibilita_salvaAttributi">
						<s:include value="/jsp/include/messaggi.jsp" />
						<s:hidden name="gestioneUEB" id="HIDDEN_gestioneUEB" data-maintain="" />
						<h3>
							Configurazione FCDE Previsione
							<s:if test="accantonamentoFondiDubbiaEsigibilitaAttributiBilancio != null">
								&mdash; Versione: <s:property value="accantonamentoFondiDubbiaEsigibilitaAttributiBilancio.versione" />
								<s:if test="accantonamentoFondiDubbiaEsigibilitaAttributiBilancio.dataCreazione != null">
									del <s:property value="accantonamentoFondiDubbiaEsigibilitaAttributiBilancio.dataCreazione" />
								</s:if>
								<s:if test="accantonamentoFondiDubbiaEsigibilitaAttributiBilancio.statoAccantonamentoFondiDubbiaEsigibilita != null">
									&mdash; Stato: <s:property value="%{accantonamentoFondiDubbiaEsigibilitaAttributiBilancio.statoAccantonamentoFondiDubbiaEsigibilita.codice}" />
								</s:if>
							</s:if>
						</h3>
						<fieldset class="form-horizontal"> 
							<br />
							<s:hidden name="accantonamentoFondiDubbiaEsigibilitaAttributiBilancio.uid" />
							<s:hidden name="accantonamentoFondiDubbiaEsigibilitaAttributiBilancio.versione" />
							<s:hidden id="fcde-tipo" value="PREVISIONE" />

							<div class="row span8 ml-0">
								<div class="span12">
									<h4 class="step-pane">Selezione parametri</h4>
									<div class="control-group">
										<label class="control-label">Elaborazione FCDE per il triennio</label>
										<div class="controls">
											<input class="span3" type="text" readonly value="<s:property value="%{sessionHandler.bilancio.anno}" /> - <s:property value="%{sessionHandler.bilancio.anno + 2}" />">
											<span class="al">
												<label for="perA" class="radio inline">Quinquennio di riferimento</label>
											</span>
											<span class="parametri-editabili <s:if test="accantonamentoFondiDubbiaEsigibilitaAttributiBilancio != null">hidden</s:if>">
												<s:select id="" list="#{'':'', (sessionHandler.bilancio.anno - 1) : (sessionHandler.bilancio.anno - 1) + '-' + (sessionHandler.bilancio.anno-5),
													(sessionHandler.bilancio.anno - 2) : (sessionHandler.bilancio.anno - 2) + '-' + (sessionHandler.bilancio.anno-6) }" name="accantonamentoFondiDubbiaEsigibilitaAttributiBilancio.quinquennioRiferimento" cssClass="span3" />
											</span>
											<input class="parametri-non-editabili <s:if test="accantonamentoFondiDubbiaEsigibilitaAttributiBilancio == null">hidden</s:if>"
												type="text" <s:if test="accantonamentoFondiDubbiaEsigibilitaAttributiBilancio != null">readonly</s:if>
												value="<s:property value="%{accantonamentoFondiDubbiaEsigibilitaAttributiBilancio.quinquennioRiferimento}" /> - <s:property value="%{accantonamentoFondiDubbiaEsigibilitaAttributiBilancio.quinquennioRiferimento-4}" />">
										</div>
									</div>
									<div class="control-group">
										<label class="control-label">Accantonamento graduale enti locali</label>
										<div class="controls">
											<s:textfield cssClass="parametri-editabili lbTextSmall span2 text-right decimale soloNumeri" id="accantonamentoGraduale" name="accantonamentoFondiDubbiaEsigibilitaAttributiBilancio.accantonamentoGraduale" readonly="accantonamentoFondiDubbiaEsigibilitaAttributiBilancio.accantonamentoGraduale != null" />%
											<span class="al">
												<label for="perA" class="radio inline">Riscossione virtuosa</label>
											</span>
											<label class="radio inline">
												<input class="parametri-editabili" type="radio" id="riscossioneVirtuosaTrue"
													<s:if test="accantonamentoFondiDubbiaEsigibilitaAttributiBilancio.riscossioneVirtuosa != null">disabled</s:if>
													name="accantonamentoFondiDubbiaEsigibilitaAttributiBilancio.riscossioneVirtuosa" 
													value="true" <s:if test='%{accantonamentoFondiDubbiaEsigibilitaAttributiBilancio.riscossioneVirtuosa}'>checked</s:if> />S&igrave;
											</label>
											<label class="radio inline">
												<input class="parametri-editabili" type="radio" id="riscossioneVirtuosaFalse"
													<s:if test="accantonamentoFondiDubbiaEsigibilitaAttributiBilancio.riscossioneVirtuosa != null">disabled</s:if>
													name="accantonamentoFondiDubbiaEsigibilitaAttributiBilancio.riscossioneVirtuosa" value="false" 
													<s:if test='%{! accantonamentoFondiDubbiaEsigibilitaAttributiBilancio.riscossioneVirtuosa}'>checked</s:if> />No
											</label>
											<s:if test="accantonamentoFondiDubbiaEsigibilitaAttributiBilancio.riscossioneVirtuosa != null">
												<s:hidden name="accantonamentoFondiDubbiaEsigibilitaAttributiBilancio.riscossioneVirtuosa" cssClass="parametri-non-editabili"/>
											</s:if>
											
											<span class="al">
												<s:if test="accantonamentoFondiDubbiaEsigibilitaAttributiBilancio != null">
													<button id="pulsante-modifica-parametri" type="button" class="btn btn-secondary">Modifica parametri</button>
												</s:if>
												<button type="submit" id="pulsante-seleziona-capitoli" class="btn btn-primary 
													<s:if test="accantonamentoFondiDubbiaEsigibilitaAttributiBilancio != null">hidden</s:if>">
													Conferma modifica
												</button>
											</span>
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
									<button type="button" class="btn btn-secondary span4" id="pulsanteCaricaElaborazioneSelezionata">Copia capitoli da versione selezionata</button>
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
							<h4>Elaborazione accantonamento FCDE </h4>
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
											<td scope="col" colspan="7" rowspan="2" width="47%"></td>
											<td scope="col" colspan="4"             width="24%" class="text-center">Applica media a tutti i capitoli</td>
											<td scope="col" colspan="4" rowspan="2" width="29%"></td>
										</tr>
										
										<tr role="row">
											<td scope="col" width="6%" class="text-right checkbox"><input name="mediaSelTutti" class="applica-tutti-media" data-media="semplice-totali"    id="applica-tutti-media-semplice-totali"    disabled type="radio"></td>
											<td scope="col" width="6%" class="text-right checkbox"><input name="mediaSelTutti" class="applica-tutti-media" data-media="semplice-rapporti"  id="applica-tutti-media-semplice-rapporti"  disabled type="radio"></td>
											<td scope="col" width="6%" class="text-right checkbox"><input name="mediaSelTutti" class="applica-tutti-media" data-media="ponderata-totali"   id="applica-tutti-media-ponderata-totali"   disabled type="radio"></td>
											<td scope="col" width="6%" class="text-right checkbox"><input name="mediaSelTutti" class="applica-tutti-media" data-media="ponderata-rapporti" id="applica-tutti-media-ponderata-rapporti" disabled type="radio"></td>
										</tr>
										
										<tr role="row">
											<th scope="col" width="15%" colspan="2" rowspan="2" role="columnheader" class="sorting_disabled v-middle">Capitolo</th>
											<th scope="col" width="7%"  colspan="1" rowspan="2" role="columnheader" class="sorting_disabled a-q4 v-middle text-center"><s:property value="%{accantonamentoFondiDubbiaEsigibilitaAttributiBilancio.quinquennioRiferimento-4}" /></th>
											<th scope="col" width="7%"  colspan="1" rowspan="2" role="columnheader" class="sorting_disabled a-q3 v-middle text-center"><s:property value="%{accantonamentoFondiDubbiaEsigibilitaAttributiBilancio.quinquennioRiferimento-3}" /></th>
											<th scope="col" width="7%"  colspan="1" rowspan="2" role="columnheader" class="sorting_disabled a-q2 v-middle text-center"><s:property value="%{accantonamentoFondiDubbiaEsigibilitaAttributiBilancio.quinquennioRiferimento-2}" /></th>
											<th scope="col" width="7%"  colspan="1" rowspan="2" role="columnheader" class="sorting_disabled a-q1 v-middle text-center"><s:property value="%{accantonamentoFondiDubbiaEsigibilitaAttributiBilancio.quinquennioRiferimento-1}" /></th>
											<th scope="col" width="7%"  colspan="1" rowspan="2" role="columnheader" class="sorting_disabled a-q v-middle text-center"><s:property value="%{accantonamentoFondiDubbiaEsigibilitaAttributiBilancio.quinquennioRiferimento}" /></th>
											<th scope="col" width="14%" colspan="2" rowspan="1" role="columnheader" class="sorting_disabled text-center">Media semplice</th>
											<th scope="col" width="14%" colspan="2" rowspan="1" role="columnheader" class="sorting_disabled text-center">Media ponderata</th>
											<th scope="col" width="7%"  colspan="1" rowspan="2" role="columnheader" class="sorting_disabled v-middle text-center">Media utente</th>
											<th scope="col" width="18%" colspan="2" rowspan="1" role="columnheader" class="sorting_disabled text-center">Accantonamento al FCDE</th>
											<th scope="col" width="10%"  colspan="2" rowspan="2" role="columnheader" class="sorting_disabled text-center"></th>
										</tr>
										
										<tr role="row">
											<th scope="col" width="7%"  role="columnheader" class="sorting_disabled text-center">totali</th>
											<th scope="col" width="7%"  role="columnheader" class="sorting_disabled text-center">rapporti</th>
											<th scope="col" width="7%"  role="columnheader" class="sorting_disabled text-center">totali</th>
											<th scope="col" width="7%"  role="columnheader" class="sorting_disabled text-center">rapporti</th>
											<th scope="col" width="5%"  role="columnheader" class="sorting_disabled text-center">%</th>
											<th scope="col" width="13%" role="columnheader" class="sorting_disabled">
												<div class="pull-left">Anno</div>
												<div class="pull-right text-center">EURO</div>
											</th>
											<%-- <th scope="col" width="5%" role="columnheader" class="sorting_disabled">EURO</th> --%>
											<%-- <th scope="col" width="5%" role="columnheader" class="sorting_disabled text-center"></th> --%>
										</tr>
									</thead>
									<s:if test="listaAccantonamentoFondiDubbiaEsigibilita.empty">
										<tbody>
											<tr>
												<td colspan="15">Nessun capitolo collegato all'accantonamento</td>
											</tr>
										</tbody>
									</s:if><s:else>
										<s:iterator value="listaAccantonamentoFondiDubbiaEsigibilita" status="status">
											<tbody class="capitolo bc-titolo-<s:property value="capitolo.titoloEntrata.codice"/>"
													data-index="<s:property value="#status.index" />"
													data-uid="<s:property value="uid"/>"
													data-filtro-capitolo="<s:property value="capitolo.numeroCapitolo"/>/<s:property value="capitolo.numeroArticolo"/>/<s:property value="capitolo.numeroUEB"/>"
													data-filtro-titolo="<s:property value="capitolo.titoloEntrata.codice"/>"
													data-filtro-tipologia="<s:property value="capitolo.tipologiaTitolo.codice"/>">
												<tr class="incassi" role="row">
													<td scope="col" width="6%" rowspan="3">
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
													<td scope="col" width="6%" class="text-right v-middle">Incassi</td>
													<td scope="col" width="7%" class="v-middle"><input readonly data-peso="0.10" data-original="<siac:plainstringproperty value="numeratore4Originale" />" class="importo soloNumeri modificabile soloNumeri w-95 text-right <s:if test="numeratore4ModificaUtente">modificato</s:if> numeratore4" type="text" value="<s:property value="numeratore4"/>"></td>
													<td scope="col" width="7%" class="v-middle"><input readonly data-peso="0.10" data-original="<siac:plainstringproperty value="numeratore3Originale" />" class="importo soloNumeri modificabile soloNumeri w-95 text-right <s:if test="numeratore3ModificaUtente">modificato</s:if> numeratore3" type="text" value="<s:property value="numeratore3"/>"></td>
													<td scope="col" width="7%" class="v-middle"><input readonly data-peso="0.10" data-original="<siac:plainstringproperty value="numeratore2Originale" />" class="importo soloNumeri modificabile soloNumeri w-95 text-right <s:if test="numeratore2ModificaUtente">modificato</s:if> numeratore2 riscossioneVirtuosa" type="text" value="<s:property value="numeratore2"/>"></td>
													<td scope="col" width="7%" class="v-middle"><input readonly data-peso="0.35" data-original="<siac:plainstringproperty value="numeratore1Originale" />" class="importo soloNumeri modificabile soloNumeri w-95 text-right <s:if test="numeratore1ModificaUtente">modificato</s:if> numeratore1 riscossioneVirtuosa" type="text" value="<s:property value="numeratore1"/>"></td>
													<td scope="col" width="7%" class="v-middle"><input readonly data-peso="0.35" data-original="<siac:plainstringproperty value="numeratoreOriginale"  />" class="importo soloNumeri modificabile soloNumeri w-95 text-right <s:if test="numeratoreModificaUtente" >modificato</s:if> numeratore  riscossioneVirtuosa" type="text" value="<s:property value="numeratore"/>" ></td>
													<td scope="col" width="33%" colspan="6" ></td>
													<td scope="col" width="12%" class="v-middle" data-popover data-container="body" data-placement="left" data-trigger="hover" data-title="Stanziamento" data-content="<s:property value="stanziamentoCapitolo" />">
														<span><s:property value="%{sessionHandler.bilancio.anno}"/>:</span>
														<span class="w-70 d-inline-block text-right accantonamento accFcde" data-anno="0" data-importo="<s:property value="stanziamentoCapitolo" />"></span>
													</td>
													<td scope="col" width="5%"></td>
												</tr>
												
												<tr class="accertamenti" role="row">
													<td scope="col" width="6%"  class="text-right v-middle">Accertamenti</td>
													<td scope="col" width="7%"  class="v-middle"><input readonly data-peso="0.10" data-original="<siac:plainstringproperty value="denominatore4Originale" />" class="importo soloNumeri modificabile soloNumeri w-95 text-right <s:if test="denominatore4ModificaUtente">modificato</s:if> denominatore4" type="text" value="<s:property value="denominatore4"/>"></td>
													<td scope="col" width="7%"  class="v-middle"><input readonly data-peso="0.10" data-original="<siac:plainstringproperty value="denominatore3Originale" />" class="importo soloNumeri modificabile soloNumeri w-95 text-right <s:if test="denominatore3ModificaUtente">modificato</s:if> denominatore3" type="text" value="<s:property value="denominatore3"/>"></td>
													<td scope="col" width="7%"  class="v-middle"><input readonly data-peso="0.10" data-original="<siac:plainstringproperty value="denominatore2Originale" />" class="importo soloNumeri modificabile soloNumeri w-95 text-right <s:if test="denominatore2ModificaUtente">modificato</s:if> denominatore2 riscossioneVirtuosa" type="text" value="<s:property value="denominatore2"/>"></td>
													<td scope="col" width="7%"  class="v-middle"><input readonly data-peso="0.35" data-original="<siac:plainstringproperty value="denominatore1Originale" />" class="importo soloNumeri modificabile soloNumeri w-95 text-right <s:if test="denominatore1ModificaUtente">modificato</s:if> denominatore1 riscossioneVirtuosa" type="text" value="<s:property value="denominatore1"/>"></td>
													<td scope="col" width="7%"  class="v-middle"><input readonly data-peso="0.35" data-original="<siac:plainstringproperty value="denominatoreOriginale"  />" class="importo soloNumeri modificabile soloNumeri w-95 text-right <s:if test="denominatoreModificaUtente" >modificato</s:if> denominatore  riscossioneVirtuosa" type="text" value="<s:property value="denominatore"/>" ></td>
													<td scope="col" width="7%"  class="v-middle text-right whitespace-nowrap">
														<span class="media-semplice-totali"><siac:numeric value="mediaSempliceTotali" decimalPlaces="2"/></span>
														<input disabled name="mediaSel-<s:property value="#status.index" />" data-media="semplice-totali"    class="applica-media applica-media-semplice-totali"    type="radio" <s:if test="%{@it.csi.siac.siacbilser.model.fcde.TipoMediaAccantonamentoFondiDubbiaEsigibilita@SEMPLICE_TOTALI.equals(tipoMediaPrescelta) }">checked</s:if>>
													</td>
													<td scope="col" width="7%"  class="v-middle text-right whitespace-nowrap">
														<span class="media-semplice-rapporti"><siac:numeric value="mediaSempliceRapporti" decimalPlaces="2"/></span>
														<input disabled name="mediaSel-<s:property value="#status.index" />" data-media="semplice-rapporti"  class="applica-media applica-media-semplice-rapporti"  type="radio" <s:if test="%{@it.csi.siac.siacbilser.model.fcde.TipoMediaAccantonamentoFondiDubbiaEsigibilita@SEMPLICE_RAPPORTI.equals(tipoMediaPrescelta) }">checked</s:if>>
													</td>
													<td scope="col" width="7%"  class="v-middle text-right whitespace-nowrap">
														<span class="media-ponderata-totali"><siac:numeric value="mediaPonderataTotali" decimalPlaces="2"/></span>
														<input disabled name="mediaSel-<s:property value="#status.index" />" data-media="ponderata-totali"   class="applica-media applica-media-ponderata-totali"   type="radio" <s:if test="%{@it.csi.siac.siacbilser.model.fcde.TipoMediaAccantonamentoFondiDubbiaEsigibilita@PONDERATA_TOTALI.equals(tipoMediaPrescelta) }">checked</s:if>>
													</td>
													<td scope="col" width="7%"  class="v-middle text-right whitespace-nowrap">
														<span class="media-ponderata-rapporti"><siac:numeric value="mediaPonderataRapporti" decimalPlaces="2"/></span>
														<input disabled name="mediaSel-<s:property value="#status.index" />" data-media="ponderata-rapporti" class="applica-media applica-media-ponderata-rapporti" type="radio" <s:if test="%{@it.csi.siac.siacbilser.model.fcde.TipoMediaAccantonamentoFondiDubbiaEsigibilita@PONDERATA_RAPPORTI.equals(tipoMediaPrescelta) }">checked</s:if>>
													</td>
													<td scope="col" width="7%"  class="v-middle text-right whitespace-nowrap">
														<%-- SIAC-8513 data-decimal-places="2" --%>
														<input readonly class="media-utente modificabile soloNumeri w-55 text-right <s:if test="mediaUtenteModificaUtente">modificato</s:if> decimale" data-original="<siac:plainstringproperty value="mediaUtenteOriginale" />" data-decimal-places="2" type="text" value="<siac:numeric value="mediaUtente" decimalPlaces="2"/>">
														<input disabled name="mediaSel-<s:property value="#status.index" />" data-media="utente"             class="applica-media applica-media-utente"             type="radio" <s:if test="%{@it.csi.siac.siacbilser.model.fcde.TipoMediaAccantonamentoFondiDubbiaEsigibilita@UTENTE.equals(tipoMediaPrescelta) }">checked</s:if>>
													</td>
													<td scope="col" width="3%"  class="v-middle text-right"><span class="perc-accantonamento"><siac:numeric value="percentualeAccantonamentoFCDE" decimalPlaces="2"/></span></td>
													<td scope="col" width="12%" class="v-middle" data-popover data-container="body" data-placement="left" data-trigger="hover" data-title="Stanziamento" data-content="<s:property value="stanziamentoCapitolo1" />">
														<span><s:property value="%{sessionHandler.bilancio.anno + 1}"/>:</span>
														<span class="w-70 d-inline-block text-right accantonamento accFcde1" data-anno="1" data-importo="<s:property value="stanziamentoCapitolo1" />"></span>
													</td>
													<td scope="col" width="5%"  class="v-middle whitespace-nowrap">
														<button type="button" class="ripristinaAccantonamento"><i class="icon-time  icon-2x"></i></button>
														<button type="button" class="eliminaAccantonamento"   ><i class="icon-trash icon-2x"></i></button>
														<span class="prevChoose hide"></span>
													</td>
												</tr>
												
												<tr class="perc-incassi" role="row">
													<td scope="col" width="6%" class="text-right v-middle">% incasso</td>
													<td scope="col" width="7%" class="text-right v-middle"><span data-indice-anno="4" data-peso="0.10" class="perc"><siac:numeric value="percentualeAccantonamentoFondi4" decimalPlaces="2"/></span></td>
													<td scope="col" width="7%" class="text-right v-middle"><span data-indice-anno="3" data-peso="0.10" class="perc"><siac:numeric value="percentualeAccantonamentoFondi3" decimalPlaces="2"/></span></td>
													<td scope="col" width="7%" class="text-right v-middle"><span data-indice-anno="2" data-peso="0.10" class="perc riscossioneVirtuosa"><siac:numeric value="percentualeAccantonamentoFondi2" decimalPlaces="2"/></span></td>
													<td scope="col" width="7%" class="text-right v-middle"><span data-indice-anno="1" data-peso="0.35" class="perc riscossioneVirtuosa"><siac:numeric value="percentualeAccantonamentoFondi1" decimalPlaces="2"/></span></td>
													<td scope="col" width="7%" class="text-right v-middle"><span data-indice-anno=""  data-peso="0.35" class="perc riscossioneVirtuosa"><siac:numeric value="percentualeAccantonamentoFondi"  decimalPlaces="2"/></span></td>
													<td scope="col" width="33%" colspan="6"></td>
													<td scope="col" width="12%" data-popover data-container="body" data-placement="left" data-trigger="hover" data-title="Stanziamento" data-content="<s:property value="stanziamentoCapitolo2" />">
														<span><s:property value="%{sessionHandler.bilancio.anno + 2}"/>:</span>
														<span class="w-70 d-inline-block text-right accantonamento accFcde2" data-anno="2" data-importo="<s:property value="stanziamentoCapitolo2" />"></span>
													</td>
													<td scope="col" width="5%"></td>
												</tr>
											</tbody>
										</s:iterator>
									</s:else>
								</table>
								
								<div>
									<s:form action="inserisciConfigurazioneStampaDubbiaEsigibilita_estraiInFoglioDiCalcolo" method="post">
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
							<s:url action="inserisciConfigurazioneStampaDubbiaEsigibilita" >
								<s:param name="accantonamentoFondiDubbiaEsigibilitaAttributiBilancio.versione" value="%{accantonamentoFondiDubbiaEsigibilitaAttributiBilancio.versione}" />
							</s:url>">reset</a> 
						<a class="btn btn-secondary <s:if test="statoAccantonamentoFondiDubbiaEsigibilitaDefinitivo">hidden</s:if>"     id="pulsanteModificaStatoInDefinitivo" href="<s:url action="inserisciConfigurazioneStampaDubbiaEsigibilita_modificaStatoInDefinitivo" />">passa in definitivo per Allegato C</a>
						<s:if test="isConsentitoTornaInBozza()"><a class="btn btn-secondary <s:if test="not statoAccantonamentoFondiDubbiaEsigibilitaDefinitivo">hidden</s:if>" id="pulsanteModificaStatoInBozza"      href="<s:url action="inserisciConfigurazioneStampaDubbiaEsigibilita_modificaStatoInBozza" />"     >torna in bozza</a></s:if>
					</p>
					
					<s:form id="form-salvaCapitoli"                   cssClass="hide" novalidate="novalidate" method="post" action="inserisciConfigurazioneStampaDubbiaEsigibilita_salvaCapitoli" />
					<s:form id="formRipristinaAccantonamento_hidden"  cssClass="hide" novalidate="novalidate" method="post" action="inserisciConfigurazioneStampaDubbiaEsigibilita_ripristinaAccantonamento" />
					<s:form id="formEliminaAccantonamento_hidden"     cssClass="hide" novalidate="novalidate" method="post" action="inserisciConfigurazioneStampaDubbiaEsigibilita_eliminaAccantonamento" />
					<s:form id="formModificaStatoInDefinitivo_hidden" cssClass="hide" novalidate="novalidate" method="post" action="inserisciConfigurazioneStampaDubbiaEsigibilita_modificaStatoInDefinitivo" />
					<s:form id="formModificaStatoInBozza_hidden"      cssClass="hide" novalidate="novalidate" method="post" action="inserisciConfigurazioneStampaDubbiaEsigibilita_modificaStatoInBozza" />
					<s:form id="formSelezionaVersione_hidden"         cssClass="hide" novalidate="novalidate" method="post" action="inserisciConfigurazioneStampaDubbiaEsigibilita_selezionaVersione" />
					<s:form id="formNuovaVersione_hidden"             cssClass="hide" novalidate="novalidate" method="post" action="inserisciConfigurazioneStampaDubbiaEsigibilita_nuovaVersione" />
				</div>
			</div>
		</div>
	</div>
	<s:include value="/jsp/dubbiaEsigibilita/popolaDaCapitoloDubbiaEsigibilta_modale.jsp">
		<s:param name="baseActionName">inserisciConfigurazioneStampaDubbiaEsigibilita</s:param>
	</s:include>
	<s:include value="/jsp/dubbiaEsigibilita/selezionaCapitoloDubbiaEsigibilita_modale.jsp">
		<s:param name="baseActionName">inserisciConfigurazioneStampaDubbiaEsigibilita</s:param>
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
	<script type="text/javascript" src="/siacbilapp/js/local/dubbiaEsigibilita/inserisciConfigurazioneStampaDubbiaEsigibilita.js"></script>

</body>
</html>