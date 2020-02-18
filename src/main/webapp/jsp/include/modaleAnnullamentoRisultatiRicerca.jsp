<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div aria-hidden="true" aria-labelledby="labelModaleAnnullamentoRisultatiRicerca" role="dialog" tabindex="-1"
		class="modal hide fade" id="modaleAnnullamentoRisultatiRicerca">
	<div class="modal-body">
		<div class="alert alert-error alert-persistent" id="labelModaleAnnullamentoRisultatiRicerca">
			<p><strong>Attenzione!</strong></p>
			<p><strong>Elemento selezionato: <span id="spanModaleAnnullamentoRisultatiRicerca"></span></strong></p>
			<p>Stai per annullare l'elemento selezionato, questo cambier&agrave; lo stato dell'elemento: sei sicuro di voler proseguire?</p>
		</div>
	</div>

	<div class="modal-footer">
		<button aria-hidden="true" data-dismiss="modal" class="btn">no, indietro</button>
		<button aria-hidden="true" data-href="${param.href}" class="btn btn-primary" id="buttonConfermaModaleAnnullamentoRisultatiRicerca">
			s&igrave;, prosegui
		</button>
	</div>
</div>