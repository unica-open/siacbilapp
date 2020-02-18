<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<fieldset class="form-horizontal step-pane" id="fieldsetSlideSpezzaQuotaElenco">
	<s:hidden name="subdocumentoEntrata.uid" />
	<s:hidden name="documentoEntrata.uid" />
	<s:hidden name="elencoDocumentiAllegato.uid" />
	<s:hidden name="subdocumentoEntrata.importoDaDedurre" />

	<h4 class="step-pane">Quota originale </h4>
	<div class="control-group">
		<label class="control-label">Movimento contabile</label>
		<div class="controls">
			<s:textfield disabled="true" value="%{subdocumentoEntrata.accertamento.annoMovimento + '/' + subdocumentoEntrata.accertamento.numero.toPlainString()}" cssClass="span4 soloNumeri" />
		</div>
	</div>
	
	<div class="control-group">
		<label class="control-label">Numero quota</label>
		<div class="controls">
			<s:textfield disabled="true" cssClass="span3" name="subdocumentoEntrata.numero" />
			<span class="al">
				<label class="radio inline">Importo quota</label>
			</span>
			<s:textfield readonly="true" cssClass="span3" name="subdocumentoEntrata.importo" />
		</div>
	</div>
	<div class="control-group">
		<label class="control-label">Data scadenza</label>
		<div class="controls">
			<s:textfield disabled="true" cssClass="span2" name="subdocumentoEntrata.dataScadenza" />
			<span class="al">
				<label class="radio inline">Provvisorio di cassa</label>
			</span>
			<s:textfield disabled="true" cssClass="span3" name="subdocumentoEntrata.provvisorioCassa.numero" maxlength="8" />
		</div>
	</div>
	
	<h4 class="step-pane">Nuova quota</h4>
	<div class="control-group">
		<label class="control-label" for="nuovoImpQuotaSlide">Nuovo importo quota *</label>
		<div class="controls">
			<s:textfield cssClass="span3 soloNumeri decimale" name="nuovoSubdocumentoEntrata.importo" id="nuovoImpQuotaSlide" />
		</div>
	</div>

	<div class="control-group">
		<label class="control-label">Provvisorio di cassa</label>
		<div class="controls">
			<input type="hidden" id="slide_hidden_tipoProvvisorioDiCassa" value="E" />
			<span class="al">
				<label class="radio inline" for="nuovoAnnoPcSlide">Anno</label>
			</span>
			<s:textfield cssClass="span2 soloNumeri" name="nuovoSubdocumentoEntrata.provvisorioCassa.anno" id="nuovoAnnoPcSlide" />
			<span class="al">
				<label class="radio inline" for="nuovoNumeroPcSlide">Numero</label>
			</span>
			<s:textfield cssClass="span3 soloNumeri" name="nuovoSubdocumentoEntrata.provvisorioCassa.numero" id="nuovoNumeroPcSlide" maxlength="8" />
			<span class="radio guidata">
				<button type="button" class="btn btn-primary" id="pulsanteCompilazioneGuidataProvvisorioCassa">compilazione guidata</button>
			</span>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="nuovaDataScadenzaSlide">Data di scadenza *</label>
		<div class="controls">
			<s:textfield cssClass="span3 datepicker" name="nuovoSubdocumentoEntrata.dataScadenza" id="nuovaDataScadenzaSlide" />
		</div>
	</div>
	
</fieldset>

<div>
	<button type="button" id="pulsanteAnnullaSpezzaQuotaElenco" class="btn">annulla</button>
	<button type="button" class="btn btn-primary" id="pulsanteConfermaSpezzaQuotaElenco">
		conferma&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_pulsanteConfermaSpezzaQuotaElenco"></i>
	</button>
</div>
