<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<div aria-hidden="true" aria-labelledby="modaleAggiornamentoContoLabel" role="dialog" tabindex="-1" class="modal hide fade" id="modaleAggiornamentoConto">
	<div class="modal-header">
		<button aria-hidden="true" data-dismiss="modal" class="close" type="button">&times;</button>
		<h3 id="modaleAggiornamentoContoLabel">Aggiorna conto associato alla causale</h3>
	</div>
	<div class="modal-body">
		<div class="alert alert-error hide" id="ERRORI_modale">
			<button type="button" class="close" data-hide="alert">&times;</button>
			<strong>Attenzione!!</strong><br/>
			<ul>
			</ul>
		</div>
		<fieldset id="fieldsetModaleAggiornamentoConto" class="form-horizontal">
			<s:hidden id="indiceContoModale" name="modale.indiceConto" />
			<s:hidden id="uidClassePianoPianoDeiContiContoContoTipoOperazioneModale" name="modale.contoTipoOperazione.conto.pianoDeiConti.classePiano.uid" />
			<s:hidden id="codiceContoContoTipoOperazioneModale" name="modale.contoTipoOperazione.conto.codice" />
			<div class="control-group">
				<label class="control-label" for="operazioneSegnoContoContoTipoOperazioneModale">Segno Conto *</label>
				<div class="controls">
					<s:select list="listaOperazioneSegnoConto" name="modale.contoTipoOperazione.operazioneSegnoConto" id="operazioneSegnoContoContoTipoOperazioneModale" cssClass="span6" required="true"
						headerKey="" headerValue="" listValue="descrizione" />
				</div>
			</div>
			<s:if test="tipoCausaleDiRaccordo">
				<div class="control-group">
					<label class="control-label" for="operazioneUtilizzoContoContoTipoOperazioneModale">Utilizzo conto *</label>
					<div class="controls">
						<s:select list="listaOperazioneUtilizzoConto" name="modale.contoTipoOperazione.operazioneUtilizzoConto" id="operazioneUtilizzoContoContoTipoOperazioneModale" cssClass="span6" required="true"
							headerKey="" headerValue="" listValue="descrizione" />
					</div>
				</div>
				<div class="control-group">
					<label class="control-label" for="operazioneUtilizzoImportoContoTipoOperazioneModale">Utilizzo importo *</label>
					<div class="controls">
						<s:select list="listaOperazioneUtilizzoImporto" name="modale.contoTipoOperazione.operazioneUtilizzoImporto" id="operazioneUtilizzoImportoContoTipoOperazioneModale" cssClass="span6" required="true"
							headerKey="" headerValue="" listValue="descrizione" />
					</div>
				</div>
			</s:if>
			<div class="control-group">
				<label class="control-label" for="operazioneTipoImportoContoTipoOperazioneModale">Tipo importo *</label>
				<div class="controls">
					<s:select list="listaOperazioneTipoImporto" name="modale.contoTipoOperazione.operazioneTipoImporto" id="operazioneTipoImportoContoTipoOperazioneModale" cssClass="span6" required="true"
						headerKey="" headerValue="" listValue="descrizione" />
				</div>
			</div>
		</fieldset>
	</div>
	<div class="modal-footer">
		<button aria-hidden="true" data-dismiss="modal" class="btn btn-secondary">annulla</button>
		<button type="button" class="btn btn-primary" id="pulsanteSalvaAggiornamentoConto">
			conferma&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_pulsanteSalvaAggiornamentoConto"></i>
		</button>
	</div>
</div>