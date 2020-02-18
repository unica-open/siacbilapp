<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<%-- Modal dettQuote --%>
<div id="modaleDettaglioQuote" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="dettQuoteLabel" aria-hidden="true">
	<div class="row-fluid">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
			<h4>Dettaglio quote</h4>
		</div>
		<div class="modal-body">
			<fieldset id="fieldsetModaleDettaglioQuoteNoteCredito">
				<h4>Totale importo da dedurre su fattura: <span id="totaleNoteCreditoModale"></span></h4>		
				<div class="alert alert-error hide" id="ERRORI_MODALE_QUOTA">
					<button type="button" class="close" data-hide="alert">&times;</button>
					<strong>Attenzione!!</strong><br>
					<ul></ul>
				</div>
				<div class="alert alert-success hide" id="INFORMAZIONI_MODALE_QUOTA">
					<button type="button" class="close" data-hide="alert">&times;</button>
					<ul></ul>
				</div>
				<table class="table table-hover tab_left" summary="...."id="tabella_modaleDettaglioQuote">
					<thead>
						<tr id="txt_Left">
							<th scope="col">Numero</th>
							<th scope="col">Impegno</th>
							<th scope="col">Provvedimento</th>
							<th scope="col">Liquidazione</th>
							<th scope="col">Ordinativo</th>
							<th scope="col" class="tab_Right">Importo da dedurre</th>
							<th class="tab_Right" scope="col">Importo</th>
						</tr>
					</thead>
					<tbody>
					</tbody>
					<tfoot>
						<tr>
							<th colspan="5">Totali quote</th>
							<th class="tab_Right"><span id="totaleImportoDaDedurreNotaCreditoModale" class="text-right"></span></th>
		                    <th class="tab_Right"><span id="totaleQuoteNotaCreditoModale" class="text-right"></span></th>
						</tr>
					</tfoot>
				</table>
			</fieldset>
			<p>
				<a class="btn btn-secondary" id="pulsanteApplicaNoteCreditoModale">
					applica&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_pulsanteApplicaNoteCreditoModale"></i>
				</a>
			</p>
		</div>
	</div>
	<div class="modal-footer">
		<button class="btn btn-primary" id="pulsanteConfermaNoteCreditoModale">conferma</button>
	</div>
</div>
<!--end Modal --->