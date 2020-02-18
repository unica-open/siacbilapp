<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<div class="accordion margin-large" id="accordion5">
	<div class="accordion-group">
		<div class="accordion-heading">
			<a class="accordion-toggle collapsed" data-toggle="collapse" data-parent="#accordion4" href="#collapseProvvedimento">
				<i class="icon-pencil icon-2x"></i>
				<span id="SPAN_InformazioniProvvedimento"> 
					<s:property value="stringaProvvedimento" />
				</span>
				<span class="icon"></span>
			</a>
		</div>
		<div id="collapseProvvedimento" class="accordion-body collapse">
			<div class="accordion-inner">
				<%-- Ricerca come dato aggiuntivo --%>
				<p>&Egrave; necessario inserire oltre all'anno almeno il numero atto oppure il tipo atto</p>
				<s:include value="/jsp/provvedimento/formRicercaProvvedimento.jsp" />
			</div>
		</div>
	</div>
</div>