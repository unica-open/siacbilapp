<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
						<form method="post" action="aggiornamentoMassivoCapEntrataPrevisione.do" id="aggiornamentoCapEntrataPrevisione"
								name="formAggiornamento" novalidate="novalidate">
							<s:include value="/jsp/include/messaggi.jsp" />
							<h3>
								Capitolo
								<s:property value="capitoloEntrataPrevisione.numeroCapitolo" />
								/
								<s:property value="capitoloEntrataPrevisione.numeroArticolo" />
							</h3>
							<ul class="nav nav-tabs">
								<li class="active"><a href="#">Capitolo</a></li>
							</ul>
							<h3>Aggiorna Capitolo</h3>
							<p>
								<small>I campi contrassegnati con l'asterisco (*) sono
									obbligatori.</small>
							</p>
							<s:hidden name="capitoloEntrataPrevisione.uid" value="%{uidDaAggiornare}" />
							<s:hidden name="capitoloEntrataPrevisione.statoOperativoElementoDiBilancio" />
							<s:hidden name="capitoloEntrataPrevisione.stato" />
							<s:hidden name="capitoloEntrataPrevisione.annoCapitolo" />
							<s:hidden name="bilancio.uid" />
							<s:hidden name="annoCapitoloDaAggiornare" />
							<s:hidden name="numeroCapitoloDaAggiornare" />
							<s:hidden name="numeroArticoloDaAggiornare" />
							
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
											value="${capitoloEntrataPrevisione.numeroCapitolo}" />
										<s:hidden name="capitoloEntrataPrevisione.numeroCapitolo"
											value="%{capitoloEntrataPrevisione.numeroCapitolo}" />
										<span class="al"> <label class="radio inline" for="numeroArticolo">Articolo
												*</label>
										</span> <input type="text" id="numeroArticolo" class="lbTextSmall span2"
											maxlength="200" disabled="disabled"
											value="${capitoloEntrataPrevisione.numeroArticolo}" />
										<s:hidden name="capitoloEntrataPrevisione.numeroArticolo"
											value="%{capitoloEntrataPrevisione.numeroArticolo}" />
									</div>
								</div>
	
								<%-- DATI secondari --%>
								<s:if test="descrizioneEditabile">
									<div class="control-group">
										<label for="descrizioneCapitolo" class="control-label">Descrizione *</label>
										<div class="controls">
											<s:textarea id="descrizioneCapitolo" name="capitoloEntrataPrevisione.descrizione" cssClass="span10"
													required="true" maxlength="500" rows="5" cols="15" readonly="%{!descrizioneEditabile}"></s:textarea>
										</div>
									</div>
								</s:if><s:else>
									<s:hidden name="capitoloEntrataPrevisione.descrizione" />
								</s:else>
								<s:if test="descrizioneArticoloEditabile">
									<div class="control-group">
										<label for="descrizioneArticolo" class="control-label">Descrizione Articolo</label>
										<div class="controls">
											<s:textarea id="descrizioneArticolo" name="capitoloEntrataPrevisione.descrizioneArticolo" cssClass="span10"
													required="true" maxlength="500" rows="5" cols="15"></s:textarea>
										</div>
									</div>
								</s:if><s:else>
									<s:hidden name="capitoloEntrataPrevisione.descrizioneArticolo" />
								</s:else>
								<%-- Primo gruppo collegato --%>
								<s:hidden id="HIDDEN_codiceTitolo" name="codiceTitolo" />
								<s:if test="titoloEntrataEditabile">
									<div class="control-group">
										<label for="titoloEntrata" class="control-label">Titolo *</label>
										<div class="controls">
											<s:select list="listaTitoloEntrata" id="titoloEntrata" name="titoloEntrata.uid" required="true" cssClass="span10"
												headerKey="" headerValue="" listKey="uid" listValue="%{codice + '-' + descrizione}"
												data-original-uid="%{titoloEntrata.uid}" />
										</div>
									</div>
								</s:if><s:else>
									<s:hidden name="titoloEntrata.uid" />
								</s:else>
								<s:if test="tipologiaTitoloEditabile">
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
												name="tipologiaTitolo.uid" headerKey="" headerValue="" disabled="%{titoloEntrata == null || titoloEntrata.uid == 0}"
												listKey="uid" listValue="%{codice + '-' + descrizione}"
												data-original-uid="%{tipologiaTitolo.uid}" />
										</div>
									</div>
								</s:if><s:else>
									<s:hidden name="tipologiaTitolo.uid" />
								</s:else>
								<s:if test="categoriaTipologiaTitoloEditabile">
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
												name="categoriaTipologiaTitolo.uid" headerKey="" headerValue=""
												disabled="%{tipologiaTitolo == null || tipologiaTitolo.uid == 0}"
												listKey="uid" listValue="%{codice + '-' + descrizione}"
												data-original-uid="%{categoriaTipologiaTitolo.uid}" />
										</div>
									</div>
								</s:if><s:else>
									<s:hidden name="categoriaTipologiaTitolo.uid" />
								</s:else>
								<%-- zTree --%>
								<s:hidden id="HIDDEN_ElementoPianoDeiContiEditabile" name="elementoPianoDeiContiEditabile" />
								<s:if test="elementoPianoDeiContiEditabile">
									<div class="control-group">
										<label for="bottonePdC" class="control-label"> <abbr title="Piano dei Conti">P.d.C.</abbr> finanziario * 
											<a class="tooltip-test" title="selezionare prima la Categoria" href="#">
												<i class="icon-info-sign">&nbsp; <span class="nascosto">selezionare prima la Categoria</span></i>
											</a>
										</label>
										<div class="controls">
											<s:hidden id="HIDDEN_ElementoPianoDeiContiUid" name="elementoPianoDeiConti.uid"
												data-original-uid="%{elementoPianoDeiConti.uid}" />
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
								</s:if>
								<%-- zTree --%>
								<s:hidden id="HIDDEN_SIOPEEditabile" name="siopeEntrataEditabile"/>
								<s:if test='siopeEntrataEditabile' >
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
											<s:hidden id="HIDDEN_SIOPEEditabile" name="siopeEditabile"/>
											<a href="#modaleSIOPE" role="button" class="btn" data-toggle="modal" id="bottoneSIOPE">
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
											<span class="radio guidata">
												<button type="button" class="btn btn-primary" id="compilazioneGuidataSIOPE">compilazione guidata</button>
											</span>
										</div>
									</div>
								</s:if>
								<%-- zTree --%>
								<s:hidden id="HIDDEN_StrutturaAmministrativoContabileEditabile" name="strutturaAmministrativoContabileEditabile" />
								<s:if test="strutturaAmministrativoContabileEditabile">
									<div class="control-group">
										<label for="bottoneSAC" class="control-label"> Struttura Amministrativa Responsabile * </label>
										<div class="controls">
											<s:hidden id="HIDDEN_StrutturaAmministrativoContabileUid" name="strutturaAmministrativoContabile.uid"
												data-original-uid="%{strutturaAmministrativoContabile.uid}" />
											<s:hidden id="HIDDEN_StrutturaAmministrativoContabileStringa" name="strutturaAmministrativoResponsabile" />										
											<s:hidden id="HIDDEN_StrutturaAmministrativoContabileDescrizione" name="strutturaAmministrativoContabile.descrizione" />
											<a href="#struttAmm" role="button" class="btn" id="bottoneSAC"
													data-toggle="modal">
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
								</s:if>
								<s:if test="categoriaCapitoloEditabile">
									<div class="control-group">
										<label for="categoriaCapitolo" class="control-label">Tipo capitolo</label>
										<div class="controls">
											<select id="categoriaCapitolo" name="capitoloEntrataPrevisione.categoriaCapitolo.uid" class="span10">
												<option></option>
												<s:iterator value="listaCategoriaCapitolo" var="cc">
													<option data-codice="<s:property value="#cc.codice" />" value="<s:property value="#cc.uid"/>" <s:if test="%{capitoloEntrataPrevisione.categoriaCapitolo.uid == #cc.uid}">selected</s:if>>
														<s:property value="%{#cc.codice + ' - ' + #cc.descrizione}"/>
													</option>
												</s:iterator>
											</select>
										</div>
									</div>
								</s:if><s:else>
									<s:hidden name="capitoloEntrataPrevisione.categoriaCapitolo.uid" />
								</s:else>
								<s:if test="flagImpegnabileEditabile">
									<div class="control-group">
										<label for="flagImpegnabile" class="control-label">Accertabile</label>
										<div class="controls">
											<s:checkbox id="flagImpegnabile" name="capitoloEntrataPrevisione.flagImpegnabile" />
										</div>
									</div>
								</s:if><s:else>
									<s:hidden name="capitoloEntrataPrevisione.flagImpegnabile" />
								</s:else>
								<s:if test="flagAccertatoPerCassaEditabile">
									<div class="control-group">
										<label for="flagAccertatoPerCassa" class="control-label">Accertabile</label>
										<div class="controls">
											<s:checkbox id="flagAccertatoPerCassa" name="capitoloEntrataPrevisione.flagAccertatoPerCassa" />
										</div>
									</div>
								</s:if><s:else>
									<s:hidden name="capitoloEntrataPrevisione.flagAccertatoPerCassa" />
								</s:else>
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
												<s:if test="tipoFinanziamentoEditabile">
													<div class="control-group">
														<label for="tipoFinanziamento" class="control-label">Tipo Finanziamento</label>
														<div class="controls input-append">
															<s:select id="tipoFinanziamento" name="tipoFinanziamento.uid"
																list="listaTipoFinanziamento" cssClass="span10"
																headerKey="" headerValue=""
																value="%{tipoFinanziamento.uid}" listKey="uid"
																listValue="%{codice + '-' + descrizione}" />
														</div>
													</div>
												</s:if><s:else>
													<s:hidden name="tipoFinanziamento.uid" />
												</s:else>
												<s:if test="flagRilevanteIvaEditabile">
													<div class="control-group">
														<label for="rilevanteIva" class="control-label">
															Rilevante IVA
														</label>
														<div class="controls">
															<s:checkbox id="rilevanteIva"
																	name="capitoloEntrataPrevisione.flagRilevanteIva" />
														</div>
													</div>
												</s:if><s:else>
													<s:hidden name="capitoloEntrataPrevisione.flagRilevanteIva" />
												</s:else>
												<s:if test="flagPerMemoriaEditabile">
													<div class="control-group">
														<label for="memoria" class="control-label">Corsivo per memoria</label>
														<div class="controls">
															<s:checkbox id="memoria"
																name="capitoloEntrataPrevisione.flagPerMemoria" />
														</div>
													</div>
												</s:if><s:else>
													<s:hidden name="capitoloEntrataPrevisione.flagPerMemoria" />
												</s:else>
												<s:if test="tipoFondoEditabile">
													<div class="control-group">
														<label for="tipoFondo" class="control-label">Tipo fondo</label>
														<div class="controls input-append">
															<s:select id="tipoFondo" name="tipoFondo.uid"
																list="listaTipoFondo" cssClass="span10" headerKey=""
																headerValue="" value="%{tipoFondo.uid}" listKey="uid"
																listValue="%{codice + '-' + descrizione}" />
														</div>
													</div>
												</s:if><s:else>
													<s:hidden name="tipoFondo.uid" />
												</s:else>
												<s:if test="ricorrenteEditabile">
													<div class="control-group">
														<span class="control-label"></span>
														<div class="controls">
															<s:iterator value="listaRicorrente" var="ricorrente" status="stat">
																<label class="radio inline">
																	<input type="radio" name="ricorrente.uid" value="<s:property value="%{#ricorrente.uid}" />" <s:if test="%{ricorrente.uid == #ricorrente.uid}">checked="checked"</s:if>>&nbsp;<s:property value="%{#ricorrente.descrizione}" />
																</label>
															</s:iterator>
														</div>
													</div>
												</s:if><s:else>
													<s:hidden name="ricorrente.uid" />
												</s:else>
												<s:if test="perimetroSanitarioEditabile">
													<div class="control-group">
														<label for="perimetroSanitario" class="control-label">Codifica identificativo del perimetro sanitario</label>
														<div class="controls input-append">
															<s:select list="listaPerimetroSanitario" id="perimetroSanitario" cssClass="span10"
																	name="perimetroSanitario.uid" headerKey="" headerValue=""
																	listKey="uid" listValue="%{codice + '-' + descrizione}" />
														</div>
													</div>
												</s:if><s:else>
													<s:hidden name="perimetroSanitario.uid" />
												</s:else>
												<s:if test="transazioneUnioneEuropeaEditabile">
													<div class="control-group">
														<label for="transazioneUnioneEuropea" class="control-label">Codifica transazione UE</label>
														<div class="controls input-append">
															<s:select list="listaTransazioneUnioneEuropea" id="transazioneUnioneEuropea" cssClass="span10"
																	name="transazioneUnioneEuropea.uid" headerKey="" headerValue=""
																	listKey="uid" listValue="%{codice + '-' + descrizione}" />
														</div>
													</div>
												</s:if><s:else>
													<s:hidden name="transazioneUnioneEuropea.uid" />
												</s:else>
												<%-- Classificatori Generici --%>
												<s:iterator var="idx" begin="1" end="%{numeroClassificatoriGenerici}">
													<s:if test="%{#attr['labelClassificatoreGenerico' + #idx] != null && #attr['classificatoreGenerico' + #idx + 'Editabile']}">
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
													</s:if><s:else>
														<s:hidden name="%{'classificatoreGenerico' + #idx + '.uid'}" />
													</s:else>
												</s:iterator>
												<s:if test="noteEditabile">
													<div class="control-group">
														<label for="note" class="control-label">Note</label>
														<div class="controls">
															<s:textarea id="note" name="capitoloEntrataPrevisione.note" rows="5" cols="15"></s:textarea>
														</div>
													</div>
												</s:if><s:else>
													<s:hidden name="capitoloEntrataPrevisione.note" />
												</s:else>
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
							<p>
								<s:include value="/jsp/include/indietro.jsp" />
								<button type="button" class="btn reset">annulla</button>
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
						<s:include value="/jsp/cap/modaleCompilazioneGuidataSIOPE.jsp">
							<s:param name="ricercaUrl">ricercaClassificatoreGerarchico_siopeEntrata.do</s:param>
							<s:param name="ajaxUrl">risultatiRicercaSiopeEntrataAjax.do</s:param>
						</s:include>
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
	<script type="text/javascript" src="${jspath}capitolo/capitoloEntrata.js"></script>
	<script type="text/javascript" src="${jspath}capitoloEntrataPrevisione/aggiorna.js"></script>
</body>
</html>