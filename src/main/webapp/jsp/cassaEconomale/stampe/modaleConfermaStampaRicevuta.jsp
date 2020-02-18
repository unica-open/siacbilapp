<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<div aria-hidden="true" aria-labelledby="bodyConfermaStampaRicevuta" role="dialog" tabindex="-1" class="modal hide fade" id="modaleConfermaStampaRicevuta">
	<div class="modal-body" id="bodyConfermaStampaRicevuta">
		<div class="alert alert-warning alert-persistent">
			<p><strong>Conferma Stampa Ricevuta</strong></p>
			<p>Si sta per elaborare la stampa Ricevuta, vuoi proseguire?</p>
		</div>
	</div>
	<div class="modal-footer">
		<button type="button" class="btn" aria-hidden="true" data-dismiss="modal">no, indietro</button>
		<button type="button" class="btn btn-primary" id="pulsanteConfermaStampaRicevuta">s&iacute;, prosegui</button>
	</div>
</div>