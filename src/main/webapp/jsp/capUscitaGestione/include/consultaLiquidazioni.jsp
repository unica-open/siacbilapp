<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<h4>Liquidazioni</h4>
<fieldset class="form-horizontal" data-reload-datatable="tabellaLiquidazioniCollegate">
	<div class="control-group">
		<span class="control-label">Filtro</span>
		<div class="controls">
			<s:iterator value="listaFiltroLiquidazione" var="fl">
				<label class="radio inline">
					<input type="radio" name="filtroLiquidazione" value="<s:property value="#fl.codice" />" <s:property value="#fl.checked" /> />&nbsp;<s:property value="#fl.descrizione" />
				</label>
			</s:iterator>
		</div>
	</div>
</fieldset>

<table class="table table-hover table-bordered" id="tabellaLiquidazioniCollegate">
	<thead></thead>
	<tbody></tbody>
	<tfoot></tfoot>
</table>
<fieldset class="form-horizontal" id="esportazioneLiquidazioniCollegate">
	<button type="button" class="pull-left btn btn-secondary" data-esportazione data-xlsx="false" data-figlio="LIQUIDAZIONE" data-filters="input[name='filtroLiquidazione']:checked">
		Esporta risultati in Excel <i class="icon-download-alt icon-large"></i>&nbsp;
	</button>
	<button type="button" class="pull-left btn btn-secondary" data-esportazione data-xlsx="true" data-figlio="LIQUIDAZIONE" data-filters="input[name='filtroLiquidazione']:checked">
		Esporta risultati in Excel (XLSX) <i class="icon-download-alt icon-large"></i>&nbsp;
	</button>
</fieldset>