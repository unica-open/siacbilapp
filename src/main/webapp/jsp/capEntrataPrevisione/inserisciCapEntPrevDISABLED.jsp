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

						<h3>Capitolo <s:property value="capitoloEntrataPrevisione.numeroCapitolo"/>/<s:property value="capitoloEntrataPrevisione.numeroArticolo"/><s:if test="gestioneUEB">/<s:property value="capitoloEntrataPrevisione.numeroUEB"/></s:if></h3>
						<ul id="tabs" class="nav nav-tabs" data-tabs="tabs" >
							<li class="active"><a href="#capitolo" data-toggle="tab">Capitolo</a></li>
							<%-- <li><a href="#vincoli" data-toggle="tab">Vincoli</a></li> --%>
							<li><a href="#attiDiLegge" data-toggle="tab">Atti di legge</a></li>
						</ul>

						<div id="my-tab-content" class="tab-content" >
							<!-- BEGIN Blocco Capitolo -->

							<div class="tab-pane active" id="capitolo">
								<form action="redirezioneAggiornamentoCapEntrataPrevisione.do" method="post" id="aggiornamentoCapEntrataPrevisione" name="formInserimento" data-disabled-form="true">
									<%-- Uid del capitolo inserito --%>
									<s:hidden value="%{capitoloEntrataPrevisione.uid}" name="uidDaAggiornare" id="uidCapitoloDaAggiornare" />
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
												<s:textfield id="annoa" cssClass="lbTextSmall span2" maxlength="200" disabled="true" name="capitoloEntrataPrevisione.numeroCapitolo" />
												<span class="al">
													<label class="radio inline" for="art">Articolo *</label>
												</span>
												<s:textfield id="art" cssClass="lbTextSmall span2" maxlength="200" disabled="true" name="capitoloEntrataPrevisione.numeroArticolo" />
												<s:if test="gestioneUEB">
													<span class="al">
														<label class="radio inline" for="ueb2">
															<abbr title="Unit&agrave; Elementare Bilancio">UEB</abbr>
														</label>
													</span>
													<s:textfield id="ueb" cssClass="lbTextSmall span2" maxlength="200" disabled="true" name="capitoloEntrataPrevisione.numeroUEB" />
												</s:if>
											</div>
										</div>

										<div class="control-group">
											<label for="titolo_adv" class="control-label">Descrizione *</label>
											<div class="controls">
												<s:textarea rows="5" cols="15" id="titolo_adv" disabled="true" cssClass="span10" name="capitoloEntrataPrevisione.descrizione" />
											</div>
										</div>
										<div class="control-group">
											<label for="descrart" class="control-label">Descrizione Articolo</label>
											<div class="controls">
												<s:textarea rows="5" cols="15" id="titolo_adv" disabled="true" cssClass="span10" name="capitoloEntrataPrevisione.descrizioneArticolo" />
											</div>
										</div>
										<div class="control-group">
											<label for="Titolo" class="control-label">Titolo *</label>
											<div class="controls">
												<s:select list="listaTitoloEntrata" id="titoloEntrata" cssClass="span10" disabled="true" name="titoloEntrata.uid"
													headerKey="0" headerValue="" listKey="uid" listValue="%{codice + '-' + descrizione}" />
											</div>
										</div>
										<div class="control-group">
											<label for="Macroaggregato" class="control-label">Tipologia *
												<a class="tooltip-test" title="selezionare prima il Titolo" href="#">
													<i class="icon-info-sign">&nbsp;
														<span class="nascosto">selezionare prima il Titolo</span>
													</i>
												</a>
											</label>
											<div class="controls">
												<s:select list="listaTipologiaTitolo" id="Macroaggregato" cssClass="span10"
													name="tipologiaTitolo.uid" headerKey="0" headerValue="" disabled="true"
													listKey="uid" listValue="%{codice + '-' + descrizione}" />
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
												<s:select list="listaCategoriaTipologiaTitolo" id="Categoria" cssClass="span10"
													name="categoriaTipologiaTitolo.uid" headerKey="0" headerValue="" disabled="true"
													listKey="uid" listValue="%{codice + '-' + descrizione}" />
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
												<s:select id="categoriaCapitolo" listKey="uid" list="listaCategoriaCapitolo" name="capitoloEntrataPrevisione.categoriaCapitolo.uid" cssClass="span10"
													listValue="%{codice + '-' + descrizione}" disabled="true" headerKey="" headerValue="" />
											</div>
										</div>
										<div class="control-group">
											<label for="flagImpegnabile" class="control-label">Accertabile</label>
											<div class="controls">
												<s:checkbox id="flagImpegnabile" name="capitoloEntrataPrevisione.flagImpegnabile" disabled="true" data-editabile="false" />
											</div>
										</div>
										<!-- SIAC-7858 CM 12/05/2021 Inizio -->
										<div class="control-group">
											<label for="flagEntrataDubbiaEsigFCDE" class="control-label">Capitolo pertinente per il calcolo FCDE</label>
											<div class="controls">
												<span class="al">
													<label class="radio inline" >
														<input type="radio" disabled="true" value="true" name="flagEntrataDubbiaEsigFCDE" <s:if test='%{capitoloEntrataPrevisione.flagEntrataDubbiaEsigFCDE==true}'>checked="checked"</s:if>>Si
													</label>
												</span>
												<span class="al">
													<label class="radio inline" style="margin-left: 15px;">
														<input type="radio" disabled="true" value="false" name="flagEntrataDubbiaEsigFCDE" <s:if test='%{(capitoloEntrataPrevisione.flagEntrataDubbiaEsigFCDE==null)||(capitoloEntrataPrevisione.flagEntrataDubbiaEsigFCDE==false)}'>checked="checked"</s:if>>No
													</label>
												</span>
											</div>
										</div>
										<!-- SIAC-7858 CM 12/05/2021 Fine -->
										<div class="control-group <s:if test="!flagAccertatoPerCassaVisibile">hide</s:if>">
											<label for="flagAccertatoPerCassa" class="control-label">Accertato per cassa</label>
											<div class="controls">
												<s:checkbox id="flagAccertatoPerCassa" name="capitoloEntrataPrevisione.flagAccertatoPerCassa" disabled="true" data-editabile="false" />
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

														<div class="control-group">
															<label for="finanz" class="control-label">Tipo Finanziamento</label>
															<div class="controls input-append">
																<s:select list="listaTipoFinanziamento" id="tipoFinanziamento" cssClass="span10"
																		name="tipoFinanziamento.uid" headerKey="" headerValue="" disabled="true"
																		listKey="uid" listValue="%{codice + '-' + descrizione}" />
															</div>
														</div>
														<div class="control-group">
															<label for="iva" class="control-label">
																Rilevante IVA
															</label>
															<div class="controls">
																<s:checkbox id="iva" disabled="true" name="capitoloEntrataPrevisione.flagRilevanteIva" />
															</div>
														</div>
														<div class="control-group">
															<label for="memoria" class="control-label">Corsivo per memoria</label>
															<div class="controls">
																<s:checkbox id="memoria" disabled="true" name="capitoloEntrataPrevisione.flagPerMemoria" />
															</div>
														</div>
														<div class="control-group">
															<label for="fondo" class="control-label">Tipo fondo</label>
															<div class="controls input-append">
																<s:select list="listaTipoFondo" id="tipoFondo" cssClass="span10"
																		name="tipoFondo.uid" headerKey="" headerValue="" disabled="true"
																		listKey="uid" listValue="%{codice + '-' + descrizione}" />
															</div>
														</div>
														<div class="control-group">
															<span class="control-label">Entrata</span>
															<div class="controls">
																<s:iterator value="listaRicorrenteEntrata" var="ricorrente" status="stat">
																	<label class="radio inline">
																		<input type="radio" name="ricorrenteEntrata.uid" value="<s:property value="%{#ricorrente.uid}" />" <s:if test="%{ricorrenteEntrata.uid == #ricorrente.uid}">checked="checked"</s:if> disabled="disabled">&nbsp;<s:property value="%{#ricorrente.descrizione}" />
																	</label>
																</s:iterator>
															</div>
														</div>
														<div class="control-group">
															<label for="perimetroSanitario" class="control-label">Codifica identificativo del perimetro sanitario</label>
															<div class="controls input-append">
																<s:select list="listaPerimetroSanitarioEntrata" id="perimetroSanitario" cssClass="span10"
																		name="perimetroSanitarioEntrata.uid" headerKey="" headerValue="" disabled="true"
																		listKey="uid" listValue="%{codice + '-' + descrizione}" />
															</div>
														</div>
														<div class="control-group">
															<label for="transazioneUnioneEuropea" class="control-label">Codifica transazione UE</label>
															<div class="controls input-append">
																<s:select list="listaTransazioneUnioneEuropeaEntrata" id="transazioneUnioneEuropea" cssClass="span10"
																		name="transazioneUnioneEuropeaEntrata.uid" headerKey="" headerValue="" disabled="true"
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
																			listKey="uid" listValue="%{codice + '-' + descrizione}" disabled="true" />
																	</div>
																</div>
															</s:if>
														</s:iterator>
														<div class="control-group">
															<label for="Note" class="control-label">Note</label>
															<div class="controls">
																<textarea id="Note" disabled="disabled" rows="5"
																		cols="15">${capitoloEntrataPrevisione.note}</textarea>
																<s:hidden name="capitoloEntrataPrevisione.note"
																		value="%{capitoloEntrataPrevisione.note}" />
															</div>
														</div>
													</fieldset>
												</div>
											</div>
										</div>
									</div>

									<h4>Stanziamenti</h4>

									<table summary="riepilogo incarichi" class="table table-hover table-bordered">
										<tr>
											<th width="20%">&nbsp;</th>
											<th width="20%" class="text-right"><s:property value="%{annoEsercizioInt - 1}"/></th>
											<th width="20%" class="text-right"><s:property value="%{annoEsercizioInt + 0}"/></th>
											<th width="20%" class="text-right"><s:property value="%{annoEsercizioInt + 1}"/></th>
											<th width="20%" class="text-right"><s:property value="%{annoEsercizioInt + 2}"/></th>
										</tr>

										<tr>
											<th>Competenza</th>
											<td class="text-right">
												<s:textfield id="imp1" cssClass="input-small text-right" name="importiEx.stanziamento" disabled="true" />
											</td>
											<td class="text-right">
												<s:textfield id="imp1" cssClass="input-small text-right" name="importiCapitoloEntrataPrevisione0.stanziamento" disabled="true" />
											</td>
											<td class="text-right">
												<s:textfield id="imp3a" cssClass="input-small text-right" name="importiCapitoloEntrataPrevisione1.stanziamento" disabled="true" />
											</td>
											<td class="text-right">
												<s:textfield id="imp3b" cssClass="input-small text-right" name="importiCapitoloEntrataPrevisione2.stanziamento" disabled="true" />
											</td>
										</tr>
										<tr>
											<th><em>di cui gi&agrave; accertato</em></th>
											<td class="text-right">
												<s:property value="importiEx.diCuiAccertatoAnno1" />
											</td>
											<td class="text-right">
												<s:property value="importiCapitoloEntrataPrevisione0.diCuiAccertatoAnno1" />
											</td>
											<td class="text-right">
												<s:property value="importiCapitoloEntrataPrevisione0.diCuiAccertatoAnno2" />
											</td>
											<td class="text-right">
												<s:property value="importiCapitoloEntrataPrevisione0.diCuiAccertatoAnno3" />
											</td>
										</tr>
										<tr>
											<th>Residuo</th>
											<td class="text-right">
												<s:textfield id="imp2" cssClass="input-small text-right" name="importiEx.stanziamentoResiduo" disabled="true" />
											</td>
											<td class="text-right">
												<s:textfield id="imp2" cssClass="input-small text-right" name="importiCapitoloEntrataPrevisione0.stanziamentoResiduo" disabled="true" />
											</td>
											<td class="text-right"></td>
											<td class="text-right"></td>
										</tr>

										<tr>
											<th>Cassa</th>
											<td class="text-right">
												<s:textfield id="imp3" cssClass="input-small text-right" name="importiEx.stanziamentoCassa" disabled="true" />
											</td>
											<td class="text-right">
												<s:textfield id="imp3" cssClass="input-small text-right" name="importiCapitoloEntrataPrevisione0.stanziamentoCassa" disabled="true" />
											</td>
											<td class="text-right"></td>
											<td class="text-right"></td>
										</tr>
									</table>

									<div class="spaceBottom">
										<input type="submit" class="btn" value="visualizza totali di previsione" name="action:visualizzaTotaliPrevisioneCapEntrataPrevisione" />
									</div>

									<p>
										<%-- Cosa chiamare? --%>
										<s:if test="%{titolo.contains('Inserimento')}">
											<s:a action="inserisciCapEntrataPrevisione" cssClass="btn" >indietro</s:a>
										</s:if><s:elseif test="%{daAggiornamento}">
											<s:include value="/jsp/include/indietro.jsp" />
										</s:elseif><s:else>
											<s:url action="redirezioneAggiornamentoCapEntrataPrevisione" var="url">
												<s:param name="uidDaAggiornare">${capitoloEntrataPrevisione.uid}</s:param>
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
	<script type="text/javascript" src="/siacbilapp/js/local/capitolo/capitoloEntrata.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/capitoloEntrataPrevisione/inserisci.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/attoDiLegge/attoDiLegge.js"></script>
</body>
</html>