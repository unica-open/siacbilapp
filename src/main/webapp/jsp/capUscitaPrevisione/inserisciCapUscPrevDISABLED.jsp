<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/struts-json-tags" prefix="json" %>
<%@ taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

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

						<h3>Capitolo <s:property value="capitoloUscitaPrevisione.numeroCapitolo"/>/<s:property value="capitoloUscitaPrevisione.numeroArticolo"/><s:if test="gestioneUEB">/<s:property value="capitoloUscitaPrevisione.numeroUEB"/></s:if></h3>
						<ul id="tabs" class="nav nav-tabs" data-tabs="tabs" >
							<li class="active"><a href="#capitolo" data-toggle="tab">Capitolo</a></li>
							<%-- <li><a href="#vincoli" data-toggle="tab">Vincoli</a></li> --%>
							<li><a href="#attiDiLegge" data-toggle="tab">Atti di legge</a></li>
						</ul>

						<div id="my-tab-content" class="tab-content" >
							<!-- BEGIN Blocco Capitolo -->

							<div class="tab-pane active" id="capitolo">
								<form action="redirezioneAggiornamentoCapUscitaPrevisione.do" method="post" id="aggiornamentoCapUscitaPrevisione" name="formInserimento" data-disabled-form="true">
									<%-- Uid del capitolo inserito --%>
									<s:hidden value="%{capitoloUscitaPrevisione.uid}" name="uidDaAggiornare" id="uidCapitoloDaAggiornare" />
									<s:hidden name="daAggiornamento" />
									<fieldset class="form-horizontal">
										<div class="control-group">
											<label class="control-label" for="annoda2">Anno</label>
											<div class="controls">
												<input type="text" id="annoda2" class="lbTextSmall span2" disabled="disabled" value="${AnnoEsercizioInt}" maxlength="4" />
												<s:hidden name="bilancio.anno" value="%{annoEsercizioInt}" />
												<s:hidden name="bilancio.uid" />
												<span class="al">
													<label class="radio inline" for="annoa">Capitolo *</label>
												</span>
												<s:textfield id="annoa" cssClass="lbTextSmall span2" maxlength="200" disabled="true" name="capitoloUscitaPrevisione.numeroCapitolo" />
												<span class="al">
													<label class="radio inline" for="art">Articolo *</label>
												</span>
												<s:textfield id="art" cssClass="lbTextSmall span2" maxlength="200" disabled="true" name="capitoloUscitaPrevisione.numeroArticolo" />
												<s:if test="gestioneUEB">
													<span class="al">
														<label class="radio inline" for="ueb2">
															<abbr title="Unit&agrave; Elementare Bilancio">UEB</abbr>
														</label>
													</span>
													<s:textfield id="ueb" cssClass="lbTextSmall span2" maxlength="200" disabled="true" name="capitoloUscitaPrevisione.numeroUEB" />
												</s:if>
											</div>
										</div>

										<div class="control-group">
											<label for="titolo_adv" class="control-label">Descrizione *</label>
											<div class="controls">
												<s:textarea rows="5" cols="15" id="titolo_adv" disabled="true" cssClass="span10" name="capitoloUscitaPrevisione.descrizione" />
											</div>
										</div>
										<div class="control-group">
											<label for="descrart" class="control-label">Descrizione Articolo</label>
											<div class="controls">
												<s:textarea rows="5" cols="15" id="titolo_adv" disabled="true" cssClass="span10" name="capitoloUscitaPrevisione.descrizioneArticolo" />
											</div>
										</div>
										<div class="control-group">
											<label for="nomeautore" class="control-label">Missione *</label>
											<div class="controls">
												<s:select list="listaMissione" id="nomeautore" name="missione.uid" cssClass="span10" disabled="true"
													headerKey="0" headerValue="" listKey="uid" listValue="%{codice + '-' + descrizione}" />
											</div>
										</div>
										<!-- task-55 -->
										<div id="containerFlagCapitoloDaNonInserireA1" <s:if test='%{!missioneFondi}'>class="hide"</s:if>>
											<div class="control-group" id="capitoloDaNonInserire">
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
														<s:radio theme="simple" name="capitoloUscitaPrevisione.flagNonInserireAllegatoA1" list="#{true:'Sì'}" disabled="true"/>
													</label>
													<label class="radio inline"> 
														<s:radio theme="simple" name="capitoloUscitaPrevisione.flagNonInserireAllegatoA1" list="#{false:'No'}" disabled="true"/>
													</label>
												</div>
											</div>
										</div>
										<div class="control-group">
											<label for="soggetto5" class="control-label">Programma *
												<a class="tooltip-test" title="selezionare prima la Missione" href="#">
													<i class="icon-info-sign">&nbsp;
														<span class="nascosto">selezionare prima la Missione</span>
													</i>
												</a>
											</label>
											<div class="controls">
												<s:select list="listaProgramma" id="soggetto5" name="programma.uid" cssClass="span10" disabled="true"
													headerKey="0" headerValue="" listKey="uid" listValue="%{codice + '-' + descrizione}" />
											</div>
										</div>
										<div class="control-group">
											<label for="Categoria" class="control-label">Cofog
												<a class="tooltip-test" title="selezionare prima il Programma" href="#">
													<i class="icon-info-sign">&nbsp;
														<span class="nascosto">selezionare prima il Programma</span>
													</i>
												</a>
											</label>
											<div class="controls">
												<s:select list="listaClassificazioneCofog" name="classificazioneCofog.uid" id="Categoria" cssClass="span10" disabled="true"
													headerKey="0" headerValue="" listKey="uid" listValue="%{codice + '-' + descrizione}" />
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
												<s:select list="listaTitoloSpesa" name="titoloSpesa.uid" id="Titolo" cssClass="span10" disabled="true"
													headerKey="0" headerValue="" listKey="uid" listValue="%{codice + '-' + descrizione}" />
											</div>
										</div>
										<div class="control-group">
											<label for="Macroaggregato" class="control-label">Macroaggregato *
												<a class="tooltip-test" title="selezionare prima il Titolo" href="#">
													<i class="icon-info-sign">&nbsp;
														<span class="nascosto">selezionare prima il Titolo</span>
													</i>
												</a>
											</label>
											<div class="controls">
												<s:select list="listaMacroaggregato" name="macroaggregato.uid" id="Macroaggregato" cssClass="span10" disabled="true"
													headerKey="0" headerValue="" listKey="uid" listValue="%{codice + '-' + descrizione}" />
											</div>
										</div>
										<%-- zTree --%>
										<div class="control-group">
											<label for="pdc" class="control-label">
												<abbr title="Piano dei Conti">P.d.C.</abbr> finanziario *
												<a class="tooltip-test" title="selezionare prima il Macroaggregato" href="#">
													<i class="icon-info-sign">&nbsp;
														<span class="nascosto">selezionare prima il Macroaggregato</span>
													</i>
												</a>
											</label>
											<div class="controls">
												<s:property value="pdcFinanziario"/>
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
											<div class="controls">
												<s:property value="siopeInserito"/>
											</div>
										</div>
										<%-- zTree --%>
										<div class="control-group">
											<label for="finanz2" class="control-label">
												Struttura Amministrativa Responsabile *
											</label>
											<div class="controls">
												<s:property value="strutturaAmministrativoResponsabile"/>
											</div>
										</div>
										<div class="control-group">
											<label for="categoriaCapitolo" class="control-label">Tipo capitolo</label>
											<div class="controls">
												<s:select id="categoriaCapitolo" listKey="uid" list="listaCategoriaCapitolo" name="capitoloUscitaPrevisione.categoriaCapitolo.uid" cssClass="span10"
													listValue="%{codice + '-' + descrizione}" disabled="true" headerKey="0" headerValue="" />
											</div>
										</div>
										
										<div id="containerRisorsaAccantonata" <s:if test='%{!missioneFondi}'>class="hide"</s:if>>
											<div class="control-group">
												<label for="risorsaAccantonata" class="control-label">Risorsa accantonata * </label>
												<div class="controls">
													<select id="risorsaAccantonata" name="risorsaAccantonata.uid"  disabled class="span10">
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
												<s:checkbox id="flagImpegnabile" name="capitoloUscitaPrevisione.flagImpegnabile" disabled="true" data-editabile="false" />
											</div>
										</div>
									</fieldset>

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

														<div class="control-group"> <%-- Non presente in inserimento ma in aggiornamento --%>
															<label for="memoria" class="control-label">Corsivo per memoria</label>
															<div class="controls">
																<s:checkbox id="memoria" disabled="true"
																	name="capitoloUscitaPrevisione.flagPerMemoria" />
															</div>
														</div>

														<div class="control-group">
															<label for="finanz" class="control-label">Tipo Finanziamento</label>
															<div class="controls input-append">
																<s:select list="listaTipoFinanziamento" id="tipoFinanziamento" cssClass="span10"
																		name="tipoFinanziamento.uid" headerKey="0" headerValue="" disabled="true"
																		listKey="uid" listValue="%{codice + '-' + descrizione}" />
															</div>
														</div>
														<div class="control-group">
															<label for="Ricorrente" class="control-label">
																Rilevante IVA
															</label>
															<div class="controls">
																<s:checkbox id="Ricorrente" disabled="true"
																		name="capitoloUscitaPrevisione.flagRilevanteIva"
																		value="%{capitoloUscitaPrevisione.flagRilevanteIva}" />
															</div>
														</div>
														<div class="control-group">
															<label for="delegate" class="control-label">
																Funzioni delegate dalla Regione
															</label>
															<div class="controls">
																<s:checkbox id="delegate" disabled="true"
																		name="capitoloUscitaPrevisione.funzDelegateRegione"
																		value="%{capitoloUscitaPrevisione.funzDelegateRegione}" />
															</div>
														</div>
														<div class="control-group">
															<label for="tipoFondo" class="control-label">Tipo fondo</label>
															<div class="controls input-append">
																<s:select list="listaTipoFondo" id="tipoFondo" cssClass="span10"
																		name="tipoFondo.uid" headerKey="0" headerValue="" disabled="true"
																		listKey="uid" listValue="%{codice + '-' + descrizione}" />
															</div>
														</div>
														<div class="control-group">
															<span class="control-label">Spesa</span>
															<div class="controls">
																<s:iterator value="listaRicorrenteSpesa" var="ricorrente" status="stat">
																	<label class="radio inline">
																		<input type="radio" name="ricorrenteSpesa.uid" value="<s:property value="%{#ricorrente.uid}" />" <s:if test="%{ricorrenteSpesa.uid == #ricorrente.uid}">checked="checked"</s:if> disabled="disabled">&nbsp;<s:property value="%{#ricorrente.descrizione}" />
																	</label>
																</s:iterator>
															</div>
														</div>
														<div class="control-group">
															<label for="perimetroSanitario" class="control-label">Codifica identificativo del perimetro sanitario</label>
															<div class="controls input-append">
																<s:select list="listaPerimetroSanitarioSpesa" id="perimetroSanitario" cssClass="span10"
																		name="perimetroSanitarioSpesa.uid" headerKey="0" headerValue="" disabled="true"
																		listKey="uid" listValue="%{codice + '-' + descrizione}" />
															</div>
														</div>
														<div class="control-group">
															<label for="transazioneUnioneEuropea" class="control-label">Codifica transazione UE</label>
															<div class="controls input-append">
																<s:select list="listaTransazioneUnioneEuropeaSpesa" id="transazioneUnioneEuropea" cssClass="span10"
																		name="transazioneUnioneEuropeaSpesa.uid" headerKey="0" headerValue="" disabled="true"
																		listKey="uid" listValue="%{codice + '-' + descrizione}" />
															</div>
														</div>
														<div class="control-group">
															<label for="politicheRegionaliUnitarie" class="control-label">Codifica politiche regionali unitarie</label>
															<div class="controls input-append">
																<s:select list="listaPoliticheRegionaliUnitarie" id="politicheRegionaliUnitarie" cssClass="span10"
																		name="politicheRegionaliUnitarie.uid" headerKey="0" headerValue="" disabled="true"
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
																			cssClass="span10" name="%{'classificatoreGenerico' + #idx + '.uid'}" headerKey="0" headerValue=""
																			listKey="uid" listValue="%{codice + '-' + descrizione}" disabled="true" />
																			
																	</div>
																</div>
															</s:if>
														</s:iterator>
														<!--SIAC 6884-->
														<s:hidden value="%{capitoloFondino}" name="capitoloFondino" id="fondinoHiddenValue"/>
														<div class="control-group">
															<label for="Note" class="control-label">Note</label>
															<div class="controls">
																<s:textarea id="Note" disabled="true" rows="5" cols="15" name="capitoloUscitaPrevisione.note" />
															</div>
														</div>
													</fieldset>
												</div>
											</div>
										</div>
									</div>

									<h4>Stanziamenti</h4>
									
									<table class="table table-hover table-condensed table-bordered"
										id="tabellaStanziamentiTotaliComponenti">
										<tr>
											<th>&nbsp;</th>
											<th>&nbsp;</th>
											<th class="text-right">${annoEsercizioInt - 1}</th>
											<th class="text-right">Residui ${annoEsercizioInt + 0}</th>
											<th class="text-right">${annoEsercizioInt + 0}</th>
											<th class="text-right">${annoEsercizioInt + 1}</th>
											<th class="text-right">${annoEsercizioInt + 2}</th>
											<th class="text-right">>${annoEsercizioInt + 2}</th>
										</tr>
										<tr>
											<th rowspan="3" class="stanziamenti-titoli">
												<a id="competenzaTotale" href="#" class="disabled">Competenza</a>
											</th>
											<td class="text-center">Stanziamento</td>
											<td class="text-right"></td>
											<td class="text-right"></td>
											<td class="text-right"></td>
											<td class="text-right"></td>
											<td class="text-right"></td>
											<td class="text-right"></td>
										</tr>
										<tr>
											<td class="text-center">Impegnato</td>
											<td class="text-right"></td>
											<td class="text-right"></td>
											<td class="text-right"></td>
											<td class="text-right"></td>
											<td class="text-right"></td>
											<td class="text-right"></td>
										</tr>
										<tr>
											<td class="text-center">Disponibilit&agrave; Impegnare</td>
											<td class="text-right"></td>
											<td class="text-right"></td>
											<td class="text-right"></td>
											<td class="text-right"></td>
											<td class="text-right"></td>
											<td class="text-right"></td>
										</tr>
										<tr>
											<th rowspan="2" class="stanziamenti-titoli">
												Residuo
											</th>
											<td class="text-center">Presunto</td>
											<td class="text-right"></td>
											<td class="text-right"></td>
											<td class="text-right"></td>
											<td class="text-right"></td>
											<td class="text-right"></td>
											<td class="text-right"></td>
										</tr>
										<tr>
											<td class="text-center">Effettivo</td>
											<td class="text-right"></td>
											<td class="text-right"></td>
											<td class="text-right"></td>
											<td class="text-right"></td>
											<td class="text-right"></td>
											<td class="text-right"></td>
										</tr>
										<tr>
											<th rowspan="2" class="stanziamenti-titoli">
												Cassa
											</th>
											<td class="text-center">Stanziamento</td>
											<td class="text-right"></td>
											<td class="text-right"></td>
											<td class="text-right"></td>
											<td class="text-right"></td>
											<td class="text-right"></td>
											<td class="text-right"></td>
										</tr>
										<tr>
											<td class="text-center">Pagato</td>
											<td class="text-right"></td>
											<td class="text-right"></td>
											<td class="text-right"></td>
											<td class="text-right"></td>
											<td class="text-right"></td>
											<td class="text-right"></td>
										</tr>
									</table>

									<div class="spaceBottom">
										<input type="submit" class="btn" value="visualizza totali di previsione" name="action:visualizzaTotaliPrevisioneCapUscitaPrevisione" />
									</div>

									<p>
										<%-- Cosa chiamare? --%>

										<s:if test="%{titolo.contains('Inserimento')}">
											<s:a action="inserisciCapUscitaPrevisione" cssClass="btn" >indietro</s:a>
										</s:if><s:elseif test="%{daAggiornamento}">
											<s:include value="/jsp/include/indietro.jsp" />
										</s:elseif><s:else>
											<s:url action="redirezioneAggiornamentoCapUscitaPrevisione" var="url">
												<s:param name="uidDaAggiornare">${capitoloUscitaPrevisione.uid}</s:param>
											</s:url>
											<s:a href="%{url}" cssClass="btn" >indietro</s:a>
										</s:else>
										<button type="submit" class="btn btn-primary pull-right submit">aggiorna</button>

										<!--Prima di aggiungere stanziamenti, vai su aggiorna-->
										<%--
										<s:a action="gestioneComponenteImportoCapitoloNelCapitolo" cssClass="btn btn-primary pull-right">
											<s:param name="uidCapitolo" value="%{capitoloUscitaPrevisione.uid}"/>
											<s:param name="fromPage">INSERT</s:param>
											<!--SIAC 6884 passo il valore del fondino-->
											<s:param name="isCapitoloFondino" value="%{capitoloFondino}"/>
											stanziamenti
										</s:a>
										--%>
									</p>
								</form>
							</div>
							<!-- END Blocco Capitolo -->

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
	<script type="text/javascript" src="/siacbilapp/js/local/capitolo/capitoloUscita.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/capitoloUscitaPrevisione/inserisci.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/attoDiLegge/attoDiLegge.js"></script>
</body>
</html>