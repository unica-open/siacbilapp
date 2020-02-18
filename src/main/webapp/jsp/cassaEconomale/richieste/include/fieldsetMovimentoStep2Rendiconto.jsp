<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<div class="well" id="wellDatiEconomo">
	<s:hidden name="richiestaEconomale.movimento.uid" />
	<s:hidden name="richiestaEconomale.movimento.numeroMovimento" />
	<s:hidden name="rendicontoRichiesta.movimento.uid" />
	<s:hidden name="rendicontoRichiesta.movimento.numeroMovimento" />
	<div class="control-group">
		<label class="control-label" for="dataMovimentoMovimento">Data operazione&nbsp;*</label>
		<div class="controls">
			<s:textfield id="dataMovimentoMovimento" name="rendicontoRichiesta.movimento.dataMovimento" cssClass="span2 datepicker" required="true" />
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="modalitaPagamentoCassaMovimento">Modalit&agrave; di pagamento cassa&nbsp;*</label>
		<div class="controls">
			<s:select list="listaModalitaPagamentoCassa" id="modalitaPagamentoCassaMovimento" name="rendicontoRichiesta.movimento.modalitaPagamentoCassa.uid" cssClass="span9"
				listKey="uid" listValue="descrizione" headerKey="" headerValue="" required="true" disabled="%{!cassaMista || altroUfficio}" />
			<s:if test="%{!cassaMista || altroUfficio}">
				<s:hidden name="rendicontoRichiesta.movimento.modalitaPagamentoCassa.uid"/>
			</s:if>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="modalitaPagamentoDipendenteMovimento">Modalit&agrave; di pagamento del creditore&nbsp;*</label>
		<div class="controls">
		
			<s:select list="listaModalitaPagamentoDipendente" id="modalitaPagamentoDipendenteMovimento"
				name="rendicontoRichiesta.movimento.modalitaPagamentoDipendente.uid" cssClass="span9"
				listKey="uid" listValue="%{codice + ' - ' + descrizione}" headerKey="" headerValue="" required="true" disabled="%{altroUfficio}"/>
			<s:if test="%{altroUfficio}">
				<s:hidden name="rendicontoRichiesta.movimento.modalitaPagamentoDipendente.uid"/>
			</s:if>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="dettaglioPagamentoMovimento">Dettaglio del pagamento&nbsp;*</label>
		<div class="controls">
			<s:textfield id="dettaglioPagamentoMovimento" name="rendicontoRichiesta.movimento.dettaglioPagamento" cssClass="span9" required="true" disabled="%{altroUfficio}" />
			<s:if test="%{altroUfficio}">
				<s:hidden name="rendicontoRichiesta.movimento.dettaglioPagamento"/>
			</s:if>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="bicMovimento"><abbr title="Business Identifier Code">BIC</abbr></label>
		<div class="controls">
			<s:textfield id="bicMovimento" name="rendicontoRichiesta.movimento.bic" cssClass="span9" maxlength="11" />
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="contoCorrenteMovimento">Conto corrente</label>
		<div class="controls">
			<s:textfield id="contoCorrenteMovimento" name="rendicontoRichiesta.movimento.contoCorrente" cssClass="span9" />
		</div>
	</div>
	<s:hidden name="richiestaEconomale.movimento.modalitaPagamentoSoggetto.uid" id="modalitaPagamentoSoggettoMovimento" />
	<div class="control-group<s:if test="!datiEconomoCompilati"> hide</s:if>" data-importo-economo>
		<label class="control-label" for="importoDaRestituire">Importo restituito</label>
		<div class="controls">
			<s:textfield id="importoDaRestituire" name="importoDaRestituire" cssClass="span3 soloNumeri decimale" readonly="true" />
		</div>
	</div>
	<div class="control-group<s:if test="!datiEconomoCompilati"> hide</s:if>" data-importo-economo>
		<label class="control-label" for="importoDaIntegrare">Importo integrato</label>
		<div class="controls">
			<s:textfield id="importoDaIntegrare" name="importoDaIntegrare" cssClass="span3 soloNumeri decimale" readonly="true" />
		</div>
	</div>
</div>