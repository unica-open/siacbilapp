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
				<s:form cssClass="form-horizontal" action="inserisciDocumentoEntrataEnterStep2.do" id="formInserimentoDocumentoEntrata" novalidate="novalidate" >
					<s:include value="/jsp/include/messaggi.jsp" />
					
					<h3>Inserimento documenti entrata</h3>
					<div class="wizard" id="MyWizard">
						<ul class="steps">
							<li class="active" data-target="#step1">
								<span class="badge">1</span>dati principali<span class="chevron"></span>
							</li>
							<li data-target="#step2">
								<span class="badge">2</span>dettaglio<span class="chevron"></span>
							</li>
							<li data-target="#step3">
								<span class="badge">3</span>riepilogo<span class="chevron"></span>
							</li>
						</ul>
					</div>

					<div class="step-content">
						<div id="step1" class="step-pane active">
							<h4>Dati principali</h4>
							<fieldset class="form-horizontal">
								<div class="control-group">
									<label for="tipoDocumento" class="control-label">Tipo *</label>
									<div class="controls">
										<s:select list="listaTipoDocumento" cssClass="span6" id="tipoDocumento" name="documento.tipoDocumento.uid" headerKey="0" headerValue=""
												listKey="uid" listValue="%{codice + ' - ' + descrizione}" required="true" />
									</div>
								</div>
								<div class="control-group">
									<label for="annoDocumento" class="control-label">Anno *</label>
									<div class="controls">
										<s:textfield id="annoDocumento" name="documento.anno" cssClass="lbTextSmall span2" required="true" placeholder="anno" maxlength="4" readonly="%{fatturaFEL != null}" />
										<span class="alRight">
											<label for="numeroDocumento" class="radio inline">Numero *</label>
										</span>
										<s:textfield id="numeroDocumento" name="documento.numero" cssClass="lbTextSmall span2" required="true" placeholder="numero" maxlength="200" readonly="%{fatturaFEL != null}" />
										<span class="alRight">
											<label for="dataEmissioneDocumento" class="radio inline">Data *</label>
										</span>
										<%-- SIAC-7571 adeguo la pagina come per la spesa in quanto anche l'entrata ora possiede le fatture --%>
										<s:if test="%{fatturaFEL != null}">
											<s:textfield id="dataEmissioneDocumento" name="documento.dataEmissione" cssClass="lbTextSmall span2" size="10" required="required" disabled="true" readonly="true"/>
											<s:hidden name="documento.dataEmissione" />
										</s:if>
										<s:else>
											<s:textfield id="dataEmissioneDocumento" name="documento.dataEmissione" cssClass="lbTextSmall span2 datepicker" size="10" required="required"/>
										</s:else>
										<%-- SIAC-7571 --%>
										
										<%-- SIAC 6677 --%>
										<span class="alRight">
											<label for="dataOperazioneDocumento" class="radio inline">Data Operazione</label>
										</span>
										
										<%-- SIAC-7571 adeguo la pagina come per la spesa in quanto anche l'entrata ora possiede le fatture  --%>
										<s:if test='%{fatturaFEL != null}'>
											<s:textfield id="dataOperazioneDocumento" name="documento.dataOperazione" cssClass="lbTextSmall span2" size="10" disabled="true" readonly="true" tabindex="-1" />
											<s:hidden name="documento.dataOperazione" />
										</s:if>
										<s:else>
											<s:textfield id="dataOperazioneDocumento" name="documento.dataOperazione" cssClass="lbTextSmall span2 datepicker" size="10" tabindex="-1" />
										</s:else>
										<%-- SIAC-7571 --%>
									</div>
								</div>
								
								<h4 class="step-pane">Soggetto
									<span id="descrizioneCompletaSoggetto">
										<%-- SIAC-7571 adeguo la descrizione del sogetto nel doc di entrata come per la spesa --%>
										<s:if test='%{soggetto != null && (soggetto.codiceSoggetto != null && soggetto.codiceSoggetto != "")}'>
											<s:property value="soggetto.codiceSoggetto" />
										</s:if>
										<s:if test='%{soggetto != null && (soggetto.denominazione != null && soggetto.denominazione != "")}'>
											- <s:property value="soggetto.denominazione" />
										</s:if>
										<s:if test='%{soggetto != null && (soggetto.codiceFiscale != null && soggetto.codiceFiscale != "")}'>
											- <s:property value="soggetto.codiceFiscale" />
										</s:if>
										<%-- SIAC-7571 --%>
									</span>
								</h4>
								<s:hidden id="HIDDEN_soggettoDenominazione" name="soggetto.denominazione" />
								<s:hidden id="HIDDEN_soggettoCodiceFiscale" name="soggetto.codiceFiscale" />
								<div class="control-group">
									<label class="control-label" for="codiceSoggetto">Codice *</label>
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
							</fieldset>
						</div>
					</div>

					<p class="margin-medium">
						<s:include value="/jsp/include/indietro.jsp" />
						<s:reset cssClass="btn" value="annulla" />
						<s:submit id="toStep2" cssClass="btn btn-primary pull-right" value="prosegui" />
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
	<script type="text/javascript" src="/siacbilapp/js/local/documento/inserisciEntrata.js"></script>

</body>
</html>