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
				<s:form cssClass="form-horizontal" action="inserisciTestataDocumentoSpesaEnterStep3" id="formInserimentoDocumentoSpesa" novalidate="novalidate" >
					<s:hidden id="HIDDEN_anno_datepicker" value="%{documento.anno}" />
					<s:include value="/jsp/include/messaggi.jsp" />
					<h3>Inserimento documenti iva spesa</h3>
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
									</div>
								</div>
							
								<div class="control-group">
									<label for="descrizioneDocumento" class="control-label">Descrizione *</label>
									<div class="controls">
										<s:textarea id="descrizioneDocumento" name="documento.descrizione" cols="15" rows="2" cssClass="span10" required="required"></s:textarea>
									</div>
								</div>
							
								<div class="control-group">
									<s:hidden id="HIDDEN_dataDocumento" value="%{documento.dataEmissione}" />
									<label class="control-label" for="dataScadenzaDocumento">Data scadenza</label>
									<div class="controls">
										<s:textfield id="dataScadenzaDocumento" name="documento.dataScadenza" cssClass="lbTextSmall span2 datepicker" placeholder="data scadenza" />
									</div>
								</div>
							</fieldset>
						</div>
					</div>

					<p class="margin-medium">
						<s:a cssClass="btn" action="inserisciTestataDocumentoSpesaReturnToStep1" id="pulsanteRedirezioneIndietro">indietro</s:a>
						<s:reset cssClass="btn" value="annulla" />
						<s:submit cssClass="btn btn-primary pull-right" value="salva" />
					</p>
				</s:form>
			</div>
		</div>
	</div>
	
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	
</body>
</html>