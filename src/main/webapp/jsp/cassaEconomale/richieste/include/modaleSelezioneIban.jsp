<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<div aria-hidden="true" aria-labelledby="labelModaleSelezioneIban" role="dialog" tabindex="-1" class="modal hide fade" id="modaleSelezioneIban">
	<div class="modal-header">
		<button aria-hidden="true" data-dismiss="modal" class="close" type="button">&times;</button>
		<h4 class="nostep-pane" id="labelModaleSelezioneIban">Seleziona l'<abbr title="International Bank Account Number">IBAN</abbr></h4>
	</div>
	<div class="modal-body">
		<div class="alert alert-error hide" id="ERRORI_modaleSelezioneIban">
			<button type="button" class="close" data-hide="alert">&times;</button>
			<strong>Attenzione!!</strong><br>
			<ul>
			</ul>
		</div>
		<div id="selezioneIbanModaleSelezioneIban">
			<h4>Elenco IBAN per il dipendente</h4>
			<table class="table table-hover tab_left" id="tabellaModaleSelezioneIban">
				<thead>
					<tr>
						<th></th>
						<th>Codice</th>
					</tr>
				</thead>
				<tbody>
				</tbody>
			</table>
		</div>
	</div>
	<div class="modal-footer">
		<button type="button" class="btn btn-primary" id="pulsanteConfermaModaleSelezioneIban">conferma&nbsp;<i class="icon-spin icon-refresh spinner" id="spinnerModaleSelezioneIban"></i></button>
	</div>
</div>