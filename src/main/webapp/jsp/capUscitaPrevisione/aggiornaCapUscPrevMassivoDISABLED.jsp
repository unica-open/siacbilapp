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
						<form method="post" action="aggiornaMassivaCapUscitaPrevisione.do" novalidate="novalidate">
							<h3>
								Capitolo&nbsp;<s:property value="capitoloUscitaPrevisione.numeroCapitolo" />&nbsp;/&nbsp;<s:property value="capitoloUscitaPrevisione.numeroArticolo" />
							</h3>
							<ul class="nav nav-tabs">
								<li class="active"><a href="#">Capitolo</a></li>
							</ul>
							<s:include value="/jsp/include/messaggi.jsp" />
							<h3>Aggiorna Capitolo</h3>
							<s:hidden name="annoCapitoloDaAggiornare" />
							<s:hidden name="numeroCapitoloDaAggiornare" />
							<s:hidden name="numeroArticoloDaAggiornare" />
							<p>
								<small>I campi contrassegnati con l'asterisco (*) sono obbligatori.</small>
							</p>
							<fieldset class="form-horizontal">
								<div class="control-group">
									<label class="control-label" for="annoCapitolo">Anno</label>
									<div class="controls">
										<input type="text" id="annoCapitolo" class="lbTextSmall span2" value="${annoEsercizio}" disabled="disabled" maxlength="4" />
										<span class="al">
											<label class="radio inline" for="numeroCapitolo">Capitolo *</label>
										</span>
										<input type="text" id="numeroCapitolo" class="lbTextSmall span2" maxlength="200" disabled="disabled" value="${capitoloUscitaPrevisione.numeroCapitolo}" />
										<span class="al">
											<label class="radio inline" for="numeroArticolo">Articolo *</label>
										</span>
										<input type="text" id="numeroArticolo" class="lbTextSmall span2" maxlength="200" disabled="disabled" value="${capitoloUscitaPrevisione.numeroArticolo}" />
									</div>
								</div>
	
								<%-- DATI secondari --%>
								<s:if test="descrizioneEditabile">
									<div class="control-group">
										<label for="descrizioneCapitolo" class="control-label">Descrizione *</label>
										<div class="controls">
											<s:textarea rows="5" cols="15" id="descrizioneCapitolo" name="capitoloUscitaPrevisione.descrizione" cssClass="span10" disabled="true"></s:textarea>
										</div>
									</div>
								</s:if>
								<s:if test="descrizioneArticoloEditabile">
									<div class="control-group">
										<label for="descrizioneArticolo" class="control-label">Descrizione Articolo</label>
										<div class="controls">
											<s:textarea rows="5" cols="15" id="descrizioneArticolo" name="capitoloUscitaPrevisione.descrizioneArticolo" cssClass="span10" disabled="true"></s:textarea>
										</div>
									</div>
								</s:if> 
								<%-- Primo gruppo collegato --%>
								<s:if test="missioneEditabile">
									<div class="control-group">
										<label for="missione" class="control-label">Missione *</label>
										<div class="controls">
											<s:select id="missione" list="listaMissione" name="missione.uid" cssClass="span10" required="required" headerKey="" headerValue="" listKey="uid"
													listValue="%{codice + '-' + descrizione}" disabled="true" />
										</div>
									</div>
								</s:if>
								<s:if test="programmaEditabile">
									<div class="control-group">
										<label for="programma" class="control-label">Programma *
											<a class="tooltip-test" title="selezionare prima la Missione" href="#">
												<i class="icon-info-sign">&nbsp; <span class="nascosto">selezionare prima la Missione</span></i>
											</a>
										</label>
										<div class="controls">
											<s:if test="%{listaProgramma != null}">
												<s:select id="programma" list="listaProgramma" name="programma.uid" cssClass="span10" required="required" headerKey="" headerValue="" listKey="uid"
													listValue="%{codice + '-' + descrizione}" disabled="true" />
											</s:if><s:else>
												<select id="programma" name="programma.uid" class="span10" required="required" disabled="disabled"></select>
											</s:else>
										</div>
									</div>
								</s:if>
								<s:if test="classificazioneCofogEditabile">
									<div class="control-group">
										<label for="classificazioneCofog" class="control-label">Cofog
											<a class="tooltip-test" title="selezionare prima il Programma" href="#">
												<i class="icon-info-sign">&nbsp; <span class="nascosto">selezionare prima il Programma</span></i>
											</a>
										</label>
										<div class="controls">
											<s:if test="%{listaClassificazioneCofog != null}">
												<s:select id="classificazioneCofog" list="listaClassificazioneCofog" name="classificazioneCofog.uid" cssClass="span10" headerKey="" headerValue="" listKey="uid"
														listValue="%{codice + '-' + descrizione}" disabled="true" />
											</s:if><s:else>
												<select id="classificazioneCofog" name="classificazioneCofog.uid" class="span10" required="required" disabled="disabled"></select>
											</s:else>
										</div>
									</div>
								</s:if>
								<%-- Secondo gruppo collegato --%>
								<s:if test="titoloSpesaEditabile">
									<div class="control-group">
											<label for="titoloSpesa" class="control-label">Titolo *
												<a class="tooltip-test" title="selezionare prima il Programma" href="#">
													<i class="icon-info-sign">&nbsp;
														<span class="nascosto">selezionare prima il Programma</span>
													</i>
												</a>
											</label>
											<div class="controls">
											<s:select id="titoloSpesa" list="listaTitoloSpesa" name="titoloSpesa.uid" cssClass="span10" required="required" headerKey="" headerValue="" listKey="uid"
												listValue="%{codice + '-' + descrizione}" disabled="true" />
										</div>
									</div>
								</s:if>
								<s:if test="macroaggregatoEditabile">
									<div class="control-group">
										<label for="macroaggregato" class="control-label">Macroaggregato *
											<a class="tooltip-test" title="selezionare prima il Titolo" href="#">
												<i class="icon-info-sign">&nbsp; <span class="nascosto">selezionare prima il Titolo</span></i>
											</a>
										</label>
										<div class="controls">
											<s:if test="%{listaMacroaggregato != null}">
												<s:select id="macroaggregato" list="listaMacroaggregato" name="macroaggregato.uid" cssClass="span10" required="required" headerKey="" headerValue="" listKey="uid"
													listValue="%{codice + '-' + descrizione}" disabled="true" />
											</s:if><s:else>
												<select id="macroaggregato" name="macroaggregato.uid" class="span10" required="required" disabled="disabled"></select>
											</s:else>
										</div>
									</div>
								</s:if>
								<%-- zTree --%>
								<s:if test="elementoPianoDeiContiEditabile">
									<div class="control-group">
										<label for="bottonePdC" class="control-label">
											<abbr title="Piano dei Conti">P.d.C.</abbr> finanziario * 
											<a class="tooltip-test" title="selezionare prima il Macroaggregato" href="#">
												<i class="icon-info-sign">&nbsp; <span class="nascosto">selezionare prima il Macroaggregato</span></i>
											</a>
										</label>
										<div class="controls">
											<s:property value="pdcFinanziario"/>
										</div>
									</div>
								</s:if>
								<%-- zTree --%>
								<s:hidden id="HIDDEN_SIOPEEditabile" name="siopeSpesaEditabile"/>
								<s:if test="siopeSpesaEditabile">
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
								</s:if>
								<%-- zTree --%>
								<s:if test="strutturaAmministrativoContabileEditabile">
									<div class="control-group">
										<label for="bottoneSAC" class="control-label"> Struttura Amministrativa Responsabile * </label>
										<div class="controls">
											<s:property value="strutturaAmministrativoResponsabile"/>
										</div>
									</div>
								</s:if>
								<s:if test="categoriaCapitoloEditabile">
									<div class="control-group">
										<label for="categoriaCapitolo" class="control-label">Tipo capitolo</label>
										<div class="controls">
											<s:select id="categoriaCapitolo" listKey="uid" list="listaCategoriaCapitolo" name="capitoloUscitaPrevisione.categoriaCapitolo.uid" cssClass="span10"
												listValue="%{codice + '-' + descrizione}" disabled="true" headerKey="" headerValue="" />
										</div>
									</div>
								</s:if>
								<s:if test="flagImpegnabileEditabile">
									<div class="control-group">
										<label for="flagImpegnabile" class="control-label">Impegnabile</label>
										<div class="controls">
											<s:checkbox id="flagImpegnabile" name="capitoloUscitaPrevisione.flagImpegnabile" disabled="true" data-editabile="false" />
										</div>
									</div>
								</s:if>
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
												<s:if test="flagPerMemoriaEditabile">
													<div class="control-group">
														<label for="memoria" class="control-label">Corsivo per memoria</label>
														<div class="controls">
															<s:checkbox id="memoria" name="capitoloUscitaPrevisione.flagPerMemoria" disabled="true" />
														</div>
													</div>
												</s:if>
												<s:if test="tipoFinanziamentoEditabile">
													<div class="control-group">
														<label for="tipoFinanziamento" class="control-label">Tipo Finanziamento</label>
														<div class="controls input-append">
															<s:select id="tipoFinanziamento" name="tipoFinanziamento.uid" list="listaTipoFinanziamento" cssClass="span10" headerKey="" headerValue=""
																value="%{tipoFinanziamento.uid}" listKey="uid" listValue="%{codice + '-' + descrizione}" disabled="true" />
														</div>
													</div>
												</s:if>
												<s:if test="flagRilevanteIvaEditabile">
													<div class="control-group">
														<label for="rilevanteIva" class="control-label">Rilevante IVA</label>
														<div class="controls">
															<s:checkbox id="rilevanteIva" name="capitoloUscitaPrevisione.flagRilevanteIva" disabled="true" />
														</div>
													</div>
												</s:if>
												<s:if test="flagFunzioniDelegateRegioneEditabile">
													<div class="control-group">
														<label for="delegate" class="control-label">
															Funzioni delegate dalla Regione </label>
														<div class="controls">
															<s:checkbox id="delegate" name="capitoloUscitaPrevisione.funzDelegateRegione" disabled="true" />
														</div>
													</div>
												</s:if>
												<s:if test="tipoFondoEditabile">
													<div class="control-group">
														<label for="tipoFondo" class="control-label">Tipo fondo</label>
														<div class="controls input-append">
															<s:select id="tipoFondo" name="tipoFondo.uid" list="listaTipoFondo" cssClass="span10" headerKey="" headerValue="" value="%{tipoFondo.uid}" listKey="uid"
																listValue="%{codice + '-' + descrizione}" disabled="true" />
														</div>
													</div>
												</s:if><s:else>
													<s:hidden name="tipoFondo.uid" />
												</s:else>
												<s:if test="ricorrenteSpesaEditabile">
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
												</s:if><s:else>
													<s:hidden name="ricorrenteSpesa.uid" />
												</s:else>
												<s:if test="perimetroSanitarioSpesaEditabile">
													<div class="control-group">
														<label for="perimetroSanitario" class="control-label">Codifica identificativo del perimetro sanitario</label>
														<div class="controls input-append">
															<s:select list="listaPerimetroSanitarioSpesa" id="perimetroSanitario" cssClass="span10"
																	name="perimetroSanitarioSpesa.uid" headerKey="" headerValue="" disabled="true"
																	listKey="uid" listValue="%{codice + '-' + descrizione}" />
														</div>
													</div>
												</s:if><s:else>
													<s:hidden name="perimetroSanitarioSpesa.uid" />
												</s:else>
												<s:if test="transazioneUnioneEuropeaSpesaEditabile">
													<div class="control-group">
														<label for="transazioneUnioneEuropea" class="control-label">Codifica transazione UE</label>
														<div class="controls input-append">
															<s:select list="listaTransazioneUnioneEuropeaSpesa" id="transazioneUnioneEuropea" cssClass="span10"
																	name="transazioneUnioneEuropeaSpesa.uid" headerKey="" headerValue="" disabled="true"
																	listKey="uid" listValue="%{codice + '-' + descrizione}" />
														</div>
													</div>
												</s:if><s:else>
													<s:hidden name="transazioneUnioneEuropeaSpesa.uid" />
												</s:else>
												<s:if test="politicheRegionaliUnitarieEditabile">
													<div class="control-group">
														<label for="politicheRegionaliUnitarie" class="control-label">Codifica politiche regionali unitarie</label>
														<div class="controls input-append">
															<s:select list="listaPoliticheRegionaliUnitarie" id="politicheRegionaliUnitarie" cssClass="span10"
																	name="politicheRegionaliUnitarie.uid" headerKey="" headerValue="" disabled="true"
																	listKey="uid" listValue="%{codice + '-' + descrizione}" />
														</div>
													</div>
												</s:if><s:else>
													<s:hidden name="politicheRegionaliUnitarie.uid" />
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
																	disabled="true" listKey="uid" listValue="%{codice + '-' + descrizione}" />
															</div>
														</div>
													</s:if>
												</s:iterator>
												<s:if test="noteEditabile">
													<div class="control-group">
														<label for="note" class="control-label">Note</label>
														<div class="controls">
															<s:textarea id="note" name="capitoloUscitaPrevisione.note" rows="5" cols="15" disabled="true"></s:textarea>
														</div>
													</div>
												</s:if>
											</fieldset>
										</div>
									</div>
								</div>
							</div>
							<p>
								<s:include value="/jsp/include/indietro.jsp" />
								<s:submit cssClass="btn btn-primary pull-right" value="aggiorna"/>
							</p>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>

	<%-- Caricamento del footer --%>
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
</body>
</html>