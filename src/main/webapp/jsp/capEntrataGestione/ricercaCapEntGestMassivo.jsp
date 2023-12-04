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
					
					<h3>Ricerca Capitolo Entrata Gestione</h3>
					<s:form method="post" action="effettuaRicercaMassivaConOperazioniCapEntrataGestione">
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
													name="capitoloEntrataGestione.numeroCapitolo" maxlength="200" 
													placeholder="capitolo" value="${capitoloEntrataGestione.numeroCapitolo}" />
												<span class="al"> 
													<label class="radio inline" for="numeroArticolo">Articolo</label>
												</span> 
												<input type="text" id="numeroArticolo" class="lbTextSmall span2 soloNumeri" 
														name="capitoloEntrataGestione.numeroArticolo" maxlength="200"
														placeholder="articolo" value="${capitoloEntrataGestione.numeroArticolo}" />
											</div>
										</div>
										<div class="control-group">
											<label for="descrizioneCapitolo" class="control-label">Descrizione</label>
											<div class="controls">
												<textarea rows="5" cols="15" id="descrizioneCapitolo" maxlength="500"
														name="capitoloEntrataGestione.descrizione"
														class="span10">${capitoloEntrataGestione.descrizione}</textarea>
											</div>
										</div>
										<div class="control-group">
											<label for="descrizioneArticolo" class="control-label">Descrizione Articolo</label>
											<div class="controls">
												<textarea rows="5" cols="15" id="descrizioneArticolo" maxlength="500"
													name="capitoloEntrataGestione.descrizioneArticolo"
													class="span10">${capitoloEntrataGestione.descrizioneArticolo}</textarea>
											</div>
										</div>
										<s:if test="titoloEntrataEditabile">
											<div class="control-group">
												<label for="titoloEntrata" class="control-label">Titolo</label>
												<div class="controls">
													<s:select list="listaTitoloEntrata" id="titoloEntrata" cssClass="span10"
															name="titoloEntrata.uid" headerKey="" headerValue=""
															listKey="uid" listValue="%{codice + '-' + descrizione}" />
												</div>
											</div>
										</s:if>
										<s:if test="tipologiaTitoloEditabile">
											<div class="control-group">
												<label for="tipologiaTitolo" class="control-label">Tipologia 
														<a class="tooltip-test" title="selezionare prima il Titolo" href="#">
														<i class="icon-info-sign">&nbsp;
															<span class="nascosto">selezionare prima il Titolo</span>
														</i>
														</a>
												</label>
												<div class="controls">
													<s:select list="listaTipologiaTitolo" id="tipologiaTitolo" cssClass="span10"
													name="tipologiaTitolo.uid" headerKey="" headerValue="" disabled="%{titoloEntrata == null || titoloEntrata.uid == 0}"
													listKey="uid" listValue="%{codice + '-' + descrizione}" />
												</div>
											</div>
										</s:if>
										<s:if test="categoriaTipologiaTitoloEditabile">
											<div class="control-group">
												<label for="categoriaTipologiaTitolo" class="control-label">Categoria 
													<a class="tooltip-test" title="selezionare prima la Tipologia" href="#">
														<i class="icon-info-sign">&nbsp;
															<span class="nascosto">selezionare prima la Tipologia</span>
														</i>
													</a>
												</label>
												<div class="controls">
													<s:select list="listaCategoriaTipologiaTitolo" id="categoriaTipologiaTitolo" cssClass="span10"
													name="categoriaTipologiaTitolo.uid" headerKey="" headerValue="" disabled="%{tipologiaTitolo == null || tipologiaTitolo.uid == 0}"
													listKey="uid" listValue="%{codice + '-' + descrizione}" />
												</div>
											</div>
										</s:if>
										<%-- zTree --%>
										<s:if test="elementoPianoDeiContiEditabile">
											<div class="control-group">
												<label for="bottonePdC" class="control-label">
													<abbr title="Piano dei Conti">P.d.C.</abbr> finanziario 
													<a class="tooltip-test" title="selezionare prima la Categoria" href="#">
														<i class="icon-info-sign">&nbsp;
															<span class="nascosto">selezionare prima la Categoria</span>
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
														<a href="#myModal" role="button" class="btn" data-toggle="modal">
															Seleziona il Piano dei Conti &nbsp; 
															<i class="icon-spin icon-refresh spinner" id="SPINNER_ElementoPianoDeiConti"></i>
														</a>
													</s:if>
													<s:else>
														<a href="#myModal" role="button" class="btn" data-toggle="modal"
																disabled="disabled" id="bottonePdC">
															Seleziona il Piano dei Conti &nbsp; 
															<i class="icon-spin icon-refresh spinner" id="SPINNER_ElementoPianoDeiConti"></i>
														</a>
													</s:else>
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
										</s:if>
										
										<%-- zTree --%>
										<s:hidden id="HIDDEN_SIOPEEditabile" name="siopeEntrataEditabile" data-maintain=""/>
										<s:if test="siopeEntrataEditabile">
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
													<s:textfield id="siopeEntrata" name="siopeEntrata.codice" cssClass="span3" />
													&nbsp;<span id="descrizioneSiopeEntrata"><s:property value="siopeEntrata.descrizione"/></span>
													<s:hidden id="HIDDEN_idSiopeEntrata" name="siopeEntrata.uid" />
													<s:hidden id="HIDDEN_descrizioneSiopeEntrata" name="siopeEntrata.descrizione" />
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
											<s:if test="ricorrenteEntrataEditabile">
												<div class="control-group">
													<span class="control-label">Entrata</span>
													<div class="controls">
														<label class="radio inline">
														<input type="radio" name="ricorrenteEntrata.uid" value="" <s:if test='%{ricorrenteEntrata == null || ricorrenteEntrata.uid == ""}'>checked="checked"</s:if>>&nbsp;Non si applica
													</label>
														<s:iterator value="listaRicorrenteEntrata" var="ricorrente" status="stat">
															<label class="radio inline">
																<input type="radio" name="ricorrenteEntrata.uid" value="<s:property value="%{#ricorrente.uid}" />" <s:if test="%{ricorrenteEntrata.uid == #ricorrente.uid}">checked="checked"</s:if>>&nbsp;<s:property value="%{#ricorrente.descrizione}" />
															</label>
														</s:iterator>
													</div>
												</div>
											</s:if>
											<s:if test="perimetroSanitarioEntrataEditabile">
												<div class="control-group">
													<label for="perimetroSanitario" class="control-label">Codifica identificativo del perimetro sanitario</label>
													<div class="controls input-append">
														<s:select list="listaPerimetroSanitarioEntrata" id="perimetroSanitario" cssClass="span10"
																name="perimetroSanitarioEntrata.uid" headerKey="" headerValue=""
																listKey="uid" listValue="%{codice + '-' + descrizione}" />
													</div>
												</div>
											</s:if>
											<s:if test="transazioneUnioneEuropeaEntrataEditabile">
												<div class="control-group">
													<label for="transazioneUnioneEuropea" class="control-label">Codifica transazione UE</label>
													<div class="controls input-append">
														<s:select list="listaTransazioneUnioneEuropeaEntrata" id="transazioneUnioneEuropea" cssClass="span10"
																name="transazioneUnioneEuropeaEntrata.uid" headerKey="" headerValue=""
																listKey="uid" listValue="%{codice + '-' + descrizione}" />
													</div>
												</div>
											</s:if>
											<s:iterator var="idx" begin="36" end="%{lastIndexClassificatoriGenerici}">
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
													<s:textfield id="attoDiLeggeNumero" cssClass="lbTextSmall span2" name="attoDiLegge.numero soloNumeri" maxlength="8" />
													<span class="al">
														<label class="radio inline" for="attoDiLeggeTipoAtto">Tipo</label>
													</span>
													<s:select list="listaTipoAtto" id="attoDiLeggeTipoAtto" cssClass="span3"
															name="attoDiLegge.tipoAtto.codice" headerKey="" headerValue=""
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
					<s:if test="siopeEntrataEditabile">
						<s:include value="/jsp/cap/modaleCompilazioneGuidataSIOPE.jsp">
							<s:param name="ricercaUrl">ricercaClassificatoreGerarchico_siopeEntrata.do</s:param>
							<s:param name="ajaxUrl">risultatiRicercaSiopeEntrataAjax.do</s:param>
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
	<script type="text/javascript" src="/siacbilapp/js/local/capitolo/capitoloEntrata.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/capitoloEntrataGestione/ricerca.js"></script>
</body>
</html>