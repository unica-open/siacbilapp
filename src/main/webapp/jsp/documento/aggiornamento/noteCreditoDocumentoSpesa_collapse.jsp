<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<div class="accordion_info">
	<fieldset id="fieldsetInserisciNotaCredito" class="form-horizontal">
		<h4 class="step-pane">Dati principali</h4>
		<fieldset class="form-horizontal">
			<div class="control-group">
				<label for="tipoDocumento" class="control-label">Tipo </label>
				<div class="controls">
					<s:select list="listaTipoDocumentoNote" cssClass="span10" id="tipoDocumento" name="notaCredito.tipoDocumento.uid" headerKey="0" headerValue=""
											listKey="uid" listValue="%{codice + ' - ' + descrizione}" required="true" />				
				</div>
			</div>
			<div class="control-group">
				<label for="annoDocumento" class="control-label">Anno *</label>
				<div class="controls">
					<s:textfield id="annoDocumento" name="notaCredito.anno" cssClass="lbTextSmall span2" required="true" placeholder="anno" maxlength="4" />
					<span class="alRight">
						<label for="numeroDocumento" class="radio inline">Numero *</label>
					</span>
					<s:textfield id="numeroDocumento" name="notaCredito.numero" cssClass="lbTextSmall span2" required="true" placeholder="numero" maxlength="200" />
					<span class="alRight">
						<label for="dataEmissioneDocumento" class="radio inline">Data *</label>
					</span>
					<s:textfield id="dataEmissioneDocumento" name="notaCredito.dataEmissione" cssClass="lbTextSmall span2 datepicker" size="10" />
				</div>
			</div>
			
			<div class="control-group">
				<label for="descrizioneDocumento" class="control-label">Descrizione *</label>
				<div class="controls">
					<s:textarea id="descrizioneDocumento" name="notaCredito.descrizione" cols="15" rows="2" cssClass="span10" required="required"></s:textarea>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label">Dati repertorio/protocollo</label>
				<div class="controls">
					<span class="alRight">
						<label class="radio inline" for="numeroRepertorioDocumento">Numero</label>
					</span>
					<s:textfield id="numeroRepertorioDocumento" name="notaCredito.numeroRepertorio" cssClass="lbTextSmall span2" placeholder="numero" />
					<span class="alRight">
						<label class="radio inline" for="dataRepertorioDocumento">Data</label>
					</span>
					<s:textfield id="dataRepertorioDocumento" name="notaCredito.dataRepertorio" cssClass="lbTextSmall span2 datepicker" placeholder="data" />
				</div>
			</div>
			<div class="control-group">
				<label for="noteDocumento" class="control-label">Note</label>
				<div class="controls">
					<s:textarea id="noteDocumento" name="notaCredito.note" rows="3" cols="15" cssClass="span10"></s:textarea>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="importoNotaDocumento">Importo *</label>
				<div class="controls">
					<s:textfield id="importoNotaDocumento" cssClass="lbTextSmall span2 soloNumeri decimale" name="notaCredito.importo" placeholder="importo" required="required" />
					<span class="alRight">
						<label for="importoDaDedurreSuFattura" class="radio inline">Importo da dedurre su fattura *</label>
					</span>
					<s:textfield id="importoDaDedurreSuFattura" cssClass="lbTextSmall span2 soloNumeri decimale" name="importoDaDedurreSuFattura" placeholder="importo da dedurre" required="required" />
				</div>
			</div>			
		</fieldset>
			
		<h4 class="step-pane">Provvedimento
			<span id="SPAN_InformazioniProvvedimento_NOTECREDITO">
				<s:if test="%{attoAmministrativo != null && attoAmministrativo.anno != 0 && attoAmministrativo.numero != 0 && tipoAtto.uid != 0}">
					: <s:property value="attoAmministrativo.anno" />
					/ <s:property value="attoAmministrativo.numero" />
					- <s:property value="tipoAtto.codice" />
					- <s:property value="attoAmministrativo.oggetto" />
					<s:if test="%{strutturaAmministrativoContabile.uid != 0}">
						- <s:property value="strutturaAmministrativoContabile.codice" />-<s:property value="strutturaAmministrativoContabile.descrizione" />
					</s:if>
					- Stato: <s:property value="attoAmministrativo.statoOperativo" />
				</s:if>
			</span>
		</h4>
		<fieldset class="form-horizontal">
			<div class="control-group">
				<label class="control-label" for="annoProvvedimento_NOTECREDITO">Anno</label>
				<div class="controls">
					<s:textfield id="annoProvvedimento_NOTECREDITO" cssClass="lbTextSmall span1 soloNumeri" name="attoAmministrativo.anno" />
					<span class="al">
						<label class="radio inline" for="numeroProvvedimento_NOTECREDITO">Numero</label>
					</span>
					<s:textfield id="numeroProvvedimento_NOTECREDITO" cssClass="lbTextSmall span2 soloNumeri" name="attoAmministrativo.numero" maxlength="7" />
					<span class="al">
						<label class="radio inline" for="tipoAttoProvvedimento_NOTECREDITO">Tipo</label>
					</span>
					<s:select list="listaTipoAtto" listKey="uid" listValue="descrizione" name="tipoAtto.uid" id="tipoAttoProvvedimento_NOTECREDITO" cssClass="span4" headerKey="0" headerValue="" />
					<s:hidden id="HIDDEN_statoProvvedimento_NOTECREDITO" name="attoAmministrativo.statoOperativo" />
					<span class="radio guidata">
						<a href="#" id="pulsanteApriModaleProvvedimento_NOTECREDITO" class="btn btn-primary">compilazione guidata</a>
					</span>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">Struttura Amministrativa</label>
				<div class="controls">
					<div class="accordion span8 struttAmm">
						<div class="accordion-group">
							<div class="accordion-heading">
								<a class="accordion-toggle" id="accordionPadreStrutturaAmministrativa_NOTECREDITO" href="#struttAmm_NOTECREDITO">
									<span id="SPAN_StrutturaAmministrativoContabile_NOTECREDITO">Seleziona la Struttura amministrativa</span>
									<i class="icon-spin icon-refresh spinner"></i>
								</a>
							</div>
							<div id="struttAmm_NOTECREDITO" class="accordion-body collapse">
								<div class="accordion-inner">
									<ul id="treeStruttAmm_NOTECREDITO" class="ztree treeStruttAmm"></ul>
								</div>
							</div>
						</div>
					</div>
											
					<s:hidden id="HIDDEN_StrutturaAmministrativoContabileUid_NOTECREDITO" name="strutturaAmministrativoContabile.uid" />
					<s:hidden id="HIDDEN_StrutturaAmministrativoContabileCodice_NOTECREDITO" name="strutturaAmministrativoContabile.codice" />
					<s:hidden id="HIDDEN_StrutturaAmministrativoContabileDescrizione_NOTECREDITO" name="strutturaAmministrativoContabile.descrizione" />
				</div>
			</div>
		</fieldset> 
	</fieldset>	

	<p>
		<a class="btn btn-secondary" id="pulsanteAnnullaInserisciNotaCredito">annulla</a>
		<a class="btn btn-primary pull-right" id="pulsanteSalvaInserisciNotaCredito">
			salva&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_pulsanteNotaCreditoSalva"></i>
		</a>
	</p>
</div>
<s:include value="/jsp/provvedimento/selezionaProvvedimento_modale_new.jsp" />
