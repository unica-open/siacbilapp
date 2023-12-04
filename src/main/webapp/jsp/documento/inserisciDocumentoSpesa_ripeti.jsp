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
				<s:form cssClass="form-horizontal" action="inserisciDocumentoSpesa_ripetiSalva" id="formInserimentoDocumentoSpesa" novalidate="novalidate" >
					<s:include value="/jsp/include/messaggi.jsp" />
					<s:hidden id="HIDDEN_anno_datepicker" value="%{documento.anno}" />
					<h3>Ripeti Documento spesa</h3>

					<fieldset class="form-horizontal">
						<h4>Dati principali</h4>
						<div class="control-group">
							<label for="tipoDocumento" class="control-label">Tipo *</label>
							<div class="controls">
								<s:select list="listaTipoDocumento" cssClass="span6" id="tipoDocumento" name="documento.tipoDocumento.uid" headerKey="0" headerValue=""
										listKey="uid" listValue="%{codice + '-' + descrizione}" required="true" />
							</div>
						</div>
						<div class="control-group">
							<label for="siopeDocumentoTipo" class="control-label">Tipo documento siope *</label>
							<div class="controls">
								<select name="documento.siopeDocumentoTipo.uid" class="span6" required id="siopeDocumentoTipo" disabled>
									<option value="0"></option>
									<s:iterator value="listaSiopeDocumentoTipo" var="sdt">
										<option value="<s:property value="#sdt.uid" />"
												data-codice="<s:property value="#sdt.codice" />"
												<s:if test="#sdt.uid == documento.siopeDocumentoTipo.uid">selected</s:if>>
											<s:property value="#sdt.codice" /> - <s:property value="#sdt.descrizione" />
										</option>
									</s:iterator>
								</select>
								<s:hidden name="documento.siopeDocumentoTipo.uid" />
							</div>
						</div>
						<div class="control-group <s:if test="!tipoDocumentoSiopeAnalogico">hide</s:if>" data-siope-analogico>
							<label for="siopeDocumentoTipoAnalogico" class="control-label">Tipo documento analogico siope *</label>
							<div class="controls">
								<s:select list="listaSiopeDocumentoTipoAnalogico" cssClass="span6" id="siopeDocumentoTipoAnalogico"
										name="documento.siopeDocumentoTipoAnalogico.uid" headerKey="0" headerValue=""
										listKey="uid" listValue="%{codice + ' - ' + descrizione}" required="true" />
							</div>
						</div>
						<div class="control-group">
							<label for="annoDocumento" class="control-label">Anno *</label>
							<div class="controls">
								<s:textfield id="annoDocumento" name="documento.anno" cssClass="lbTextSmall span2" required="true" placeholder="anno" maxlength="4" />
								<span class="alRight">
									<label for="numeroDocumento" class="radio inline">Numero *</label>
								</span>
								<s:textfield id="numeroDocumento" name="documento.numero" cssClass="lbTextSmall span2" required="true" placeholder="numero" maxlength="200" />
								<span class="alRight">
									<label for="dataEmissioneDocumento" class="radio inline">Data *</label>
								</span>
								<s:textfield id="dataEmissioneDocumento" name="documento.dataEmissione" cssClass="lbTextSmall span2 datepicker" size="10" />
								<span class="alRight">
									<label for="dataRicezionePortaleDocumento" class="radio inline">Data ricezione</label>
								</span>
								<s:textfield id="dataRicezionePortaleDocumento" name="documento.dataRicezionePortale" cssClass="lbTextSmall span2 datepicker" size="10" tabindex="-1" />
							</div>
						</div>
						
						<h4 class="step-pane">Soggetto
							<span id="descrizioneCompletaSoggetto">
								<s:if test='%{soggetto != null && (soggetto.codice ne null && soggetto.codice != "") && (soggetto.descrizione ne null && soggetto.descrizione != "") && (soggetto.codiceFiscale ne null && soggetto.codiceFiscale != "")}'>
									<s:property value="%{soggetto.codice + ' - ' + soggetto.descrizione + ' - ' + soggetto.codiceFiscale}" />
								</s:if>
							</span>
						</h4>
						<s:hidden id="HIDDEN_soggettoDenominazione" name="soggetto.denominazione" />
						<s:hidden id="HIDDEN_soggettoCodiceFiscale" name="soggetto.codiceFiscale" />
						<div class="control-group">
							<label class="control-label" for="codiceSoggetto">Codice </label>
							<div class="controls">
								<s:textfield id="codiceSoggetto" cssClass="lbTextSmall span2" name="soggetto.codiceSoggetto" maxlength="20" placeholder="codice" required="required" />
								<span class="radio guidata">
									<a href="#" class="btn btn-primary" id="pulsanteAperturaCompilazioneGuidataSoggetto">compilazione guidata</a>
								</span>
							</div>
						</div>
						<div class="control-group">
							<label for="beneficiarioMultiplo" class="control-label">Beneficiario multiplo</label>
							<div class="controls">
								<s:checkbox id="beneficiarioMultiplo" name="documento.flagBeneficiarioMultiplo" />
							</div>
						</div>
						<div class="control-group">
							<label class="control-label">Struttura Amministrativa</label>
							<div class="controls">
								<div class="accordion span8 struttAmm">
									<div class="accordion-group">
										<div class="accordion-heading">
											<a class="accordion-toggle" id="accordionPadreStrutturaAmministrativa" href="#struttAmm">
												<span id="SPAN_StrutturaAmministrativoContabile">Seleziona la Struttura amministrativa</span>
											</a>
										</div>
										<div id="struttAmm" class="accordion-body collapse">
											<div class="accordion-inner">
												<ul id="treeStruttAmm" class="ztree treeStruttAmm"></ul>
											</div>
										</div>
									</div>
								</div>
			
								<s:hidden id="HIDDEN_StrutturaAmministrativoContabileUid" name="documento.strutturaAmministrativoContabile.uid" />
								<s:hidden id="HIDDEN_StrutturaAmministrativoContabileCodice" name="documento.strutturaAmministrativoContabile.codice" />
								<s:hidden id="HIDDEN_StrutturaAmministrativoContabileDescrizione" name="documento.strutturaAmministrativoContabile.descrizione" />
							</div>
						</div>
						
						<h4>Dati importi</h4>
						<div class="control-group">
							<label class="control-label" for="importoDocumento">Importo *</label>
							<div class="controls">
								<s:textfield id="importoDocumento" cssClass="lbTextSmall span2 soloNumeri decimale" name="documento.importo" placeholder="importo" required="required" />
								<span class="alRight">
									<label class="radio inline" for="arrotondamentoDocumento">Arrotondamento</label>
								</span>
								<s:textfield id="arrotondamentoDocumento" cssClass="lbTextSmall span2 soloNumeri decimale" name="documento.arrotondamento" placeholder="arrotondamento" />
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
								<s:textfield id="terminePagamentoDocumento" name="documento.terminePagamento" cssClass="lbTextSmall span2 numeroNaturale" placeholder="termine pagamento" />
								<span class="alRight">
									<label class="radio inline" for="dataScadenzaDocumento">Data scadenza</label>
								</span>
								<s:textfield id="dataScadenzaDocumento" name="documento.dataScadenza" cssClass="lbTextSmall span2 datepicker" placeholder="data scadenza" />
								<s:if test="flagDatiSospensioneEditabili">
									<span class="alRight">
										<label class="radio inline" for="dataScadenzaDopoSospensioneDocumento">Data scadenza dopo sospensione</label>
									</span>
									<s:textfield id="dataScadenzaDopoSospensioneDocumento" name="documento.dataScadenzaDopoSospensione" cssClass="lbTextSmall span2 datepicker" placeholder="data scadenza dopo sospensione" />
								</s:if>
							</div>
						</div>
					
						<div class="control-group">
							<label class="control-label">Sospensione</label>
							<div class="controls">
								<span class="al">
									<label class="radio inline" for="dataSospensioneDocumento">Data&nbsp;</label>
								</span>
								<s:textfield id="dataSospensioneDocumento" name="documento.dataSospensione" cssClass="lbTextSmall span2 datepicker" placeholder="data sospensione" />
								<span class="al">
									<label class="radio inline" for="causaleSospensioneDocumento">Causale</label>
								</span>
								<select id="causaleSospensione" name="causaleSospensione" class="lbTextSmall span2">
									<option value="0"></option>
									<s:iterator value="listaCausaleSospensione" var="cs">
										<option value="<s:property value="#cs.uid"/>" data-template="<s:property value="#cs.descrizione"/>"><s:property value="#cs.codice"/></option>
									</s:iterator>
								</select>
								&nbsp;
								<s:textfield id="causaleSospensioneDocumento" name="documento.causaleSospensione" cssClass="span4" placeholder="causale" />
								<span class="al">
									<label class="radio inline" for="dataRiattivazioneDocumento">Data riattivazione</label>
								</span>
								<s:textfield id="dataRiattivazioneDocumento" name="documento.dataRiattivazione" cssClass="lbTextSmall span2 datepicker" placeholder="data riattivazione" />
							</div>
						</div>
					
						<div class="control-group">
							<label class="control-label">Dati repertorio/protocollo</label>
							<div class="controls">
								<span class="alRight">
									<label class="radio inline" for="registroRepertorioDocumento">Registro</label>
								</span>
								<s:textfield id="registroRepertorioDocumento" name="documento.registroRepertorio" cssClass="lbTextSmall span2" placeholder="registro" />
								<span class="alRight">
									<label class="radio inline" for="annoRepertorioDocumento">Anno</label>
								</span>
								<s:textfield id="annoRepertorioDocumento" name="documento.annoRepertorio" cssClass="lbTextSmall span2" maxlength="4" placeholder="anno"/>
								<span class="alRight">
									<label class="radio inline" for="numeroRepertorioDocumento">Numero</label>
								</span>
								<s:textfield id="numeroRepertorioDocumento" name="documento.numeroRepertorio" cssClass="lbTextSmall span2" placeholder="numero" />
								<span class="alRight">
									<label class="radio inline" for="dataRepertorioDocumento">Data</label>
								</span>
								<s:textfield id="dataRepertorioDocumento" name="documento.dataRepertorio" cssClass="lbTextSmall span2 datepicker" placeholder="data" />
							</div>
						</div>
						
						<%-- SIAC 6677 --%>
						<%-- SIAC-6840 si riabilita il campo --%>
	 							<div class="control-group">
	 								<label class="control-label" for="codAvvisoPagoPA">Codice Avviso Pago PA</label> -->
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
																listKey="uid" listValue="%{codice + '-' + descrizione}" cssClass="span10" required="required" />
													</div>
												</div>
												<div class="control-group">
													<label for="tipoImpresaDocumento" class="control-label">Tipo impresa</label>
													<div class="controls ">
														<s:select list="listaTipoImpresa" name="documento.tipoImpresa.uid" headerKey="0" headerValue=""
																listKey="uid" listValue="%{codice + '-' + descrizione}" cssClass="span10" />
													</div>
												</div>
												
												<div class="control-group">
													<label for="noteDocumento" class="control-label">Note</label>
													<div class="controls">
														<s:textarea id="noteDocumento" name="documento.note" rows="3" cols="15" cssClass="span10"></s:textarea>
													</div>
												</div>
												<div class="control-group">
													<label for="codicePCCDocumento" class="control-label">Codice PCC</label>
													<div class="controls input-append">
														<s:select list="listaCodicePCC" name="documento.codicePCC.uid" headerKey="0" headerValue=""
																listKey="uid" listValue="%{codice + ' - ' + descrizione}" cssClass="span10" />
													</div>
												</div>
												<div class="control-group">
													<label for="codiceUfficioDestinatarioDocumento" class="control-label">Codice Ufficio Destinatario FEL</label>
													<div class="controls input-append">
														<s:select list="listaCodiceUfficioDestinatarioPCC" name="documento.codiceUfficioDestinatario.uid" headerKey="0" headerValue=""
																listKey="uid" listValue="%{codice + ' - ' + descrizione}" cssClass="span10" />
													</div>
												</div>
											</fieldset>
										</div>
									</div>
								</div>
							</div>
						</div>
					</fieldset>
						
					<p class="margin-medium">
						<s:include value="/jsp/include/indietro.jsp" />
						<s:reset cssClass="btn" value="annulla" />
						<s:submit cssClass="btn btn-primary pull-right" value="salva" />
					</p>
				</s:form>
			</div>
		</div>
	</div>
	
	<s:include value="/jsp/soggetto/selezionaSoggetto_modale.jsp" />
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/codiceFiscale.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/soggetto/ricerca.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/documento/inserisci.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/documento/inserisciSpesa.js"></script>

</body>
</html>