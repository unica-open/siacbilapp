<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<div id="modaleDettaglioQuote" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="dettQuoteLabel" aria-hidden="true">
	<div class="row-fluid">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
			<h4>Dettaglio quote</h4>
		</div>
		<div class="modal-body">
			<div class="alert alert-error hide" id="ERRORI_MODALE_QUOTA">
				<button type="button" class="close" data-hide="alert">&times;</button>
				<strong>Attenzione!!</strong><br>
				<ul></ul>
			</div>
			<table class="table table-hover tab_left" summary="...."
				id="tabella_modaleDettaglioQuote">
				<thead>
					<tr id="txt_Left">
						<th scope="col">Numero</th>
						<th scope="col">Impegno</th>
						<th scope="col">Provvedimento</th>
						<th scope="col">Liquidazione</th>
						<th scope="col">Ordinativo</th>
						<th scope="col">Data emissione</th>
						<th class="tab_Right" scope="col">Importo</th>
					</tr>
				</thead>
				<tbody>
				</tbody>
				<tfoot>
					<tr>
						<th colspan="6">Totali quote</th>
						<th class="tab_Right"><span id="totaleQuote" class="text-right"></span></th>
					</tr>
				</tfoot>
			</table>
		</div>
		<div class="modal-footer">
			<button class="btn btn-primary" data-dismiss="modal" aria-hidden="true">chiudi</button>
		</div>
	</div>
</div>