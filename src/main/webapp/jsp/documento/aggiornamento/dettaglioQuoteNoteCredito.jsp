<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<div aria-hidden="true" aria-labelledby="dettQuoteCreditoLabel" role="dialog" tabindex="-1" class="modal hide fade" id="dettaglioQuoteNoteCreditoModale">
	<div class="modal-header">
		<button aria-hidden="true" data-dismiss="modal" class="close" type="button">×</button>
		<h4>Dettaglio quote</h4>
	</div>
	<div class="modal-body">
		<fieldset>
			<h4>Totale Note: <span id="totaleNoteCreditoModale"></span></h4>
			<table summary="...." class="table table-hover tab_left" id="tabellaModaleNoteCredito">
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
					<tr>
						<th colspan="5">Totali</th>
						<th class="tab_Right" id="totaleImportoDaDedurreNoteCreditoModale"></th>
						<th class="tab_Right" id="totaleImportoNoteCreditoModale"></th>
					</tr>
				</tfoot>
			</table>
		</fieldset>
		<p>
			<a class="btn btn-secondary" id="pulsanteApplicaNoteCreditoModale">applica</a>
		</p>
	</div>
	<div class="modal-footer">
		<button class="btn btn-primary" id="pulsanteConfermaNoteCreditoModale">conferma</button>
	</div>
</div>