<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<div class="accordion_info">
	<fieldset class="form-horizontal" id="aggiornamento_fieldsetConciliazioni">
		<h4 class="titleTxt nostep-pane">Aggiorna conciliazione per capitolo</h4>
		<h4 class="step-pane">Capitolo</h4>
		<s:hidden name="conciliazionePerCapitolo1.uid" id="aggiornamento_hidden_uidConciliazionePerCapitolo" />
		<s:hidden name="capitolo.uid" id="aggiornamento_hidden_uidCapitolo" />
		<div class="control-group">
			<label class="control-label">Tipo *</label>
			<div class="controls">
				<span class="al">
					<label class="radio inline">
						<input type="radio" value="S" name="tipoCapitolo" disabled data-maintain>Spese
					</label>
				</span>
				<span class="al">
					<label class="radio inline">
						<input type="radio" value="E" name="tipoCapitolo" disabled data-maintain>Entrate
					</label>
				</span>
				<s:hidden id="aggiornamento_hidden_tipoCapitolo" name="tipoCapitolo" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="aggiornamento_annoCapitoloCapitolo">Anno *</label>
			<div class="controls">
				<s:textfield id="aggiornamento_annoCapitoloCapitolo" name="capitolo.annoCapitolo" cssClass="span2 soloNumeri" disabled="true" value="%{annoEsercizio}" />
				<s:hidden name="capitolo.annoCapitolo" id="aggiornamento_hidden_annoCapitoloCapitolo" />
				<span class="al">
					<label class="radio inline" for="aggiornamento_numeroCapitoloCapitolo">Numero *</label>
				</span>
				<s:textfield id="aggiornamento_numeroCapitoloCapitolo" name="capitolo.numeroCapitolo" cssClass="span2 soloNumeri" disabled="true" />
				<s:hidden id="aggiornamento_hidden_numeroCapitoloCapitolo" name="capitolo.numeroCapitolo" />
				<span class="al">
					<label class="radio inline" for="aggiornamento_numeroArticoloCapitolo">Articolo *</label>
				</span>
				<s:textfield id="aggiornamento_numeroArticoloCapitolo" name="capitolo.numeroArticolo" cssClass="span2 soloNumeri" disabled="true" />
				<s:hidden id="aggiornamento_hidden_numeroArticoloCapitolo" name="capitolo.numeroArticolo" />
				<s:if test="gestioneUEB">
					<span class="al">
						<label class="radio inline" for="aggiornamento_numeroUEBCapitolo">UEB *</label>
					</span>
					<s:textfield id="aggiornamento_numeroUEBCapitolo" name="capitolo.numeroUEB" cssClass="span2 soloNumeri" disabled="true" />
					<s:hidden id="aggiornamento_hidden_numeroUEBCapitolo" name="capitolo.numeroUEB" />
				</s:if><s:else>
					<s:hidden id="aggiornamento_numeroUEBCapitolo" name="capitolo.numeroUEB" value="1" />
				</s:else>
			</div>
		</div>
		<h4 class="step-pane">Conciliato in Contabilit&agrave; generale con:</h4>
		<div class="control-group">
			<label class="control-label" for="aggiornamento_classeDiConciliazioneConciliazionePerCapitolo1">Classe di conciliazione *</label>
			<div class="controls">
				<s:textfield id="aggiornamento_classeDiConciliazioneConciliazionePerCapitolo1" name="conciliazionePerCapitolo1.classeDiConciliazione" disabled="true"/>
				<s:hidden id="aggiornamento_hidden_classeDiConciliazioneConciliazionePerCapitolo1" name="conciliazionePerCapitolo1.classeDiConciliazione" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="aggiornamento_contoConciliazionePerCapitolo1">Conto *</label>
			<div class="controls">
				<select name="conciliazionePerCapitolo1.conto.uid" id="aggiornamento_contoConciliazionePerCapitolo1"></select>
			</div>
		</div>
		<p>
			<button type="button" class="btn btn-secondary" id="aggiornamento_buttonAnnulla">annulla</button>
			<span class="pull-right">
				<button type="button" class="btn btn-primary" id="aggiornamento_buttonSalva">salva</button>
			</span>
		</p>
	</fieldset>
</div>