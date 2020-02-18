<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<div aria-hidden="true" aria-labelledby="aggRitenutaLabel" role="dialog" tabindex="-1" class="modal hide fade" id="modaleAggiornaRitenute">
	<div class="modal-header">
		<button aria-hidden="true" data-dismiss="modal" class="close" type="button">×</button>
		<h4>Aggiorna onere</h4>
	</div>
	<div class="modal-body">
		<div class="alert alert-error hide" id="ERRORI_ModaleAggiornamentoRitenuta">
			<button type="button" class="close" data-hide="alert">&times;</button>
			<strong>Attenzione!!</strong><br>
			<ul>
			</ul>
		</div>
		<fieldset class="form-horizontal" id="fieldsetAggiornamentoRitenutaModale">
			<s:if test="%{codiceSplitReverse.equals(dettaglioOnere.tipoOnere.naturaOnere.codice)}">
				<div class="control-group">
					<label class="control-label">Tipo Split / Reverse</label>
					<div class="controls">
						<s:textfield disabled="true" cssClass="lbTextSmall span9" value="%{dettaglioOnere.tipoOnere.tipoIvaSplitReverse.codice + ' ' + dettaglioOnere.tipoOnere.tipoIvaSplitReverse.descrizione}" />
					</div>
				</div>
			</s:if>
			<div class="control-group">
				<label for="importoImponibileDettaglioOnereModale" class="control-label">
					<s:if test="%{codiceEsenzione.equals(dettaglioOnere.tipoOnere.naturaOnere.codice)}">Importo</s:if><s:else>Imponibile</s:else>
				</label>
				<div class="controls">
					<s:textfield id="importoImponibileDettaglioOnereModale" name="dettaglioOnere.importoImponibile" cssClass="lbTextSmall span2 soloNumeri decimale" placeholder="imponibile" required="true" />
				</div>
			</div>
			<s:if test='%{!"ES".equals(dettaglioOnere.tipoOnere.naturaOnere.codice) && ! "RC".equals(dettaglioOnere.tipoOnere.tipoIvaSplitReverse.codice)}'>
				<div class="control-group">
					<label for="aliquotaCaricoSoggettoTipoOnereDettaglioOnereModale" class="control-label">
						<s:if test='%{"SI".equals(dettaglioOnere.tipoOnere.tipoIvaSplitReverse.codice) || "SC".equals(dettaglioOnere.tipoOnere.tipoIvaSplitReverse.codice)}'>Aliquota</s:if><s:else>Aliquota a carico soggetto</s:else>
					</label>
					<div class="controls">
						<s:textfield id="aliquotaCaricoSoggettoTipoOnereDettaglioOnereModale" name="dettaglioOnere.tipoOnere.aliquotaCaricoSoggetto" cssClass="lbTextSmall span2 soloNumeri decimale"
							placeholder="aliquota" disabled='%{!"SI".equals(dettaglioOnere.tipoOnere.tipoIvaSplitReverse.codice) && !"SC".equals(dettaglioOnere.tipoOnere.tipoIvaSplitReverse.codice)}' />
						<s:if test='%{!"SP".equals(dettaglioOnere.tipoOnere.naturaOnere.codice)'>
							<span class="alRight">
								<label for="aliquotaCaricoEnteTipoOnereDettaglioOnereModale" class="radio inline">Aliquota a carico Ente</label>
							</span>
							<s:textfield id="aliquotaCaricoEnteTipoOnereDettaglioOnereModale" name="dettaglioOnere.tipoOnere.aliquotaCaricoEnte" cssClass="lbTextSmall span2 soloNumeri decimale" placeholder="aliquota" disabled="true" />
						</s:if>
					</div>
				</div>
			</s:if>
			
			<div class="control-group">
				<label for="importoCaricoSoggettoDettaglioOnereModale" class="control-label">
					<s:if test='%{"SP".equals(dettaglioOnere.tipoOnere.naturaOnere.codice)}'>Imposta</s:if><s:else>Importo a carico soggetto</s:else>
				</label>
				<div class="controls">
					<s:textfield id="importoCaricoSoggettoDettaglioOnereModale" name="dettaglioOnere.importoCaricoSoggetto" cssClass="lbTextSmall span2 soloNumeri decimale" placeholder="importo" />
					<s:if test='%{!"ES".equals(dettaglioOnere.tipoOnere.naturaOnere.codice && !"SP".equals(dettaglioOnere.tipoOnere.naturaOnere.codice}'>
						<span class="alRight">
							<label for="importoCaricoEnteDettaglioOnereModale" class="radio inline">Importo a carico Ente</label>
						</span>
						<s:textfield id="importoCaricoEnteDettaglioOnereModale" name="dettaglioOnere.importoCaricoEnte" cssClass="lbTextSmall span2 soloNumeri decimale" placeholder="importo" />
					</s:if>
				</div>
			</div>
			<s:if test='%{!"ES".equals(dettaglioOnere.tipoOnere.naturaOnere.codice) && !"SP".equals(dettaglioOnere.tipoOnere.naturaOnere.codice)}'>
				<div class="control-group">
					<label for="attivitaOnereDettaglioOnereModale" class="control-label">Attivit&agrave;</label>
					<div class="controls">
						<s:select list="listaAttivitaOnere" name="attivitaOnere.uid" cssClass="span5" id="attivitaOnereDettaglioOnereModale"
							listKey="uid" listValue="%{codice + '-' + descrizione}" headerKey="0" headerValue="" />
						<span class="attivitaInizioDettaglioOnereModale">
							<label for="attDal" class="radio inline">Dal</label>
						</span>
						<s:textfield id="attivitaInizioDettaglioOnereModale" name="dettaglioOnere.attivitaInizio" cssClass="lbTextSmall span2 datepicker" maxlength="10" placeholder="dal" />
						<span class="alRight">
							<label for="attivitaFineDettaglioOnereModale" class="radio inline">Al</label>
						</span>
						<s:textfield id="attivitaFineDettaglioOnereModale" name="dettaglioOnere.attivitaFine" cssClass="lbTextSmall span2 datepicker" maxlength="10" placeholder="al" />
					</div>
				</div>
				<div class="control-group">
					<label for="causale770DettaglioOnereModale" class="control-label">Causale 770</label>
					<div class="controls">
						<s:select list="listaCausale770" name="causale770.uid" cssClass="span9" id="causale770DettaglioOnereModale"
							listKey="uid" listValue="%{codice + '-' + descrizione}" headerKey="0" headerValue="" />
					</div>
				</div>
				<div class="control-group">
					<label for="importoSommaNonSoggettaModale" class="control-label">Somma non soggetta</label>
					<div class="controls">
						<s:textfield id="importoSommaNonSoggettaModale" name="dettaglioOnere.sommaNonSoggetta" cssClass="lbTextSmall span2 soloNumeri decimale" placeholder="somma non soggetta"/>
					</div>
				</div>
				<div class="control-group">
					<label for="codiceSommaNonSoggettaDettaglioOnereModale" class="control-label">Codice somma non soggetta</label>
					<div class="controls">
						<s:select list="listaSommeNonSoggette" name="tipoSommaNonSoggetta.uid" cssClass="span9" id="codiceSommaNonSoggettaDettaglioOnereModale"
							listKey="uid" listValue="%{codice + '-' + descrizione}" headerKey="0" headerValue="" />
					</div>
				</div>
			</s:if>
			<div class="control-group">
				<label class="control-label">Ordinativo di incasso</label>
				<div class="controls">
					<span class="alRight">
						<label for="annoReversaleDettaglioOnere" class="radio inline">Anno Reversale</label>
					</span>
					<s:textfield id="annoReversaleDettaglioOnere" name="dettaglioOnere.ordinativi[0].anno" cssClass="lbTextSmall span2" placeholder="anno" disabled="true" />
					<span class="alRight">
						<label for="numeroReversaleDettaglioOnere" class="radio inline">Numero Reversale</label>
					</span>
					<s:textfield id="numeroReversaleDettaglioOnere" name="dettaglioOnere.ordinativi[0].numero" cssClass="lbTextSmall span2" placeholder="numero" disabled="true" />
					<s:hidden name="dettaglioOnere.ordinativi[0].uid" id="HIDDEN_uidOrdinativo" />
				</div>
			</div>
		</fieldset>
	</div>
	<div class="modal-footer">
		<a class="btn btn-primary" id="pulsanteConfermaAggiornamentoOnere" href="#">
			conferma&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_pulsanteConfermaAggiornamentoOnere"></i>
		</a>
	</div>
</div>