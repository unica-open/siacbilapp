<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<h4>Ordinativi</h4>
<fieldset class="form-horizontal" data-reload-datatable="tabellaOrdinativiCollegati">
	<div class="control-group">
		<span class="control-label">Filtro</span>
		<div class="controls">
			<s:iterator value="listaFiltroOrdinativo" var="fo">
				<label class="radio inline">
					<input type="radio" name="filtroOrdinativo" value="<s:property value="#fo.codice" />" <s:property value="#fo.checked" /> />&nbsp;<s:property value="#fo.descrizione" />
				</label>
			</s:iterator>
		</div>
	</div>
</fieldset>

<table class="table table-hover table-bordered" id="tabellaOrdinativiCollegati">
	<thead></thead>
	<tbody></tbody>
	<tfoot></tfoot>
</table>
<fieldset class="form-horizontal" id="esportazioneOrdinativiCollegati">
	<button type="button" class="pull-left btn btn-secondary" data-esportazione data-xlsx="false" data-figlio="MANDATO" data-filters="input[name='filtroOrdinativo']:checked">
		Esporta risultati in Excel <i class="icon-download-alt icon-large"></i>&nbsp;
	</button>
	<button type="button" class="pull-left btn btn-secondary" data-esportazione data-xlsx="true" data-figlio="MANDATO" data-filters="input[name='filtroOrdinativo']:checked">
		Esporta risultati in Excel (XLSX) <i class="icon-download-alt icon-large"></i>&nbsp;
	</button>
</fieldset>