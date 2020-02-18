<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<h4 class="step-pane">Ritenute</h4>
<fieldset id="formRitenute" class="form-horizontal">
	<fieldset id="fieldsetTesta">
		<div class="control-group">
			<label for="importoEsenteRitenuteDocumentoDocumento" class="control-label">Importo esente</label>
			<div class="controls">
				<s:textfield id="importoEsenteRitenuteDocumentoDocumento" name="documento.ritenuteDocumento.importoEsente" cssClass="span2 soloNumeri decimale" readonly="true"/>
			</div>
		</div>
		
		<div class="control-group">
			<label for="importoCassaPensioniRitenuteDocumentoDocumento" class="control-label">Cassa pensioni</label>
			<div class="controls">
				<s:textfield id="importoCassaPensioniRitenuteDocumentoDocumento" name="documento.ritenuteDocumento.importoCassaPensioni" cssClass="span2 soloNumeri decimale"/>
				<span class="alRight">
					<label for="importoRivalsaRitenuteDocumentoDocumento" class="radio inline">Rivalsa</label>
				</span>
				<s:textfield id="importoRivalsaRitenuteDocumentoDocumento" name="documento.ritenuteDocumento.importoRivalsa" cssClass="span2 soloNumeri decimale"/>
				<span class="alRight">
					<label for="importoIVARitenuteDocumentoDocumento" class="radio inline">Importo IVA</label>
				</span>
				<s:textfield id="importoIVARitenuteDocumentoDocumento" name="documento.ritenuteDocumento.importoIVA" cssClass="span2 soloNumeri decimale" />
			</div>
		</div>
	</fieldset>
	
	<%-- <div class="control-group">
		<label class="control-label">
			<h4>Totale</h4>
		</label>
		<div class="controls">
			<label class="radio inline">
				<h4 id="totaleRitenute">
					<s:property value="totaleRitenuteDocumento" />
				</h4>
			</label>
		</div>
	</div> --%>
	
	<s:if test="%{fatturaFELPresente}">
		<div id="datiFattureDiv" class="step-pane active">
			<div class="accordion">
				<div class="accordion-group">
					<div class="accordion-heading">
						<a href="#" class="accordion-toggle collapsed" id="datiFatturePulsante">
							Visualizza dettaglio FEL<span class="icon">&nbsp;</span>
						</a>
					</div>
					<div class="accordion-body collapse" id="datiFattureAccordion">
						<div class="accordion-inner">
							<fieldset class="form-horizontal">
								<table summary="..." class="table table-hover tab_left table-bordered" id="tabellaDatiFatture">
									<thead>
										<tr>
											<th>Numero</th>
											<th><abbr title="Codice esente">Cod. Es</abbr></th>
											<th class="tab_Right">Aliquota &#37;</th>
											<th class="tab_Right">Imponibile</th>
											<th class="tab_Right">Imposta</th>
											<th class="tab_Right">Esente</th>
										</tr>
									</thead>
									<tbody>
									</tbody>
									<tfoot>
										<tr>
											<th class="tab_Center" colspan="3">Totale</th>
											<th data-totale-imponibile class="tab_Right"></th>
											<th data-totale-imposta class="tab_Right"></th>
											<th data-totale-esente class="tab_Right"></th>
										</tr>
									</tfoot>
								</table>
							</fieldset>
						</div>
					</div>
				</div>
			</div>
		</div>
	</s:if>
	
	<h4 class="step-pane">Elenco oneri</h4>
	<table summary="...." class="table table-hover tab_left" id="tabellaRitenute">
		<thead>
			<tr>
				<th scope="col">Natura</th>
				<th scope="col">Tipologia</th>
				<th scope="col">Ordinativo</th>
				<th scope="col" class="tab_Right">Imponibile</th>
				<th scope="col" class="tab_Right">Aliquota a carico soggetto</th>
				<th scope="col" class="tab_Right">Importo a carico soggetto</th>
				<th scope="col" class="tab_Right">Aliquota a carico Ente</th>
				<th scope="col" class="tab_Right">Importo a carico Ente</th>
				<th scope="col" class="tab_Right">Somma non soggetta</th>
				<%-- <s:if test="flagEditabilitaRitenute"> --%>
					<th scope="col" class="tab_Right"></th>
					<th scope="col"></th>
				<%-- </s:if> --%>
			</tr>
		</thead>
		<tbody>
		</tbody>
		<tfoot>
		</tfoot>
	</table>

	<div id ="divInserimentoOnere">
		<s:hidden id="hidden_codiceEsenzione" value="%{codiceEsenzione}"/>
		<s:hidden id="hidden_codiceSplitReverse" value="%{codiceSplitReverse}"/>
		<p>
			<a class="btn btn-secondary" id="pulsanteInserisciNuovoOnere">inserisci nuovo onere</a>
		</p>
		
		<div class="collapse" id="collapseInserisciNuovoOnere">
			<div class="accordion_info">
				<h4 class="step-pane">Dati principali</h4>
				<fieldset class="form-horizontal" id="fieldsetDatiOnere">
					<div class="control-group">
						<label for="naturaOnereTipoOnereDettaglioOnere" class="control-label">Natura Onere *</label>
						<div class="controls">
							<select name="dettaglioOnere.tipoOnere.naturaOnere.uid" required id="naturaOnereTipoOnereDettaglioOnere" class="span4">
								<option value="0"></option>
								<s:iterator value="listaNaturaOnere" var="no">
									<option value="<s:property value="#no.uid"/>" data-codice="<s:property value="#no.codice"/>">
										<s:property value="#no.codice"/> - <s:property value="#no.descrizione" />
									</option>
								</s:iterator>
							</select>
						</div>
					</div>
					<div class="control-group">
						<label class="control-label" for="tipoOnereDettaglioOnere">Codice Tributo *</label>
						<div class="controls">
							<select name="dettaglioOnere.tipoOnere.uid" class="span9" required disabled="disabled" id="tipoOnereDettaglioOnere"></select>
						</div>
					</div>
					<div class="control-group hide" id="tipoIvaSplitReverseDiv">
						<label class="control-label" for="tipoIvaSplitReverse">Tipo Split / Reverse</label>
						<div class="controls">
							<input type="text" disabled id="tipoIvaSplitReverse" class="span4" />
						</div>
					</div>
					
					<div class="control-group">
						<label class="control-label" for="importoImponibileDettaglioOnere">Imponibile *</label>
						<div class="controls">
							<s:textfield id="importoImponibileDettaglioOnere" name="dettaglioOnere.importoImponibile" cssClass="lbTextSmall span2 soloNumeri decimale" placeholder="imponibile" required="true" />
							<span class="alRight" data-hidden-natura="<s:property value="codiceEsenzione"/>" data-hidden-split-reverse="<s:property value="codiceReverseChange"/>">
								<label for="aliquotaCaricoSoggettoTipoOnereDettaglioOnere" class="radio inline">Aliquota a carico soggetto</label>
							</span>
							<s:textfield id="aliquotaCaricoSoggettoTipoOnereDettaglioOnere" name="dettaglioOnere.tipoOnere.aliquotaCaricoSoggetto" cssClass="lbTextSmall span2 soloNumeri decimale"
								placeholder="aliquota" disabled="true" data-hidden-natura="%{codiceEsenzione}" data-hidden-split-reverse="%{codiceReverseChange}"
								data-enabled-split-reverse="SI SC" />
							<span class="alRight" data-hidden-natura="<s:property value="codiceEsenzione"/> <s:property value="codiceSplitReverse"/>">
								<label for="aliquotaCaricoEnteTipoOnereDettaglioOnere" class="radio inline">Aliquota a carico Ente</label>
							</span>
							<s:textfield id="aliquotaCaricoEnteTipoOnereDettaglioOnere" name="dettaglioOnere.tipoOnere.aliquotaCaricoEnte" cssClass="lbTextSmall span2 soloNumeri decimale"
								placeholder="aliquota" disabled="true" data-hidden-natura="%{codiceEsenzione + ' ' + codiceSplitReverse}" />
						</div>
					</div>
		
					<div class="control-group">
						<label class="control-label" for="importoCaricoSoggettoDettaglioOnere">Importo a carico soggetto </label>
						<div class="controls">
							<s:textfield id="importoCaricoSoggettoDettaglioOnere" name="dettaglioOnere.importoCaricoSoggetto" cssClass="lbTextSmall span2 soloNumeri decimale" placeholder="importo" />
							<span class="alRight">
								<label for="importoCaricoEnteDettaglioOnere" class="radio inline" data-hidden-natura="<s:property value="codiceEsenzione"/> <s:property value="codiceSplitReverse"/>">Importo a carico Ente</label>
							</span>
							<s:textfield id="importoCaricoEnteDettaglioOnere" name="dettaglioOnere.importoCaricoEnte" cssClass="lbTextSmall span2 soloNumeri decimale" placeholder="importo" data-hidden-natura="%{codiceSplitReverse + ' ' + codiceEsenzione}" />
						</div>
					</div>
		
					<div class="control-group" data-hidden-natura="<s:property value="codiceEsenzione"/> <s:property value="codiceSplitReverse"/>">
						<label for="attivitaOnereDettaglioOnere" class="control-label">Attivit&agrave;</label>
						<div class="controls">
							<select name="attivitaOnere.uid" class="span5" disabled="disabled" id="attivitaOnereDettaglioOnere"></select>
							<span class="alRight">
								<label for="attivitaInizioDettaglioOnere" class="radio inline">Dal</label>
							</span>
							<s:textfield id="attivitaInizioDettaglioOnere" name="dettaglioOnere.attivitaInizio" cssClass="lbTextSmall span2 datepicker" maxlength="10" placeholder="dal" />
							<span class="alRight">
								<label for="attivitaFineDettaglioOnere" class="radio inline">Al</label>
							</span>
							<s:textfield id="attivitaFineDettaglioOnere" name="dettaglioOnere.attivitaFine" cssClass="lbTextSmall span2 datepicker" maxlength="10" placeholder="al" />
						</div>
					</div>
		
					<div class="control-group" data-hidden-natura="<s:property value="codiceEsenzione"/> <s:property value="codiceSplitReverse"/>">
						<label for="quadro770TipoOnereDettaglioOnere" class="control-label">Quadro 770 </label>
						<div class="controls">
							<s:textfield id="quadro770TipoOnereDettaglioOnere" name="dettaglioOnere.tipoOnere.quadro770" cssClass="lbTextSmall span1" placeholder="quadro" disabled="true" />
							<span class="alRight">
								<label for="causale770DettaglioOnere" class="radio inline">Causale 770</label>
							</span>
							<select id="causale770DettaglioOnere" name="causale770.uid" class="span5" disabled="disabled"></select>
						</div>
					</div>
					<div class="control-group" data-hidden-natura="<s:property value="codiceEsenzione"/> <s:property value="codiceSplitReverse"/>">
					<label for="importoSommaNonSoggetta" class="control-label">Somma non soggetta</label>
					<div class="controls">
						<s:textfield id="importoSommaNonSoggetta" name="dettaglioOnere.sommaNonSoggetta" cssClass="lbTextSmall span2 soloNumeri decimale" placeholder="somma non soggetta"/>
					</div>
					</div>
					<div class="control-group" data-hidden-natura="<s:property value="codiceEsenzione"/> <s:property value="codiceSplitReverse"/>">
						<label for="tipoSommaNonSoggetta" class="control-label">Codice somma non soggetta</label>
						<div class="controls">
							<select id="tipoSommaNonSoggetta" name="tipoSommaNonSoggetta.uid" class="span9" disabled="disabled"></select>
						</div>
					</div>
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
					
					<p>
						<a class="btn btn-primary" id="pulsanteInserisciOnere">
							inserisci&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_pulsanteInserisciOnere"></i>
						</a>
					</p>
				</fieldset>
			</div>
		</div>
	</div>
	
	<div class="Border_line"></div>
	<p class="margin-medium">
		<s:include value="/jsp/include/indietro.jsp" />
		<%-- <s:if test="flagEditabilitaRitenute"> --%>
		<span id="pulsanteRitenute">
			<a id="pulsanteResetRitenute" class="btn" href="#">annulla</a>
			
			<a id="pulsanteSalvaRitenute" class="btn btn-primary pull-right" href="#">
				salva&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_pulsanteSalvaRitenute"></i>
			</a>
		</span>
		<%-- </s:if> --%>
	</p>
</fieldset>

	<div id="modaleAggiornamentoRitenuteContainer"></div>
	
	<div aria-hidden="true" aria-labelledby="msgEliminaLabel" role="dialog" tabindex="-1" class="modal hide fade" id="modaleEliminaRitenute">
		<div class="modal-body">
			<div class="alert alert-error alert-persistent">
				<button data-dismiss="alert" class="close" type="button">&times;</button>
				<p><strong>Attenzione!</strong></p>
				<p><strong>Elemento selezionato: <span id="SPAN_elementoSelezionatoRitenuteEliminazione"></span></strong></p>
				<p>Stai per eliminare l'elemento selezionato: sei sicuro di voler proseguire?</p>
			</div>
		</div>
		<div class="modal-footer">
			<a href="#" class="btn" id="pulsanteNoEliminaRitenute">no, indietro</a>
			<a href="#" class="btn btn-primary" id="pulsanteSiEliminaRitenute">
				s&iacute;, prosegui&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_pulsanteSiEliminaRitenute"></i>
			</a>
		</div>
	</div>
