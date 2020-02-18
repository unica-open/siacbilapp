<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<div class="accordion_info">
	<fieldset id="fieldsetOperazioneCassa" class="form-horizontal">
		<h4 class="titleTxt nostep-pane"><s:property value="operazioneOperazioneCassa.descrizione" /></h4>
		<s:hidden id="uidOperazioneCassa" name="operazioneCassa.uid" />
		<s:hidden id="numeroOperazioneOperazioneCassa" name="operazioneCassa.numeroOperazione" />
		<s:hidden id="statoOperativoOperazioneCassaOperazioneCassa" name="operazioneCassa.statoOperativoOperazioneCassa" />
		<h4 class="step-pane">Dati</h4>
		<div class="control-group">
			<label class="control-label" for="dataOperazioneOperazioneCassa">Data operazione *</label>
			<div class="controls">
				<s:textfield id="dataOperazioneOperazioneCassa" name="operazioneCassa.dataOperazione" cssClass="span2 datepicker" required="true" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="tipoOperazioneOperazioneCassa">Tipo operazione *</label>
			<div class="controls">
				<s:select list="listaTipoOperazioneCassa" id="tipoOperazioneOperazioneCassa" name="operazioneCassa.tipoOperazioneCassa.uid" cssClass="span9"
					required="true" headerKey="" headerValue="" listKey="uid" listValue="%{tipologiaOperazioneCassa.codice + ' - ' + descrizione}" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="importoOperazioneCassa">Importo *</label>
			<div class="controls">
				<s:textfield id="importoOperazioneCassa" name="operazioneCassa.importo" cssClass="span4 soloNumeri decimale" required="true" value="%{operazioneCassa.importo.abs()}" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="modalitaPagamentoCassaOperazioneCassa">Modalit&agrave; di pagamento *</label>
			<div class="controls">
				<s:select list="listaModalitaPagamentoCassa" id="modalitaPagamentoCassaOperazioneCassa" name="operazioneCassa.modalitaPagamentoCassa.uid"
					cssClass="span9" required="true" headerKey="" headerValue="" listKey="uid" listValue="%{codice + ' - ' + descrizione}" disabled="%{!cassaMista}" />
				<s:if test="%{!cassaMista}">
					<s:hidden id="modalitaPagamentoCassaOperazioneCassa" name="operazioneCassa.modalitaPagamentoCassa.uid" />
				</s:if>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="noteOperazioneCassa">Note</label>
			<div class="controls">
				<s:textarea id="noteOperazioneCassa" name="operazioneCassa.note" cssClass="span9" cols="15" rows="2"></s:textarea>
			</div>
		</div>
		
		<h4 class="step-pane">Provvedimento
			<span class="datiRIFProvvedimento" id="datiRiferimentoProvvedimentoSpan">
				<s:if test="%{attoAmministrativo != null && attoAmministrativo.anno != 0 && attoAmministrativo.numero != 0
					&& attoAmministrativo.tipoAtto != null && attoAmministrativo.tipoAtto.uid != 0}">
					: <s:property value="attoAmministrativo.anno" />
					/ <s:property value="attoAmministrativo.numero" />
					/ <s:property value="attoAmministrativo.tipoAtto.codice" />
					<s:if test="%{attoAmministrativo.strutturaAmmContabile != null && attoAmministrativo.strutturaAmmContabile.uid != 0}">
						/ <s:property value="attoAmministrativo.strutturaAmmContabile.codice" />
					</s:if>
				</s:if>
			</span>
		</h4>
		<fieldset class="form-horizontal imputazioniContabiliProvvedimento">
			<div class="control-group">
				<label for="annoAttoAmministrativo" class="control-label">Anno</label>
				<div class="controls">
					<s:textfield id="annoAttoAmministrativo" name="attoAmministrativo.anno" cssClass="span1" />
					<span class="al">
						<label for="numeroAttoAmministrativo" class="radio inline">Numero</label>
					</span>
					<s:textfield id="numeroAttoAmministrativo" name="attoAmministrativo.numero" cssClass="lbTextSmall span2" maxlength="7" />
					<span class="al">
						<label for="tipoAtto" class="radio inline">Tipo </label>
					</span>
					<s:select list="listaTipoAtto" id="tipoAtto" name="attoAmministrativo.tipoAtto.uid" headerKey="0" headerValue="" listKey="uid"
						listValue="descrizione" cssClass="lbTextSmall span4" />
					<s:hidden id="statoOperativoAttoAmministrativo" name="attoAmministrativo.statoOperativo" />
					<span class="radio guidata">
						<button type="button" class="btn btn-primary" id="pulsanteCompilazioneAttoAmministrativo">compilazione guidata</button>
					</span>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">Struttura Amministrativa</label>
				<div class="controls">
					<div id="accordionStrutturaAmministrativaContabile" class="accordion span8">
						<div class="accordion-group">
							<div class="accordion-heading">
								<a data-target="#collapseStrutturaAmministrativaContabile" data-toggle="collapse"
										data-parent="#accordionStrutturaAmministrativaContabile" class="accordion-toggle collapsed">
									<span id="SPAN_StrutturaAmministrativoContabile">Seleziona la Struttura amministrativa</span>
								</a>
							</div>
							<div class="accordion-body collapse" id="collapseStrutturaAmministrativaContabile">
								<div class="accordion-inner">
									<ul class="ztree treeStruttAmm" id="treeStruttAmm"></ul>
								</div>
							</div>
						</div>
					</div>
					<s:hidden id="HIDDEN_StrutturaAmministrativoContabileUid" name="attoAmministrativo.strutturaAmmContabile.uid" />
					<s:hidden id="HIDDEN_StrutturaAmministrativoContabileCodice" name="attoAmministrativo.strutturaAmmContabile.codice" />
					<s:hidden id="HIDDEN_StrutturaAmministrativoContabileDescrizione" name="attoAmministrativo.strutturaAmmContabile.descrizione" />
				</div>
			</div>
		</fieldset>
		
	</fieldset>
	<p>
		<button type="button" id="annullaOperazioneCassa" class="btn btn-secondary">annulla</button>
		<span class="pull-right">
			<button type="button" id="salvaOperazioneCassa" class="btn btn-primary">salva</button>
		</span>
	</p>
</div>