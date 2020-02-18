<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<h4>Modifica gli importi</h4>
<div id="nuovoDettaglioVariazioneImporti">
	<fieldset class="form-horizontal" id="fieldset_inserimentoVariazioneImporti">
		<div id="divComponentiInVariazioneNuovoDettaglio" class="hide">
			<h5>Componenti</h5>
			<s:include value="/jsp/variazione/include/fieldsetComponenti.jsp">
				<s:param name="suffix">NuovoDettaglio</s:param>
			</s:include>
		</div>
		
		<s:include value="/jsp/variazione/include/fieldsetStanziamenti.jsp">
			<s:param name="suffix">NuovoDettaglio</s:param>
		</s:include>
		<button type="button" class="btn pull-right" id="button_registraVariazioneNuovoDettaglio">inserisci modifiche</button>
	</fieldset>
</div>

