<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<fieldset class="form-horizontal" id="formRicercaProvvedimentoAggiuntivo">
	<div class="control-group">
		<label class="control-label" for="annoRicercaProvvedimentoAggiuntivo">Anno *</label>
		<div class="controls">
			<s:textfield id="annoRicercaProvvedimentoAggiuntivo" cssClass="lbTextSmall span1 soloNumeri" maxlength="4" />
			<span class="al">
				<label class="radio inline" for="numeroRicercaProvvedimentoAggiuntivo">Numero</label>
			</span>
			<s:textfield id="numeroRicercaProvvedimentoAggiuntivo" cssClass="lbTextSmall span1 soloNumeri" maxlength="6" />
			<span class="al">
				<label class="radio inline" for="tipoAttoRicercaProvvedimentoAggiuntivo">Tipo</label>
			</span>
			<s:select list="listaTipoAtto" listKey="uid" listValue="descrizione" id="tipoAttoRicercaProvvedimentoAggiuntivo" cssClass="lbTextSmall span2"
				headerKey="" headerValue="" />
		</div>
	</div>
	<div class="control-group">
		<label class="control-label">Struttura Amministrativa</label>
		<div class="controls">
			<a href="#strutturaAmministrativoContabileRicercaProvvedimentoAggiuntivo" role="button" class="btn" data-toggle="modal">
				Seleziona la Struttura amministrativa&nbsp;
				<i class="icon-spin icon-refresh spinner" id="SPINNER_StrutturaAmministrativoContabileRicercaProvvedimentoAggiuntivo"></i>
			</a>&nbsp;
			<span id="SPAN_StrutturaAmministrativoContabileRicercaProvvedimentoAggiuntivo">
				<%-- <s:if test='%{strutturaAmministrativoContabileAggiuntivo.codice != null && strutturaAmministrativoContabileAggiuntivo.codice neq ""}'>
					<s:property value="strutturaAmministrativoContabileAggiuntivo.codice"/> - <s:property value="strutturaAmministrativoContabileAggiuntivo.descrizione"/>
				</s:if><s:else> --%>
					Nessuna Struttura Amministrativa Responsabile selezionata 
				<%-- </s:else> --%>
			</span>
			<s:hidden id="HIDDEN_StrutturaAmministrativoContabileUidRicercaProvvedimentoAggiuntivo" />
			<s:hidden id="HIDDEN_StrutturaAmministrativoContabileCodiceRicercaProvvedimentoAggiuntivo" />
			<s:hidden id="HIDDEN_StrutturaAmministrativoContabileDescrizioneRicercaProvvedimentoAggiuntivo"/>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="oggettoRicercaProvvedimentoAggiuntivo">Oggetto</label>
		<div class="controls">
			<s:textfield id="oggettoRicercaProvvedimentoAggiuntivo" cssClass="lbTextSmall span9" maxlength="500" />
			<a class="btn btn-primary" href="#" id="pulsanteRicercaProvvedimentoAggiuntivo">
				<i class="icon-search icon"></i>&nbsp;cerca&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_ProvvedimentoAggiuntivo"></i>
			</a>
			<span class="nascosto"> | </span>
		</div>
	</div>

	<!-- Modal -->
	<div id="strutturaAmministrativoContabileRicercaProvvedimentoAggiuntivo" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
			<h3 id="myModalLabel2">Struttura Amministrativa Responsabile</h3>
		</div>
		<div class="modal-body">
			<ul id="treeStrutturaAmministrativoContabileRicercaProvvedimentoAggiuntivo" class="ztree"></ul>
		</div>
		<div class="modal-footer">
			<button type="button" id="deselezionaStrutturaAmministrativoContabileRicercaProvvedimentoAggiuntivo" class="btn">Deseleziona</button>
		</div>
	</div>
</fieldset>

<div id="tabellaProvvedimentoAggiuntivo" class="hide">
	<h4 id="informazioniProvvedimentoAggiuntivo">
			Elenco provvedimenti trovati
	</h4>
	<table class="table table-hover" id="risultatiRicercaProvvedimentoAggiuntivo">
		<thead>
			<tr>
				<th></th>
				<th scope="col">Anno</th>
				<th scope="col">Numero</th>
				<th scope="col">Tipo</th>
				<th scope="col">Oggetto</th>
				<th scope="col"><abbr title="Struttura Amministrativa Responsabile">Strutt Amm Resp</abbr></th>
				<th scope="col">Stato</th>
			</tr>
		</thead>
		<tbody>
		</tbody>
		<tfoot>
		</tfoot>
	</table>
	<a class="btn" href="#" id="pulsanteSelezionaProvvedimentoAggiuntivo">seleziona</a>
	<a class="btn" href="#" id="pulsanteDeselezionaProvvedimentoAggiuntivo">deseleziona</a>
</div>
<s:hidden id="HIDDEN_uidProvvedimentoAggiuntivo" name="uidProvvedimentoAggiuntivo" />
<s:hidden id="HIDDEN_uidProvvedimentoAggiuntivoInjettato" name="attoAmministrativoAggiuntivo.uid" />
<s:hidden id="HIDDEN_tipoAttoUidAggiuntivo" name="attoAmministrativoAggiuntivo.tipoAtto.uid" />
<s:hidden id="HIDDEN_tipoAttoDescrizioneAggiuntivo" name="attoAmministrativoAggiuntivo.tipoAtto.descrizione" />
<s:hidden id="HIDDEN_tipoAttoCodiceAggiuntivo" name="attoAmministrativoAggiuntivo.tipoAtto.codice" />
<s:hidden id="HIDDEN_numeroProvvedimentoAggiuntivo" name="attoAmministrativoAggiuntivo.numero" />
<s:hidden id="HIDDEN_annoProvvedimentoAggiuntivo" name="attoAmministrativoAggiuntivo.anno" />
<s:hidden id="HIDDEN_statoProvvedimentoAggiuntivo" name="attoAmministrativoAggiuntivo.statoOperativo" />