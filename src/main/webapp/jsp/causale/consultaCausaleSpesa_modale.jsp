<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!--modale consultaCausale -->
<div aria-hidden="true" aria-labelledby="ConsultaCausaleLabel" role="dialog" tabindex="-1" class="modal hide fade" id="msgConsulta" >
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
		<h4 class="nostep-pane">Consulta</h4>
	</div>
	<div class="modal-body">
		<fieldset class="form-horizontal">
			<div id="divStoricoCausali">
				<h4>
					Causale: <span id="MODALE_codiceCausale"></span> -
					Tipo causale: <span id="MODALE_codiceTipoCausale"></span> -
					Stato causale: <span id="MODALE_statoCausale"></span>
				</h4>
				<table class="table table-hover tab_left"  summary="...." id="tabellaStoricoCausali">
					<thead>
						<tr>
							<th scope="col">Data</th>
							<th scope="col">Capitolo </th>
							<th scope="col">Impegno</th>
							<th scope="col">Soggetto</th>
							<th scope="col">Provvedimento</th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
				<div class="Border_line"></div>
			</div>
		</fieldset>
	</div>
	<div class="modal-footer">
		<button class="btn btn-primary" data-dismiss="modal" aria-hidden="true">chiudi</button>
	</div>
</div>