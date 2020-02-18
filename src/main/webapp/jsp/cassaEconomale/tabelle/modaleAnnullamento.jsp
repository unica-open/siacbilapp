<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div aria-hidden="true" aria-labelledby="labelAnnullamento" role="dialog" tabindex="-1" class="modal hide fade" id="modaleAnnullamentoElemento">
	<div class="modal-body" id="labelAnnullamento">
		<div class="alert alert-error alert-persistent">
			<p><strong>Attenzione!</strong></p>
			<p><strong>Elemento selezionato: <span id="elementoSelezionatoModaleAnnullamentoElemento"></span></strong></p>
			<p>Stai per annullare l'elemento selezionato. Sei sicuro di voler proseguire?</p>
		</div>
	</div>
	<div class="modal-footer">
		<button type="button" class="btn" aria-hidden="true" data-dismiss="modal">no, indietro</button>
		<button type="button" class="btn btn-primary" id="confermaModaleAnnullamentoElemento" data-uid="">
			s&iacute;, prosegui&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_confermaModaleAnnullamentoElemento"></i>
		</button>
	</div>
</div>