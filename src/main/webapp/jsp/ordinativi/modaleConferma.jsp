<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div aria-hidden="true" aria-labelledby="confermaEmissioneLabel" role="dialog" tabindex="-1" class="modal hide fade" id="confermaEmissioneModal">
	<div class="modal-header" id="confermaEmissioneLabel">
		<button aria-hidden="true" data-dismiss="modal" class="close" type="button">&times;</button>
		<h4 class="nostep-pane">Conferma emissione</h4>
	</div>

	<div class="modal-body">
		<fieldset class="form-horizontal">
			<p>Stai per emettere <span id="confermaEmissioneNumeroSpan"></span> per un importo di <span id="confermaEmissioneImportoSpan"></span> euro. Sei sicuro di voler proseguire?</p>
		</fieldset>
	</div>
	<div class="modal-footer">
		<button type="button" class="btn btn-secondary" aria-hidden="true" data-dismiss="modal">annulla</button>
		<button type="button" class="btn btn-primary" id="confermaEmissioneModalConferma">conferma</button>
	</div>
</div>