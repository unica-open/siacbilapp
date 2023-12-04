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

						<h3>Capitolo
							<s:property value="capitoloUscitaGestione.numeroCapitolo" />/
							<s:property value="capitoloUscitaGestione.numeroArticolo" />
							<s:if test="gestioneUEB">/
								<s:property value="capitoloUscitaGestione.numeroUEB" />
							</s:if>
						</h3>
						<ul id="tabs" class="nav nav-tabs" data-tabs="tabs">
							<li class="active"><a href="#capitolo" data-toggle="tab">Capitolo</a></li>
							<%-- <li><a href="#vincoli" data-toggle="tab">Vincoli</a></li> --%>
							<li><a href="#attiDiLegge" data-toggle="tab">Atti di legge</a></li>
						</ul>

						<div id="my-tab-content" class="tab-content">
							<!-- BEGIN Blocco Capitolo -->

							<div class="tab-pane active" id="capitolo">
								<form action="redirezioneAggiornamentoCapUscitaGestione.do" method="post"
									id="aggiornamentoCapUscitaGestione" name="formInserimento"
									data-disabled-form="true">
									<%-- Uid del capitolo inserito --%>
									<s:hidden value="%{capitoloUscitaGestione.uid}" name="uidDaAggiornare"
										id="uidCapitoloDaAggiornare" />
									<s:hidden name="daAggiornamento" />
									<fieldset class="form-horizontal">
										<div class="control-group">
											<label class="control-label" for="annoda2">Anno</label>
											<div class="controls">
												<input type="text" id="annoda2" class="lbTextSmall span2"
													disabled="disabled" value="${AnnoEsercizioInt}" maxlength="4" />
												<s:hidden name="bilancio.anno" value="%{annoEsercizioInt}" />
												<s:hidden name="bilancio.uid" />
												<span class="al">
													<label class="radio inline" for="annoa">Capitolo *</label>
												</span>
												<s:textfield id="annoa" cssClass="lbTextSmall span2" maxlength="200"
													disabled="true" name="capitoloUscitaGestione.numeroCapitolo" />
												<span class="al">
													<label class="radio inline" for="art">Articolo *</label>
												</span>
												<s:textfield id="art" cssClass="lbTextSmall span2" maxlength="200"
													disabled="true" name="capitoloUscitaGestione.numeroArticolo" />
												<s:if test="gestioneUEB">
													<span class="al">
														<label class="radio inline" for="ueb2">
															<abbr title="Unit&agrave; Elementare Bilancio">UEB</abbr>
														</label>
													</span>
													<s:textfield id="ueb" cssClass="lbTextSmall span2" maxlength="200"
														disabled="true" name="capitoloUscitaGestione.numeroUEB" />
												</s:if>
											</div>
										</div>

										<div class="control-group">
											<label for="titolo_adv" class="control-label">Descrizione *</label>
											<div class="controls">
												<s:textarea rows="5" cols="15" id="titolo_adv" disabled="true"
													name="capitoloUscitaGestione.descrizione" cssClass="span10" />
											</div>
										</div>
										<div class="control-group">
											<label for="descrart" class="control-label">Descrizione Articolo</label>
											<div class="controls">
												<s:textarea rows="5" cols="15" id="titolo_adv" disabled="true"
													name="capitoloUscitaGestione.descrizioneArticolo"
													cssClass="span10" />
											</div>
										</div>
										<div class="control-group">
											<label for="nomeautore" class="control-label">Missione *</label>
											<div class="controls">
												<s:select list="listaMissione" id="nomeautore" name="missione.uid"
													cssClass="span10" disabled="true" headerKey="0" headerValue=""
													listKey="uid" listValue="%{codice + '-' + descrizione}" />
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
														<s:radio theme="simple" name="capitoloUscitaGestione.flagNonInserireAllegatoA1" list="#{true:'Sì'}" disabled="true"/>
													</label>
													<label class="radio inline">
														<s:radio theme="simple" name="capitoloUscitaGestione.flagNonInserireAllegatoA1" list="#{false:'No'}" disabled="true"/>
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
												<s:select list="listaProgramma" id="soggetto5" name="programma.uid"
													cssClass="span10" disabled="true" headerKey="0" headerValue=""
													listKey="uid" listValue="%{codice + '-' + descrizione}" />
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
												<s:select list="listaClassificazioneCofog"
													name="classificazioneCofog.uid" id="Categoria" cssClass="span10"
													disabled="true" headerKey="0" headerValue="" listKey="uid"
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
												<s:select list="listaTitoloSpesa" name="titoloSpesa.uid" id="Titolo"
													cssClass="span10" disabled="true" headerKey="0" headerValue=""
													listKey="uid" listValue="%{codice + '-' + descrizione}" />
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
												<s:select list="listaMacroaggregato" name="macroaggregato.uid"
													id="Macroaggregato" cssClass="span10" disabled="true" headerKey="0"
													headerValue="" listKey="uid"
													listValue="%{codice + '-' + descrizione}" />
											</div>
										</div>
										<%-- zTree --%>
										<div class="control-group">
											<label for="pdc" class="control-label">
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
												<s:property value="pdcFinanziario" />
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
											<div class="controls">
												<s:property value="siopeInserito" />
											</div>
										</div>
										<%-- zTree --%>
										<div class="control-group">
											<label for="finanz2" class="control-label">
												Struttura Amministrativa Responsabile *
											</label>
											<div class="controls">
												<s:property value="strutturaAmministrativoResponsabile" />
											</div>
										</div>
										<div class="control-group">
											<label for="categoriaCapitolo" class="control-label">Tipo capitolo</label>
											<div class="controls">
												<s:select id="categoriaCapitolo" listKey="uid"
													list="listaCategoriaCapitolo"
													name="capitoloUscitaGestione.categoriaCapitolo.uid"
													cssClass="span10" listValue="%{codice + '-' + descrizione}"
													disabled="true" headerKey="0" headerValue="" />
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
												<s:checkbox id="flagImpegnabile"
													name="capitoloUscitaGestione.flagImpegnabile" disabled="true"
													data-editabile="false" />
											</div>
										</div>
									</fieldset>

									<div class="accordion" id="accordion2">
										<div class="accordion-group">
											<div class="accordion-heading">
												<a class="accordion-toggle collapsed" data-toggle="collapse"
													id="ModalAltriDati" data-parent="#accordion2" href="#collapseOne">
													Altri dati<span class="icon">&nbsp;</span>
												</a>
											</div>
											<div id="collapseOne" class="accordion-body collapse">
												<div class="accordion-inner">
													<fieldset class="form-horizontal">

														<div class="control-group">
															<label for="finanz" class="control-label">Tipo
																Finanziamento</label>
															<div class="controls input-append">
																<s:select list="listaTipoFinanziamento" id="finanz"
																	cssClass="span10" name="tipoFinanziamento.uid"
																	headerKey="0" headerValue="" disabled="true"
																	listKey="uid"
																	listValue="%{codice + '-' + descrizione}" />
															</div>
														</div>
														<div class="control-group">
															<label for="Ricorrente" class="control-label">
																Rilevante IVA
															</label>
															<div class="controls">
																<s:checkbox id="Ricorrente" disabled="true"
																	name="capitoloUscitaGestione.flagRilevanteIva"
																	value="%{capitoloUscitaGestione.flagRilevanteIva}" />
															</div>
														</div>
														<div class="control-group">
															<label for="delegate" class="control-label">
																Funzioni delegate dalla Regione
															</label>
															<div class="controls">
																<s:checkbox id="delegate" disabled="true"
																	name="capitoloUscitaGestione.funzDelegateRegione"
																	value="%{capitoloUscitaGestione.funzDelegateRegione}" />
															</div>
														</div>
														<div class="control-group">
															<label for="fondo" class="control-label">Tipo fondo</label>
															<div class="controls input-append">
																<s:select list="listaTipoFondo" id="tipoFondo"
																	cssClass="span10" name="tipoFondo.uid" headerKey="0"
																	headerValue="" disabled="true" listKey="uid"
																	listValue="%{codice + '-' + descrizione}" />
															</div>
														</div>
														<div class="control-group">
															<span class="control-label">Spesa</span>
															<div class="controls">
																<s:iterator value="listaRicorrenteSpesa"
																	var="ricorrente" status="stat">
																	<label class="radio inline">
																		<input type="radio" name="ricorrenteSpesa.uid"
																			value="<s:property value=" %{#ricorrente.uid}" />" <s:if test="%{ricorrenteSpesa.uid == #ricorrente.uid}">checked="checked"</s:if> disabled="disabled">&nbsp;<s:property value="%{#ricorrente.descrizione}" />
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
																<s:textarea id="Note" disabled="disabled" rows="5" cols="15" name="capitoloUscitaGestione.note" />
															</div>
														</div>
													</fieldset>
												</div>
											</div>
										</div>
									</div>


									<h4>Stanziamenti</h4>
									<!--Nuova tabella GESC035-->
									<table summary="riepilogo incarichi" class="table table-hover table-bordered">
										<tr>
											<th width="20%" >&nbsp;</th>
											<th width="20%" >&nbsp;</th>
											<th width="10%"><s:property value="%{annoEsercizioInt -1}"/></th>
											<th width="10%" class="text-center">Residui <s:property value="%{annoEsercizioInt + 0}"/></th>
											<th width="10%" class="text-center"><s:property value="%{annoEsercizioInt + 0}"/></th>
											<th width="10%" class="text-center"><s:property value="%{annoEsercizioInt + 1}"/></th>
											<th width="10%" class="text-center"><s:property value="%{annoEsercizioInt + 2}"/></th>
											<th width="10%" class="text-center">><s:property value="%{annoEsercizioInt + 2}"/></th>
										</tr>

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
											</td>
											<td class="text-right"> <!--Residuo -->
												<label for="stanziamentoR" class="nascosto">inserisci importo</label>
												<span id="stanziamentoR" ><s:property value="competenzaStanziamento.dettaglioResidui.importo"/></span>	
											</td>
											<td class="text-right">
													<label for="stanziamento0" class="nascosto">inserisci importo</label>
													<s:property value="competenzaStanziamento.dettaglioAnno0.importo"/>												
												</td>
											<td class="text-right">
												<label for="imp3a" class="nascosto">inserisci importo</label>
												<s:property value="competenzaStanziamento.dettaglioAnno1.importo"/>
											</td>
											<td class="text-right">
												<label for="imp3b" class="nascosto">inserisci importo</label>
												<s:property value="competenzaStanziamento.dettaglioAnno2.importo"/>
											</td>
											<td class="text-right">
												<label for="imp3c" class="nascosto">inserisci importo</label>
												<s:property value="competenzaStanziamento.dettaglioAnniSucc.importo"/>
											</td>
											
										</tr>
										<tr>
											<td class="text-center">Impegnato</td>
											<td class="text-right">
												<s:property value="disponibilita.dettaglioAnnoPrec.importo"/>
											</td>
											<td class="text-right"> 
												<s:property value="disponibilita.dettaglioResidui.importo"/>	
											</td>
											<td class="text-right">
												<s:property value="disponibilitaCapitoloUscitaGestioneAnno0.impegnato"/>
											</td>
											<td class="text-right">
												<s:property value="disponibilitaCapitoloUscitaGestioneAnno1.impegnato"/>
											</td>
											<td class="text-right">
												<s:property value="disponibilitaCapitoloUscitaGestioneAnno2.impegnato"/>
											</td>
											<td class="text-right">
												<s:property value="disponibilita.dettaglioAnniSucc.importo"/>
											</td>
																						
										</tr>
										<!--SIAC-7349 - MR - SR210 12.05.2020 Nascondo riga come richiesto-->
										<%--<tr>
											<td class="text-center">Disponibilit&agrave; ad impegnare</td>
											<td class="text-right">
												<s:property value="competenzaImpegnato.dettaglioAnnoPrec.importo"/>
											</td>
											<td class="text-right"> <!--Residuo -->
												<s:property value="competenzaImpegnato.dettaglioResidui.importo"/>		
											</td>
											<td class="text-right">
												<s:property value="competenzaImpegnato.dettaglioAnno0.importo"/>
											</td>
											<td class="text-right">
												<s:property value="competenzaImpegnato.dettaglioAnno1.importo"/>
											</td>
											<td class="text-right">
												<s:property value="competenzaImpegnato.dettaglioAnno2.importo"/>
											</td>
											<td class="text-right">
												<s:property value="competenzaImpegnato.dettaglioAnniSucc.importo"/>
											</td>
																						
										</tr>--%>
										
										<tbody id="componentiCompetenzaAgg">
											<s:iterator value="importiComponentiCapitolo" var="cc">												
												<tr>													
													<td id="description-component" class="componenti-competenza" rowspan="1">
														<s:property value="#cc.tipoComponenteImportiCapitolo.descrizione"/>
													</td> 
														
													<td class="text-center" id="type-component">
														<s:property value="#cc.tipoDettaglioComponenteImportiCapitolo.descrizione"/>
													</td>
													<td class="text-right"> 
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

										<tr>
											<th rowspan="2" class="stanziamenti-titoli">Residuo</th>
											<td class="text-center">
												Presunti
											</td>
											<td class="text-right">
												<label for="residuoPrM1" class="nascosto">inserisci importo</label>
												<span id="residuoPrM1"><s:property value="residuiPresunti.dettaglioAnnoPrec.importo"/></span>
												
											</td>
											<td class="text-right"> 
													<s:property value="residuiPresunti.dettaglioResidui.importo"/>	
												</td>
											<td class="text-right">
												<label for="residuo0" class="nascosto">inserisci importo</label>
												<s:property value="residuiPresunti.dettaglioAnno0.importo"/>
												
											</td>
											<td class="text-right">
												<label for="residuo1" class="nascosto">inserisci importo</label>
												<s:property value="residuiPresunti.dettaglioAnno1.importo"/>
												
											</td>
											<td class="text-right">
												<label for="residuo2" class="nascosto">inserisci importo</label>
												<s:property value="residuiPresunti.dettaglioAnno2.importo"/>
												
											</td>
											<td class="text-right">
													<label for="residuo2" class="nascosto">inserisci importo</label>
													<s:property value="residuiPresunti.dettaglioAnniSucc.importo"/>
													
												</td>
											
										</tr>
										<tr>
											<td class="text-center">Effettivi</td>
											<td class="text-right">
												<span id="residuoEffM1">
													<s:property value="residuiEffettivi.dettaglioAnnoPrec.importo"/>
												</span>
																									
											</td>
											<td class="text-right"> <!--Residuo -->
												<s:property value="residuiEffettivi.dettaglioResidui.importo"/>	
											</td>
											<td class="text-right">
												<s:property value="residuiEffettivi.dettaglioAnno0.importo"/>
												
											</td>
											<td class="text-right">
												<s:property value="residuiEffettivi.dettaglioAnno1.importo"/>
											</td>
											<td class="text-right">
												<s:property value="residuiEffettivi.dettaglioAnno2.importo"/>												
											</td>
											<td class="text-right">
												<s:property value="residuiEffettivi.dettaglioAnniSucc.importo"/>												
											</td>
											
											
										</tr>	
										<tr>
											<th rowspan="2" class="stanziamenti-titoli">Cassa</th>
											<td class="text-center">
												Stanziamento
											</td>
											<td class="text-right">
												<label for="cassaM1" class="nascosto">inserisci importo</label>
												<span id="impCasM1"><s:property value="cassaStanziato.dettaglioAnnoPrec.importo"/></span>
												
											</td>
											<td class="text-right"> <!--Residuo -->
												<s:property value="cassaStanziato.dettaglioResidui.importo"/>	
											</td>
											<td class="text-right">
												<label for="cassa0" class="nascosto">inserisci importo</label>
												<s:property value="cassaStanziato.dettaglioAnno0.importo"/>
												
											</td>
											<td class="text-right">
												<label for="cassa1" class="nascosto">inserisci importo</label>
												<s:property value="cassaStanziato.dettaglioAnno1.importo"/>
												
											</td>
											<td class="text-right">
												<label for="cassa2" class="nascosto">inserisci importo</label>
												&nbsp;		
											</td>
											<td class="text-right">
													<label for="cassa2" class="nascosto">inserisci importo</label>
													&nbsp;		
												</td>
											
												
										</tr>
											
											<tr>											
												<td class="text-center">Pagato</td>
												<td class="text-right">
													<span id="impCasM1"><s:property value="cassaPagato.dettaglioAnnoPrec.importo"/></span>
													
												</td>
												<td class="text-right">
													<s:property value="cassaPagato.dettaglioResidui.importo"/>
													
												</td>
												<td class="text-right"> <!--Residuo -->
													<s:property value="cassaPagato.dettaglioAnno0.importo"/>	
												</td>
												<td class="text-right">
													<s:property value="cassaPagato.dettaglioAnno1.importo"/>
													
												</td>
												<td class="text-right">
														&nbsp;
												</td>
												<td class="text-right">
														<label for="cassa2" class="nascosto">inserisci importo</label>
														&nbsp;		
												</td>
												
											</tr>
									</table>
									<p>
										<s:if test="%{titolo.contains('Inserimento')}">
											<s:a action="inserisciCapUscitaGestione" cssClass="btn">indietro</s:a>
										</s:if><s:elseif test="%{daAggiornamento}">
											<s:include value="/jsp/include/indietro.jsp" />
										</s:elseif><s:else>
											<s:url action="redirezioneAggiornamentoCapUscitaGestione" var="url">
												<s:param name="uidDaAggiornare">${capitoloUscitaGestione.uid}</s:param>
											</s:url>
											<s:a href="%{url}" cssClass="btn" >indietro</s:a>
										</s:else>
										<s:submit cssClass="btn btn-primary pull-right" value="aggiorna"/>
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
	<script type="text/javascript" src="/siacbilapp/js/local/capitoloUscitaGestione/inserisci.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/attoDiLegge/attoDiLegge.js"></script>
</body>
</html>