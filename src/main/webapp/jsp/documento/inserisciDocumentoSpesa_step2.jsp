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
				<s:hidden id="HIDDEN_dataDocumento" value="%{documento.dataEmissione}" />
				<s:hidden id="HIDDEN_dataRicezioneDocumento" value="%{documento.dataRicezionePortale}" />
				<s:form cssClass="form-horizontal" action="inserisciDocumentoSpesaEnterStep3" id="formInserimentoDocumentoSpesa" novalidate="novalidate" >
					<s:hidden id="HIDDEN_anno_datepicker" value="%{documento.anno}" />
					<s:include value="/jsp/include/messaggi.jsp" />
					<h3>Inserimento documenti spesa</h3>
					<h3>
						<s:property value="documento.tipoDocumento.codice"/> - <s:property value="documento.anno"/> - <s:property value="documento.numero" /> - 
						<s:property value="documento.dataEmissione" /> - <s:property value="soggetto.codiceSoggetto"/> - <s:property value="soggetto.denominazione" />
					</h3>
					<div class="wizard" id="MyWizard">
						<ul class="steps">
							<li data-target="#step1" class="complete"><span class="badge badge-success">1</span>dati principali<span class="chevron"></span></li>
							<li data-target="#step2" class="active"><span class="badge">2</span>dettaglio<span class="chevron"></span></li>
							<li data-target="#step3"><span class="badge">3</span>riepilogo<span class="chevron"></span></li>
						</ul>
					</div>

					<div class="step-content">
						<div id="step2" class="step-pane active">
							<fieldset>
								<h4>Dati importi</h4>
								<div class="control-group">
									<label class="control-label" for="importoDocumento">Importo *</label>
									<div class="controls">
										<s:textfield id="importoDocumento" cssClass="lbTextSmall span2 soloNumeri decimale" name="documento.importo" placeholder="importo" required="required" readonly="%{inibisciModificaDatiImportatiFEL && importoDocumentoMinoreUgualeZero}" />
										<span class="alRight">
											<label class="radio inline" for="arrotondamentoDocumento">Arrotondamento</label>
										</span>
										<s:textfield id="arrotondamentoDocumento" cssClass="lbTextSmall span2 soloNumeri decimale" name="documento.arrotondamento" placeholder="arrotondamento" readonly = "%{inibisciModificaDatiImportatiFEL}" />
										<span class="alRight">
											<label class="radio inline" for="nettoDocumento">Netto</label>
										</span>
										<s:textfield id="nettoDocumento" readonly="true" cssClass="lbTextSmall span2 soloNumeri decimale" name="netto" />
									</div>
								</div>
							
								<div class="control-group">
									<label for="descrizioneDocumento" class="control-label">Descrizione *</label>
									<div class="controls">
										<s:textarea id="descrizioneDocumento" name="documento.descrizione" cols="15" rows="2" cssClass="span10" required="required"></s:textarea>
									</div>
								</div>
							
								<h4 class="step-pane">Altri dati</h4>
								<div class="control-group">
									<label class="control-label" for="terminePagamentoDocumento">Termine di pagamento</label>
									<div class="controls">
										<s:textfield id="terminePagamentoDocumento" name="documento.terminePagamento" cssClass="lbTextSmall span2 numeroNaturale" placeholder="termine pagamento" readonly = "%{inibisciModificaDatiImportatiFEL}" />
										<span class="alRight">
											<label class="radio inline" for="dataScadenzaDocumento">Data scadenza</label>
										</span>
										
										<s:if test="%{inibisciModificaDatiImportatiFEL}">
											<s:textfield id="dataScadenzaDocumento" name="documento.dataScadenza" cssClass="lbTextSmall span2 datepicker" placeholder="data scadenza" disabled="true" readonly="true"/>
											<s:hidden name="documento.dataScadenza" />
										</s:if>
										<s:else>
											<s:textfield id="dataScadenzaDocumento" name="documento.dataScadenza" cssClass="lbTextSmall span2 datepicker" placeholder="data scadenza" />
										</s:else>
										
										<s:if test="flagDatiSospensioneEditabili">
											<span class="alRight">
												<label class="radio inline" for="dataScadenzaDopoSospensioneDocumento">Data scadenza dopo sospensione</label>
											</span>
											<s:textfield id="dataScadenzaDopoSospensioneDocumento" name="documento.dataScadenzaDopoSospensione" cssClass="lbTextSmall span2 datepicker" placeholder="data scadenza dopo sospensione" />
										</s:if>
									</div>
								</div>
								
								<s:if test="flagDatiSospensioneEditabili">
									<div class="control-group">
										<label class="control-label">Sospensione</label>
										<div class="controls">
											<span class="al">
												<label class="radio inline" for="dataSospensioneDocumento">Data&nbsp;</label>
											</span>
											<s:textfield id="dataSospensioneDocumento" name="documento.dataSospensione" cssClass="lbTextSmall span2 datepicker" placeholder="data sospensione" />
											<span class="al">
												<label class="radio inline" for="dataRiattivazioneDocumento">Data riattivazione</label>
											</span>
											<s:textfield id="dataRiattivazioneDocumento" name="documento.dataRiattivazione" cssClass="lbTextSmall span2 datepicker" placeholder="data riattivazione" />
										</div>
									</div>
									<div class="control-group">
										<label class="control-label" for="causaleSospensioneDocumento">Causale sospensione</label>
										<div class="controls">
											<select id="causaleSospensione" name="causaleSospensione" class="lbTextSmall span6">
												<option value="0"></option>
												<s:iterator value="listaCausaleSospensione" var="cs">
													<option value="<s:property value="#cs.uid"/>" data-template="<s:property value="#cs.descrizione"/>"><s:property value="#cs.codice"/> - <s:property value="#cs.descrizione"/></option>
												</s:iterator>
											</select>
											&nbsp;
											<s:textfield id="causaleSospensioneDocumento" name="documento.causaleSospensione" cssClass="span6" placeholder="causale" />
										</div>
									</div>
								</s:if>
							
								<div class="control-group">
									<label class="control-label">Dati repertorio/protocollo</label>
									<div class="controls">
										<span class="alRight">
											<label class="radio inline" for="registroRepertorioDocumento">Registro</label>
										</span>
										<s:textfield id="registroRepertorioDocumento" name="documento.registroRepertorio" cssClass="lbTextSmall span2" placeholder="registro" readonly = "%{inibisciModificaDatiImportatiFEL}" />
										<span class="alRight">
											<label class="radio inline" for="annoRepertorioDocumento">Anno</label>
										</span>
										<s:textfield id="annoRepertorioDocumento" name="documento.annoRepertorio" cssClass="lbTextSmall span2" maxlength="4" placeholder="anno" readonly = "%{inibisciModificaDatiImportatiFEL}"/>
										<span class="alRight">
											<label class="radio inline" for="numeroRepertorioDocumento">Numero</label>
										</span>
										<s:textfield id="numeroRepertorioDocumento" name="documento.numeroRepertorio" cssClass="lbTextSmall span2" placeholder="numero" readonly="%{inibisciModificaDatiImportatiFEL}" />
										<span class="alRight">
											<label class="radio inline" for="dataRepertorioDocumento">Data</label>
										</span>
										<s:if test="%{inibisciModificaDatiImportatiFEL}">
											<s:textfield id="dataRepertorioDocumento" name="documento.dataRepertorio" cssClass="lbTextSmall span2 datepicker" placeholder="data"  disabled="true" readonly="true"/>
											<s:hidden name="documento.dataRepertorio" />
										</s:if>
										<s:else>
											<s:textfield id="dataRepertorioDocumento" name="documento.dataRepertorio" cssClass="lbTextSmall span2 datepicker" placeholder="data"/>
										</s:else>
									</div>
								</div>
								
								<%-- SIAC 6677 --%>
								<%-- SIAC-6840 si riabilita il campo --%>
	 								<div class="control-group"> 
	 									<label class="control-label" for="codAvvisoPagoPA">Codice Avviso Pago PA</label> 
	 									<div class="controls"> 
	 										<s:textfield id="codAvvisoPagoPA" name="documento.codAvvisoPagoPA" cssClass="lbTextSmall span2 numeroNaturale" placeholder="Codice Avviso Pago PA" />
	 									</div> 
	 								</div> 
								<%-- SIAC-6840 si riabilita il campo --%>
							
								<div class="control-group">
									<label class="control-label" for="codiceFiscalePignoratoDocumento">Soggetto pignorato</label>
									<div class="controls">
										<s:textfield id="codiceFiscalePignoratoDocumento" name="documento.codiceFiscalePignorato" cssClass="lbTextSmall span4" placeholder="codice fiscale" maxlength="16" />
									</div>
								</div>
							</fieldset>
							
							<%-- Accordion --%>
							<div class="step-pane active" id="datiPagamentoPadre">
								<div class="accordion" >
									<div class="accordion-group">
										<div class="accordion-heading">
											<a class="accordion-toggle" data-toggle="collapse" data-parent="#datiPagamentoPadre" href="#datiPagamentoTab">
												Dati pagamento<span class="icon">&nbsp;</span>
											</a>
										</div>
										<div id="datiPagamentoTab" class="accordion-body collapse in">
											<div class="accordion-inner">
												<fieldset class="form-horizontal">
													<div class="control-group">
														<label for="codiceBolloDocumento" class="control-label">Imposta di bollo *</label>
														<div class="controls input-append">
															<s:select list="listaCodiceBollo" name="documento.codiceBollo.uid" headerKey="0" headerValue=""
																	listKey="uid" listValue="%{codice + ' - ' + descrizione}" cssClass="span10" required="required" />
														</div>
													</div>
													<div class="control-group">
														<label for="tipoImpresaDocumento" class="control-label">Tipo impresa</label>
														<div class="controls ">
															<s:select list="listaTipoImpresa" name="documento.tipoImpresa.uid" headerKey="0" headerValue=""
																	listKey="uid" listValue="%{codice + ' - ' + descrizione}" cssClass="span10" />
														</div>
													</div>
													
													<div class="control-group">
														<label for="noteDocumento" class="control-label">Note</label>
														<div class="controls">
															<s:textarea id="noteDocumento" name="documento.note" rows="3" cols="15" cssClass="span10"></s:textarea>
														</div>
													</div>
													<div class="control-group">
														<label for="codicePCCDocumento" class="control-label">Codice PCC<s:if test="codicePccObbligatorio"> *</s:if></label>
														<div class="controls input-append">
															<s:select id="codicePCCDocumento" list="listaCodicePCC" name="documento.codicePCC.uid" headerKey="0" headerValue=""
																	listKey="uid" listValue="%{codice + ' - ' + descrizione}" cssClass="span10" data-disabilita-pcc="%{disabilitaPCCSeUnivoco}" data-pcc-obbligatorio="%{codicePccObbligatorio}" required="%{codicePccObbligatorio}" />
														</div>
													</div>
													<div class="control-group">
														<label for="codiceUfficioDestinatarioDocumento" class="control-label">Codice Ufficio Destinatario FEL</label>
														<div class="controls input-append">
															<s:select id="codiceUfficioDestinatarioDocumento" list="listaCodiceUfficioDestinatarioPCC" name="documento.codiceUfficioDestinatario.uid" headerKey="0" headerValue=""
																	listKey="uid" listValue="%{codice + ' - ' + descrizione}" cssClass="span10" data-disabilita-pcc="%{disabilitaPCCSeUnivoco}" disabled="%{fatturaFEL != null && inibisciModificaDatiImportatiFEL}"/>
															<s:if test="%{fatturaFEL != null && inibisciModificaDatiImportatiFEL}">			
																<s:hidden id="" name="documento.codiceUfficioDestinatario.uid"/>
															</s:if>
														</div>
													</div>
												</fieldset>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>

					<p class="margin-medium">
						<s:a cssClass="btn" action="inserisciDocumentoSpesaReturnToStep1" id="pulsanteRedirezioneIndietro">indietro</s:a>
						<s:a cssClass="btn" action="inserisciDocumentoSpesaAnnullaStep2">annulla</s:a>
						<s:submit cssClass="btn btn-primary pull-right" value="salva" />
					</p>
				</s:form>
			</div>
		</div>
	</div>
	
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/codiceFiscale.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/documento/inserisci.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/documento/inserisciSpesa.js"></script>
	
</body>
</html>