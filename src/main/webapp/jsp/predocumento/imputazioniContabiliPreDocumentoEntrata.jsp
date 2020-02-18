<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<h4 class="step-pane">Capitolo
	<span class="datiRIFCapitolo" id="datiRiferimentoCapitoloSpan">
		<s:if test="%{capitolo != null && capitolo.annoCapitolo != null && capitolo.numeroCapitolo != null && capitolo.numeroArticolo != null && (!gestioneUEB || capitolo.numeroUEB != null)}">
			: <s:property value="capitolo.annoCapitolo" /> / <s:property value="capitolo.numeroCapitolo" /> / <s:property value="capitolo.numeroArticolo" />
			<s:if test="%{gestioneUEB}">
				/ <s:property value="capitolo.numeroUEB" />
			</s:if>
		</s:if>
	</span>
</h4>
<fieldset class="form-horizontal imputazioniContabiliCapitolo">
	<div class="control-group">
		<label for="annoCapitolo" class="control-label">Anno</label>
		<div class="controls">
			<s:textfield id="annoCapitolo" name="capitolo.annoCapitolo" value="%{annoEsercizioInt}" cssClass="lbTextSmall" disabled="true" data-maintain="" />
			<s:hidden id="annoCapitoloHidden" name="capitolo.annoCapitolo" value="%{annoEsercizioInt}" data-maintain="" />
			<span class="al">
				<label for="numeroCapitolo" class="radio inline">Capitolo</label>
			</span>
			<s:textfield id="numeroCapitolo" name="capitolo.numeroCapitolo" cssClass="lbTextSmall soloNumeri span2" maxlength="30" placeholder="%{'capitolo'}" />
			<span class="al">
				<label for="numeroArticolo" class="radio inline">Articolo</label>
			</span>
			<s:textfield id="numeroArticolo" name="capitolo.numeroArticolo" cssClass="lbTextSmall soloNumeri span2" maxlength="7" placeholder="%{'articolo'}" />
			<s:if test="%{gestioneUEB}">
				<span class="al">
					<label for="numeroUEB" class="radio inline">UEB</label>
				</span>
				<s:textfield id="numeroUEB" name="capitolo.numeroUEB" cssClass="lbTextSmall soloNumeri span2" placeholder="%{'UEB'}" />
			</s:if><s:else>
				<s:hidden id="numeroUEB" name="capitolo.numeroUEB" value="1" />
			</s:else>
			<span class="radio guidata">
				<button type="button" class="btn btn-primary" id="pulsanteCompilazioneGuidataCapitolo">compilazione guidata</button>
			</span>
		</div>
	</div>
</fieldset>

<h4 class="step-pane">Accertamento
	<span class="datiRIFImpegno" id="datiRiferimentoImpegnoSpan">
		<s:if test="%{movimentoGestione != null && movimentoGestione.annoMovimento != 0 && movimentoGestione.numero != null}">
			: <s:property value="movimentoGestione.annoMovimento"/> / <s:property value="movimentoGestione.numero.toString()"/>
			<s:if test="%{subMovimentoGestione.numero != null}">
				- <s:property value="subMovimentoGestione.numero.toString()"/>
			</s:if>
		</s:if>
	</span>
</h4>
<s:hidden id="HIDDEN_forzaDisponibilitaAccertamento" name="forzaDisponibilitaAccertamento" />
<s:if test="%{richiediConfermaUtente}">
		<s:hidden id="HIDDEN_richiediConfermaUtente" data-messaggio-conferma = "%{messaggioRichiestaConfermaProsecuzione}" />
</s:if>
<fieldset class="form-horizontal imputazioniContabiliMovimentoGestione">
	<div class="control-group">
		<label for="annoMovimentoMovimentoGestione" class="control-label">Anno</label>
		<div class="controls">
			<s:textfield id="annoMovimentoMovimentoGestione" name="movimentoGestione.annoMovimento" cssClass="lbTextSmall soloNumeri span2" maxlength="7" placeholder="%{'anno'}" />
			<span class="alRight">
				<label for="numeroMovimentoGestione" class="radio inline">Numero</label>
			</span>
			<s:textfield id="numeroMovimentoGestione" name="movimentoGestione.numero" cssClass="lbTextSmall soloNumeri span2" maxlength="7" placeholder="%{'numero'}" value="%{movimentoGestione.numero.toString()}" />
			<span class="alRight">
				<label for="numeroSubMovimentoGestione" class="radio inline">Subaccertamento</label>
			</span>
			<s:textfield id="numeroSubMovimentoGestione" name="subMovimentoGestione.numero" cssClass="lbTextSmall soloNumeri span2" maxlength="7" placeholder="%{'numero subaccertamento'}" value="%{subMovimentoGestione.numero.toString()}" />
			<span class="radio guidata">
				<button type="button" class="btn btn-primary" id="pulsanteCompilazioneGuidataMovimentoGestione">compilazione guidata</button>
			</span>
		</div>
	</div>
</fieldset>

<h4 class="step-pane">Soggetto
	<span class="datiRIFSoggetto" id="datiRiferimentoSoggettoSpan">
		<s:if test="%{soggetto != null && soggetto.codiceSoggetto != null && soggetto.denominazione != null && soggetto.codiceFiscale != null
				&& !soggetto.codiceSoggetto.isEmpty() && !soggetto.denominazione.isEmpty() && !soggetto.codiceFiscale.isEmpty()} ">
			<s:property value="soggetto.codiceSoggetto" /> - <s:property value="soggetto.denominazione" /> - <s:property value="soggetto.codiceFiscale" />
		</s:if>
	</span>
</h4>
<fieldset class="form-horizontal imputazioniContabiliSoggetto">
	<div class="control-group">
		<label for="codiceSoggettoSoggetto" class="control-label">Codice</label>
		<div class="controls">
			<s:textfield id="codiceSoggettoSoggetto" name="soggetto.codiceSoggetto" cssClass="span2" />
			<span class="radio guidata">
				<button type="button" class="btn btn-primary" id="pulsanteCompilazioneSoggetto">compilazione guidata</button>
			</span>
			<s:hidden id="HIDDEN_denominazioneSoggetto" name="soggetto.denominazione" />
			<s:hidden id="HIDDEN_codiceFiscaleSoggetto" name="soggetto.codiceFiscale" />
		</div>
	</div>
</fieldset>

<h4 class="step-pane">Provvedimento 
	<span class="datiRIFProvvedimento" id="SPAN_InformazioniProvvedimentoAttoAmministrativo">
		<s:if test="%{attoAmministrativo != null && attoAmministrativo.anno != 0 && attoAmministrativo.numero != 0 
				&& tipoAtto != null && tipoAtto.uid != 0 && tipoAtto.codice != null 
				&& strutturaAmministrativoContabileAttoAmministrativo != null 
				&& strutturaAmministrativoContabileAttoAmministrativo.uid != 0
				&& strutturaAmministrativoContabileAttoAmministrativo.codice != 0}">
			: <s:property value="attoAmministrativo.anno" /> / <s:property value="attoAmministrativo.numero" /> / <s:property value="tipoAtto.codice" /> / <s:property value="strutturaAmministrativoContabileAttoAmministrativo.codice" />
		</s:if>
	</span>
</h4>
<fieldset class="form-horizontal imputazioniContabiliProvvedimento">
	<div class="control-group">
		<label for="annoProvvedimentoAttoAmministrativo" class="control-label">Anno</label>
		<div class="controls">
			<s:textfield id="annoProvvedimentoAttoAmministrativo" name="attoAmministrativo.anno" cssClass="span1" />
			<span class="al">
				<label for="numeroProvvedimentoAttoAmministrativo" class="radio inline">Numero</label>
			</span>
			<s:textfield id="numeroProvvedimentoAttoAmministrativo" name="attoAmministrativo.numero" cssClass="lbTextSmall span2" maxlength="7" />
			<span class="al">
				<label for="tipoAttoAttoAmministrativo" class="radio inline">Tipo </label>
			</span>
			<s:select list="listaTipoAtto" id="tipoAttoAttoAmministrativo" name="tipoAtto.uid" headerKey="0" headerValue="" listKey="uid"
				listValue="%{descrizione}" cssClass="lbTextSmall span4" />
			<s:hidden id="statoOperativoProvvedimentoAttoAmministrativo" name="attoAmministrativo.statoOperativo" />
			<span class="radio guidata">
				<button type="button" class="btn btn-primary" id="pulsanteCompilazioneAttoAmministrativo">compilazione guidata</button>
			</span>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label">Struttura Amministrativa</label>
		<div class="controls">
			<div id="accordionStrutturaAmministrativaContabileAttoAmministrativo" class="accordion span8">
				<div class="accordion-group">
					<div class="accordion-heading">
						<a href="#collapseStrutturaAmministrativaContabileAttoAmministrativo" data-parent="#accordionStrutturaAmministrativaContabileAttoAmministrativo" data-toggle="collapse" class="accordion-toggle collapsed">
							<span id="SPAN_StrutturaAmministrativoContabileAttoAmministrativo">Seleziona la Struttura amministrativa</span>
						</a>
					</div>
					<div class="accordion-body collapse" id="collapseStrutturaAmministrativaContabileAttoAmministrativo">
						<div class="accordion-inner">
							<ul class="ztree treeStruttAmm" id="treeStruttAmmAttoAmministrativo"></ul>
							<br/>
							<button type="button" class="btn btn-primary pull-right" data-dismiss="modal" aria-hidden="true">Conferma</button>
<!-- 							<button type="button" id="BUTTON_deselezionaStrutturaAmministrativoContabileAttoAmministrativo" class="btn btn-secondary">deseleziona</button> -->
						</div>
					</div>
				</div>
			</div>
			<s:hidden id="HIDDEN_StrutturaAmministrativoContabileUidAttoAmministrativo" name="strutturaAmministrativoContabileAttoAmministrativo.uid" />
			<s:hidden id="HIDDEN_StrutturaAmministrativoContabileCodiceAttoAmministrativo" name="strutturaAmministrativoContabileAttoAmministrativo.codice" />
			<s:hidden id="HIDDEN_StrutturaAmministrativoContabileDescrizioneAttoAmministrativo" name="strutturaAmministrativoContabileAttoAmministrativo.descrizione" />
		</div>
	</div>
</fieldset>
