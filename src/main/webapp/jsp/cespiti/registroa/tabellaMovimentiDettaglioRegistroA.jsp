<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<fieldset id="fieldsetTabellaMovimentoPrimaNota" class="form-horizontal">
	<table class="table tab_left dataTable" id="tabellaMovimentiDettaglioRegistroA">
		<thead>
			<tr>
				<th class="span3">Movimento Finanziario</th>
				<th class="span3">Atto di liquidazione</th>
				<th><abbr title="Iva commerciale">Iva Comm.</abbr></th>
				<th class="tab_Right">Importo finanziario</th>
				<th>Conto</th>
				<th class="span3">Voce</th>
				<th class="tab_Right">Importo da inventariare/alineare</th>
				<th class="tab_Right">Importo inventariato/ alineato</th>
				<th class="tab_Right"><abbr title="Numero beni">N. Beni</abbr></th>
				<th class="tab_Right">&nbsp;</th>
			</tr>
		</thead>
	</table>
	<s:hidden id="HIDDEN_tipoCausale" name="tipoCausale.codice"/>
</fieldset>																
