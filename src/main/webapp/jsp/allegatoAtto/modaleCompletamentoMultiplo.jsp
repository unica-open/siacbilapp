<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div aria-hidden="true" aria-labelledby="labelModaleCompletamentoMultiplo" role="dialog" tabindex="-1"
		class="modal hide fade"id="modaleCompletamentoMultiplo">
	<div class="modal-body">
		<div class="alert alert-error alert-persistent" id="labelModaleCompletamentoRisultatiRicerca">
			<p><strong>Attenzione!</strong></p>
			<p><strong>Elementi selezionati: <span id="spanModaleCompletamentoElementiSelezionati"></span></strong></p>
<!-- 			<p><strong>Importo totale: <span id="spanModaleCompletamentoImportoTotale"></span></strong></p> -->
			<p>Si stanno per completare gli allegati selezionati. Proseguire con l'operazione?</p>
		</div>
	</div>
	<div class="modal-footer">
		<button aria-hidden="true" data-dismiss="modal" class="btn">no, indietro</button>
		<button type="button" aria-hidden="true" data-href="risultatiRicercaAllegatoAttoOperazioniMultiple_completaMultiplo.do" class="btn btn-primary" id="buttonConfermaModaleCompletamento">
			s&igrave;, prosegui
		</button>
	</div>
</div>