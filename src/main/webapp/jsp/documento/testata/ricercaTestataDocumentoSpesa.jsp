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
			<div class="span12 ">
				<div class="span12 contentPage">
					<s:form action="effettuaRicercaTestataDocumentoSpesa" cssClass="form-horizontal" novalidate="novalidate">
						<s:include value="/jsp/include/messaggi.jsp" />
						<s:hidden id="HIDDEN_anno_datepicker" value="%{documento.anno}" />
						<h3><s:property value="labelRicercaEntrataSpesa"/></h3>
						<p>&Egrave; necessario inserire almeno un criterio di ricerca.</p>
						<div class="step-content">
							<div class="step-pane active" id="step1"> <br/>
								<p><s:submit cssClass="btn btn-primary pull-right" value="cerca" /></p>
								<br/>
								<h4><s:property value="labelEntrataSpesa"/></h4>
								<fieldset class="form-horizontal">
									<div class="control-group">
										<label class="control-label" for="tipoDocumento">Tipo documento</label>
										<div class="controls">
											<s:select list="listaTipoDocumento" name="documento.tipoDocumento.uid" id="tipoDocumento" headerKey="" headerValue=""
												listValue="%{codice + ' - ' + descrizione}" listKey="uid" />
											<span class="al">
												<label class="radio inline" for="statoOperativoDocumento">Stato documento</label>
													<s:select id="statoOperativoDocumento" list="listaStatoOperativoDocumento" name="documento.statoOperativoDocumento"
														required="false" headerKey="" headerValue="" listValue="%{codice + ' - ' + descrizione}" />
											</span>
										</div>
									</div>
									<div class="control-group">
										<label class="control-label" for="anno">Anno documento</label>
										<div class="controls">
											<s:textfield id="anno" cssClass="lbTextSmall span2 soloNumeri" name="documento.anno" maxlength="4" placeholder="anno" />
											<span class="al">
												<label class="radio inline" for="numero">Numero documento</label>
											</span>
											<s:textfield id="numero" cssClass="lbTextSmall span2" name="documento.numero" placeholder="numero" />
										</div>
									</div>
									<div class="control-group">
										<label class="control-label" for="dataEmissione">Data documento</label>
										<div class="controls">
											<s:textfield id="dataEmissione" cssClass="lbTextSmall span2 datepicker" name="documento.dataEmissione" placeholder="dataEmissione" />
										</div>
									</div>
									<div class="control-group">
										<label class="control-label" for="flagIva"><s:property value="labelRilevanteIvaEntrataSpesa"/></label>
										<div class="controls">
											<select id="flagIva" name="flagIva">
												<option value=""  <s:if test='flagIva == ""' >selected</s:if>>Non si applica</option>
												<option value="S" <s:if test='flagIva == "S"'>selected</s:if>>S&iacute;</option>
												<option value="N" <s:if test='flagIva == "N"'>selected</s:if>>No</option>
											</select>
										</div>
									</div>
									<div class="control-group">
										<label class="control-label">Elenco</label>
										<div class="controls">
											<span class="al">
												<label class="radio inline" for="elenco.anno">Anno</label>
											</span>
											<s:textfield id="elenco.anno" cssClass="lbTextSmall span2" name="elenco.anno" maxlength="4" placeholder="elenco.anno" />
											<span class="al">
												<label class="radio inline" for="elenco.numero">Numero</label>
											</span>
											<s:textfield id="elenco.numero" cssClass="lbTextSmall span2" name="elenco.numero" placeholder="elenco.numero" />
										</div>
									</div>
								</fieldset>
							
								<h4 class="step-pane">&nbsp;Provvedimento&nbsp;
								<span id="SPAN_InformazioniProvvedimento">
										<s:if test='%{attoAmministrativo != null && (attoAmministrativo.anno ne null && attoAmministrativo.anno != "") && (attoAmministrativo.numero ne null && attoAmministrativo.numero != "") && (tipoAtto.uid ne null && tipoAtto.uid != "")}'>
											<s:property value="%{tipoAtto.descrizione+ '/'+ attoAmministrativo.anno + ' / ' + attoAmministrativo.numero + ' - ' + attoAmministrativo.oggetto}" />
										</s:if>
								</span>
								</h4>
								<fieldset class="form-horizontal">
									<div class="control-group">
										<label class="control-label" for="annoProvvedimento">Anno</label>
										<div class="controls">
											<s:textfield id="annoProvvedimento" cssClass="lbTextSmall span1 soloNumeri" name="attoAmministrativo.anno" />
											<span class="al">
												<label class="radio inline" for="numeroProvvedimento">Numero</label>
											</span>
											<s:textfield id="numeroProvvedimento" cssClass="lbTextSmall span2 soloNumeri" name="attoAmministrativo.numero" maxlength="7" />
											<span class="al">
												<label class="radio inline" for="tipoAttoProvvedimento">Tipo</label>
											</span>
											<s:select list="listaTipoAtto" listKey="uid" listValue="descrizione" name="tipoAtto.uid" id="tipoAttoProvvedimento" cssClass="span4"
												headerKey="" headerValue="" />
											<s:hidden id="HIDDEN_statoProvvedimento" name="attoAmministrativo.statoOperativo" />
											<span class="radio guidata">
												<a href="#" id="pulsanteApriModaleProvvedimento" class="btn btn-primary">compilazione guidata</a>
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
															<span id="SPAN_StrutturaAmministrativoContabile"> Seleziona la Struttura amministrativa </span>
															<i class="icon-spin icon-refresh spinner"></i>
														</a>
													</div>
													<div id="struttAmm" class="accordion-body collapse">
														<div class="accordion-inner">
															<ul id="treeStruttAmm" class="ztree treeStruttAmm"></ul>
															<button type="button" id="pulsanteDelesezionaStrutturaAmministrativoContabile"  class="btn">Deseleziona</button>
														</div>
													</div>
												</div>
											</div>

											<s:hidden id="HIDDEN_StrutturaAmministrativoContabileUid" name="strutturaAmministrativoContabile.uid" />
											<s:hidden id="HIDDEN_StrutturaAmministrativoContabileCodice" name="strutturaAmministrativoContabile.codice" />
											<s:hidden id="HIDDEN_StrutturaAmministrativoContabileDescrizione" name="strutturaAmministrativoContabile.descrizione" />
										</div>
									</div>

								</fieldset> 

								<s:hidden id="HIDDEN_soggettoDenominazione" name="soggetto.denominazione" />
								<s:hidden id="HIDDEN_soggettoCodiceFiscale" name="soggetto.codiceFiscale" />

								<h4 class="step-pane">Soggetto
									<span id="descrizioneCompletaSoggetto">
											<s:if test='%{soggetto != null && (soggetto.codice ne null && soggetto.codice != "") && (soggetto.descrizione ne null && soggetto.descrizione != "") && (soggetto.codiceFiscale ne null && soggetto.codiceFiscale != "")}'>
												<s:property value="%{soggetto.codice + ' - ' + soggetto.descrizione + ' - ' + soggetto.codiceFiscale}" />
											</s:if>
									</span>
								</h4>
								<div class="control-group">
									<label class="control-label" for="codiceSoggetto">Codice </label>
									<div class="controls">
									<s:textfield id="codiceSoggetto" cssClass="lbTextSmall span2" name="soggetto.codiceSoggetto" maxlength="20" placeholder="codice" required="required" />
										<span class="radio guidata">
											<a href="#" id="pulsanteApriModaleSoggetto" class="btn btn-primary">compilazione guidata</a>
										</span>
									</div>
								</div>

								<s:include value="/jsp/soggetto/selezionaSoggetto_modale.jsp" />
								<s:include value="/jsp/provvedimento/selezionaProvvedimento_modale.jsp" />
							</div>
						</div>
						<p class="margin-large">
							<s:include value="/jsp/include/indietro.jsp" />
							<button type="reset" class="btn btn-link">annulla</button>
							<button type="submit" class="btn btn-primary pull-right">cerca</button>
						</p>
					</s:form>
				</div>
			</div>
		</div>
	</div>

	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="${jspath}codiceFiscale.js"></script>
	<script type="text/javascript" src="${jspath}soggetto/ricerca.js"></script>
	<script type="text/javascript" src="${jspath}documento/ztree.js"></script>
	<script type="text/javascript" src="${jspath}provvedimento/ricerca_modale_doc.js"></script>
	<script type="text/javascript" src="${jspath}documento/ricercaSpesa.js"></script>

</body>
</html>