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
			<div class="span12 contentPage">
				<s:hidden name="tipoSubdocumentoIva" id="HIDDEN_tipoSubdocumentoIva" />
				<s:hidden name="movimentoResiduo" id="HIDDEN_isMovimentoResiduo" />
				<s:form cssClass="form-horizontal" action="inserimentoDocumentoIva%{tipoSubdocumentoIva}"
						id="formInserimentoDocumentoIva%{tipoSubdocumentoIva}" novalidate="novalidate" >
					<s:include value="/jsp/include/messaggi.jsp" />
					<h3>Inserimento documento iva di <s:property value="tipoSubdocumentoIvaTitolo" /></h3>
					<h4>
						<s:property value="documento.tipoDocumento.codice"/> <s:property value="documento.anno"/> - <s:property value="documento.numero" /> - 
						<s:property value="documento.dataEmissione" /> - <s:property value="soggetto.codiceSoggetto"/> - <s:property value="soggetto.denominazione" />
					</h4>
					<s:hidden name="registrazioneSenzaProtocollo" id="HIDDEN_isSenzaProtocollo" />
					<div id="datiAnagraficaDocumento" class="step-pane active">
						<div class="accordion">
							<div class="accordion-group">
								<div class="accordion-heading">
									<a href="#collapseDatiAnagraficaDocumento" data-parent="#datiAnagraficaDocumento" data-toggle="collapse"
											class="accordion-toggle collapsed">
										Dati principali<span class="icon">&nbsp;</span>
									</a>
								</div>
								<div class="accordion-body collapse" id="collapseDatiAnagraficaDocumento">
									<div class="accordion-inner">
										<fieldset class="form-horizontal">
											<div class="boxOrInLeft">
												<p>Dati anagrafica</p>
												<ul class="htmlelt">
													<li>
														<dfn>Data emissione</dfn>
														<dl><s:property value="documento.dataEmissione" />&nbsp;</dl>
													</li>
													<li>
														<dfn>Data scadenza</dfn>
														<dl><s:property value="documento.dataScadenza" />&nbsp;</dl>
													</li>
													<li>
														<dfn>Codice fiscale</dfn>
														<dl><s:property value="soggetto.codiceFiscale"/>&nbsp;</dl>
													</li>
													<li>
														<dfn>Partita iva</dfn>
														<dl><s:property value="soggetto.partitaIva"/>&nbsp;</dl>
													</li>
													<li>
														<dfn>Descrizione</dfn>
														<dl><s:property value="documento.descrizione" />&nbsp;</dl>
													</li>
													<li>
														<dfn>Sede secondaria</dfn>
														<dl><s:property value="sedeSecondariaSoggetto.denominazione" />&nbsp;</dl>
													</li>
												</ul>
											</div>
											<div class="boxOrInRight">
												<p>Importi</p>
												<ul class="htmlelt">
													<li>
														<dfn>Importo totale documento</dfn>
														<dl><s:property value="documento.importo" />&nbsp;</dl>
													</li>
													<li>
														<dfn>Importo non rilevante iva</dfn>
														<dl><s:property value="importoNonRilevanteIva" />&nbsp;</dl>
													</li>
													<li>
														<dfn>Importo rilevante iva</dfn>
														<dl><s:property value="importoRilevanteIva" />&nbsp;</dl>
													</li>
													<s:if test="tipoSubdocumentoIvaQuota">
														<li>
															<dfn>Importo quota</dfn>
															<dl><s:property value="subdocumento.importo" />&nbsp;</dl>
														</li>
														
														<li>
															<dfn>Numero quota</dfn>
															<dl><s:property value="subdocumento.numero" />&nbsp;</dl>
														</li>
													</s:if>
												</ul>
											</div>
										</fieldset>
									</div>
								</div>
							</div>
						</div>
					</div>
					<p class="margin-medium">
						<button type="button" class="btn btn-primary pull-right pulsanteSalvaFormIva" id="pulsanteSalvaForm_sup">salva</button>
					</p>
					<br>
					<h4 class="step-pane">Dati iva</h4>
					<fieldset>
						<s:if test="flagIntracomunitarioUtilizzabile">
							<div class="control-group">
								<label for="flagIntracomunitario" class="control-label">Documento intracomunitario</label>
								<div class="controls">
									<s:checkbox id="flagIntracomunitario" name="subdocumentoIva.flagIntracomunitario" cssClass="flagIntracomunitario" />
									<span id="campiDocumentoIntracomunitario" class="fade">
										<span class="alRight">
											<label for="valuta" class="radio inline">valuta estera</label>
										</span>
										<s:select list="listaValuta" name="valuta.uid" cssClass="lbTextSmall span2" id="valuta" headerKey="0" headerValue="" listKey="uid" listValue="descrizione" />
										<span class="alRight">
											<label for="importoInValuta" class="radio inline">importo in valuta</label>
										</span>
										<s:textfield id="importoInValuta" name="importoInValuta" cssClass="lbTextSmall span2 soloNumeri decimale" />
									</span>
								</div>
							</div>
						</s:if>
						<div class="control-group">
							<label for="progressivoIvaSubdocumentoIVA" class="control-label">N. registrazione *</label>
							<div class="controls">
								<s:textfield id="progressivoIVASubdocumentoIva" name="subdocumentoIva.progressivoIVA" cssClass="lbTextSmall span2"
									required="true" readonly="true" data-maintain="" />
								<span class="alRight">
									<label for="dataRegistrazioneSubdocumentoIva" class="radio inline">Data registrazione *</label>
								</span>
								<s:textfield id="dataRegistrazioneSubdocumentoIva" name="subdocumentoIva.dataRegistrazione" cssClass="lbTextSmall span2"
									required="true" readonly="true" value="%{dataOdierna}" data-maintain="" />
							</div>
						</div>
						<div class="control-group">
							<label for="tipoRegistrazioneIvaSubdocumentoIva" class="control-label">Tipo registrazione *</label>
							<div class="controls">
								<s:select list="listaTipoRegistrazioneIva" name="subdocumentoIva.tipoRegistrazioneIva.uid" id="tipoRegistrazioneIvaSubdocumentoIva"
									headerKey="0" headerValue="" listKey="uid" listValue="%{codice + ' - ' + descrizione}" required="true" cssClass="span6" />
							</div>
						</div>
						<div class="control-group">
							<label for="tipoRegistroIva" class="control-label">Tipo registro iva *</label>
							<div class="controls">
								<select id="tipoRegistroIva" name="tipoRegistroIva" required="required" class="span6">
									<option></option>
									<s:iterator value="listaTipoRegistroIva" var="tri">
										<option value="<s:property value="%{#tri}"/>" data-tipo-esigibilita-iva="<s:property value="%{#tri.tipoEsigibilitaIva}"/>"
												<s:if test="%{#tri.equals(tipoRegistroIva)}">selected</s:if>>
											<s:property value="%{#tri.codice + ' - ' + #tri.descrizione}"/>
										</option>
									</s:iterator>
								</select>
							</div>
						</div>
						<div class="control-group">
							<label for="attivitaIva" class="control-label">Attivit&agrave;</label>
							<div class="controls">
								<select id="attivitaIva" name="attivitaIva.uid" class="span6">
									<option value="0"></option>
									<s:iterator value="listaAttivitaIva" var="ai">
										<option value="<s:property value="%{#ai.uid}" />" data-gruppo-attivita-iva="<s:property value="%{#ai.gruppoAttivitaIva.uid}" />"
											data-flag-rilevante-irap="<s:property value="%{#ai.flagRilevanteIRAP}" />"
											<s:if test="%{#ai.uid == attivitaIva.uid}">selected</s:if>>
											<s:property value="%{#ai.codice}" /> - <s:property value="%{#ai.descrizione}" />
										</option>
									</s:iterator>
								</select>
								<span class="alRight">
									<label for="flagRilevanteIRAPSubdocumentoIva" class="radio inline">Rilevante IRAP</label>
								</span>
								<s:checkbox id="flagRilevanteIRAPSubdocumentoIva" name="subdocumentoIva.flagRilevanteIRAP" />
							</div>
						</div>
						<div class="control-group">
							<label for="registroIvaSubdocumentoIva" class="control-label">Registro *</label>
							<div class="controls">
								<s:select list="listaRegistroIva" name="subdocumentoIva.registroIva.uid" cssClass="span6" id="registroIvaSubdocumentoIva"
									headerKey="0" headerValue="" listKey="uid" listValue="%{codice + ' - ' + descrizione}" required="true"
									disabled="%{tipoRegistroIva == null || tipoRegistroIva.uid == 0}" data-overlay="" />
							</div>
						</div>
						<div class="control-group hide" id="gruppoProtocolloProvvisorio">
							<label for="numeroProtocolloProvvisorioSubdocumentoIva" class="control-label">Protocollo provvisorio</label>
							<div class="controls">
								<s:textfield id="numeroProtocolloProvvisorioSubdocumentoIva" name="subdocumentoIva.numeroProtocolloProvvisorio"
									cssClass="lbTextSmall span2 soloNumeri" placeholder="%{'numero'}" readonly="true" data-maintain="" />
								<span class="alRight">
									<label for="dataProtocolloProvvisorioSubdocumentoIva" class="radio inline">In data</label>
								</span>
								<s:textfield id="dataProtocolloProvvisorioSubdocumentoIva" name="subdocumentoIva.dataProtocolloProvvisorio"
									cssClass="lbTextSmall span2 datepicker" placeholder="%{'data'}" />
							</div>
						</div>
						<div class="control-group hide" id="gruppoProtocolloDefinitivo">
							<label for="numeroProtocolloDefinitivoSubdocumentoIva" class="control-label">Protocollo definitivo</label>
							<div class="controls">
								<s:textfield id="numeroProtocolloDefinitivoSubdocumentoIva" name="subdocumentoIva.numeroProtocolloDefinitivo"
									cssClass="lbTextSmall span2 soloNumeri" placeholder="%{'numero'}" readonly="true" data-maintain="" />
								<span class="alRight">
									<label for="dataProtocolloDefinitivoSubdocumentoIva" class="radio inline">In data</label>
								</span>
								<s:textfield id="dataProtocolloDefinitivoSubdocumentoIva" name="subdocumentoIva.dataProtocolloDefinitivo"
									cssClass="lbTextSmall span2 datepicker" placeholder="%{'data'}" />
							</div>
						</div>
						<s:if test="tipoSubdocumentoIvaQuota">
							<div class="control-group">
								<label for="numeroOrdinativoDocumentoSubdocumentoIva" class="control-label">N. ordinativo</label>
								<div class="controls">
									<s:textfield id="numeroOrdinativoDocumentoSubdocumentoIva" name="subdocumentoIva.numeroOrdinativoDocumento"
										cssClass="lbTextSmall span2" readonly="true" data-maintain="" />
									<span class="alRight">
										<label for="dataOrdinativoDocumentoSubdocumentoIva" class="radio inline">Data ordinativo</label>
									</span>
									<s:textfield id="dataOrdinativoDocumentoSubdocumentoIva" name="subdocumentoIva.dataOrdinativoDocumento"
										cssClass="lbTextSmall span2" readonly="true" data-maintain="" />
									<span class="alRight">
										<label for="dataCassaDocumentoSubdocumentoIva" class="radio inline">Data pagamento</label>
									</span>
									<s:textfield id="dataCassaDocumentoSubdocumentoIva" name="subdocumentoIva.dataCassaDocumento"
										cssClass="lbTextSmall span2" readonly="true" data-maintain="" />
								</div>
							</div>
						</s:if>
						
						<div class="control-group">
							<label for="descrizioneIvaSubdocumentoIva" class="control-label">Descrizione</label>
							<div class="controls">
								<s:textarea id="descrizioneIvaSubdocumentoIva" name="subdocumentoIva.descrizioneIva"
									cssClass="input-medium span9" cols="15" rows="2" maxlength="500"></s:textarea>
							</div>
						</div>
						<p>
							<a data-target="#collapseMovimentiIva" data-toggle="collapse" class="btn btn-primary">movimenti iva</a>
						</p>
						
						<div class="collapse" id="collapseMovimentiIva">
							<s:include value="/jsp/documentoIva/movimentiIva.jsp"/>
						</div>
					</fieldset>
					<div class="Border_line"></div>
					<p class="margin-medium">
						<s:include value="/jsp/include/indietro.jsp" />
						<button type="button" class="btn reset">annulla</button>
						<button type="button" class="btn btn-primary pull-right pulsanteSalvaFormIva" id="pulsanteSalvaForm">salva</button>
					</p>
					
					<div aria-hidden="true" aria-labelledby="msgEliminaLabel" role="dialog" tabindex="-1" class="modal hide fade" id="modaleElimina">
						<div class="modal-body">
							<div class="alert alert-error">
								<button data-dismiss="alert" class="close" type="button">&times;</button>
								<p><strong>Attenzione!</strong></p>
								<p><strong>Elemento selezionato: <span id="SPAN_elementoSelezionato"></span></strong></p>
								<p>Stai per eliminare l'elemento selezionato: sei sicuro di voler proseguire?</p>
							</div>
						</div>
						<div class="modal-footer">
							<button type="button" aria-hidden="true" data-dismiss="modal" class="btn">no, indietro</button>
							<button type="button" class="btn btn-primary" id="pulsanteSiElimina">
								s&iacute;, prosegui&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_pulsanteSiElimina"></i>
							</button>
						</div>
					</div>
					<div aria-hidden="true" aria-labelledby="msgMovimentoResiduoLabel" role="dialog" tabindex="-1" class="modal hide fade" id="modaleMovimentoResiduo">
						<div class="modal-body">
							<div class="alert alert-warning alert-persistent">
								<button data-dismiss="alert" class="close" type="button">&times;</button>
								<p><strong>Attenzione!</strong></p>
								<p>Sono presenti movimenti residui associati al documento, 
									si desidera procedere con l'inserimento della contabilizzazione del dato fiscale nel registro ?</p>
							</div>
						</div>
						<div class="modal-footer">
							<button type="button" class="btn btn-primary" id="pulsanteProseguiSenzaProtocollo">
								no&nbsp;
							</button>
							<button type="button" class="btn btn-primary" id="pulsanteProseguiMovimentoResiduo">
								s&iacute;&nbsp;
							</button>
						</div>
					</div>
					<div id="divModaleMovimentiIva"><%--<s:include value="/jsp/documentoIva/modaleMovimentiIva.jsp" />--%></div>
					<s:if test="flagIntracomunitarioUtilizzabile">
						<s:include value="/jsp/documentoIva/modaleIntracomunitario.jsp">
							<s:param name="docId">Documento</s:param>
							<s:param name="docName">Documento</s:param>
							<s:param name="docDisabled">false</s:param>
						</s:include>
					</s:if>
				</s:form>
			</div>
		</div>
	</div>
	
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="${jspath}documentoIva/documentoIva.js"></script>
	<script type="text/javascript" src="${jspath}documentoIva/inserisci.js"></script>
	
</body>
</html>