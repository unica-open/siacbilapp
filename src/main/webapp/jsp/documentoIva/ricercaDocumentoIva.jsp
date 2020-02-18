<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>

<%-- Inclusione head e CSS --%>
<s:include value="/jsp/include/head.jsp" />
</head>
<body>
	<%-- Inclusione header --%>
	<s:include value="/jsp/include/header.jsp" />
	<s:hidden id="HIDDEN_anno_datepicker" value="%{annoEsercizioInt}" />
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12 contentPage">
				<s:form cssClass="form-horizontal" action="effettuaRicercaDocumentoIva%{tipoSubdocumentoIva}" id="formRicercaDocumentoIva" novalidate="novalidate">
					<s:include value="/jsp/include/messaggi.jsp" />
					<h3>Ricerca Documenti iva di <s:property value="tipoSubdocumentoIvaTitolo" /></h3>
					<p>&Eacute; necessario inserire almeno un criterio di ricerca.</p>
					<div class="step-content">
						<div class="step-pane active" id="step1">
							<br>
							<p>
								<s:submit cssClass="btn btn-primary pull-right" value="cerca" />
							</p>
							<br>
							<h4>Dati documento di riferimento</h4>
							<fieldset class="form-horizontal">
								<div class="control-group">
									<label class="control-label" for="tipoDocumento">Tipo documento</label>
									<div class="controls">
										<s:select id="tipoDocumento" list="listaTipoDocumento" name="tipoDocumento.uid"
											cssClass="span6" required="true" headerKey="0" headerValue=""
											listKey="uid" listValue="%{codice + ' - ' + descrizione}" />
									</div>
								</div>
								<div class="control-group">
									<label class="control-label" for="annoDocumento">Anno</label>
									<div class="controls">										
										<s:textfield id="annoDocumento" name="documento.anno" placeholder="%{'anno esercizio'}" cssClass="lbTextSmall span2"
												maxlength="4" />
										<span class="alRight">
											<label class="radio inline" for="numeroDocumento">Numero</label>
										</span>
										<s:textfield id="numeroDocumento" name="documento.numero" placeholder="%{'numero documento'}" cssClass="lbTextSmall span2"
												maxlength="500" />
									</div>
								</div>
								<div class="control-group">
									<label class="control-label" for="dataEmissioneDocumento">Data emissione </label>
									<div class="controls">
										<s:textfield id="dataEmissioneDocumento" name="documento.dataEmissione"
											cssClass="lbTextSmall span2 datepicker" maxlength="500" />
									</div>
								</div>
								
								<h4 class="step-pane">Soggetto
									<span id="descrizioneCompletaSoggetto">
										<s:if test='%{soggetto != null && (soggetto.codice ne null && soggetto.codice != "") && (soggetto.descrizione ne null && soggetto.descrizione != "") && (soggetto.codiceFiscale ne null && soggetto.codiceFiscale != "")}'>
											<s:property value="%{soggetto.codice + ' - ' + soggetto.descrizione + ' - ' + soggetto.codiceFiscale}" />
										</s:if>
									</span>
								</h4>
								<fieldset class="form-horizontal">
									<div class="control-group">
										<label class="control-label" for="codiceSoggetto">Codice</label>
										<div class="controls">
											<s:textfield id="codiceSoggetto" name="soggetto.codiceSoggetto"
												cssClass="span2" maxlength="500" placeholder="%{'codice'}" />
											<span class="radio guidata">
												<button type="button" class="btn btn-primary" id="pulsanteCompilazioneSoggetto">compilazione guidata</button>
											</span>
										</div>
									</div>
								</fieldset>

								<!-- accordion --->
								<div class="step-pane active" id="datiIVA">
									<div class="accordion">
										<div class="accordion-group">
											<div class="accordion-heading">
												<a class="accordion-toggle collapsed" data-toggle="collapse" data-parent="#datiIVA" href="#datiIVAtab">
													Dati iva<span class="icon">&nbsp;</span>
												</a>
											</div>
											<div id="datiIVAtab" class="accordion-body collapse">
												<div class="accordion-inner">
													<fieldset class="form-horizontal">
														<s:if test="flagIntracomunitarioUtilizzabile">
															<div class="control-group">
																<label class="control-label" for="DocIntracomunitario">Documento intracomunitario</label>
																<div class="controls">																
																	<s:select id="DocIntracomunitario" list="#{null:'Non si applica', 'S':'Sì', 'N':'No'}"
																			name="flagIntracomunitario" cssClass="span2" />
																</div>
															</div>
														</s:if>
														<div class="control-group">
															<label class="control-label" for="Reg">N. registrazione</label>
															<div class="controls">
																<span class="alRight"> 
																	<label class="radio inline" for="DaReg">Da</label>
																</span>
																<s:textfield id="DaReg" name="progressivoIvaDa" cssClass="lbTextSmall span2 soloNumeri" maxlength="500" />
																<span class="alRight">
																	<label class="radio inline" for="AReg">A</label>
																</span>
																<s:textfield id="AReg" name="progressivoIvaA" cssClass="lbTextSmall span2 soloNumeri" maxlength="500" />
															</div>
														</div>
														<div class="control-group">
															<label class="control-label" for="tipoRegistrazioneIva">Tipo registrazione</label>
															<div class="controls">
																<s:select id="tipoRegistrazioneIva" list="listaTipoRegistrazioneIva"
																	cssClass="span6" name="tipoRegistrazioneIva.uid" headerKey="0" headerValue=""
																	listKey="uid" listValue="%{codice + ' - ' + descrizione}" />
															</div>
														</div>
														<div class="control-group">
															<label class="control-label" for="tipoRegistroIva">Tipo registro iva</label>
															<div class="controls">
																<select id="tipoRegistroIva" class="span6" name="tipoRegistroIva">
																	<option></option>
																	<s:iterator value="listaTipoRegistroIva" var="tri">
																		<option value="<s:property value="%{#tri}" />" data-tipo-esigibilita-iva="<s:property value="%{#tri.tipoEsigibilitaIva}" />"
																				<s:if test="%{#tri.equals(tipoRegistroIva)}">
																					selected
																				</s:if>>
																			<s:property value="%{#tri.codice}" /> - <s:property value="%{#tri.descrizione}" />
																		</option>
																	</s:iterator>
																</select>
															</div>
														</div>
														<div class="control-group">
															<label class="control-label" for="attivitaIva">Attivit&agrave;</label>
															<div class="controls">
																<select id="attivitaIva" name="attivitaIva.uid" class="span6">
																	<option value="0"></option>
																	<s:iterator value="listaAttivitaIva" var="ai">
																		<option value="<s:property value="%{#ai.uid}" />"
																				<s:if test="%{#ai.uid == attivitaIva.uid}">selected</s:if>
																				data-gruppo-attivita-iva="<s:property value="%{#ai.gruppoAttivitaIva.uid}" />"
																				data-flag-rilevante-irap="<s:property value="%{#ai.flagRilevanteIRAP}" />">
																			<s:property value="%{#ai.codice}" /> - <s:property value="%{#ai.descrizione}" />
																		</option>
																	</s:iterator>
																</select>
																<span class="alRight">
																	<label class="radio inline" for="flagRilevanteIRAP">Rilevante IRAP</label>
																</span>
																<s:select id="flagRilevanteIRAP" list="#{null:'Non si applica', 'S':'Sì', 'N':'No'}" name="flagRilevanteIrap" cssClass="span2" />
															</div>
														</div>

														<div class="control-group">
															<label class="control-label" for="registroIvaSubdocumentoIva">Registro</label>
															<div class="controls">
																<s:select id="registroIvaSubdocumentoIva" list="listaRegistroIva"
																	cssClass="span6" name="registroIva.uid" headerKey="0" headerValue=""
																	listKey="uid" listValue="%{codice + ' - ' + descrizione}" data-overlay=""/>
																</div>
														</div>
														<div id="ricercaProtocolloProvvisorio" class="control-group">
															<label class="control-label" for="numeroProtocolloProvvisorioDa">Protocollo provvisorio</label>
															<div class="controls">
																<span class="alRight">
																	<label class="radio inline" for="numeroProtocolloProvvisorioDa">Da</label>
																</span>
																<s:textfield id="numeroProtocolloProvvisorioDa" name="numeroProtocolloProvvisorioDa"
																	cssClass="lbTextSmall span2 soloNumeri" maxlength="500" />
																<span class="alRight">
																	<label class="radio inline" for="numeroProtocolloProvvisorioA">A</label>
																</span>
																<s:textfield id="numeroProtocolloProvvisorioA" name="numeroProtocolloProvvisorioA"
																	cssClass="lbTextSmall span2 soloNumeri" maxlength="500" />
															</div>
														</div>

														<div id="ricercaDataProtocolloProvvisorio" class="control-group">
															<label class="control-label" for="dataProtocolloProvvisorioDa">Data protocollo provvisorio</label>
															<div class="controls">
																<span class="alRight"> 
																	<label class="radio inline" for="dataProtocolloProvvisorioDa">Da</label>
																</span>
																<s:textfield id="dataProtocolloProvvisorioDa" name="dataProtocolloProvvisorioDa"
																	placeholder="%{'Da'}" cssClass="lbTextSmall span2 datepicker" />
																<span class="alRight">
																	<label class="radio inline" for="dataProtocolloProvvisorioA">A</label>
																</span>
																<s:textfield id="dataProtocolloProvvisorioA" name="dataProtocolloProvvisorioA"
																	placeholder="%{'A'}" cssClass="lbTextSmall span2 datepicker" />
															</div>
														</div>

														<div id="ricercaProtocolloDefinitivo" class="control-group">
															<label class="control-label" for="numeroProtocolloDefinitivoDa">Protocollo definitivo</label>
															<div class="controls">
																<span class="alRight">
																	<label class="radio inline" for="numeroProtocolloDefinitivoDa">Da</label>
																</span>
																<s:textfield id="numeroProtocolloDefinitivoDa" name="numeroProtocolloDefinitivoDa"
																	cssClass="lbTextSmall span2 soloNumeri" maxlength="500" />
																<span class="alRight">
																	<label class="radio inline" for="numeroProtocolloDefinitivoA">A</label>
																</span>
																<s:textfield id="numeroProtocolloDefinitivoA" name="numeroProtocolloDefinitivoA"
																	cssClass="lbTextSmall span2 soloNumeri" maxlength="500" />
															</div>
														</div>

														<div id="ricercaDataProtocolloDefinitivo" class="control-group">
															<label class="control-label" for="dataProtocolloDefinitivoDa">Data protocollo definitivo</label>
															<div class="controls">
																<span class="alRight">
																	<label class="radio inline" for="dataProtocolloDefinitivoDa">Da</label>
																</span>
																<s:textfield id="dataProtocolloDefinitivoDa" name="dataProtocolloDefinitivoDa"
																	placeholder="%{'Da'}" cssClass="lbTextSmall span2 datepicker" />
																<span class="alRight">
																	<label class="radio inline" for="dataProtocolloDefinitivoA">A</label>
																</span>
																<s:textfield id="dataProtocolloDefinitivoA" name="dataProtocolloDefinitivoA"
																	placeholder="%{'Da'}" cssClass="lbTextSmall span2 datepicker" />
															</div>
														</div>

													</fieldset>
												</div>
											</div>
										</div>
									</div>
								</div>
								<!-- end accordion --->
							</fieldset>
							<s:include value="/jsp/soggetto/selezionaSoggetto_modale.jsp" />
						</div>
					</div>
					<p class="margin-medium">
						<s:include value="/jsp/include/indietro.jsp" />
						<button type="button" class="btn reset">annulla</button>
						<s:submit cssClass="btn btn-primary pull-right" value="cerca" />
					</p>
				</s:form>
			</div>
		</div>
	</div>

	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="${jspath}codiceFiscale.js"></script>
	<script type="text/javascript" src="${jspath}soggetto/ricerca.js"></script>
	<script type="text/javascript" src="${jspath}documentoIva/ricerca.js"></script>

</body>
</html>