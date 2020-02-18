<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<h4>Accertamenti</h4>
<fieldset class="form-horizontal" data-reload-datatable="tabellaAccertamentiCollegati">
	<div class="control-group">
		<span class="control-label">Filtro</span>
		<div class="controls">
			<s:iterator value="listaFiltroAccertamento" var="fa">
				<label class="radio inline">
					<input type="radio" name="filtroAccertamento" value="<s:property value="#fa.codice" />" <s:property value="#fa.checked" /> />&nbsp;<s:property value="#fa.descrizione" />
				</label>
			</s:iterator>
		</div>
	</div>
</fieldset>

<table class="table table-hover table-bordered" id="tabellaAccertamentiCollegati">
	<thead></thead>
	<tbody></tbody>
	<tfoot></tfoot>
</table>
<fieldset class="form-horizontal" id="esportazioneAccertamentiCollegati">
	<button type="button" class="pull-left btn btn-secondary" data-esportazione data-xlsx="false" data-figlio="ACCERTAMENTO" data-filters="input[name='filtroAccertamento']:checked">
		Esporta risultati in Excel <i class="icon-download-alt icon-large"></i>&nbsp;
	</button>
	<button type="button" class="pull-left btn btn-secondary" data-esportazione data-xlsx="true" data-figlio="ACCERTAMENTO" data-filters="input[name='filtroAccertamento']:checked">
		Esporta risultati in Excel (XLSX) <i class="icon-download-alt icon-large"></i>&nbsp;
	</button>
</fieldset>