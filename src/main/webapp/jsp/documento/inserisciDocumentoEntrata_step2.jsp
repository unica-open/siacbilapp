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
				<s:form cssClass="form-horizontal" action="inserisciDocumentoEntrataEnterStep3" id="formInserimentoDocumentoEntrata" novalidate="novalidate" >
					<s:hidden id="HIDDEN_anno_datepicker" value="%{documento.anno}" />
					<s:include value="/jsp/include/messaggi.jsp" />
					<h3>Inserimento documenti entrata</h3>
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
								<s:hidden id="HIDDEN_dataDocumento" value="%{documento.dataEmissione}" />
									<label class="control-label" for="terminePagamentoDocumento">Termine di pagamento</label>
									<div class="controls">
										<s:textfield id="terminePagamentoDocumento" name="documento.terminePagamento" cssClass="lbTextSmall span2 numeroNaturale" placeholder="termine pagamento" />
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
							</fieldset>
							
							<%-- Accordion --%>
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
														<label for="codiceBolloDocumento" class="control-label">Codice bollo *</label>
														<div class="controls input-append">
															<s:select list="listaCodiceBollo" name="documento.codiceBollo.uid" headerKey="0" headerValue=""
																	listKey="uid" listValue="%{codice + ' - ' + descrizione}" cssClass="span10" required="required" />
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
						</div>
					</div>

					<p class="margin-medium">
						<s:a cssClass="btn" action="inserisciDocumentoEntrataReturnToStep1" id="pulsanteRedirezioneIndietro">indietro</s:a>
						<s:reset cssClass="btn" value="annulla" />
						<s:submit cssClass="btn btn-primary pull-right" value="salva" />
					</p>
				</s:form>
			</div>
		</div>
	</div>
	
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="${jspath}codiceFiscale.js"></script>
	<script type="text/javascript" src="${jspath}documento/inserisci.js"></script>
	<script type="text/javascript" src="${jspath}documento/inserisciEntrata.js"></script>
	
</body>
</html>