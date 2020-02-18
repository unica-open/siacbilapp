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
					<th>Denominazione</th>
					<th>Indirizzo</th>
					<th>Comune</th>
					<th>Stato</th>
				</tr>
			</thead>
			<tbody>

				<tr>

					<td>
						<s:property value="sedeSecondariaSoggetto.denominazione" />
					</td>
					<td>
						<s:property value="indirizzoCompletoSedeSecondariaSoggetto" />

					<td>
						<s:property value="sedeSecondariaSoggetto.indirizzoSoggettoPrincipale.comune" />
					</td>
					<td>
						<s:property value="sedeSecondariaSoggetto.statoOperativoSedeSecondaria" />
					</td>
				</tr>
					
			</tbody>
		</table>
		
		
	</fieldset>
</div>