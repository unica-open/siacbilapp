<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<%-- Inclusione head e CSS --%>
<s:include value="/jsp/include/head.jsp" />
</head>
<body>
	<%-- Inclusione header --%>
	<s:include value="/jsp/include/header.jsp" />

	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12 ">
				<div class="contentPage">
					<s:include value="/jsp/include/messaggi.jsp" />
					<h3>Consulta provvedimento</h3>
					<dl class="dl-horizontal">
						<dt>Anno</dt>
						<dd><s:property value="attoAmministrativo.anno"/>&nbsp;</dd>
						<dt>Numero</dt>
						<dd><s:property value="attoAmministrativo.numero"/>&nbsp;</dd>
						<dt>Tipo Atto</dt>
						<dd><s:property value="attoAmministrativo.tipoAtto.descrizione"/>&nbsp;</dd>
						<dt>Struttura</dt>
						<dd><s:property value="attoAmministrativo.strutturaAmmContabile.codice"/>-<s:property value="attoAmministrativo.strutturaAmmContabile.descrizione"/></dd>
						<dt>Oggetto</dt>
						<dd><s:property value="attoAmministrativo.oggetto"/>&nbsp;</dd>
						<dt>Stato</dt>
						<dd><s:property value="attoAmministrativo.statoOperativo"/>&nbsp;</dd>
						<!--SIAC 6929-->
						<dt>Blocco Rag.</dt>
						<dd>
							<s:if test="attoAmministrativo.bloccoRagioneria==null">N/A</s:if>
							<s:elseif test="attoAmministrativo.bloccoRagioneria==true">SI</s:elseif>
							<s:else>No</s:else>&nbsp;
						</dd>
						<dt>Ins. Manualmente</dt>
						<dd><s:if test="attoAmministrativo.provenienza==null">N/A</s:if>
							<s:elseif test="attoAmministrativo.provenienza.equalsIgnoreCase('MANUALE')">SI</s:elseif>
							<s:else>No</s:else>&nbsp;
						</dd>
						<!--SIAC 6929-->
						<dt>Note</dt>
						<dd><s:property value="attoAmministrativo.note"/>&nbsp;</dd>
						<dt>Parere di Regolarit&agrave; Contabile</dt>
						<dd><s:if test="attoAmministrativo.parereRegolaritaContabile">S&igrave;</s:if><s:else>No</s:else>&nbsp;</dd>
					</dl>
					<p>
						<s:include value="/jsp/include/indietro.jsp" />
					</p>
				</div>
			</div>
		</div>
	</div>
	
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
</body>
</html>