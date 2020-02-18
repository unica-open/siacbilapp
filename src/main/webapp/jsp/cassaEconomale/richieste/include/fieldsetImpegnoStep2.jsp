<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/siac-tags" prefix="si" %>

<h4 class="step-pane">Impegno<span class="datiRiferimentoImpegnoSpan"></span></h4>
<fieldset class="form-horizontal">
	<div class="control-group">
		<label class="control-label" for="annoMovimentoMovimentoGestione">Anno *</label>
		<div class="controls">
			<s:textfield id="annoMovimentoMovimentoGestione" name="movimentoGestione.annoMovimento" cssClass="lbTextSmall span1 soloNumeri" required="true" maxlength="4" data-overlay="" disabled="disabledImpegnoFields" />
			<span class="alRight">
				<label class="radio inline" for="numeroMovimentoGestione">Numero *</label>
			</span>
			<si:plainstringtextfield id="numeroMovimentoGestione" name="movimentoGestione.numero" cssClass="lbTextSmall span2 soloNumeri" required="true" data-overlay="" disabled="disabledImpegnoFields" />
			<span class="alRight">
				<label class="radio inline" for="numeroSubMovimentoGestione">Sub</label>
			</span>
			<si:plainstringtextfield id="numeroSubMovimentoGestione" name="subMovimentoGestione.numero" cssClass="lbTextSmall span2 soloNumeri" required="true" data-overlay="" disabled="disabledImpegnoFields" />
			<span class="marginLeft2 radio inline collapse_alertGiudata<s:if test="%{disponibileMovimentoGestione == null}"> hide</s:if>" id="containerDisponibileMovimentoGestione">
				<span class="icon-chevron-right icon-red alRight"></span>
				Disponibile <span id="SPAN_disponibileMovimentoGestione"><s:property value="disponibileMovimentoGestione"/></span>
			</span>
			<s:if test="%{!disabledImpegnoFields}">
				<span class="radio guidata">
					<button type="button" class="btn btn-primary" id="pulsanteCompilazioneGuidataMovimentoGestione">compilazione guidata</button>
				</span>
			</s:if>
			<s:if test="disabledImpegnoFields">
				<s:hidden name="movimentoGestione.annoMovimento" />
				<si:plainstringhidden name="movimentoGestione.numero" />
				<si:plainstringhidden name="subMovimentoGestione.numero" />
			</s:if>
		</div>
	</div>
	<div class="control-group <s:if test="%{disponibileMovimentoGestione == null}">hide</s:if>" id="containerFlagCassaEconomale">
		<label class="control-label invisible">Anno *</label>
		<div class="controls">
			<input type="text" disabled class="invisible lbTextSmall span1" maxlength="4" />
			<span class="alRight">
				<label class="radio inline invisible">Numero *</label>
			</span>
			<input type="text" disabled class="invisible lbTextSmall span2" />
			<span class="alRight">
				<label class="radio inline invisible">Sub</label>
			</span>
			<input type="text" disabled class="invisible lbTextSmall span2" />
			<span class="marginLeft2 radio inline collapse_alertGiudata">
				<span class="icon-chevron-right icon-red alRight"></span>
				Impegno di cassa economale: <span id="SPAN_flagCassaEconomaleMovimentoGestione"><s:property value="flagCassaEconomaleMovimentoGestione"/></span>
			</span>
		</div>
	</div>
</fieldset>
<br/>
<div class="boxOrSpan2<s:if test="%{capitoloMovimento == null}"> hide</s:if>" id="datiCapitoloStrutturaMovimentoGestione">
	<div class="boxOrInLeft">
		<p>Capitolo</p>
		<ul class="htmlelt">
			<li>
				<dfn>Capitolo</dfn>
				<dl id="numeroCapitoloCapitolo"><s:property value="capitoloMovimento.numeroCapitolo"/>&nbsp;</dl>
			</li>
			<li>
				<dfn>Articolo</dfn>
				<dl id="numeroArticoloCapitolo"><s:property value="capitoloMovimento.numeroArticolo"/>&nbsp;</dl>
			</li>
			<li>
				<dfn>Descrizione</dfn>
				<dl id="descrizioneCapitolo"><s:property value="capitoloMovimento.descrizione"/>&nbsp;</dl>
			</li>
			<s:if test="%{gestioneUEB}">
				<li>
					<dfn>UEB</dfn>
					<dl id="numeroUEBCapitolo"><s:property value="capitoloMovimento.numeroUEB"/>&nbsp;</dl>
				</li>
			</s:if>
			<li>
				<dfn>Struttura</dfn>
				<dl id="strutturaAmministrativoContabileCapitolo"><s:property value="capitoloMovimento.strutturaAmministrativoContabile.codice"/>&nbsp;</dl>
			</li>
			<li>
				<dfn><abbr title="Tipo finanziamento">TIpo fin.</abbr></dfn>
				<dl id="tipoFinanziamentoCapitolo"><s:property value="capitoloMovimento.tipoFinanziamento.codice"/>&nbsp;</dl>
			</li>
		</ul>
	</div>
	<div class="boxOrInRight">
		<p>Provvedimento</p>
		<ul class="htmlelt">
			<li>
				<dfn>Tipo</dfn>
				<dl id="tipoAttoAttoAmministrativo"><s:property value="attoAmministrativoMovimento.tipoAtto.codice"/>&nbsp;</dl>
			</li>
			<li>
				<dfn>Anno</dfn>
				<dl id="annoAttoAmministrativo"><s:property value="attoAmministrativoMovimento.anno"/>&nbsp;</dl>
			</li>
			<li>
				<dfn>Numero</dfn>
				<dl id="numeroAttoAmministrativo"><s:property value="attoAmministrativoMovimento.numero"/>&nbsp;</dl>
			</li>
			<li>
				<dfn>Struttura</dfn>
				<dl id="strutturaAmmContabileAttoAmministrativo"><s:property value="attoAmministrativoMovimento.strutturaAmmContabile.codice"/>&nbsp;</dl>
			</li>
			<li>
				<dfn>Oggetto</dfn>
				<dl id="oggettoAttoAmministrativo"><s:property value="attoAmministrativoMovimento.oggetto"/>&nbsp;</dl>
			</li>
			<li>
				<dfn>Stato</dfn>
				<dl id="statoOperativoAttoAmministrativo"><s:property value="attoAmministrativoMovimento.statoOperativo"/>&nbsp;</dl>
			</li>
		</ul>
	</div>
</div>