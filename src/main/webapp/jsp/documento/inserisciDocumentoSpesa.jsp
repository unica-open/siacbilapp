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
				<s:hidden name="nomeAzioneSAC" id="nomeAzioneSAC" />
				<s:form cssClass="form-horizontal" action="inserisciDocumentoSpesaEnterStep2" id="formInserimentoDocumentoSpesa" novalidate="novalidate" >
					<s:include value="/jsp/include/messaggi.jsp" />
					
					<h3>Inserimento documenti spesa</h3>
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
								<div class="control-group">
									<label for="tipoDocumento" class="control-label">Tipo *</label>
									<div class="controls">
										<s:if test="%{inibisciModificaDatiImportatiFEL}">
											<s:select list="listaTipoDocumento" cssClass="span6" id="tipoDocumento" name="documento.tipoDocumento.uid" headerKey="0" headerValue=""
												listKey="uid" listValue="%{codice + ' - ' + descrizione}" required="true" disabled="true"/>
												<s:hidden name="documento.tipoDocumento.uid"/>
										</s:if><s:else>
											<s:select list="listaTipoDocumento" cssClass="span6" id="tipoDocumento" name="documento.tipoDocumento.uid" headerKey="0" headerValue=""
												listKey="uid" listValue="%{codice + ' - ' + descrizione}" required="true" />
										</s:else>
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
										<s:textfield id="annoDocumento" name="documento.anno" cssClass="lbTextSmall span2" required="true" placeholder="anno" maxlength="4" readonly="%{inibisciModificaDatiImportatiFEL}"/>
										<span class="alRight">
											<label for="numeroDocumento" class="radio inline">Numero *</label>
										</span>
										<s:textfield id="numeroDocumento" name="documento.numero" cssClass="lbTextSmall span2" required="true" placeholder="numero" maxlength="200" readonly='%{fatturaFEL != null}'/>
										<span class="alRight">
											<label for="dataEmissioneDocumento" class="radio inline">Data emissione *</label>
										</span>
										<s:if test="%{fatturaFEL != null}">
											<s:textfield id="dataEmissioneDocumento" name="documento.dataEmissione" cssClass="lbTextSmall span2" size="10" required="required" disabled="true" readonly="true"/>
											<s:hidden name="documento.dataEmissione" />
										</s:if>
										<s:else>
											<s:textfield id="dataEmissioneDocumento" name="documento.dataEmissione" cssClass="lbTextSmall span2 datepicker" size="10" required="required"/>
										</s:else>
										<span class="alRight">
											<label for="dataRicezionePortaleDocumento" class="radio inline">Data ricezione</label>
										</span>
										<s:if test='%{fatturaFEL != null}'>
											<s:textfield id="dataRicezionePortaleDocumento" name="documento.dataRicezionePortale" cssClass="lbTextSmall span2" size="10" disabled="true" readonly="true" tabindex="-1" />
											<s:hidden name="documento.dataRicezionePortale" />
										</s:if>
										<s:else>
											<s:textfield id="dataRicezionePortaleDocumento" name="documento.dataRicezionePortale" cssClass="lbTextSmall span2 datepicker" size="10" tabindex="-1" />
										</s:else>
										
									</div>
								</div>
								
								<h4 class="step-pane">Soggetto
									<span id="descrizioneCompletaSoggetto">
										<s:if test='%{soggetto != null && (soggetto.codiceSoggetto != null && soggetto.codiceSoggetto != "")}'>
											<s:property value="soggetto.codiceSoggetto" />
										</s:if>
										<s:if test='%{soggetto != null && (soggetto.denominazione != null && soggetto.denominazione != "")}'>
											- <s:property value="soggetto.denominazione" />
										</s:if>
										<s:if test='%{soggetto != null && (soggetto.codiceFiscale != null && soggetto.codiceFiscale != "")}'>
											- <s:property value="soggetto.codiceFiscale" />
										</s:if>
									</span>
								</h4>
								<s:hidden id="HIDDEN_soggettoDenominazione" name="soggetto.denominazione" />
								<s:hidden id="HIDDEN_soggettoCodiceFiscale" name="soggetto.codiceFiscale" />
								<div class="control-group">
									<label class="control-label" for="codiceSoggetto">Codice *</label>
									<div class="controls">
										<s:textfield id="codiceSoggetto" cssClass="lbTextSmall span2" name="soggetto.codiceSoggetto" maxlength="20" placeholder="codice" required="required" readonly='%{soggetto != null && (soggetto.codiceSoggetto != null && soggetto.codiceSoggetto != "")}'/>
										<span class="radio guidata">
											<a href="#" class="btn btn-primary" id="pulsanteAperturaCompilazioneGuidataSoggetto">compilazione guidata</a>
										</span>
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
								<div class="control-group">
									<label for="beneficiarioMultiplo" class="control-label">Beneficiario multiplo</label>
									<div class="controls">
										<s:checkbox id="beneficiarioMultiplo" name="documento.flagBeneficiarioMultiplo" />
									</div>
								</div>
								<div class="control-group">
									<label for="collegatoCEC" class="control-label">Collegato a Cassa Economale</label>
									<div class="controls">
										<s:checkbox id="collegatoCEC" name="documento.collegatoCEC" />
									</div>
								</div>
						</div>
					</div>

					<p class="margin-medium">
						<s:include value="/jsp/include/indietro.jsp" />
						<s:a cssClass="btn" action="inserisciDocumentoSpesaAnnullaStep1">annulla</s:a>
						<s:submit cssClass="btn btn-primary pull-right" value="prosegui" />
					</p>
				</s:form>
			</div>
		</div>
	</div>
	
	<s:include value="/jsp/soggetto/selezionaSoggetto_modale.jsp" />
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/documento/ztree.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/codiceFiscale.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/soggetto/ricerca.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/documento/inserisci.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/documento/inserisciSpesa.js"></script>

</body>
</html>