<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<div aria-hidden="true" aria-labelledby="labelModaleRifiutaAllegato" role="dialog" tabindex="-1" class="modal hide fade" id="modaleRifiutaAllegato">
	<div class="modal-body" id="labelModaleRifiutaAllegato">
		<div class="alert alert-error alert-persistent">
			<button data-dismiss="modal" class="close" type="button">&times;</button>
			<p><strong>Attenzione!</strong></p>
			<p>L'operazione di rifiuto verr&agrave; effettuata sull'intero Atto (elenchi e documenti) rendendolo inutilizzabile, proseguire con l'operazione?</p>
		</div>
	</div>
	<div class="modal-footer">
		<button type="button" data-dismiss="modal" class="btn">no, indietro</button>
		<button type="button" class="btn btn-primary" id="pulsanteSiModaleRifiutaAllegato">si, prosegui</button>
	</div>
</div>