<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<h4>Impegni</h4>
<fieldset class="form-horizontal" data-reload-datatable="tabellaImpegniCollegati">
	<div class="control-group">
		<span class="control-label">Filtro</span>
		<div class="controls">
			<s:iterator value="listaFiltroImpegno" var="fi">
				<label class="radio inline">
					<input type="radio" name="filtroImpegno" value="<s:property value="#fi.codice" />" <s:property value="#fi.checked" /> />&nbsp;<s:property value="#fi.descrizione" />
				</label>
			</s:iterator>
		</div>
	</div>
</fieldset>

<table class="table table-hover table-bordered" id="tabellaImpegniCollegati">
	<thead></thead>
	<tbody></tbody>
	<tfoot></tfoot>
</table>
<fieldset class="form-horizontal" id="esportazioneImpegniCollegati">
	<button type="button" class="pull-left btn btn-secondary" data-esportazione data-xlsx="false" data-figlio="IMPEGNO" data-filters="input[name='filtroImpegno']:checked">
		Esporta risultati in Excel <i class="icon-download-alt icon-large"></i>&nbsp;
	</button>
	<button type="button" class="pull-left btn btn-secondary" data-esportazione data-xlsx="true" data-figlio="IMPEGNO" data-filters="input[name='filtroImpegno']:checked">
		Esporta risultati in Excel (XLSX) <i class="icon-download-alt icon-large"></i>&nbsp;
	</button>
</fieldset>