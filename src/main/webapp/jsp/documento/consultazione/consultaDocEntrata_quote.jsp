<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

 
<h4 class="step-pane">
	<span class="num_result"><s:property value="numeroQuote"/></span> Quote
</h4>
<h4> 
	<span class="alLeft">Totali:</span>
	<span class="alLeft">Totale quote: <s:property value="totaleQuote"/></span>
	<span class="alLeft">Netto: <s:property value="netto"/></span>
	<span class="alLeft">Importo da attribuire: <s:property value="totaleImportoDaAttribuire"/></span>
</h4>

<fieldset class="form-horizontal">
	<div class="accordion" id="accordionQuote${idWorkaround}">
		<table class="table-accordion" id="tabellaElencoQuote">
			<thead>
				<tr>
					<th></th>
				</tr>
			</thead>
			<tbody></tbody>
		</table>
	</div>
</fieldset>