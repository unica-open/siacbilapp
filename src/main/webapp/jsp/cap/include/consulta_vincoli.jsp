<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<div class="accordion" id="accordionVincoloPadre">
	<s:iterator value="listaVincoloCapitoli" var="vincoloCapitolo" status="status" >
		<div class="accordion-group">
			<div class="accordion-heading" id="accordionHeading${vincoloCapitolo.uid}">
				<a class="accordion-toggle collapsed" data-toggle="collapse" data-parent="#accordionVincoloPadre" href="#collapse${vincoloCapitolo.uid}" data-overlay data-vincolo="true"> 
					Vincolo <s:property value="%{#vincoloCapitolo.codice}"/><span class="icon">&nbsp;</span>
				</a>
				<input type="hidden" id="HIDDEN_collapse${vincoloCapitolo.uid}" value="${vincoloCapitolo.uid}"/>
			</div>
			<div id="collapse${vincoloCapitolo.uid}" class="accordion-body collapse">
				<div class="accordion-inner">
				</div>
			</div>
		</div>
	</s:iterator>
</div>