<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<div class="accordion-inner">
	<fieldset class="form-horizontal">
		<s:if test="%{modalitaPagamentoSoggetto != null && modalitaPagamentoSoggetto.modalitaPagamentoSoggettoCessione2 != null && modalitaPagamentoSoggetto.modalitaPagamentoSoggettoCessione2.uid != 0}">
			<s:set scope="action" value="modalitaPagamentoSoggetto.modalitaPagamentoSoggettoCessione2.uid" var="uidModPagSog" />
		</s:if><s:else>
			<s:set scope="action" value="modalitaPagamentoSoggetto.uid" var="uidModPagSog" />
		</s:else>
		<table class="table table-hover tab_left" id="tabellaModalitaPagamento">
			<thead>
				<tr>
					<th class="span1"></th>
					<th class="span2">Numero d'ordine</th>
					<th class="span6">Modalit&agrave;</th>
					<th class="span2"><abbr title="progressivo">Associato a</abbr></th>
					<th class="span1">Stato</th>
				</tr>
			</thead>
			<tbody>
				<s:iterator value="listaModalitaPagamentoSoggettoFiltrate" var="mod">
<%-- 					<s:set var="initializedStringMPS" value="false" /> --%>
					<tr>
						<td>
							<s:if test="%{#mod.modalitaPagamentoSoggettoCessione2 != null && #mod.modalitaPagamentoSoggettoCessione2.uid != 0}">
								<input type="radio" name="modalitaPagamentoSoggetto.uid"
									value="<s:property value="%{#mod.modalitaPagamentoSoggettoCessione2.uid}"/>"
									<s:if test="%{#mod.modalitaPagamentoSoggettoCessione2.uid == #uidModPagSog}">checked</s:if>
									data-codice-modalita-pagamento="<s:property value='%{#mod.modalitaAccreditoSoggetto.codice}'/>" />
							</s:if><s:else>
								<input type="radio" name="modalitaPagamentoSoggetto.uid"
									value="<s:property value="%{#mod.uid}"/>" <s:if test="%{#mod.uid == #uidModPagSog}">checked</s:if>
									data-codice-modalita-pagamento="<s:property value='%{#mod.modalitaAccreditoSoggetto.codice}'/>" />
							</s:else>
						</td>
						<td><s:property value="#mod.codiceModalitaPagamento" /></td>
						<td><s:property value="#mod.descrizioneInfo.descrizioneArricchita" /></td>
						<td><s:property value="%{#mod.associatoA}" /></td>
						<td><s:property value="%{#mod.descrizioneStatoModalitaPagamento}" /></td>
					</tr>
				</s:iterator>
			</tbody>
		</table>
	</fieldset>
</div>