<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<div aria-hidden="true" aria-labelledby="labelModaleInvioRisultatiRicerca" role="dialog" tabindex="-1" class="modal hide fade" id="modaleInvioRisultatiRicerca">
	<s:hidden name="descrAllegatoAtto" id="descrAllegatoAtto"/>
	<div class="modal-body">
		<div class="alert alert-error alert-persistent" id="labelModaleInvioRisultatiRicerca">
			<p><strong>Attenzione!</strong></p>
			<p>Si sta per inviare l'ATTO <span id="spanModaleInvioRisultatiRicerca"></span> per la firma. 
			&Egrave; necessario compilare una check list tramite l'apposizione di opportuni flag prima dell'invio.
			 Vuoi proseguire?<p>
		</div>
	</div>
	<div class="modal-footer">
		<button aria-hidden="true" data-dismiss="modal" class="btn">no, indietro</button>
		<button aria-hidden="true" data-href="inviaAllegatoAttoChecklist.do" class="btn btn-primary" id="buttonConfermaModaleInvioRisultatiRicerca">
			s&igrave;, prosegui
		</button>
	</div>
</div>