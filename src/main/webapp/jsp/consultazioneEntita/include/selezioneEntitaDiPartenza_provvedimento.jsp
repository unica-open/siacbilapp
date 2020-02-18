<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<s:form id = "formSelezionaEntitaDiPartenza_provvedimento" action="" cssClass="form-horizontal hide" novalidate="novalidate">
	<div  id="buttonSliding"  data-toggle="slidewidth" class=" fieldset-heading button-sliding" data-target="#selezioneConsultazioneEntitaCollegate">								
		<h4>
			<i id ="buttonSlidingIcon" data-original-title="" class="icon-double-angle-left sliding-icon icon-large tooltip-test defaultcolor"></i>&nbsp;									
			Selezione provvedimento
		</h4>								
	</div>
	<p>Specificare l'elemento da cui iniziare la consultazione:</p>
		<h3>&nbsp;</h3>
		<div class="control-group">
			<label class="control-label" for="annoProvvedimento">Anno *</label>
			<div class="controls">
				<s:textfield id="annoProvvedimento"
					cssClass="lbTextSmall span3 soloNumeri"
					name="annoProvvedimento" maxlength="4" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="numeroProvvedimento">Numero</label>
			<div class="controls">
				<s:textfield id="numeroProvvedimento"
					cssClass="lbTextSmall span5 soloNumeri"
					name="numeroProvvedimento" maxlength="6" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="tipoAttoProvvedimento">Tipo</label>
			<div class="controls">
				<select id="tipoAttoProvvedimento" name="tipoAttoProvvedimento.uid" class="span6 loading-data" disabled="disabled">
					<option value="0"></option>
				</select>
				<s:hidden id="statoOperativoAttoAmministrativo"
					name="attoAmministrativo.statoOperativo" />
			</div>
		</div> 

		<div class="control-group span11">
			<label class="control-label">Struttura Amministrativa</label>
			<div class="controls">
				<div class="accordion struttAmm">
					<div class="accordion-group">
						<div class="accordion-heading">
							<a class="accordion-toggle"
								id="accordionPadreStrutturaAmministrativa" href="#struttAmmProvvedimento">
								<span id="SPAN_StrutturaAmministrativoContabile_treeStruttAmmProvvedimento">
									Seleziona la Struttura amministrativa </span> <i
								class="icon-spin icon-refresh spinner"></i>
							</a>
						</div>
						<div id="struttAmmProvvedimento" class="accordion-body collapse">
							<div class="accordion-inner">
								<ul id="treeStruttAmmProvvedimento" class="ztree treeStruttAmm"></ul>
								<button type="button"
									id="pulsanteDeselezionaStrutturaAmministrativoContabile_treeStruttAmmProvvedimento"
									class="btn deselezionaSACProvvedimento">Deseleziona</button>
							</div>
						</div>
					</div>
				</div>

				<s:hidden id="HIDDEN_StrutturaAmministrativoContabile_treeStruttAmmProvvedimento_uid" name="strutturaAmministrativoContabile.uid" />

			</div>
		</div>
		<div class=control-group>
			<s:submit id = "pulsanteRicercaEntitaDiPartenzaProvvedimento" data-class-name="it.csi.siac.siacconsultazioneentitaser.model.ProvvedimentoConsultabile" cssClass="btn btn-primary btn-block" value="cerca" />
		</div>

</s:form>