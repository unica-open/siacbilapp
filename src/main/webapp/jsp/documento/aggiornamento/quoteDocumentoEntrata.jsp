<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<fieldset class="form-horizontal">
	<h4 class="nostep-pane">
		Importo documento: <span id="SPAN_importoDocumento"><s:property
				value="documento.importo" /></span> <span class="alLeft">netto:</span> <span
			id="SPAN_nettoDocumento"><s:property value="netto" /></span> <span
			class="alLeft">importo da attribuire:</span> <span
			id="SPAN_importoDaAttribuireDocumento"><s:property
				value="importoDaAttribuire" /></span>
	</h4>
	<h4 class="step-pane">
		Elenco quote: <span id="SPAN_numeroQuoteDocumento"></span>
	</h4>
	<table summary="...." class="table table-hover tab_left"
		id="tabellaQuoteDocumento">
		<thead>
			<tr id="txt_Left">
				<th scope="col">Numero</th>
				<th scope="col">Accertamento</th>
				<th scope="col">Provvedimento</th>
				<th scope="col">Ordinativo</th>
				<th scope="col">Provvisorio</th>
				<th scope="col" class="tab_Right">Importo</th>
				<th scope="col" class="tab_Right"></th>
			</tr>
		</thead>
		<tbody>
		</tbody>
		<tfoot>
			<tr>
				<th colspan="5">Totali quote</th>
				<th class="tab_Right" id="thTotaliQuote"></th>
				<th class="tab_Right"></th>
			</tr>
		</tfoot>
	</table>
	<p>
		<a id="pulsanteInserimentoNuovaQuota" class="btn btn-secondary">
			inserisci nuova quota&nbsp;<i class="icon-spin icon-refresh spinner"
			id="SPINNER_pulsanteNuovaQuota"></i>
		</a>
	</p>

	<%-- Collapse inserisci nuova quota e ripeti quota--%>
	<div id="accordionInserisciQuotaEntrata"></div>
</fieldset>

<div aria-hidden="true" aria-labelledby="msgAnnullaLabel" role="dialog"
	tabindex="-1" class="modal hide fade"
	id="modaleConfermaEliminazioneQuota">
	<div class="modal-body">
		<div class="alert alert-error alert-persistent">
			<p>
				<strong>Attenzione!</strong>
			</p>
			<p>
				<strong>Elemento selezionato: <span
					id="SPAN_elementoSelezionatoEliminazioneQuota"></span></strong>
			</p>
			<p>Stai per annullare l'elemento selezionato, questo
				cambier&agrave; lo stato dell'elemento: sei sicuro di voler
				proseguire?</p>
		</div>
	</div>
	<div class="modal-footer">
		<a href="#" class="btn" id="pulsanteNoEliminazioneQuota">no,
			indietro</a> <a href="#" class="btn btn-primary"
			id="pulsanteSiEliminazioneQuota"> s&iacute;, prosegui&nbsp;<i
			class="icon-spin icon-refresh spinner"
			id="SPINNER_pulsanteEliminaQuota"></i>
		</a>
	</div>
</div>

<p class="margin-medium">
	<s:include value="/jsp/include/indietro.jsp" />
</p>