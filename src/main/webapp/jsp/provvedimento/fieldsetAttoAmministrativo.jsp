<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<fieldset class="form-horizontal">
	<div class="control-group">
		<label class="control-label" for="annoAttoAmministrativo">Anno</label>
		<div class="controls">
			<s:textfield id="annoAttoAmministrativo" cssClass="lbTextSmall span1 soloNumeri" name="attoAmministrativo.anno" maxlength="4" />
			<span class="al">
				<label class="radio inline" for="numeroAttoAmministrativo">Numero</label>
			</span>
			<s:textfield id="numeroAttoAmministrativo" cssClass="lbTextSmall span2 soloNumeri" name="attoAmministrativo.numero" maxlength="7" />
			<span class="al">
				<label class="radio inline" for="tipoAtto">Tipo</label>
			</span>
			<s:select list="listaTipoAtto" listKey="uid" listValue="descrizione" name="tipoAtto.uid"
				id="tipoAtto" cssClass="span4" headerKey="0" headerValue="" />
			<s:hidden id="statoOperativoAttoAmministrativo" name="attoAmministrativo.statoOperativo" />
			<span class="radio guidata">
				<a href="#" id="pulsanteApriModaleProvvedimento" class="btn btn-primary">compilazione guidata</a>
			</span>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label">Struttura Amministrativa</label>
		<div class="controls">
			<div class="accordion span8 struttAmm" id="accordionStrutturaAmministrativaContabileAttoAmministrativo">
				<div class="accordion-group">
					<div class="accordion-heading">
						<a class="accordion-toggle" id="accordionPadreStrutturaAmministrativa" href="#collapseStrutturaAmministrativaContabileAttoAmministrativo"
								data-parent="#accordionStrutturaAmministrativaContabileAttoAmministrativo">
							<span id="SPAN_StrutturaAmministrativoContabileAttoAmministrativo">Seleziona la Struttura amministrativa</span>
						</a>
					</div>
					<div id="collapseStrutturaAmministrativaContabileAttoAmministrativo" class="accordion-body collapse">
						<div class="accordion-inner">
							<ul id="treeStruttAmmAttoAmministrativo" class="ztree treeStruttAmm"></ul>
						</div>
					</div>
				</div>
			</div>

			<s:hidden id="HIDDEN_StrutturaAmministrativoContabileAttoAmministrativoUid" name="strutturaAmministrativoContabile.uid" />
			<s:hidden id="HIDDEN_StrutturaAmministrativoContabileAttoAmministrativoCodice" name="strutturaAmministrativoContabileAttoAmministrativo.codice" />
			<s:hidden id="HIDDEN_StrutturaAmministrativoContabileAttoAmministrativoDescrizione" name="strutturaAmministrativoContabileAttoAmministrativo.descrizione" />
		</div>
	</div>
</fieldset>