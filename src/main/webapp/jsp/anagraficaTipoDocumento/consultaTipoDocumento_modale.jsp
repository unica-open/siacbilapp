<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<!--modale consultaCausale -->
<div aria-hidden="true" aria-labelledby="ConsultaTipoDocumentoLabel" role="dialog" tabindex="-1" class="modal hide fade" id="msgConsulta" >
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
		<h4 class="nostep-pane">Consulta</h4>
	</div>
	<div class="modal-body">
		<fieldset class="form-horizontal">
			<div id="divStoricoTipoDoc">
				<h4>
					Codice FEL: <span id="MODALE_codiceTipoDocumento"></span> -
					Descrizione: <span id="MODALE_descrizioneTipoDocumento"></span> 
				 
				</h4>
				 
				<div class="Border_line"></div>
			</div>
		</fieldset>
	</div>
	<div class="modal-footer">
		<button class="btn btn-primary" data-dismiss="modal" aria-hidden="true">chiudi</button>
	</div>
</div>