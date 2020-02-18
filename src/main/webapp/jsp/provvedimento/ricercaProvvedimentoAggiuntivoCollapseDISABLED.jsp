<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<div class="accordion margin-large" id="accordion5">
	<div class="accordion-group">
		<div class="accordion-heading">
			<a class="accordion-toggle collapsed" >
				<i class="icon-pencil icon-2x"></i>
				<span id="SPAN_InformazioniProvvedimentoAggiuntivo"> 
					<s:property value="stringaProvvedimentoAggiuntivo" />
				</span>
				<span class="icon"></span>
			</a>
		</div>
		<div id="collapseProvvedimentoAggiuntivo" class="accordion-body collapse">
			<div class="accordion-inner">
				<%-- Ricerca come dato aggiuntivo --%>
				<p>&Egrave; necessario inserire oltre all'anno almeno il numero atto oppure il tipo atto</p>
				<s:include value="/jsp/provvedimento/formRicercaProvvedimentoAggiuntivo.jsp" />
			</div>
		</div>
	</div>
</div>