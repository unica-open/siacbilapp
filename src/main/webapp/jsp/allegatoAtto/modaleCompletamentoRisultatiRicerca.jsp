<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div aria-hidden="true" aria-labelledby="labelModaleCompletamentoRisultatiRicerca" role="dialog" tabindex="-1"
		class="modal hide fade"id="modaleCompletamentoRisultatiRicerca">
	<div class="modal-body">
		<div class="alert alert-warning hide" id="">
			<ul></ul>
		</div>
		<div class="alert alert-error alert-persistent" id="labelModaleCompletamentoRisultatiRicerca">
			<p><strong>Attenzione!</strong></p>
			<p><strong>Elemento selezionato: <span id="spanModaleCompletamentoRisultatiRicerca"></span></strong></p>
			<p>Stai per completare l'elemento selezionato.Sei sicuro di voler proseguire?</p>
		</div>
	</div>
	<div class="modal-footer">
		<button aria-hidden="true" data-dismiss="modal" class="btn">no, indietro</button>
		<button type="button" aria-hidden="true" data-href="risultatiRicercaAllegatoAtto_completa.do" class="btn btn-primary" id="buttonConfermaModaleCompletamentoRisultatiRicerca">
			s&igrave;, prosegui
		</button>
	</div>
</div>