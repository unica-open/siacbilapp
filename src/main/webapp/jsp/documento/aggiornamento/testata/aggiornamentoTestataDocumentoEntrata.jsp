<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<s:form id="formAggiornamentoDatiDocumento" cssClass="form-horizontal" novalidate="novalidate" action="aggiornamentoTestataDocumentoEntrata_aggiornamentoAnagrafica">
	<h4 class="step-pane">Dati principali</h4>

	<div class="control-group">
		<label for="dataEmissioneDocumento" class="control-label">Data *</label>
		<div class="controls">
			<s:textfield id="dataEmissioneDocumento" name="documento.dataEmissione" cssClass="lbTextSmall span2 datepicker" size="10" required="required" />
		</div>
	</div>
	<h4 class="step-pane">Altri dati</h4>
<!--  	<s:hidden name="documento.importo" /> -->
	<div class="control-group">
			<label class="control-label" for="importoDocumento">Importo *</label>
			<div class="controls">
			<s:textfield id="importoDocumento" cssClass="lbTextSmall span2 soloNumeri decimale" name="documento.importo" required="required" />
			</div>
	</div>
	<div class="control-group">
		<label for="descrizioneDocumento" class="control-label">Descrizione *</label>
		<div class="controls">
			<s:textarea id="descrizioneDocumento" name="documento.descrizione" cols="15" rows="2" cssClass="span10" required="required"></s:textarea>
		</div>
	</div>
	<div class="control-group">
		<label for="dataScadenzaDocumento" class="control-label">Data scadenza</label>
		<div class="controls">
			<s:textfield id="dataScadenzaDocumento" name="documento.dataScadenza" cssClass="lbTextSmall span2 datepicker" placeholder="data scadenza" />
		</div>
	</div>
	
	<s:if test="documentoIncompleto">
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
	</s:if>

	<div class="Border_line"></div>
	<p class="margin-medium">
		<s:include value="/jsp/include/indietro.jsp" />
		<button type="reset" class="btn reset">annulla</button>
		<button type="submit" class="btn btn-primary pull-right">aggiorna</button>
	</p>
	<s:include value="/jsp/soggetto/selezionaSoggetto_modale.jsp" />
</s:form>