<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<div aria-hidden="true" aria-labelledby="bodyAnnullamentoCassaEconomale" role="dialog" tabindex="-1" class="modal hide fade" id="modaleAnnullamentoCassaEconomale">
	<div class="modal-body" id="bodyAnnullamentoCassaEconomale">
		<div class="alert alert-error">
			<p><strong>Attenzione!</strong></p>
			<p>Stai per annullare la cassa: sei sicuro di voler proseguire?</p>
		</div>
	</div>
	<div class="modal-footer">
		<button type="button" class="btn" aria-hidden="true" data-dismiss="modal">no, indietro</button>
		<button type="button" class="btn btn-primary" id="pulsanteProsecuzioneModaleAnnullamentoCassaEconomale">s&iacute;, prosegui</button>
	</div>
</div>