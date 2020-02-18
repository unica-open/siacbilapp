<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<div class="well">
	<s:hidden name="richiestaEconomale.movimento.uid" />
	<s:hidden name="richiestaEconomale.movimento.numeroMovimento" />
	<div class="control-group">
		<label class="control-label" for="dataMovimentoMovimento">Data operazione&nbsp;*</label>
		<div class="controls">
			<s:textfield id="dataMovimentoMovimento" name="richiestaEconomale.movimento.dataMovimento" cssClass="span2 datepicker" required="true" />
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="modalitaPagamentoCassaMovimento">Modalit&agrave; di pagamento cassa&nbsp;*</label>
		<div class="controls">
			<s:select list="listaModalitaPagamentoCassa" id="modalitaPagamentoCassaMovimento"
				name="richiestaEconomale.movimento.modalitaPagamentoCassa.uid" cssClass="span9"
				listKey="uid" listValue="descrizione" headerKey="" headerValue="" required="true" disabled="%{!cassaMista}" />
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="modalitaPagamentoDipendenteMovimento">Modalit&agrave; di pagamento del creditore&nbsp;*</label>
		<div class="controls">
			<s:select list="listaModalitaPagamentoDipendente" id="modalitaPagamentoDipendenteMovimento"
				name="richiestaEconomale.movimento.modalitaPagamentoDipendente.uid" cssClass="span9"
				listKey="uid" listValue="%{codice + ' - ' + descrizione}" headerKey="" headerValue="" required="true" disabled="cassaContanti" />
			<s:if test="cassaContanti">
				<s:hidden name="richiestaEconomale.movimento.modalitaPagamentoDipendente.uid" />
			</s:if>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="dettaglioPagamentoMovimento">Dettaglio del pagamento&nbsp;*</label>
		<div class="controls">
			<s:textfield id="dettaglioPagamentoMovimento" name="richiestaEconomale.movimento.dettaglioPagamento" cssClass="span9" required="true" />
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="bicMovimento"><abbr title="Business Identifier Code">BIC</abbr></label>
		<div class="controls">
			<s:textfield id="bicMovimento" name="richiestaEconomale.movimento.bic" cssClass="span9" maxlength="11" />
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="contoCorrenteMovimento">Conto corrente</label>
		<div class="controls">
			<s:textfield id="contoCorrenteMovimento" name="richiestaEconomale.movimento.contoCorrente" cssClass="span9" />
		</div>
	</div>
	<s:hidden name="richiestaEconomale.movimento.modalitaPagamentoSoggetto.uid" id="modalitaPagamentoSoggettoMovimento" />
</div>