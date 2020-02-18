<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<div id="modaleInserimentoProvvedimento" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="modaleInserimentoProvvedimento_label" aria-hidden="true">
	<div class="row-fluid">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
			<h4 class="nostep-pane">Inserisci provvedimento</h4>
		</div>
		<div class="modal-body">
			<fieldset class="form-horizontal" id="modaleInserimentoProvvedimento_fieldset">
				<div class="alert alert-error hide" id="modaleInserimentoProvvedimento_ERRORI">
					<button type="button" class="close" data-hide="alert">&times;</button>
					<strong>Attenzione!!</strong><br>
					<ul></ul>
				</div>
				<br>
				<div class="control-group">
					<label class="control-label" for="modaleInserimentoProvvedimento_anno">Anno *</label>
					<div class="controls">
						<input type="text" name="inserimento.attoAmministrativo.anno" id="modaleInserimentoProvvedimento_anno" class="lbTextSmall span2 soloNumeri" maxlength="4" placeholder="Anno" />
						<span class="al">
							<label class="radio inline" for="modaleInserimentoProvvedimento_numero">Numero *</label>
						</span>
						<input type="text" name="inserimento.attoAmministrativo.numero" id="modaleInserimentoProvvedimento_numero" class="lbTextSmall span2 soloNumeri" maxlength="7" placeholder="Numero" />
						<span class="al">
							<label class="radio inline" for="modaleInserimentoProvvedimento_tipoAtto">Tipo Atto *</label>
						</span>
						<select name="inserimento.attoAmministrativo.tipoAtto.uid" id="modaleInserimentoProvvedimento_tipoAtto" class="lbTextSmall span3">
							<option value=""></option>
							<s:iterator value="listaTipoAtto" var="ta">
								<option value="<s:property value="#ta.uid" />"<s:if test="#ta.progressivoAutomatico"> data-progressivo-automatico</s:if>><s:property value="#ta.descrizione" /></option>
							</s:iterator>
						</select>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">Struttura Amministrativa</label>
					<div class="controls">
						<div class="accordion span9 struttAmm" id="modaleInserimentoProvvedimento_accordionPadreStrutturaAmministrativa">
							<div class="accordion-group">
								<div class="accordion-heading">
									<a href="#modaleInserimentoProvvedimento_struttAmm" data-toggle="collapse" class="accordion-toggle collapsed">
										<span id="SPAN_StrutturaAmministrativoContabile_modaleInserimentoProvvedimento">Seleziona la Struttura amministrativa</span>
									</a>
								</div>
								<div id="modaleInserimentoProvvedimento_struttAmm" class="accordion-body collapse">
									<div class="accordion-inner">
										<ul id="modaleInserimentoProvvedimento_treeStruttAmm" class="ztree treeStruttAmm"></ul>
									</div>
								</div>
								<input type="hidden" id="HIDDEN_StrutturaAmministrativoContabile_modaleInserimentoProvvedimentoUid" name="inserimento.attoAmministrativo.strutturaAmmContabile.uid"/>
							</div>
						</div>
					</div>
				</div>
			
				<div class="control-group">
					<label class="control-label" for="modaleInserimentoProvvedimento_oggetto">Oggetto</label>
					<div class="controls">
						<input type="text" id="modaleInserimentoProvvedimento_oggetto" class="lbTextSmall span9" name="inserimento.attoAmministrativo.oggetto" maxlength="500" />
					</div>
				</div>
				<div class="control-group">
					<label for="modaleInserimentoProvvedimento_statoOperativo" class="control-label">Stato operativo</label>
					<div class="controls">
						<s:select list="listaStatoOperativoAtti" name="inserimento.attoAmministrativo.statoOperativo" id="modaleInserimentoProvvedimento_statoOperativo" cssClass="lbTextSmall span10" headerKey="" headerValue="" listKey="descrizione" listValue="descrizione" />
					</div>
				</div>
				<div class="control-group">
					<label for="modaleInserimentoProvvedimento_note" class="control-label">Note</label>
					<div class="controls">
						<textarea rows="5" cols="15" id="modaleInserimentoProvvedimento_note" name="inserimento.attoAmministrativo.note" class="span10" maxlength="500"></textarea>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label" for="modaleInserimentoProvvedimento_parereRegolaritaContabile">Parere di Regolarit&agrave; Contabile *</label>
					<div class="controls">
						<input type="checkbox" id="modaleInserimentoProvvedimento_parereRegolaritaContabile" disabled />
					</div>
				</div>
			</fieldset>
		</div>
		<div class="modal-footer">
			<button type="button" class="btn btn-secondary" data-dismiss="modal" aria-hidden="true">Annulla</button>
			<button type="button" class="btn btn-primary" aria-hidden="true" id="modaleInserimentoProvvedimento_pulsanteConferma">Inserisci</button>
		</div>
	</div>
</div>


