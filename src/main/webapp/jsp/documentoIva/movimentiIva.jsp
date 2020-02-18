<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<div class="accordion_info">
	<div class="step-pane active">
		<table class="table table-hover tab_left" id="${param.prefix}tabellaMovimentiIva${param.suffix}">
			<thead>
				<tr>
					<th>Aliquota iva</th>
					<th class="tab_Right">&#37;</th>
					<th class="tab_Right">Imponibile</th>
					<th class="tab_Right">Imposta</th>
					<th class="tab_Right">Importo detraibile</th>
					<th class="tab_Right">Importo indetraibile</th>
					<th class="tab_Right">Totale</th>
					<th class="tab_Right">&nbsp;</th>
				</tr>
			</thead>
			<tbody>
			</tbody>
			<tfoot>
				<tr>
					<th colspan="2">Totali</th>
					<th class="tab_Right" id="${param.prefix}totaleImponibileMovimentiIva${param.suffix}"></th>
					<th class="tab_Right" id="${param.prefix}totaleImpostaMovimentiIva${param.suffix}"></th>
					<th class="tab_Right">&nbsp;</th>
					<th class="tab_Right">&nbsp;</th>
					<th class="tab_Right" id="${param.prefix}totaleMovimentiIva${param.suffix}"></th>
					<th class="tab_Right">&nbsp;</th>
				</tr>
			</tfoot>
		</table>
		<p>
			<button type="button" class="btn btn-secondary" id="${param.prefix}bottoneInserisciNuovoMovimentoIva${param.suffix}">
				inserisci nuovo movimento iva&nbsp;
				<i class="icon-spin icon-refresh spinner" id="${param.prefix}SPINNER_bottoneInserisciNuovoMovimentoIva${param.suffix}"></i>
			</button>
		</p>
		
		<div class="collapse" id="${param.prefix}collapseInserisciNuovoMovimentoIva${param.suffix}"></div>
	</div>
	<div class="clear"></div>
</div>