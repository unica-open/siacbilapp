<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>

<%-- Inclusione head e CSS --%>
<s:include value="/jsp/include/head.jsp" />
</head>

<body>
	<%-- Inclusione header --%>
	<s:include value="/jsp/include/header.jsp" />

	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12 ">
				<div class="step-content">
					<div class="step-pane active" id="step1">
						<s:include value="/jsp/include/messaggi.jsp" />
						<h3>
							Capitolo
							<s:property value="capitoloUscitaGestione.numeroCapitolo" /> /
							<s:property value="capitoloUscitaGestione.numeroArticolo" />
							<s:if test="gestioneUEB">
								/
								<s:property value="capitoloUscitaGestione.numeroUEB" />
							</s:if>
						</h3>
						<ul id="tabs" class="nav nav-tabs" data-tabs="tabs">
							<li class="active"><a href="#capitolo" data-toggle="tab">Capitolo</a></li>
							<li><a href="#attiDiLegge" data-toggle="tab">Atti di legge</a></li>
						</ul>
						<div id="my-tab-content" class="tab-content">
							<!-- BEGIN Blocco Capitolo -->
							<div class="tab-pane active" id="capitolo">
								<form method="post" action="aggiornamentoCapUscitaGestione.do"
									id="aggiornamentoCapUscitaGestione" name="formAggiornamento"
									novalidate="novalidate">
									<h3>Aggiorna Capitolo</h3>
									<p>
										<small>I campi contrassegnati con l'asterisco (*) sono
											obbligatori.</small>
									</p>
									<s:hidden name="capitoloUscitaGestione.uid" value="%{uidDaAggiornare}" />
									<s:hidden name="capitoloUscitaGestione.statoOperativoElementoDiBilancio" />
									<s:hidden name="capitoloUscitaGestione.stato" />
									<s:hidden name="bilancio.uid" />
									<s:hidden id="uidCapitoloDaAggiornare" value="%{uidDaAggiornare}" />
									<s:hidden name="daAggiornamento" />
									<fieldset class="form-horizontal">

										<div class="control-group">
											<label class="control-label" for="annoCapitolo">Anno</label>
											<div class="controls">
												<input type="text" id="annoCapitolo" class="lbTextSmall span2"
													value="${annoEsercizioInt}" disabled="disabled" maxlength="4" />
												<s:hidden name="bilancio.anno" value="%{annoEsercizioInt}" />
												<span class="al">
													<label class="radio inline" for="numeroCapitolo">Capitolo *</label>
												</span>
												<input type="text" id="numeroCapitolo" class="lbTextSmall span2"
													maxlength="200" disabled="disabled"
													value="${capitoloUscitaGestione.numeroCapitolo}" />
												<s:hidden name="capitoloUscitaGestione.numeroCapitolo"
													value="%{capitoloUscitaGestione.numeroCapitolo}" />
												<span class="al">
													<label class="radio inline" for="numeroArticolo">Articolo *</label>
												</span>
												<input type="text" id="numeroArticolo" class="lbTextSmall span2"
													maxlength="200" disabled="disabled"
													value="${capitoloUscitaGestione.numeroArticolo}" />
												<s:hidden name="capitoloUscitaGestione.numeroArticolo"
													value="%{capitoloUscitaGestione.numeroArticolo}" />
												<s:if test="gestioneUEB">
													<span class="al">
														<label class="radio inline" for="numeroUEB"><abbr
																title="Unit&agrave; Elementare Bilancio">UEB</abbr></label>
													</span>
													<input type="text" id="numeroUEB" class="lbTextSmall span2"
														maxlength="200" disabled="disabled"
														value="${capitoloUscitaGestione.numeroUEB}" />
												</s:if>
												<s:hidden name="capitoloUscitaGestione.numeroUEB"
													value="%{capitoloUscitaGestione.numeroUEB}" />
												<%-- Anno del capitolo e uid del bilancio --%>
												<s:hidden value="%{capitoloUscitaGestione.annoCapitolo}"
													name="capitoloUscitaGestione.annoCapitolo" />
											</div>
										</div>
										<%-- DATI secondari --%>
										<div class="control-group">
											<label for="descrizioneCapitolo" class="control-label">Descrizione *</label>
											<div class="controls">
												<s:textarea id="descrizioneCapitolo"
													name="capitoloUscitaGestione.descrizione" cssClass="span10"
													required="true" maxlength="500" rows="5" cols="15"
													disabled="%{!descrizioneEditabile}"></s:textarea>
												<s:if test="!descrizioneEditabile">
													<s:hidden name="capitoloUscitaGestione.descrizione" />
												</s:if>
											</div>
										</div>
										<div class="control-group">
											<label for="descrizioneArticolo" class="control-label">Descrizione
												Articolo</label>
											<div class="controls">
												<s:textarea id="descrizioneArticolo"
													name="capitoloUscitaGestione.descrizioneArticolo" cssClass="span10"
													required="true" maxlength="500" rows="5" cols="15"
													disabled="%{!descrizioneArticoloEditabile}"></s:textarea>
												<s:if test="!descrizioneArticoloEditabile">
													<s:hidden name="capitoloUscitaGestione.descrizioneArticolo" />
												</s:if>
											</div>
										</div>
										<%-- Primo gruppo collegato --%>
										<div class="control-group">
											<label for="missione" class="control-label">Missione *</label>
											<div class="controls">
												<s:select list="listaMissione" id="missione" name="missione.uid"
													required="true" cssClass="span10" headerKey="" headerValue=""
													listKey="uid" listValue="%{codice + '-' + descrizione}"
													disabled="%{!missioneEditabile}"
													data-original-uid="%{missione.uid}" />
												<s:if test="!missioneEditabile">
													<s:hidden name="missione.uid" />
												</s:if>
											</div>
										</div>
										<div class="control-group">
											<label for="programma" class="control-label">Programma *
												<a class="tooltip-test" title="selezionare prima la Missione" href="#">
													<i class="icon-info-sign">&nbsp; <span class="nascosto">selezionare
															prima la Missione</span></i>
												</a>
											</label>
											<div class="controls">
												<s:select id="programma" list="listaProgramma" name="programma.uid"
													cssClass="span10" required="required" headerKey="" headerValue=""
													listKey="uid" listValue="%{codice + '-' + descrizione}"
													disabled="%{!programmaEditabile || (missione == null || missione.uid == 0)}"
													data-original-uid="%{programma.uid}" />
												<s:if test="!programmaEditabile">
													<s:hidden name="programma.uid" />
												</s:if>
											</div>
										</div>
										<div class="control-group">
											<label for="classificazioneCofog" class="control-label">Cofog
												<a class="tooltip-test" title="selezionare prima il Programma" href="#">
													<i class="icon-info-sign">&nbsp; <span class="nascosto">selezionare
															prima il Programma</span></i>
												</a>
											</label>
											<div class="controls">
												<s:select id="classificazioneCofog" list="listaClassificazioneCofog"
													name="classificazioneCofog.uid" cssClass="span10" headerKey=""
													headerValue="" listKey="uid"
													listValue="%{codice + '-' + descrizione}"
													disabled="%{!classificazioneCofogEditabile || (programma == null || programma.uid == 0)}"
													data-original-uid="%{classificazioneCofog.uid}" />
												<s:if test="!classificazioneCofogEditabile">
													<s:hidden name="classificazioneCofog.uid" />
												</s:if>
											</div>
										</div>
										<%-- Secondo gruppo collegato --%>
										<div class="control-group">
											<label for="titoloSpesa" class="control-label">Titolo *
												<a class="tooltip-test" title="selezionare prima il Programma" href="#">
													<i class="icon-info-sign">&nbsp;
														<span class="nascosto">selezionare prima il Programma</span>
													</i>
												</a>
											</label>
											<div class="controls">
												<s:select list="listaTitoloSpesa" name="titoloSpesa.uid"
													id="titoloSpesa" cssClass="span10" required="required"
													disabled="%{!titoloSpesaEditabile || (programma == null || programma.uid == 0)}"
													listKey="uid" listValue="%{codice + ' - ' + descrizione}"
													headerKey="" headerValue="" />
												<input type="hidden" value="${titoloSpesa.uid}" />
											</div>
										</div>
										<div class="control-group">
											<label for="macroaggregato" class="control-label">Macroaggregato *
												<a class="tooltip-test" title="selezionare prima il Titolo" href="#">
													<i class="icon-info-sign">&nbsp; <span class="nascosto">selezionare
															prima il Titolo</span></i>
												</a>
											</label>
											<div class="controls">
												<s:select id="macroaggregato" list="listaMacroaggregato"
													name="macroaggregato.uid" cssClass="span10" required="required"
													headerKey="" headerValue="" listKey="uid"
													listValue="%{codice + '-' + descrizione}"
													disabled="%{!macroaggregatoEditabile || (titoloSpesa == null || titoloSpesa.uid == 0)}"
													data-original-uid="%{macroaggregato.uid}" />
												<s:if test="!macroaggregatoEditabile">
													<s:hidden name="macroaggregato.uid" />
												</s:if>
											</div>
										</div>
										<%-- zTree --%>
										<div class="control-group">
											<label for="bottonePdC" class="control-label">
												<abbr title="Piano dei Conti">P.d.C.</abbr> finanziario *
												<a class="tooltip-test" title="selezionare prima il Macroaggregato"
													href="#">
													<i class="icon-info-sign">&nbsp;
														<span class="nascosto">selezionare prima il
															Macroaggregato</span>
													</i>
												</a>
											</label>
											<div class="controls">
												<s:hidden id="HIDDEN_ElementoPianoDeiContiUid"
													name="elementoPianoDeiConti.uid"
													data-original-uid="%{elementoPianoDeiConti.uid}" />
												<s:hidden id="HIDDEN_ElementoPianoDeiContiStringa"
													name="pdcFinanziario" />
												<s:hidden id="HIDDEN_ElementoPianoDeiContiEditabile"
													name="elementoPianoDeiContiEditabile" />
												<s:if test="elementoPianoDeiContiEditabile">
													<a href="#myModal" role="button" class="btn" data-toggle="modal"
														disabled="disabled" id="bottonePdC">
														Seleziona il Piano dei Conti &nbsp;
														<i class="icon-spin icon-refresh spinner"
															id="SPINNER_ElementoPianoDeiConti"></i>
													</a>
													&nbsp;
												</s:if>
												<span id="SPAN_ElementoPianoDeiConti">
													<s:if test='%{pdcFinanziario != null && pdcFinanziario neq ""}'>
														<s:property value="pdcFinanziario" />
													</s:if>
													<s:else>
														Nessun P.d.C. finanziario selezionato
													</s:else>
												</span>
											</div>
										</div>
										<%-- zTree --%>
										<div class="control-group">
											<label for="bottoneSIOPE" class="control-label">
												<abbr
													title="Sistema Informativo sulle OPerazioni degli Enti pubblici">SIOPE</abbr>
												<%--a class="tooltip-test" title="selezionare prima il P.d.C." href="#">
													<i class="icon-info-sign">&nbsp;<span class="nascosto">selezionare prima il P.d.C.</span></i>
												</a--%>
											</label>
											<%--div class="controls">
												<s:hidden id="HIDDEN_SIOPEUid" name="siopeSpesa.uid" data-original-uid="%{siopeSpesa != null ? siopeSpesa.uid : ''}" />
												<s:hidden id="HIDDEN_SIOPEStringa" name="siopeInserito" />
												<s:hidden id="HIDDEN_SIOPEEditabile" name="siopeSpesaEditabile"/>
												<s:if test="siopeSpesaEditabile">
													<a href="#modaleSIOPE" role="button" class="btn" data-toggle="modal" id="bottoneSIOPE">
														Seleziona il codice SIOPE
														<i class="icon-spin icon-refresh spinner" id="SPINNER_SIOPE"></i>
													</a>
												</s:if>
												&nbsp;
												<span id="SPAN_SIOPE">
													<s:if test='%{siopeInserito != null && siopeInserito neq ""}'>
														<s:property value="siopeInserito"/>
													</s:if><s:else>
														Nessun SIOPE selezionato
													</s:else>
												</span>
											</div--%>
											<div class="controls">
												<s:hidden id="HIDDEN_SIOPEEditabile" name="siopeSpesaEditabile" />
												<s:if test="siopeSpesaEditabile">
													<s:textfield id="siopeSpesa" name="siopeSpesa.codice"
														cssClass="span3" />
												</s:if>
												<s:else>
													<s:property value="siopeSpesa.codice" />
													<s:hidden name="siopeSpesa.codice" />
												</s:else>
												&nbsp;<span id="descrizioneSiopeSpesa">
													<s:property value="siopeSpesa.descrizione" /></span>
												<s:hidden id="HIDDEN_idSiopeSpesa" name="siopeSpesa.uid" />
												<s:hidden id="HIDDEN_descrizioneSiopeSpesa"
													name="siopeSpesa.descrizione" />
												<s:if test="siopeSpesaEditabile">
													<span class="radio guidata">
														<button type="button" class="btn btn-primary"
															id="compilazioneGuidataSIOPE">compilazione guidata</button>
													</span>
												</s:if>
											</div>
										</div>
										<%-- zTree --%>
										<div class="control-group">
											<label for="bottoneSAC" class="control-label"> Struttura
												Amministrativa Responsabile * </label>
											<div class="controls">
												<s:hidden id="HIDDEN_StrutturaAmministrativoContabileUid"
													name="strutturaAmministrativoContabile.uid"
													data-original-uid="%{strutturaAmministrativoContabile.uid}" />
												<s:hidden id="HIDDEN_StrutturaAmministrativoContabileStringa"
													name="strutturaAmministrativoResponsabile" />
												<s:hidden id="HIDDEN_StrutturaAmministrativoContabileDescrizione"
													name="strutturaAmministrativoContabile.descrizione" />
												<s:hidden id="HIDDEN_StrutturaAmministrativoContabileEditabile"
													name="strutturaAmministrativoContabileEditabile" />
												<s:if test="strutturaAmministrativoContabileEditabile">
													<a href="#struttAmm" role="button" class="btn" id="bottoneSAC"
														data-toggle="modal">
														Seleziona la Struttura amministrativa &nbsp;
														<i class="icon-spin icon-refresh spinner"
															id="SPINNER_StrutturaAmministrativoContabile"></i>
													</a>
													&nbsp;
												</s:if>
												<span id="SPAN_StrutturaAmministrativoContabile">
													<s:if
														test='%{strutturaAmministrativoResponsabile != null && strutturaAmministrativoResponsabile neq ""}'>
														<s:property value="strutturaAmministrativoResponsabile" />
													</s:if>
													<s:else>
														Nessuna Struttura Amministrativa Responsabile selezionata
													</s:else>
												</span>
											</div>
										</div>
										<div class="control-group">
											<label for="categoriaCapitolo" class="control-label">Tipo capitolo</label>
											<div class="controls">
												<select id="categoriaCapitolo"
													name="capitoloUscitaGestione.categoriaCapitolo.uid" class="span10"
													<s:if test="!categoriaCapitoloEditabile">disabled</s:if>>
													<option></option>
													<s:iterator value="listaCategoriaCapitolo" var="cc">
														<option data-codice="<s:property value=" #cc.codice" />" value="
														<s:property value="#cc.uid" />" <s:if
															test="%{capitoloUscitaGestione.categoriaCapitolo.uid == #cc.uid}">
															selected</s:if>>
														<s:property value="%{#cc.codice + ' - ' + #cc.descrizione}"/>
														</option>
													</s:iterator>
												</select>
												<!-- SIAC 6884- condizione per passare il valore quando viene disabilitata la categoria-->
												<s:if test="stanziamentiNegativiPresenti || !categoriaCapitoloEditabile">
													<s:hidden name="capitoloUscitaGestione.categoriaCapitolo.uid" />
												</s:if>
											</div>
										</div>
										<div class="control-group">
											<label for="flagImpegnabile" class="control-label">Impegnabile</label>
											<div class="controls">
												<s:checkbox id="flagImpegnabile" name="capitoloUscitaGestione.flagImpegnabile" disabled="%{!flagImpegnabileEditabile}" />
											</div>
											<s:if test="stanziamentiNegativiPresenti || !flagImpegnabileEditabile">
												<s:hidden name="capitoloUscitaGestione.flagImpegnabile" />
											</s:if>
										</div>
									</fieldset>
	
									<%-- Tabella 2 --%>
									<div class="accordion" id="accordion2">
										<div class="accordion-group">
											<div class="accordion-heading">
												<a class="accordion-toggle collapsed" data-toggle="collapse"
													id="ModalAltriDati" data-parent="#accordion2"
													href="#collapseOne">
													Altri dati<span class="icon">&nbsp;</span>
												</a>
											</div>
											<div id="collapseOne" class="accordion-body collapse">
												<div class="accordion-inner">
													<fieldset class="form-horizontal">
														<div class="control-group">
															<label class="control-label" for="exAnnoCapitolo">Ex Anno</label>
															<div class="controls">
																<s:textfield id="exAnnoCapitolo" cssClass="lbTextSmall span2" name="capitoloUscitaGestione.exAnnoCapitolo" maxlength="4" placeholder="%{placeholderAnnoExCapitolo}" disabled="!exAnnoEditabile" />
																<span class="al">
																	<label class="radio inline" for="exCapitolo">Ex
																		Capitolo</label>
																</span>
																<input type="text" id="exCapitolo" class="lbTextSmall span2"
																	name="capitoloUscitaGestione.exCapitolo" maxlength="200"
																	placeholder="capitolo"
																	value="${capitoloUscitaGestione.exCapitolo}" />
																<span class="al">
																	<label class="radio inline" for="exArticolo">Ex
																		Articolo</label>
																</span>
																<input type="text" id="exArticolo" class="lbTextSmall span2"
																	name="capitoloUscitaGestione.exArticolo" maxlength="200"
																	placeholder="articolo"
																	value="${capitoloUscitaGestione.exArticolo}" />
																<s:if test="gestioneUEB">
																	<span class="al">
																		<label class="radio inline" for="exUEB"> Ex
																			<abbr title="Unit&agrave; Elementare Bilancio">UEB</abbr>
																		</label>
																	</span>
																	<input type="text" id="exUEB" class="lbTextSmall span2"
																		name="capitoloUscitaGestione.exUEB" maxlength="200"
																		placeholder="UEB"
																		value="${capitoloUscitaGestione.exUEB}" />
																</s:if>
															</div>
														</div>
														<div class="control-group">
															<label for="tipoFinanziamento" class="control-label">Tipo Finanziamento</label>
															<div class="controls input-append">
																<s:select id="tipoFinanziamento" name="tipoFinanziamento.uid"
																	list="listaTipoFinanziamento" cssClass="span10"
																	headerKey="" headerValue="" disabled="%{!tipoFinanziamentoEditabile}"
																	value="%{tipoFinanziamento.uid}" listKey="uid"
																	listValue="%{codice + '-' + descrizione}" />
																<s:if test="!tipoFinanziamentoEditabile">
																	<s:hidden name="tipoFinanziamento.uid" />
																</s:if>
															</div>
														</div>
														<div class="control-group">
															<label for="rilevanteIva" class="control-label">
																Rilevante IVA </label>
															<div class="controls">
																<s:checkbox id="rilevanteIva"
																		name="capitoloUscitaGestione.flagRilevanteIva" />
															</div>
														</div>
														<div class="control-group">
															<label for="delegate" class="control-label">
																Funzioni delegate dalla Regione </label>
															<div class="controls">
																<s:checkbox id="delegate"
																		name="capitoloUscitaGestione.funzDelegateRegione" />
															</div>
														</div>
														<div class="control-group">
															<label for="tipoFondo" class="control-label">Tipo fondo</label>
															<div class="controls input-append">
																<s:select id="tipoFondo" name="tipoFondo.uid" list="listaTipoFondo"
																	cssClass="span10" headerKey="" disabled="%{!tipoFondoEditabile}"
																	headerValue="" value="%{tipoFondo.uid}" listKey="uid"
																	listValue="%{codice + '-' + descrizione}" />
																<s:if test="!tipoFondoEditabile">
																	<s:hidden name="tipoFondo.uid" />
																</s:if>
															</div>
														</div>
														<div class="control-group">
															<span class="control-label">Spesa</span>
															<div class="controls">
																<s:iterator value="listaRicorrenteSpesa" var="ricorrente" status="stat">
																	<label class="radio inline">
																		<input type="radio" name="ricorrenteSpesa.uid" value="<s:property value="%{#ricorrente.uid}" />" <s:if test="%{ricorrenteSpesa.uid == #ricorrente.uid}">checked="checked"</s:if>>&nbsp;<s:property value="%{#ricorrente.descrizione}" />
																	</label>
																</s:iterator>
															</div>
														</div>
														<div class="control-group">
															<label for="perimetroSanitario" class="control-label">Codifica identificativo del perimetro sanitario</label>
															<div class="controls input-append">
																<s:select list="listaPerimetroSanitarioSpesa" id="perimetroSanitario" cssClass="span10"
																		name="perimetroSanitarioSpesa.uid" headerKey="" headerValue=""
																		listKey="uid" listValue="%{codice + '-' + descrizione}" />
															</div>
														</div>
														<div class="control-group">
															<label for="transazioneUnioneEuropea" class="control-label">Codifica transazione UE</label>
															<div class="controls input-append">
																<s:select list="listaTransazioneUnioneEuropeaSpesa" id="transazioneUnioneEuropea" cssClass="span10"
																		name="transazioneUnioneEuropeaSpesa.uid" headerKey="" headerValue=""
																		listKey="uid" listValue="%{codice + '-' + descrizione}" />
															</div>
														</div>
														<div class="control-group">
															<label for="politicheRegionaliUnitarie" class="control-label">Codifica politiche regionali unitarie</label>
															<div class="controls input-append">
																<s:select list="listaPoliticheRegionaliUnitarie" id="politicheRegionaliUnitarie" cssClass="span10"
																		name="politicheRegionaliUnitarie.uid" headerKey="" headerValue=""
																		listKey="uid" listValue="%{codice + '-' + descrizione}" />
															</div>
														</div>
														<%-- Classificatori Generici --%>
														<s:iterator var="idx" begin="1" end="%{numeroClassificatoriGenerici}">
															<s:if test="%{#attr['labelClassificatoreGenerico' + #idx] != null}">
																<div class="control-group">
																	<label for="classificatoreGenerico<s:property value="%{#idx}"/>" class="control-label">
																		<s:property value="%{#attr['labelClassificatoreGenerico' + #idx]}"/>
																	</label>
																	<div class="controls">
																		<s:select list="%{#attr['listaClassificatoreGenerico' + #idx]}" id="classificatoreGenerico%{#idx}"
																			cssClass="span10" name="%{'classificatoreGenerico' + #idx + '.uid'}" headerKey="" headerValue=""
																			disabled="%{!#attr['classificatoreGenerico' + #idx + 'Editabile']}"
																			listKey="uid" listValue="%{codice + '-' + descrizione}" />
																		<s:if test="%{!#attr['classificatoreGenerico' + #idx + 'Editabile']">
																			<s:hidden name="%{'classificatoreGenerico' + #idx + '.uid'}" />
																		</s:if>
																		<input type="hidden" id="classificatoreGenerico3Hidden" name="classificatoreGenerico3.uid" value="" disabled>
																	</div>
																</div>
															</s:if>
														</s:iterator>
														<s:hidden value="%{capitoloFondino}" name="capitoloFondino" id="fondinoHiddenValue"/>														
														<div class="control-group">
															<label for="note" class="control-label">Note</label>
															<div class="controls">
																<s:textarea id="note" name="capitoloUscitaGestione.note" rows="5" cols="15"></s:textarea>
															</div>
														</div>
													</fieldset>
												</div>
											</div>
										</div>
									</div>
	
									<div class="alert alert-warning hide" id="alertErrori">
										<button type="button" class="close" data-hide="alert">&times;</button>
										<strong>Attenzione!!</strong><br>
										<ul id="errori"></ul>
									</div>

									<h4>Stanziamenti</h4>
									<table summary="riepilogo incarichi" class="table table-bordered" id="tabellaAggiornaVariazioni">
	
										<tr>
											<th width="12%">&nbsp;</th>
											<th width="12%">&nbsp;</th>
											<th  class="text-center" width="11%">${annoEsercizioInt - 1}</th>
											<th  class="text-center" width="11%">Residui ${annoEsercizioInt + 0}</th>
											<th  class="text-center" width="11%">${annoEsercizioInt + 0}</th>
											<th  class="text-center" width="11%">${annoEsercizioInt + 1}</th>
											<th  class="text-center" width="11%">${annoEsercizioInt + 2}</th>
											<th  class="text-center" width="11%">>${annoEsercizioInt + 2}</th>
										</tr>
										
										<!-- SIAC-6881 scope="rowgroup"-->
										<tr  class="componentiRowFirst">
											<th rowspan = "3" class="stanziamenti-titoli">
												<a id="competenzaTotale" href="#tabellaAggiornaVariazioni"  class="disabled" >Competenza</a>
											</th>
											<td class="text-center">
												Stanziamento
											</td>
											<td class="text-right">
												<label for="stanziamentoM1" class="nascosto">inserisci importo</label>
												<span id="stanziamentoM1" ><s:property value="competenzaStanziamento.dettaglioAnnoPrec.importo"/></span>
												<s:hidden id="HIDDEN_StanziamentoM1" value="%{competenzaStanziamento.dettaglioAnnoPrec.importo}" name="competenzaStanziamento.dettaglioAnnoPrec.importo" />
												
											</td>
											<td class="text-right"> <!--Residuo -->
												<label for="stanziamentoR" class="nascosto">inserisci importo</label>
												<span id="stanziamentoR" ><s:property value="competenzaStanziamento.dettaglioResidui.importo"/></span>
												<s:hidden id="HIDDEN_StanziamentoR" value="%{competenzaStanziamento.dettaglioResidui.importo}" name="competenzaStanziamento.dettaglioResidui.importo" />
													
											</td>
											<td class="text-right">
												<label for="stanziamento0" class="nascosto">inserisci importo</label>
												<s:property value="competenzaStanziamento.dettaglioAnno0.importo"/> 
												<s:hidden value="%{competenzaStanziamento.dettaglioAnno0.importo}" name="competenzaStanziamento.dettaglioAnno0.importo" />
											</td>
											<td class="text-right">
												<label for="stanziamento1" class="nascosto">inserisci importo</label>
												<s:property value="competenzaStanziamento.dettaglioAnno1.importo"/> 
												<s:hidden value="%{competenzaStanziamento.dettaglioAnno1.importo}" name="competenzaStanziamento.dettaglioAnno1.importo" />
												
											</td>
											<td class="text-right">
												<label for="stanziamento2" class="nascosto">inserisci importo</label>
												<s:property value="competenzaStanziamento.dettaglioAnno2.importo"/>
												<s:hidden value="%{competenzaStanziamento.dettaglioAnno2.importo}" name="competenzaStanziamento.dettaglioAnno2importo" />
											</td>
											<td class="text-right">
													<label for="stanziamentoSucc" class="nascosto">inserisci importo</label>
													<s:property value="competenzaStanziamento.dettaglioAnniSucc.importo"/>
													<s:hidden value="%{competenzaStanziamento.dettaglioAnniSucc.importo}" name="competenzaStanziamento.dettaglioAnniSucc.importo" />
					
											</td>
										</tr>
										<tr class="componentiRowOther"> <!--  in aggiorna, l'impegnato corrisponde con la disponibilità. FIX -->
											<td class="text-center">Impegnato</td>
											<td class="text-right">
												<s:property value="disponibilita.dettaglioAnnoPrec.importo"/>
												<!-- <s:hidden value="%{disponibilita.dettaglioAnnoPrec.importo}" name="disponibilita.dettaglioAnnoPrec.importo" /> -->
	
											</td>
											<td class="text-right">
												<s:property value="disponibilita.dettaglioResidui.importo"/>
												<!-- <s:hidden value="%{disponibilita.dettaglioResidui.importo}" name="disponibilita.dettaglioResidui.importo" /> -->
												
											</td>
											<td class="text-right">
												<s:property value="disponibilitaCapitoloUscitaGestioneAnno0.impegnato"/>
												<s:hidden value="%{disponibilitaCapitoloUscitaGestioneAnno0.impegnato}" name="disponibilitaCapitoloUscitaGestioneAnno0.impegnato" />
											</td>
											<td class="text-right">
												<s:property value="disponibilitaCapitoloUscitaGestioneAnno1.impegnato"/>
												<s:hidden value="%{disponibilitaCapitoloUscitaGestioneAnno1.impegnato}" name="disponibilitaCapitoloUscitaGestioneAnno1.impegnato" />
											</td>
											<td class="text-right">
												<s:property value="disponibilitaCapitoloUscitaGestioneAnno2.impegnato"/>
												<s:hidden value="%{disponibilitaCapitoloUscitaGestioneAnno2.impegnato}" name="disponibilitaCapitoloUscitaGestioneAnno2.impegnato" />
											</td>
											<td class="text-right">
													<s:property value="disponibilita.dettaglioAnniSucc.importo"/>
													<!-- <s:hidden value="%{disponibilita.dettaglioAnniSucc.importo}" name="disponibilita.dettaglioAnniSucc.importo" /> -->
											</td>											
										</tr>
										<tr class="componentiRowOther">
											<td class="text-center">Disponibilit&agrave; ad impegnare</td>
											<td class="text-right">
												<s:property value="disponibilita.dettaglioAnnoPrec.importo"/>
												<!-- <s:hidden value="%{disponibilita.dettaglioAnnoPrec.importo}" name="disponibilita.dettaglioAnnoPrec.importo" /> -->

											</td>
											<td class="text-right">
												<s:property value="disponibilita.dettaglioResidui.importo"/>
												<!-- <s:hidden value="%{disponibilita.dettaglioResidui.importo}" name="disponibilita.dettaglioResidui.importo" />							 -->
											</td>
											<td class="text-right">
												<s:property value="disponibilita.dettaglioAnno0.importo"/>
												<s:hidden value="%{disponibilita.dettaglioAnno0.importo}" name="disponibilita.dettaglioAnno0.importo" />
											</td>
											<td class="text-right">
												<s:property value="disponibilita.dettaglioAnno1.importo"/>
												<s:hidden value="%{disponibilita.dettaglioAnno0.importo}" name="disponibilita.dettaglioAnno1.importo" />
											</td>
											<td class="text-right">
												<s:property value="disponibilita.dettaglioAnno2.importo"/>
												<s:hidden value="%{disponibilita.dettaglioAnno0.importo}" name="disponibilita.dettaglioAnno2.importo" />
											</td>
											<td class="text-right">
													<s:property value="disponibilita.dettaglioAnniSucc.importo"/>
													<!-- <s:hidden value="%{disponibilita.dettaglioAnniSucc.importo}" name="disponibilita.dettaglioAnniSucc.importo" /> -->
											</td>											
										</tr>
										<tbody id="componentiCompetenzaAgg"> <!--residui e anno >n+2-->
											<s:iterator value="importiComponentiCapitolo" var="cc">												
												
												
												<s:if test="#cc.tipoComponenteImportiCapitolo.descrizione=='Stanziamento'">
													<tr  class="componentiCompetenzaRow" >
												</s:if>
												<s:else>
													<tr  class="componentiRowOther" >
												</s:else>
												
																									
													<td id="description-component" class="componenti-competenza" rowspan="1">
														<s:property value="#cc.tipoComponenteImportiCapitolo.descrizione"/>
													</td>
													
													<td class="text-center" id="type-component">
															<s:property value="#cc.tipoDettaglioComponenteImportiCapitolo.descrizione"/>
													</td>
														
													<td class="text-right" id="type-component">
														<s:property value="#cc.dettaglioAnnoPrec.importo"/>
													</td>
													
													 <td class="text-right"> 
														<s:property value="#cc.dettaglioResidui.importo"/>
													</td>
													
													<td class="text-right">
														<s:property value="#cc.dettaglioAnno0.importo"/>
													</td>
													
													<td class="text-right">
														<s:property value="#cc.dettaglioAnno1.importo"/>
													</td>
													<td class="text-right">
														<s:property value="#cc.dettaglioAnno2.importo"/>
													</td>
													<td class="text-right">
														<s:property value="#cc.dettaglioAnniSucc.importo"/>
													</td>
												</tr>
											</s:iterator>
										</tbody>
									
										<tr class="componentiRowFirst">
											<th rowspan="2" class="stanziamenti-titoli">Residuo</th>
											<td class="text-center">
												Presunti
											</td>
											<td class="text-right">
												<label for="residuoPrM1" class="nascosto">inserisci importo</label>
												<span id="residuoPrM1"><s:property value="residuiPresunti.dettaglioAnnoPrec.importo"/></span>
												<s:hidden id="HIDDEN_StanziamentoResiduoM1" value="%{residuiPresunti.dettaglioAnnoPrec.importo}" name="residuiPresunti.dettaglioAnnoPrec.importo" />
												
											</td>
											<td class="text-right">
													<s:property value="residuiPresunti.dettaglioResidui.importo"/>
													<s:hidden value="%{residuiPresunti.dettaglioResidui.importo}" name="residuiPresunti.dettaglioResidui.importo" />
											</td>
											<td class="text-right">
												<label for="residuo0" class="nascosto">inserisci importo</label>
												<s:property value="residuiPresunti.dettaglioAnno0.importo"/>
												<s:hidden value="%{residuiPresunti.dettaglioAnno0.importo}" name="residuiPresunti.dettaglioAnno0.importo" />
													
											</td>
											<td class="text-right">
												<label for="residuo1" class="nascosto">inserisci importo</label>
												<s:property value="residuiPresunti.dettaglioAnno1.importo"/>
												<s:hidden value="%{residuiPresunti.dettaglioAnno1.importo}" name="residuiPresunti.dettaglioAnno1.importo" />
												
											</td>
											<td class="text-right">
												<label for="residuo2" class="nascosto">inserisci importo</label>
												<s:property value="residuiPresunti.dettaglioAnno2.importo"/>
												<s:hidden value="%{residuiPresunti.dettaglioAnno2.importo}" name="residuiPresunti.dettaglioAnno2.importo" />
												
											</td>
											<td class="text-right">
													<label for="residuoSucc" class="nascosto">inserisci importo</label>
													<s:property value="residuiPresunti.dettaglioAnniSucc.importo"/>
													<s:hidden value="%{residuiPresunti.dettaglioAnniSucc.importo}" name="residuiPresunti.dettaglioAnniSucc.importo" />
													
											</td>
										</tr>
										
										<tr class="componentiRowOther">
											<td class="text-center">Effettivi</td>
											<td class="text-right">
												<span id="residuoEffM1"><s:property value="residuiEffettivi.dettaglioAnnoPrec.importo"/></span>
												<s:hidden value="%{residuiEffettivi.dettaglioAnnoPrec.importo}" name="residuiEffettivi.dettaglioAnnoPrec.importo" />
																									
											</td>
											<td class="text-right">
													<s:property value="residuiEffettivi.dettaglioResidui.importo"/>
													<s:hidden value="%{residuiEffettivi.dettaglioResidui.importo}" name="residuiEffettivi.dettaglioResidui.importo" />
											
											</td>
											<td class="text-right">
												<s:property value="residuiEffettivi.dettaglioAnno0.importo"/>
												<s:hidden value="%{residuiEffettivi.dettaglioAnno0.importo}" name="residuiEffettivi.dettaglioAnno0.importo" />
													
											</td>
											<td class="text-right">
												<s:property value="residuiEffettivi.dettaglioAnno1.importo"/>
												<s:hidden value="%{residuiEffettivi.dettaglioAnno1.importo}" name="residuiEffettivi.dettaglioAnno1.importo" />
												
											</td>
											<td class="text-right">
												<s:property value="residuiEffettivi.dettaglioAnno2.importo"/>
												<s:hidden value="%{residuiEffettivi.dettaglioAnno2.importo}" name="residuiEffettivi.dettaglioAnno2.importo" />
												
											</td>
											<td class="text-right">
													<s:property value="residuiEffettivi.dettaglioAnniSucc.importo"/>
													<s:hidden value="%{residuiEffettivi.dettaglioAnniSucc.importo}" name="residuiEffettivi.dettaglioAnniSucc.importo" />
											</td>
											
										</tr>										
										<tr class="componentiRowFirst">
											<th rowspan="2" class="stanziamenti-titoli">Cassa</th>
											<td class="text-center">
												Stanziamento
											</td>
											<td class="text-right">
												<label for="cassaM1" class="nascosto">inserisci importo</label>
												<span id="impCasM1" ><s:property value="cassaStanziato.dettaglioAnnoPrec.importo"/></span>
												<s:hidden id="HIDDEN_StanziamentoCassaM1" value="%{cassaStanziato.dettaglioAnnoPrec.importo}" name="cassaStanziato.dettaglioAnnoPrec.importo" />
												
											</td>
											<td class="text-right">
													<s:property value="cassaStanziato.dettaglioResidui.importo"/>
													<s:hidden value="%{cassaStanziato.dettaglioResidui.importo}" name="cassaStanziato.dettaglioResidui.importo" />
											</td>
											<td class="text-right">
												<label for="cassa0" class="nascosto">inserisci importo</label>
												<s:property value="cassaStanziato.dettaglioAnno0.importo"/>
												<s:hidden value="%{cassaStanziato.dettaglioAnno0.importo}" name="cassaStanziato.dettaglioAnno0.importo" />
												
											</td>
											<td class="text-right">
												<label for="cassa1" class="nascosto">inserisci importo</label>
												<s:property value="cassaStanziato.dettaglioAnno1.importo"/>
												<s:hidden value="%{cassaStanziato.dettaglioAnno1.importo}" name="cassaStanziato.dettaglioAnno1.importo" />
												
											</td>
											<td class="text-right">
												<label for="cassa2" class="nascosto">inserisci importo</label>
												&nbsp;
												<s:hidden value="%{cassaStanziato.dettaglioAnno2.importo}" name="cassaStanziato.dettaglioAnno2.importo" />
												
											</td>
											<td class="text-right">
													&nbsp;
													<s:hidden value="%{cassaStanziato.dettaglioAnniSucc.importo}" name="cassaStanziato.dettaglioAnniSucc.importo" />
											</td>
											
										</tr>
										
										<tr class="componentiRowOther">											
											<td class="text-center">Pagato</td>
											<td class="text-right">
												<span id="impCasM1" ><s:property value="cassaPagato.dettaglioAnnoPrec.importo"/></span>
												<s:hidden  value="%{cassaPagato.dettaglioAnnoPrec.importo}" name="cassaPagato.dettaglioAnnoPrec.importo" />
												
											</td>
											<td class="text-right">
												<s:property value="disponibilitaCapitoloUscitaGestioneResiduo.pagato"/>
												<s:hidden value="%{disponibilitaCapitoloUscitaGestioneResiduo.pagato}" name="disponibilitaCapitoloUscitaGestioneResiduo.pagato" />
											
											</td>
											<td class="text-right">
													<s:property value="disponibilitaCapitoloUscitaGestioneAnno0.pagato"/>
													<s:hidden  value="%{disponibilitaCapitoloUscitaGestioneAnno0.pagato}" name="disponibilitaCapitoloUscitaGestioneAnno0.pagato" />
													
												</td>
											<td class="text-right">
												<s:property value="disponibilitaCapitoloUscitaGestioneAnno1.pagato"/>
												<s:hidden  value="%{disponibilitaCapitoloUscitaGestioneAnno1.pagato}" name="disponibilitaCapitoloUscitaGestioneAnno1.pagato" />
												
											</td>
											<td class="text-right">
												<%--<s:property value="cassaPagato.dettaglioAnno2.importo"/>--%>
												<s:hidden  value="%{disponibilitaCapitoloUscitaGestioneAnno2.pagato}" name="disponibilitaCapitoloUscitaGestioneAnno2.pagato" />
											</td>
											<td class="text-right">
													<%--<s:property value="cassaPagato.dettaglioAnniSucc.importo"/>--%>
													<s:hidden  value="%{cassaPagato.dettaglioAnniSucc.importo}" name="cassaPagato.dettaglioAnniSucc.importo" />
											</td>
										</tr>
									</table>
									<s:hidden name="importiCapitoloUscitaGestione0.massimoImpegnabile" data-maintain="" />
									<s:hidden name="importiCapitoloUscitaGestione1.massimoImpegnabile" data-maintain="" />
									<s:hidden name="importiCapitoloUscitaGestione2.massimoImpegnabile" data-maintain="" />
									<div class="spaceBottom">
										<div class="btn-group">
<!-- 											<a class="btn dropdown-toggle" data-toggle="dropdown" href="#"> -->
<%-- 												calcola<span class="caret"></span> --%>
<!-- 											</a> -->
											<ul class="dropdown-menu">
												<li>
													<a href="#cassa0" id="pulsanteStanziamentoAnnoPrecedente">
														<abbr title="stanziamento anno precedente">stanz. anno prec.</abbr>
													</a>
												</li>
											</ul>
										</div>&nbsp;<span class="nascosto">|</span>
									</div>
									<p>
										<s:include value="/jsp/include/indietro.jsp" />
										<s:url action="aggiornaCapUscitaGestione.do" var="urlAnnulla">
											<s:param name="uidDaAggiornare" value="%{model.uidDaAggiornare}"></s:param>
											<s:param name="daAggiornamento">true</s:param>
										</s:url>
										<s:a cssClass="btn" href="%{urlAnnulla}">
											annulla
										</s:a>
										<s:submit cssClass="btn btn-primary pull-right" value="salva"/>
									</p>
									<div id="myModal" class="modal hide fade" tabindex="-1"
										role="dialog" aria-labelledby="myModalLabel"
										aria-hidden="true">
										<div class="modal-header">
											<button type="button" class="close" data-dismiss="modal"
												aria-hidden="true">×</button>
											<h3 id="myModalLabel">Piano dei Conti</h3>
										</div>
										<div class="modal-body">
											<ul id="treePDC" class="ztree"></ul>
										</div>
										<div class="modal-footer">
											<button id="deselezionaElementoPianoDeiConti" class="btn">Deseleziona</button>
											<button type="button" class="btn btn-primary pull-right" data-dismiss="modal" aria-hidden="true">Conferma</button>
										</div>
									</div>
									<%--<div id="modaleSIOPE" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
										<div class="modal-header">
											<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
											<h4 id="myModalLabel">SIOPE</h4>
										</div>
										<div class="modal-body">
											<ul id="treeSIOPE" class="ztree"></ul>
										</div>
										<div class="modal-footer">
											<button id="deselezionaSIOPE" class="btn">Deseleziona</button>
											<button type="button" class="btn btn-primary pull-right" data-dismiss="modal" aria-hidden="true">Conferma</button>
										</div>
									</div>--%>
									<div id="struttAmm" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
										<div class="modal-header">
											<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
											<h3 id="myModalLabel2">Struttura Amministrativa Responsabile</h3>
										</div>
										<div class="modal-body">
											<ul id="treeStruttAmm" class="ztree"></ul>
										</div>
										<div class="modal-footer">
											<button id="deselezionaStrutturaAmministrativoContabile" class="btn">Deseleziona</button>
											<button type="button" class="btn btn-primary pull-right" data-dismiss="modal" aria-hidden="true">Conferma</button>
										</div>
									</div>
								</form>

								
								<s:hidden value="%{stanziamentiNegativiPresenti}" id="stanziamentiNegativiPresentiHidden" />
								<s:hidden value="%{componentiDiversiDaFresco}" id="componentiDiversiDaFrescoHidden" />
								<s:include value="/jsp/cap/modaleCompilazioneGuidataSIOPE.jsp">
									<s:param name="ricercaUrl">ricercaClassificatoreGerarchico_siopeSpesa.do</s:param>
									<s:param name="ajaxUrl">risultatiRicercaSiopeSpesaAjax.do</s:param>
								</s:include>
							</div>
							<%-- BEGIN Blocco Vincoli -->
<!-- 							<div class="tab-pane active" id="vincoli"> -->
<!-- 									<h3>Vincoli</h3> -->
<!-- 							</div> -->
							<!-- END Blocco Vincoli --%>
	
	
	
							<!-- BEGIN Blocco Atti di Legge -->
							<div class="tab-pane active" id="attiDiLegge">
								<h3>Atti di Legge</h3>
	
								<s:include value="/jsp/attoDiLegge/stabilisciRelazioneAttoDiLeggeCapitolo.jsp" />
							</div>
							<!-- END Blocco Atti di Legge -->
						</div>
					</div>
				</div>
			</div>
		</div>
	
	<%-- Caricamento del footer --%>
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="${jspath}capitolo/ricercaSIOPE.js"></script>
	<script type="text/javascript" src="${jspath}capitolo/capitolo.js"></script>
	<script type="text/javascript" src="${jspath}capitolo/capitoloUscita.js"></script>
	<script type="text/javascript" src="${jspath}capitoloUscitaGestione/aggiorna.js"></script>
	<script type="text/javascript" src="${jspath}attoDiLegge/attoDiLegge.js"></script>
</body>
</html>