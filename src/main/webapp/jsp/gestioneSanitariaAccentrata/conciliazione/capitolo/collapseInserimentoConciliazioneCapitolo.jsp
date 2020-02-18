<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="accordion_info">
	<fieldset class="form-horizontal" id="inserimento_fieldsetConciliazioni">
		<h4 class="titleTxt nostep-pane">Inserisci conciliazione per capitolo</h4>
		<h4 class="step-pane">Capitolo</h4>
		<fieldset id="inserimento_fieldsetRicercaCapitolo">
			<div class="control-group">
				<label class="control-label">Tipo *</label>
				<div class="controls">
					<span class="al">
						<label class="radio inline">
							<input type="radio" value="S" name="tipoCapitolo" data-maintain>Spese
						</label>
					</span>
					<span class="al">
						<label class="radio inline">
							<input type="radio" value="E" name="tipoCapitolo" data-maintain>Entrate
						</label>
					</span>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="inserimento_annoCapitoloCapitolo">Anno *</label>
				<div class="controls">
					<s:textfield id="inserimento_annoCapitoloCapitolo" name="capitolo.annoCapitolo" cssClass="span2 soloNumeri" disabled="true" value="%{annoEsercizio}" data-maintain="" />
					<s:hidden name="capitolo.annoCapitolo" id="inserimento_hidden_annoCapitoloCapitolo" value="%{annoEsercizio}" data-maintain="" />
					<span class="al">
						<label class="radio inline" for="inserimento__numeroCapitoloCapitolo">Numero *</label>
					</span>
					<s:textfield id="inserimento_numeroCapitoloCapitolo" name="capitolo.numeroCapitolo" cssClass="span2 soloNumeri" />
					<span class="al">
						<label class="radio inline" for="inserimento__numeroArticoloCapitolo">Articolo *</label>
					</span>
					<s:textfield id="inserimento_numeroArticoloCapitolo" name="capitolo.numeroArticolo" cssClass="span2 soloNumeri" />
					<s:if test="gestioneUEB">
						<span class="al">
							<label class="radio inline" for="inserimento__numeroUEBCapitolo">UEB *</label>
						</span>
						<s:textfield id="inserimento_numeroUEBCapitolo" name="capitolo.numeroUEB" cssClass="span2 soloNumeri" />
					</s:if><s:else>
						<s:hidden id="inserimento_numeroUEBCapitolo" name="capitolo.numeroUEB" value="1" data-maintain=""/>
					</s:else>
					<button type="button" class="btn btn-primary pull-right" id="inserimento_buttonRicercaCapitolo">cerca</button>
					<s:hidden id="inserimento_uidCapitolo" name="capitolo.uid" />
				</div>
			</div>
		</fieldset>
		<div class="hide" id="inserimento_divConciliazione">
			<h4 class="step-pane">Conciliato in Contabilit&agrave; generale con:</h4>
			<div data-tipo="S" class="hide">
				<div class="control-group">
					<label class="control-label" for="inserimento_classeDiConciliazioneConciliazionePerCapitolo1_S">Classe di conciliazione *</label>
					<div class="controls">
						<s:textfield id="inserimento_classeDiConciliazioneConciliazionePerCapitolo1_S" name="conciliazionePerCapitolo1.classeDiConciliazione" value="%{classeDiConciliazione1Spesa.descrizione}" disabled="true" data-maintain="" />
						<s:hidden id="inserimento_hidden_classeDiConciliazioneConciliazionePerCapitolo1_S" name="conciliazionePerCapitolo1.classeDiConciliazione" value="%{classeDiConciliazione1Spesa}" disabled="true" data-maintain="" />
					</div>
				</div>
				<div class="control-group">
					<label class="control-label" for="inserimento_contoConciliazionePerCapitolo1_S">Conto *</label>
					<div class="controls">
						<select name="conciliazionePerCapitolo1.conto.uid" id="inserimento_contoConciliazionePerCapitolo1_S" disabled></select>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label" for="inserimento_classeDiConciliazioneConciliazionePerCapitolo2_S">Classe di conciliazione *</label>
					<div class="controls">
						<s:textfield id="inserimento_classeDiConciliazioneConciliazionePerCapitolo2_S" name="conciliazionePerCapitolo2.classeDiConciliazione" value="%{classeDiConciliazione2Spesa.descrizione}" disabled="true" data-maintain="" />
						<s:hidden id="inserimento_hidden_classeDiConciliazioneConciliazionePerCapitolo2_S" name="conciliazionePerCapitolo2.classeDiConciliazione" value="%{classeDiConciliazione2Spesa}" disabled="true" data-maintain="" />
					</div>
				</div>
				<div class="control-group">
					<label class="control-label" for="inserimento_contoConciliazionePerCapitolo2_S">Conto *</label>
					<div class="controls">
						<select name="conciliazionePerCapitolo2.conto.uid" id="inserimento_contoConciliazionePerCapitolo2_S" disabled></select>
					</div>
				</div>
			</div>
			<div data-tipo="E" class="hide">
				<div class="control-group">
					<label class="control-label" for="inserimento_classeDiConciliazioneConciliazionePerCapitolo1_E">Classe di conciliazione *</label>
					<div class="controls">
						<s:textfield id="inserimento_classeDiConciliazioneConciliazionePerCapitolo1_E" name="conciliazionePerCapitolo1.classeDiConciliazione" value="%{classeDiConciliazione1Entrata.descrizione}" disabled="true" data-maintain="" />
						<s:hidden id="inserimento_hidden_classeDiConciliazioneConciliazionePerCapitolo1_E" name="conciliazionePerCapitolo1.classeDiConciliazione" value="%{classeDiConciliazione1Entrata}" disabled="true" data-maintain="" />
					</div>
				</div>
				<div class="control-group">
					<label class="control-label" for="inserimento_contoConciliazionePerCapitolo1_E">Conto *</label>
					<div class="controls">
						<select name="conciliazionePerCapitolo1.conto.uid" id="inserimento_contoConciliazionePerCapitolo1_E" disabled></select>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label" for="inserimento_classeDiConciliazioneConciliazionePerCapitolo2_E">Classe di conciliazione *</label>
					<div class="controls">
						<s:textfield id="inserimento_classeDiConciliazioneConciliazionePerCapitolo2_E" name="conciliazionePerCapitolo2.classeDiConciliazione" value="%{classeDiConciliazione2Entrata.descrizione}" disabled="true" data-maintain="" />
						<s:hidden id="inserimento_hidden_classeDiConciliazioneConciliazionePerCapitolo2_E" name="conciliazionePerCapitolo2.classeDiConciliazione" value="%{classeDiConciliazione2Entrata}" disabled="true" data-maintain="" />
					</div>
				</div>
				<div class="control-group">
					<label class="control-label" for="inserimento_contoConciliazionePerCapitolo2_E">Conto *</label>
					<div class="controls">
						<select name="conciliazionePerCapitolo2.conto.uid" id="inserimento_contoConciliazionePerCapitolo2_E" disabled></select>
					</div>
				</div>
			</div>
		</div>
		<p>
			<button type="button" class="btn btn-secondary" id="inserimento_buttonAnnulla">annulla</button>
			<span class="pull-right">
				<button type="button" class="btn btn-primary" id="inserimento_buttonSalva">salva</button>
			</span>
		</p>
	</fieldset>
</div>