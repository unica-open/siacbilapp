<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<h4 class="step-pane">Documenti collegati</h4> 
		<table class="table table-hover tab_left" summary="...." id="tabellaDocumentiCollegatiEntrata">
			<thead>
				<tr>
					<th scope="col">Tipo</th>
					<th scope="col">Documento</th>
					<th scope="col">Data</th>
					<th scope="col">Stato</th>
					<th scope="col">Soggetto</th>
					<th scope="col">Utente ultima modifica</th>
					<th scope="col" class="tab_Right">Importo</th>
					<th scope="col" class="tab_Right">Importo da dedurre su fattura</th>
					<th scope="col" class="tab_Right"></th>
				</tr>
			</thead>
			<tbody>
			</tbody>
			<tfoot>
			</tfoot>
		</table>
	
	<div id="dettQuotaSpesa">
	<s:include value="/jsp/documento/dettaglioQuoteDocumentoSpesa_Modale.jsp" />
	</div>
	
	<div id="dettQuotaEntrata">
	<s:include value="/jsp/documento/dettaglioQuoteDocumentoEntrata_Modale.jsp" />
	</div>
	
	<div id="dettQuoteNoteCredito" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="dettQuoteCredLabel" aria-hidden="true">
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
		<h4>Dettaglio quote</h4>
	</div>
	<div class="modal-body"> 
		<h4>Totale Note: <span id="modaleNoteCredito_totaleNoteCredito"></span> - 
			Totale Importo Da Dedurre Su Fattura: <span id="modaleNoteCredito_totaleImportoDaDedurreSuFattura"></span> </h4>	
			<table class="table table-hover tab_left" summary="...." id="tabellaDettaglioQuoteNoteCredito">
				<thead>
					<tr id="txt_Left">
						<th scope="col">Numero</th>
						<th scope="col">Accertamento</th>
						<th scope="col">Provvedimento</th>
						<th scope="col">Ordinativo</th>
						<th scope="col">Provvisorio</th>
						<th class="tab_Right" scope="col">Importo da dedurre</th>
						<th class="tab_Right" scope="col">Importo</th>
					</tr>
				</thead>
				<tbody>
				</tbody>
				<tfoot>
					<tr>
						<th colspan="5">Totali</th>
						<th id="totaleNoteCredito_totaleImportoDaDedurre" class="tab_Right"></th>
						<th id="totaleNoteCredito_totaleImporto"class="tab_Right"></th>
					</tr>
				</tfoot>
			</table>
	</div>
	<div class="modal-footer">
			<button class="btn btn-primary" data-dismiss="modal" aria-hidden="true">chiudi</button>
		</div>
</div>
	