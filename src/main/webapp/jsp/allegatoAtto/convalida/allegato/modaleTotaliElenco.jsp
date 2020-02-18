<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<div aria-hidden="true" aria-labelledby="labelModaleTotaliElenco" role="dialog" tabindex="-1" class="modal hide fade" id="modaleTotaliElenco">
	<div class="modal-header" id="labelModaleTotaliElenco">
		<button aria-hidden="true" data-dismiss="modal" class="close" type="button">&times;</button>
		<h4 class="nostep-pane">Report totali</h4>
	</div>
	<div class="modal-body">
		<fieldset class="form-horizontal">
			<table class="table tab_left ">
				<thead>
					<tr>
						<th>&nbsp;</th>
						<th class="ReportTable tab_Right">Totale entrata</th>
						<th class="ReportTable tab_Right">Totale spesa</th>
					</tr>
				</thead>
				<tbody>
					<tr class="table-bordered">
						<td class="ReportTable">Da convalidare</td>
						<td class="tab_Right"><s:property value="totaleEntrataDaConvalidareSubdocumenti"/></td>
						<td class="tab_Right"><s:property value="totaleSpesaDaConvalidareSubdocumenti"/></td>
					</tr>
					<tr class="table-bordered">
						<td class="ReportTable">Convalidate</td>
						<td class="tab_Right"><s:property value="totaleEntrataConvalidateSubdocumenti"/></td>
						<td class="tab_Right"><s:property value="totaleSpesaConvalidateSubdocumenti"/></td>
					</tr>
					<tr class="table-bordered">
						<td class="ReportTable">Non convalidabili</td>
						<td class="tab_Right"><s:property value="totaleEntrataACoperturaSubdocumenti"/></td>
						<td class="tab_Right"><s:property value="totaleSpesaACoperturaSubdocumenti"/></td>
					</tr>
				</tbody>
				<tfoot>
					<tr>
						<th>&nbsp;</th>
						<th class="ReportTable tab_Right"><s:property value="totaleEntrataSubdocumenti"/></th>
						<th class="ReportTable tab_Right"><s:property value="totaleSpesaSubdocumenti"/></th>
					</tr>
				</tfoot>
			</table>
		</fieldset>
	</div>
	<div class="modal-footer">
		<button aria-hidden="true" data-dismiss="modal" class="btn btn-primary">chiudi</button>
	</div>
</div>