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

	<%-- Pagina JSP vera e propria --%>
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12 ">

				<div class="step-content">
					<div class="step-pane active" id="step1">
					<s:include value="/jsp/include/messaggi.jsp" />
						<ul class="nav nav-tabs">
							<li class="active"><a href="#">Capitolo</a></li>
							<%-- <li class="disabled"><a href="#">Vincoli</a></li> --%>
							<li class="disabled"><a href="#">Atti di legge</a></li>
						</ul>
	
						<%-- TABELLA 1 - MASCHERA 1 --%>
						<%-- Sezione A --%>
						<form action="inserimentoCapUscitaGestione.do" method="post" id="inserimentoCapUscitaGestione" name="formInserimento" novalidate="novalidate">
							<s:hidden name="annoImporti" data-maintain="" />
							<div class="accordion" id="accordion3">
								<div class="accordion-group">
									<div class="accordion-heading">
										<a class="accordion-toggle collapsed" data-toggle="collapse"
												data-parent="#accordion3" href="#collapseTwo">
											Copia i dati da <span class="icon">&nbsp;</span>
										</a>
									</div>
	
									<%-- COPIA dei dati --%>
									<div id="collapseTwo" class="accordion-body collapse">
										<div class="accordion-inner">
											<div class="form-inline">
												<label class="radio inline">Gestione <input type="radio" name="tipoCapitoloCopia" value="CAPITOLO_USCITA_GESTIONE" <s:if test="'CAPITOLO_USCITA_GESTIONE'.equals(tipoCapitoloCopia.name())">checked</s:if> /></label>
												&nbsp;&nbsp;
												<label class="radio inline">Previsione <input type="radio" name="tipoCapitoloCopia" value="CAPITOLO_USCITA_PREVISIONE" <s:if test="'CAPITOLO_USCITA_PREVISIONE'.equals(tipoCapitoloCopia.name())">checked</s:if> /></label>
											</div>
											<div class="form-inline">
												<label for="annoCapitoloDaCopiare" class="control-label">Anno *</label>&nbsp;
												<input type="text" id="annoCapitoloDaCopiare" class="input-small" disabled="disabled"
														value="${AnnoEsercizioInt}" data-maintain="" />
												<s:hidden name="bilancioDaCopiare.anno" value="%{annoEsercizioInt}" data-maintain="" />
												<label for="numeroCapitoloDaCopiare" class="control-label">Capitolo</label>&nbsp;
												<input type="text" id="numeroCapitoloDaCopiare" name="capitoloDaCopiare.numeroCapitolo"
														class="input-small soloNumeri" value="${capitoloDaCopiare.numeroCapitolo}"
														placeholder="capitolo" required="required" maxlength="9" />
												<label for="numeroArticoloDaCopiare" class="control-label">Articolo *</label>&nbsp;
												<input type="text" id="numeroArticoloDaCopiare" name="capitoloDaCopiare.numeroArticolo"
														class="input-small soloNumeri" value="${capitoloDaCopiare.numeroArticolo}"
														placeholder="articolo" required="required" maxlength="9" />
												<s:if test="gestioneUEB">
													<label for="numeroUEBDaCopiare" class="control-label">
														<abbr title="Unit&agrave; Elementare Bilancio">UEB</abbr>
													</label>&nbsp;
													<input type="text" id="numeroUEBDaCopiare" name="capitoloDaCopiare.numeroUEB"  class="input-small soloNumeri"
															value="${capitoloDaCopiare.numeroUEB}" placeholder="UEB" required="required" maxlength="9" />
												</s:if><s:else>
													<s:hidden name="capitoloDaCopiare.numeroUEB" data-maintain="" />
												</s:else>
												<input type="button" class="btn" value="copia" id="buttonCopia" data-url="copiaCapUscitaGestione.do" />
											</div>
										</div>
									</div>
								</div>
							</div>
	
							<%-- Sezione B --%>
							<%-- Vedere di aggiungere un <div> nel <h3> per l'inserimento di capitolo/articolo ... (cfr. CDU) --%>
							<h3>Inserimento Capitolo
								<s:if test="%{(capitoloUscitaGestione.numeroCapitolo != null) &&
										(capitoloUscitaGestione.numeroCapitolo != 0)}">
								<s:property value="capitoloUscitaGestione.numeroCapitolo"/>
								/
								<s:property value="capitoloUscitaGestione.numeroArticolo"/>
									<s:if test="gestioneUEB">
									/
									<s:property value="capitoloUscitaGestione.numeroUEB"/>
								</s:if>
							</s:if>
							</h3>
							<fieldset class="form-horizontal">
								<div class="control-group">
									<%-- DATI iniziali + RICERCA del Capitolo di Uscita Gestione --%>
									<label class="control-label" for="annoCapitolo">Anno</label>
									<div class="controls">
										<input type="text" id="annoCapitolo" class="lbTextSmall span2" disabled="disabled"
												value="${AnnoEsercizioInt}" maxlength="4" data-maintain="" />
										<s:hidden name="bilancio.anno" value="%{annoEsercizioInt}" id="annoBilancio" data-maintain="" />
										<span class="al">
											<label class="radio inline" for="numeroCapitolo">Capitolo *</label>
										</span>
										<input type="text" id="numeroCapitolo" class="lbTextSmall span2 soloNumeri" required="required"
												name="capitoloUscitaGestione.numeroCapitolo" maxlength="9"
												placeholder="capitolo" value="${capitoloUscitaGestione.numeroCapitolo}" />
										<span class="al">
											<label class="radio inline" for="numeroArticolo">Articolo *</label>
										</span>
										<input type="text" id="numeroArticolo" class="lbTextSmall span2 soloNumeri" required="required"
												name="capitoloUscitaGestione.numeroArticolo" maxlength="9"
												placeholder="articolo" value="${capitoloUscitaGestione.numeroArticolo}" />
										<s:if test="gestioneUEB">
											<span class="al">
												<label class="radio inline" for="numeroUEB">
													<abbr title="Unit&agrave; Elementare Bilancio">UEB</abbr> *
												</label>
											</span>
											<input type="text" id="numeroUEB" class="lbTextSmall span2 soloNumeri" name="capitoloUscitaGestione.numeroUEB"
													maxlength="9" value="${capitoloUscitaGestione.numeroUEB}" placeholder="UEB" required="required" />
										</s:if><s:else>
											<s:hidden name="capitoloUscitaGestione.numeroUEB" data-maintain="" />
										</s:else>
										<button class="btn btn-primary" title="Verifica esistenza capitolo"
												name="action:cercaCapUscitaGestione">
											<i class="icon-search icon"></i> cerca
										</button>
									</div>
								</div>
	
								<%-- DATI secondari --%>
								<div class="control-group">
									<label for="descrizioneCapitolo" class="control-label">Descrizione *</label>
									<div class="controls">
										<s:textarea rows="5" cols="15" id="descrizioneCapitolo" name="capitoloUscitaGestione.descrizione" cssClass="span10"
												required="required" maxlength="500"></s:textarea>
									</div>
								</div>
								<div class="control-group">
									<label for="descrizioneArticolo" class="control-label">Descrizione Articolo</label>
									<div class="controls">
										<s:textarea rows="5" cols="15" id="descrizioneArticolo" name="capitoloUscitaGestione.descrizioneArticolo" cssClass="span10"
												required="required" maxlength="500"></s:textarea>
									</div>
								</div>
								<%-- Primo gruppo collegato --%>
								<div class="control-group">
									<label for="missione" class="control-label">Missione *</label>
									<div class="controls">
										<select id="missione" name="missione.uid" required class="span10">
												<option value="0"></option>
												<s:iterator value="listaMissione" var="mm">
													<option data-codice="<s:property value="#mm.codice" />" value="<s:property value="#mm.uid"/>" <s:if test="%{missione.uid == #mm.uid}">selected</s:if>>
														<s:property value="%{#mm.codice + ' - ' + #mm.descrizione}"/>
													</option>
												</s:iterator>
										</select>
									</div>
								</div>
								<!-- task-55 -->
								<div id="containerFlagCapitoloDaNonInserireA1" <s:if test='%{!missioneFondi}'>class="hide"</s:if>>
									<div class="control-group">
										<label for="flagCapitoloDaNonInserireA1" class="control-label">Capitolo da non inserire nell'allegato A1 *
											<!-- task-55 -->
											<a class="tooltip-test" title="Risorse Accantonate per Risultato di Amministrazione - Allegato a1" href="#">
												<i class="icon-info-sign">&nbsp;
													<span class="nascosto">Risorse Accantonate per Risultato di Amministrazione - Allegato a1</span>
												</i>
											</a>
										</label>
										<div class="controls">
											<label class="radio inline">
												<s:radio theme="simple" name="capitoloUscitaGestione.flagNonInserireAllegatoA1" list="#{true:'Sì'}" value="null"/>
											</label>
											<label class="radio inline">
												<s:radio theme="simple" name="capitoloUscitaGestione.flagNonInserireAllegatoA1" list="#{false:'No'}" value="null"/>
											</label>
										</div>
									</div>
								</div>
								
								<div class="control-group">
									<label for="programma" class="control-label">Programma *
										<a class="tooltip-test" title="selezionare prima la Missione" href="#">
											<i class="icon-info-sign">&nbsp;
												<span class="nascosto">selezionare prima la Missione</span>
											</i>
										</a>
									</label>
									<div class="controls">
										<s:select id="programma" list="listaProgramma" name="programma.uid" cssClass="span10"
											required="required" headerKey="0" headerValue="" listKey="uid" disabled='%{missione == null || missione.uid == 0}'
											listValue="%{codice + '-' + descrizione}" />
									</div>
								</div>
								<div class="control-group">
								<!-- task-9 obbligatorio cofog -->
									<label for="classificazioneCofog" class="control-label">Cofog *
										<a class="tooltip-test" title="selezionare prima il Programma" href="#">
											<i class="icon-info-sign">&nbsp;
												<span class="nascosto">selezionare prima il Programma</span>
											</i>
										</a>
									</label>
									<div class="controls">
										<s:select id="classificazioneCofog" list="listaClassificazioneCofog" name="classificazioneCofog.uid"
											cssClass="span10" headerKey="0" headerValue="" listKey="uid" disabled="%{programma == null || programma.uid == 0}"
											listValue="%{codice + '-' + descrizione}" />
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
										<s:select list="listaTitoloSpesa" name="titoloSpesa.uid" id="titoloSpesa" cssClass="span10"
											required="required" disabled="%{titoloSpesa == null || titoloSpesa.uid == 0}"
											listKey="uid" listValue="%{codice + ' - ' + descrizione}" headerKey="0" headerValue="" />
									</div>
								</div>
								<div class="control-group">
									<label for="macroaggregato" class="control-label">Macroaggregato *
										<a class="tooltip-test" title="selezionare prima il Titolo" href="#">
											<i class="icon-info-sign">&nbsp;
												<span class="nascosto">selezionare prima il Titolo</span>
											</i>
										</a>
									</label>
									<div class="controls">
										<s:select id="macroaggregato" list="listaMacroaggregato" name="macroaggregato.uid" cssClass="span10"
											required="required" headerKey="0" headerValue="" listKey="uid" disabled="%{titoloSpesa == null || titoloSpesa.uid == 0}"
											listValue="%{codice + '-' + descrizione}" />
									</div>
								</div>
								<%-- zTree --%>
								<div class="control-group">
									<label for="bottonePdC" class="control-label">
										<abbr title="Piano dei Conti">P.d.C.</abbr> finanziario *
										<a class="tooltip-test" title="selezionare prima il Macroaggregato" href="#">
											<i class="icon-info-sign">&nbsp;
												<span class="nascosto">selezionare prima il Macroaggregato</span>
											</i>
										</a>
									</label>
									<div class="controls">
										<input type="hidden" id="HIDDEN_ElementoPianoDeiContiUid" name="elementoPianoDeiConti.uid" value="${elementoPianoDeiConti.uid}" />
										<s:hidden id="HIDDEN_ElementoPianoDeiContiStringa" name="pdcFinanziario" />
										<a href="#myModal" role="button" class="btn" data-toggle="modal" disabled="disabled" id="bottonePdC">
											Seleziona il Piano dei Conti &nbsp;
											<i class="icon-spin icon-refresh spinner" id="SPINNER_ElementoPianoDeiConti"></i>
										</a>
										&nbsp;
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
										<s:hidden id="HIDDEN_SIOPEUid" name="siopeSpesa.uid" data-original-uid="%{siopeSpesa != null ? siopeSpesa.uid : ''}" />
										<s:hidden id="HIDDEN_SIOPEStringa" name="siopeInserito" />
										<s:hidden id="HIDDEN_SIOPEEditabile" name="siopeSpesaEditabile"/>
										<a href="#modaleSIOPE" role="button" class="btn" data-toggle="modal" id="bottoneSIOPE" disabled="disabled" >
											Seleziona il codice SIOPE
											<i class="icon-spin icon-refresh spinner" id="SPINNER_SIOPE"></i>
										</a>
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
										<s:textfield id="siopeSpesa" name="siopeSpesa.codice" cssClass="span3" />
										&nbsp;<span id="descrizioneSiopeSpesa"><s:property value="siopeSpesa.descrizione"/></span>
										<s:hidden id="HIDDEN_idSiopeSpesa" name="siopeSpesa.uid" />
										<s:hidden id="HIDDEN_descrizioneSiopeSpesa" name="siopeSpesa.descrizione" />
										<span class="radio guidata">
											<button type="button" class="btn btn-primary" id="compilazioneGuidataSIOPE">compilazione guidata</button>
										</span>
									</div>
								</div>
								<%-- zTree --%>
								<div class="control-group">
									<label for="bottoneSAC" class="control-label">
										Struttura Amministrativa Responsabile *
									</label>
									<div class="controls">
										<input type="hidden" id="HIDDEN_StrutturaAmministrativoContabileUid" name="strutturaAmministrativoContabile.uid" value="${strutturaAmministrativoContabile.uid}" />
										<s:hidden id="HIDDEN_StrutturaAmministrativoContabileStringa" name="strutturaAmministrativoResponsabile" />
										<a href="#struttAmm" role="button" class="btn" id="bottoneSAC" data-toggle="modal">
											Seleziona la Struttura amministrativa &nbsp;
											<i class="icon-spin icon-refresh spinner" id="SPINNER_StrutturaAmministrativoContabile"></i>
										</a>
										&nbsp;
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
										<select id="categoriaCapitolo" name="capitoloUscitaGestione.categoriaCapitolo.uid" class="span10">
											<option value="0"></option>
											<s:iterator value="listaCategoriaCapitolo" var="cc">
												<option data-codice="<s:property value="#cc.codice" />" value="<s:property value="#cc.uid"/>" <s:if test="%{capitoloUscitaGestione.categoriaCapitolo.uid == #cc.uid}">selected</s:if>>
													<s:property value="%{#cc.codice + ' - ' + #cc.descrizione}"/>
												</option>
											</s:iterator>
										</select>
									</div>
								</div>
								<div id="containerRisorsaAccantonata" <s:if test='%{!missioneFondi}'>class="hide"</s:if>>
									<div class="control-group">
										<label for="risorsaAccantonata" class="control-label">Risorsa accantonata * </label>
										<div class="controls">
											<select id="risorsaAccantonata" name="risorsaAccantonata.uid"  <s:if test='%{!missioneFondi}'>disabled</s:if> class="span10">
												<option value="0" <s:if test="%{risorsaAccantonata == null || risorsaAccantonata.uid == 0}">selected</s:if> ></option>
												<s:iterator value="listaRisorsaAccantonata" var="dd">
													<option data-codice="<s:property value="#dd.codice" />" value="<s:property value="#dd.uid"/>" <s:if test="%{risorsaAccantonata != null && risorsaAccantonata.uid == #dd.uid}">selected</s:if>>
														<s:property value="%{#dd.codice + ' - ' + #dd.descrizione}"/>
													</option>
												</s:iterator>
											</select>
										</div>
									</div>
								</div>
								<div class="control-group">
									<label for="flagImpegnabile" class="control-label">Impegnabile</label>
									<div class="controls">
										<s:checkbox id="flagImpegnabile" disabled='%{missioneFondi}' name="capitoloUscitaGestione.flagImpegnabile" />
									</div>
								</div>
							</fieldset>
	
							<%-- TABELLA 2 --%>
							<%-- Sezione G --%>
							<%-- ALTRI dati --%>
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
													<label for="tipoFinanziamento" class="control-label">Tipo Finanziamento</label>
													<div class="controls input-append">
														<s:select id="tipoFinanziamento" name="tipoFinanziamento.uid" list="listaTipoFinanziamento" cssClass="span10"
															headerKey="0" headerValue="" listKey="uid" listValue="%{codice + '-' + descrizione}" />
													</div>
												</div>
												<div class="control-group">
													<label for="rilevanteIva" class="control-label">
														Rilevante IVA
													</label>
													<div class="controls">
														<s:checkbox id="rilevanteIva" name="capitoloUscitaGestione.flagRilevanteIva" />
													</div>
												</div>
												<div class="control-group">
													<label for="delegate" class="control-label">
														Funzioni delegate dalla Regione
													</label>
													<div class="controls">
														<s:checkbox id="delegate" name="capitoloUscitaGestione.funzDelegateRegione" />
													</div>
												</div>
												<div class="control-group">
													<label for="tipoFondo" class="control-label">Tipo fondo</label>
													<div class="controls input-append">
														<s:select id="tipoFondo" name="tipoFondo.uid" list="listaTipoFondo" cssClass="span10"
															headerKey="0" headerValue="" listKey="uid" listValue="%{codice + '-' + descrizione}" />
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
																name="perimetroSanitarioSpesa.uid" headerKey="0" headerValue="" listKey="uid" listValue="%{codice + '-' + descrizione}" />
													</div>
												</div>
												<div class="control-group">
													<label for="transazioneUnioneEuropea" class="control-label">Codifica transazione UE</label>
													<div class="controls input-append">
														<s:select list="listaTransazioneUnioneEuropeaSpesa" id="transazioneUnioneEuropea" cssClass="span10"
																name="transazioneUnioneEuropeaSpesa.uid" headerKey="0" headerValue="" listKey="uid" listValue="%{codice + '-' + descrizione}" />
													</div>
												</div>
												<div class="control-group">
													<label for="politicheRegionaliUnitarie" class="control-label">Codifica politiche regionali unitarie</label>
													<div class="controls input-append">
														<s:select list="listaPoliticheRegionaliUnitarie" id="politicheRegionaliUnitarie" cssClass="span10"
																name="politicheRegionaliUnitarie.uid" headerKey="0" headerValue="" listKey="uid" listValue="%{codice + '-' + descrizione}" />
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
																	cssClass="span10" name="%{'classificatoreGenerico' + #idx + '.uid'}" headerKey="0" headerValue=""
																	listKey="uid" listValue="%{codice + '-' + descrizione}" />
															</div>
														</div>
													</s:if>
												</s:iterator>
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
	
							<%-- MASCHERA 1 --%>
							<%-- Sezione C-D-E --%>
							<h4>Stanziamenti</h4>
							<table summary="riepilogo incarichi" class="table table-hover table-bordered">
								<tr>
									<th width="20%">&nbsp;</th>
									<th width="27%" class="text-right"><s:property value="%{annoEsercizioInt + 0}"/></th>
									<th width="27%" class="text-right"><s:property value="%{annoEsercizioInt + 1}"/></th>
									<th width="26%" class="text-right"><s:property value="%{annoEsercizioInt + 2}"/></th>
								</tr>
	
								<tr>
									<th>Competenza</th>
									<td class="text-right">
										<label for="stanziamento0" class="nascosto">inserisci importo</label>
										<s:property value="importiCapitoloUscitaGestione0.stanziamento"/>
										<s:hidden name="importiCapitoloUscitaGestione0.stanziamento" data-maintain="" />
									</td>
									<td class="text-right">
										<label for="stanziamento1" class="nascosto">inserisci importo</label>
										<s:property value="importiCapitoloUscitaGestione1.stanziamento"/>
										<s:hidden name="importiCapitoloUscitaGestione1.stanziamento" data-maintain="" />
									</td>
									<td class="text-right">
										<label for="stanziamento2" class="nascosto">inserisci importo</label>
										<s:property value="importiCapitoloUscitaGestione2.stanziamento"/>
										<s:hidden name="importiCapitoloUscitaGestione2.stanziamento" data-maintain="" />
									</td>
								</tr>
								<tr>
									<th>Residuo</th>
									<td class="text-right">
										<label for="residuo0" class="nascosto">inserisci importo</label>
										<s:property value="importiCapitoloUscitaGestione0.stanziamentoResiduo"/>
										<s:hidden name="importiCapitoloUscitaGestione0.stanziamentoResiduo" data-maintain="" />
									</td>
									<td class="text-right">
										<label for="residuo1" class="nascosto">inserisci importo</label>
										&nbsp;
										<s:hidden name="importiCapitoloUscitaGestione1.stanziamentoResiduo" data-maintain="" />
									</td>
									<td class="text-right">
										<label for="residuo2" class="nascosto">inserisci importo</label>
										&nbsp;
										<s:hidden name="importiCapitoloUscitaGestione2.stanziamentoResiduo" data-maintain="" />
									</td>
								</tr>
	
								<tr>
									<th>Cassa</th>
									<td class="text-right">
										<label for="cassa0" class="nascosto">inserisci importo</label>
										<s:property value="importiCapitoloUscitaGestione0.stanziamentoCassa"/>
										<s:hidden name="importiCapitoloUscitaGestione0.stanziamentoCassa" data-maintain="" />
									</td>
									<td class="text-right">
										<label for="cassa1" class="nascosto">inserisci importo</label>
										&nbsp;
										<s:hidden name="importiCapitoloUscitaGestione1.stanziamentoCassa" data-maintain="" />
									</td>
									<td class="text-right">
										<label for="cassa2" class="nascosto">inserisci importo</label>
										&nbsp;
										<s:hidden name="importiCapitoloUscitaGestione2.stanziamentoCassa" data-maintain="" />
									</td>
								</tr>
							</table>
							<p>
								<s:if test="daVariazione">
									<s:a cssClass="btn" href="/siacbilapp/aggiornamentoVariazioneImporti.do" id="pulsanteRedirezioneIndietro">indietro</s:a>
								</s:if><s:else>
									<s:include value="/jsp/include/indietro.jsp" />
								</s:else>
								<s:a action="annullaInserisciCapUscitaGestione" cssClass="btn btn-secondary">annulla</s:a>
								<s:submit cssClass="btn btn-primary pull-right" value="salva"/>
							</p>
							<s:hidden name ="daVariazione"/>
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
						<s:include value="/jsp/cap/modaleCompilazioneGuidataSIOPE.jsp">
							<s:param name="ricercaUrl">ricercaClassificatoreGerarchico_siopeSpesa.do</s:param>
							<s:param name="ajaxUrl">risultatiRicercaSiopeSpesaAjax.do</s:param>
						</s:include>
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
	<script type="text/javascript" src="/siacbilapp/js/local/capitolo/capitoloUscita.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/capitoloUscitaGestione/inserisci.js"></script>
</body>
</html>