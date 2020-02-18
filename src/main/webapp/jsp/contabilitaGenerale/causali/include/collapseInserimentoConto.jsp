<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<div id="collapseInserimentoConto" class="collapse">
	<div class="step-content">
		<div class="step-pane active">
			<h4 class="step-pane">Dati conto associato alla causale</h4>
			<fieldset id="fieldsetCollapseInserimentoConto">
				<div class="control-group">
					<label class="control-label" for="uidClassePianoPianoDeiContiContoContoTipoOperazione">Classe *</label>
					<div class="controls">
						<s:select list="listaClassePiano" name="contoTipoOperazione.conto.pianoDeiConti.classePiano.uid" id="uidClassePianoPianoDeiContiContoContoTipoOperazione" cssClass="span6" required="true"
							headerKey="" headerValue="" listKey="uid" listValue="%{codice + ' - ' + descrizione}" />
					</div>
				</div>
				<div class="control-group">
					<label class="control-label" for="codiceContoContoTipoOperazione">Codice conto *</label>
					<div class="controls">
						<s:textfield id="codiceContoContoTipoOperazione" name="contoTipoOperazione.conto.codice" required="true" cssClass="span6" />
						<span id="descrizioneContoContoTipoOperazione"></span>
						<span class="radio guidata">
							<button type="button" class="btn btn-primary " id="pulsanteCompilazioneGuidataConto">compilazione guidata</button>
						</span>
					</div>
				</div>
				<h4 class="step-pane">Attributi conto</h4>
				<div class="control-group">
					<label class="control-label" for="operazioneSegnoContoContoTipoOperazione">Segno Conto *</label>
					<div class="controls">
						<s:select list="listaOperazioneSegnoConto" name="contoTipoOperazione.operazioneSegnoConto" id="operazioneSegnoContoContoTipoOperazione" cssClass="span6" required="true"
							headerKey="" headerValue="" listValue="descrizione" />
					</div>
				</div>
				<s:if test="tipoCausaleDiRaccordo">
					<div class="control-group">
						<label class="control-label" for="operazioneUtilizzoContoContoTipoOperazione">Utilizzo conto *</label>
						<div class="controls">
							<s:select list="listaOperazioneUtilizzoConto" name="contoTipoOperazione.operazioneUtilizzoConto" id="operazioneUtilizzoContoContoTipoOperazione" cssClass="span6" required="true"
								headerKey="" headerValue="" listValue="descrizione" />
						</div>
					</div>
					<div class="control-group">
						<label class="control-label" for="operazioneUtilizzoImportoContoTipoOperazione">Utilizzo importo *</label>
						<div class="controls">
							<s:select list="listaOperazioneUtilizzoImporto" name="contoTipoOperazione.operazioneUtilizzoImporto" id="operazioneUtilizzoImportoContoTipoOperazione" cssClass="span6" required="true"
								headerKey="" headerValue="" listValue="descrizione"/>
						</div>
					</div>
				</s:if>
				<div class="control-group">
					<label class="control-label" for="operazioneTipoImportoContoTipoOperazione">Tipo importo *</label>
					<div class="controls">
						<s:select list="listaOperazioneTipoImporto" name="contoTipoOperazione.operazioneTipoImporto" id="operazioneTipoImportoContoTipoOperazione" cssClass="span6" required="true"
							headerKey="" headerValue="" listValue="descrizione" />
					</div>
				</div>
			</fieldset>
			<div class="Border_line"></div>
			<p>
				<button type="button" aria-controls="collapseInserimentoConto" aria-expanded="false" data-toggle="collapse" class="btn btn-secondary" data-target="#collapseInserimentoConto">annulla</button>
				<span class="pull-right">
					<button type="button" class="btn btn-primary" id="pulsanteSalvaInserimentoConto">salva inserimento</button>
				</span>
			</p>
		</div>
	</div>
</div>