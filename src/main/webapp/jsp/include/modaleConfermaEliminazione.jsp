<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div aria-hidden="true" aria-labelledby="labelModaleEliminazione" role="dialog" tabindex="-1" class="modal hide fade" id="modaleEliminazione">
	<div class="modal-body" id="labelModaleEliminazione">
		<div class="alert alert-error alert-persistent">
			<button data-dismiss="modal" class="close" type="button">&times;</button>
			<p><strong>Attenzione!</strong></p>
			<p><strong>Elemento selezionato: <span id="spanElementoSelezionatoModaleEliminazione"></span></strong></p>
			<p>Stai per eliminare l'elemento selezionato: sei sicuro di voler proseguire?</p>
		</div>
	</div>
	<div class="modal-footer">
		<button type="button" data-dismiss="modal" class="btn">no, indietro</button>
		<button type="button" class="btn btn-primary" id="pulsanteSiModaleEliminazione">
			si, prosegui&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_pulsanteSiModaleEliminazione"></i>
		</button>
	</div>
</div>