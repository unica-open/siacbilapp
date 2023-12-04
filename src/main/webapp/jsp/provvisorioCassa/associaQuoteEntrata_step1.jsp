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
				<s:form id="formAssociaDocumentoProvvisorioStep1" cssClass="form-horizontal" novalidate="novalidate" action="associaQuoteEntrataAProvvisorioDiCassa_completeSpep1" method="post">
					<s:include value="/jsp/include/messaggi.jsp" />
					<h4>Associa documenti al provvisorio n. <s:property value="provvisorio.numero"/> 
					 <s:if test="%{provvisorio.dataEmissione != null}"> del <s:property value="provvisorio.dataEmissione"/></s:if> 
					</h4>
					<div class="wizard">
						<ul class="steps">
							<li class="active" data-target="#step1"><span class="badge">1</span>Ricerca documenti<span class="chevron"></span></li>
							<li data-target="#step2"><span class="badge">2</span>Associa documenti<span class="chevron"></span></li>
						</ul>
					</div>
					<div class="step-content">
						<div id="step1" class="step-pane active">
							<h4>Documenti</h4>
							<fieldset class="form-horizontal">
								<div class="control-group">
									<label class="control-label" for="tipoDocumento">Tipo documento</label>
									<div class="controls">
										<s:select list="listaTipoDocumento" id="tipoDocumento" name="tipoDocumento.uid" cssClass="span6" data-overlay=""
											headerKey="" headerValue="" listKey="uid" listValue="%{codice + ' - ' + descrizione}" />
									</div>
								</div>
								<div class="control-group">
									<label class="control-label">Documento</label>
									<div class="controls">
										<s:textfield id="annoDocumento" name="annoDocumento" cssClass="span1 soloNumeri" maxlength="4" placeholder="anno"  />
										<s:textfield id="numeroDocumento" name="numeroDocumento" cssClass="span1" maxlength="50" placeholder="numero"  />
										<s:textfield id="numeroQuota" name="numeroSubdocumento" cssClass="span2 soloNumeri" maxlength="7" placeholder="quota" />
										<span class="al">
											<label class="radio inline" for="dataEmissioneDocumento">Data documento</label>
										</span>
										<s:textfield id="dataEmissioneDocumento" name="dataEmissioneDocumento" cssClass="span2 datepicker" maxlength="10" />
									</div>
								</div>
								<div class="control-group">
									<label class="control-label">Movimento</label>
									<div class="controls">
										<s:textfield id="annoMovimento" name="annoMovimento" cssClass="span1 soloNumeri" maxlength="4" placeholder="anno" />
										<s:textfield id="numeroMovimento" name="numeroMovimento" cssClass="span1 soloNumeri" maxlength="7" placeholder="numero" value="%{numeroMovimento.toString()}" />
									</div>
								</div>
								<div class="control-group">
									<label class="control-label">Elenco</label>
									<div class="controls">
										<s:textfield id="annoElenco" name="annoElenco" cssClass="span1 soloNumeri" maxlength="4" placeholder="anno" />
										<s:textfield id="numeroElenco" name="numeroElenco" cssClass="span1 soloNumeri" maxlength="7" placeholder="numero" />
									</div>
								</div>
							</fieldset>
							<h4 class="step-pane">Soggetto
								<span id="datiRiferimentoSoggettoSpan">
									<s:if test='%{soggetto != null && (soggetto.codice ne null && soggetto.codice != "") && (soggetto.descrizione ne null && soggetto.descrizione != "") && (soggetto.codiceFiscale ne null && soggetto.codiceFiscale != "")}'>
										<s:property value="%{soggetto.codice + ' - ' + soggetto.descrizione + ' - ' + soggetto.codiceFiscale}" />
									</s:if>
								</span>
							</h4>
							<s:hidden id="HIDDEN_soggettoDenominazione" name="soggetto.denominazione" />
							<s:hidden id="HIDDEN_soggettoCodiceFiscale" name="soggetto.codiceFiscale" />
							<s:hidden id="HIDDEN_soggettoUid" name="soggetto.uid" />
							<div class="control-group">
								<label class="control-label" for="codiceSoggettoSoggetto">Codice</label>
								<div class="controls">
									<s:textfield id="codiceSoggettoSoggetto" cssClass="lbTextSmall span2" name="soggetto.codiceSoggetto" maxlength="20" placeholder="codice" />
									<span class="radio guidata">
										<a href="#" class="btn btn-primary" id="pulsanteAperturaCompilazioneGuidataSoggetto">compilazione guidata</a>
									</span>
								</div>
							</div>
						</div>
					</div>
					<p class="margin-medium">
						<s:include value="/jsp/include/indietro.jsp" />
						<button type="button" class="btn btn-secondary reset" id="btnAnnulla">annulla</button>
						<span class="pull-right">
							<s:submit cssClass="btn btn-primary" value="prosegui" />
						</span>
					</p>
					<s:include value="/jsp/soggetto/selezionaSoggetto_modale.jsp" />
				</s:form>
			</div>
		</div>
	</div>

	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/codiceFiscale.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/soggetto/ricerca.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/provvisorioDiCassa/associaQuote_step1.js"></script>
	
</body>
</html>