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
				<s:form cssClass="form-horizontal" action="inserisciDocumentoEntrata_ripetiSalva" id="formInserimentoDocumentoEntrata" novalidate="novalidate" >
					<s:include value="/jsp/include/messaggi.jsp" />
					<s:hidden id="HIDDEN_anno_datepicker" value="%{documento.anno}" />
					<h3>Ripeti Documento entrata</h3>

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
								<!-- SIAC 6677 -->
								<span class="alRight">
									<label for="dataOperazioneDocumento" class="radio inline">Data Operazione</label>
								</span>
								<s:textfield id="dataOperazioneDocumento" name="documento.dataOperazione" cssClass="lbTextSmall span2 datepicker" size="10" />
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
							<label for="debitoreMultiplo" class="control-label">Debitore multiplo</label>
							<div class="controls">
								<s:checkbox id="debitoreMultiplo" name="documento.flagDebitoreMultiplo" />
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
							<label class="control-label" for="termineIncassoDocumento">Termine di pagamento</label>
							<div class="controls">
								<s:textfield id="termineIncassoDocumento" name="documento.termineIncasso" cssClass="lbTextSmall span2 numeroNaturale" placeholder="termine pagamento" />
								<span class="alRight">
									<label class="radio inline" for="dataScadenzaDocumento">Data scadenza</label>
								</span>
								<s:textfield id="dataScadenzaDocumento" name="documento.dataScadenza" cssClass="lbTextSmall span2 datepicker" placeholder="data scadenza" />
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
						
						<!-- SIAC 6677 -->
						<div class="control-group">
							<label class="control-label" for="codAvvisoPagoPA">Codice Avviso Pago PA</label>
							<div class="controls">
								<s:textfield id="codAvvisoPagoPA" name="documento.codAvvisoPagoPA" cssClass="lbTextSmall span2 numeroNaturale" placeholder="Codice Avviso Pago PA" />
								<span class="alRight">
									<label class="radio inline" for="iuv">IUV</label>
								</span>
								<s:textfield id="iuv" name="documento.iuv" readonly="true" cssClass="lbTextSmall span2" placeholder="IUV" />
							</div>
						</div>
					
						
						<div class="step-pane active" id="datiIncassoPadre">
							<div class="accordion" >
								<div class="accordion-group">
									<div class="accordion-heading">
										<a class="accordion-toggle" data-toggle="collapse" data-parent="#datiIncassoPadre" href="#datiIncassoTab">
											Dati incasso<span class="icon">&nbsp;</span>
										</a>
									</div>
									<div id="datiIncassoTab" class="accordion-body collapse in">
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
													<label for="noteDocumento" class="control-label">Note</label>
													<div class="controls">
														<s:textarea id="noteDocumento" name="documento.note" rows="3" cols="15" cssClass="span10"></s:textarea>
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
	<script type="text/javascript" src="${jspath}codiceFiscale.js"></script>
	<script type="text/javascript" src="${jspath}soggetto/ricerca.js"></script>
	<script type="text/javascript" src="${jspath}documento/inserisci.js"></script>
	<script type="text/javascript" src="${jspath}documento/inserisciEntrata.js"></script>

</body>
</html>