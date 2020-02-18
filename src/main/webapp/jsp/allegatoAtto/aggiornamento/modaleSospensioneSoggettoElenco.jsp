<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div aria-hidden="true" aria-labelledby="labelModaleSospensioneSoggettoElenco" role="dialog" tabindex="-1" class="modal hide fade" id="modaleSospensioneSoggettoElenco">
	<div class="modal-header" id="labelModaleSospensioneSoggettoElenco">
		<button aria-hidden="true" data-dismiss="modal" class="close" type="button">&times;</button>
		<h4 class="nostep-pane">Dati sospensione soggetto<span id="datiSoggettoModaleSospensioneSoggettoElenco"></span></h4>
	</div>
	<div class="modal-body">
		<div class="alert alert-error hide" id="ERRORI_modaleSospensioneSoggettoElenco">
			<button type="button" class="close" data-hide="alert">&times;</button>
			<strong>Attenzione!!</strong><br>
			<ul></ul>
		</div>
		<div class="alert alert-info hide" id="modaleSospensioneSoggettoElenco_datiNonUnivoci">
			<button type="button" class="close" data-hide="alert">&times;</button>
			<strong>Attenzione!!</strong><br />
			I dati del soggetto non sono univocamente determinati. Non saranno pertanto visualizzati
		</div>
		<fieldset class="form-horizontal" id="fieldsetModaleSospensioneSoggettoElenco">
			<input type="hidden" name="datiSoggettoAllegato.uid" id="uidDatiSoggettoAllegato" />
			<input type="hidden" name="datiSoggettoAllegato.soggetto.uid" id="uidSoggettoDatiSoggettoAllegato" />
			<div class="control-group">
				<label class="control-label" for="dataSospensioneDatiSoggettoAllegato">Data *</label>
				<div class="controls">
					<input type="text" class="span2 datepicker" name="datiSoggettoAllegato.dataSospensione" id="dataSospensioneDatiSoggettoAllegato" maxlength="10"
						required />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="causaleSospensioneDatiSoggettoAllegato">Causale *</label>
				<div class="controls">
					<input type="text" value="" class="span11" name="datiSoggettoAllegato.causaleSospensione" id="causaleSospensioneDatiSoggettoAllegato" required />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="dataRiattivazioneDatiSoggettoAllegato">Data riattivazione</label>
				<div class="controls">
					<input type="text" class="span2 datepicker" name="datiSoggettoAllegato.dataRiattivazione" id="dataRiattivazioneDatiSoggettoAllegato" maxlength="10" />
				</div>
			</div>
		</fieldset>
	</div>
	<div class="modal-footer">
		<button type="button" data-dismiss="modal" class="btn">annulla</button>
		<button type="button" class="btn btn-primary" id="pulsanteConfermaModaleSospensioneSoggettoElenco">
			conferma&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_pulsanteConfermaModaleSospensioneSoggettoElenco"></i>
		</button>
	</div>
</div>