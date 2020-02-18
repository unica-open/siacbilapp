<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<div class="accordion-inner">
	<fieldset class="form-horizontal">
	<table class="table table-hover tab_left" id="tabellaModalitaPagamento">
			<thead>
				<tr>
					<th class="span2">Numero d'ordine</th>
					<th class="span6">Modalit&agrave;</th>
					<th class="span2"><abbr title="progressivo">Associato a</abbr></th>
					<th class="span2">Stato</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td><s:property value="modalitaPagamentoSoggetto.codiceModalitaPagamento" /></td>
					<td><s:property value="modalitaPagamentoSoggetto.descrizioneInfo.descrizioneArricchita" /></td>
					<td><s:property value="modalitaPagamentoSoggetto.associatoA" /></td>
					<td><s:property value="modalitaPagamentoSoggetto.descrizioneStatoModalitaPagamento" /></td>
				</tr>
				
			</tbody>
		</table>
		
	</fieldset>
</div>