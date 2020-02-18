<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<s:if test="%{previousCrumb == null && numeroPrecedentiAncore lte 2}">
	<s:url action="redirectToCruscotto" var="indietroURL" />
	<s:a cssClass="btn" href="%{indietroURL}" id="pulsanteRedirezioneIndietro">Indietro</s:a>
</s:if><s:elseif test="%{numeroPrecedentiAncore gt 2}">
	<s:url var="surl" action="%{previousAnchor.action}" namespace="%{previousAnchor.namespace}" includeContext="false"/>
	<c:url var="url" value="${surl}">
		<c:forEach var="p" items="${previousAnchor.params}">
			<c:forEach var="v" items="${p.value}">${v}
				<c:param name="${p.key}" value="${v}"/>
			</c:forEach>
		</c:forEach>
	</c:url>
	<s:a cssClass="btn" href="%{#attr['url']}" id="pulsanteRedirezioneIndietro">indietro</s:a>
</s:elseif><s:else>
	<s:url var="surl" action="%{previousCrumb.action}" namespace="%{previousCrumb.namespace}" includeContext="false"/>
	<c:url var="url" value="${surl}">
		<c:forEach var="p" items="${previousCrumb.params}">
			<c:forEach var="v" items="${p.value}">${v}
				<c:param name="${p.key}" value="${v}"/>
			</c:forEach>
		</c:forEach>
	</c:url>
	<s:a cssClass="btn" href="%{#attr['url']}" id="pulsanteRedirezioneIndietro">indietro</s:a>
</s:else>