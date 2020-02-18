<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div id="modaleConfermaNoProvvedimento" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="labelModaleConfermaNoProvvedimento" aria-hidden="true">
	<div class="modal-body" id="labelModaleConfermaNoProvvedimento">
		<div class="alert alert-warning alert-persistent">
			<button data-dismiss="modal" class="close" type="button">&times;</button>
			<p><strong>Attenzione!</strong></p>
			<p>Non &eacute; stato selezionato il Provvedimento: sei sicuro di voler proseguire?</p>
		</div>
	</div>
	<div class="modal-footer">
		<button type="button" data-dismiss="modal" class="btn">no, indietro</button>
		<button type="button" class="btn btn-primary" id="pulsanteSiModaleConfermaNoProvvedimento">si, prosegui</button>
	</div>
</div>
