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
				<s:form cssClass="form-horizontal" action="inserisciTestataDocumentoSpesaEnterStep2" id="formInserimentoDocumentoSpesa" novalidate="novalidate" >
					<s:include value="/jsp/include/messaggi.jsp" />
					
					<h3>Inserimento documenti iva spesa</h3>
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
									<label class="control-label" for="codiceSoggetto">Codice *</label>
									<div class="controls">
										<s:textfield id="codiceSoggetto" cssClass="lbTextSmall span2" name="soggetto.codiceSoggetto" maxlength="20" placeholder="codice" required="required" />
										<span class="radio guidata">
											<a href="#" class="btn btn-primary" id="pulsanteAperturaCompilazioneGuidataSoggetto">compilazione guidata</a>
										</span>
									</div>
								</div>
							</fieldset>
						</div>
					</div>

					<p class="margin-medium">
						<s:include value="/jsp/include/indietro.jsp" />
						<s:reset cssClass="btn" value="annulla" />
						<s:submit cssClass="btn btn-primary pull-right" value="prosegui" />
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
	<script type="text/javascript" src="${jspath}documento/inserisciTestataSpesa.js"></script>

</body>
</html>