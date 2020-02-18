<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<fieldset class="form-horizontal" id="fieldsetModaleAggiornamentoQuotaElenco">
	<s:hidden name="subdocumentoEntrata.uid" id="uidModale" />
	<s:hidden name="documentoEntrata.uid" id="uidDocumentoModale" />
	<s:hidden name="elencoDocumentiAllegato.uid" id="uidElencoModale" />
	<s:hidden name="subdocumentoEntrata.importoDaDedurre" id="importoDaDedurreModale" />
	
	<h4 class="step-pane">Aggiornamento dati quota</h4>
	<div class="control-group">
		<label for="importoModale" class="control-label">Importo</label>
		<div class="controls">
			<s:textfield id="importoModale" name="subdocumentoEntrata.importo" cssClass="span4 soloNumeri decimale" />
		</div>
	</div>
	
	<div class="control-group">
		<label class="control-label">Provvisorio di cassa</label>
		<div class="controls">
			<input type="hidden" id="tipoProvvisorioDiCassaModale" value="E" />
			<span class="al">
				<label class="radio inline" for="annoProvvisorioCassaModale">Anno</label>
			</span>
			<s:textfield cssClass="span2 soloNumeri" name="subdocumentoEntrata.provvisorioCassa.anno" id="annoProvvisorioCassaModale" />
			<span class="al">
				<label class="radio inline" for="numeroProvvisorioCassaModale">Numero</label>
			</span>
			<s:textfield cssClass="span3 soloNumeri" name="subdocumentoEntrata.provvisorioCassa.numero" id="numeroProvvisorioCassaModale" maxlength="8" />
			<span class="radio guidata">
				<button type="button" class="btn btn-primary" id="pulsanteCompilazioneGuidataProvvisorioCassaModaleAggiornamentoQuota">compilazione guidata</button>
			</span>
		</div>
	</div>
</fieldset>
<div>
	<button type="button" data-dismiss="modal" class="btn">annulla</button>
	<button type="button" class="btn btn-primary" id="pulsanteConfermaModaleAggiornamentoQuotaElenco">
		conferma&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_pulsanteConfermaModaleAggiornamentoQuotaElenco"></i>
	</button>
</div>
