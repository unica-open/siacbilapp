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
				<div class="contentPage">
					<s:include value="/jsp/include/messaggi.jsp" />
					<h3>Ricerca Capitolo Spesa Gestione</h3>
					<s:form method="post" action="effettuaRicercaMassivaConOperazioniCapUscitaGestione" novalidate="novalidate">
						<s:submit cssClass="btn btn-primary pull-right" value="cerca"/>
						<p>&Egrave; necessario inserire almeno un criterio di ricerca.</p>
						<br/>
						<div class="fieldset">
							<div class="fieldset-group">
								<div class="fieldset-heading">
									<h4>Capitolo</h4>
								</div>
								<div class="fieldset-body">
									<fieldset class="form-horizontal">
										<div class="control-group">
											<label class="control-label" for="numeroCapitolo">Capitolo</label>
											<div class="controls">
												<input type="text" id="numeroCapitolo" class="lbTextSmall span2 soloNumeri" 
													name="capitoloUscitaGestione.numeroCapitolo" maxlength="200" 
													placeholder="capitolo" value="${capitoloUscitaGestione.numeroCapitolo}" />
												<span class="al"> 
													<label class="radio inline" for="numeroArticolo">Articolo</label>
												</span> 
												<input type="text" id="numeroArticolo" class="lbTextSmall span2 soloNumeri" 
														name="capitoloUscitaGestione.numeroArticolo" maxlength="200"
														placeholder="articolo" value="${capitoloUscitaGestione.numeroArticolo}" />
											</div>
										</div>
										<div class="control-group">
											<label for="descrizioneCapitolo" class="control-label">Descrizione</label>
											<div class="controls">
												<textarea rows="5" cols="15" id="descrizioneCapitolo" maxlength="500"
														name="capitoloUscitaGestione.descrizione" class="span10">${capitoloUscitaGestione.descrizione}</textarea>
											</div>
										</div>
										<div class="control-group">
											<label for="descrizioneArticolo" class="control-label">Descrizione Articolo</label>
											<div class="controls">
												<textarea rows="5" cols="15" id="descrizioneArticolo"  maxlength="500"
													name="capitoloUscitaGestione.descrizioneArticolo" class="span10">${capitoloUscitaGestione.descrizioneArticolo}</textarea>
											</div>
										</div>
										<s:if test="missioneEditabile">
											<div class="control-group">
												<label for="missione" class="control-label">Missione</label>
												<div class="controls">
													<s:select id="missione" list="listaMissione" name="missione.uid" cssClass="span10"
														headerKey="" headerValue="" listKey="uid" listValue="%{codice + '-' + descrizione}" />
												</div>
											</div>
										</s:if>
										<s:if test="programmaEditabile">
											<div class="control-group">
												<label for="programma" class="control-label">Programma 
														<a class="tooltip-test" title="selezionare prima la Missione" href="#">
														<i class="icon-info-sign">&nbsp;
															<span class="nascosto">selezionare prima la Missione</span>
														</i>
														</a>
												</label>
												<div class="controls">
													<s:select id="programma" list="listaProgramma" name="programma.uid" cssClass="span10"
														headerKey="" headerValue="" listKey="uid" disabled="%{missione == null || missione.uid == 0}"
														listValue="%{codice + '-' + descrizione}" />
												</div>
											</div>
										</s:if>
										<s:if test="classificazioneCofogEditabile">
											<div class="control-group">
												<label for="classificazioneCofog" class="control-label">Cofog 
													<a class="tooltip-test" title="selezionare prima il Programma" href="#">
														<i class="icon-info-sign">&nbsp;
															<span class="nascosto">selezionare prima il Programma</span>
														</i>
													</a>
												</label>
												<div class="controls">
													<s:select id="classificazioneCofog" list="listaClassificazioneCofog" name="classificazioneCofog.uid" cssClass="span10"
														headerKey="" headerValue="" listKey="uid" disabled="%{programma == null || programma.uid == 0}"
														listValue="%{codice + '-' + descrizione}" />
													<s:hidden id="codiceTipoClassificatoreClassificazioneCofog" name="codiceTipoClassificatoreClassificazioneCofog" />
												</div>
											</div>
										</s:if>
										<s:if test="titoloSpesaEditabile">
											<div class="control-group">
											<label for="titoloSpesa" class="control-label">Titolo
												<a class="tooltip-test" title="&Eacute; possibile filtrare per il Programma" href="#">
													<i class="icon-info-sign">&nbsp;
														<span class="nascosto">&Eacute; possibile filtrare per il Programma</span>
													</i>
												</a>
											</label>
												<div class="controls">
													<s:select id="titoloSpesa" list="listaTitoloSpesa" name="titoloSpesa.uid" cssClass="span10"
														headerKey="" headerValue="" listKey="uid" listValue="%{codice + '-' + descrizione}" />
												</div>
											</div>
										</s:if>
										<s:if test="macroaggregatoEditabile">
											<div class="control-group">
												<label for="macroaggregato" class="control-label">Macroaggregato
												<a class="tooltip-test" title="selezionare prima il Titolo" href="#">
													<i class="icon-info-sign">&nbsp;
														<span class="nascosto">selezionare prima il Titolo spesa</span>
													</i>
													</a>
												</label>
												<div class="controls">
													<s:select id="macroaggregato" list="listaMacroaggregato" name="macroaggregato.uid" cssClass="span10"
														headerKey="" headerValue="" listKey="uid" disabled="%{titoloSpesa == null || titoloSpesa.uid == 0}"
														listValue="%{codice + '-' + descrizione}" />
												</div>
											</div>
										</s:if>
										<%-- zTree --%>
										<s:if test="elementoPianoDeiContiEditabile">
											<div class="control-group">
												<label for="bottonePdC" class="control-label">
													<abbr title="Piano dei Conti">P.d.C.</abbr> finanziario 
													<a class="tooltip-test" title="selezionare prima il Macroaggregato" href="#">
														<i class="icon-info-sign">&nbsp;
															<span class="nascosto">selezionare prima il Macroaggregato</span>
														</i>
													</a>
												</label>
												<div class="controls">
													<s:hidden id="HIDDEN_ElementoPianoDeiContiUid" name="elementoPianoDeiConti.uid" />
													<s:hidden id="HIDDEN_ElementoPianoDeiContiStringa" name="pdcFinanziario" />
													<s:hidden id="HIDDEN_ElementoPianoDeiContiCodice" name="pdcFinanziarioCodice" />
													<s:hidden id="HIDDEN_ElementoPianoDeiContiCodiceTipoClassificatore"
														name="codiceTipoClassificatoreElementoPianoDeiConti" />
													<s:if test='%{macroaggregato.uid neq ""}' >
														<a href="#myModal" role="button" class="btn" data-toggle="modal" id="bottonePdC">
															Seleziona il Piano dei Conti &nbsp; 
															<i class="icon-spin icon-refresh spinner" id="SPINNER_ElementoPianoDeiConti"></i>
														</a>
													</s:if>
													<s:else>
														<a href="#myModal" role="button" class="btn" data-toggle="modal" disabled="disabled" id="bottonePdC">
															Seleziona il Piano dei Conti &nbsp; 
															<i class="icon-spin icon-refresh spinner" id="SPINNER_ElementoPianoDeiConti"></i>
														</a>
													</s:else>
													&nbsp;
													<span id="SPAN_ElementoPianoDeiConti">
														<s:if test='%{pdcFinanziarioCodice != null && pdcFinanziario neq ""}'>
															<s:property value="pdcFinanziarioCodice"/>
														</s:if><s:else>
															Nessun P.d.C. finanziario selezionato
														</s:else>
													</span>
												</div>
											</div>
										</s:if>
										
										<%-- zTree --%>
										<s:hidden id="HIDDEN_SIOPEEditabile" name="siopeSpesaEditabile" data-maintain=""/>
										<s:if test="siopeSpesaEditabile">
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
													<s:hidden id="HIDDEN_SIOPECodiceTipoClassificatore" name="codiceTipoClassificatoreSiope" />
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
													<s:hidden id="HIDDEN_SIOPECodiceTipoClassificatore" name="codiceTipoClassificatoreSiope" />
													<span class="radio guidata">
														<button type="button" class="btn btn-primary" id="compilazioneGuidataSIOPE">compilazione guidata</button>
													</span>
												</div>
											</div>
										</s:if>
										<%-- zTree --%>
										<s:if test="strutturaAmministrativoContabileEditabile">
											<div class="control-group">
												<label for="bottoneSAC" class="control-label">
													Struttura Amministrativa Responsabile 
												</label>
												<div class="controls">
													<s:hidden id="HIDDEN_StrutturaAmministrativoContabileUid" name="strutturaAmministrativoContabile.uid" />
													<s:hidden id="HIDDEN_StrutturaAmministrativoContabileStringa" name="strutturaAmministrativoResponsabile" />
													<s:hidden id="HIDDEN_StrutturaAmministrativoContabileCodice" name="strutturaAmministrativoResponsabileCodice" />
													<s:hidden id="HIDDEN_StrutturaAmministrativoContabileCodiceTipoClassificatore"
														name="codiceTipoClassificatoreStrutturaAmministrativoContabile" />
													<a href="#struttAmm" role="button" class="btn" id="bottoneSAC"
															data-toggle="modal">
														Seleziona la Struttura amministrativa &nbsp;
														<i class="icon-spin icon-refresh spinner" id="SPINNER_StrutturaAmministrativoContabile"></i>
													</a>
													&nbsp;
													<span id="SPAN_StrutturaAmministrativoContabile">
														<s:if test="%{strutturaAmministrativoResponsabileCodice != null}">
															<s:property value="strutturaAmministrativoResponsabileCodice"/>
														</s:if><s:else>
															Nessuna Struttura Amministrativa Responsabile selezionata 
														</s:else>
													</span>
												</div>
											</div>
										</s:if>
									</fieldset>
								</div>
							</div>
						</div>
						
						<%-- Tabella B --%>
						<div class="accordion" id="accordion2">
							<div class="accordion-group">
								<div class="accordion-heading">
									<a class="accordion-toggle collapsed" data-toggle="collapse" 
											data-parent="#accordion2" href="#collapseOne"> 
										Altri dati<span class="icon">&nbsp;</span>
									</a>
								</div>
								<div id="collapseOne" class="accordion-body collapse">
									<div class="accordion-inner">
										<fieldset class="form-horizontal">
											<s:if test="tipoFinanziamentoEditabile">
												<div class="control-group">
													<label for="tipoFinanziamento" class="control-label">Tipo Finanziamento</label>
													<div class="controls input-append">
														<s:select list="listaTipoFinanziamento" id="tipoFinanziamento" cssClass="span10"
																name="tipoFinanziamento.uid" headerKey="" headerValue=""
																listKey="uid" listValue="%{codice + '-' + descrizione}" 
																value="tipoFinanziamento.uid" />
													</div>
												</div>
											</s:if>
											<s:if test="flagRilevanteIvaEditabile">
												<div class="control-group">
													<label for="rilevanteIva" class="control-label">Rilevante IVA</label>
													<div class="controls">
														<select id="rilevanteIva" name="flagRilevanteIva">
															<option value=""  <s:if test='flagRilevanteIva == ""' >selected</s:if>>Non si applica</option>
															<option value="S" <s:if test='flagRilevanteIva == "S"'>selected</s:if>>S&iacute;</option>
															<option value="N" <s:if test='flagRilevanteIva == "N"'>selected</s:if>>No</option>
														</select>
													</div>
												</div>
											</s:if>
											<s:if test="flagFunzioniDelegateRegioneEditabile">
												<div class="control-group">
													<label for="delegate" class="control-label">
														Funzioni delegate regione
													</label>
													<div class="controls">
														<select id="delegate" name="flagFunzioniDelegate">
															<option value=""  <s:if test='flagFunzioniDelegate == ""' >selected</s:if>>Non si applica</option>
															<option value="S" <s:if test='flagFunzioniDelegate == "S"'>selected</s:if>>S&iacute;</option>
															<option value="N" <s:if test='flagFunzioniDelegate == "N"'>selected</s:if>>No</option>
														</select>
													</div>
												</div>
											</s:if>
											<s:if test="tipoFondoEditabile">
												<div class="control-group">
													<label for="tipoFondo" class="control-label">Tipo fondo</label>
													<div class="controls input-append">
														<s:select list="listaTipoFondo" id="tipoFondo" cssClass="span10"
																name="tipoFondo.uid" headerKey="" headerValue=""
																listKey="uid" listValue="%{codice + '-' + descrizione}" />
													</div>
												</div>
											</s:if>
											<s:if test="ricorrenteSpesaEditabile">
												<div class="control-group">
													<span class="control-label">Spesa</span>
													<div class="controls">
														<label class="radio inline">
															<input type="radio" name="ricorrenteSpesa.uid" value="" <s:if test='%{ricorrenteSpesa == null || ricorrenteSpesa.uid == ""}'>checked="checked"</s:if>>&nbsp;Non si applica
														</label>
														<s:iterator value="listaRicorrenteSpesa" var="ricorrente" status="stat">
															<label class="radio inline">
																<input type="radio" name="ricorrenteSpesa.uid" value="<s:property value="%{#ricorrente.uid}" />" <s:if test="%{ricorrenteSpesa.uid == #ricorrente.uid}">checked="checked"</s:if>>&nbsp;<s:property value="%{#ricorrente.descrizione}" />
															</label>
														</s:iterator>
													</div>
												</div>
											</s:if>
											<s:if test="perimetroSanitarioSpesaEditabile">
												<div class="control-group">
													<label for="perimetroSanitario" class="control-label">Codifica identificativo del perimetro sanitario</label>
													<div class="controls input-append">
														<s:select list="listaPerimetroSanitarioSpesa" id="perimetroSanitario" cssClass="span10"
																name="perimetroSanitarioSpesa.uid" headerKey="" headerValue=""
																listKey="uid" listValue="%{codice + '-' + descrizione}" />
													</div>
												</div>
											</s:if>
											<s:if test="transazioneUnioneEuropeaSpesaEditabile">
												<div class="control-group">
													<label for="transazioneUnioneEuropea" class="control-label">Codifica transazione UE</label>
													<div class="controls input-append">
														<s:select list="listaTransazioneUnioneEuropeaSpesa" id="transazioneUnioneEuropea" cssClass="span10"
																name="transazioneUnioneEuropeaSpesa.uid" headerKey="" headerValue=""
																listKey="uid" listValue="%{codice + '-' + descrizione}" />
													</div>
												</div>
											</s:if>
											<s:if test="politicheRegionaliUnitarieEditabile">
												<div class="control-group">
													<label for="politicheRegionaliUnitarie" class="control-label">Codifica politiche regionali unitarie</label>
													<div class="controls input-append">
														<s:select list="listaPoliticheRegionaliUnitarie" id="politicheRegionaliUnitarie" cssClass="span10"
																name="politicheRegionaliUnitarie.uid" headerKey="" headerValue=""
																listKey="uid" listValue="%{codice + '-' + descrizione}" />
													</div>
												</div>
											</s:if>
											<s:iterator var="idx" begin="1" end="%{numeroClassificatoriGenerici}">
												<s:if test="%{#attr['labelClassificatoreGenerico' + #idx] != null && #attr['classificatoreGenerico1' + #idx + 'Editabile']}">
													<div class="control-group">
														<label for="classificatoreGenerico<s:property value="%{#idx}"/>" class="control-label">
															<s:property value="%{#attr['labelClassificatoreGenerico' + #idx]}"/>
														</label>
														<div class="controls">
															<s:select list="%{#attr['listaClassificatoreGenerico' + #idx]}" id="classificatoreGenerico%{#idx}"
																cssClass="span10" name="%{'classificatoreGenerico' + #idx + '.uid'}" headerKey="" headerValue=""
																listKey="uid" listValue="%{codice + '-' + descrizione}" />
														</div>
													</div>
												</s:if>
											</s:iterator>
										</fieldset>
									</div>
								</div>
							</div>
						</div>
						<div class="accordion" id="accordion4">
							<div class="accordion-group">
								<div class="accordion-heading">
									<a class="accordion-toggle collapsed" data-toggle="collapse"
											data-parent="#accordion4" href="#collapsefour"> 
										Atti di legge <span class="icon">&nbsp;</span>
									</a>
								</div>
								<div id="collapsefour" class="accordion-body collapse">
									<div class="accordion-inner">
										<fieldset class="form-horizontal">
											<div class="control-group">
												<label class="control-label" for="attoDiLeggeAnno">Anno</label>
												<div class="controls">
													<s:textfield id="attoDiLeggeAnno" cssClass="lbTextSmall span2 soloNumeri" name="attoDiLegge.anno" maxlength="4" />
													<span class="al">
														<label class="radio inline" for="attoDiLeggeNumero">Numero</label>
													</span>
													<s:textfield id="attoDiLeggeNumero" cssClass="lbTextSmall span2 soloNumeri" maxlength="8" />
													<span class="al">
														<label class="radio inline" for="attoDiLeggeTipoAtto">Tipo</label>
													</span>
													<s:select list="listaTipoAtto" id="attoDiLeggeTipoAtto" cssClass="span3"
															name="attoDiLegge.tipoAtto.codice" 
															headerKey="" headerValue=""
															listKey="codice" listValue="%{codice + '-' + descrizione}"  />
												</div>
											</div>

										</fieldset>
									</div>
								</div>
							</div>
						</div>
						<p>
							<s:include value="/jsp/include/indietro.jsp" />
							<button type="button" class="btn reset">annulla</button>
							<s:submit cssClass="btn btn-primary pull-right" value="cerca"/>
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
					</s:form>
					<s:if test="siopeSpesaEditabile">
						<s:include value="/jsp/cap/modaleCompilazioneGuidataSIOPE.jsp">
							<s:param name="ricercaUrl">ricercaClassificatoreGerarchico_siopeSpesa.do</s:param>
							<s:param name="ajaxUrl">risultatiRicercaSiopeSpesaAjax.do</s:param>
						</s:include>
					</s:if>
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
	<script type="text/javascript" src="/siacbilapp/js/local/capitoloUscitaGestione/ricerca.js"></script>
</body>
</html>