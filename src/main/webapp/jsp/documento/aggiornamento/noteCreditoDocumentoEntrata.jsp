<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<fieldset class="form-horizontal">
<h4>
	Importo documento: 
	<span id=importo_tabNote><s:property value="documento.importo" /></span>
	Totale quote: 
	<span id=totaleQuote_tabNote><s:property value="totaleQuote" /></span>
	<span id=stato_tabNote><s:property value="stato" /></span>
</h4>

<h4 class="step-pane">Note credito</h4>
<table summary="...." class="table table-hover tab_left" id="tabellaNoteCreditoDocumento">
	<thead>
		<tr>
			<th scope="col">Documento</th>
			<th scope="col">Data</th>
			<th scope="col">Stato</th>
			<th scope="col">Soggetto</th>
			<th scope="col" class="tab_Right">Importo da dedurre su fattura</th>
			<th scope="col" class="tab_Right">Importo</th>
			<th scope="col" class="tab_Right"></th>
		</tr>
	</thead>
	<tbody>
	</tbody>
	<tfoot>
		<tr>
			<th colspan="4">Totale</th>
			<th class="tab_Right" id="thtotaleDaDedurreSuFattura"></th>
			<th class="tab_Right" id="thtotaleNoteCredito"></th>
			<th class="tab_Right"></th>
		</tr>
	</tfoot>
</table>

	<p>
		<s:if test="checkStatoValidoPerInserimentoNota">
			<a id="pulsanteInserimentoNuovaNotaCredito" class="btn btn-secondary">
				inserisci nuova nota&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_pulsanteInserisciNuovaNotaCredito"></i>
			</a>
			<s:hidden id="HIDDEN_documentoPerNOTECREDITO_Uid" name="documento.uid" />
			<a id="pulsanteCollegaEntrataNotaCreditoEsistente" class="btn btn-secondary">
				collega nota esistente&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_pulsanteCollegaEntrataNotaCreditoEsistente"></i>
			</a>
			</s:if>
		<a id="pulsanteDettaglioQuoteNotaCredito" class="btn btn-secondary">
			dettaglio quote&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_pulsanteDettaglioQuoteNotaCredito"></i>
		</a>
		
	</p>

	<%-- Collapse inserisci nuova notaCredito --%>
	<div id="accordionInserisciNotaCreditoEntrata" class="collapse">
	</div>
</fieldset>

<div aria-hidden="true" aria-labelledby="msgAnnullaLabel" role="dialog" tabindex="-1" class="modal hide fade" id="modaleConfermaAnnullamentoNotaCredito">
	<div class="modal-body">
		<div class="alert alert-error alert-persistent">
			<p><strong>Attenzione!</strong></p>
			<p><strong>Elemento selezionato: <span id="SPAN_elementoSelezionatoAnnullamentoNotaCredito"></span></strong></p>
			<p>Stai per annullare l'elemento selezionato, questo cambier&agrave; lo stato dell'elemento: sei sicuro di voler proseguire?</p>
		</div>
	</div>
	<div class="modal-footer">
		<a href="#" class="btn" id="pulsanteNoEliminazioneNotaCredito">no, indietro</a>
		<a href="#" class="btn btn-primary" id="pulsanteSiEliminazioneNotaCredito">
			s&iacute;, prosegui&nbsp;&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_pulsanteEliminaNotaCredito"></i>
		</a>
	</div>
</div>
<div aria-hidden="true" aria-labelledby="msgScollegaLabel" role="dialog" tabindex="-1" class="modal hide fade" id="modaleConfermaScollegaNotaCredito">
	<div class="modal-body">
		<div class="alert alert-error alert-persistent">
			<p><strong>Attenzione!</strong></p>
			<p><strong>Elemento selezionato: <span id="SPAN_elementoSelezionatoScollegaNotaCredito"></span></strong></p>
			<p>Stai per scollegare l'elemento selezionato: sei sicuro di voler proseguire?</p>
		</div>
	</div>
	<div class="modal-footer">
		<a href="#" class="btn" id="pulsanteNoScollegaNotaCredito">no, indietro</a>
		<a href="#" class="btn btn-primary" id="pulsanteSiScollegaNotaCredito">
			s&iacute;, prosegui&nbsp;&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_pulsanteScollegaNotaCredito"></i>
		</a>
	</div>
</div>
<p class="margin-medium">
	<s:include value="/jsp/include/indietro.jsp" />
</p>
<s:include value="/jsp/documento/aggiornamento/include/ricercaEntrataNotaCredito_Modale.jsp" />
<s:include value="/jsp/documento/aggiornamento/dettaglioQuoteNoteCreditoDocumentoEntrata_Modale.jsp" />
