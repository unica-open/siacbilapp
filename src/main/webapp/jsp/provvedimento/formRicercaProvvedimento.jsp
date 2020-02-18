<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<fieldset class="form-horizontal" id="formRicercaProvvedimento">
	<div class="control-group">
		<label class="control-label" for="annoRicercaProvvedimento">Anno *</label>
		<div class="controls">
			<s:textfield id="annoRicercaProvvedimento" cssClass="lbTextSmall span1 soloNumeri" maxlength="4" />
			<span class="al">
				<label class="radio inline" for="numeroRicercaProvvedimento">Numero</label>
			</span>
			<s:textfield id="numeroRicercaProvvedimento" cssClass="lbTextSmall span1 soloNumeri" maxlength="7" />
			<span class="al">
				<label class="radio inline" for="tipoAttoRicercaProvvedimento">Tipo</label>
			</span>
			<s:select list="listaTipoAtto" listKey="uid" listValue="descrizione" id="tipoAttoRicercaProvvedimento" cssClass="lbTextSmall span2"
				headerKey="" headerValue="" />
		</div>
	</div>
	<div class="control-group">
		<label class="control-label">Struttura Amministrativa</label>
		<div class="controls">
			<a id="bottoneSACRicercaProvvedimento" href="#strutturaAmministrativoContabileProvvedimento" role="button" class="btn" data-toggle="modal">
				Seleziona la Struttura amministrativa&nbsp;
				<i class="icon-spin icon-refresh spinner" id="SPINNER_StrutturaAmministrativoContabileRicercaProvvedimento"></i>
			</a>&nbsp;
			<span id="SPAN_StrutturaAmministrativoContabileRicercaProvvedimento">
				<%-- <s:if test='%{strutturaAmministrativoContabile.codice != null && strutturaAmministrativoContabile.codice neq ""}'>
					<s:property value="strutturaAmministrativoContabile.codice"/> - <s:property value="strutturaAmministrativoContabile.descrizione"/>
				</s:if><s:else> --%>
					Nessuna Struttura Amministrativa Responsabile selezionata 
			<%-- 	</s:else> --%>
			</span>
			<s:hidden id="HIDDEN_StrutturaAmministrativoContabileUidRicercaProvvedimento" />
			<s:hidden id="HIDDEN_StrutturaAmministrativoContabileCodiceRicercaProvvedimento" />
			<s:hidden id="HIDDEN_StrutturaAmministrativoContabileDescrizioneRicercaProvvedimento"/>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="oggettoRicercaProvvedimento">Oggetto</label>
		<div class="controls">
			<s:textfield id="oggettoRicercaProvvedimento" cssClass="lbTextSmall span9" maxlength="500" />
			<a class="btn btn-primary" href="#" id="pulsanteRicercaProvvedimento">
				<i class="icon-search icon"></i>&nbsp;cerca&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_Provvedimento"></i>
			</a>
			<span class="nascosto"> | </span>
		</div>
	</div>

	<!-- Modal -->
	<div id="strutturaAmministrativoContabileRicercaProvvedimento" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
			<h3 id="myModalLabel2">Struttura Amministrativa Responsabile</h3>
		</div>
		<div class="modal-body">
			<ul id="treeStrutturaAmministrativoContabileRicercaProvvedimento" class="ztree"></ul>
		</div>
		<div class="modal-footer">
			<button id="deselezionaStrutturaAmministrativoContabileRicercaProvvedimento" type="button" class="btn">Deseleziona</button>
		</div>
	</div>
</fieldset>

<div id="tabellaProvvedimento" class="hide">
	<h4 id="informazioniProvvedimento">
			Elenco provvedimenti trovati
	</h4>
	<table class="table table-hover" id="risultatiRicercaProvvedimento">
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
	<a class="btn" href="#" id="pulsanteSelezionaProvvedimento">seleziona</a>
	<a class="btn" href="#" id="pulsanteDeselezionaProvvedimento">deseleziona</a>
</div>
<s:hidden id="HIDDEN_uidProvvedimento" name="uidProvvedimento" />
<s:hidden id="HIDDEN_uidProvvedimentoInjettato" name="attoAmministrativo.uid" />
<s:hidden id="HIDDEN_tipoAttoUid" name="attoAmministrativo.tipoAtto.uid" />
<s:hidden id="HIDDEN_tipoAttoCodice" name="attoAmministrativo.tipoAtto.codice" />
<s:hidden id="HIDDEN_tipoAttoDescrizione" name="attoAmministrativo.tipoAtto.descrizione" />
<s:hidden id="HIDDEN_numeroProvvedimento" name="attoAmministrativo.numero" />
<s:hidden id="HIDDEN_annoProvvedimento" name="attoAmministrativo.anno" />
<s:hidden id="HIDDEN_statoProvvedimento" name="attoAmministrativo.statoOperativo" />