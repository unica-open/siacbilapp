<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<div class="accordion-inner">
	<fieldset class="form-horizontal">
		<table class="table table-hover tab_left" id="tabellaSediSecondarie">
			<thead>
				<tr>
					<th></th>
					<th>Denominazione</th>
					<th>Indirizzo</th>
					<th>Comune</th>
					<th>Stato</th>
				</tr>
			</thead>
			<tbody>
				<s:iterator value="listaSedeSecondariaSoggettoValide" var="sede">
					<tr>
						<td>
							<input type="radio" name="sedeSecondariaSoggetto.uid" value="<s:property value="%{#sede.uid}"/>"
								<s:if test="%{#sede.uid == sedeSecondariaSoggetto.uid}">checked</s:if> />
						</td>
						<td>
							<s:property value="%{#sede.denominazione}" />
						</td>
						<td>
							<s:property value="%{#sede.indirizzoSoggettoPrincipale.sedime}" />&nbsp;<s:property value="%{#sede.indirizzoSoggettoPrincipale.denominazione}" />,&nbsp;<s:property value="%{#sede.indirizzoSoggettoPrincipale.numeroCivico}" />
						</td>
						<td>
							<s:property value="%{#sede.indirizzoSoggettoPrincipale.comune}" />
						</td>
						<td>
							<s:property value="%{#sede.statoOperativoSedeSecondaria}" />
						</td>
					</tr>
				</s:iterator>
			</tbody>
		</table>
		<button id="pulsanteDeselezionaSedeSecondaria" type="button" class="btn" data-uncheck="sedeSecondariaSoggetto.uid">deseleziona</button>
	</fieldset>
</div>