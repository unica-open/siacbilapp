<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<div aria-hidden="true" aria-labelledby="headerSelezioneCassaEconomale" role="dialog" tabindex="-1" class="modal hide fade" id="modaleSelezioneCassaEconomale">
	<div class="modal-header" id="headerSelezioneCassaEconomale">
		<h4>Seleziona la cassa economale su cui operare</h4>
	</div>
	<s:form action="cassaEconomaleStartSelectCassa" novalidate="novalidate">
		<div class="alert alert-error hide" id="erroriModaleSelezioneCassaEconomale">
			<button type="button" class="close" data-hide="alert">&times;</button>
			<strong>Attenzione!!</strong><br>
			<ul>
			</ul>
		</div>
		<div class="modal-body">
			<fieldset class="form-horizontal margin-medium">
				<div class="control-group margin-medium">
					<s:select list="listaCassaEconomale" cssClass="span12" headerKey="" headerValue="" name="cassaEconomale.uid" listKey="uid" listValue="descrizione"
						required="true" />
				</div>
				<br/>
				<br/>
			</fieldset>
		</div>
		<div class="modal-footer">
			<s:a cssClass="btn" action="redirectToCruscotto" id="pulsanteRedirezioneCruscotto">Indietro</s:a>
			<button type="submit" aria-hidden="true" class="btn btn-primary">conferma</button>
		</div>
	</s:form>
</div>