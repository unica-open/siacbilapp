<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<div aria-hidden="true" aria-labelledby="labelModaleRestituzioneTotale" role="dialog" tabindex="-1" class="modal hide fade" id="modaleRestituzioneTotale">
	<div class="modal-header" id="labelModaleRestituzioneTotale">
		<button aria-hidden="true" data-dismiss="modal" class="close" type="button">&times;</button>
		<h4 class="nostep-pane">Restituzione totale</h4>
	</div>
	<div class="modal-body">
		<fieldset class="form-horizontal">
			<div class="control-group">
				<label class="control-label" for="importoAnticipato">Importo anticipo</label>
				<div class="controls">
					<s:textfield id="importoAnticipato" name="importoRichiestaAnticipato" cssClass="lbTextSmall span3" readonly="true" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="importoDaRestituire">Importo da restituire</label>
				<div class="controls">
					<s:textfield id="importoDaRestituire" name="importoRichiestaAnticipato" cssClass="lbTextSmall span3" readonly="true" />
				</div>
			</div>
		</fieldset>
	</div>
	<div class="modal-footer">
		<button type="button" class="btn btn-secondary" aria-hidden="true" data-dismiss="modal">annulla</button>
		<button type="button" class="btn btn-primary" id="pulsanteConfermaModaleRestituzioneTotale">conferma</button>
	</div>
</div>