<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<div class="accordion-inner">
	<fieldset class="form-horizontal">
		<table class="table table-hover tab_left" id="tabellaDatiSospensioneQuota">
			<thead>
				<tr>
					<th>Data</th>
					<th>Causale</th>
					<th>Data riattivazione</th>
					<th></th>
				</tr>
			</thead>
			<tbody>
			</tbody>
		</table>
		<s:if test="datiSospensioneQuotaEditabili">
			<button id="aggiungiDatiSospensioneQuota" type="button" class="btn" data-insert-sospensione>Aggiungi</button>
		</s:if>
	</fieldset>
</div>