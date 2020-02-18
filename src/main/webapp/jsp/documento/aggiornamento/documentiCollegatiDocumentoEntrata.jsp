<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<h4 class="step-pane">Documenti collegati</h4>
<table summary="...." class="table table-hover tab_left" id="tabellaDocumentiCollegatiDocumento">
	<thead>
		<tr>
			<th scope="col">Documento</th>
			<th scope="col">Data</th>
			<th scope="col">Stato</th>
			<th scope="col">Soggetto</th>
			<th class="tab_Right" scope="col">Importo</th>
		</tr>
	</thead>
	<tbody>
	</tbody>
	<tfoot>
		<tr>
			<th colspan="4">Totale</th>
			<th class="tab_Right"><s:property value="totaleDocumentiCollegati"/></th>
		</tr>
	</tfoot>
</table>
<p>
	<a class="btn btn-secondary" href="aggiornamentoDocumentoEntrata_redirezioneInserimentoDocumentoSpesa.do">inserisci nuovo documento spesa</a>
</p>
<div class="Border_line"></div>
<p>
	<s:include value="/jsp/include/indietro.jsp" />
</p>