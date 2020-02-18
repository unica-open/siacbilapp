<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<!-- Modal dettQuote -->
<div id="modaleDettaglioQuoteQuote" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="dettQuoteLabel" aria-hidden="true">
	<div class="row-fluid">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
			<h4>Dettaglio quote</h4>
		</div>
		<div class="modal-body">
			<table class="table table-hover tab_left" summary="...."id="tabella_modaleDettaglioQuoteQuote">
				<thead>
					<tr id="txt_Left">
						<th scope="col">Numero</th>
						<th scope="col">Impegno</th>
						<th scope="col">Provvedimento</th>
						<th scope="col">Liquidazione</th>
						<th scope="col">Ordinativo</th>
						<th scope="col" class="tab_Right">Importo da dedurre</th>
						<th scope="col" class="tab_Right">Importo</th>
					</tr>
				</thead>
				<tbody>
				</tbody>
				<tfoot>
				</tfoot>
			</table>
		</div>
	</div>
	<div class="modal-footer">
		<button class="btn" data-dismiss="modal">chiudi</button>
	</div>
</div>
<!--end Modal --->