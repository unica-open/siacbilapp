<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<fieldset class="form-horizontal" id="fieldsetModaleAggiornamentoQuotaElenco">
	<s:hidden name="subdocumentoSpesa.uid" id="uidModale" />
	<s:hidden name="documentoSpesa.uid" id="uidDocumentoModale" />
	<s:hidden name="elencoDocumentiAllegato.uid" id="uidElencoModale" />
	<s:hidden name="subdocumentoSpesa.importoDaDedurre" id="importoDaDedurreModale" />
	<s:hidden name="subdocumentoSpesa.impegno.uid" />
	<s:hidden name="subdocumentoSpesa.subImpegno.uid" />
	<s:hidden name="subdocumentoSpesa.impegno.siopeTipoDebito.codice" />
	<s:hidden name="subdocumentoSpesa.subImpegno.siopeTipoDebito.codice" />

	<h4 class="step-pane">Aggiornamento dati quota</h4>
	<div class="control-group">
		<label class="control-label" for="cigModale">CIG</label> 
		<div class="controls">
			<s:textfield cssClass="span3 forzaMaiuscole" data-allowed-chars="[A-Za-z0-9]" name="subdocumentoSpesa.cig" id="cigModale" />
			<s:if test="impegnoTipoDebitoCommerciale">
				<span class="al">
					<label class="radio inline" for="siopeAssenzaMotivazioneModale">Motivo di assenza CIG</label>
				</span>
				<s:select list="listaSiopeAssenzaMotivazione" cssClass="span4" id="siopeAssenzaMotivazioneModale" name="subdocumentoSpesa.siopeAssenzaMotivazione.uid"
					headerKey="0" headerValue="" listKey="uid" listValue="%{codice + ' - ' + descrizione}" />
			</s:if><s:else>
				<s:hidden name="subdocumentoSpesa.siopeAssenzaMotivazione.uid" value="0" />
			</s:else>
		</div>
	</div>

	<div class="control-group">
		<label class="control-label" for="cupModale">CUP</label>
		<div class="controls">
			<s:textfield cssClass="span3 forzaMaiuscole" data-allowed-chars="[A-Za-z0-9]" name="subdocumentoSpesa.cup" id="cupModale" />
		</div>
	</div>
	<div class="control-group">
		<label for="importoModale" class="control-label">Importo</label>
		<div class="controls">
			<s:textfield id="importoModale" name="subdocumentoSpesa.importo" cssClass="span4 soloNumeri decimale" />
		</div>
	</div>
	
	<div id="divModalitaPagamento" class="step-pane active">
		<div class="accordion">
			<div class="accordion-group">
				<div class="accordion-heading">
					<a href="#collapseModalitaPagamento" data-parent="#divModalitaPagamento" data-toggle="collapse" class="accordion-toggle">
						 Modalit&agrave; pagamento <span class="datiPagamento" id="SPAN_modalitaPagamentoSoggetto"></span><span class="icon">&nbsp;</span></a>
				</div>
				<div class="accordion-body collapse in" id="collapseModalitaPagamento">
					<s:include value="/jsp/soggetto/accordionModalitaPagamentoSoggetto.jsp" />
				</div>
			</div>
		</div>
	</div>
	
	<div class="control-group">
		<label class="control-label">Provvisorio di cassa</label>
		<div class="controls">
			<input type="hidden" id="tipoProvvisorioDiCassaModale" value="S" />
			<span class="al">
				<label class="radio inline" for="annoProvvisorioCassaModale">Anno</label>
			</span>
			<s:textfield cssClass="span2 soloNumeri" name="subdocumentoSpesa.provvisorioCassa.anno" id="annoProvvisorioCassaModale" />
			<span class="al">
				<label class="radio inline" for="numeroProvvisorioCassaModale">Numero</label>
			</span>
			<s:textfield cssClass="span3 soloNumeri" name="subdocumentoSpesa.provvisorioCassa.numero" id="numeroProvvisorioCassaModale" maxlength="8" />
			<span class="radio guidata">
				<button type="button" class="btn btn-primary" id="pulsanteCompilazioneGuidataProvvisorioCassaModaleAggiornamentoQuota">compilazione guidata</button>
			</span>
		</div>
	</div>
</fieldset>
<div>
	<button type="button" id="pulsanteAnnullaModaleAggiornamentoQuotaElenco" class="btn">annulla</button>
	<button type="button" class="btn btn-primary" id="pulsanteConfermaModaleAggiornamentoQuotaElenco">
		conferma&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_pulsanteConfermaModaleAggiornamentoQuotaElenco"></i>
	</button>
</div>
