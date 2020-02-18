<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<div aria-hidden="true" aria-labelledby="modaleImportiCassaLabel" role="dialog" tabindex="-1" class="modal hide fade" id="modaleImportiCassa">
	<div class="modal-header">
		<button aria-hidden="true" data-dismiss="modal" class="close" type="button">&times;</button>
		<h4 class="nostep-pane" id="modaleImportiCassaLabel">Importi</h4>
	</div>
	<div class="modal-body">
		<table class="table tab_left">
			<thead>
				<tr>
					<th class="span4">&nbsp;</th>
					<th class="tab_Right"><span class="alRight">c/c</span></th>
					<th class="tab_Right"><span class="alRight">contanti</span></th>
					<th class="tab_Right"><span class="alRight">Totale</span></th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<th>Importo</th>
					<td class="tab_Right"><s:property value="cassaEconomale.disponibilitaCassaContoCorrente"/></td>
					<td class="tab_Right"><s:property value="cassaEconomale.disponibilitaCassaContanti"/></td>
					<td class="tab_Right"><s:property value="cassaEconomale.disponibilitaCassaTotale"/></td>
				</tr>
			</tbody>
		</table>
	</div>
	<div class="modal-footer">
		<button type="button" aria-hidden="true" data-dismiss="modal" class="btn btn-primary">chiudi</button>
	</div>
</div>