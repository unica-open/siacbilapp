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
			<div class="span12 ">

				<div class="step-content">
					<div class="step-pane active" id="step1">
						<s:include value="/jsp/include/messaggi.jsp" />
						<h3>
							Capitolo
							<s:property value="capitoloEntrataGestione.numeroCapitolo" /> / <s:property value="capitoloEntrataGestione.numeroArticolo" />
							<s:if test="gestioneUEB">
							/ <s:property value="capitoloEntrataGestione.numeroUEB" />
							</s:if>
						</h3>
						<ul id="tabs" class="nav nav-tabs" data-tabs="tabs" >
							<li class="active"><a href="#capitolo" data-toggle="tab">Capitolo</a></li>
							<li><a href="#attiDiLegge" data-toggle="tab">Atti di legge</a></li>
						</ul>
						<div id="my-tab-content" class="tab-content" >
							<!-- BEGIN Blocco Capitolo -->
							<div class="tab-pane active" id="capitolo">
								<form method="post" action="aggiornamentoCapEntrataGestione.do" id="aggiornamentoCapEntrataGestione" name="formAggiornamento" novalidate="novalidate">
									<h3>Aggiorna Capitolo</h3>
									<p>
										<small>I campi contrassegnati con l'asterisco (*) sono
											obbligatori.</small>
									</p>
									<s:hidden name="capitoloEntrataGestione.uid" id="uidCapitoloDaAggiornare" />
									<s:hidden name="capitoloEntrataGestione.statoOperativoElementoDiBilancio" />
									<s:hidden name="capitoloEntrataGestione.stato" />
									<s:hidden name="bilancio.uid" />
									<s:hidden name="capitoloEntrataGestione.annoCapitolo" />
									<s:hidden id="uidCapitoloDaAggiornare" value="%{uidDaAggiornare}" />
									<s:hidden name="daAggiornamento" />
	
									<fieldset class="form-horizontal">
	
										<div class="control-group">
											<label class="control-label" for="annoCapitolo">Anno</label>
											<div class="controls">
												<input type="text" id="annoCapitolo" class="lbTextSmall span2"
													value="${annoEsercizio}" disabled="disabled" maxlength="4" />
												<s:hidden name="bilancio.anno" value="%{annoEsercizio}" />
												<span class="al"> <label class="radio inline"
													for="numeroCapitolo">Capitolo *</label>
												</span> <input type="text" id="numeroCapitolo" class="lbTextSmall span2"
													maxlength="200" disabled="disabled"
													value="${capitoloEntrataGestione.numeroCapitolo}" />
												<s:hidden name="capitoloEntrataGestione.numeroCapitolo"
													value="%{capitoloEntrataGestione.numeroCapitolo}" />
												<span class="al"> <label class="radio inline" for="numeroArticolo">Articolo
														*</label>
												</span> <input type="text" id="numeroArticolo" class="lbTextSmall span2"
													maxlength="200" disabled="disabled"
													value="${capitoloEntrataGestione.numeroArticolo}" />
												<s:hidden name="capitoloEntrataGestione.numeroArticolo"
													value="%{capitoloEntrataGestione.numeroArticolo}" />
												<s:if test="gestioneUEB">
													<span class="al"> <label class="radio inline" for="numeroUEB">
															<abbr title="Unit&agrave; Elementare Bilancio">UEB</abbr>
													</label>
													</span> <input type="text" id="numeroUEB" class="lbTextSmall span2"
														maxlength="200" disabled="disabled"
														value="${capitoloEntrataGestione.numeroUEB}" />
												</s:if>
												<s:hidden name="capitoloEntrataGestione.numeroUEB"
													value="%{capitoloEntrataGestione.numeroUEB}" />
											</div>
										</div>
	
										<%-- DATI secondari --%>
										<div class="control-group">
											<label for="descrizioneCapitolo" class="control-label">Descrizione *</label>
											<div class="controls">
												<s:textarea id="descrizioneCapitolo" name="capitoloEntrataGestione.descrizione" cssClass="span10"
													required="true" maxlength="500" rows="5" cols="15" disabled="%{!descrizioneEditabile}"></s:textarea>
												<s:if test="!descrizioneEditabile">
													<s:hidden name="capitoloEntrataGestione.descrizione" />
												</s:if>
											</div>
										</div>
										<div class="control-group">
											<label for="descrizioneArticolo" class="control-label">Descrizione Articolo</label>
											<div class="controls">
												<s:textarea id="descrizioneArticolo" name="capitoloEntrataGestione.descrizioneArticolo" cssClass="span10"
													required="true" maxlength="500" rows="5" cols="15" disabled="%{!descrizioneArticoloEditabile}"></s:textarea>
												<s:if test="!descrizioneArticoloEditabile">
													<s:hidden name="capitoloEntrataGestione.descrizioneArticolo" />
												</s:if>
											</div>
										</div>
										<%-- Primo gruppo collegato --%>
										<div class="control-group">
										<!-- task-230 -->
											<label for="titoloEntrata" class="control-label">Titolo *</label>
											<div class="controls">
												<s:hidden id="HIDDEN_codiceTitolo" name="codiceTitolo" />
												<s:select list="listaTitoloEntrata" id="titoloEntrata" name="titoloEntrata.uid" required="true" cssClass="span10"
													headerKey="" headerValue="" listKey="uid" listValue="%{codice + '-' + descrizione}" disabled="%{!titoloEntrataEditabile}"
													data-original-uid="%{titoloEntrata.uid}" />
												<s:if test="!titoloEntrataEditabile">
													<s:hidden name="titoloEntrata.uid" />
												</s:if>
											</div>
										</div>
										<div class="control-group">
											<label for="tipologiaTitolo" class="control-label">Tipologia *
												<a class="tooltip-test" title="selezionare prima il Titolo" href="#">
													<i class="icon-info-sign">&nbsp;
														<span class="nascosto">selezionare prima il Titolo</span>
													</i>
												</a>
											</label>
											<div class="controls">
												<s:select list="listaTipologiaTitolo" id="tipologiaTitolo" cssClass="span10"
													name="tipologiaTitolo.uid" headerKey="" headerValue="" listKey="uid" listValue="%{codice + '-' + descrizione}"
													disabled="%{!tipologiaTitoloEditabile || titoloEntrata == null || titoloEntrata.uid == 0}"
													data-original-uid="%{tipologiaTitolo.uid}" />
												<s:if test="!tipologiaTitoloEditabile">
													<s:hidden name="tipologiaTitolo.uid" />
												</s:if>
											</div>
										</div>
										<div class="control-group">
											<label for="categoriaTipologiaTitolo" class="control-label">Categoria *
												<a class="tooltip-test" title="selezionare prima la Tipologia" href="#">
													<i class="icon-info-sign">&nbsp;
														<span class="nascosto">selezionare prima la Tipologia</span>
													</i>
												</a>
											</label>
											<div class="controls">
												<s:select list="listaCategoriaTipologiaTitolo" id="categoriaTipologiaTitolo" cssClass="span10"
													name="categoriaTipologiaTitolo.uid" headerKey="" headerValue="" listKey="uid" listValue="%{codice + '-' + descrizione}"
													disabled="%{!categoriaTipologiaTitoloEditabile || tipologiaTitolo == null || tipologiaTitolo.uid == 0}"
													data-original-uid="%{categoriaTipologiaTitolo.uid}" />
												<s:if test="!categoriaTipologiaTitoloEditabile">
													<s:hidden name="categoriaTipologiaTitolo.uid" />
												</s:if>
											</div>
										</div>
										<%-- zTree --%>
										<div class="control-group">
											<label for="bottonePdC" class="control-label">
												<abbr title="Piano dei Conti">P.d.C.</abbr> finanziario *
												<a class="tooltip-test" title="selezionare prima la Categoria" href="#">
													<i class="icon-info-sign">&nbsp;
														<span class="nascosto">selezionare prima la Categoria</span>
													</i>
												</a>
											</label>
											<div class="controls">
												<s:hidden id="HIDDEN_ElementoPianoDeiContiUid" name="elementoPianoDeiConti.uid"
													data-original-uid="%{elementoPianoDeiConti.uid}" />
												<s:hidden id="HIDDEN_ElementoPianoDeiContiStringa" name="pdcFinanziario" />
												<s:hidden id="HIDDEN_ElementoPianoDeiContiEditabile" name="elementoPianoDeiContiEditabile"/>
												<s:if test="elementoPianoDeiContiEditabile">
													<a href="#myModal" role="button" class="btn" data-toggle="modal" disabled="disabled" id="bottonePdC">
														Seleziona il Piano dei Conti &nbsp;
														<i class="icon-spin icon-refresh spinner" id="SPINNER_ElementoPianoDeiConti"></i>
													</a>
												</s:if>
												<span id="SPAN_ElementoPianoDeiConti">
													<s:if test='%{pdcFinanziario != null && pdcFinanziario neq ""}'>
														<s:property value="pdcFinanziario"/>
													</s:if><s:else>
														Nessun P.d.C. finanziario selezionato
													</s:else>
												</span>
											</div>
										</div>
										<%-- zTree --%>
										<div class="control-group">
											<label for="bottoneSIOPE" class="control-label">
												<abbr title="Sistema Informativo sulle OPerazioni degli Enti pubblici">SIOPE</abbr>
												<%--a class="tooltip-test" title="selezionare prima il P.d.C." href="#">
													<i class="icon-info-sign">&nbsp;<span class="nascosto">selezionare prima il P.d.C.</span></i>
												</a--%>
											</label>
											<%--div class="controls">
												<s:hidden id="HIDDEN_SIOPEUid" name="siopeEntrata.uid" data-original-uid="%{siopeEntrata != null ? siopeEntrata.uid : ''}" />
												<s:hidden id="HIDDEN_SIOPEStringa" name="siopeInserito" />
												<s:hidden id="HIDDEN_SIOPEEditabile" name="siopeEntrataEditabile"/>
												<s:if test="siopeEntrataEditabile">
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
												<s:hidden id="HIDDEN_SIOPEEditabile" name="siopeEntrataEditabile"/>
												<s:if test="siopeEntrataEditabile">
													<s:textfield id="siopeEntrata" name="siopeEntrata.codice" cssClass="span3" />
												</s:if><s:else>
													<s:property value="siopeEntrata.codice"/>
													<s:hidden name="siopeEntrata.codice" />
												</s:else>
												&nbsp;<span id="descrizioneSiopeEntrata"><s:property value="siopeEntrata.descrizione"/></span>
												<s:hidden id="HIDDEN_idSiopeEntrata" name="siopeEntrata.uid" />
												<s:hidden id="HIDDEN_descrizioneSiopeEntrata" name="siopeEntrata.descrizione" />
												<s:if test="siopeEntrataEditabile">
													<span class="radio guidata">
														<button type="button" class="btn btn-primary" id="compilazioneGuidataSIOPE">compilazione guidata</button>
													</span>
												</s:if>
											</div>
										</div>
										<%-- zTree --%>
										<div class="control-group">
											<label for="bottoneSAC" class="control-label"> Struttura
												Amministrativa Responsabile * </label>
											<div class="controls">
												<s:hidden id="HIDDEN_StrutturaAmministrativoContabileUid" name="strutturaAmministrativoContabile.uid"
													data-original-uid="%{strutturaAmministrativoContabile.uid}" />
												<s:hidden id="HIDDEN_StrutturaAmministrativoContabileStringa" name="strutturaAmministrativoResponsabile" />
												<s:hidden id="HIDDEN_StrutturaAmministrativoContabileDescrizione" name="strutturaAmministrativoContabile.descrizione" />
												<s:hidden id="HIDDEN_StrutturaAmministrativoContabileEditabile" name="strutturaAmministrativoContabileEditabile" />
												<s:if test="strutturaAmministrativoContabileEditabile">
													<a href="#struttAmm" role="button" class="btn" id="bottoneSAC" data-toggle="modal">
														Seleziona la Struttura amministrativa &nbsp;
														<i class="icon-spin icon-refresh spinner" id="SPINNER_StrutturaAmministrativoContabile"></i>
													</a>
													&nbsp;
												</s:if>
												<span id="SPAN_StrutturaAmministrativoContabile">
													<s:if test='%{strutturaAmministrativoResponsabile != null && strutturaAmministrativoResponsabile neq ""}'>
														<s:property value="strutturaAmministrativoResponsabile"/>
													</s:if><s:else>
														Nessuna Struttura Amministrativa Responsabile selezionata
													</s:else>
												</span>
											</div>
										</div>
										<div class="control-group">
											<label for="categoriaCapitolo" class="control-label">Tipo capitolo</label>
											<div class="controls">
												<select id="categoriaCapitolo" name="capitoloEntrataGestione.categoriaCapitolo.uid" class="span10" <s:if test="!categoriaCapitoloEditabile">disabled</s:if>>
													<option></option>
													<s:iterator value="listaCategoriaCapitolo" var="cc">
														<option data-codice="<s:property value="#cc.codice" />" value="<s:property value="#cc.uid"/>" <s:if test="%{capitoloEntrataGestione.categoriaCapitolo.uid == #cc.uid}">selected</s:if>>
															<s:property value="%{#cc.codice + ' - ' + #cc.descrizione}"/>
														</option>
													</s:iterator>
												</select>
												<s:if test="!categoriaCapitoloEditabile">
													<s:hidden name="capitoloEntrataGestione.categoriaCapitolo.uid" />
												</s:if>
											</div>
										</div>
										<div class="control-group">
											<label for="flagImpegnabile" class="control-label">Accertabile</label>
											<div class="controls">
												<s:checkbox id="flagImpegnabile" name="capitoloEntrataGestione.flagImpegnabile" disabled="%{!flagImpegnabileEditabile}" />
											</div>
											<s:if test="!flagImpegnabileEditabile">
												<s:hidden name="capitoloEntrataGestione.flagImpegnabile" />
											</s:if>
										</div>
										<!-- SIAC-7858 CM 19/05/2021 Inizio -->
										<div class="control-group">
											<label for="flagEntrataDubbiaEsigFCDE" class="control-label">Capitolo pertinente per il calcolo FCDE</label>
											<div class="controls">
												<span class="al">
													<label class="radio inline" >
														<input type="radio" value="true" name="flagEntrataDubbiaEsigFCDE" <s:if test='%{capitoloEntrataGestione.flagEntrataDubbiaEsigFCDE==true}'>checked="checked"</s:if>>Si
													</label>
												</span>
												<span class="al">
													<label class="radio inline" style="margin-left: 15px;">
														<input type="radio" value="false" name="flagEntrataDubbiaEsigFCDE" <s:if test='%{(capitoloEntrataGestione.flagEntrataDubbiaEsigFCDE==null)||(capitoloEntrataGestione.flagEntrataDubbiaEsigFCDE==false)}'>checked="checked"</s:if>>No
													</label>
												</span>
											</div>
										</div>
										<!-- SIAC-7858 CM 19/05/2021 Fine -->
										<div class="control-group <s:if test="!flagAccertatoPerCassaVisibile">hide</s:if>">
											<label for="flagAccertatoPerCassa" class="control-label">Accertato per cassa</label>
											<div class="controls">
												<s:checkbox id="flagAccertatoPerCassa" name="capitoloEntrataGestione.flagAccertatoPerCassa" disabled="!flagAccertatoPerCassaEditabile" data-editabile="%{flagAccertatoPerCassaEditabile}" />
												<s:if test="!flagAccertatoPerCassaEditabile">
													<s:hidden name="capitoloEntrataGestione.flagAccertatoPerCassa" />
												</s:if>
											</div>
										</div>
									</fieldset>
	
									<%-- Tabella 2 --%>
									<div class="accordion" id="accordion2">
										<div class="accordion-group">
											<div class="accordion-heading">
												<a class="accordion-toggle collapsed" data-toggle="collapse" id="ModalAltriDati" data-parent="#accordion2" href="#collapseOne">
													Altri dati<span class="icon">&nbsp;</span>
												</a>
											</div>
											<div id="collapseOne" class="accordion-body collapse">
												<div class="accordion-inner">
													<fieldset class="form-horizontal">
														<div class="control-group">
															<label class="control-label" for="exAnnoCapitolo">Ex Anno</label>
															<div class="controls">
																<s:textfield id="exAnnoCapitolo" cssClass="lbTextSmall span2" name="capitoloEntrataGestione.exAnnoCapitolo" maxlength="4" placeholder="%{placeholderAnnoExCapitolo}" disabled="!exAnnoEditabile" />
																<span class="al">
																	<label class="radio inline" for="exCapitolo">Ex
																		Capitolo</label>
																</span>
																<input type="text" id="exCapitolo" class="lbTextSmall span2"
																	name="capitoloEntrataGestione.exCapitolo" maxlength="200"
																	placeholder="capitolo"
																	value="${capitoloEntrataGestione.exCapitolo}" />
																<span class="al">
																	<label class="radio inline" for="exCapitolo">Ex
																		Articolo</label>
																</span>
																<input type="text" id="exCapitolo" class="lbTextSmall span2"
																	name="capitoloEntrataGestione.exArticolo" maxlength="200"
																	placeholder="articolo"
																	value="${capitoloEntrataGestione.exArticolo}" />
																<s:if test="gestioneUEB">
																	<span class="al">
																		<label class="radio inline" for="exUEB"> Ex
																			<abbr title="Unit&agrave; Elementare Bilancio">UEB</abbr>
																		</label>
																	</span>
																	<input type="text" id="exUEB" class="lbTextSmall span2"
																		name="capitoloEntrataGestione.exUEB" maxlength="200"
																		placeholder="UEB"
																		value="${capitoloEntrataGestione.exUEB}" />
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
																Rilevante IVA
															</label>
															<div class="controls">
																<s:checkbox id="rilevanteIva"
																		name="capitoloEntrataGestione.flagRilevanteIva" />
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
															<span class="control-label">Entrata</span>
															<div class="controls">
																<s:iterator value="listaRicorrenteEntrata" var="ricorrente" status="stat">
																	<label class="radio inline">
																		<input type="radio" name="ricorrenteEntrata.uid" value="<s:property value="%{#ricorrente.uid}" />" <s:if test="%{ricorrenteEntrata.uid == #ricorrente.uid}">checked="checked"</s:if>>&nbsp;<s:property value="%{#ricorrente.descrizione}" />
																	</label>
																</s:iterator>
															</div>
														</div>
														<div class="control-group">
															<label for="perimetroSanitario" class="control-label">Codifica identificativo del perimetro sanitario</label>
															<div class="controls input-append">
																<s:select list="listaPerimetroSanitarioEntrata" id="perimetroSanitario" cssClass="span10"
																		name="perimetroSanitarioEntrata.uid" headerKey="" headerValue=""
																		listKey="uid" listValue="%{codice + '-' + descrizione}" />
															</div>
														</div>
														<div class="control-group">
															<label for="transazioneUnioneEuropea" class="control-label">Codifica transazione UE</label>
															<div class="controls input-append">
																<s:select list="listaTransazioneUnioneEuropeaEntrata" id="transazioneUnioneEuropea" cssClass="span10"
																		name="transazioneUnioneEuropeaEntrata.uid" headerKey="" headerValue=""
																		listKey="uid" listValue="%{codice + '-' + descrizione}" />
															</div>
														</div>
														<%-- Classificatori Generici --%>
														<s:iterator var="idx" begin="36" end="%{lastIndexClassificatoriGenerici}">
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
																	</div>
																</div>
															</s:if>
														</s:iterator>
														<div class="control-group">
															<label for="note" class="control-label">Note</label>
															<div class="controls">
																<s:textarea id="note" name="capitoloEntrataGestione.note" rows="5" cols="15"></s:textarea>
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
									<table summary="riepilogo incarichi" class="table table-hover">
	
										<tr>
											<th width="25%">&nbsp;</th>
											<th width="15%" class="text-right">${annoEsercizioInt - 1}</th>
											<th width="25%" class="text-right">${annoEsercizioInt + 0}</th>
											<th width="25%" class="text-right">${annoEsercizioInt + 1}</th>
											<th width="25%" class="text-right">${annoEsercizioInt + 2}</th>
										</tr>
										<s:hidden name="importiCapitoloEntrataGestioneM1.diCuiAccertatoAnno1" />
										<s:hidden name="importiCapitoloEntrataGestione0.diCuiAccertatoAnno1" />
										<s:hidden name="importiCapitoloEntrataGestione0.diCuiAccertatoAnno2" />
										<s:hidden name="importiCapitoloEntrataGestione0.diCuiAccertatoAnno3" />
										<tr>
											<th>Competenza</th>
											<td class="text-right">
												<label for="stanziamentoM1" class="nascosto">inserisci importo</label>
												<s:hidden id="stanziamentoM1" name="importiCapitoloEntrataGestioneM1.stanziamento" />
												<s:property value="importiCapitoloEntrataGestioneM1.stanziamento" />
												<s:hidden  name="importiCapitoloEntrataGestioneM1.stanziamentoIniziale" />
											</td>
											<td class="text-right">
												<label for="stanziamento0" class="nascosto">inserisci importo</label>
												<s:hidden id="stanziamento0" name="importiCapitoloEntrataGestione0.stanziamento" />
												<s:property value="importiCapitoloEntrataGestione0.stanziamento"/>
												<s:hidden  name="importiCapitoloEntrataGestione0.stanziamentoIniziale" />
											</td>
											<td class="text-right">
												<label for="stanziamento1" class="nascosto">inserisci importo</label>
												<s:hidden id="stanziamento1" name="importiCapitoloEntrataGestione1.stanziamento" />
												<s:property value="importiCapitoloEntrataGestione1.stanziamento"/>
												<s:hidden  name="importiCapitoloEntrataGestione1.stanziamentoIniziale" />
											</td>
											<td class="text-right">
												<label for="stanziamento2" class="nascosto">inserisci importo</label>
												<s:hidden id="stanziamento2" name="importiCapitoloEntrataGestione2.stanziamento" />
												<s:property value="importiCapitoloEntrataGestione2.stanziamento"/>
												<s:hidden  name="importiCapitoloEntrataGestione2.stanziamentoIniziale" />
											</td>
										</tr>
										<tr>
											<th>Residuo</th>
											<td class="text-right">
												<label for="residuoM1" class="nascosto">inserisci importo</label>
												<s:hidden id="residuoM1" name="importiCapitoloEntrataGestioneM1.stanziamentoResiduo" />
												<s:property value="importiCapitoloEntrataGestioneM1.stanziamentoResiduo"/>
												<s:hidden value="importiCapitoloEntrataGestioneM1.stanziamentoResiduoIniziale"/>
											</td>
											<td class="text-right">
												<label for="residuo0" class="nascosto">inserisci importo</label>
												<s:hidden id="residuo0" name="importiCapitoloEntrataGestione0.stanziamentoResiduo" />
												<s:property value="importiCapitoloEntrataGestione0.stanziamentoResiduo"/>
												<s:hidden value="importiCapitoloEntrataGestione0.stanziamentoResiduoIniziale"/>
											</td>
											<td class="text-right">
												<label for="residuo1" class="nascosto">inserisci importo</label>
												<s:hidden id="residuo1" name="importiCapitoloEntrataGestione1.stanziamentoResiduo" />
												<s:property value="importiCapitoloEntrataGestione1.stanziamentoResiduo"/>
												<s:hidden value="importiCapitoloEntrataGestione1.stanziamentoResiduoIniziale"/>
											</td>
											<td class="text-right">
												<label for="residuo2" class="nascosto">inserisci importo</label>
												<s:hidden id="residuo2" name="importiCapitoloEntrataGestione2.stanziamentoResiduo" />
												<s:property value="importiCapitoloEntrataGestione2.stanziamentoResiduo"/>
												<s:hidden value="importiCapitoloEntrataGestione2.stanziamentoResiduoIniziale"/>
											</td>
										</tr>
										<tr>
											<th>Cassa</th>
											<td class="text-right">
													<label for="cassaM1" class="nascosto">inserisci importo</label>
													<s:hidden id="cassaM1" name="importiCapitoloEntrataGestioneM1.stanziamentoCassa" />
													<s:property value="importiCapitoloEntrataGestioneM1.stanziamentoCassa"/>
													<s:hidden value="importiCapitoloEntrataGestioneM1.stanziamentoCassaIniziale"/>
											</td>
											<td class="text-right">
													<label for="cassa0" class="nascosto">inserisci importo</label>
													<s:hidden id="cassa0" name="importiCapitoloEntrataGestione0.stanziamentoCassa" />
													<s:property value="importiCapitoloEntrataGestione0.stanziamentoCassa"/>
													<s:hidden value="importiCapitoloEntrataGestione0.stanziamentoCassaIniziale"/>
											</td>
											<td class="text-right">
													<label for="cassa1" class="nascosto">inserisci importo</label>
													<s:hidden id="cassa1" name="importiCapitoloEntrataGestione1.stanziamentoCassa" />
													<s:property value="importiCapitoloEntrataGestione1.stanziamentoCassa"/>
													<s:hidden value="importiCapitoloEntrataGestione1.stanziamentoCassaIniziale"/>
											</td>
											<td class="text-right">
													<label for="cassa2" class="nascosto">inserisci importo</label>
													<s:hidden id="cassa2" name="importiCapitoloEntrataGestione2.stanziamentoCassa" />
													<s:property value="importiCapitoloEntrataGestione2.stanziamentoCassa"/>
													<s:hidden value="importiCapitoloEntrataGestione2.stanziamentoCassaIniziale"/>
											</td>
										</tr>
									</table>
									<div class="spaceBottom">
										<div class="btn-group">
											<a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
												calcola<span class="caret"></span>
											</a>
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
										<button type="button" class="btn reset">annulla</button>
										<s:submit cssClass="btn btn-primary pull-right" value="salva"/>
									</p>
									<div id="myModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
										<div class="modal-header">
											<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
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
								</form>
								<s:include value="/jsp/cap/modaleCompilazioneGuidataSIOPE.jsp">
									<s:param name="ricercaUrl">ricercaClassificatoreGerarchico_siopeEntrata.do</s:param>
									<s:param name="ajaxUrl">risultatiRicercaSiopeEntrataAjax.do</s:param>
								</s:include>
							</div>
							<%-- BEGIN Blocco Vincoli -->
							<div class="tab-pane active" id="vincoli">
								<h3>Vincoli</h3>
							</div>
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
	</div>
	<%-- Caricamento del footer --%>
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/capitolo/ricercaSIOPE.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/capitolo/capitolo.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/capitolo/capitoloEntrata.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/capitoloEntrataGestione/aggiorna.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/attoDiLegge/attoDiLegge.js"></script>

</body>
</html>