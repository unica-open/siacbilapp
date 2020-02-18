<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<div aria-hidden="true" aria-labelledby="labelModaleConfermaValidazioneMassiva" role="dialog" tabindex="-1" class="modal hide fade" id="modaleConfermaValidazioneMassiva">
	<div class="modal-header">
		<button aria-hidden="true" data-dismiss="modal" class="close" type="button">&times;</button>
		<h3 id="labelModaleConfermaValidazioneMassiva">Valida Prima Nota</h3>
	</div>
	<div class="modal-body">
		<div class="alert alert-error alert-persistent">
			<p><strong>Attenzione!</strong></p>
			<p>
				Stai per validare <span id="spanDatiModaleConfermaValidazioneMassiva"></span>
				<br/>
				Sei sicuro di voler proseguire?
			</p>
		</div>
	</div>
	<div class="modal-footer">
		<button aria-hidden="true" data-dismiss="modal" class="btn">no, indietro</button>
		<button type="button" class="btn btn-primary" id="confermaModaleConfermaValidazioneMassiva">
			si, prosegui&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_confermaModaleConfermaValidazioneMassiva"></i>
		</button>
	</div>
</div>