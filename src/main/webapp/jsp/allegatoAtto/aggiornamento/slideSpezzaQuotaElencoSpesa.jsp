<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<fieldset class="form-horizontal" id="fieldsetSlideSpezzaQuotaElenco">
	<s:hidden name="subdocumentoSpesa.uid" />
	<s:hidden name="documentoSpesa.uid" />
	<s:hidden name="elencoDocumentiAllegato.uid" />
	<s:hidden name="subdocumentoSpesa.importoDaDedurre" />

	<h4 class="step-pane">Quota originale</h4>
	<div class="control-group">
		<label class="control-label">Movimento contabile</label>
		<div class="controls">
			<s:textfield disabled="true" value="%{subdocumentoSpesa.impegno.annoMovimento + '/' + subdocumentoSpesa.impegno.numero.toPlainString()}" cssClass="span4 soloNumeri" />
		</div>
	</div>
	
	<div class="control-group">
		<label class="control-label">Numero quota</label>
		<div class="controls">
			<s:textfield disabled="true" cssClass="span3" name="subdocumentoSpesa.numero" />
			<span class="al">
				<label class="radio inline">Importo quota</label>
			</span>
			<s:textfield readonly="true" cssClass="span3" name="subdocumentoSpesa.importo" />
		</div>
	</div>
	<div class="control-group">
		<label class="control-label">CIG</label>
		<div class="controls">
			<s:textfield disabled="true" cssClass="span3" name="subdocumentoSpesa.cig" />
			<s:if test="impegnoTipoDebitoCommerciale">
				<span class="al">
					<label class="radio inline" for="siopeAssenzaMotivazioneModale">Motivo di assenza CIG</label>
				</span>
				<s:select disabled="true" list="listaSiopeAssenzaMotivazione" cssClass="span4" id="siopeAssenzaMotivazioneModale" name="subdocumentoSpesa.siopeAssenzaMotivazione.uid"
					headerKey="0" headerValue="" listKey="uid" listValue="%{codice + ' - ' + descrizione}" />
					<s:hidden name="subdocumentoSpesa.siopeAssenzaMotivazione.uid" />
			</s:if><s:else>
				<s:hidden name="subdocumentoSpesa.siopeAssenzaMotivazione.uid" value="0" />
			</s:else>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label">CUP</label>
		<div class="controls">
			<s:textfield disabled="true" cssClass="span3" name="subdocumentoSpesa.cup" />
			<span class="al">
				<label class="radio inline">Data scadenza</label>
			</span>
			<s:textfield disabled="true" cssClass="span2" name="subdocumentoSpesa.dataScadenza" />
		</div>
	</div>
	
	<div class="control-group">
		<label class="control-label">Modalit&agrave; pagamento</label>
		<div class="controls">
			<s:textfield disabled="true" cssClass="span3" name="modalitaPagamentoSoggetto.descrizione" />
			<span class="al">
				<label class="radio inline">Provvisorio di cassa</label>
			</span>
			<s:textfield disabled="true" cssClass="span3" value="%{subdocumentoSpesa.provvisorioCassa.anno + '/' + subdocumentoSpesa.provvisorioCassa.numero}" />
		</div>
	</div>
	
	<div class="control-group">
		<label class="control-label">Tipo IVA Split/Reverse</label>
		<div class="controls">
			<s:textfield disabled="true" cssClass="span3" name="subdocumentoSpesa.tipoIvaSplitReverse.descrizione" />
			<span class="al">
				<label class="radio inline">Importo IVA Split</label>
			</span>
			<s:textfield readonly="true" cssClass="span3" name="subdocumentoSpesa.importoSplitReverse" />
			<span class="al">
				<label class="radio inline">Liquidazione</label>
			</span>
			<s:textfield disabled="true" cssClass="span2" value="%{subdocumentoSpesa.liquidazione.annoLiquidazione +  '/' +  subdocumentoSpesa.liquidazione.numeroLiquidazione.toPlainString()}" />
		</div>
	</div>

	<h4 class="step-pane">Nuova quota</h4>
	<div class="control-group">
		<label class="control-label" for="nuovoImpQuotaSlide">Nuovo importo quota *</label>
		<div class="controls">
			<s:textfield cssClass="span3 soloNumeri decimale" name="nuovoSubdocumentoSpesa.importo" id="nuovoImpQuotaSlide" />
			<span class="al">
				<label class="radio inline" for="nuovoImpIvaSplitSlide">Nuovo importo IVA Split</label>
			</span>
			<s:textfield cssClass="span3 soloNumeri decimale" name="nuovoSubdocumentoSpesa.importoSplitReverse" id="importoQuotaSlide" />
		</div>
	</div>

	<%-- SIAC-5574, codice solo commentato perche' possibile un cambiamento
	 <div class="control-group">
		<label class="control-label" for="nuovoCigSlide">CIG</label>
		<div class="controls">
			<s:textfield cssClass="span3 soloNumeri" name="nuovoSubdocumentoSpesa.cig" id="nuovoCigSlide" />
			<span class="al">
				<label class="radio inline" for="nuovoCupSlide">CUP</label>
			</span>
			<s:textfield  cssClass="span3" name="nuovoSubdocumentoSpesa.cup" id="nuovoCupSlide" />
		</div>
	</div> --%>
	
	<div id="divModalitaPagamentoSlide" class="step-pane active">
		<div class="accordion">
			<div class="accordion-group">
				<div class="accordion-heading">
					<a href="#collapseModalitaPagamentoSlide" data-parent="#divModalitaPagamentoSlide" data-toggle="collapse" class="accordion-toggle">
						Modalit&agrave; pagamento <span class="datiPagamento" id="SPAN_modalitaPagamentoSoggettoSlide"></span><span class="icon">&nbsp;</span>
					</a>
				</div>
				<div class="accordion-body collapse in" id="collapseModalitaPagamentoSlide">
					<s:include value="/jsp/soggetto/accordionModalitaPagamentoSoggetto.jsp" />
				</div>
			</div>
		</div>
	</div>
	
	<div class="control-group">
		<label class="control-label">Provvisorio di cassa</label>
		<div class="controls">
			<input type="hidden" id="slide_hidden_tipoProvvisorioDiCassa" value="S" />
			<span class="al">
				<label class="radio inline" for="nuovoAnnoPcSlide">Anno</label>
			</span>
			<s:textfield  cssClass="span2 soloNumeri" name="nuovoSubdocumentoSpesa.provvisorioCassa.anno" id="nuovoAnnoPcSlide" />
			<span class="al">
				<label class="radio inline" for="nuovoNumeroPcSlide">Numero</label>
			</span>
			<s:textfield  cssClass="span3 soloNumeri" name="nuovoSubdocumentoSpesa.provvisorioCassa.numero" id="nuovoNumeroPcSlide" maxlength="8" />
			<span class="radio guidata">
				<button type="button" class="btn btn-primary" id="pulsanteCompilazioneGuidataProvvisorioCassa">compilazione guidata</button>
			</span>
		</div>
	</div>
	
	<div class="control-group">
		<label class="control-label" for="nuovaDataScadenzaSlide">Data di scadenza *</label>
		<div class="controls">
			<s:textfield cssClass="span3 datepicker" name="nuovoSubdocumentoSpesa.dataScadenza" id="nuovaDataScadenzaSlide" />
		</div>
	</div>
</fieldset>

<div>
	<button type="button" id="pulsanteAnnullaSpezzaQuotaElenco" class="btn">annulla</button>
	<button type="button" class="btn btn-primary" id="pulsanteConfermaSpezzaQuotaElenco">
		conferma&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_pulsanteConfermaSpezzaQuotaElenco"></i>
	</button>
</div>
