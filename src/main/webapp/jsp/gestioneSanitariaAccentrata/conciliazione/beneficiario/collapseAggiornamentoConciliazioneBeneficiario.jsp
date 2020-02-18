<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="accordion_info">
	<fieldset class="form-horizontal" id="aggiornamento_fieldsetConciliazioni">
		<h4 class="titleTxt nostep-pane">Aggiorna conciliazione per beneficiario</h4>
		<s:hidden id="aggiornamento_hidden_uidConciliazionePerBeneficiario" name="conciliazionePerBeneficiario1.uid" />
		<div class="control-group">
			<label class="control-label" for="aggiornamento_codiceSoggettoSoggetto">Soggetto *</label>
			<div class="controls">
				<s:textfield id="aggiornamento_codiceSoggettoSoggetto" name="soggetto.codiceSoggetto" cssClass="span6" disabled="true" />
				&nbsp;<span id="aggiornamento_datiSoggetto"></span>
				<s:hidden id="aggiornamento_hidden_uidSoggetto" name="soggetto.uid" />
			</div>
		</div>
		<fieldset id="aggiornamento_fieldsetRicercaCapitolo">
			<div class="control-group">
				<label class="control-label">Tipo *</label>
				<div class="controls">
					<span class="al">
						<label class="radio inline">
							<input type="radio" value="S" name="tipoCapitolo" data-maintain disabled>Spese
						</label>
					</span>
					<span class="al">
						<label class="radio inline">
							<input type="radio" value="E" name="tipoCapitolo" data-maintain disabled>Entrate
						</label>
					</span>
					<s:hidden id="aggiornamento_hidden_tipoCapitolo" name="tipoCapitolo" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="aggiornamento_annoCapitoloCapitolo">Anno *</label>
				<div class="controls">
					<s:textfield id="aggiornamento_annoCapitoloCapitolo" name="capitolo.annoCapitolo" cssClass="span2 soloNumeri" disabled="true" value="%{annoEsercizio}" data-maintain="" />
					<s:hidden name="capitolo.annoCapitolo" id="aggiornamento_hidden_annoCapitoloCapitolo" value="%{annoEsercizio}" data-maintain="" />
					<span class="al">
						<label class="radio inline" for="aggiornamento__numeroCapitoloCapitolo">Numero *</label>
					</span>
					<s:textfield id="aggiornamento_numeroCapitoloCapitolo" name="capitolo.numeroCapitolo" cssClass="span2 soloNumeri" disabled="true" />
					<s:hidden id="aggiornamento_hidden_numeroCapitoloCapitolo" name="capitolo.numeroCapitolo" />
					<span class="al">
						<label class="radio inline" for="aggiornamento__numeroArticoloCapitolo">Articolo *</label>
					</span>
					<s:textfield id="aggiornamento_numeroArticoloCapitolo" name="capitolo.numeroArticolo" cssClass="span2 soloNumeri" disabled="true" />
					<s:hidden id="aggiornamento_hidden_numeroArticoloCapitolo" name="capitolo.numeroArticolo" />
					<s:if test="gestioneUEB">
						<span class="al">
							<label class="radio inline" for="aggiornamento__numeroUEBCapitolo">UEB *</label>
						</span>
						<s:textfield id="aggiornamento_numeroUEBCapitolo" name="capitolo.numeroUEB" cssClass="span2 soloNumeri" disabled="true" />
						<s:hidden id="aggiornamento_hidden_numeroUEBCapitolo" name="capitolo.numeroUEB" />
					</s:if><s:else>
						<s:hidden id="aggiornamento_numeroUEBCapitolo" name="capitolo.numeroUEB" value="1" />
					</s:else>
					<s:hidden id="aggiornamento_hidden_uidCapitolo" name="capitolo.uid" />
				</div>
			</div>
		</fieldset>
		<div id="aggiornamento_divConciliazione">
			<h4 class="step-pane">Conciliato in Contabilit&agrave; generale con:</h4>
			<div class="control-group">
				<label class="control-label" for="aggiornamento_classeDiConciliazioneConciliazionePerBeneficiario1">Classe di conciliazione *</label>
				<div class="controls">
					<s:textfield id="aggiornamento_classeDiConciliazioneConciliazionePerBeneficiario1" name="conciliazionePerBeneficiario1.classeDiConciliazione" disabled="true" />
					<s:hidden id="aggiornamento_hidden_classeDiConciliazioneConciliazionePerBeneficiario1" name="conciliazionePerBeneficiario1.classeDiConciliazione" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="aggiornamento_contoConciliazionePerBeneficiario1">Conto *</label>
				<div class="controls">
					<select name="conciliazionePerBeneficiario1.conto.uid" id="aggiornamento_contoConciliazionePerBeneficiario1"></select>
				</div>
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